import pandas as pd

df = pd.read_csv('../engine/experiment.csv')
df['zscore'] = (df.skills - df.skills.mean())/df.skills.std(ddof=1)
print df['zscore'], df['skills']
df = df[(df.skills < 5) & (df.job_hist > 0.15) & (df.years_exp < 11) & (df.titles == 1) & (df.edu < 0.85)]
df.to_csv('filtered_experiments.csv')
