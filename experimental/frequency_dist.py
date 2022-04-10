import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("../engine/experiment.csv")
skills = df['skills']

skills.plot(kind='hist')
df['zscore'] = (df.skills - df.skills.mean())/df.skills.std(ddof=1)
df['zscore'].plot(kind='hist')
plt.show()
