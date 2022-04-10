#!/usr/bin/env python
# -*- coding: utf-8 -*-

'''
author: Shubham Singh Tomar <tomarshubham24@gmail.com>
'''
import string
import numpy as np
from xml.dom.minidom import parseString
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn import preprocessing
from nltk.tokenize import RegexpTokenizer
import nltk.collocations as cl
from nltk.stem.lancaster import LancasterStemmer
from datetime import datetime as dt
from fuzzywuzzy import fuzz
import pandas as pd


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

        self.weights = {'skills':1, 'job_hist':1, 'education':1}
        job_hierarchy = np.genfromtxt('Data/job_hierarchy.txt',delimiter=',',names=True,dtype=None)
        job_level = [level.lower().translate(string.maketrans("",""), string.punctuation) \
                        for level in job_hierarchy['job_level']]


        self.hierarchy = dict(zip(job_level,job_hierarchy['numeric']))
        company = pd.read_csv('../experimental/scrapers/glassdoor_ratings_copy_2.csv')
        self.company = company

        skills_data = np.genfromtxt('Data/Sample_skills.txt',delimiter=',',names=True,dtype=None)
        skill_domains = np.genfromtxt('Data/Domain_skill_relevancies.csv',delimiter=',',names=True,dtype='int')
        string_domains = np.genfromtxt('Data/Domains_predikt.csv',delimiter=',',names=True,dtype=None)
        string_domains = dict(zip(string_domains['Domain_ID'],string_domains['Domain_name']))
        taxonomy = pd.read_csv('Data/taxonomyMap_Comp_Tax_3_2_15.csv')

        mappings = pd.read_csv('Data/job_title_to_role_mappings.csv')


        skill_to_id = {}

        for i,v in enumerate(skills_data['Skill_name']):
            if v not in skill_to_id.keys():
                skill_to_id[v.lower()] = skills_data['Skill_id'][i]

        skill_to_domain = {}

        for i,v in enumerate(skill_domains['Skill_id']):
            if v not in skill_to_domain.keys():
                skill_to_domain[v] = skill_domains['Domain_id'][i]

        self.hierarchy = dict(zip(job_level,job_hierarchy['numeric']))
        self.string_domains = string_domains
        self.skill_to_id = skill_to_id
        self.skill_to_domain = skill_to_domain
        self.mappings = mappings
        self.taxonomy = taxonomy



    def count(self, items):
        counted = {}
        most_freq = []
        for item in items:
            try:
                counted[item] += 1.
            except KeyError:
                counted[item] = 1
        for key in counted.keys():
            most_freq.append((key, counted[key]))
        most_freq.sort(key=lambda tup: tup[1])
        return most_freq


    def getApplicantData(self, xmlfile):
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

        skills = 'CompetencyEvidence'
        job_description = 'Description'
        job_titles = 'PositionHistory'
        achievements = 'Achievements'
        taxonomies = 'sov:BestFitTaxonomies'

        company_names = 'OrganizationName'
        degree_major = 'DegreeMajor'

        with open(xmlfile, 'r') as infile:
            data = infile.read()
            data = filter(lambda x: x in string.printable, data).replace('&', 'and').replace('<>','')

        dom = parseString(data)

        #Convert past job descriptions into bigrams and trigrams to compare to target job description
        bigrams, trigrams = [], []
        try:
            jobs_from_file = dom.getElementsByTagName(job_description)
        except Exception:
            jobs_from_file = []
        times = 0


        for job in jobs_from_file:
            if job.firstChild != None:
                job_hist = job.firstChild.nodeValue.lower()
                biTri_temp = self.getBiTrigrams(job_hist)
                bigrams.append(biTri_temp[0])
                trigrams.append(biTri_temp[0])

        #Convert past job titles into bigrams and trigrams and get years of experience in respective roles
        experience_yrs = []
        raw_titles = []
        try:
            job_titles_from_file = dom.getElementsByTagName(job_titles)
        except Exception:
            job_titles_from_file = []
        position_yrs = []

        for jobTitle in job_titles_from_file:
            #See getYearsExperience module
            yrs_xp = self.getYearsExperience(jobTitle)
            if yrs_xp > 0.5 and yrs_xp != 1.0:
                position_yrs.append(str(yrs_xp))
                total_exp += yrs_xp


            if jobTitle.getElementsByTagName('Title') != []:
                past_job = jobTitle.getElementsByTagName('Title')[0].firstChild.nodeValue.lower()
                raw_titles.append(past_job)
                experience_yrs.append(yrs_xp)

        #Get names of companies applicant has worked at
        companies = []
        for company in dom.getElementsByTagName(company_names):
            if company.firstChild != None:
                companies.append(company.firstChild.nodeValue.lower())
            else:
                companies.append("")

        #Get skills from given competencies and years of experience with that skill
        skill_vec = {}
        competency = dom.getElementsByTagName(skills)
        relevant_table = {"Found in WORK HISTORY; POS-1", "Found in WORK HISTORY; POS-2",
                          "Found in WORK HISTORY; POS-1; POS-2", "Found in SKILLS; WORK HISTORY",
                          "Found in WORK HISTORY", "Found in WORK HISTORY; POS-3"}
        for skill in competency:
            if skill.getAttribute('typeDescription') in relevant_table:
                yrs_exp = int(skill.getElementsByTagName('NumericValue')[0].firstChild.nodeValue) / 12.
                skill_name = skill.getAttribute('name').lower()
                if skill_name not in skill_vec.keys():
                    skill_vec[skill_name] = yrs_exp

        #Get education data: level and field
        degrees = {}
        for degree in dom.getElementsByTagName('Degree'):
            level = degree.getAttribute('degreeType')
            if degree.getElementsByTagName('DegreeMajor') != []:
                field = degree.getElementsByTagName(degree_major)[0].getElementsByTagName('Name')[0].firstChild.nodeValue
                degrees[level] = field
            else:
                degrees[level] = ''

        #Get university/college name
        schools = []
        for school in dom.getElementsByTagName('SchoolOrInstitution'):
            level = school.getAttribute('schoolType')
            if school.getElementsByTagName('School') != []:
                name = school.getElementsByTagName('SchoolName')
                schools.append(name[0].firstChild.nodeValue)
            else:
                schools.append("")

        #Get scores in schools
        scores = []
        for score in dom.getElementsByTagName('EducationalMeasure'):
            scores.append(score.getElementsByTagName('StringValue')[0].firstChild.nodeValue)

        #Get achievement counts
        achievement_count = []
        for achievement in dom.getElementsByTagName(achievements):
            achievement_count.append(achievement.getElementsByTagName('Achievement'))


        #Get Sovren specific data
        best_fit_taxonomies = []
        for best_fit in dom.getElementsByTagName(taxonomies):
            taxonomy = best_fit.getElementsByTagName('sov:BestFitTaxonomy')

        #Get locations
        locations = []
        for location in dom.getElementsByTagName('Municipality'):
            locations.append(location.firstChild.nodeValue)


        emp_data = {'skills':skill_vec, 'job_bigrams':bigrams, 'job_trigrams':trigrams,
                    'education':degrees, 'schools':schools, 'scores':scores, 'locations':locations,
                    'companies':companies,'raw_titles':raw_titles, 'job_experience':experience_yrs,
                    'years_exp':total_exp, 'position_tenure':position_yrs, 'achievements': achievement_count}

        personal_data = self.get_personal_data(dom)
        return (emp_data, personal_data)


    def get_personal_data(self, dom):
        try:
            name = dom.getElementsByTagName('FormattedName')[0].firstChild.nodeValue
        except Exception:
            name = ''
        try:
            email = dom.getElementsByTagName('InternetEmailAddress')[0].firstChild.nodeValue
        except Exception:
            email = ''
        return {'name': name, 'email': email}


    def getYearsExperience(self, job):
        """
        Calculate the years of experience for a past job. The input 'job' is a DOM element <JobHistory> from the
        XML file. Outputs a simple int or float for years of experience.
        """
        start_date = job.getElementsByTagName('StartDate')[0]
        if job.getElementsByTagName('EndDate') != []:
            end_date = job.getElementsByTagName('EndDate')[0]
        else:
            return 1.

        if start_date.getElementsByTagName('Year') != []:
            if end_date.getElementsByTagName('Year') != []:
                end_yr = int(end_date.getElementsByTagName('Year')[0].firstChild.nodeValue)
                start_yr = int(start_date.getElementsByTagName('Year')[0].firstChild.nodeValue)
                yrs_exp = end_yr - start_yr

            #'StringDate' always corresponds to 'current', thus we take the current year for the end year
            elif end_date.getElementsByTagName('StringDate') != []:
                start_yr = int(start_date.getElementsByTagName('Year')[0].firstChild.nodeValue)
                yrs_exp = 2014 - start_yr
            else:
                yrs_exp = 1.

        elif start_date.getElementsByTagName('YearMonth') != []:
            if end_date.getElementsByTagName('YearMonth') != []:
                end_yrmonth = end_date.getElementsByTagName('YearMonth')[0].firstChild.nodeValue.split('-')
                start_yrmonth = start_date.getElementsByTagName('YearMonth')[0].firstChild.nodeValue.split('-')
                end_yrmonth = dt(int(end_yrmonth[0]), int(end_yrmonth[1]), 1)
                start_yrmonth = dt(int(start_yrmonth[0]), int(start_yrmonth[1]), 1)
                yrs_exp = (end_yrmonth - start_yrmonth).days/365.

            #Since start_date is the year AND month, we should use the datetime.now() for the end_date for better accuracy
            elif end_date.getElementsByTagName('StringDate') != []:
                start_yrmonth = start_date.getElementsByTagName('YearMonth')[0].firstChild.nodeValue.split('-')
                start_yrmonth = dt(int(start_yrmonth[0]), int(start_yrmonth[1]), 1)
                yrs_exp = (dt.now() - start_yrmonth).days/365.
            else:
                #Default to 1 year exp. if we do not know start/end date
                yrs_exp = 1.

        elif start_date.getElementsByTagName('AnyDate') != []:
            if end_date.getElementsByTagName('AnyDate') != []:
                end_yrmonth = end_date.getElementsByTagName('AnyDate')[0].firstChild.nodeValue.split('-')
                start_yrmonth = start_date.getElementsByTagName('AnyDate')[0].firstChild.nodeValue.split('-')
                end_yrmonth = dt(int(end_yrmonth[0]), int(end_yrmonth[1]), int(end_yrmonth[2]))
                start_yrmonth = dt(int(start_yrmonth[0]), int(start_yrmonth[1]), int(start_yrmonth[2]))
                yrs_exp = (end_yrmonth - start_yrmonth).days/365.

            #Since start_date is the year AND month, we should use the datetime.now() for the end_date for better accuracy
            elif end_date.getElementsByTagName('StringDate') != []:
                start_yrmonth = start_date.getElementsByTagName('AnyDate')[0].firstChild.nodeValue.split('-')
                start_yrmonth = dt(int(start_yrmonth[0]), int(start_yrmonth[1]), int(start_yrmonth[1]))
                yrs_exp = (dt.now() - start_yrmonth).days/365.
            else:
                #Default to 1 year exp. if we do not know start/end date
                yrs_exp = 1.

        else:
            yrs_exp = 1.

        return yrs_exp

    
    def mag(self, vector):
        sum = 0.
        for item in vector:
            sum += (item*item)
        return np.sqrt(sum)


    def getBiTrigrams(self, text):
        """
        Take an input string 'text', convert to lowercase and utf-8, remove stop words,
        stem the words, and finally output all bigrams and trigrams in the text.
        """
        bigram_measures = cl.BigramAssocMeasures()
        trigram_measures = cl.TrigramAssocMeasures()
        tk = RegexpTokenizer(r'\w+')

        #Choose a stemmer
        st = LancasterStemmer()

        text = tk.tokenize(text.lower()) #.decode('utf-8'))
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


    def scoreLocation(self, locations, job_location):
        """
        Takes in list of cities in resume (location of colleges, past jobs etc) and location of current job.
        Assigns an additive score if there is any match
        """
        match = [city for city in locations if city in job_location]

        score = 1 if match else 0

        return score


    def scoreJobTitleMovement(self, past_titles, target_title):
        """
        Fuzzy matches Job description title and resume titles. Outputs a tuple of
        a list of booleans, hierarchy weight and experience years.
        """

        resume_title_scores = {}
        hierarchy_list = []

        hier_weight = {0:1, 1:0.95, 2:0.8, 3:0.7, 4:0.7, 5:0.6, 6:0.6, 7:0.6, 8:0.6, 9:0.6, 10:0.5}


        for _level in self.hierarchy.keys():
            print _level
            if _level in target_title:
                job_level = self.hierarchy[_level]
                break
            else:
                job_level = 9.

        last_title = past_titles[-1]

        for _level in self.hierarchy.keys():
            if _level in last_title:
                emp_level = self.hierarchy[_level]
                break
            else:
                emp_level = 9.

            diff = np.abs(emp_level - job_level)

        return diff


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
            except KeyError, e:
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

    """
    def scoreSkills(self, emp_skills, job_skills):
        emp_skills = [skill.lower() for skill in emp_skills]
        job_skills = [skill.lower() for skill in job_skills]
        print emp_skills, job_skills
        return len(list(set(emp_skills).intersection(job_skills)))
    """
    def scoreSkills(self, employee_skills_temp, req_skills, skillIDs, skillDoms, raw_domains):
        """
        Find how similar the applicant skills are to the target job skills. The inputs are strings which we convert
        to integers using the LabelEncoder() function from sklearn.preprocessing. These integers then specify the index
        of a skill in the skills vector. It then finds the distance between employee and job skills feature vectors as
        a metric for similarity; the closer they are the more similar they are. We also only consider employee skills
        that are found in the job description so as to avoid lowering the score if the resume has many unrelated skills.
        Example:

        Resume skill vector:    Job skill vector:   Final vector (Job - Resume):    Normalized distance:
        C++             [1]                 [0]
        Java            [1]                 [0]
        Python          [1]                 [1]     =>      [0]
        Data Analysis   [1]                 [1]     =>      [0]         sqrt(0^2 + 0^2 + 1^2)/sqrt(1^2 + 1^2 + 1^2)
        R               [0]                 [1]     =>      [1]                             = 0.58

        Since the resume has two unrelated skills  (C++ and Java) we remove those in the final vector. We see that
        the worst possible score (no match) is 1, and the best possible (perfect match) is 0. These values are also
        weighted by years of experience i.e., more years of experience in a skill weighs it closer to 0 in the final
        vector.
        """

        taxonomy = self.taxonomy
        filtered_taxonomy = taxonomy[taxonomy['ParentTaxonomy'] == 'personal attributes']
        behavioural_skills = [skills for skills in filtered_taxonomy['competency']]

        employee_skills_temp = dict((k,v) for (k,v) in employee_skills_temp.items() if k not in behavioural_skills)
        req_skills = [skills for skills in req_skills if skills not in behavioural_skills]


        employee_skills = []
        scores = []
        yrs = []
        employee_domains = []
        le = preprocessing.LabelEncoder()
        req_skill_doms = []
        scored_domains = []

        if not isinstance(req_skills, dict):
            req_skills = dict(zip(req_skills, np.zeros(len(req_skills))))

        non_employee_skills = req_skills.keys()

        for skill in req_skills.keys():
            if skill in skillIDs.keys():
                try:
                    req_skill_doms.append(skillDoms[skillIDs[skill]])
                except KeyError:
                    continue

        for skill in employee_skills_temp.keys()[:3]:
            if skill in req_skills.keys():
                yrs.append(round(employee_skills_temp[skill],2))
                years_diff = employee_skills_temp[skill] - req_skills[skill]
                employee_skills.append(skill)
                if years_diff >= 0.:
                    scores.append(1.5 - (1. / (1 + years_diff)))
                else:
                    scores.append(0.5*years_diff)
                #This weights the scores by experience (1 / (1 + years_exp))
                non_employee_skills.remove(skill)

        for skill in employee_skills_temp.keys()[3:]:
            if skill in req_skills.keys():
                yrs.append(round(employee_skills_temp[skill],2))
                years_diff = employee_skills_temp[skill] - req_skills[skill]
                employee_skills.append(skill)
                if years_diff >= 0.:
                    scores.append(1. - (1. / (1 + years_diff)))
                else:
                    scores.append(0.3*years_diff)
                #This weights the scores by experience (1 / (1 + years_exp))
                non_employee_skills.remove(skill)

            elif skill not in req_skills.keys() and (skill in skillIDs.keys() and skillIDs[skill] in skillDoms.keys()
                    and skillDoms[skillIDs[skill]] in req_skill_doms):
                yrs.append(round(employee_skills_temp[skill],2))
                _domain = skillDoms[skillIDs[skill]]
                employee_domains.append(raw_domains[_domain])
                employee_skills.append(skill)
                scores.append(1. - (1. / (1 + employee_skills_temp[skill])))

        total_skills = np.concatenate((req_skills.keys(),employee_skills))
        le.fit(total_skills)
        enc_employee_skills = le.transform(employee_skills) #encode employee's skills to integers
        emp_skills = np.zeros(len(list(set(total_skills)))) #initialize employee skill scores
        job_skills = np.ones(len(list(set(total_skills)))) #initialize job skill vector
        for i,skill in enumerate(enc_employee_skills): #index corresponds to the encoded skill
            emp_skills[skill] = scores[i]

        cos_sim = np.dot(emp_skills, job_skills) / (self.mag(emp_skills)*self.mag(job_skills)) #find cosine similarity between employee and job skill vectors
        if np.isnan(cos_sim):
            cos_sim = 0.

        #Find the relevant domains the applicant has, and score these domains based on simple occurence frequency
        norm = float(len(employee_domains))
        sorted_domains = self.count(employee_domains)
        for i in range(len(sorted_domains)):
            scored_domains.append((sorted_domains[i][0],sorted_domains[i][1] / norm))
        print employee_skills
        print req_skills

        return [np.abs(cos_sim), zip(employee_skills,yrs), scored_domains, non_employee_skills]



    def outputScore(self, inresume, job):

        data, personal_data = self.getApplicantData(inresume)

        job_desc = self.getBiTrigrams(job['jobDescription'])
        location_score = self.scoreLocation(data['locations'], job['location'])

        score = self.scoreJobDescription(data['job_bigrams'], job_desc[0])
        #title_score = self.scoreJobTitleMovement(data['raw_titles'], job['jobTitle'])
        company_movement = self.scoreCompanyMovement(data['companies'], job['jobComapnyName'])
        #skills = self.scoreSkills(data['skills'], job['jobSkills'].split('\n'))
        skills_results = self.scoreSkills(data['skills'], job['jobSkills'].split('\n'), self.skill_to_id, self.skill_to_domain,
                self.string_domains)


        req_exp = job["jobExperience"]
        exp = [int(s) for s in req_exp.split() if s.isdigit()]
        min_exp, max_exp = exp[0], exp[1]


        if min_exp <= data['years_exp'] <= max_exp:
            #print company_movement, title_score, score[0], location_score, skills
            output = company_movement, score[0], skills_results
            return output
        else:
            return "Less experience"
