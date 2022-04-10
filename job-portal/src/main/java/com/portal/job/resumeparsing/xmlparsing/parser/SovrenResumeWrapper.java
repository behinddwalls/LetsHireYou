package com.portal.job.resumeparsing.xmlparsing.parser;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.common.base.Joiner;
import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.BasicAccountDetail;
import com.portal.job.dao.model.UserCertificationDetail;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserEducationDetail;
import com.portal.job.dao.model.UserExperinceDetail;
import com.portal.job.dao.model.UserPatentDetail;
import com.portal.job.dao.model.UserPublicationDetail;
import com.portal.job.dao.model.UserVolunteerDetail;
import com.portal.job.enums.CountryCodes;
import com.portal.job.mapper.resumeparsing.xmlparsing.SovrenToJobseekerModelMapper;
import com.portal.job.resumeparsing.xmlparsing.model.ArticleType;
import com.portal.job.resumeparsing.xmlparsing.model.BookType;
import com.portal.job.resumeparsing.xmlparsing.model.CompetencyType;
import com.portal.job.resumeparsing.xmlparsing.model.CompetencyType.CompetencyEvidence;
import com.portal.job.resumeparsing.xmlparsing.model.ConferencePaperType;
import com.portal.job.resumeparsing.xmlparsing.model.ContactMethodType;
import com.portal.job.resumeparsing.xmlparsing.model.CredentialType;
import com.portal.job.resumeparsing.xmlparsing.model.EduDegreeType;
import com.portal.job.resumeparsing.xmlparsing.model.EducationHistoryType;
import com.portal.job.resumeparsing.xmlparsing.model.EmployerOrgDataType.PositionHistory;
import com.portal.job.resumeparsing.xmlparsing.model.EmploymentHistoryType;
import com.portal.job.resumeparsing.xmlparsing.model.FlexibleDatesType;
import com.portal.job.resumeparsing.xmlparsing.model.LicensesAndCertifications;
import com.portal.job.resumeparsing.xmlparsing.model.OtherPublicationType;
import com.portal.job.resumeparsing.xmlparsing.model.PatentDescriptionType;
import com.portal.job.resumeparsing.xmlparsing.model.PatentHistoryType;
import com.portal.job.resumeparsing.xmlparsing.model.PatentStatusTypeTypes;
import com.portal.job.resumeparsing.xmlparsing.model.PositionTypes;
import com.portal.job.resumeparsing.xmlparsing.model.PostalAddressType;
import com.portal.job.resumeparsing.xmlparsing.model.PublicationHistoryType;
import com.portal.job.resumeparsing.xmlparsing.model.Resume;
import com.portal.job.resumeparsing.xmlparsing.model.SchoolOrInstitutionType;
import com.portal.job.resumeparsing.xmlparsing.model.StructuredXMLResumeType;
import com.portal.job.resumeparsing.xmlparsing.model.StructuredXMLResumeType.Qualifications;
import com.portal.job.resumeparsing.xmlparsing.model.TelcomNumberType;
import com.portal.job.resumeparsing.xmlparsing.model.UserAreaType;
import com.portal.job.resumeparsing.xmlparsing.userArea.model.MaritalStatusEnum;
import com.portal.job.resumeparsing.xmlparsing.userArea.model.ResumeUserArea;
import com.portal.job.utils.DateUtility;
import com.portal.job.utils.DateUtility.DateBuilder;

/**
 * TODO: Award details, Project Details
 * 
 * @author abbhasin
 *
 */

public class SovrenResumeWrapper {
    private final Resume resume;
    private Optional<StructuredXMLResumeType> structuredResume;
    private Optional<UserAreaType> userAreaType;

    private static BigDecimal LAKH = new BigDecimal(100000);

    private static Logger log = LoggerFactory.getLogger(SovrenResumeWrapper.class);

    public SovrenResumeWrapper(Resume resume) {
        this.resume = resume;
        structuredResume = Optional.ofNullable(resume.getStructuredXMLResume());
        userAreaType = Optional.ofNullable(resume.getUserArea());
    }

