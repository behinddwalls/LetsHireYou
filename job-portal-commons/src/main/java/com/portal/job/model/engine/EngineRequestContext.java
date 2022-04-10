package com.portal.job.model.engine;

import java.util.Set;

/**
 * 
 * @author pandeysp Holds the all input Data required by the 'Engine' to compute
 *         the request.
 */
public class EngineRequestContext {

	private Set<EngineJobData> engineJobDataSet;
	private Set<EngineUserData> engineUserDataSet;

	public Set<EngineJobData> getEngineJobDataSet() {
		return engineJobDataSet;
	}

	public void setEngineJobDataSet(Set<EngineJobData> engineJobDataSet) {
		this.engineJobDataSet = engineJobDataSet;
	}

	public Set<EngineUserData> getEngineUserDataSet() {
		return engineUserDataSet;
	}

	public void setEngineUserDataSet(Set<EngineUserData> engineUserDataSet) {
		this.engineUserDataSet = engineUserDataSet;
	}

	@Override
	public String toString() {
		return "EngineRequestContext [engineJobDataSet=" + engineJobDataSet
				+ ", engineUserDataSet=" + engineUserDataSet + "]";
	}

}
