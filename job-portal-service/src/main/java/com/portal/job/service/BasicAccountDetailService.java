package com.portal.job.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cribbstechnologies.clients.mandrill.exception.RequestFailedException;
import com.google.common.base.Preconditions;
import com.googlecode.genericdao.search.Search;
import com.portal.job.constants.JobPortalConstants;
import com.portal.job.dao.BasicAccountDetailDao;
import com.portal.job.dao.UserDetailDao;
import com.portal.job.dao.model.BasicAccountDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.enums.EmailType;
import com.portal.job.enums.UserType;
import com.portal.job.exceptions.InvalidRequestException;
import com.portal.job.mapper.BasicAccountDetailDataMapper;
import com.portal.job.mapper.UserDetailMapper;
import com.portal.job.model.BasicAccountDataModel;
import com.portal.job.model.ExpertRegisterDataModel;
import com.portal.job.model.JobseekerRegisterDataModel;
import com.portal.job.model.RecruiterRegisterDataModel;
import com.portal.job.model.UserContactDataModel;
import com.portal.job.model.mail.MandrillSendMailInput;
import com.portal.job.services.mail.JobPortalMailService;
import com.portal.job.utils.KeyGeneratorUtility;

@Service
public class BasicAccountDetailService {
    private static final String fakePass = "test123";

    @Autowired
    private BasicAccountDetailDao basicAccountDetailDao;
    @Autowired
    private BasicAccountDetailDataMapper accountDetailMapper;
    @Autowired
    private UserDetailDao userDetailDao;
    @Autowired
    private UserDetailMapper jobseekerDetailMapper;
    @Autowired
    private JobPortalMailService jobPortalMailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserDetailService userDetailService;

    private static final Logger log = LoggerFactory.getLogger(BasicAccountDetailService.class);

    @Transactional
    public BasicAccountDataModel getAccountDataModelByEmailIdAndUserType(final String emailId, final UserType userType) {

        final String EMAIL_ID = UserType.RECRUITER.equals(userType) ? "workEmailId" : "emailId";
        final List<BasicAccountDetail> accountDetails = basicAccountDetailDao.getEntitiesByPropertyValue(
                new HashMap<String, String>() {
                    private static final long serialVersionUID = 1L;
                    {
                        put(EMAIL_ID, emailId);
                    }
                }, BasicAccountDetail.class);

        return (accountDetails == null || accountDetails.isEmpty()) ? null : accountDetailMapper
                .getAccountDataModel(accountDetails.get(0));
    }

    @Transactional
    public BasicAccountDataModel getAccountDataModelByAccountId(final Long accountId) {

        Preconditions.checkNotNull(accountId, "Account Id can not be null");
        return accountDetailMapper.getAccountDataModel(basicAccountDetailDao.find(accountId));
    }

    @Transactional
    public UserContactDataModel getUserContactDataModel(@NotNull Long userId) {
        UserContactDataModel userContactDataModel = new UserContactDataModel();
        UserDetail userDetail = userDetailDao.find(userId);
        BasicAccountDetail basicAccountDetail = userDetail.getBasicAccountDetail();

        userContactDataModel.setEmailId(basicAccountDetail.getEmailId());
        userContactDataModel.setWorkEmailId(basicAccountDetail.getWorkEmailId());
        userContactDataModel.setMobileNumber(userDetail.getMobileNumber());
        userContactDataModel.setOtherContactDetails(userDetail.getOtherContactNumbers());
        return userContactDataModel;
    }