    public Optional<UserDetail> getUserBasicDetails() {
        if (!structuredResume.isPresent())
            return Optional.empty();
        UserDetail userDetail = new UserDetail();
        getUserContactDetails(userDetail);
        getUserSkillDetails(userDetail);
        getUserSkillDetailsInWorkHistory(userDetail);
        fillPersonalInformation(userDetail);

        return Optional.of(userDetail);
    }

    private void getUserContactDetails(UserDetail user) {

        List<ContactMethodType> contactMethodList = structuredResume.get().getContactInfo().getContactMethod();
        for (ContactMethodType contactMethod : contactMethodList) {
            getFormatMobileNumber(contactMethod.getMobile()).ifPresent(
                    mobileNumber -> user.setMobileNumber(mobileNumber));
            getTelephoneNumber(contactMethod.getTelephone()).ifPresent(tel -> {
                StringBuilder b = new StringBuilder();
                if (!StringUtils.isEmpty(user.getOtherContactNumbers())) {
                    b.append(user.getOtherContactNumbers());
                }
                b.append(tel);
                user.setOtherContactNumbers(b.toString());
            });
            getLocationDetails(contactMethod.getPostalAddress()).ifPresent(address -> user.setAddress(address));
            if (!StringUtils.isEmpty(contactMethod.getInternetEmailAddress())) {
                BasicAccountDetail accountDetail = new BasicAccountDetail();
                accountDetail.setEmailId(contactMethod.getInternetEmailAddress());
                user.setBasicAccountDetail(accountDetail);

                if (structuredResume.get().getContactInfo().getPersonName() != null) {

                    if (structuredResume.get().getContactInfo().getPersonName().getGivenName() != null
                            && !structuredResume.get().getContactInfo().getPersonName().getGivenName().isEmpty()) {
                        user.setFirstName(structuredResume.get().getContactInfo().getPersonName().getGivenName().get(0));
                    }
                    if (structuredResume.get().getContactInfo().getPersonName().getFamilyName() != null
                            && !structuredResume.get().getContactInfo().getPersonName().getFamilyName().isEmpty()) {
                        user.setLastName(structuredResume.get().getContactInfo().getPersonName().getFamilyName().get(0)
                                .getValue());
                    }
                }
                if (StringUtils.isEmpty(user.getFirstName()) && StringUtils.isEmpty(user.getFirstName())) {
                    user.setFirstName(accountDetail.getEmailId().split("@")[0]);
                }

            }

        }
    }

    private void fillPersonalInformation(UserDetail userDetail) {
        if (!userAreaType.isPresent())
            return;
        log.info("zzzzzz userArea: " + userAreaType.get().toString());
        log.info("zzzzzz userArea element: " + userAreaType.get().getAny().get(0));
        for (Object obj : userAreaType.get().getAny()) {
            log.info("zzzzz object = " + obj);
        }
        ResumeUserArea resumeUserArea = (ResumeUserArea) userAreaType.get().getAny().get(0);
        log.info("zzzzz resumeUserArea = " + resumeUserArea.getExperienceSummary().getDescription());
        if (resumeUserArea.getExperienceSummary() != null) {
            userDetail.setSummary(resumeUserArea.getExperienceSummary().getDescription());
        }
        if (resumeUserArea.getPersonalInformation() != null) {
            if (resumeUserArea.getPersonalInformation().getCurrentSalary() != null
                    && resumeUserArea.getPersonalInformation().getCurrentSalary().getValue() != null) {
                userDetail.setCtc(resumeUserArea.getPersonalInformation().getCurrentSalary().getValue().divide(LAKH)
                        .intValue());
            }
            if (resumeUserArea.getPersonalInformation().getGender() != null) {
                userDetail.setGender(SovrenToJobseekerModelMapper.getGenderMap()
                        .get(resumeUserArea.getPersonalInformation().getGender().getValue()).name());
            }
            if (resumeUserArea.getPersonalInformation().getMaritalStatus() != null
                    && resumeUserArea.getPersonalInformation().getMaritalStatus().getValue() != MaritalStatusEnum.UNKNOWN) {
                userDetail.setMaritalStatus(SovrenToJobseekerModelMapper.getMaritalStatusMap()
                        .get(resumeUserArea.getPersonalInformation().getMaritalStatus().getValue()).name());
            }

        }

    }

