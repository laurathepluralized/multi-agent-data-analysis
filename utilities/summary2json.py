import os
import numpy as np
import pandas as pd
from pandas import Series
import glob
import argparse

def ensure_dir_exists (filepath):
    dir = os.path.dirname(filepath)
    if not os.path.exists(dir):
        os.makedirs(dir)

def glob2df (filepath, job_num, already_moved):
    globbable_path = filepath + '/20*_job_' + job_num + '_*/summary.csv'
    # Concatenation procedure from:
    # https://stackoverflow.com/questions/30335474/merging-multiple-csv-files-without-headers-being-repeated-using-python/30336243#30336243
    data_files = glob.glob(globbable_path)
    df_list = []

    for filename in sorted(data_files):
        df_list.append(pd.read_csv(filename))

    df = pd.concat(df_list)

    print(filepath)

    if already_moved:
        thedir = filepath
    else:
        thedir = filepath + '/job_' + job_num

    ensure_dir_exists(thedir)
    print(thedir)
    thefilename = thedir + args.filename
    print(thefilename)
    df.to_json(thefilename, orient='index')
    print('Created JSON file at:')
    print(thefilename)
    return df


parser = argparse.ArgumentParser()
parser.add_argument('--path', help='Path to root directory containing output directories (dirs containing summary.csv)')
parser.add_argument('--job', help='Relevant job number')
parser.add_argument('--put_in_given_dir', help='Put generated csv in given directory = true, put generated csv in job_# directory within given directory = false')
parser.add_argument('-j', '--filename', help='Desired JSON file name (please include extension)')
args = parser.parse_args()

thedf = glob2df(args.path, args.job, args.put_in_given_dir)
