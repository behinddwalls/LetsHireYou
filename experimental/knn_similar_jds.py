import pandas as pd
from glob2 import glob
import string
import numpy as np
from xml.dom.minidom import parseString
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.neighbors import KNeighborsClassifier


features = []
for job_file in glob('./sample_jds/**/*.xml'):
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
            min_exp = 1

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
            edu_from_file = [x.firstChild.nodeValue.lower() for x in dom.getElementsByTagName('DegreeType')][0]
        except Exception:
            edu_from_file = 'Bachelors'

        for each in ed_dict.keys():
            if each in edu_from_file:
                education.append(each)

        #Get raw job description
        try:
            job_description = dom.getElementsByTagName('SourceText')[0].firstChild.nodeValue.lower()
        except Exception:
            job_description = ""

        try:
            taxonomy = dom.getElementsByTagName('SubTaxonomyName')[0].firstChild.nodeValue.lower()
        except Exception:
            taxonomy = ""

        features.append({'title':job_title, 'skills':skill_vec, 'education':education, 'description':job_description, 'min_exp':min_exp, 'taxonomy':taxonomy})

df = pd.DataFrame(features)


def split_into_lemmas(text):
    bigram_vectorizer = CountVectorizer(ngram_range=(1, 3), token_pattern=r'\b\w+\b', min_df=1)
    analyze = bigram_vectorizer.build_analyzer()
    return analyze(text)

test_idx = np.random.uniform(0, 1, len(df)) <= 0.8
train = df[test_idx==True]
test = df[test_idx==False]

bow_transformer = CountVectorizer(analyzer = split_into_lemmas, stop_words='english', strip_accents='ascii').fit(train['description'])
text_bow = bow_transformer.transform(train['description'])
tfidf_transformer = TfidfTransformer().fit(text_bow)
tfidf = tfidf_transformer.transform(text_bow)
text_tfidf = tfidf_transformer.transform(text_bow)

clf = MultinomialNB().fit(text_tfidf, train['taxonomy'])

cols = ['title', 'description', 'taxonomy']

pred_class = pd.DataFrame(columns = ['title', 'description', 'taxonomy'])
i = 0
for row in test[cols].iterrows():
    try:
        bow_text = bow_transformer.transform(row[1])
        tfidf_text = tfidf_transformer.transform(bow_text)
        pred_class.loc[i-1, 'title'] = row[0]
        pred_class.loc[i-1, 'taxonomy'] = zip(clf.classes_, clf.predict_proba(tfidf_text)[0])
    except Exception as e:
        print str(e)
        pass
        #pred_class.loc[i-1, 'title'] = row['title']
print pred_class
pred_class.to_csv('test.csv')
