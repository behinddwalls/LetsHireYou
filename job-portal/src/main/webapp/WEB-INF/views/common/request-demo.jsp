<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<jsp:include page="../common/head.jsp"></jsp:include>
<section>
	<div class="clearfix"></div>

	<div
		class="col-sm-12 col-xs-12 col-md-6 col-md-offset-3 auth-form container">
		<h2 class="col-sm-12">Request Demo</h2>


		<c:set var="contextPath" value="${pageContext.request.contextPath}" />
		<form:form action="${contextPath}/register/request/demo" method="post"
			commandName="requestDemoDetail">
			<c:if test="${not empty success}">
				<span class="succes">Thanks You for requesting demo. We will
					get back to you shortly.</span>
				<hr />
			</c:if>
			<c:if test="${not empty error}">
				<span class="error">${error}</span>
				<hr />
			</c:if>
			<c:if test="${empty success}">
				<div class="form-group">
					<label for="name">Full Name</label>
					<form:input type="text" class="form-control" id="name" path="name" />
					<form:errors path="name" cssClass="error" />
				</div>

				<div class="form-group">
					<label for="company-name">Company Name</label>
					<form:input type="text" class="form-control" id="company-name"
						path="companyName" />
					<form:errors path="companyName" cssClass="error" />
				</div>

				<div class="form-group">
					<label for="email-id">Email</label>
					<form:input type="email" class="form-control" id="email-id"
						path="emailId" />
					<form:errors path="emailId" cssClass="error" />
				</div>

				<div class="form-group">
					<label for="mobile-number">Mobile number</label>
					<form:input type="text" class="form-control" id="mobile-number"
						path="mobileNumber" />
					<form:errors path="mobileNumber" cssClass="error" />
				</div>
				<div class="form-group">
					<div class="g-recaptcha"
						data-sitekey="6LeqFgITAAAAADFToZ3qYtPBscAI_LxXxawt26AG"></div>
				</div>
				<div class="form-group text-center">
					<button type="submit" class="btn btn-primary text-center">Request
						Demo</button>
				</div>
			</c:if>
		</form:form>

	</div>

	<div class="clearfix"></div>
</section>
<!-- ============ FOOTER START ============ -->

<jsp:include page="../common/footer.jsp"></jsp:include>

<!-- ============ FOOTER END ============ -->