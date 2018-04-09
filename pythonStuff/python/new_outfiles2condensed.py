import os
#import re
import sys
import numpy as np
import json
import pickle
import pandas as pd
from pandas import Series
import xml.etree.ElementTree as ET
import glob
import argparse
import pdb

def ensure_dir_exists (datadir):
    dir = os.path.dirname(datadir)
    if not os.path.exists(dir):
        os.makedirs(dir)

def df_to_pickle(thedf, thefilename):
    thedf.to_pickle(thefilename);
            
def df_to_csv(thedf, thefilename):
    thedf.to_csv(thefilename, index_label='index');

def df_to_json(thedf, thefilename):
    thedf.to_json(thefilename, orient='records', double_precision = 10, force_ascii = True);

def glob2df(datadir, linecount):
    print(datadir)
    thepaths = glob.iglob(datadir + '/*/')

    df_list = []
    progress_counter = 1000;
    counter = 0;
    # for filename, missionname in zip(sorted(data_files), sorted(mission_files)):
    for dirname in sorted(thepaths):
        dirstructure = dirname.split('/')
        lastdir = dirstructure[-1]
        if '_job_' not in lastdir:
            # handle trailing slash if present
            lastdir = dirstructure[-2];

        if '_job_' not in lastdir:
            # something's wrong; skip this case
            continue;

        if '_task_' not in lastdir:
            # something's wrong; skip this case
            continue;

        if 'latest' in lastdir:
            continue;

        filename = dirname + 'summary.csv'
        if not os.path.isfile(filename):
            print('No summary file at ', filename);
            # no summary file means no results, unless results saved using a 
            # different mechanism, which is out of scope of this script
            continue;

        missionname = dirname + 'mission.xml'
        if not os.path.isfile(missionname):
            print('No mission file at ', missionname);
            continue;


        split_on_task = lastdir.split('_task_')
        tasknum = int(split_on_task[-1])

        jobnum = int(split_on_task[0].split('_job_',1)[1])

        counter += 1;
        if counter > progress_counter:
            print('j ', jobnum, ', t ', tasknum)
            counter = 0;

        # thisjob_df = pd.DataFrame(index=range(1))
        thisjob_df = pd.read_csv(filename)

        if thisjob_df.empty:
            # no actual content in df; maybe only header rows
            continue;

        # Add column to df for job number
        thisjob_df['job_num']=jobnum
        # and task number
        thisjob_df['task_num']=tasknum
        # and results directory
        thisjob_df['results_dir']=lastdir
        # add how many rows there are in the df so plot scripts know what to 
        # expect
        thisjob_df['num_rows']=len(thisjob_df.index)

        df_to_append = pd.DataFrame()

        thisjob_params_df = xml_param_df_cols(missionname);
        num_lines = len(thisjob_df.index)
        if linecount > 0:
            if num_lines < linecount:
                continue;
        df_to_append = pd.concat([thisjob_params_df]*num_lines, ignore_index=True);

        if df_to_append.empty:
            continue;

        this_job_df = thisjob_df
        if not df_to_append.empty:
            this_job_df = pd.concat([thisjob_df, df_to_append], axis=1);

        # indexed_by_team_df = this_job_df.set_index(['team_id'])
        # df_list.append(indexed_by_team_df)
        df_list.append(this_job_df)

    df = pd.concat(df_list)

    print('df created for job ', jobnum)

    return df;


