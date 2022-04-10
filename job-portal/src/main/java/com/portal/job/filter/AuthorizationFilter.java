package com.portal.job.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author preetam
 *
 */
@WebFilter(filterName = "AuthorizationFilter")
public class AuthorizationFilter extends AbstractAuthorizationFilter {

	public final String UNAUTHORIZED_PAGE = "/signin/jobseeker";
	private String skipAuthRegex;

	public void destroy() {
		this.skipAuthRegex = null;

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		boolean isAuthorized = this.isUserAuthorized(request, response);

		if (!skipUrlRegex(httpRequest, skipAuthRegex) && !isAuthorized) {

			httpResponse.sendRedirect(getContextPath(httpRequest)
					+ UNAUTHORIZED_PAGE);
		} else {
			chain.doFilter(request, response);
		}
		return;

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		skipAuthRegex = filterConfig.getInitParameter("skipAuthentication");
	}

}