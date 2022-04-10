package com.portal.job.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.portal.job.context.RequestContextContainer;
import com.portal.job.enums.UserType;

/**
 * @author behinddwalls
 *
 */
public class RecruiterAuthorizedURIFilter extends AbstractAuthorizationFilter {

    private String[] skipUrls = new String[] { "/recruiter/account/verify", "/recruiter/verify/workemail",
            "/recruiter/profile" };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (RequestContextContainer.getRequestContext().isUserAuthorized()
                && UserType.RECRUITER.equals(RequestContextContainer.getRequestContext().signedInAs())) {

            if (!RequestContextContainer.getRequestContext().isRecruiterAccountVerified()
                    && !doesSkipUrlsExistInRequestURI(httpRequest.getRequestURI(), skipUrls)) {
                System.out.println(RequestContextContainer.getRequestContext());
                httpResponse.sendRedirect(getContextPath(httpRequest) + "/recruiter/account/verify");
            } else if (!RequestContextContainer.getRequestContext().isWorkEmailVerified()
                    && !doesSkipUrlsExistInRequestURI(httpRequest.getRequestURI(), skipUrls)) {
                httpResponse.sendRedirect(getContextPath(httpRequest) + "/user/verify/workemail");
            } else if (!RequestContextContainer.getRequestContext().isRecruiterProfileComplete()
                    && !doesSkipUrlsExistInRequestURI(httpRequest.getRequestURI(), skipUrls)) {
                httpResponse.sendRedirect(getContextPath(httpRequest) + "/recruiter/profile?incomplete=1");
            } else {
                chain.doFilter(request, response);
            }
        } else {
            httpResponse.sendRedirect(getContextPath(httpRequest) + "/error/403");
        }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
