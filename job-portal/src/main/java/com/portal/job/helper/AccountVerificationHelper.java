package com.portal.job.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.portal.job.constants.JobPortalConstants;
import com.portal.job.enums.EmailType;
import com.portal.job.enums.UserType;
import com.portal.job.model.BasicAccountDataModel;
import com.portal.job.service.BasicAccountDetailService;

/**
 * @author behinddwalls
 *
 */
public final class AccountVerificationHelper {

	public static BasicAccountDataModel getBasicAccountDataModelForAccountVerification(
			final Long accountId, final String verificationKey,
			final BasicAccountDetailService accountDetailService,
			final UserType userType) throws UnsupportedEncodingException {
		final String key = URLDecoder.decode(verificationKey,
				JobPortalConstants.UTF_8);
		final BasicAccountDataModel accountDataModel = accountDetailService
				.getAccountDataModelForRegistrationVerifcation(accountId, key,
						userType);
		return accountDataModel;
	}

	public static BasicAccountDataModel getBasicAccountDataModelForMailVerification(
			final Long accountId, final String verificationKey,
			final EmailType emailType,
			final BasicAccountDetailService accountDetailService)
			throws UnsupportedEncodingException {
		final String key = URLDecoder.decode(verificationKey,
				JobPortalConstants.UTF_8);
		final BasicAccountDataModel accountDataModel = accountDetailService
				.getAccountDataModelForEmailVerifcationInTransaction(accountId,
						key, emailType);
		return accountDataModel;
	}

}
