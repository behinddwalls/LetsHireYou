package com.portal.job.model.mail;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Send Mail Input
 * 
 * @author behinddwalls
 *
 */
public class MandrillSendMailInput {

	private final String fromName;
	private final String fromEmailId;
	private final String subject;
	private final String htmlMessage;
	private final Set<String> toEmailIds;
	private final Set<String> ccEmailIds;
	private final Set<String> bccEmailIds;

	public static class Builder {

		private String fromName;
		private String fromEmailId;
		private String subject;
		private String htmlMessage;
		private Set<String> toEmailIds = Sets.newHashSet();
		private Set<String> ccEmailIds = Sets.newHashSet();
		private Set<String> bccEmailIds = Sets.newHashSet();

		public Builder(final String fromName, final String fromEmailId) {
			this.fromName = fromName;
			this.fromEmailId = fromEmailId;
		}

		public Builder subject(final String subject) {
			this.subject = subject;
			return this;
		}

		public Builder htmlMessage(final String htmlMessage) {
			this.htmlMessage = htmlMessage;
			return this;
		}

		public Builder toEmailId(final String toEmailId) {
			this.toEmailIds.add(toEmailId);
			return this;
		}

		public Builder ccEmailId(final String ccEmailId) {
			this.ccEmailIds.add(ccEmailId);
			return this;
		}

		public Builder bccEmailId(final String bccEmailId) {
			this.bccEmailIds.add(bccEmailId);
			return this;
		}

		public MandrillSendMailInput build() {
			return new MandrillSendMailInput(this);
		}
	}

	public MandrillSendMailInput(final Builder builder) {
		this.fromName = builder.fromName;
		this.fromEmailId = builder.fromEmailId;
		this.subject = builder.subject;
		this.htmlMessage = builder.htmlMessage;
		this.toEmailIds = builder.toEmailIds;
		this.ccEmailIds = builder.ccEmailIds;
		this.bccEmailIds = builder.bccEmailIds;
	}

	public String getFromEmailId() {
		return fromEmailId;
	}

	public Set<String> getToEmailIds() {
		return toEmailIds;
	}

	public Set<String> getCcEmailIds() {
		return ccEmailIds;
	}

	public Set<String> getBccEmailIds() {
		return bccEmailIds;
	}

	public String getFromName() {
		return fromName;
	}

	public String getHtmlMessage() {
		return htmlMessage;
	}

	public String getSubject() {
		return subject;
	}

}