    private void getUserSkillDetails(UserDetail user) {
        Qualifications qualifications = structuredResume.get().getQualifications();
        if (qualifications == null)
            return;
        List<String> skills = qualifications.getCompetency().stream()
                .map(competency -> getUserSkillDetailFromCompetency(competency, "SKILLS"))
                .filter(optional -> optional.isPresent()).map(optional -> optional.get()).collect(Collectors.toList());
        if (!skills.isEmpty())
            user.setSkills("," + Joiner.on(",").join(skills) + ",");
        return;
    }

    private void getUserSkillDetailsInWorkHistory(UserDetail user) {
        Qualifications qualifications = structuredResume.get().getQualifications();
        if (qualifications == null)
            return;
        List<String> skills = qualifications.getCompetency().stream()
                .map(competency -> getUserSkillDetailFromCompetency(competency, "WORK HISTORY"))
                .filter(optional -> optional.isPresent()).map(optional -> optional.get()).collect(Collectors.toList());
        if (!skills.isEmpty())
            user.setSkillsFoundInWork("," + Joiner.on(",").join(skills) + ",");
        return;
    }

    public List<UserExperinceDetail> getUserExperinceDetails() {
        List<UserExperinceDetail> userExpList = new ArrayList<UserExperinceDetail>();
        if (!structuredResume.isPresent())
            return userExpList;
        EmploymentHistoryType employmentHistory = structuredResume.get().getEmploymentHistory();
        if (employmentHistory == null) {
            log.info("zzzzzzz employment history is null");
            return userExpList;
        }
        // we can get all the information from the position history, no need to
        // user the empoymentOrg.
        userExpList = employmentHistory.getEmployerOrg().stream().map(empOrg -> empOrg.getPositionHistory())
                .flatMap(positionListStream -> positionListStream.stream())
                .map(this::getUserExperienceFromPositionHistory).filter(optionalExp -> optionalExp.isPresent())
                .map(exp -> exp.get()).collect(Collectors.toList());
        log.info("zzzzz userExpList size = " + userExpList.size());
        log.info("zzzzz userExpList is empty " + userExpList.isEmpty());
        userExpList.stream().forEach(exp -> log.info("zzzzz exp = " + exp.toString()));
        return userExpList;
        // adding details of military experience
        // MilitaryHistoryType militaryHistoryType =
        // structuredResume.get().getMilitaryHistory();
        // if(militaryHistoryType==null)
        // return userExpList;
        //
        // List<UserExperinceDetail> militaryExpDetails =
        // militaryHistoryType.getServiceDetail().stream().map(this::getUserExperienceFromMilitaryService).collect(Collectors.toList());
        // militaryExpDetails.stream().forEach(exp -> {
        // StringBuilder b = new StringBuilder();
        // b.append(checkEmpty(militaryHistoryType.getComments())?"":"Comments: "+militaryHistoryType.getComments());
        // if(!StringUtils.isEmpty(exp.getDescription()))
        // b.append("  "+exp.getDescription());
        // exp.setDescription(b.toString());
        // exp.setLocationDetail(militaryHistoryType.getCountryServed());
        // });
        // userExpList.addAll(militaryExpDetails);
        // return userExpList;
    }

    public List<UserVolunteerDetail> getUserVolunteerDetails() {
        List<UserVolunteerDetail> userVolList = new ArrayList<UserVolunteerDetail>();
        if (!structuredResume.isPresent())
            return userVolList;
        EmploymentHistoryType employmentHistory = structuredResume.get().getEmploymentHistory();
        if (employmentHistory == null) {
            log.info("zzzzzzz employment history is null for volunteering experience");
            return userVolList;
        }
        // we can get all the information from the position history, no need to
        // user the empoymentOrg.
        userVolList = employmentHistory.getEmployerOrg().stream().map(empOrg -> empOrg.getPositionHistory())
                .flatMap(positionListStream -> positionListStream.stream())
                .map(this::getUserVolunteerDetailFromPoistionHistory).filter(optionalVol -> optionalVol.isPresent())
                .map(vol -> vol.get()).collect(Collectors.toList());

        return userVolList;
    }

