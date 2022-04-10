package com.portal.job.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.portal.job.httpclient.HttpClientWrapper;
import com.portal.job.httpclient.HttpRequest;

/**
 * @author nivedita
 *
 */
@RequestMapping("linkedin/auth")
@Controller
public class LinkedInController {
	String redirectUrl = "http://localhost:8080/sample/linkedin/auth/callback";
	final String clientId = "757y9bj9ko8xrb";
	final String accesstokenUrl = "https://www.linkedin.com/uas/oauth2/accessToken";

	// @Autowired
	// private HashMap<String, String> Linkedin_map;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String processForm(HttpSession httpSession)
			throws UnsupportedEncodingException {

		final String uuidString = UUID.randomUUID().toString();
		httpSession.setAttribute("state", uuidString);
		final String linkedinUrl = new StringBuilder(
				"https://www.linkedin.com/uas/oauth2/authorization")
				.append("?response_type=code&client_id=")
				.append(clientId)
				.append("&redirect_uri=")
				.append(redirectUrl)
				.append("&state=")
				.append(uuidString)
				.append("&scope=")
				.append(URLEncoder
						.encode("r_basicprofile r_emailaddress rw_company_admin w_share",
								"UTF-8")).toString();

		return "redirect:" + linkedinUrl;
	}

	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public @ResponseBody String callback(
			@RequestParam(value = "code") String code,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "error_description", required = false) String error_description,
			HttpSession httpSession) throws Exception {

		String authstate = (String) httpSession.getAttribute("state");
		HttpClientWrapper wrap = new HttpClientWrapper();

		if ((null != code && !code.isEmpty()) && ((null != state))
				&& (state.equals(authstate))) {

			HttpRequest obj = new HttpRequest.Builder(accesstokenUrl)
					.header("Content-Type", "application/x-www-form-urlencoded")
					.urlParameter("grant_type", "authorization_code")
					.urlParameter("code", code)
					.urlParameter("redirect_uri", redirectUrl)
					.urlParameter("client_id", clientId)
					.urlParameter("client_secret", "V0rs9ohxxLBOtXEa").build();

			HttpResponse response = wrap.post(obj);
			System.out.println("Response Code : "
					+ response.getStatusLine().getStatusCode());

			String responseString = EntityUtils.toString(response.getEntity());

			System.out.println("responseString: " + responseString);
			MappingJackson2HttpMessageConverter json = new MappingJackson2HttpMessageConverter();
			JsonNode node = json.getObjectMapper().readTree(responseString);

			String acees_token = node.get("access_token").asText();
			String expires_in = node.get("expires_in").asText();

			HttpRequest obj1 = new HttpRequest.Builder(
					"https://api.linkedin.com/v1/people/~?format=json&oauth2_access_token="
							+ acees_token).build();

			HttpResponse getresponse = wrap.get(obj1);

			final String profileGetResponse = EntityUtils.toString(getresponse
					.getEntity());

			return profileGetResponse;

		}

		else {
			System.out.println("ERROR!!!!!");
		}
		return "";

	}

}
