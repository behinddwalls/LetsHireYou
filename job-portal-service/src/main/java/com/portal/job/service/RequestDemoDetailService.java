package com.portal.job.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.job.dao.RequestDemoDetailDao;
import com.portal.job.dao.model.RequestDemoDetail;

@Service
public class RequestDemoDetailService {

	@Autowired
	private RequestDemoDetailDao requestDemoDetailDao;

	@Transactional
	public RequestDemoDetail addRequestDemo(
			final RequestDemoDetail requestDemoDetail) {
		this.requestDemoDetailDao.save(requestDemoDetail);
		return requestDemoDetail;
	}

}
