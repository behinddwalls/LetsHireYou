<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<form:form action="${contextPath}/register/recruiter"
	commandName="recruiterRegisterDataModel">
	<c:if test="${not empty errorMessage}">
		<span class="error">${errorMessage}</span>
		<hr />
	</c:if>

	<!-- ul class="social-login">
		<li><a class="btn btn-linkedin"><i class="fa fa-linkedin"></i>Register
				with LinkedIn</a></li>
	</ul>
	<hr-->
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
			path="workEmailId" />
		<form:errors path="workEmailId" cssClass="error" />
	</div>
	<div class="form-group">
		<label for="register-recruiter-type">Recruiter Type</label>
		<form:select class="form-control" id="register-recruiter-type"
			path="recruiterType">
			<c:forEach items="${recruiterTypeMap}" var="entry">
				<form:option value="${entry.key}">${entry.value}</form:option>
			</c:forEach>
		</form:select>
		<form:errors path="recruiterType" cssClass="error" />
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
