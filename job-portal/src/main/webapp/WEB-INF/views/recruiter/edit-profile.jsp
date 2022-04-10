<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<jsp:include page="../common/head.jsp"></jsp:include>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<section>
	<div class="container">
		<div class="row">
			<div class="col-sm-12 col-sm-offset-0 ">
				<h2 class="pull-left margin-0">My Profile</h2>
				<a href="${contextPath}/recruiter/dashboard" class="pull-right"><i
					class="fa fa-arrow-left"></i> Go back to Dashboard</a>
			</div>
		</div>
		<c:if test="${not empty incomplete}">
			<div
				class="col-sm-10 col-sm-offset-1 alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				${incomplete}
			</div>

		</c:if>
		<c:if test="${not empty errorMessage}">
			<div
				class="col-sm-10 col-sm-offset-1 alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				${errorMessage}
			</div>

		</c:if>
		<c:if test="${not empty successMessage}">
			<div
				class="col-sm-10 col-sm-offset-1 alert alert-success alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				${successMessage}
			</div>

		</c:if>
		<div class="row">
			<div class="col-sm-12 ">
				<br>
				<form:form commandName="recruiterProfileDataModel" method="post"
					action="${contextPath}/recruiter/profile">
					<div class="col-sm-10 col-sm-offset-1">
						<h5>Your Details</h5>
						<br />
						<form:input type="hidden" class="form-control" id="user-id"
							path="userBasicDataModel.userId" />

						<div class="form-group col-sm-6">
							<label for="first-name">First Name</label>
							<form:input type="text" class="form-control" id="first-name"
								path="userBasicDataModel.firstName" required="required" />
							<form:errors path="userBasicDataModel.firstName" cssClass="error" />
						</div>
						<div class="form-group col-sm-6">
							<label for="last-name">Last Name</label>
							<form:input type="text" class="form-control" id="last-name"
								path="userBasicDataModel.lastName" />
							<form:errors path="userBasicDataModel.lastName" cssClass="error" />
						</div>
						<div class="form-group col-sm-6">
							<label for="profile-headline">Profile Headline</label>
							<form:input type="text" class="form-control"
								id="profile-headline" path="userBasicDataModel.profileHeadline"
								required="required" />
							<form:errors path="userBasicDataModel.profileHeadline"
								cssClass="error" />
						</div>
						<div class="form-group col-sm-6">
							<label for="mobile-number">Mobile Number (Your 10-digit
								mobile number)</label>
							<form:input type="number" class="form-control" id="mobile-number"
								path="userBasicDataModel.mobileNumber" required="required" />
							<form:errors path="userBasicDataModel.mobileNumber"
								cssClass="error" />
						</div>
<!-- 						<div class="form-group col-sm-6"> -->
<!-- 							<label for="other-mobile-number">Other Contact Numbers -->
<!-- 								(Optional)</label> -->
<%-- 							<form:input type="text" class="form-control" --%>
<%-- 								id="other-mobile-number" --%>
<%-- 								path="userBasicDataModel.otherMobileNumber" /> --%>
<%-- 							<form:errors path="userBasicDataModel.otherMobileNumber" --%>
<%-- 								cssClass="error" /> --%>
<!-- 						</div> -->
						<!-- 						<br /> -->
						<div class="clearfix"></div>
						<!-- 						<h5>Currently working at</h5> -->
						<!-- 						<br /> -->
						<form:input type="hidden" class="form-control" id="experience-id"
							path="userExperienceDataModel.experienceId" />
						<div class="form-group col-sm-6">
							<label for="company-name">Company </label>
							<form:input type="text" class="form-control typeahead"
								id="company-name" path="userExperienceDataModel.companyName"
								required="required" />
							<form:errors path="userExperienceDataModel.companyName"
								cssClass="error" />
						</div>
						<div class="form-group col-sm-6">
							<label for="role-name">Role </label>
							<form:input type="text" class="form-control typeahead"
								id="role-name" path="userExperienceDataModel.roleName"
								required="required" />
							<form:errors path="userExperienceDataModel.roleName"
								cssClass="error" />
						</div>
						<div class="form-group col-sm-6">
							<label for="start-date">Start Date </label>
							<form:input type="date" class="form-control" id="start-date"
								path="userExperienceDataModel.startDate" required="required" />
							<form:errors path="userExperienceDataModel.startDate"
								cssClass="error" />
						</div>
						<div class="form-group col-sm-6 hidden">
							<label for="end-date">End Date </label>
							<form:input type="date" class="form-control" id="end-date"
								data-date-format="yyyy-mm-dd"
								path="userExperienceDataModel.endDate" />
							<form:errors path="userExperienceDataModel.endDate"
								cssClass="error" />
						</div>
						<div class="form-group col-sm-6">
							<label for="location">Location </label>
							<form:input type="text" class="form-control autocompleteAddress"
								id="location" path="userExperienceDataModel.location"
								required="required" />
							<form:errors path="userExperienceDataModel.location"
								cssClass="error" />
						</div>
						<!-- 						<div class="form-group col-sm-12"> -->
						<!-- 							<label for="description">Description </label> -->
						<%-- 							<form:textarea class="form-control" id="description" --%>
						<%-- 								path="userExperienceDataModel.description" /> --%>
						<%-- 							<form:errors path="userExperienceDataModel.description" --%>
						<%-- 								cssClass="error" /> --%>
						<!-- 						</div> -->
						<div class="form-group col-sm-12 text-center">
							<br />
							<button type="submit" class="btn btn-primary ">Update
								Profile</button>
						</div>
					</div>
				</form:form>
			</div>

		</div>

	</div>
</section>



<!-- ============ FOOTER START ============ -->

<jsp:include page="../common/footer.jsp"></jsp:include>

<!-- ============ FOOTER END ============ -->

<script type="text/javascript">
    $('#role-name.typeahead').typeahead({
        hint : true,
        highlight : true,
        minLength : 1
    }, {
        name : 'role-name',
        source : getBloodHoundObject("${contextPath}/role/search?roleName=")
    });

    $('#company-name.typeahead').typeahead({
        hint : true,
        highlight : true,
        minLength : 1
    }, {
        name : 'company-name',
        source : getBloodHoundObject("${contextPath}/organisation/search?orgName=")
    });
</script>

