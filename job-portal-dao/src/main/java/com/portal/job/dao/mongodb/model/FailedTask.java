package com.portal.job.dao.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.portal.job.enums.TaskState;

@Document(collection = "failedTask")
public class FailedTask {

	@Id
	private String _id;

	/**
	 * Task Id is the combination of the taskName and key identifier of the
	 * task.
	 */
	@Indexed(unique = true)
	private String taskId;

	private String additionalKey;

	private TaskState state;

	private TaskState tempState;

	private Date createdDate;

	private Date modifiedDate;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAdditionalKey() {
		return additionalKey;
	}

	public void setAdditionalKey(String additionalKey) {
		this.additionalKey = additionalKey;
	}

	public TaskState getState() {
		return state;
	}

	public void setState(TaskState state) {
		this.state = state;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public String toString() {
		return "FailedTask [_id=" + _id + ", taskId=" + taskId
				+ ", additionalKey=" + additionalKey + ", state=" + state
				+ ", tempState=" + tempState + ", createdDate=" + createdDate
				+ ", modifiedDate=" + modifiedDate + "]";
	}

	public TaskState getTempState() {
		return tempState;
	}

	public void setTempState(TaskState tempState) {
		this.tempState = tempState;
	}

}