    public List<UserEducationDetail> getUserEducationDetails() {
        List<UserEducationDetail> educationDetails = new ArrayList<UserEducationDetail>();
        if (!structuredResume.isPresent())
            return educationDetails;
        EducationHistoryType educationHistory = structuredResume.get().getEducationHistory();
        if (educationHistory == null) {
            return educationDetails;
        }
        educationDetails = educationHistory.getSchoolOrInstitution().stream()
                .map(this::getUserEducationListFromInstituteType).filter(eduList -> !eduList.isEmpty())
                .flatMap(educationList -> educationList.stream()).collect(Collectors.toList());
        log.info("zzzzz educationList size = " + educationDetails.size());
        educationDetails.stream().forEach(edu -> log.info("zzzzz edu = " + edu.toString()));
        return educationDetails;

    }

    public List<UserCertificationDetail> getUserCertificationDetails() {
        List<UserCertificationDetail> certificationDetails = new ArrayList<UserCertificationDetail>();
        if (!structuredResume.isPresent())
            return certificationDetails;
        LicensesAndCertifications licensesAndCertifications = structuredResume.get().getLicensesAndCertifications();
        if (licensesAndCertifications == null)
            return certificationDetails;
        certificationDetails = licensesAndCertifications.getLicenseOrCertification().stream()
                .map(this::getUserCertificationFromCredentialTypes).filter(optionalCert -> optionalCert.isPresent())
                .map(cer -> cer.get()).collect(Collectors.toList());
        return certificationDetails;
    }

    public List<UserPatentDetail> getUserPatentDetails() {
        List<UserPatentDetail> patentDetails = new ArrayList<UserPatentDetail>();
        if (!structuredResume.isPresent())
            return patentDetails;
        PatentHistoryType patentHistory = structuredResume.get().getPatentHistory();
        if (patentHistory == null)
            return patentDetails;
        patentDetails = patentHistory.getPatent().stream().map(this::getUserPatentDetailFromPatentHistory)
                .filter(optionalPatent -> optionalPatent.isPresent()).map(pat -> pat.get())
                .collect(Collectors.toList());
        return patentDetails;
    }

    public List<UserPublicationDetail> getUserPublicationDetails() {
        List<UserPublicationDetail> publicationDetails = new ArrayList<UserPublicationDetail>();
        if (!structuredResume.isPresent())
            return publicationDetails;
        PublicationHistoryType publicationHistory = structuredResume.get().getPublicationHistory();
        if (publicationHistory == null)
            return publicationDetails;
        publicationDetails = publicationHistory.getArticle().stream().map(this::getUserPublicationFromArticle)
                .collect(Collectors.toList());
        publicationDetails.addAll(publicationHistory.getBook().stream().map(this::getUserPublicationFromBook)
                .collect(Collectors.toList()));
        publicationDetails.addAll(publicationHistory.getConferencePaper().stream()
                .map(this::getUserPublicationFromConferencePaper).collect(Collectors.toList()));
        publicationDetails.addAll(publicationHistory.getOtherPublication().stream()
                .map(this::getUserPublicationDetailFromOtherPublications).collect(Collectors.toList()));
        return publicationDetails;
    }

    private Optional<String> getUserSkillDetailFromCompetency(@NotNull CompetencyType competencyType,
            String foundInValue) {
        List<CompetencyEvidence> evidenceList = competencyType.getCompetencyEvidence();
        if (evidenceList.isEmpty())
            return Optional.empty();
        if (!evidenceList.get(0).getTypeDescription().contains(foundInValue))
            return Optional.empty();
        return Optional.ofNullable(competencyType.getName());
    }

    private UserPublicationDetail getUserPublicationDetailFromOtherPublications(@NotNull OtherPublicationType otherType) {
        UserPublicationDetail publicationDetail = new UserPublicationDetail();
        publicationDetail.setTitle(otherType.getTitle());
        getDate(otherType.getPublicationDate()).ifPresent(d -> publicationDetail.setDate(d));
        publicationDetail.setPublicationOrganisation(otherType.getPublisherName());
        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append(checkEmpty(otherType.getISSN()) ? "" : "ISSN:" + otherType.getISSN() + "  ")
                .append(checkEmpty(otherType.getAbstract()) ? "" : "Abstract: " + otherType.getAbstract() + "  ")
                .append(checkEmpty(otherType.getComments()) ? "" : "Comments: " + otherType.getComments() + "  ");
        if (!otherType.getLink().isEmpty())
            descriptionBuilder.append("Links: " + StringUtils.collectionToCommaDelimitedString(otherType.getLink()));

        publicationDetail.setDescription(descriptionBuilder.toString());
        return publicationDetail;
    }