def append_block(theblock, blk_name, nonetype_var):
    thedf = pd.DataFrame()

    if type(theblock) is not type(nonetype_var):
        thedict = {}
        try:
            thedict = theblock.attrib
        except:
            pass

        if thedict:
            try:
                thedict[blk_name] = theblock.text
            except:
                print('block has no name:', blk_name)
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
        # if child.tag != 'entity_common' and child.tag != 'entity':
        #     continue

        if child.tag == "entity_common" or child.tag == "entity":
            ent_common_type = None
            if child.tag == 'entity_common':
                ent_common_type = child.attrib['name']
            elif child.tag == 'entity':
                try:
                    ent_common_type = child.attrib['entity_common']
                except KeyError:
                    ent_common_type = None
                    pass;

            theteam = '0'
            theteamtag = child.find('team_id')
            if type(theteamtag) != nonetype:
                # we have a team_id tag
                theteam = theteamtag.text

            teamint = int(theteam)

            entdf = pd.DataFrame()
            toappend = ''
            if (teamint != 0):
                if (theteam in team_keys):
                    team_keys.append(theteam + 'a')
                else:
                    team_keys.append(theteam)
                key_idx += 1;
                toappend += '_t_' + team_keys[key_idx]

            if ent_common_type is not None:
                try:
                    toappend += '_' + ent_common_type
                except:
                    pdb.set_trace()

            aut_block = child.find('autonomy')
            motion_block = child.find('motion_model')
            controller_block = child.find('controller')
            sensor_block = child.find('sensor')

            autdf = append_block(aut_block, 'autonomy', nonetype)
            if (entdf.empty):
                entdf = autdf
            else:
                entdf = entdf.join(autdf, sort=False)

            motiondf = append_block(motion_block, 'motion_model', nonetype)
            if (entdf.empty):
                entdf = motiondf
            else:
                entdf = entdf.join(motiondf, sort=False)

            controllerdf = append_block(controller_block, 'controller', nonetype)
            if (entdf.empty):
                entdf = controllerdf
            else:
                entdf = entdf.join(controllerdf, sort=False)

            sensordf = append_block(sensor_block, 'sensor', nonetype)
            if (entdf.empty):
                entdf = sensordf
            else:
                entdf = entdf.join(sensordf, sort=False)

            if (not entdf.empty):
                collist = entdf.columns
                colmap = {}
                for col in collist:
                    colmap[col] = col + toappend
                entdf.rename(index=str, columns=colmap, inplace=True)

            if (big_df_params.empty):
                big_df_params = entdf
            else:
                big_df_params = big_df_params.join(entdf, sort=False)

    # Return a df of all modified attributes of all teams, with team number 
    # suffixes and/or entity_common attributes in column names
    if big_df_params.empty:
        pdb.set_trace();
    return big_df_params;


def main ():
    parser = argparse.ArgumentParser()
    parser.add_argument('--path', help='Path to root directory containing' \
            ' output directories (dirs containing summary.csv and mission.xml)')
    parser.add_argument('--filename', help='Put generated CSV and/or JSON in' \
            ' file with this name (relative to path argument)' \
            ' (please DO NOT include extension)')
    parser.add_argument('-l', '--linecount', help='number of lines per case:' \
            ' if not given, do not exclude cases if they have fewer than' \
            ' a certain number of lines', type=int, default=-1)
    parser.add_argument('-c', '--csv', help='generate csv file at' \
            ' path/filename.csv', action='store_true')
    parser.add_argument('-j', '--json', help='generate json file at' \
            ' path/filename.json', action='store_true')
    parser.add_argument('-r', '--repickle', help='If this is True, delete any' \
            'existing pickle of data (at path/filename.pickle) and generate a'
            ' new one. Choose this option if the data at path has changed.',
            action='store_true')
    args = parser.parse_args()

    thedf = pd.DataFrame();

    picklename = args.path + '/' + args.filename + '.pickle'

    linecount = args.linecount;

    # generate pickle if none exists or if args.repickle is set to true
    if not args.repickle:
        print('Looking for pickle file ', picklename)
        try:
            thedf = pd.read_pickle(picklename)
            print('Loaded pickle file ', picklename)
        except:
            print('No pickle found or error reading pickle. Generating new' \
            ' pickle.')
            thedf = glob2df(args.path, linecount)
            thedf.reset_index(inplace=True, drop=True)
            df_to_pickle(thedf, picklename);
    else:
        print('Regenerating pickle file at ', picklename)
        # argument explicitly given to regenerate the pickle file
        thedf = glob2df(args.path, linecount)
        thedf.reset_index(inplace=True, drop=True)
        df_to_pickle(thedf, picklename);

    # Generate json
    if args.json:
        jsonname = args.path + '/' + args.filename + '.json'
        df_to_json(thedf, jsonname);

    # generate csv
    if args.csv:
        csvname = args.path + '/' + args.filename + '.csv'
        df_to_csv(thedf, csvname);

if __name__ == '__main__':
    sys.exit(main())



