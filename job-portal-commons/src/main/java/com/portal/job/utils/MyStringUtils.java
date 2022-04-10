package com.portal.job.utils;

public class MyStringUtils {

	/**
	 * s is the test string and t is the string to test in
	 * @param s
	 * @param t
	 * @return
	 */
	public static boolean isSubsequence(String s, String t) {
		int M = s.length();
		int N = t.length();

		int i = 0;
		for (int j = 0; j < N; j++) {
			if (s.charAt(i) == t.charAt(j))
				i++;
			if (i == M)
				return true;
		}
		return false;
	}

}