    private UserPublicationDetail getUserPublicationFromConferencePaper(@NotNull ConferencePaperType paperType) {
        UserPublicationDetail publicationDetail = new UserPublicationDetail();
        publicationDetail.setTitle(paperType.getTitle());
        getDate(paperType.getPublicationDate()).ifPresent(d -> publicationDetail.setDate(d));

        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder
                .append(checkEmpty(paperType.getAbstract()) ? "" : "Abstract: " + paperType.getAbstract() + "  ")
                .append(checkEmpty(paperType.getComments()) ? "" : "Comments: " + paperType.getComments() + "  ")
                .append(checkEmpty(paperType.getEventName()) ? "" : "Event: " + paperType.getEventName() + "  ");
        if (!paperType.getLink().isEmpty())
            descriptionBuilder.append("Links: " + StringUtils.collectionToCommaDelimitedString(paperType.getLink()));
        publicationDetail.setDescription(descriptionBuilder.toString());
        return publicationDetail;
    }

    private UserPublicationDetail getUserPublicationFromBook(@NotNull BookType bookType) {
        final UserPublicationDetail publicationDetail = new UserPublicationDetail();
        publicationDetail.setTitle(bookType.getTitle());
        getDate(bookType.getPublicationDate()).ifPresent(d -> publicationDetail.setDate(d));
        publicationDetail.setPublicationOrganisation(bookType.getPublisherName());
        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append(checkEmpty(bookType.getISSN()) ? "" : "ISSN:" + bookType.getISSN() + "  ")
                .append(checkEmpty(bookType.getAbstract()) ? "" : "Abstract: " + bookType.getAbstract() + "  ")
                .append(checkEmpty(bookType.getComments()) ? "" : "Comments: " + bookType.getComments() + "  ")
                .append(checkEmpty(bookType.getChapter()) ? "" : "Chapters: " + bookType.getChapter() + "  ");
        if (!bookType.getLink().isEmpty())
            descriptionBuilder.append("Links: " + StringUtils.collectionToCommaDelimitedString(bookType.getLink()));
        publicationDetail.setDescription(descriptionBuilder.toString());
        return publicationDetail;
    }

    private UserPublicationDetail getUserPublicationFromArticle(@NotNull ArticleType articleType) {
        final UserPublicationDetail publicationDetail = new UserPublicationDetail();
        publicationDetail.setTitle(articleType.getTitle());
        getDate(articleType.getPublicationDate()).ifPresent(d -> publicationDetail.setDate(d));

        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder
                .append(checkEmpty(articleType.getISSN()) ? "" : "ISSN:" + articleType.getISSN() + "  ")
                .append(checkEmpty(articleType.getAbstract()) ? "" : "Abstract: " + articleType.getAbstract() + "  ")
                .append(checkEmpty(articleType.getIssue()) ? "" : "Issue: " + articleType.getIssue() + "  ")
                .append(checkEmpty(articleType.getComments()) ? "" : "Comments: " + articleType.getComments() + "  ")
                .append(checkEmpty(articleType.getJournalOrSerialName()) ? "" : "Journal: "
                        + articleType.getJournalOrSerialName() + "  ");
        if (!articleType.getLink().isEmpty())
            descriptionBuilder.append("Links: " + StringUtils.collectionToCommaDelimitedString(articleType.getLink()));

        publicationDetail.setDescription(descriptionBuilder.toString());
        return publicationDetail;
    }