    @SuppressWarnings("serial")
    @Transactional
    public BasicAccountDataModel getAccountDataModelForRegistrationVerifcation(final Long accountId,
            final String verificationKey, final UserType userType) {

        Preconditions.checkNotNull(accountId, "Account Id can not be null");
        Preconditions.checkNotNull(verificationKey, "Verification Key can not be null");

        List<BasicAccountDetail> basicAccountDetails = basicAccountDetailDao.getEntitiesByPropertyValue(
                new HashMap<String, Object>() {
                    {
                        String verificationKeyField = null;
                        if (UserType.JOBSEEKER.equals(userType)) {
                            verificationKeyField = "jobseekerVerificationKey";
                        } else if (UserType.RECRUITER.equals(userType)) {
                            verificationKeyField = "recruiterVerificationKey";
                        } else {
                            verificationKeyField = "expertVerificationKey";
                        }
                        put("accountId", accountId);
                        put(verificationKeyField, verificationKey);
                    }
                }, BasicAccountDetail.class);

        if (null != basicAccountDetails && !basicAccountDetails.isEmpty()) {
            return accountDetailMapper.getAccountDataModel(basicAccountDetails.get(0));
        }
        return null;
    }

    @SuppressWarnings("serial")
    @Transactional
    public BasicAccountDataModel getAccountDataModelForEmailVerifcationInTransaction(final Long accountId,
            final String verificationKey, final EmailType emailType) {

        Preconditions.checkNotNull(accountId, "Account Id can not be null");
        Preconditions.checkNotNull(verificationKey, "Verification Key can not be null");

        List<BasicAccountDetail> basicAccountDetails = basicAccountDetailDao.getEntitiesByPropertyValue(
                new HashMap<String, Object>() {
                    {
                        String verificationKeyField = null;
                        if (EmailType.WORK_EMAIL.equals(emailType)) {
                            verificationKeyField = "workEmailidVerificationKey";
                        } else {
                            verificationKeyField = "emailidVerificationKey";
                        }
                        put("accountId", accountId);
                        put(verificationKeyField, verificationKey);
                    }
                }, BasicAccountDetail.class);

        if (null != basicAccountDetails && !basicAccountDetails.isEmpty()) {
            return accountDetailMapper.getAccountDataModel(basicAccountDetails.get(0));
        }
        return null;
    }

    @Transactional
    public BasicAccountDataModel getAccountDataModelByAccountIdAndForgotPasswordKeyInTransaction(final Long accountId,
            final String verificationKey) {
        return getAccountDataModelByAccountIdAndForgotPasswordKey(accountId, verificationKey);
    }

    public BasicAccountDataModel getAccountDataModelByAccountIdAndForgotPasswordKey(final Long accountId,
            final String verificationKey) {

        Preconditions.checkNotNull(accountId, "Account Id can not be null");
        Preconditions.checkNotNull(verificationKey, "Verification Key can not be null");

        @SuppressWarnings("serial")
        List<BasicAccountDetail> basicAccountDetails = this.basicAccountDetailDao.getEntitiesByPropertyValue(
                new HashMap<String, Object>() {
                    {
                        put("accountId", accountId);
                        put("forgotPasswordKey", verificationKey);
                    }
                }, BasicAccountDetail.class);
        if (null != basicAccountDetails && !basicAccountDetails.isEmpty()) {
            return accountDetailMapper.getAccountDataModel(basicAccountDetails.get(0));
        }
        return null;
    }

    @Transactional
    public BasicAccountDataModel addOrUpdateAccountDetail(final BasicAccountDataModel basicAccountDataModel) {
        Preconditions.checkNotNull(basicAccountDataModel, "Account data can not be null");
        Preconditions.checkArgument(
                basicAccountDataModel.getAccountId() != null && basicAccountDataModel.getAccountId() != 0,
                "Account Id can not be null or zero");
        final BasicAccountDetail accountDetail = accountDetailMapper.getAccountDetail(basicAccountDataModel);
        basicAccountDetailDao.save(accountDetail);
        return accountDetailMapper.getAccountDataModel(accountDetail);
    }

    @Transactional
    public void registerJobseeker(final JobseekerRegisterDataModel jobseekerRegisterDataModel)
            throws NoSuchAlgorithmException {
        final BasicAccountDetail accountDetail = accountDetailMapper.getAccountDetail(jobseekerRegisterDataModel);
        this.basicAccountDetailDao.save(accountDetail);
        final UserDetail jobseekerDetail = jobseekerDetailMapper.getUserDetail(jobseekerRegisterDataModel);
        jobseekerDetail.setBasicAccountDetail(accountDetail);
        this.userDetailDao.save(jobseekerDetail);

        try {
            sendMail(
                    accountDetail.getEmailId(),
                    getVerificationLink(UserType.JOBSEEKER, accountDetail.getAccountId(),
                            accountDetail.getJobseekerVerificationKey()), getVerificationEmailMessage());
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to encode verificationKey ", e);
        }

    }

