package com.portal.job.helper;

import org.apache.commons.lang3.StringUtils;

public final class StringUtility {

	public static String replaceEndOfLineWithBreakLine(String originalText) {

		if (StringUtils.isEmpty(originalText)) {
			return StringUtils.EMPTY;
		}
		return originalText.replaceAll(System.getProperty("line.separator"),
				"<br/>").replaceAll("\n", "<br/>");

	}

}
