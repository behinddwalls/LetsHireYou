<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<form:form action="${contextPath}/register/jobseeker" method="post"
	commandName="jobseekerRegisterDataModel">
	<c:if test="${not empty errorMessage}">
		<span class="error">${errorMessage}</span>
		<hr />
	</c:if>

	<!-- ul class="social-login text-center">
		<li><a class="btn btn-linkedin"><i class="fa fa-linkedin"></i>Register
				with LinkedIn</a></li>
	</ul>
	<hr /-->
	<div class="form-group">
		<label for="register-name">First Name</label>
		<form:input type="text" class="form-control" id="register-name"
			path="firstName" />
		<form:errors path="firstName" cssClass="error" />
	</div>
	<div class="form-group">
		<label for="register-surname">Last Name</label>
		<form:input type="text" class="form-control" id="register-surname"
			path="lastName" />
		<form:errors path="lastName" cssClass="error" />
	</div>
	<div class="form-group">
		<label for="register-email">Email</label>
		<form:input type="email" class="form-control" id="register-email"
			path="emailId" />
		<form:errors path="emailId" cssClass="error" />
	</div>
	<div class="form-group">
		<label for="register-password1">Password</label>
		<form:input type="password" class="form-control"
			id="register-password1" path="password" />
		<form:errors path="password" cssClass="error" />
	</div>
	<div class="form-group">
		<label for="register-password2">Repeat Password</label>
		<form:input type="password" class="form-control"
			id="register-password2" path="verifyPassword" />
		<form:errors path="verifyPassword" cssClass="error" />
	</div>
	<div class="form-group">
		<div class="g-recaptcha"
			data-sitekey="6LeqFgITAAAAADFToZ3qYtPBscAI_LxXxawt26AG"></div>
	</div>
	<div class="form-group text-center">
		<button type="submit" class="btn btn-primary text-center">Register</button>
	</div>
</form:form>