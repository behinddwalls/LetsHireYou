package com.portal.job.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.job.dao.UserExperienceDetailDao;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserExperinceDetail;
import com.portal.job.mapper.UserExperienceDetailsMapper;
import com.portal.job.model.ExperienceProcessedData;
import com.portal.job.model.UserExperienceDataModel;
import com.portal.job.utils.DateUtility;
import com.portal.job.utils.WorkExperienceUtitlity;

@Service
public class UserExperienceDetailsService {
    private static Logger log = LoggerFactory.getLogger(UserEducationDetailsService.class);

    @Autowired
    private UserExperienceDetailDao jobSeekerExperienceDetailsDao;
    @Autowired
    UserExperienceDetailsMapper jsExperienceDetailMapper;
    @Autowired
    OrganisationDetailService orgDetailService;

    @Transactional
    public ExperienceProcessedData getExperienceProcessedData(@NotNull Long jsId) throws Exception {
        List<UserExperinceDetail> experinceDetails = getUserExperienceDetails(jsId);
        List<WorkExperienceUtitlity.ExperiencePeriod> expPeriodList = experinceDetails
                .stream()
                .filter(exp -> (exp.getTimePeriodStart() != null && exp.getTimePeriodEnd() != null))
                .map(exp -> new WorkExperienceUtitlity.ExperiencePeriod(exp.getTimePeriodStart(), exp
                        .getTimePeriodEnd())).collect(Collectors.toList());
        Optional<UserExperinceDetail> currentExp = experinceDetails.stream()
                .filter(exp -> exp.getIsCurrentJob() != null && exp.getIsCurrentJob() == (byte) 1).findFirst();
        if (currentExp.isPresent() && currentExp.get().getTimePeriodStart() != null) {
            expPeriodList.add(new WorkExperienceUtitlity.ExperiencePeriod(currentExp.get().getTimePeriodStart(),
                    new Date()));
        }
        int totalExp = WorkExperienceUtitlity.getWorkExperienceInMonths(expPeriodList);
        // int totalExp = 0;
        // for(UserExperinceDetail exp: experinceDetails){
        // totalExp = totalExp + exp.getTotalExpMonth();
        // }

        String latestCompanyName = null;
        String latestJobRole = null;
        if (currentExp.isPresent()) {
            latestCompanyName = currentExp.get().getCompanyName();
            latestJobRole = currentExp.get().getRoleName();
        } else {
            Optional<UserExperinceDetail> latestNonCurrentExp = experinceDetails.stream()
                    .filter(exp -> exp.getTimePeriodEnd() != null)
                    .sorted((t1, t2) -> -1 * t1.getTimePeriodEnd().compareTo( // sort
                                                                              // in
                                                                              // descending
                                                                              // order
                            t2.getTimePeriodEnd())).findFirst();
            if (latestNonCurrentExp.isPresent()) {
                latestCompanyName = latestNonCurrentExp.get().getCompanyName();
                latestJobRole = latestNonCurrentExp.get().getRoleName();
            }

        }
        log.info("zzzzz total Exp = " + totalExp);
        final ExperienceProcessedData experienceProcessedData = new ExperienceProcessedData(totalExp,
                Optional.ofNullable(latestCompanyName));
        experienceProcessedData.setLatestJobTitle(Optional.ofNullable(latestJobRole));
        return experienceProcessedData;
    }

    @Transactional
    public boolean[] saveUserExperienceDetails(List<UserExperinceDetail> userExpDetails) {
        for (UserExperinceDetail exp : userExpDetails) {
            int tier = orgDetailService.getOrganisationTier(exp.getCompanyName());
            exp.setOrganisationTier(tier);
            int totalExpMonth = 0;
            if (exp.getTimePeriodStart() != null) {
                if (exp.getIsCurrentJob() != null && exp.getIsCurrentJob() == 1)
                    totalExpMonth = DateUtility.getDifferenceInMonths(exp.getTimePeriodStart(), new Date());
                else if (exp.getTimePeriodEnd() != null)
                    totalExpMonth = DateUtility.getDifferenceInMonths(exp.getTimePeriodStart(), exp.getTimePeriodEnd());
            }
            exp.setTotalExpMonth(totalExpMonth);
        }
        return jobSeekerExperienceDetailsDao.save(userExpDetails.toArray(new UserExperinceDetail[0]));
    }

