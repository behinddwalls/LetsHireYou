package com.portal.job.dao.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@CompoundIndexes({ @CompoundIndex(name = "user_job_unique", def = "{'user.userId' : 1, 'job.jobId' : 1}") })
public class UserByJob {

	@Id
	private String _id;

	@Indexed
	private Job job;

	@Indexed
	private User user;

	@Indexed
	private double percentageMatch;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getPercentageMatch() {
		return percentageMatch;
	}

	public void setPercentageMatch(double percentageMatch) {
		this.percentageMatch = percentageMatch;
	}

}
