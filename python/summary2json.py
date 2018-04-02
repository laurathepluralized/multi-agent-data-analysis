import os
#import re
import sys
import numpy as np
import pandas as pd
from pandas import Series
import xml.etree.ElementTree as ET
import glob
import argparse
import lvdb

def ensure_dir_exists (datadir):
    dir = os.path.dirname(datadir)
    if not os.path.exists(dir):
        os.makedirs(dir)
            

def df_to_json(thedf, thefilename):
    thedf.reset_index(inplace=True)
    thedf.to_json(thefilename, orient='index');

def glob2df(datadir, already_moved):
    print(datadir)
    summary_paths = datadir + '/20*/summary.csv'
    mission_paths = datadir + '/20*/mission.xml'
    # Concatenation procedure from:
    # https://stackoverflow.com/questions/30335474/merging-multiple-csv-files-without-headers-being-repeated-using-python/30336243#30336243
    data_files = glob.glob(summary_paths)
    mission_files = glob.glob(mission_paths)
    df_list = []

    for filename, missionname in zip(sorted(data_files), sorted(mission_files)):
        jobnumpart = filename.split('_job_',1)[1]
        jobnum = jobnumpart.split('_task',1)[0]

        thisjob_df = pd.read_csv(filename)
        # Add column to df for job number
        thisjob_df['job_num']=jobnum
        thisjob_params_df = xml_param_df_cols(missionname);
        num_lines = len(thisjob_df.index)
        df_to_append = pd.concat([thisjob_params_df]*num_lines, ignore_index=True);
        if (df_to_append.empty) or (len(df_to_append) < 2):
            lvdb.set_trace()

        # df_to_append['job_num']=jobnum

        this_job_df = pd.concat([thisjob_df, df_to_append], axis=1);

        df_list.append(this_job_df)

    df = pd.concat(df_list)

    print(datadir)

    if already_moved:
        thedir = datadir
    else:
        thedir = datadir# + '/job_' + job_num

    ensure_dir_exists(thedir)
    return df;


def append_block(theblock, block_name_string, none_type_var):
    thedf = pd.DataFrame()

    if type(theblock) is not type(none_type_var):
        thedict = {}
        try:
            thedict = theblock.attrib
        except:
            pass

        if thedict:
            try:
                thedict[block_name_string] = theblock.text
            except:
                print('block has no name:', block_name_string)
                pass;

            thedf = thedf.from_records([thedict])
    return thedf;

def xml_param_df_cols(mission_file_name):
    # Borrowing this xml parsing and looping snippet from
    # scrimmage/scripts/generate_scenarios.py (GPL3 license)

    tree = ET.parse(mission_file_name)
    root = tree.getroot()

    team_keys = []
    list_df_by_team = []
    big_df_params = pd.DataFrame()

    # there is probably a nicer way to do this, but this gets the right type
    notatag = tree.find('this_isnt_a_tag')
    nonetype = type(notatag)

    # Find and loop over all "entity" tags in mission file.
    entity_num = 0
    key_idx = -1;
    for child in root:
        # This is still a work in progress.
        # Want to get all of the parameters from the various plugins,
        # add the tag name, plugin name, and team number (where applicable) to
        # a Pandas DF column name, and then set the value for that column to be
        # what the param was during simulation as defined by the mission file.
        if child.tag != "entity_common" and child.tag != "entity":
            continue

        df_list_of_attr = []

        if child.tag == "entity_common" or child.tag == "entity":
            ent_common_type = None
            if child.tag == "entity_common":
                ent_common_type = child.attrib['name']
            elif child.tag == "entity":
                try:
                    ent_common_type = child.attrib['entity_common']
                except KeyError:
                    ent_common_type = None

            theteam = '0'
            theteamtag = child.find('team_id')
            if type(theteamtag) != nonetype:
                # we have a team_id tag
                theteam = theteamtag.text

            teamint = int(theteam)

            entdf = pd.DataFrame
            if (theteam in team_keys):
                team_keys.append(theteam + 'a')
            else:
                team_keys.append(theteam)
            key_idx += 1;

            toappend = ''
            if (teamint != 0):
                toappend += '~t~' + team_keys[key_idx]
            if ent_common_type is not None:
                try:
                    toappend += '~' + ent_common_type
                except:
                    lvdb.set_trace()

            aut_block = child.find('autonomy')
            motion_block = child.find('motion_model')
            controller_block = child.find('controller')
            sensor_block = child.find('sensor')

            autdf = append_block(aut_block, 'autonomy', nonetype)
            if (entdf.empty):
                entdf = autdf
            else:
                entdf = entdf.join(autdf)

            motiondf = append_block(motion_block, 'motion_model', nonetype)
            if (entdf.empty):
                entdf = motiondf
            else:
                entdf = entdf.join(motiondf)

            controllerdf = append_block(controller_block, 'controller', nonetype)
            if (entdf.empty):
                entdf = controllerdf
            else:
                entdf = entdf.join(controllerdf)

            sensordf = append_block(sensor_block, 'sensor', nonetype)
            if (entdf.empty):
                entdf = sensordf
            else:
                entdf = entdf.join(sensordf)

            if (not entdf.empty):
                collist = entdf.columns
                colmap = {}
                for col in collist:
                    colmap[col] = col + toappend
                entdf.rename(index=str, columns=colmap, inplace=True)


        if (big_df_params.empty):
            big_df_params = entdf
        else:
            big_df_params = big_df_params.join(entdf)

    # Return a df of all modified attributes of all teams, with team number 
    # suffixes and/or entity_common attributes in column names
    return big_df_params;





def main ():
    parser = argparse.ArgumentParser()
    parser.add_argument('--path', help='Path to root directory containing output directories (dirs containing summary.csv)')
    parser.add_argument('--put_in_given_dir', help='Put generated csv in given directory = true, put generated csv in job_# directory within given directory = false')
    parser.add_argument('--filename', help='Put generated JSON in file with this name (relative to path argument)(please include extension)')
    args = parser.parse_args()

    thedf = glob2df(args.path, args.put_in_given_dir)
    df_to_json(thedf, args.path + '/' + args.filename);





if __name__ == '__main__':
    sys.exit(main())



