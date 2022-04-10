package com.portal.job.helper;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.portal.job.model.UserEducationDataModel;
import com.portal.job.model.UserExperienceDataModel;

public final class UserProfileHelper {

	/**
	 * @param responseList
	 */
	public static void sortExperienceDetails(
			List<UserExperienceDataModel> responseList) {
		if (!responseList.isEmpty()) {
			Collections.sort(responseList,
					new UserExperienceDataModel.OrderByLatestCompany());
			Collections.reverse(responseList);

			responseList
					.stream()
					.filter(x -> !StringUtils.isEmpty(x.getDescription()))
					.forEach(
							response -> {
								String formattedResponse = StringUtility
										.replaceEndOfLineWithBreakLine(response
												.getDescription());
								response.setDescription(formattedResponse);
							});

		}
	}

	/**
	 * @param responseList
	 */
	public static void sortEducationDetails(
			List<UserEducationDataModel> responseList) {
		if (!responseList.isEmpty()) {
			Collections.sort(responseList,
					new UserEducationDataModel.OrderByLatestEducation());
			Collections.reverse(responseList);

			responseList
					.stream()
					.filter(x -> !StringUtils.isEmpty(x.getDescription()))
					.forEach(
							response -> {
								String formattedResponse = StringUtility
										.replaceEndOfLineWithBreakLine(response
												.getDescription());
								response.setDescription(formattedResponse);
							});

		}
	}

}
