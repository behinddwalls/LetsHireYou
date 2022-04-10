package com.portal.job.utils.matching;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.google.common.collect.Maps;

/**
 * Cosine similarity calculator class
 * 
 * @author behinddwalls
 */
public class CosineSimilarity {

	/**
	 * Method to calculate cosine similarity between two documents.
	 * 
	 * @param vector1
	 *            : document vector 1 (a)
	 * @param vector2
	 *            : document vector 2 (b)
	 * @return
	 */
	public double getCosineSimilarity(double[] vector1, double[] vector2) {
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;

		for (int i = 0; i < vector1.length; i++) // docVector1 and docVector2
													// must be of same length
		{
			dotProduct += vector1[i] * vector2[i]; // a.b
			magnitude1 += Math.pow(vector1[i], 2); // (a^2)
			magnitude2 += Math.pow(vector2[i], 2); // (b^2)
		}

		magnitude1 = Math.sqrt(magnitude1);// sqrt(a^2)
		magnitude2 = Math.sqrt(magnitude2);// sqrt(b^2)

		if (magnitude1 != 0.0 | magnitude2 != 0.0) {
			cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		} else {
			return 0.0;
		}
		return cosineSimilarity;
	}

	public Map<String, double[]> getTermFrequenceyVectors(
			final @NotNull String string1, final @NotNull String string2) {
		final String REPLACE_PATTERN = "[\\W&&[^\\s]]";

		final Map<String, double[]> termFrequencyMap = Maps.newLinkedHashMap();

		final String[] stringArray1 = string1.replaceAll(REPLACE_PATTERN, "")
				.split("\\W+");
		final String[] stringArray2 = string2.replaceAll(REPLACE_PATTERN, "")
				.split("\\W+");

		for (String item1 : stringArray1) {
			item1 = item1.toLowerCase();
			if (termFrequencyMap.containsKey(item1.toLowerCase())) {
				termFrequencyMap.put(item1,
						new double[] { termFrequencyMap.get(item1)[0] + 1, 0 });
			} else {
				termFrequencyMap.put(item1, new double[] { 1, 0 });
			}
		}

		for (String item2 : stringArray2) {
			item2 = item2.toLowerCase();
			if (termFrequencyMap.containsKey(item2)) {
				termFrequencyMap.put(item2,
						new double[] { termFrequencyMap.get(item2)[0],
								termFrequencyMap.get(item2)[1] + 1 });
			} else {
				termFrequencyMap.put(item2, new double[] { 0, 1 });
			}
		}
		return termFrequencyMap;

	}

	public static void main(String[] args) {
		CosineSimilarity cos = new CosineSimilarity();
		Map<String, double[]> termMap = cos.getTermFrequenceyVectors(
				"Business Development Manager", "Software Development Manager");
		double v1[] = new double[termMap.size()];
		double v2[] = new double[termMap.size()];
		int i = 0;
		for (Map.Entry<String, double[]> entry : termMap.entrySet()) {
			v1[i] = entry.getValue()[0];
			v2[i] = entry.getValue()[1];
			i++;
		}
		System.out.println(cos.getCosineSimilarity(v1, v2));

	}
}