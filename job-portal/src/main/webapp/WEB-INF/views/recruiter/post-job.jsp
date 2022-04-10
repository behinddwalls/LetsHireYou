<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<jsp:include page="../common/head.jsp"></jsp:include>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<!-- Main Stylesheet -->
<link href="<%=request.getContextPath()%>/resources/css/typeahead.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap-tagsinput.css"
	rel="stylesheet">

<section>

	<div class="container">
		<div class="row">
			<div class="col-sm-12">
				<h2 class="pull-left">Post New Job</h2>
				<a href="${contextPath}/recruiter/dashboard" class="pull-right"><i
					class="fa fa-arrow-left"></i> Go back to Dashboard</a>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-8 col-sm-offset-2">
				<div class="row">
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
				<form:form action="${contextPath}/recruiter/job/post-job"
					method="post" commandName="jobDataModel">
					<div id="jobId" style="visibility: hidden">
						<form:input type="hidden" class="form-control" id="jobId"
							path="jobId" />
						<form:input type="hidden" class="form-control" id="jobCreatedDate"
							path="jobCreatedDate" />
						<form:input type="hidden" class="form-control"
							id="jobModifiedDate" path="jobModifiedDate" />
						<form:input type="hidden" class="form-control" id="jobExpiaryDate"
							path="jobExpiaryDate" />
						<form:input type="hidden" class="form-control" id="jobStatus"
							path="jobStatus" />
					</div>
					<div class="form-group col-sm-12 ">
						<label for="job-title"><em>* Please complete all
								fields to allow us to find you the best candidates.</em></label>
					</div>
					<div class="form-group col-sm-12">
						<label for="job-title">Job Title</label>
						<form:input type="text" class="form-control typeahead"
							placeholder="Enter Job Title" id="job-title" path="title" />
						<form:errors path="title" cssClass="error" />
					</div>

					<div class="form-group col-sm-12">
						<label for="jobDescription">Job Description</label>
						<form:textarea path="jobDescription" class="form-control jobTitle"
							placeholder="Enter Job Description" id="job-description" />
						<form:errors path="jobDescription" cssClass="error" />
					</div>

					<div class="form-group col-sm-12">
						<label for="job-organisationName">Organisation </label>
						<form:input type="text" class="form-control typeahead"
							id="job-organisationName" path="organisationName"
							placeholder="Enter Job Organisation" />
						<form:errors path="organisationName" cssClass="error" />
					</div>

					<div class="form-group col-sm-12">
						<%-- label for="job-employmentType">Employment Type </label --%>
						<form:input path="employmentType" type="hidden" value="Full-time" />
						<%-- <form:select class="form-control" path="employmentType"
							id="field-employment-type">
							<form:option value="_CHOOSE_">Choose...</form:option>
							<form:option value="Full-time">Full-time</form:option>
							<form:option value="Part-time">Part-time</form:option>
							<form:option value="Contract">Contract</form:option>
							<form:option value="Temporary">Temporary</form:option>
							<form:option value="Volunteer">Volunteer</form:option>
							<form:option value="Other">Other</form:option>
							<form:errors path="employmentType" cssClass="error" />
						</form:select>
						<form:errors path="employmentType" cssClass="error" /> --%>
					</div>

					<div class="form-group col-sm-6">
						<label for="job-industryName">Industry Type</label>
						<form:select class="form-control" path="industryName"
							id="job-industryName">
							<form:option value="">Choose...</form:option>
							<form:option value="Accounting/Auditing">Accounting/Auditing</form:option>
							<form:option value="Advertising">Advertising</form:option>
							<form:option value="Analytics">Analytics</form:option>
							<form:option value="Art/Creative/Design">Art/Creative/Design</form:option>
							<form:option value="Banking and Financial Services">Banking and Financial Services</form:option>
							<form:option value="Consulting">Consulting</form:option>
							<form:option value="Education/Training">Education/Training</form:option>
							<form:option value="Healthcare">Healthcare</form:option>
							<form:option value="Human Resources">Human Resources</form:option>
							<form:option value="Information Technology">Information Technology</form:option>
							<form:option value="Legal">Legal</form:option>
							<form:option value="Logistics">Logistics</form:option>
							<form:option value="Manufacturing">Manufacturing</form:option>
							<form:option value="Pharmaceuticals">Pharmaceuticals</form:option>
							<form:option value="Public Relations">Public Relations</form:option>

						</form:select>
						<form:errors path="industryName" cssClass="error" />
					</div>
					<div class="form-group col-sm-6">
						<label for="job-jobFunction">Job Function </label>
						<form:select class="form-control" path="jobFunction"
							id="job-jobFunction">
							<form:option value="">Choose...</form:option>
							<form:option value="Business Development">Business Development</form:option>
							<form:option value="Finance">Finance</form:option>
							<form:option value="Human Resources">Human Resources</form:option>
							<form:option value="Information Technology">Information Technology</form:option>
							<form:option value="Marketing">Marketing</form:option>
							<form:option value="Operations">Operations</form:option>
							<form:option value="Quality Assurance">Quality Assurance</form:option>
							<form:option value="Research and Analytics">Research and Analytics</form:option>
							<form:option value="Sales">Sales</form:option>
							<form:option value="Strategy/Planning">Strategy/Planning</form:option>
							<form:option value="Supply Chain">Supply Chain</form:option>
						</form:select>
						<form:errors path="jobFunction" cssClass="error" />
					</div>

					<div class="form-group">
						<div class="col-sm-12">
							<label for="job-jobExperiance">Minimum Experience </label>
						</div>
						<div class="col-sm-12">
							<form:input type="number" class="form-control"
								id="job-jobExperiance" path="jobExperience"
								placeholder="Min-exp" min="0" step="1" />
							<form:errors path="jobExperience" cssClass="error" />
						</div>
						<!--  div class="col-sm-6">
							<form:input type="number" class="form-control"
								id="job-jobExperiance" path="jobExperience"
								placeholder="Max-exp" min="0" step="1" />
							<form:errors path="jobExperience" cssClass="error" />
						</div-->

					</div>
					<div class="clearfix"></div>
					<br>
					<div class="form-group col-sm-12">
						<label for="skill-name">Skills (Up to 10 skills)</label>
						<form:input type="text" class="form-control tagsinput"
							id="skill-name" path="skills" placeholder="Skills" />
						<form:errors path="skills" cssClass="error" />
					</div>
					<div class="form-group">
						<div class="col-sm-12">
							<label for="job-minSalary">Salary (in lakhs)</label>
						</div>
						<div class=" col-sm-6">
							<form:input type="number" value="" class="form-control col-sm-4"
								path="minSalary" id="job-minSalary" placeholder="Minimum"
								min="0" step="1" />
							<form:errors path="minSalary" cssClass="error" />
						</div>
						<div class="col-sm-6">
							<form:input type="number" value="" class="form-control col-sm-4"
								path="maxSalary" id="job-maxSalary" placeholder="Maximum"
								max="100" step="1" />
							<form:errors path="maxSalary" cssClass="error" />
						</div>
					</div>

					<div class="form-group col-sm-12">
						<form:input type="hidden" value="INR" class="form-control"
							path="salaryCurrencyCode" id="job-salaryCurrencyCode" />

						<%-- <label for="job-salaryCurrencyCode">Salary Currency </label>
						
						<form:select class="form-control" path="salaryCurrencyCode"
							type="hidden" id="job-salaryCurrencyCode">
							<form:option selected="selected" value="INR" label="INR" />
						</form:select>
						<form:errors path="salaryCurrencyCode" cssClass="error" /> --%>
					</div>

					<div class="form-group col-sm-6">
						<form:checkbox path="keepSalaryHidden" id="job-keepSalaryHidden" />
						<form:errors path="keepSalaryHidden" cssClass="error" />
						<label for="job-keepSalaryHidden">&nbsp; Keep Salary
							Hidden</label>
					</div>
					<div class="form-group col-sm-6">
						<form:checkbox path="topTierOnly" id="job-topTierOnly" />
						<form:errors path="topTierOnly" cssClass="error" />
						<label for="job-topTierOnly">&nbsp; Only Top Tier
							institutes</label>
					</div>


					<div class="form-group col-sm-12">
						<label for="job-location">Location</label>
						<form:input type="text" class="form-control autocompleteAddress"
							id="job-location" path="location" />
						<form:errors path="location" cssClass="error" />
					</div>


					<div class="form-group col-sm-12 hidden">
						<h5>How candidates apply</h5>
						<ul class="applicant-setting-types">
							<li><label for="field-applicant-routing-email" class="radio">
									<form:radiobutton path="applicantSettingTypes"
										checked="checked" value="EMAIL" id="id-applicantSettingTypes"
										data-value="email" /> Collect applications via JOBXR.
							</label></li>
							<li><label for="field-applicant-routing-external-url"
								class="radio"> <form:radiobutton
										path="applicantSettingTypes" value="EXTERNALURLLINK"
										class="job-applicantSettingTypes" data-value="url" /> Direct
									applicants to an external site to apply: <form:input
										type="text" name="extApplyUrl"
										placeholder="http://www.exampleJpb.com/job123"
										path="linkToExternalSite" value="" /> <form:errors
										path="linkToExternalSite" cssClass="error" /> <script
										id="control-7" type="linkedin/control" class="li-control">LI.Controls.addControl('control-7','GhostLabel',{});</script>
							</label></li>
						</ul>
					</div>

					<div class="clearfix"></div>
					<div class="form-group text-center">
						<button type="submit" class="btn btn-primary">Submit Job</button>
					</div>


				</form:form>
			</div>
		</div>

	</div>

