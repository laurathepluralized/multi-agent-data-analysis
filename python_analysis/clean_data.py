# -*- coding: utf-8 -*-
"""
Created on Sun Apr  8 13:17:23 2018

@author: gtcbh
"""
import pandas as pd

# Data file to read (relative path w/o the .csv)
filename = "../../alldata"
df = pd.read_csv(filename + ".csv", index_col=0)

#  Non important columns
df.drop(columns=[
        "autonomy~t~1",
        "autonomy~t~2~predator",
        "autonomy~t~3~predator",
        "motion_model~predator",
        "motion_model~t~1",
        "results_dir",
        "entity_count",
        "flight_time",
        "flight_time_norm",
        "non_team_coll",
        "team_coll",
        "ground_coll",
        #Score is a duplicate of NonTeamCaptures, our output metric
        "score",
        #Other things to remove
        "max_pred_speed~t~1",
        "align_weight~t~2~predator",
        "avoid_nonteam_weight~t~2~predator",
        "avoid_team_weight~t~2~predator",
        "centroid_weight~t~2~predator",
        "comms_range~t~2~predator",
        "max_speed~t~2~predator",
        "align_weight~t~3~predator",
        "avoid_nonteam_weight~t~3~predator",
        "avoid_team_weight~t~3~predator",
        "centroid_weight~t~3~predator",
        "comms_range~t~3~predator",
        "max_speed~t~3~predator"
        ],
        inplace=True)
df = df.drop([0, 1]).reset_index(drop=True)
columns = df.columns
for col in columns:
    newcol = col.replace("~", "_")
    df.rename(columns={col: newcol}, inplace=True)
    
    
#Theoretically this data is more ready now
df = df[pd.notnull(df["NonTeamCapture"])]

#File to write to
df.to_csv(filename + "_clean.csv", index=0)

