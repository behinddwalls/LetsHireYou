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

import com.portal.job.dao.SkillDetailDao;
import com.portal.job.dao.model.SkillDetail;
import com.portal.job.mapper.UserSkillDetailMapper;

/**
 * 
 * @author pandeysp
 *
 *         This class contains Transaction related to Skill table,
 *         UserSkillDetail table.
 */
@Service
public class SkillDetailService {

    private static int SKILLS_SIZE = 10;

    @Autowired
    private SkillDetailDao skillDetailDao;
    @Autowired
    private UserSkillDetailMapper userSkillDetailMapper;

    private static final Logger log = LoggerFactory.getLogger(SkillDetailService.class);

    /**
     * 
     * @param skillname
     * @return
     */
    @Transactional
    public Set<String> serachSkillByName(final String skillname) {
        final List<SkillDetail> skillDetails = this.skillDetailDao.getEntitiesSimilarToPropertyValue(
                new HashMap<String, String>() {
                    private static final long serialVersionUID = 1L;
                    {
                        put("skillName", "%" + skillname + "%");
                    }
                }, SkillDetail.class);
        final Set<String> skillSet = new HashSet<String>();
        for (SkillDetail skillDetail : skillDetails) {
            skillSet.add(WordUtils.capitalize(skillDetail.getSkillName()));
        }
        return skillSet;
    }

}
