#!/usr/bin/python2.7
# -*- coding: utf-8 -*-

'''
author: pandeysp@
shubham <tomarshubham24@gmail.com>
'''

'''
Takes the input data as JSON and internally calls the 'ultra_algorithm_models.py'
and and returnd the outside (called) with the requierd output.
'''

from flask import json, jsonify
from ultra_algorithm_models import AggregateScore
from job_seeker_engine_models import AggregateJobScore 
import traceback

recruiter = AggregateScore()
seeker = AggregateJobScore()


'''
This gets the requierd 'EngineJobData' and 'Set<EngineJobSeekerData>'
from the JSON ('data') coming to the method parameter and
applies the requierd algorithm for calculation of 'perfect User'
who are best fit according to the JOBRequirements.
'''
def getRecommendedUsersForJob(data):
    output = {}
    match = {}
    print "Calculating the Data"
    applicant_data_set = data['engineUserDataSet']
    '''
    Since for the Symetricity We are using the same model while doing
    a Calculation of a single 'JOB' against SET<Users> or a single 'USER'
    against th SET<JobData>. Hence according to the caculation we are taking the
    first element from the JOB here.
    '''
    job_data = data['engineJobDataSet'][0]

    for applicant in applicant_data_set:
        try:
            score = recruiter.calculateScoreOfUsersAgainstJob(applicant,job_data)
            match[applicant['userId']] = score['total']
        except Exception as e:
            print e
            traceback.print_exc()
            match[applicant['userId']] = 0


    #Append the Calculated value to output 'JSON'
    #Match is Dictonery of {'jobId':percantageMatch} calculated by
    #Engine
    output['userIdToPercentageMap']  = match
    output['jobId'] = job_data['jobId']
    return jsonify(output)

'''
   This gets the requierd 'EngineJobData' and 'Set<EngineJobSeekerData>'
   from the JSON ('data') coming to the method parameter and
   applies the requierd algorithm for calculation of 'perfect User'
   who are best fit according to the JOBRequirements.
'''
def getRecommendedJobsForUser(data):
        output = {}
        match = {}
        '''
        Since for the Symetricity We are using the same model while doing
        a Calculation of a single 'JOB' against SET<Users> or a single 'USER'
        against th SET<JobData>. Hence according to the caculation we are taking the
        first element from the JOB here.
        '''
        applicant_data = data['engineUserDataSet'][0]
        job_data_set = data['engineJobDataSet']

        for job in job_data_set:
            try:
                score = seeker.calculateScoreOfJobsAgainstUser(applicant_data, job)
                match[job['jobId']] = score['total']
            except Exception as e:
                print str(e)
                traceback.print_exc()
                match[job['jobId']] = 0
        
        output['jobIdToPercentageMap'] = match
        output['userId'] = applicant_data['userId']
        return jsonify(output)