    private Optional<UserCertificationDetail> getUserCertificationFromCredentialTypes(
            @NotNull CredentialType credentialType) {
        final UserCertificationDetail certificationDetail = new UserCertificationDetail();
        if (credentialType.getIssuingAuthority() == null && credentialType.getName() == null)
            return Optional.empty();
        if (credentialType.getIssuingAuthority() != null)
            certificationDetail.setOrganisationName(credentialType.getIssuingAuthority().getValue());
        certificationDetail.setName(credentialType.getName());
        if (credentialType.getEffectiveDate() != null) {

            getDate(credentialType.getEffectiveDate().getValidFrom()).ifPresent(
                    d -> certificationDetail.setStartDate(d));
            getDate(credentialType.getEffectiveDate().getValidTo()).ifPresent(d -> certificationDetail.setEndDate(d));
            if (certificationDetail.getStartDate() != null)
                getDate(credentialType.getEffectiveDate().getFirstIssuedDate()).ifPresent(
                        d -> certificationDetail.setStartDate(d));
        }

        return Optional.of(certificationDetail);
    }

    private Optional<UserPatentDetail> getUserPatentDetailFromPatentHistory(
            @NotNull PatentDescriptionType patentDescriptionType) {
        final UserPatentDetail patentDetail = new UserPatentDetail();
        if (patentDescriptionType.getPatentTitle() == null)
            return Optional.empty();
        patentDetail.setTitle(patentDescriptionType.getPatentTitle());
        patentDetail.setDescription(patentDescriptionType.getDescription());

        patentDescriptionType
                .getPatentDetail()
                .stream()
                .findFirst()
                .ifPresent(
                        pDetail -> pDetail
                                .getPatentMilestone()
                                .stream()
                                .findFirst()
                                .ifPresent(patentMilestone -> {
                                    // TODO:set the filling date (dont know the
                                    // format yet)
                                        patentDetail.setApplicationNumber(patentMilestone.getId());
                                        if (!checkEmpty(patentMilestone.getStatus())) {
                                            patentDetail.setPatentStatus(SovrenToJobseekerModelMapper
                                                    .getPatentStatusMap()
                                                    .get(PatentStatusTypeTypes.fromValue(patentMilestone.getStatus()))
                                                    .name());
                                        }
                                    }));
        patentDetail.setUrl((patentDescriptionType.getLink() == null) ? null : getFormattedLink(patentDescriptionType
                .getLink()));
        patentDescriptionType
                .getPatentDetail()
                .stream()
                .filter(pDetail -> {
                    return pDetail.getIssuingAuthority() != null;
                })
                .map(pDetail -> pDetail.getIssuingAuthority())
                .filter(issueingAuth -> issueingAuth.getCountryCode() != null)
                .map(issueingAuth -> issueingAuth.getCountryCode())
                .findFirst()
                .ifPresent(
                        countryCode -> {
                            Arrays.asList(CountryCodes.values())
                                    .stream()
                                    .map(pOffice -> pOffice.name())
                                    .filter(patentOfficeCode -> {
                                        return patentOfficeCode.compareToIgnoreCase(countryCode) == 0;
                                    })
                                    .findFirst()
                                    .ifPresent(
                                            pOfficeCode -> patentDetail.setCountryCode(CountryCodes
                                                    .valueOf(pOfficeCode).name()));
                        });

        return Optional.of(patentDetail);
    }

    private Optional<UserExperinceDetail> getUserExperienceFromPositionHistory(
            @NotNull final PositionHistory positionHistory) {

        UserExperinceDetail userExperinceDetail = new UserExperinceDetail();
        // NOTE: not adding the experience with position type as volunteer over
        // here.
        if (!checkEmpty(positionHistory.getPositionType())
                && positionHistory.getPositionType().equals(PositionTypes.VOLUNTEER.value())
                && positionHistory.getOrgName() != null)
            return Optional.empty();

        userExperinceDetail.setCompanyName((positionHistory.getOrgName() != null) ? positionHistory.getOrgName()
                .getOrganizationName() : null);
        // set location detail of orgainsation
        positionHistory
                .getOrgInfo()
                .stream()
                .filter(orgInfo -> {
                    return orgInfo.getPositionLocation() != null;
                })
                .map(orgInfo -> orgInfo.getPositionLocation())
                .findFirst()
                .ifPresent(
                        pAddress -> getLocationDetails(pAddress).ifPresent(
                                address -> userExperinceDetail.setLocationDetail(address)));

        getDate(positionHistory.getStartDate()).ifPresent(d -> userExperinceDetail.setTimePeriodStart(d));
        getDate(positionHistory.getEndDate()).ifPresent(d -> userExperinceDetail.setTimePeriodEnd(d));
        if (userExperinceDetail.getTimePeriodEnd() == null) {
            if (isCurrent(positionHistory.getEndDate()))
                userExperinceDetail.setIsCurrentJob((byte) 1);
        }

        userExperinceDetail.setDescription(positionHistory.getDescription());
        userExperinceDetail.setRoleName(positionHistory.getTitle());
        return Optional.of(userExperinceDetail);

    }

