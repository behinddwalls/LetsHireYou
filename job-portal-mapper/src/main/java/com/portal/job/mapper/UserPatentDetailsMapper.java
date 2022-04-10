package com.portal.job.mapper;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.portal.job.constants.DateConstants;
import com.portal.job.dao.model.UserDetail;
import com.portal.job.dao.model.UserPatentDetail;
import com.portal.job.enums.CountryCodes;
import com.portal.job.enums.PatentStatus;
import com.portal.job.model.UserPatentDataModel;
import com.portal.job.utils.DateUtility;

@Component
public class UserPatentDetailsMapper {

    public UserPatentDetail getEntityFromDataModel(final UserPatentDataModel patentData, final Long jobSeeker)
            throws ParseException {
        UserPatentDetail jsPatentDetail = new UserPatentDetail();
        // set userdetail
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(jobSeeker);
        jsPatentDetail.setUserDetail(userDetail);

        if (!StringUtils.isEmpty(patentData.getPatentId()))
            jsPatentDetail.setPatentId(Long.parseLong(patentData.getPatentId()));
        jsPatentDetail.setApplicationNumber(patentData.getPatentApplicationNumber());
        if (!StringUtils.isEmpty(patentData.getPatentOffice()))
            jsPatentDetail.setCountryCode(CountryCodes.fromValue(patentData.getPatentOffice().toUpperCase()).name());
        jsPatentDetail.setDescription(patentData.getPatentDescription());
        if (!StringUtils.isEmpty(patentData.getPatentFillingDate())) {
            jsPatentDetail.setFilingDate(DateUtility.getDateFromString(patentData.getPatentFillingDate(),
                    DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
        }
        if (!StringUtils.isEmpty(patentData.getPatentStatus()))
            jsPatentDetail.setPatentStatus(PatentStatus.valueOf(patentData.getPatentStatus()).name());
        jsPatentDetail.setTitle(patentData.getPatentTitle());
        jsPatentDetail.setUrl(patentData.getPatentUrl());
        return jsPatentDetail;

    }

    public UserPatentDataModel getDataModelFromEntity(final UserPatentDetail jsPatentDetail) {
        UserPatentDataModel patentData = new UserPatentDataModel();
        patentData.setPatentApplicationNumber(jsPatentDetail.getApplicationNumber());
        if (jsPatentDetail.getCountryCode() != null)
            patentData.setPatentOffice(CountryCodes.valueOf(jsPatentDetail.getCountryCode()).getCCValue());
        patentData.setPatentDescription(jsPatentDetail.getDescription());
        if (!StringUtils.isEmpty(jsPatentDetail.getFilingDate())) {
            patentData.setPatentFillingDate(DateUtility.getStringFromDate(jsPatentDetail.getFilingDate(),
                    DateConstants.YYY_MM_DD_FORMAT_WITH_DASH));
        }
        patentData.setPatentId(jsPatentDetail.getPatentId().toString());
        if (jsPatentDetail.getPatentStatus() != null)
            patentData.setPatentStatus(jsPatentDetail.getPatentStatus());
        patentData.setPatentTitle(jsPatentDetail.getTitle());
        patentData.setPatentUrl(jsPatentDetail.getUrl());
        return patentData;
    }

    public Set<UserPatentDataModel> getDataModelSetFromEntitySet(final Set<UserPatentDetail> jsPatentDetailSet) {
        Set<UserPatentDataModel> patentDateSet = new HashSet<UserPatentDataModel>();
        for (UserPatentDetail jsPatentDetail : jsPatentDetailSet) {
            patentDateSet.add(getDataModelFromEntity(jsPatentDetail));
        }
        return patentDateSet;
    }

    public UserPatentDataModel getMockPatentData() {
        return null;
    }
}
