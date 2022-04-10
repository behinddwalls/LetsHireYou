package com.portal.job.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Search;
import com.portal.job.dao.OrganisationDetailDao;
import com.portal.job.dao.model.OrganisationDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.mapper.UserOrganisationalDetailMapper;
import com.portal.job.model.OrganisationDataModel;

@Service
public class OrganisationDetailService {
    @Autowired
    private UserOrganisationalDetailMapper jobseekerOrganisationalDetailMapper;

    @Autowired
    private OrganisationDetailDao organisaztionDetailDao;

    @Transactional
    public int getOrganisationTier(String orgName) {
        if (orgName == null)
            return 0;
        Search search = new Search(OrganisationDetail.class);

        search.addFilterILike("organisationName", orgName);
        List<OrganisationDetail> orgDetails = organisaztionDetailDao.getEntities(search);
        if (orgDetails.isEmpty())
            return 0;
        return orgDetails.get(0).getOrganisationTier();
    }

    /**
     * 
     * @param orgName
     * @return Serach the near matching Organisation Names.
     */
    @Transactional
    public Set<String> serachOrganisationByName(final String orgName) {
        final List<OrganisationDetail> organisationDetails = organisaztionDetailDao.getEntitiesSimilarToPropertyValue(
                new HashMap<String, String>() {
                    private static final long serialVersionUID = 1L;
                    {
                        put("organisationName", "%" + orgName + "%");
                    }
                }, OrganisationDetail.class);

        return getOrganisationNameSet(organisationDetails);
    }

    /**
     * 
     * @return Give the Set of getAllOrganisation Name peresent in the DB.
     */
    @Transactional
    public Set<String> getAllOrganisation() {
        List<OrganisationDetail> organisationDetails = this.organisaztionDetailDao.findAll();
        return getOrganisationNameSet(organisationDetails);
    }

    // /////// ========================= start private Methods
    // ===================================
    private Set<String> getOrganisationNameSet(List<OrganisationDetail> organisationDetails) {
        final Set<String> orgNameSet = new HashSet<String>();
        for (OrganisationDetail organisationDetail : organisationDetails) {
            orgNameSet.add(WordUtils.capitalize(organisationDetail.getOrganisationName()));
        }
        return orgNameSet;
    }

    // /////// ========================== End
    // ======================================================

    /**
     * 
     * @param jobSeekerId
     * @return It returns the list of All Organisation details Associated for
     *         the Particular JobSeekerId.
     */
    @Transactional
    public List<OrganisationDataModel> getJobSeekerOrganisationDataModelForJobSeekerId(@NotNull final Long jobSeekerId) {
        final UserDetail JobseekerDetail = new UserDetail();
        JobseekerDetail.setUserId(jobSeekerId);
        return this.jobseekerOrganisationalDetailMapper
                .getJobSeekerOrganisationDataModelList(this.organisaztionDetailDao.getEntitiesByPropertyValue(
                        new HashMap<String, UserDetail>() {
                            private static final long serialVersionUID = 1L;

                            {
                                put("userDetail", JobseekerDetail);
                            }
                        }, OrganisationDetail.class));
    }

    /**
     * 
     * @param jobSeekerId
     * @return Add the project detail and associates it with the
     *         'JobSeekerDetail'
     */
    @Transactional
    public OrganisationDetail addOrUpdateJobseekerRoleDetaill(
            final OrganisationDataModel jobSeekerOrganisationDataModel, @NotNull final Long jobSeekerId)
            throws ParseException {
        final OrganisationDetail organisationDetail = jobseekerOrganisationalDetailMapper.getOrganisationalDetail(
                jobSeekerOrganisationDataModel, jobSeekerId);
        // this.createOrUpdateEntity(organisationDetail);
        return organisationDetail;
    }

    /**
     * 
     * @param jobSeekerId
     * @return remove the Particular Project Associated with the JobSeeker
     */
    @Transactional
    public boolean deleteRoleDetaillForJobSeekerId(final OrganisationDataModel jobSeekerOrganisationDataModel,
            @NotNull final Long jobSeekerId) {
        final OrganisationDetail organisationDetail = jobseekerOrganisationalDetailMapper.getOrganisationalDetail(
                jobSeekerOrganisationDataModel, jobSeekerId);
        // return this.deleteEntity(organisationDetail);
        return true;
    }

}