    private Optional<UserVolunteerDetail> getUserVolunteerDetailFromPoistionHistory(
            @NotNull final PositionHistory positionHistory) {
        UserVolunteerDetail volunteerDetail = new UserVolunteerDetail();
        if (StringUtils.isEmpty(positionHistory.getPositionType())
                || !positionHistory.getPositionType().equals(PositionTypes.VOLUNTEER.value()))
            return Optional.empty();
        volunteerDetail.setOrganisationName((positionHistory.getOrgName() != null) ? positionHistory.getOrgName()
                .getOrganizationName() : null);
        getDate(positionHistory.getStartDate()).ifPresent(d -> volunteerDetail.setStartDate(d));
        getDate(positionHistory.getEndDate()).ifPresent(d -> volunteerDetail.setEndDate(d));
        volunteerDetail.setDescription(positionHistory.getDescription());
        volunteerDetail.setRoleName(positionHistory.getTitle());
        // TODO: where to get the cause information?
        return Optional.of(volunteerDetail);
    }

    // OF NO USE RIGHT NOW
    // private UserExperinceDetail getUserExperienceFromMilitaryService(@NotNull
    // final ServiceDetail serviceDetail){
    // UserExperinceDetail experinceDetail = new UserExperinceDetail();
    // experinceDetail.setRoleName(serviceDetail.getRankAchieved().getCurrentOrEndRank());
    // if(checkEmpty(experinceDetail.getRoleName()))
    // experinceDetail.setRoleName(serviceDetail.getRankAchieved().getStartRank());
    // getDate(serviceDetail.getDatesOfService().getStartDate()).ifPresent(d ->
    // experinceDetail.setTimePeriodStart(d));
    // getDate(serviceDetail.getDatesOfService().getEndDate()).ifPresent(d ->
    // experinceDetail.setTimePeriodEnd(d));
    // //TODO: add company.
    // StringBuilder descriptionBuilder = new StringBuilder();
    // if(!serviceDetail.getAreaOfExpertise().isEmpty()){
    // descriptionBuilder.append("Expertise: "+StringUtils.collectionToCommaDelimitedString(serviceDetail.getAreaOfExpertise()));
    // }
    // if(!serviceDetail.getRecognitionAchieved().isEmpty()){
    // descriptionBuilder.append("Recognitions: "+StringUtils.collectionToCommaDelimitedString(serviceDetail.getRecognitionAchieved()));
    // }
    // descriptionBuilder.append(checkEmpty(serviceDetail.getBranch())?"":"Branch: "+serviceDetail.getBranch());
    // experinceDetail.setDescription(descriptionBuilder.toString());
    // return experinceDetail;
    // }

    private List<UserEducationDetail> getUserEducationListFromInstituteType(
            @NotNull SchoolOrInstitutionType institutionType) {
        List<UserEducationDetail> educationDetails = new ArrayList<UserEducationDetail>();
        String schoolName = (institutionType.getSchool().isEmpty()) ? null : institutionType.getSchool().get(0)
                .getSchoolName();
        educationDetails = institutionType.getDegree().stream().map(this::getUserEducationWithDegree)
                .collect(Collectors.toList());

        educationDetails.stream().forEach(edu -> {
            edu.setEducationalOrg(schoolName);
        });

        return educationDetails;

    }

