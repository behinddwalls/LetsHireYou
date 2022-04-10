package com.portal.job.enums.mail;

/**
 * @author behinddwalls
 *
 */
public enum MandrillRecipientType {

	TO, CC, BCC;

	public String getTypeName() {
		return this.name().toLowerCase();
	}
}
