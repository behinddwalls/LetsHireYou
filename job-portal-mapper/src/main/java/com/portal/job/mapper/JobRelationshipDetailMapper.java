package com.portal.job.mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;
import com.portal.job.dao.model.JobDetail;
import com.portal.job.dao.model.JobRelationshipDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.enums.JobApplicationStatus;
import com.portal.job.model.JobRelationshipDataModel;

@Component
public class JobRelationshipDetailMapper {

    @Autowired
    private UserDetailMapper userDetailMapper;
    @Autowired
    private JobDetailMapper jobDetailMapper;

    public JobRelationshipDetail getEntityFromDataModel(final JobRelationshipDataModel jobRelationshipDataModel)
            throws Exception {

        // prepare job detail
        final JobDetail jobDetail = new JobDetail();
        jobDetail.setJobId(jobRelationshipDataModel.getJobId());

        // prepare jobseeker
        final UserDetail jobseekerDetail = new UserDetail();
        jobseekerDetail.setUserId(jobRelationshipDataModel.getJobseekerId());

        // // prepare expert
        UserDetail expertDetail = null;
        if (jobRelationshipDataModel.getExpertId() != null && jobRelationshipDataModel.getExpertId().longValue() != 0) {
            expertDetail = new UserDetail();
            expertDetail.setUserId(jobRelationshipDataModel.getExpertId());
        }
        if (StringUtils.isEmpty(jobRelationshipDataModel.getRecruiterId()))
            throw new Exception("RecruiterId cannot be empty");
        UserDetail recruiterDetail = new UserDetail();
        recruiterDetail.setUserId(jobRelationshipDataModel.getRecruiterId());

        JobRelationshipDetail jobRelationshipDetail = new JobRelationshipDetail(jobDetail, expertDetail,
                recruiterDetail, jobseekerDetail, jobRelationshipDataModel.getJobApplicationStatus() == null ? null
                        : jobRelationshipDataModel.getJobApplicationStatus().name(),
                jobRelationshipDataModel.getRecruiterApplicationStatus() == null ? null : jobRelationshipDataModel
                        .getRecruiterApplicationStatus().name(),
                jobRelationshipDataModel.getJobSeekerApplicationStatus() == null ? null : jobRelationshipDataModel
                        .getJobSeekerApplicationStatus().name(), null,
                jobRelationshipDataModel.getExpertApplicationStatus() == null ? null : jobRelationshipDataModel
                        .getExpertApplicationStatus().name(), jobRelationshipDataModel.getExpertAssessment(),
                jobRelationshipDataModel.isInJobseekerWishlist() ? (byte) 1 : (byte) 0,
                jobRelationshipDataModel.isInRecruiterWishlist() ? (byte) 1 : (byte) 0, new Date(), new Date());

        jobRelationshipDetail.setJobRelationshipId(jobRelationshipDataModel.getJobRelationshipId());

        return jobRelationshipDetail;
    }

    public JobRelationshipDataModel getDataModelFromEntity(final JobRelationshipDetail jobRelationshipDetail) {

        JobRelationshipDataModel jobRelationshipDataModel = new JobRelationshipDataModel(
                jobRelationshipDetail.getJobRelationshipId(), jobRelationshipDetail.getJobDetail().getJobId(),
                (jobRelationshipDetail.getUserDetailByExpertId() == null) ? 0 : jobRelationshipDetail
                        .getUserDetailByExpertId().getUserId(), jobRelationshipDetail.getUserDetailByJobseekerId()
                        .getUserId(), jobRelationshipDetail.getUserDetailByRecruiterId().getUserId(),
                jobRelationshipDetail.getJobApplicationStatus() == null ? null : JobApplicationStatus
                        .valueOf(jobRelationshipDetail.getJobApplicationStatus()),
                jobRelationshipDetail.getRecruiterApplicationStatus() == null ? null : JobApplicationStatus
                        .valueOf(jobRelationshipDetail.getRecruiterApplicationStatus()),
                jobRelationshipDetail.getJobSeekerApplicationStatus() == null ? null : JobApplicationStatus
                        .valueOf(jobRelationshipDetail.getJobSeekerApplicationStatus()),
                jobRelationshipDetail.getExpertApplicationStatus() == null ? null : JobApplicationStatus
                        .valueOf(jobRelationshipDetail.getExpertApplicationStatus()),
                jobRelationshipDetail.getRejectedDate(), jobRelationshipDetail.getExpertAssessment(),
                jobRelationshipDetail.getIsInJobseekerWishlist() == 1 ? true : false,
                jobRelationshipDetail.getIsInRecruiterWishlist() == 1 ? true : false,
                jobRelationshipDetail.getCreateDate(), jobRelationshipDetail.getModifiedDate());

        return jobRelationshipDataModel;
    }

    public Set<JobRelationshipDataModel> getDataModelsFromEntities(
            final List<JobRelationshipDetail> jobRelationshipDetails) {
        final Set<JobRelationshipDataModel> jobRelationshipDataModels = Sets.newHashSet();

        for (final JobRelationshipDetail jobRelationshipDetail : jobRelationshipDetails) {
            jobRelationshipDataModels.add(this.getDataModelFromEntity(jobRelationshipDetail));
        }
        return jobRelationshipDataModels;
    }

    public Set<JobRelationshipDetail> getEntitiesFromDataModels(
            final List<JobRelationshipDataModel> jobRelationshipDataModels) throws Exception {
        final Set<JobRelationshipDetail> jobRelationshipDetails = Sets.newHashSet();
        for (final JobRelationshipDataModel jobRelationshipDataModel : jobRelationshipDataModels) {
            jobRelationshipDetails.add(this.getEntityFromDataModel(jobRelationshipDataModel));
        }
        return jobRelationshipDetails;
    }
}
