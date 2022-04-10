<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<jsp:include page="../common/head.jsp"></jsp:include>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<c:if test="${isRecruiter}">
	<c:set var="dashboardLink" value="${contextPath}/recruiter/dashboard" />
</c:if>
<c:if test="${isJobseeker}">
	<c:set var="dashboardLink" value="${contextPath}/jobseeker/dashboard" />
</c:if>

<section>

	<div class="container">
		<div class="row">
			<div class="col-sm-12">
				<h2>My Account</h2>
				<a href="${dashboardLink}" class="pull-right"><i
					class="fa fa-arrow-left"></i> Go back to Dashboard</a>
			</div>
		</div>
		<div class="row">
			<ul class="nav nav-tabs">
				<li role="presentation" class="active"><a
					href="${contextPath}/account">Basic Settings </a></li>
			</ul>
			<c:if test="${not empty errorMessage}">
				<div class="col-sm-12 alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert"
						aria-hidden="true">&times;</button>
					${errorMessage}
				</div>

			</c:if>
			<c:if test="${not empty successMessage}">
				<div class="col-sm-12 alert alert-success alert-dismissable">
					<button type="button" class="close" data-dismiss="alert"
						aria-hidden="true">&times;</button>
					${successMessage}
				</div>

			</c:if>
		</div>
		<br />
		<div class="row">
			<div class="col-sm-12">
				<c:if
					test="${not empty basicAccountSettingsDataModel.newUnverfiedEmailId}">
					<label for="new-unverified-email-id">Un-Verified Email : <b>${basicAccountSettingsDataModel.newUnverfiedEmailId}</b>.
						Please verify your mail to use this.
					</label>
				</c:if>
				<br />
				<c:if
					test="${not empty basicAccountSettingsDataModel.newUnverfiedWorkEmailId}">
					<label for="new-unverified-work-email-id">Un-Verified Work
						Email : <b>${basicAccountSettingsDataModel.newUnverfiedWorkEmailId}</b>.
						Please verify your mail to use this.
					</label>
				</c:if>
			</div>
			<div class="col-xs-12 col-md-5 pull-left">
				<h3>Update Email</h3>
				<form:form action="${contextPath}/account/updateEmail" method="post"
					commandName="basicAccountSettingsDataModel">
					<div class="form-group">
						<label for="email-id">Personal Email ID</label>
						<form:input type="text" class="form-control" id="email-id"
							path="emailId" />
						<form:errors path="emailId" cssClass="error" />
					</div>
					<div class="form-group">
						<label for="work-email-id">Work Email ID</label>
						<form:input type="text" class="form-control" id="work-email-id"
							path="workEmailId" />
						<form:errors path="workEmailId" cssClass="error" />
					</div>
					<div class="form-group  text-center">
						<br />
						<button type="submit" class="btn btn-primary ">Update
							Email</button>
					</div>
				</form:form>
			</div>
			<div class="col-xs-12 col-md-5  pull-right">
				<h3>Update Password</h3>
				<form:form action="${contextPath}/account/updatePassword"
					method="post" commandName="basicAccountSettingsDataModel">
					<div class="form-group">
						<label for="email-id">Password</label>
						<form:input type="password" class="form-control" id="email-id"
							path="password" required="required" />
						<form:errors path="password" cssClass="error" />
					</div>
					<div class="form-group">
						<label for="email-id">Confirm Password</label>
						<form:input type="password" class="form-control" id="email-id"
							path="verifyPassword" required="required" />
						<form:errors path="verifyPassword" cssClass="error" />
					</div>
					<div class="form-group text-center">
						<br />
						<button type="submit" class="btn btn-primary ">Update
							Password</button>
					</div>
				</form:form>
			</div>
		</div>

	</div>

</section>
<!-- ============ FOOTER START ============ -->

<jsp:include page="../common/footer.jsp"></jsp:include>

<!-- ============ FOOTER END ============ -->