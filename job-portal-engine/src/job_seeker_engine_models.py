#!/usr/bin/env python
# -*- coding: utf-8 -*-

'''
author: Shubham Singh Tomar <tomarshubham24@gmail.com>
'''

from __future__ import division
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from nltk.tokenize import RegexpTokenizer
import nltk.collocations as cl
from nltk.stem.lancaster import LancasterStemmer
from fuzzywuzzy import fuzz
import os


class AggregateJobScore():
    def __init__(self):
        self.stopWords = {'i', 'me', 'my', 'myself', 'we', 'our', 'ours', 'ourselves', 'you', 'your', 'yours', 'yourself',
                    'yourselves', 'he', 'him', 'his', 'himself', 'she', 'her', 'hers', 'herself', 'it', 'its', 'itself',
                    'they', 'them', 'their', 'theirs', 'themselves', 'what', 'which', 'who', 'whom', 'this', 'that', 'these',
                    'those', 'am', 'is', 'are', 'was', 'were', 'be', 'been', 'being', 'have', 'has', 'had', 'having', 'do',
                    'does', 'did', 'doing', 'a', 'an', 'the', 'and', 'but', 'if', 'or', 'because', 'as', 'until', 'while',
                    'of', 'at', 'by', 'for', 'with', 'about', 'against', 'between', 'into', 'through', 'during', 'before',
                    'after', 'above', 'below', 'to', 'from', 'up', 'down', 'in', 'out', 'on', 'off', 'over', 'under', 'again',
                    'further', 'then', 'once', 'here', 'there', 'when', 'where', 'why', 'how', 'all', 'any', 'both', 'each',
                    'few', 'more', 'most', 'other', 'some', 'such', 'no', 'nor', 'not', 'only', 'own', 'same', 'so', 'than',
                    'too', 'very', 's', 't', 'can', 'will', 'just', 'don', 'should', 'now'}

        self.weights = {'job_score': 1.5, 'titles': 0.5, 'tenure':0.25, 'skills':0.20, 'education':0.2}

        directoryPath = os.path.dirname(os.path.abspath(__file__))

        company = pd.read_csv(directoryPath + '/Data/glassdoor_ratings.csv')
        self.company = company


    def getApplicantData(self, resume):
        """
        Pulls the applicant data from an XML file. The input should be a string of the file name.
        It collects listed skills, job experience, past job titles, and education. It also cleans the text
        and splits experience and titles into bigrams and trigrams and returns the data as a dictionary.
        """
        bigram_measures = cl.BigramAssocMeasures()
        trigram_measures = cl.TrigramAssocMeasures()
        st = LancasterStemmer()
        tk = RegexpTokenizer(r'\w+')
        total_exp = 0.

        user_id = resume['userId']
        education = resume['userEducationList']
        experience = resume['userExperianceList']
        skills = resume['skills']
        work_hist_skills = resume['skillsFoundInWorkHistory']


        #Convert past job descriptions into bigrams and trigrams to compare to target job description
        bigrams, trigrams = [], []
        try:
            jobs_from_resume = [job['workDescription'] for job in experience]
        except Exception:
            jobs_from_file = []


        for job in jobs_from_resume:
            if job != None:
                biTri_temp = self.getBiTrigrams(job)
                bigrams.append(biTri_temp[0])
                trigrams.append(biTri_temp[0])

        #Convert past job titles into bigrams and trigrams and get years of experience in respective roles
        #experience_yrs = [] - I dont think it's being used anywhere
        raw_titles = []
        try:
            job_titles_from_resume = [job['jobWorkTitle'] for job in experience]
        except Exception:
            job_titles_from_resume = []

        position_yrs = []
        try:
            tenure_in_resume = [job['workTenureInCompany']/12 for job in experience]
        except Exception:
            tenure_in_resume = [1]
        for yrs_exp in tenure_in_resume:
            position_yrs.append(yrs_exp)

        try:
            company_name = [job['companyName'] for job in experience]
        except Exception:
            company_name = []

        #Get education data: institute tier and type.
        try:
            institute_name = [degree['instituteName'] for degree in education]
        except Exception:
            institute_name = []

        try:
            degreeType = [degree['educationType'] for degree in education]
        except Exception:
            degreeType = []

        emp_data = {'user_id': user_id ,'skills':skills, 'work_hist_skills': work_hist_skills,'job_bigrams':bigrams,
                    'job_trigrams':trigrams, 'titles': job_titles_from_resume, 'years_exp':position_yrs, 'company_name': company_name,
                    'position_tenure':position_yrs, 'institute_name': institute_name, 'degreeType': degreeType}

        return emp_data


    '''
    Converts the Job model coming form the request in the appropriate JOB model expected by
    ENGINE for processing it against the User data.
    '''
    def getJobDescriptionData(self, job):
        jobId = job['jobId']
        title = job['jobTitle'].lower()
        description = job['jobDescription']
        skills = job['jobSkills']
        company_name = job['organisationName']

        job_bitri = self.getBiTrigrams(description)

        return {'jobId': jobId, 'title': title,
                'description': description, 'skills': skills,
                'company_name': company_name, 'job_bitri': job_bitri[1]}


    def getBiTrigrams(self, text):
        """
        Take an input string 'text', convert to lowercase and utf-8, remove stop words,
        stem the words, and finally output all bigrams and trigrams in the text.
        """
        bigram_measures = cl.BigramAssocMeasures()
        trigram_measures = cl.TrigramAssocMeasures()
        tk = RegexpTokenizer(r'\w+')

        st = LancasterStemmer()

        text = tk.tokenize(text.lower())
        job_vec = [st.stem(word) for word in text if word not in self.stopWords]

        bigrams = []
        trigrams = []
        collocations = cl.BigramCollocationFinder.from_words(job_vec)
        tri_collocations = cl.TrigramCollocationFinder.from_words(job_vec)
        top10 = collocations.score_ngrams(bigram_measures.raw_freq)
        top10 = sorted(bigram for bigram,score in top10)
        tri_top10 = tri_collocations.score_ngrams(bigram_measures.raw_freq)
        tri_top10 = sorted(trigram for trigram,score in tri_top10)
        for coll in top10:
            bigrams.append(coll[0] + ' ' + coll[1])
        for tri_coll in tri_top10:
            trigrams.append(tri_coll[0] + ' ' + tri_coll[1] + ' ' + tri_coll[2])

        return bigrams, trigrams


    def scoreJobDescription(self, employee_hist, job_desc):
        """
        Computes the similarity between job description and each job mentioned in resume. Outputs a score normalized to a scale [0,1].
        """

        document = ()
        for job in employee_hist:
            document += (' '.join(job),)

        document += (' '.join(job_desc),)

        tfidf_vectorizer = TfidfVectorizer()
        tfidf_matrix = tfidf_vectorizer.fit_transform(document)
        cos_sim = cosine_similarity(tfidf_matrix[len(employee_hist):len(employee_hist)+1], tfidf_matrix).tolist().pop()[:-1]
        return cos_sim


    def scoreJobTitle(self, past_titles, target_title):

        match_score = []
        for title in past_titles:
            match_score.append(fuzz.ratio(str(title).lower(), str(target_title).lower()))
        try:
            score = max(match_score)
        except:
            score = 0
        return score/100


    def scoreSkills(self, skills, work_hist_skills, req_skills):
        """
        Computes the no. of common skills in resume and JD.
        """

        if work_hist_skills:
            score = len(set(work_hist_skills).intersection(req_skills))
        else:
            score = len(set(skills).intersection(req_skills))

        req_skills_len = len(req_skills)

        return score/req_skills_len if req_skills_len != 0 else 0


    def scoreCompanyMovement(self, companies, job_company):
        """
        Return the difference between Glassdoor's company ratings.
        """
        company_list = self.company
        tier = []
        business_type = ['inc', 'solutions', 'pvt.', 'pvt', 'private', 'ltd', 'limited', 'business']

        companies = [' '.join(w for w in company.split() if w.lower() not in business_type)
                     for company in companies]

        try:
            last_company = companies[0]
        except IndexError as e:
            print str(e)
            pass

        match_score = []
        for company_name in company_list['name']:
            try:
                match_score.append(fuzz.ratio(str(company_name), str(last_company)))
            except:
                print str(e)
                match_score.append(0)

        if max(match_score) > 70:
            last_company_tier = company_list['overallRating'][match_score.index(max(match_score))]
        else:
            last_company_tier = 2


        job_match_score = []
        for company_name in company_list['name']:
            try:
                job_match_score.append(fuzz.ratio(str(company_name), str(job_company)))
            except KeyError, e:
                job_match_score.append(0)

        if max(job_match_score) > 70:
            job_company_tier = company_list['overallRating'][job_match_score.index(max(job_match_score))]
        else:
            job_company_tier = 2

        company_movement = job_company_tier - last_company_tier

        if job_company_tier >= 1.5*last_company_tier:
            score = 0.5
        elif 0.95*job_company_tier <= last_company_tier < 1.5*job_company_tier:
            score = 0
        elif 0.95*last_company_tier >= job_company_tier:
            score = -0.2
        elif job_company_tier >= last_company_tier:
            score = 0.5

        return score

    '''
    This function returns the [ {'userId' : '1' : 'total':50} ]
    Here 'total' is the score given particular 'USERID' by ENGINE after calculating the
    Users different attributes (infprmation) against the JOB info.
    TODO - Need to check this function latter. Currently it's not being used anywhere and need to check
    whether it's calculating the scores accuratlty or not.
    '''
    def calculateScoreOfJobsAgainstUser(self, resume, job):
        data = self.getApplicantData(resume)
        job_data = self.getJobDescriptionData(job)
        job_desc_score = self.scoreJobDescription(data['job_trigrams'], job_data['job_bitri'])
        title_score = self.scoreJobTitle(data['titles'], job_data['title'])
        skills_score = self.scoreSkills(data['skills'], data['work_hist_skills'], job_data['skills'])
        companies_score = self.scoreCompanyMovement(data['company_name'], job_data['company_name'])
        title_score = self.scoreJobTitle(data['titles'], job_data['title'])

        #print job_desc_score, title_score, companies_score, skills_score
        weights_tmp = {'job_score': 0.70, 'titles': 0.10, 'company_score':0.95, 'skills':0.10}

        # Hacky exceptions - Desperate times call for desperate measures.

        try:
            if len(job_data['job_bitri']) < 85 or any([title in job_data['title'] for title in ['urgent', 'hiring', 'opening', 'drive', 'opportunity', 'recruitment', 'client']]):
                total = 0
            else:
                total = 100*sum([weights_tmp['job_score']*job_desc_score[0], weights_tmp['titles']*title_score, \
                             weights_tmp['skills']*skills_score, weights_tmp['company_score']*companies_score])
        except Exception as e:
            print str(e)
            total = 0

        total = 95 if total >= 100 else total

        return {'total':total, 'jobId': job_data['jobId']}