    @Transactional
    public void registerRecruiter(final RecruiterRegisterDataModel recruiterRegisterDataModel)
            throws NoSuchAlgorithmException {
        final BasicAccountDetail accountDetail = accountDetailMapper.getAccountDetail(recruiterRegisterDataModel);
        this.basicAccountDetailDao.save(accountDetail);
        final UserDetail jobseekerDetail = jobseekerDetailMapper.getUserDetail(recruiterRegisterDataModel);
        jobseekerDetail.setBasicAccountDetail(accountDetail);
        this.userDetailDao.save(jobseekerDetail);

        try {
            sendMail(
                    accountDetail.getWorkEmailId(),
                    getVerificationLink(UserType.RECRUITER, accountDetail.getAccountId(),
                            accountDetail.getRecruiterVerificationKey()), getVerificationEmailMessage());
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to encode verificationKey ", e);
        }

    }

    @Transactional
    public void registerExpert(final ExpertRegisterDataModel expertRegisterDataModel) throws NoSuchAlgorithmException {
        final BasicAccountDetail accountDetail = accountDetailMapper.getAccountDetail(expertRegisterDataModel);
        this.basicAccountDetailDao.save(accountDetail);
        final UserDetail jobseekerDetail = jobseekerDetailMapper.getUserDetail(expertRegisterDataModel);
        jobseekerDetail.setBasicAccountDetail(accountDetail);
        this.userDetailDao.save(jobseekerDetail);
        try {
            sendMail(
                    accountDetail.getEmailId(),
                    getVerificationLink(UserType.EXPERT, accountDetail.getAccountId(),
                            accountDetail.getExpertVerificationKey()), getVerificationEmailMessage());
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to encode verificationKey ", e);
        }
    }

