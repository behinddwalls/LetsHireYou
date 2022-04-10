<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<form:form action="${contextPath}/signin/recruiter" method="post"
	commandName="signinDataModel">
	<c:if test="${not empty errorMessage}">
		<span class="error">${errorMessage}</span>
		<hr />
	</c:if>
	<!-- ul class="social-login">
		<li><a class="btn btn-linkedin"><i class="fa fa-linkedin"></i>Sign
				In with LinkedIn</a></li>
	</ul>
	<hr -->
	<div class="form-group">
		<label for="login-email">Work Email</label>
		<form:input type="text" class="form-control" id="login-email"
			path="emailId" />
		<form:errors path="emailId" cssClass="error" />
	</div>
	<div class="form-group">
		<label for="login-password">Password</label>
		<form:input type="password" class="form-control" id="login-password"
			path="password" />
		<form:errors path="password" cssClass="error" />
	</div>
	<div class="form-group">
		<div class="g-recaptcha"
			data-sitekey="6LeqFgITAAAAADFToZ3qYtPBscAI_LxXxawt26AG"></div>
	</div>
	<div class="form-group text-center">
		<button type="submit" class="btn btn-primary">Sign In</button>
	</div>
	<hr>
	<div class="form-group">
		<label for="forgot-password">Forgot Password</label> <a
			class="btn btn-primary" href="${contextPath}/signin/recruiter/forgot-password">Forgot
			Password</a>
	</div>
<!-- 	<hr> -->
	<div class="form-group hidden">
		<label for="new-user">New User</label>
		<c:choose>
			<c:when test="${not empty signupLink}">
				<a class="btn btn-primary" href=${contextPath}${signupLink}>Sign
					Up</a>
			</c:when>
			<c:otherwise>
				<a class="btn btn-primary link-recruiter-register">Sign Up</a>
			</c:otherwise>
		</c:choose>
	</div>
</form:form>