</section>
<!-- ============ FOOTER START ============ -->
<jsp:include page="../common/footer.jsp"></jsp:include>

<!-- ============ FOOTER END ============ -->

<!-- tag Input -->
<script
	src="<%=request.getContextPath()%>/resources/js/bootstrap-tagsinput.min.js"></script>


<!-- jQuery Settings -->
<script type="text/javascript">
    $('#job-title.typeahead').typeahead({
        hint : true,
        highlight : true,
        minLength : 1
    }, {
        name : 'job-title',
        source : getBloodHoundObject("${contextPath}/role/search?roleName=")
    });

    $('#skill-name').tagsinput({
        typeaheadjs : {
            name : 'skill-name',
            source : getBloodHoundObject("${contextPath}/skilldetail/search?skillName=")
        },
        freeInput : true,
        highlight : true,
        minLength : 1,
        maxTags : 10
    });

    $('#job-organisationName.typeahead').typeahead({
        freeInput : true,
        hint : true,
        highlight : true,
        minLength : 1
    }, {
        name : 'job-organisationName',
        source : getBloodHoundObject("${contextPath}/organisation/search?orgName=")
    });

    $('#job-industryName.typeahead').typeahead({
        freeInput : true,
        hint : true,
        highlight : true,
        minLength : 1
    }, {
        name : 'job-industryName',
        source : getBloodHoundObject("${contextPath}/industry/search?industryName=")
    });

    $('.bootstrap-tagsinput').addClass('form-control');
    $('.bootstrap-tagsinput').attr("style", "background-color: #f8f8f8;height:auto !important;min-height:40px;border: 1px solid #e7e7e7;box-shadow: none;");
    //     $('.bootstrap-tagsinput input').attr("style", "width :auto !important;min-width: 200px;");
</script>
