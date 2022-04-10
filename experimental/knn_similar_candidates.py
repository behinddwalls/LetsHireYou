import numpy as np
import pandas as pd
import pylab as pl
from sklearn.neighbors import KNeighborsRegressor

df = pd.read_csv("/home/shubham/Downloads/knn.csv - Sheet1.csv")

test_idx = np.random.uniform(0, 1, len(df)) <= 0.9
train = df[test_idx==True]
test = df[test_idx==False]

features = ['total_exp', 'Edu.', 'Skill_adj', 'Manual Score']

results = []
clf = KNeighborsRegressor(n_neighbors=5)
clf.fit(train[features], train['Manual Score'])
print test
preds = clf.predict(test[features])
print preds
"""
#accuracy = np.where(preds==test['resume JD'], 1, 0).sum() / float(len(test))
    #print "Neighbors: %d, Accuracy: %3f" % (n, accuracy)

    results.append([n, accuracy])

results = pd.DataFrame(results, columns=["n", "accuracy"])
print results

pl.plot(results.n, results.accuracy)
pl.title("Accuracy with Increasing K")
pl.show()
"""
