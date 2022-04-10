package com.portal.job.services.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cribbstechnologies.clients.mandrill.exception.RequestFailedException;
import com.cribbstechnologies.clients.mandrill.model.MandrillHtmlMessage;
import com.cribbstechnologies.clients.mandrill.model.MandrillMessageRequest;
import com.cribbstechnologies.clients.mandrill.model.MandrillRecipient;
import com.cribbstechnologies.clients.mandrill.model.response.message.SendMessageResponse;
import com.cribbstechnologies.clients.mandrill.request.MandrillMessagesRequest;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.portal.job.enums.mail.MandrillRecipientType;
import com.portal.job.exceptions.InvalidRequestException;
import com.portal.job.model.mail.MandrillSendMailInput;

/**
 * @author behinddwalls
 *
 */
@Service
public class JobPortalMailService {

	@Autowired
	private MandrillMessagesRequest messagesRequest;

	private void validateInput(final MandrillSendMailInput input)
			throws InvalidRequestException {
		try {
			Preconditions
					.checkNotNull(input, "Send Mail input can not be null");
			Preconditions.checkNotNull(input.getFromEmailId(),
					"From email id can not be null");
			Preconditions.checkNotNull(input.getFromName(),
					"From Name can not be null");

		} catch (Exception e) {
			throw new InvalidRequestException(e.getMessage());
		}
	}

	public void sendMail(final MandrillSendMailInput input)
			throws InvalidRequestException, RequestFailedException {
		validateInput(input);
		MandrillMessageRequest mmr = new MandrillMessageRequest();
		MandrillHtmlMessage message = new MandrillHtmlMessage();
		Map<String, String> headers = new HashMap<String, String>();
		message.setFrom_email(input.getFromEmailId());
		message.setFrom_name(input.getFromName());
		message.setHeaders(headers);
		message.setHtml(input.getHtmlMessage());
		message.setSubject(input.getSubject());

		// prepare to list
		final List<MandrillRecipient> toRecipients = Lists.newArrayList();
		input.getToEmailIds().forEach(
				emailId -> toRecipients.add(new MandrillRecipient(emailId,
						emailId, MandrillRecipientType.TO.getTypeName())));
		if (null != input.getCcEmailIds() && !input.getCcEmailIds().isEmpty()) {
			input.getToEmailIds().forEach(
					emailId -> toRecipients.add(new MandrillRecipient(emailId,
							emailId, MandrillRecipientType.CC.getTypeName())));
		}
		if (null != input.getBccEmailIds() && !input.getBccEmailIds().isEmpty()) {
			input.getToEmailIds().forEach(
					emailId -> toRecipients.add(new MandrillRecipient(emailId,
							emailId, MandrillRecipientType.BCC.getTypeName())));
		}
		message.setTo(toRecipients.toArray(new MandrillRecipient[toRecipients
				.size()]));
		message.setTrack_clicks(true);
		message.setTrack_opens(true);

		String[] tags = new String[] { "tag1", "tag2", "tag3" };
		message.setTags(tags);
		mmr.setMessage(message);

		try {
			SendMessageResponse response = messagesRequest.sendMessage(mmr);
			response.getList().forEach(
					messageResponse -> System.out.println(messageResponse
							.getStatus()));
		} catch (RequestFailedException e) {
			e.printStackTrace();
			throw e;
		}

	}
}