    @Transactional
    public UserExperienceDataModel createOrUpdateInTransaction(final UserExperienceDataModel experienceData,
            final Long jsId) throws ParseException {
        return createOrUpdate(experienceData, jsId);
    }

    @Transactional
    public UserExperienceDataModel createOrUpdate(final UserExperienceDataModel experienceData, final Long jsId)
            throws ParseException {
        UserExperinceDetail jsExperienceDetail = jsExperienceDetailMapper.getEntityFromDataModel(experienceData, jsId);
        int tier = orgDetailService.getOrganisationTier(jsExperienceDetail.getCompanyName());
        jsExperienceDetail.setOrganisationTier(tier);
        int totalExpMonth = 0;
        if (jsExperienceDetail.getTimePeriodStart() != null) {
            if (jsExperienceDetail.getIsCurrentJob() != null && jsExperienceDetail.getIsCurrentJob() == 1)
                totalExpMonth = DateUtility.getDifferenceInMonths(jsExperienceDetail.getTimePeriodStart(), new Date());
            else if (jsExperienceDetail.getTimePeriodEnd() != null)
                totalExpMonth = DateUtility.getDifferenceInMonths(jsExperienceDetail.getTimePeriodStart(),
                        jsExperienceDetail.getTimePeriodEnd());
        }
        jsExperienceDetail.setTotalExpMonth(totalExpMonth);
        boolean isNew = jobSeekerExperienceDetailsDao.save(jsExperienceDetail);
        log.info("zzzzzz experience " + jsExperienceDetail.getExperinceId());
        if (isNew) {
            log.info("New educationDetail added for JSId " + jsId);
        } else {
            log.info("Updated educationDetail added for JSId " + jsId);
        }
        UserExperienceDataModel experienceDataResponse = jsExperienceDetailMapper
                .getDataModelFromEntity(jsExperienceDetail);
        log.info("zzzz expDataOutPut= " + experienceDataResponse.toString());
        return experienceDataResponse;

    }

    @Transactional
    public boolean delete(final UserExperienceDataModel experienceData, final Long jsId) throws ParseException {
        boolean isRemoved = jobSeekerExperienceDetailsDao.remove(jsExperienceDetailMapper.getEntityFromDataModel(
                experienceData, jsId));
        if (isRemoved) {
            log.info("educationData removed for jsId" + jsId);
        } else {
            log.info("unable to delete educationData");
        }
        return isRemoved;
    }

    @Transactional
    public Set<UserExperienceDataModel> getAllExperienceDetails() {
        Set<UserExperinceDetail> jsEducationdetailList = new HashSet<UserExperinceDetail>(
                jobSeekerExperienceDetailsDao.findAll());
        return jsExperienceDetailMapper.getDataModelSetFromEntitySet(jsEducationdetailList);
    }

    @Transactional
    public List<UserExperinceDetail> getUserExperienceDetails(@NotNull final Long jsId) {
        final UserDetail userDetail = new UserDetail();
        userDetail.setUserId(jsId);
        return jobSeekerExperienceDetailsDao.getEntitiesByPropertyValue(new HashMap<String, UserDetail>() {
            private static final long serialVersionUID = 1L;

            {
                put("userDetail", userDetail);
            }
        }, UserExperinceDetail.class);
    }

    @Transactional
    public Set<UserExperienceDataModel> getExperienceDetailByUserId(@NotNull final Long jsId) {
        final UserDetail userDetail = new UserDetail();
        userDetail.setUserId(jsId);
        Set<UserExperinceDetail> jsExperienceDetails = new HashSet<UserExperinceDetail>(
                jobSeekerExperienceDetailsDao.getEntitiesByPropertyValue(new HashMap<String, UserDetail>() {
                    private static final long serialVersionUID = 1L;

                    {
                        put("userDetail", userDetail);
                    }
                }, UserExperinceDetail.class));

        return jsExperienceDetailMapper.getDataModelSetFromEntitySet(jsExperienceDetails);
    }

    @Transactional
    public UserExperienceDataModel getExperienceDetailByExpId(@NotNull final Long jobExpId) {
        UserExperinceDetail jobExp = new UserExperinceDetail();
        jobExp = jobSeekerExperienceDetailsDao.find(jobExpId);
        return jsExperienceDetailMapper.getDataModelFromEntity(jobExp);

    }

}
