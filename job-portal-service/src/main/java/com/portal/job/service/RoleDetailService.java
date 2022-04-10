package com.portal.job.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.job.dao.RoleDetailDao;
import com.portal.job.dao.model.RoleDetail;

@Service
public class RoleDetailService {

    @Autowired
    private RoleDetailDao roleDetailDao;

    private static final Logger log = LoggerFactory.getLogger(RoleDetailService.class);

    @Transactional
    public Set<String> serachRoleByName(final String roleName) {
        final List<RoleDetail> roleDetails = roleDetailDao.getEntitiesSimilarToPropertyValue(
                new HashMap<String, String>() {
                    private static final long serialVersionUID = 1L;
                    {
                        put("roleName", "%" + roleName + "%");
                    }
                }, RoleDetail.class);
        final Set<String> roleNameSet = new HashSet<String>();
        for (RoleDetail roleDetail : roleDetails) {
            roleNameSet.add(WordUtils.capitalize(roleDetail.getRoleName()));
        }
        return roleNameSet;
    }

}
