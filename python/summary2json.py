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

def glob2json (datadir, already_moved):
    summary_paths = datadir + '/20*/summary.csv'
    mission_paths = datadir + '/20*/mission.xml'
    # Concatenation procedure from:
    # https://stackoverflow.com/questions/30335474/merging-multiple-csv-files-without-headers-being-repeated-using-python/30336243#30336243
    data_files = glob.glob(summary_paths)
    mission_files = glob.glob(mission_paths)
    df_list = []

    for filename, missionname in zip(sorted(data_files), sorted(mission_files)):
        jobnumpart = filename[26:]
        jobnum = jobnumpart.split('_task',1)[0]

        thisjob_df = pd.read_csv(filename)
        # Add column to df for job number
        thisjob_df['job_num']=jobnum
        thisjob_params_df = xml_param_df_cols(missionname);
        this_job_df = thisjob_df.join(thisjob_params_df);

        lvdb.set_trace()

        df_list.append(this_job_df)

    df = pd.concat(df_list)

    print(datadir)

    if already_moved:
        thedir = datadir
    else:
        thedir = datadir + '/job_' + job_num

    ensure_dir_exists(thedir)
    print(thedir)
    thefilename = thedir + args.filename
    print(thefilename)
    return df.to_json(thefilename, orient='index')


def xml_param_df_cols(mission_file_name):
    # Borrowing this xml parsing and looping snippet from
    # scrimmage/scripts/generate_scenarios.py (GPL3 license)

    tree = ET.parse(mission_file_name)
    root = tree.getroot()

    team_keys = []
    list_df_by_team = []

    # Find and loop over all "entity" tags in mission file.
    entity_num = 0
    for child in root:
        # This is still a work in progress.
        # Want to get all of the parameters from the various plugins,
        # add the tag name, plugin name, and team number (where applicable) to
        # a Pandas DF column name, and then set the value for that column to be
        # what the param was during simulation as defined by the mission file.
        if child.tag != "entity":
                # child.tag != "entity_common":
                # child.tag != "entity_interaction" \
                # child.tag != "entity_interaction" \
                # child.tag != "sensor" \
                # child.tag != "motion" \
                # child.tag != "controller" \
                # child.tag != "metrics":
            continue

        dummydf = pd.DataFrame
        notatag = child.find('this_isnt_a_tag')
        theteam = 0
        df_list_of_attr = []
        if child.tag == "entity":
            theteam = child.find('team_id').text
            entdf = pd.DataFrame
            team_keys.append(theteam)
            aut_block = child.find('autonomy')
            motion_block = child.find('motion_model')
            controller_block = child.find('controller')
            sensor_block = child.find('sensor')

            # TODO: make function instead of having same block of code over and 
            # over
            if type(aut_block) is not type(notatag):
                aut_dict = aut_block.attrib
                autdf = dummydf.from_records([aut_dict])
                autdf['autonomy'] = child.find('autonomy').text
                if (entdf.empty):
                    entdf = autdf
                else:
                    entdf = entdf.join(autdf)

            lvdb.set_trace()

            if type(motion_block) is not type(notatag):
                motion_dict = motion_block.attrib
                motiondf = dummydf.from_records([motion_dict])
                motiondf['motion_model'] = child.find('motion_model').text
                if (entdf.empty):
                    entdf = motiondf
                else:
                    entdf = entdf.join(motiondf)

            lvdb.set_trace()

            if type(controller_block) is not type(notatag):
                controller_dict = controller_block.attrib
                controllerdf = dummydf.from_records([controller_dict])
                controllerdf['controller'] = child.find('controller').text
                if (entdf.empty):
                    entdf = controllerdf
                else:
                    entdf = entdf.join(controllerdf)

            lvdb.set_trace()

            if type(sensor_block) is not type(notatag):
                sensor_dict = sensor_block.attrib
                sensordf = dummydf.from_records([sensor_dict])
                sensordf['sensor'] = child.find('sensor').text
                if (entdf.empty):
                    entdf = sensordf
                else:
                    entdf = entdf.join(sensordf)

            lvdb.set_trace()

            # if (theteam != 0):
            #     entdf.columns = [str(col) + '_' + str(theteam) for col in entdf.columns]

        list_df_by_team.append(entdf)

    # Return a df of all modified attributes of all teams, with team number 
    # suffixes in column names and with team numbers as keys
    finaldf = pd.concat(list_df_by_team, keys=team_keys);
    lvdb.set_trace()
    return finaldf;





def main ():
    parser = argparse.ArgumentParser()
    parser.add_argument('--path', help='Path to root directory containing output directories (dirs containing summary.csv)')
    parser.add_argument('--put_in_given_dir', help='Put generated csv in given directory = true, put generated csv in job_# directory within given directory = false')
    args = parser.parse_args()

    thedf = glob2json(args.path, args.put_in_given_dir)
    print(thedf)





if __name__ == '__main__':
    sys.exit(main())



