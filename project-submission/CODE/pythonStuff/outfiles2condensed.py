import os
import shutil
#import re
import sys
import platform
import subprocess
import numpy as np
import json
import pickle
import pandas as pd
from pandas import Series
import xml.etree.ElementTree as ET
import glob
import argparse
try:
    import lvdb
except:
    import pdb as lvdb
    print('using pdb instead of lvdb')
    pass

def ensure_dir_exists (datadir):
    if not os.path.exists(datadir):
        os.makedirs(datadir)
    if not os.path.exists(datadir):
        themessage = 'Directory {} could not be created.'.format(datadir)
        if (int(platform.python_version()[0]) > 2):
            raise NotADirectoryError(themessage)
        else:
        # python 2 doesn't have the impressive exception vocabulary 3 does
        # so just raising a generic exception with a useful description
            raise BaseException(themessage)

def rsync_the_file (from_location, to_location):
    # Assuming that the responses for how platform.system() responds to
    # different OSes given here are correct (though not assuming case):
    # https://stackoverflow.com/questions/1854/python-what-os-am-i-running-on
    if platform.system().lower() is 'windows':
        print('Windows detected. The rsync command that is about to be', \
                'executed assumes a Linux or Mac OS; no guarantee that it', \
                'will work with Windows. Please be ready to transfer files', \
                'via alternate means if necessary.')
    subprocess.call(['rsync', '-vaPhz', from_location, to_location])

def df_to_pickle(thedf, thefilename):
    thedf.to_pickle(thefilename);
            
def df_to_csv(thedf, thefilename):
    thedf.to_csv(thefilename, index_label='index');

def df_to_json(thedf, thefilename):
    thedf.to_json(thefilename, orient='records', double_precision = 10, force_ascii = True);

def glob2df(datadir, linecount, jobnum_list):
    print(datadir)
    thepaths = glob.iglob(datadir + '/*/')

    results_dirs_used = []

    df_list = []
    progress_counter = 1000;
    counter = 0;

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

        if jobnum_list and jobnum not in jobnum_list:
            # lvdb.set_trace()
            # print('Job {} not in list of jobs; skipping'.format(jobnum))
            continue;

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
            results_dirs_used.append(dirname);

        # indexed_by_team_df = this_job_df.set_index(['team_id'])
        # df_list.append(indexed_by_team_df)
        df_list.append(this_job_df)

    df = pd.concat(df_list)

    print('df created for job ', jobnum)

    return df, results_dirs_used;


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
    parser = argparse.ArgumentParser(
            formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument('--path', help='Path to root directory containing' \
            ' output directories (dirs containing summary.csv and mission.xml)')
    parser.add_argument('--filename', help='Put generated CSV and/or JSON in' \
            ' file with this name (relative to path argument)' \
            ' (please DO NOT include extension)')
    parser.add_argument('-l', '--linecount', help='number of lines per case:' \
            ' if given, exclude cases if they have fewer than' \
            ' a certain number of lines', type=int, default=-1)
    parser.add_argument('-c', '--csv', help='generate csv file at' \
            ' path/filename.csv', action='store_true')
    parser.add_argument('-j', '--json', help='generate json file at' \
            ' path/filename.json', action='store_true')
    parser.add_argument('-r', '--repickle', help='If this is True, delete any' \
            'existing pickle of data (at path/filename.pickle) and generate a'
            ' new one. Choose this option if the data at path has changed.',
            action='store_true')
    parser.add_argument('-d', '--dest', help='Destination to which to rsync' \
            ' the aggregated file and pickle to (assumes ssh keys are set' \
            ' up). NOTE: This rsync method is not guaranteed to work on' \
            ' Windows!')
    parser.add_argument('--move_dirs', help='If this flag is set,' \
            ' results directories will be moved to same location as' \
            ' aggregated results.', action='store_true')
    parser.add_argument('--start_job', help='Starting job number')
    parser.add_argument('--end_job', help='Ending job number')
    parser.add_argument('--job_list', nargs='+', help='List of job numbers')
    args = parser.parse_args()

    movedirs = args.move_dirs
    repickle = args.repickle

    # If we want to move results directories anyway, we will need to run
    # glob2df to get directory list, so need to regenerate pickle rather
    # than reading from one that is already there that won't have all
    # the info we need
    if movedirs:
        repickle = True

    thedf = pd.DataFrame();

    extensionless_path = args.path + '/' + args.filename

    startjob = -1
    endjob = 0
    use_jobnums = False;

    joblist = [];

    if args.job_list:
        startjob=int(args.job_list[0])
        endjob=int(args.job_list[-1])
        joblist = args.job_list
        use_jobnums = True;
    elif args.start_job and args.end_job:
        startjob = int(args.start_job)
        endjob = int(args.end_job)
        use_jobnums = True;
        # adding 1 to endjob so range will include the end job in the list
        joblist_temp = range(startjob, endjob+1)
        if (int(platform.python_version()[0]) > 2):
            joblist = list(joblist_temp)
        else:
            joblist = joblist_temp

    jobdirname = args.path + '/jobs_all'
    if use_jobnums:
        jobdirname = args.path + '/jobs_' + str(startjob) + '_' + str(endjob)

    ensure_dir_exists(jobdirname)
    print('Aggregated data will be placed into {}'.format(jobdirname))

    extensionless_path = jobdirname + '/' + args.filename
    picklename = extensionless_path + '.pickle'

    linecount = args.linecount;

    made_pickle = False;
    made_json = False;
    made_csv = False;

    results_dirs_list = []

    # generate pickle if none exists or if repickle is set to true
    if not repickle:
        print('Looking for pickle file ', picklename)
        try:
            thedf = pd.read_pickle(picklename)
            print('Loaded pickle file ', picklename)
            made_pickle = True;
        except:
            print('No pickle found or error reading pickle. Generating new' \
            ' pickle.')
            thedf, results_dirs_list = glob2df(args.path, linecount, joblist)
            thedf.reset_index(inplace=True, drop=True)
            df_to_pickle(thedf, picklename);
            made_pickle = True;
    else:
        print('Regenerating pickle file at ', picklename)
        # argument explicitly given to regenerate the pickle file
        thedf, results_dirs_list = glob2df(args.path, linecount, joblist)
        thedf.reset_index(inplace=True, drop=True)
        df_to_pickle(thedf, picklename);
        made_pickle = True;



    # Generate json
    if args.json:
        jsonname = extensionless_path + '.json'
        df_to_json(thedf, jsonname);
        made_json = True;

    # generate csv
    if args.csv:
        csvname = extensionless_path + '.csv'
        df_to_csv(thedf, csvname);
        made_csv = True;

    if args.dest:
        # not sure why wildcards not working here; path + '.*' gives error
        if made_pickle:
            rsync_the_file(extensionless_path + '.pickle', args.dest)
        if made_csv:
            rsync_the_file(extensionless_path + '.csv', args.dest)
        if made_json:
            rsync_the_file(extensionless_path + '.json', args.dest)

    if movedirs:
        if results_dirs_list:
            if jobdirname is not None:
                for dirname in results_dirs_list:
                    shutil.move(dirname, jobdirname)
            else:
                print('No dest; results dir {} not moved'.format(dirname))



if __name__ == '__main__':
    sys.exit(main())



