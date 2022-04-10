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


class AggregateScore():
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
        print directoryPath
        university = pd.read_csv(directoryPath + '/Data/university_ranks.csv')
        company = pd.read_csv(directoryPath + '/Data/company_list.csv')

        self.university = university
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
        title = job['jobTitle']
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

        return score/req_skills_len if score != 0 else 0


    def scoreEducation(self, employee_ed, name):
        """
        This computes the difference between applicant's academic credentials and credentials required at the job.
        Also, computes university scores (string matching between name of University in Resume and JD).
        """

        university = self.university
        schools = [school for school in name if school != ""]
        rank = []

        for school in schools:
            match_score = []
            for university_name in university['Name']:
                try:
                    match_score.append(fuzz.ratio(university_name, school))
                except Exception as e:
                    match_score.append(0)

            #Case where there is no valid school in the parsed XML, resulting in "schools" to be an empty list.
            if not match_score:
                match_score.append(0)

            try:
                if max(match_score) > 70:
                    rank.append(university['Rank'][match_score.index(max(match_score))])
                else:
                    rank.append(1)
            except KeyError:
                rank.append(1)


        ed_weight = {'Bachelor': 0.1, 'Masters': 0.15, 'doctorate': 0.2, '': 0.05}

        employee_ed = list(set(employee_ed).intersection(ed_weight.keys()))
        employee_ed_weight = [ed_weight[i] for i in employee_ed]

        try:
            highest_qualification = ed_weight[max(employee_ed, key=lambda x: ed_weight[x])]
        except:
            highest_qualification = 0.05

        return sum(i*j for i, j in zip(employee_ed_weight, rank))


    def scoreCompanies(self, companies):
        """
        Returns a tuple of normalized average company scores and last two company scores.
        """
        company_list = self.company
        tier = []
        business_type = ['inc', 'solutions', 'pvt.', 'pvt', 'private', 'ltd', 'limited', 'business']

        companies = [' '.join(w for w in company.split() if w.lower() not in business_type)
                     for company in companies]

        for company in companies:
            match_score = []
            for company_name in company_list['company_name']:
                try:
                    match_score.append(fuzz.partial_ratio(str(company_name), str(company)))
                except KeyError, e:
                    match_score.append(0)

            try:
                if max(match_score) > 70:
                    tier.append(company_list['tier'][match_score.index(max(match_score))])
                else:
                    tier.append(1)
            except KeyError as e:
                tier.append(1)

        try:
            avg_score = (sum(tier)/len(tier))/4
        except:
            avg_score = 0

        return avg_score



    '''
    This function returns the [ {'userId' : '1' : 'total':50} ]
    Here 'total' is the score given particular 'USERID' by ENGINE after calculating the
    Users different attributes (infprmation) against the JOB info.
    '''
    def calculateScoreOfUsersAgainstJob(self, resume, job):
        data = self.getApplicantData(resume)
        job_data = self.getJobDescriptionData(job)
        job_desc_score = self.scoreJobDescription(data['job_trigrams'], job_data['job_bitri'])
        edu_score = self.scoreEducation(data['degreeType'], data['institute_name'])
        skills_score = self.scoreSkills(data['skills'], data['work_hist_skills'], job_data['skills'])
        companies_score = self.scoreCompanies(data['company_name'])
        title_score = self.scoreJobTitle(data['titles'], job_data['title'])

        weights_tmp = {'job_score': 1.5, 'titles': 0.5, 'company_score':0.25, 'skills':0.20, 'education':0.2}

        try:
            position_score = sum([i*j for i, j in zip(job_desc_score, data['years_exp']) if j > 0.5 and j != 1.0])/ \
                sum([i for i in data['years_exp'] if i > 0.5 and i != 1.0])
        except:
            position_score = 0

        try:
            total = 100*sum([weights_tmp['job_score']*position_score/0.32, weights_tmp['titles']*title_score, \
                             weights_tmp['skills']*skills_score, weights_tmp['company_score']*companies_score,weights_tmp['education']*edu_score])/sum(weights_tmp.values())
        except Exception as e:
            print str(e)
            total = 0

        total = 95 if total >= 100 else total

        return {'total':total, 'userId': data['user_id']}

    '''
    TODO - Need to check this function latter. Currently it's not being used anywhere and need to check
    whether it's calculating the scores accuratlty or not.
    '''
    def calculateScoreOfJobsAgainstUser(self, resume, job):
        data = self.getApplicantData(resume)
        job_data = self.getJobDescriptionData(job)
        job_desc_score = self.scoreJobDescription(data['job_trigrams'], job_data['job_bitri'])
        title_score = self.scoreJobTitle(data['titles'], job_data['title'])
        edu_score = self.scoreEducation(data['degreeType'], data['institute_name'])
        skills_score = self.scoreSkills(data['skills'], data['work_hist_skills'], job_data['skills'])
        companies_score = self.scoreCompanies(data['company_name'])
        title_score = self.scoreJobTitle(data['titles'], job_data['title'])

        weights_tmp = {'job_score': 1.5, 'titles': 0.5, 'company_score':0.25, 'skills':0.20, 'education':0.2}

        try:
            position_score = sum([i*j for i, j in zip(job_desc_score, data['years_exp']) if j > 0.5 and j != 1.0])/ \
                sum([i for i in data['years_exp'] if i > 0.5 and i != 1.0])
        except:
            position_score = 0

        try:
            total = 100*sum([weights_tmp['job_score']*position_score/0.32, weights_tmp['titles']*title_score, \
                             weights_tmp['skills']*skills_score, weights_tmp['company_score']*companies_score,weights_tmp['education']*edu_score])/sum(weights_tmp.values())
        except Exception as e:
            print str(e)
            total = 0

        total = 95 if total >= 100 else total

        return {'total':total, 'jobId': job_data['jobId']}
