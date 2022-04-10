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
public class JobseekerAuthorizedURIFilter extends AbstractAuthorizationFilter {

    private String[] skipUrls = new String[] { "/jobseeker/account/verify", "/jobseeker/verify/workemail","/jobseeker/profile/view","/jobseeker/profile/basic/view","/viewAll","/viewBy","/save","/remove","/upload" };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (RequestContextContainer.getRequestContext().isUserAuthorized()
                && UserType.JOBSEEKER.equals(RequestContextContainer.getRequestContext().signedInAs())) {

            if (!RequestContextContainer.getRequestContext().isJobseekerAccountVerified()
                    && !doesSkipUrlsExistInRequestURI(httpRequest.getRequestURI(), skipUrls)) {
                httpResponse.sendRedirect(getContextPath(httpRequest) + "/jobseeker/account/verify");
            } else if (!RequestContextContainer.getRequestContext().isEmailVerified()
                    && !doesSkipUrlsExistInRequestURI(httpRequest.getRequestURI(), skipUrls)) {
                httpResponse.sendRedirect(getContextPath(httpRequest) + "/jobseeker/verify/email");
            }else if (!RequestContextContainer.getRequestContext().isJobSeekerProfileComplete()
                    && !doesSkipUrlsExistInRequestURI(httpRequest.getRequestURI(), skipUrls)) {
                httpResponse.sendRedirect(getContextPath(httpRequest) + "/jobseeker/profile/view?incomplete=1");
            } else
                chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(getContextPath(httpRequest) + "/error/403");
        }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
