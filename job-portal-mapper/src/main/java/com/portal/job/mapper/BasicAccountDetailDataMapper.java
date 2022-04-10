package com.portal.job.mapper;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.portal.job.dao.model.BasicAccountDetail;
import com.portal.job.model.BasicAccountDataModel;
import com.portal.job.model.ExpertRegisterDataModel;
import com.portal.job.model.JobseekerRegisterDataModel;
import com.portal.job.model.RecruiterRegisterDataModel;
import com.portal.job.utils.KeyGeneratorUtility;

/**
 * @author preetam
 *
 */
@Component
public class BasicAccountDetailDataMapper {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public BasicAccountDataModel getAccountDataModel(
			final BasicAccountDetail accountDetail) {
		System.out.println(accountDetail);
		final BasicAccountDataModel basicAccountDataModel = new BasicAccountDataModel(
				accountDetail.getAccountId(), accountDetail.getEmailId(),
				accountDetail.getWorkEmailId(),
				accountDetail.getPasswordHash(),
				accountDetail.getForgotPasswordKey(),
				accountDetail.getForgotPasswordKeyGenerateTime(),
				accountDetail.getIsJobseeker() == 0 ? false : true,
				accountDetail.getIsRecruiter() == 0 ? false : true,
				accountDetail.getIsExpert() == 0 ? false : true,
				accountDetail.getJobseekerVerificationKey(),
				accountDetail.getRecruiterVerificationKey(),
				accountDetail.getExpertVerificationKey(),
				accountDetail.getCreateDate(),
				accountDetail.getModeifiedDate(),
				accountDetail.getEmailidVerificationKey(),
				accountDetail.getWorkEmailidVerificationKey(),
				accountDetail.getIsEmailidVerified() == 0 ? false : true,
				accountDetail.getIsWorkEmailidVerified() == 0 ? false : true,
				accountDetail.getNewUnverifiedEmailId(),
				accountDetail.getNewUnverfiedWorkEmailId());

		return basicAccountDataModel;

	}

	public BasicAccountDetail getAccountDetail(
			final BasicAccountDataModel basicAccountDataModel) {

		final BasicAccountDetail accountDetail = new BasicAccountDetail(
				basicAccountDataModel.getEmailId(),
				basicAccountDataModel.getWorkEmailId(),
				basicAccountDataModel.getPasswordHash(),
				basicAccountDataModel.getForgotPasswordKey(),
				basicAccountDataModel.getForgotPasswordKeyGenerateTime(),
				basicAccountDataModel.isJobseeker() ? (byte) 1 : (byte) 0,
				basicAccountDataModel.isRecruiter() ? (byte) 1 : (byte) 0,
				basicAccountDataModel.isExpert() ? (byte) 1 : (byte) 0,
				basicAccountDataModel.getJobseekerVerificationKey(),
				basicAccountDataModel.getRecruiterVerificationKey(),
				basicAccountDataModel.getExpertVerificationKey(),
				basicAccountDataModel.getCreateDate(),
				basicAccountDataModel.getModifiedDate(),
				basicAccountDataModel.getEmailIdVerificationKey(),
				basicAccountDataModel.getWorkEmailIdVerificationKey(),
				basicAccountDataModel.isEmailIdVerified() ? (byte) 1 : (byte) 0,
				basicAccountDataModel.isWorkEmailIdVerified() ? (byte) 1
						: (byte) 0, basicAccountDataModel
						.getNewUnverifiedEmailId(), basicAccountDataModel
						.getNewUnverfiedWorkEmailId(), null);
		accountDetail.setAccountId(basicAccountDataModel.getAccountId());

		return accountDetail;
	}

	public BasicAccountDetail getAccountDetail(
			final JobseekerRegisterDataModel jobseekerRegisterDataModel)
			throws NoSuchAlgorithmException {
		final BasicAccountDetail accountDetail = new BasicAccountDetail();

		accountDetail.setEmailId(jobseekerRegisterDataModel.getEmailId());
		accountDetail.setPasswordHash(bCryptPasswordEncoder
				.encode(jobseekerRegisterDataModel.getPassword()));
		accountDetail.setJobseekerVerificationKey(KeyGeneratorUtility
				.generateKey());
		accountDetail.setIsEmailidVerified((byte) 0);

		accountDetail.setCreateDate(new Date());
		accountDetail.setModeifiedDate(new Date());
		return accountDetail;
	}

	public BasicAccountDetail getAccountDetail(
			final ExpertRegisterDataModel expertRegisterDataModel)
			throws NoSuchAlgorithmException {
		final BasicAccountDetail accountDetail = new BasicAccountDetail();
		accountDetail.setEmailId(expertRegisterDataModel.getEmailId());
		accountDetail.setPasswordHash(bCryptPasswordEncoder
				.encode(expertRegisterDataModel.getPassword()));
		accountDetail.setExpertVerificationKey(KeyGeneratorUtility
				.generateKey());
		accountDetail.setIsEmailidVerified((byte) 0);

		accountDetail.setCreateDate(new Date());
		accountDetail.setModeifiedDate(new Date());
		return accountDetail;
	}

	public BasicAccountDetail getAccountDetail(
			final RecruiterRegisterDataModel recruiterRegisterDataModel)
			throws NoSuchAlgorithmException {

		final BasicAccountDetail accountDetail = new BasicAccountDetail();

		accountDetail.setWorkEmailId(recruiterRegisterDataModel
				.getWorkEmailId());
		accountDetail.setEmailId(recruiterRegisterDataModel.getEmailId());
		accountDetail.setPasswordHash(bCryptPasswordEncoder
				.encode(recruiterRegisterDataModel.getPassword()));
		accountDetail.setRecruiterVerificationKey(KeyGeneratorUtility
				.generateKey());
		accountDetail.setIsWorkEmailidVerified((byte) 0);

		accountDetail.setCreateDate(new Date());
		accountDetail.setModeifiedDate(new Date());
		return accountDetail;
	}
}
