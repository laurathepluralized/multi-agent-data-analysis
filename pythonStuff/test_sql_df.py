#!/usr/bin/env python3

# This script brought to you by this stackoverflow post, which got mysql working
# on Ubuntu:
# https://stackoverflow.com/questions/39281594/error-1698-28000-access-denied-for-user-rootlocalhost
import sys
from sqlalchemy import create_engine
import pandas as pd


def main ():
    df = pd.read_csv('repeated_params/cse_repeated_runs_clean.csv')

    eng = create_engine('mysql://username@localhost/sqltest', echo=False)
    df.to_sql(name='repeated_runs', con=eng, if_exists='append')


    df = pd.read_csv('all_cleaner_clean.csv')

    eng = create_engine('mysql://username@localhost/sqltest', echo=False)
    df.to_sql(name='lhs_data', con=eng, if_exists='append')



if __name__ == '__main__':
    sys.exit(main())
