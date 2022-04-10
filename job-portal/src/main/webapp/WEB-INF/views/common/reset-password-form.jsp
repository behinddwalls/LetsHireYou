<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<form:form action="${contextPath}/signin/forgot-password/reset"
	method="post" commandName="signinDataModel">
	<c:if test="${not empty errorMessage}">
		<span class="error">${errorMessage}</span>
		<hr />
	</c:if>
	<c:if test="${not empty successMessage}">
		<span class="success">${successMessage}</span>
		<hr />
	</c:if>
	<c:if test="${empty successMessage and empty errorMessage}">
		<div class="form-group">
			<label for="login-password">Enter New Password</label>
			<form:input type="password" class="form-control" id="login-password"
				path="password" />
			<form:errors path="password" cssClass="error" />
			<input type="hidden" value="${key}" name="key" /> <input
				type="hidden" value="${accountId}" name="accountId" /> <input
				type="hidden" value="${verify}" name="verify" />
		</div>
		<div class="form-group">
			<div class="g-recaptcha"
				data-sitekey="6LeqFgITAAAAADFToZ3qYtPBscAI_LxXxawt26AG"></div>
		</div>
		<div class="form-group text-center">
			<button type="submit" class="btn btn-primary">Submit</button>
		</div>
	</c:if>

</form:form>