    /**
     * Only for mock purposes, DO NOT USE OTHERWISE
     * 
     * @param jobseekerRegisterDataModel
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Transactional
    public UserDetail register(final JobseekerRegisterDataModel jobseekerRegisterDataModel)
            throws NoSuchAlgorithmException {
        final BasicAccountDetail accountDetail = accountDetailMapper.getAccountDetail(jobseekerRegisterDataModel);
        this.basicAccountDetailDao.save(accountDetail);
        final UserDetail jobseekerDetail = jobseekerDetailMapper.getUserDetail(jobseekerRegisterDataModel);
        jobseekerDetail.setBasicAccountDetail(accountDetail);
        this.userDetailDao.save(jobseekerDetail);
        return jobseekerDetail;
    }

    @Transactional
    public UserDetail registerNewUser(UserDetail userDetail) throws Exception {
        if (userDetail.getBasicAccountDetail() != null
                && !StringUtils.isEmpty(userDetail.getBasicAccountDetail().getEmailId())) {
            validateUserAlreadyNotPresent(userDetail.getBasicAccountDetail());
            BasicAccountDetail accountDetail = userDetail.getBasicAccountDetail();
            createAccountDetail(accountDetail);
            this.basicAccountDetailDao.save(accountDetail);
            userDetailService.saveNewUser(userDetail);
            return userDetail;
        } else
            throw new Exception("Email Address not found for resume");
    }

    private void validateUserAlreadyNotPresent(BasicAccountDetail basicAccountDetail) throws Exception {
        boolean isUserAlreadyPresent = isEmailAddressJobSeekerPresent(basicAccountDetail.getEmailId());
        if (isUserAlreadyPresent)
            throw new Exception("User Already present for emailId: " + basicAccountDetail.getEmailId());
    }

    private boolean isEmailAddressJobSeekerPresent(final String emailId) {
        Search search = new Search(BasicAccountDetail.class);
        search.addField("accountId");
        search.addFilterEqual("emailId", emailId);
        search.addFilterEqual("isJobseeker", (byte) 1);
        List<String> accountIds = basicAccountDetailDao.search(search);
        if (accountIds == null || accountIds.isEmpty())
            return false;
        return true;
    }

    private void createAccountDetail(BasicAccountDetail accountDetail) throws NoSuchAlgorithmException {
        accountDetail.setIsJobseeker((byte) 1);
        accountDetail.setPasswordHash(bCryptPasswordEncoder.encode(fakePass));
        accountDetail.setJobseekerVerificationKey(KeyGeneratorUtility.generateKey());
        accountDetail.setIsEmailidVerified((byte) 0);

        accountDetail.setCreateDate(new Date());
        accountDetail.setModeifiedDate(new Date());
    }

    public void sendMail(final String emailId, final String link, final String message) {

        try {

            final StringBuilder htmlMessage = new StringBuilder(message).append(" <br/>").append(link)
                    .append("<br/><br/>").append("Thanks").append("<br/>").append("EvenRank Support Center")
                    .append("<br/>").append("Email: support@evenrank.com");

            final MandrillSendMailInput input = new MandrillSendMailInput.Builder("EvenRank", "no-reply@evenrank.com")
                    .toEmailId(emailId).subject("EvenRank: Account Verification").htmlMessage(htmlMessage.toString())
                    .build();

            this.jobPortalMailService.sendMail(input);
        } catch (InvalidRequestException e) {
            log.error("Failed to send  mail ", e);
        } catch (RequestFailedException e) {
            log.error("Failed to send  mail ", e);
        }
    }

    public void sendForgotPasswordResetLink(final String emailId, final UserType userType, final Long accountId,
            final String verificationKey) {
        try {
            this.sendMail(emailId, getForgotPasswordLink(userType, accountId, verificationKey),
                    getForgotPasswordEmailMessage());
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to send  forgot password mail ", e);
        }
    }

    public void sendVerifyEmailLink(final String emailId, final EmailType emailType, final String verificationKey) {
        try {
            this.sendMail(emailId, getEmailVerificationLinkLink(emailType, verificationKey),
                    getEmailVeirifcationEmailMessage());
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to send  forgot password mail ", e);
        }
    }

    private String getVerificationLink(final UserType userType, Long accountId, String verificationKey)
            throws UnsupportedEncodingException {
        final StringBuilder verificationLinkBuilder = new StringBuilder("http://www.evenrank.com/signin");
        verificationLinkBuilder.append("/account/verify/").append(userType.getUserTypeName()).append("?accountId=")
                .append(accountId).append("&verificationKey=")
                .append(URLEncoder.encode(verificationKey, JobPortalConstants.UTF_8));
        return verificationLinkBuilder.toString();
    }

    private String getForgotPasswordLink(final UserType userType, Long accountId, String verificationKey)
            throws UnsupportedEncodingException {
        final StringBuilder verificationLinkBuilder = new StringBuilder("http://www.evenrank.com/");
        verificationLinkBuilder.append("signin/").append("forgot-password/reset").append("?verify=1&accountId=")
                .append(accountId).append("&key=").append(URLEncoder.encode(verificationKey, JobPortalConstants.UTF_8));
        return verificationLinkBuilder.toString();
    }

    private String getEmailVerificationLinkLink(final EmailType emailType, String verificationKey)
            throws UnsupportedEncodingException {
        final StringBuilder verificationLinkBuilder = new StringBuilder("http://www.evenrank.com/signin");
        verificationLinkBuilder.append("account/email/verify").append("?emailType=").append(emailType)
                .append("&verificationKey=").append(URLEncoder.encode(verificationKey, JobPortalConstants.UTF_8));
        return verificationLinkBuilder.toString();
    }

    private String getVerificationEmailMessage() {
        return "Please click on this link to verify your account.";
    }

    private String getForgotPasswordEmailMessage() {
        return "Please click on this link to reset your password.";
    }

    private String getEmailVeirifcationEmailMessage() {
        return "Please click on this link to verify your emailId.";
    }
}
