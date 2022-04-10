#!flask/bin/python
# -*- coding: utf-8 -*-

'''
author: behinddwalls
'''

'''
IMPORTS:

pip install nltk
pip install scipy
pip install scikit-learn
pip install pandas
pip install fuzzywuzzy
pip install flask
pip install flask-httpauth

'''

from flask import Flask, jsonify, abort, request, make_response, url_for
from flask.ext.httpauth import HTTPBasicAuth
import process_output


app = Flask(__name__, static_url_path = "")
auth = HTTPBasicAuth()

username = 'job-portal-system'
password = 'job-portal-system123!@#'

@auth.get_password
def get_password(username):
    if username == username:
        return password
    return None

@auth.error_handler
def unauthorized():
    return make_response(jsonify( { 'error': 'Unauthorized access - 403' } ), 403)
    # return 403 instead of 401 to prevent browsers from displaying the default auth dialog

@app.errorhandler(400)
def bad_request(error):
    return make_response(jsonify( { 'error': 'Bad request' } ), 400)

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify( { 'error': 'Not found' } ), 404)

@app.errorhandler(500)
def internal_server_error(error):
    return make_response(jsonify( { 'error': 'Internal Server Error' } ), 500)

#This function Calls the Engince API for getting the 'Preferred JobSeekers' for 'JOB'.
@app.route('/engine/api/v1.0/compute/computeUsersByJob', methods = ['POST'])
def computeUsersByJob():

    print "##  Inside the computeUsersByJob  of Engine to compute the Users for JOBS."

    json = request.get_json(force=True)
    print json

    print "Calling Engine API for calculation of Preferred Users for JOB"
    #calling process_output.py module to process result.
    engineJsonresponse = process_output.getRecommendedUsersForJob(json)
    print engineJsonresponse
    return engineJsonresponse


#This function Calls the Engince API for getting the 'PreferredJobs' for 'JOBSeeker'.
@app.route('/engine/api/v1.0/compute/computeJobsByUser', methods = ['POST'])
def computeJobsByUser():
    print "##  Inside the computeJobsByUser of Engine to compute the jobs for User."

    json = request.get_json(force=True)
    print json

    print "Calling Engine API for calculation of Preferred Users for JOB"
    #calling process_output.py module to process result.
    engineJsonresponse = process_output.getRecommendedJobsForUser(json)
    print engineJsonresponse
    return engineJsonresponse

@app.route("/")
def hello():
    return "Hello world!"


if __name__ == '__main__':
    app.run(debug = True)
