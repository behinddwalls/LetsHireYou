from pymongo import MongoClient
from job_seeker_engine import AggregateScore
import pandas as pd
from glob2 import glob
import os
import csv

"""
if os.path.exists('./job_seeker_engine_scores.csv'):
    os.remove('./job_seeker_engine_scores.csv')
else:
    pass
"""

client = MongoClient('localhost', 27017)
db = client['naukri']

f = AggregateScore()

for resume in glob('/home/shubham/Dropbox/ultra/high_scores/parsed/better_ones/*.xml'):
    data = []
    for job in db.pulledJobData.find({"jobRoleCategory" : "Analytics & BI"}):
        test_score = f.outputScore(resume, job)
        print test_score
        data.append({'resume': os.path.basename(glob(resume)[0]), 'company': job['jobComapnyName'], 'link': job['jobLinkToJobSourcePortal'] ,'score':test_score})

    df = pd.DataFrame(data) # .sort('score', ascending=False)

    with open('./job_seeker_engine_scores.csv', 'a') as csvfile:
        df.to_csv(csvfile, header=False)
