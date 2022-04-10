package com.portal.job.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.job.dao.IndustryDetailDao;
import com.portal.job.dao.model.IndustryCodeDetail;

@Service
public class IndustryDetailService {

	@Autowired
	private IndustryDetailDao industryDetailDao;
	
	@Transactional
	public Set<String> serachIndustryByDescription(final String industryDescription) {
		final List<IndustryCodeDetail> industryCodeDetails = this.industryDetailDao
				.getEntitiesSimilarToPropertyValue(
						new HashMap<String, String>() {
							private static final long serialVersionUID = 1L;
							{
								put("industryDescription", "%" + industryDescription + "%");
							}
						}, IndustryCodeDetail.class);

		return getIndustrySet(industryCodeDetails) ;
	}
	
	@Transactional
	public Set<String> getAllIndustry(){
		return getIndustrySet(this.industryDetailDao.findAll());
	}
	
	///////// ========================= start private Methods   ===================================
	private Set<String> getIndustrySet(List<IndustryCodeDetail> industryDetails){
		final Set<String> industryNameSet = new HashSet<String>();
		for (IndustryCodeDetail industryCodeDetail : industryDetails) {
			industryNameSet.add(industryCodeDetail.getIndustryDescription());
		}
		return industryNameSet;
	}
	
	///////// ========================== End   ======================================================
}
