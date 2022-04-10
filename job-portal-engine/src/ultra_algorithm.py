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


class backupScore():

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

        skills_data = np.genfromtxt('Data/Sample_skills.txt',delimiter=',',names=True,dtype=None)
        skill_domains = np.genfromtxt('Data/Domain_skill_relevancies.csv',delimiter=',',names=True,dtype='int')
        string_domains = np.genfromtxt('Data/Domains_ultra.csv',delimiter=',',names=True,dtype=None)
        string_domains = dict(zip(string_domains['Domain_ID'],string_domains['Domain_name']))

        mappings = pd.read_csv('Data/job_title_to_role_mappings.csv')
        university = pd.read_csv('Data/university_ranks.csv')
        company = pd.read_csv('Data/company_list.csv')



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
        self.university = university
        self.company = company


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


    def getApplicantData(self, xmlfile, xmltype='sovren'):
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

        if xmltype.lower()=='sovren':
            company_names = 'OrganizationName'
            degree_major = 'DegreeMajor'
        else:
            return 'Invalid XML type! XML type must be "sovren".'

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


        emp_data = {'skills':skill_vec, 'job_bigrams':bigrams, 'job_trigrams':trigrams, 'education':degrees, 'schools':schools, 'scores':scores,'companies':companies,
                    'raw_titles':raw_titles, 'job_experience':experience_yrs, 'years_exp':total_exp, 'position_tenure':position_yrs, 'achievements': achievement_count}

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


    def getJobDescriptionData(self, job_file):
        with open(job_file, 'r') as infile:
            data = infile.read()
            data = filter(lambda x: x in string.printable, data).replace('&', 'and').replace('<>','')

        dom = parseString(data)

        #Get job title
        try:
            job_title = dom.getElementsByTagName('PositionTitle')[0].firstChild.nodeValue.lower()
        except (ValueError, Exception) as e:
            job_title = ""

        #Get min experience if available
        try:
            min_exp = float(dom.getElementsByTagName('MinExperience')[0].getAttribute('years'))
        except (IndexError, Exception) as e:
            min_exp = 'none'

        norm = float(max(self.hierarchy.values()))
        for career_level in self.hierarchy.keys():
            if career_level in job_title:
                numeric_level = self.hierarchy[career_level]
                break
            else:
                numeric_level = 9. #basic hierarchy level (non-junior, non-senior)

        adjusted_skill_ed = ((numeric_level+1)/(norm+1))
        adjusted_job = ((norm+1.-numeric_level)/(norm+1))

        #adjust weights based on job description's posted job hierarchy level
        #self.weights = {'skills':adjusted_skill_ed*0.6, 'job_hist':adjusted_job*1.5, 'education':adjusted_skill_ed*1.0}
        #Get skills and education
        skill_vec = []
        education = []
        ed_dict = {'bachelors':['bsc','ba','bs','bachelors', 'bachelors degree'],
                    'masters':['ma','msc','ms','msci','masters', 'masters degree'],
                    'doctorate':['phd','dphil','doctorate','doctorate degree']}

        try:
            skills_from_file = dom.getElementsByTagName('Competency')
        except Exception:
            skills_from_file = []
        for skill in skills_from_file:
            skill_name = skill.getAttribute('name').lower()
            if skill.getAttribute('description') == 'Degree/Qualification':
                education_level = skill.getAttribute('name').lower()
                for key in ed_dict.keys():
                    for degree_type in ed_dict[key]:
                        if degree_type in education_level:
                            education.append(key)
                            break
            elif 'Skill' in skill.getAttribute('description'):
                if len(skill_name.split(" ")) <= 3:
                    skill_vec.append(skill_name)
        skill_vec = list(set(skill_vec))

        #Get raw job description
        try:
            job_description = dom.getElementsByTagName('PositionDescription')[0].firstChild.nodeValue.lower()
        except Exception:
            job_description = ""

        return {'title':job_title, 'skills':skill_vec, 'education':education, 'description':job_description, 'min_exp':min_exp}


    def getJobDescriptionDataFromSovren(self, job_file):
        with open(job_file, 'r') as infile:
            data = infile.read()
            data = filter(lambda x: x in string.printable, data).replace('&', 'and').replace('<>','')
        dom = parseString(data)

        try:
            job_title_root = dom.getElementsByTagName('JobTitles')
            job_title = job_title_root[0].getElementsByTagName("MainJobTitle")[0].firstChild.nodeValue.lower()
        except Exception:
            job_title = ''

        try:
            min_exp = float(dom.getElementsByTagName('MinimumYears')[0].firstChild.nodeValue)
        except Exception:
            min_exp = 'none'

        norm = float(max(self.hierarchy.values()))
        for career_level in self.hierarchy.keys():
            if career_level in job_title:
                numeric_level = self.hierarchy[career_level]
                break
            else:
                numeric_level = 9. #basic hierarchy level (non-junior, non-senior)

        adjusted_skill_ed = ((numeric_level+1)/(norm+1))
        adjusted_job = ((norm+1.-numeric_level)/(norm+1))

        #adjust weights based on job description's posted job hierarchy level
        #self.weights = {'skills':adjusted_skill_ed*0.6, 'job_hist':adjusted_job*1.5, 'education':adjusted_skill_ed*1.0}
        #Get skills and education
        skill_vec = []
        education = []
        ed_dict = {'bachelors':['bsc','ba','bs','bachelors', 'bachelors degree'],
                    'masters':['ma','msc','ms','msci','masters', 'masters degree'],
                    'doctorate':['phd','dphil','doctorate','doctorate degree']}
        try:
            skills_from_file = [x.firstChild.nodeValue.lower() for x in dom.getElementsByTagName('RequiredSkill')]
            other_skills_from_file = [x.firstChild.nodeValue.lower() for x in dom.getElementsByTagName('OtherSkill')]
            skills_from_file = skills_from_file + other_skills_from_file
        except Exception:
            skills_from_file = []

        skill_vec = list(set(skills_from_file))

        try:
            edu_from_file = [x.firstChild.nodeValue.lower() for x in dom.getElementsByTagName('DegreeType')]
        except Exception:
            edu_from_file = []

        for each in ed_dict.keys():
            if each in edu_from_file:
                education.append(each)

        #Get raw job description
        try:
            job_description = dom.getElementsByTagName('SourceText')[0].firstChild.nodeValue.lower()
        except Exception:
            job_description = ""
        return {'title':job_title, 'skills':skill_vec, 'education':education, 'description':job_description, 'min_exp':min_exp}


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

        text = tk.tokenize(text.lower().decode('utf-8'))
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
                try:
                    start_yrmonth = dt(int(start_yrmonth[0]), int(start_yrmonth[1]), int(start_yrmonth[2]))
                    yrs_exp = (end_yrmonth - start_yrmonth).days/365.
                except ValueError:
                    yrs_exp = 1.0

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


    def scoreSkills(self, employee_skills_temp, req_skills, skillIDs, skillDoms, raw_domains):
        """
        Find how similar the applicant skills are to the target job skills. The inputs are strings which we convert
        to integers using the LabelEncoder() function from sklearn.preprocessing. These integers then specify the index
        of a skill in the skills vector. It then finds the distance between employee and job skills feature vectors as
        a metric for similarity; the closer they are the more similar they are. We also only consider employee skills
        that are found in the job description so as to avoid lowering the score if the resume has many unrelated skills.
        Example:

        Resume skill vector:	Job skill vector:	Final vector (Job - Resume):	Normalized distance:
        C++				[1]					[0]
        Java			[1]					[0]
        Python			[1]					[1]		=>		[1]
        Data Analysis	[1]					[1]		=>		[1]			sqrt(1^2 + 1^2 + 0^2)/sqrt(1^2 + 1^2 + 1^2)
        R				[0]					[1]		=>		[0]								= ?

        Change this to: overlap/no. of skills.

        Since the resume has two unrelated skills  (C++ and Java) we remove those in the final vector. We see that
        the worst possible score (no match) is 1, and the best possible (perfect match) is 0. These values are also
        weighted by years of experience i.e., more years of experience in a skill weighs it closer to 0 in the final
        vector.
        """

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

        for skill in employee_skills_temp.keys():
            if skill in req_skills.keys():
                yrs.append(round(employee_skills_temp[skill],2))
                years_diff = employee_skills_temp[skill] - req_skills[skill]
                employee_skills.append(skill)
                if years_diff >= 0.:
                    scores.append(1. - (1. / (1 + np.exp(years_diff))))
                else:
                    scores.append(0.5*np.exp(years_diff))
                #This weights the scores by experience (1 / (1 + years_exp))
                non_employee_skills.remove(skill)
            elif skill not in req_skills.keys() and (skill in skillIDs.keys() and skillIDs[skill] in skillDoms.keys()
                    and skillDoms[skillIDs[skill]] in req_skill_doms):
                yrs.append(round(employee_skills_temp[skill],2))
                _domain = skillDoms[skillIDs[skill]]
                employee_domains.append(raw_domains[_domain])
                employee_skills.append(skill)
                scores.append(1. - (1.3 / (1 + employee_skills_temp[skill])))

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


        skills_match = len(set(employee_skills_temp).intersection(req_skills))
        return [skills_match, zip(employee_skills,yrs), scored_domains, non_employee_skills]


    def scoreJobTitle(self, past_titles, experience_yrs, target_title):
        """
        Fuzzy matches Job description title and resume titles. Outputs a tuple of
        a list of booleans, hierarchy weight and experience years.
        """

        resume_title_scores = {}
        mappings = self.mappings
        mappings_roles = mappings['role']
        hierarchy_list = []

        hier_weight = {0:1, 1:0.95, 2:0.8, 3:0.7, 4:0.7, 5:0.6, 6:0.6, 7:0.6, 8:0.6, 9:0.6, 10:0.5}

        # Experience with recent post are weighed more.
        experience_weight = map(lambda x:x*1, experience_yrs[:3]) + map(lambda x:x*0.6, experience_yrs[3:])

        for _level in self.hierarchy.keys():
            if _level in target_title:
                job_level = self.hierarchy[_level]
                break
            else:
                job_level = 9.

        for i,resume_title in enumerate(zip(past_titles, experience_yrs)):
            for _level in self.hierarchy.keys():
                if _level in past_titles[i]:
                    emp_level = self.hierarchy[_level]
                    break
                else:
                    emp_level = 9.

            diff = np.abs(emp_level - job_level)
            hierarchy_list.append(hier_weight[diff])



            mapped_resume_roles = list([fuzz.partial_ratio(past_titles[i], j) for j in mappings['title'].tolist()])
            mapped_job_roles = list([fuzz.partial_ratio(target_title, j) for j in mappings['title'].tolist()])

            if max(mapped_job_roles) > 90:
                if mappings_roles[mapped_resume_roles.index(max(mapped_resume_roles))] == mappings_roles[mapped_job_roles.index(max(mapped_job_roles))]:
                    resume_title_scores[i, past_titles[i]] = 1
                else:
                    resume_title_scores[i, past_titles[i]] = 0

            else:
                resume_title_scores[i, past_titles[i]] = 0

        match_score = []
        for title in past_titles:
            match_score.append(fuzz.partial_ratio(str(title), target_title))


        return match_score #sorted(resume_title_scores.items(), key=lambda t:t[0]), experience_weight, hierarchy_list


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


    def scoreEducation(self, employee_ed, req_ed, schools):
        """
        This computes the difference between applicant's academic credentials and credentials required at the job.
        Also, computes university scores (string matching between name of University in Resume and JD).
        """

        university = self.university
        schools = [school for school in schools if school != ""]
        rank = []

        for school in schools:
            match_score = []
            for university_name in university['Name']:
                try:
                    match_score.append(fuzz.ratio(university_name, school))
                except KeyError, e:
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



        ed_weight = {'bachelors': 0.1, 'masters': 0.15, 'doctorate': 0.2, '': 0.05}

        employee_ed = list(set(employee_ed).intersection(ed_weight.keys()))
        employee_ed_weight = [ed_weight[i] for i in employee_ed]

        try:
            highest_qualification = ed_weight[max(employee_ed, key=lambda x: ed_weight[x])]
        except:
            highest_qualification = 0.05

        req_qualification = ed_weight[req_ed[0]] if req_ed and req_ed[0] in ed_weight.keys() else 0.1

        return [sum(i*j for i, j in zip(employee_ed_weight, rank)), employee_ed, rank]


    def scoreTenure(self, companies):

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
                    tier.append(4)
            except KeyError as e:
                tier.append(4)

        return tier


    def outputScore(self, inresume, job, XMLtype):
	"""
	This outputs the final score out of 100 as well as the relevant skills and the years experience the applicant has
    in those skills, and the relevant skill domains. 'inresume' should be an XML file of the resume, and 'job' is a python
    dict of the job description with keys 'title', 'education', 'description', and 'skills'. The variable 'score_dict'
    contains all of the scores and their key values, 'total', 'education', 'skills', and 'job_history'. The variable
    'relevant_skills' is a list of tuples, each tuple containing an employee skill and the number of years experience
    they have in that skill. Example, [('c++', 5.0), ('java',2.0), ...] Finally, 'employee_skill_domains' is a list
    of tuples of all the skill domains the applicant has, as well as domain score. This should be the only method called!
	"""

        data, personal_data = self.getApplicantData(inresume, XMLtype)
        word_count = sum(len(words) for words in data['job_bigrams'])

        if not isinstance(job, dict):
            job_data = self.getJobDescriptionDataFromSovren(job)
        else:
            job_data = job

        bitri_job = self.getBiTrigrams(job_data['description'])
        raw_job_title = job_data['title']
        min_req_exp = job_data['min_exp']

        # Minimum required experience.
        req_exp = min_req_exp if min_req_exp != 'none' else 1.

        bi_score = self.scoreJobDescription(data['job_trigrams'][:len(data['position_tenure'])], bitri_job[1])

        position_score = [str(i) for i in bi_score]
        titles_score = self.scoreJobTitle(data['raw_titles'], data['job_experience'], raw_job_title)

        #combined_scores = [i+j for i,j in zip(titles_score[0], bi_score)]
        #exprience_hier = [i*j for i,j in zip(titles_score[1], titles_score[2])]

        # Position scores: sum(title similarity + desc. similarity)*exp_yrs*hier
        #position_scores = [i*j for i,j in zip(combined_scores, exprience_hier)]

        skills_results = self.scoreSkills(data['skills'], job_data['skills'], self.skill_to_id, self.skill_to_domain,
                self.string_domains)

        ed_score = self.scoreEducation(data['education'], job_data['education'], data['schools'])
        company_tier = self.scoreTenure(data['companies'])


        skills_score = skills_results[0]
        relevant_skills = skills_results[1]
        employee_skill_domains = skills_results[2]
        missing_skills = skills_results[3]
        if np.isnan(skills_score):
            skills_score = 0.

        #job_score = sum(position_scores) / req_exp
        job_score = sum(bi_score)
        #modified_job_score = 1. - 1./ (1. + job_score)

        weights = self.weights
        total = 100*(weights['skills']*skills_score + weights['job_hist']*job_score+ weights['education']*ed_score[0])

        #total = 99 if total > 99 else total

        #modified_experience_sum = 1. - 1. / (1. + sum(titles_score[1]))
        score_dict = {'total':total,'education':ed_score,'skills':skills_score,'job_history': job_score}
        #return score_dict, relevant_skills, employee_skill_domains, missing_skills, personal_data
        try:
            return score_dict['total'], raw_job_title, score_dict['education'][0], score_dict['skills'], score_dict['job_history'], data['years_exp'],' '.join(str(v) for v in titles_score), word_count, ' '.join(data['scores']), ' '.join(position_score),len(data['achievements'][0]), " ".join(data['position_tenure'])," ".join(str(v) for v in ed_score[1]), " ".join(str(v) for v in ed_score[2]).replace(',', '|'), ','.join(str(v) for v in company_tier).replace(',', '|'),' '.join(data['raw_titles']) ,' '.join(data['schools']).replace(",", " "), ' '.join(data['companies']).replace(",", " ")
        except:
            return score_dict['total'], raw_job_title, score_dict['education'][0], score_dict['skills'], score_dict['job_history'], data['years_exp'],' '.join(str(v) for v in titles_score), word_count, ' '.join(data['scores']), ' '.join(position_score),0, " ".join(data['position_tenure'])," ".join(str(v) for v in ed_score[1]), " ".join(str(v) for v in ed_score[2]).replace(',', '|') ,','.join(str(v) for v in company_tier).replace(',', '|'),' '.join(data['raw_titles']) ,' '.join(data['schools']).replace(",", " "), ' '.join(data['companies']).replace(",", " ")
