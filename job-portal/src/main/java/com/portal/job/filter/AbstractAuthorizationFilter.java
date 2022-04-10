package com.portal.job.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.portal.job.constants.SessionConstants;
import com.portal.job.context.RequestContext;
import com.portal.job.context.RequestContextContainer;
import com.portal.job.enums.UserType;
import com.portal.job.model.BasicAccountDataModel;
import com.portal.job.model.UserBasicDataModel;

/**
 * @author preetam
 *
 */
public abstract class AbstractAuthorizationFilter implements Filter {

    protected Boolean isUserAuthorized(final ServletRequest request, final ServletResponse response) throws IOException {

        RequestContext requestContext = null;
        HttpSession httpSession = ((HttpServletRequest) request).getSession(false);

        if (null == httpSession || httpSession.getAttribute(SessionConstants.ACCOUNT) == null
                || httpSession.getAttribute(SessionConstants.USER_DATA) == null) {
            requestContext = new RequestContext.Builder().build();
            RequestContextContainer.setRequestContext(requestContext);
            return false;
        }

        final boolean jsSignedIn = (httpSession.getAttribute(SessionConstants.JS_SIGNED_IN) == null) ? false
                : (Boolean) httpSession.getAttribute(SessionConstants.JS_SIGNED_IN);

        final boolean rcSignedIn = (httpSession.getAttribute(SessionConstants.RC_SIGNED_IN) == null) ? false
                : (Boolean) httpSession.getAttribute(SessionConstants.RC_SIGNED_IN);
        final boolean exSignedIn = (httpSession.getAttribute(SessionConstants.EX_SIGNED_IN) == null) ? false
                : (Boolean) httpSession.getAttribute(SessionConstants.EX_SIGNED_IN);

        // is account verified

        boolean isRcAccountVerified = httpSession.getAttribute(SessionConstants.RC_ACCOUNT_VERIFIED) == null ? false
                : (Boolean) httpSession.getAttribute(SessionConstants.RC_ACCOUNT_VERIFIED);

        boolean isJsAccountVerified = httpSession.getAttribute(SessionConstants.JS_ACCOUNT_VERIFIED) == null ? false
                : (Boolean) httpSession.getAttribute(SessionConstants.JS_ACCOUNT_VERIFIED);

        boolean isExAccountVerified = httpSession.getAttribute(SessionConstants.EX_ACCOUNT_VERIFIED) == null ? false
                : (Boolean) httpSession.getAttribute(SessionConstants.EX_ACCOUNT_VERIFIED);

        // is emails verified
        boolean isWorkEmailIdVerified = httpSession.getAttribute(SessionConstants.WORK_EMAIL_ID_VERIFIED) == null ? false
                : (Boolean) httpSession.getAttribute(SessionConstants.WORK_EMAIL_ID_VERIFIED);

        boolean isEmailIdVerified = httpSession.getAttribute(SessionConstants.EMAIL_ID_VERIFIED) == null ? false
                : (Boolean) httpSession.getAttribute(SessionConstants.EMAIL_ID_VERIFIED);

        // is profile complete
        boolean isRcProfileComplete = httpSession.getAttribute(SessionConstants.IS_RECRUITER_PROFILE_COMPLETE) == null ? false
                : (Boolean) httpSession.getAttribute(SessionConstants.IS_RECRUITER_PROFILE_COMPLETE);
        boolean isJsProfileComplete = httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE) == null ? false
                : (Boolean) httpSession.getAttribute(SessionConstants.IS_JOBSEEKER_PROFILE_COMPLETE);

        final BasicAccountDataModel basicAccountDataModel = (BasicAccountDataModel) httpSession
                .getAttribute(SessionConstants.ACCOUNT);
        final UserBasicDataModel userBasicDataModel = (UserBasicDataModel) httpSession
                .getAttribute(SessionConstants.USER_DATA);

        if (jsSignedIn || rcSignedIn || exSignedIn) {
            UserType userType = null;

            if (jsSignedIn) {
                userType = UserType.JOBSEEKER;
            } else if (rcSignedIn) {
                userType = UserType.RECRUITER;
            } else if (exSignedIn) {
                userType = UserType.EXPERT;
            } else {
                return false;
            }
            requestContext = new RequestContext.Builder().isUserAuthorized(true).userId(userBasicDataModel.getUserId())
                    .accountId(basicAccountDataModel.getAccountId()).signedInAs(userType)
                    .isRecruiterProfileComplete(isRcProfileComplete).isJobSeekerProfileComplete(isJsProfileComplete).isJobseekerAccountVerified(isJsAccountVerified)
                    .isRecruiterAccountVerified(isRcAccountVerified).isExpertAccountVerified(isExAccountVerified)
                    .isWorkEmailVerified(isWorkEmailIdVerified).isEmailVerified(isEmailIdVerified).build();

            RequestContextContainer.setRequestContext(requestContext);
            return true;
        }
        return false;
    }

    protected boolean skipUrlRegex(final HttpServletRequest request, final String skipAuthRegex) {
        String uri = request.getRequestURI();
        String newUri = uri.replace(getContextPath(request), "");
        List<String> skipUrls = Arrays.asList(skipAuthRegex.split("\\|"));
        for (String skipUrl : skipUrls) {
            if ((StringUtils.isEmpty(newUri.replaceFirst("/", "").trim())) || newUri.startsWith(skipUrl)
                    || newUri.replaceFirst("/", "").startsWith(skipUrl)) {
                return true;
            }
        }
        return false;
    }

    protected boolean doesSkipUrlsExistInRequestURI(final String requestURI, final String[] skipUrls) {
        for (String url : skipUrls) {
            if (requestURI.contains(url)) {
                return true;
            }
        }
        return false;
    }

    protected String getContextPath(final HttpServletRequest request) {
        return request.getContextPath();
    }
}
