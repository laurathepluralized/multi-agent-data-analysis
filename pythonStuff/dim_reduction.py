
from sklearn import preprocessing
import numpy as np
from sklearn import decomposition
import matplotlib.pyplot as plt
from numpy import linalg
from sklearn.feature_selection import mutual_info_regression
from sklearn.feature_selection import f_regression
from sklearn.feature_selection import SelectKBest
data = df.drop(columns = "NonTeamCapture")
y = df["NonTeamCapture"]
scaler = preprocessing.StandardScaler().fit(data)
data_scaled = scaler.transform(data)

pca = decomposition.PCA()
pca.fit(data_scaled)
#print(pca.singular_values_)
#plt.plot(pca.singular_values_)
#print(pca.explained_variance_)
plt.plot(pca.explained_variance_)

# Python feature selection
#http://scikit-learn.org/stable/modules/feature_selection.html

select_k_best = SelectKBest(mutual_info_regression, k=10).fit(data_scaled, y)
k_features = []

for b, feature in zip(select_k_best.get_support(), data.columns):
    if b:
        k_features.append(feature)
        
        