    private UserEducationDetail getUserEducationWithDegree(@NotNull EduDegreeType degreeType) {
        UserEducationDetail educationDetail = new UserEducationDetail();
        degreeType.getDatesOfAttendance().stream().findFirst().map(dateOfAttendance -> dateOfAttendance.getEndDate())
                .ifPresent(endDate -> {
                    getDate(endDate).ifPresent(d -> educationDetail.setTimePeriodEnd(d));
                });
        if (educationDetail.getTimePeriodEnd() == null) {
            getDate(degreeType.getDegreeDate()).ifPresent(d -> educationDetail.setTimePeriodEnd(d));
        }
        degreeType.getDatesOfAttendance().stream().findFirst().map(dateOfAttendance -> dateOfAttendance.getStartDate())
                .ifPresent(startDate -> {
                    getDate(startDate).ifPresent(d -> educationDetail.setTimePeriodStart(d));
                });
        degreeType
                .getDegreeMajor()
                .stream()
                .findFirst()
                .ifPresent(
                        majorType -> majorType.getName().stream().findFirst()
                                .ifPresent(majorName -> educationDetail.setMajorSubject(majorName)));
        educationDetail.setDegree((degreeType.getDegreeName() == null) ? null : degreeType.getDegreeName().getValue());
        return educationDetail;
    }

    private boolean isCurrent(FlexibleDatesType datesType) {
        if (datesType == null)
            return false;
        if (!StringUtils.isEmpty(datesType.getStringDate()) && datesType.getStringDate().equals("current"))
            return true;
        return false;

    }

    private Optional<Date> getDate(FlexibleDatesType dateType) {
        if (dateType == null)
            return Optional.empty();
        log.info("zzzzz dateType : " + dateType.toString());
        if (dateType.getAnyDate() != null) {
            try {
                return Optional.of(DateUtility.getDateFromString(dateType.getAnyDate(),
                        DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
            } catch (ParseException e) {
                log.error("unable to parse date" + e);
                return Optional.empty();
            }
        }

        if (dateType.getYearMonth() != null)
            return Optional.of(new DateBuilder().setYear(dateType.getYearMonth().getYear())
                    .setMonth(dateType.getYearMonth().getMonth() - 1).build());
        if (dateType.getYear() != null)
            return Optional.of(new DateBuilder().setYear(dateType.getYear().getYear()).build());
        return Optional.empty();
    }

    private Optional<String> getTelephoneNumber(TelcomNumberType telephone) {
        if (telephone == null)
            return Optional.empty();
        return Optional.ofNullable(getNumbersOnly(telephone.getFormattedNumber()));
    }

    private Optional<String> getFormatMobileNumber(TelcomNumberType telephone) {
        if (telephone == null)
            return Optional.empty();
        Optional<String> formattedString = Optional.ofNullable(getNumbersOnly(telephone.getFormattedNumber()));
        if (formattedString.isPresent())
            return Optional.of(formattedString.get().substring(formattedString.get().length() - 10));
        return Optional.empty();
    }

    private Optional<String> getLocationDetails(PostalAddressType pAddress) {
        if (pAddress == null)
            return Optional.empty();

        StringBuilder addressString = new StringBuilder();
        if (pAddress.getRegion() != null && !pAddress.getRegion().isEmpty()) {
            addressString.append(pAddress.getRegion().get(0)).append(",").append(pAddress.getCountryCode());
        } else {
            addressString.append(pAddress.getCountryCode());
        }
        return Optional.of(addressString.toString());

    }

    private boolean checkEmpty(String x) {
        return StringUtils.isEmpty(x);
    }

    /**
     * move this to a utility
     * 
     * @param unformattedString
     * @return
     */
    private String getNumbersOnly(String unformattedString) {
        if (StringUtils.isEmpty(unformattedString)) {
            log.error("trying to parse empty or null telephone number");
            return null;
        }
        String formattedString = unformattedString.replaceAll("[^0-9]", "");
        log.info("zzzzz formattedNumber = " + formattedString);
        return formattedString;
    }

    // adds http:// if starts with www.
    private String getFormattedLink(@NotNull String unformattedLink) {
        StringBuilder formattedLink = new StringBuilder();
        if (unformattedLink.startsWith("www."))
            return formattedLink.append("http://").append(unformattedLink).toString();
        return unformattedLink;
    }

}
