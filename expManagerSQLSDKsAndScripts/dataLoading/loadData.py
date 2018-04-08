#!/usr/bin/env python3

import csv
import requests
import argparse
import json

#params
serverDomain = 'http://localhost:8080'
user = 'admin'
password = 'admin'

def loadCsv(csvFilePath):
    authRes = authenticate(user,password,serverDomain)
    with open(csvFilePath, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            print(row['index'], row['NonTeamCapture'], type(row['NonTeamCapture']))
            simulationRun = {
                'index': row['index'],
                'nonTeamCapture': int(float(row['NonTeamCapture'].strip() or 0))
            }
            authorizedPost(serverDomain, '/api/simulation-runs', {}, simulationRun, authRes)


#r4 = authorizedGet('http://localhost:8080', '/api/simulation-runs', {'page': 0, 'size': 20, 'sort':'id,asc'}, r3)
#r4.json()[0]['id']
def authorizedGet(serverDomain, path, args, authCall):
    dataCall = requests.get(serverDomain + path, params = args,
        cookies = authCall.cookies,
        headers = {'X-XSRF-TOKEN': authCall.cookies['XSRF-TOKEN']}
    )
    return dataCall

#r5 = authorizedPost('http://localhost:8080', '/api/simulation-runs', {}, {'vel_max_t_1': 0}, r3)
def authorizedPost(serverDomain, path, args, data, authCall):
    dataCall = requests.post(serverDomain + path, params = args,
        data = json.dumps(data),
        cookies = authCall.cookies,
        headers = {'X-XSRF-TOKEN': authCall.cookies['XSRF-TOKEN'], 'Content-Type': 'application/json'}
    )
    return dataCall

#r3 = authenticate('admin', 'admin', 'http://localhost:8080')
def authenticate(userName, password, serverDomain):
    initialCall = requests.get(serverDomain + '/#')
    authCall = requests.post(serverDomain + '/api/authentication',
        data = {'j_username': userName, 'j_password':password, 'submit':'Login'},
        cookies = initialCall.cookies,
        headers = {'X-XSRF-TOKEN': initialCall.cookies['XSRF-TOKEN']}
    )
    return authCall;

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("csvFile", help="The CSV file to load.")
    args = parser.parse_args()
    loadCsv(args.csvFile)

if __name__ == "__main__":
    main()
