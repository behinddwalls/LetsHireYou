<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<jsp:include page="../common/head.jsp"></jsp:include>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<style type="text/css">
.margin-0 {
	margin: 0;
}

.padding-0 {
	padding: 0;
}

.jobseeker-profile-content #userProfileHeader h2,
	.jobseeker-profile-content #userProfileHeader h3,
	.jobseeker-profile-content #userProfileHeader h4 {
	margin: 6px 0;
}

.jobseeker-profile-content #userProfileHeader h2::after {
	display: none;
}

.jobseeker-profile-content #userProfileHeader p {
	margin-bottom: 0px;
}

.jobseeker-profile-content section {
	padding-bottom: 0px;
	padding-top: 20px;
}

.jobseeker-profile-content section h3 {
	margin: -20px auto;
}

.jobseeker-profile-content span.badge {
	padding: 4px 9px;
	font-size: 13px;
}

.jobseeker-profile-content hr.hr-top {
	margin-top: 0px;
}

.jobseeker-profile-content hr.hr-bottom {
	margin-bottom: 20px;
}

.work-experience {
	overflow: auto;
}
.bootstrap-tagsinput, .bootstrap-tagsinput input {
	min-width:200px;
}
</style>

<div class="jobseeker-profile-content">

	<!-- ============ USER PROFILE-HEADER START ============ -->
	<div class="container" style="" id="userProfileHeaderForm"></div>

	<section id="JobSeekerBasic" style="padding-bottom: 0px;">
		<div class="container" id="JobSeekerProfileHeader">
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<c:if test="${not empty incomplete}">
				<div
					class="col-sm-10 col-sm-offset-1 alert alert-danger alert-dismissable profileInComplete">
					<button type="button" class="close" data-dismiss="alert"
						aria-hidden="true">&times;</button>
					${incomplete}
				</div>
				<div
					class="hidden col-sm-10 col-sm-offset-1 alert alert-success alert-dismissable profileComplete">
					<button type="button" class="close" data-dismiss="alert"
						aria-hidden="true">&times;</button>
					Profile updated successfully
				</div>

			</c:if>
			<div class="pull-left col-sm-3 text-center hidden-xs"
				style="width: 200px;">
				<div class="">
					<img class="img-thumbnail user-profile-img"
						src="${not empty userData.userBasicDataModel.profileImageUrl ? userData.userBasicDataModel.profileImageUrl : 
					'https://static.licdn.com/scds/common/u/images/themes/katy/ghosts/person/ghost_person_200x200_v1.png
					'}"
						style="height: 150px; width: 150px;">
					<p class="margin-0 text-capitalize">
						<a class="editUserProfileImage"><i class="fa fa-camera"></i>
							Update Image</a>
					</p>
				</div>
			</div>

			<div class="col-sm-8 text-xs-center" id="userProfileHeader"></div>
		</div>

		<!--   -->

		<!--  -->
		<!-- 	<div class="hidden"> -->
		<!-- 		<h3>Upload Resume:</h3> -->
		<!-- 		Select a file to upload: <br /> -->
		<%-- 		<form:form id="uploadResume" method="post" --%>
		<%-- 			enctype="multipart/form-data"> --%>
		<!-- 			<input type="file" name="resumeData" /> -->
		<!-- 			<br /> -->
		<!-- 			<button type="submit" value="Upload File" id="btn-resume">Upload -->
		<!-- 				Resume</button> -->
		<%-- 		</form:form> --%>
		<!-- 	</div> -->

	</section>


	<!-- =================USER PROFILE-HEADER END============ -->
	
	<!-- =================USER SKILL START============ -->

	<section id="UserSkill">
		<div class="container" id="UserSkillContainer">

			<div class="col-sm-10">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-bolt"></i> Skills <i
						style="font-size: 45%; margin-right: 4px; color: rgb(255, 00, 00); position: relative; top: -10px;"
						class="fa fa-asterisk"></i><span class="pull-right"
						style="margin-right: 10px; color: #0d7076;"><i
							class="fa fa-plus add-user-skill"></i></span></span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div id="listOfUserSkill"></div>
		</div>

	</section>

	<!-- =================USER SKILL END============ -->
	

	<!-- =================USER EDUCATION START============ -->

	<section id="UserEducation">
		<div class="container" id="UserEducationContainer">

			<div class="col-sm-10">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-graduation-cap"></i>
						Education <i
						style="font-size: 45%; margin-right: 4px; color: rgb(255, 00, 00); position: relative; top: -10px;"
						class="fa fa-asterisk"></i><span class="pull-right"
						style="margin-right: 10px; color: #0d7076;"><i
							class="fa fa-plus" id="add-user-education"></i></span></span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div id="listOfUserEducation"></div>
		</div>

	</section>

	<!-- =================USER EDUCATION END============ -->

	<!-- ============ UserJobExperience START ============ -->
	<section id="JobExperience">
		<div class="container" id="JobExperienceContainer">
			<div class="col-sm-10">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-briefcase"></i>
						Experience <span class="pull-right"
						style="margin-right: 10px; color: #0d7076;"><i
							class="fa fa-plus" id="add-job-experience"></i></span></span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="" id="listOfJobExperiences"></div>
		</div>

	</section>
	<!-- =================USERJOBEXPERIENCE END============ -->

	<!-- =================USER PROJECT START============ -->

	<section id="UserProject">
		<div class="container" id="UserProjectContainer">
			<div class="col-sm-10">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-book"></i> Projects
						<span class="pull-right"
						style="margin-right: 10px; color: #0d7076;"><i
							class="fa fa-plus" id="add-user-project"></i></span></span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div id="listOfUserProject"></div>
		</div>

	</section>

	<!-- =================USER PROJECT END============ -->
	<!-- =================USER AWARD START============ -->

	<section id="UserAward">
		<div class="container" id="UserAwardContainer">
			<div class="col-sm-10">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-trophy"></i> Awards
						<span class="pull-right"
						style="margin-right: 10px; color: #0d7076;"><i
							class="fa fa-plus" id="add-user-award"></i></span></span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div id="listOfUserAward"></div>
		</div>

	</section>

	<!-- =================USER AWARD END============ -->

	<!-- =================USER PUBLICATION START============ -->

	<section id="UserPublication">
		<div class="container" id="UserPublicationContainer">
			<div class="col-sm-10">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-file-text"></i>
						Publications <span class="pull-right"
						style="margin-right: 10px; color: #0d7076;"><i
							class="fa fa-plus" id="add-user-publication"></i></span></span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="" id="listOfUserPublication"></div>
		</div>

	</section>

	<!-- =================USER PUBLICATION END============ -->

	<!-- =================USER VOLUNTEER START============ -->

	<section id="UserVolunteer">
		<div class="container" id="UserVolunteerContainer">

			<div class="col-sm-10">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-angellist"></i>
						Volunteering Experience <span class="pull-right"
						style="margin-right: 10px; color: #0d7076;"><i
							class="fa fa-plus" id="add-user-volunteer"></i></span></span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="" id="listOfUserVolunteer"></div>
		</div>

	</section>

	<!-- =================USER VOLUNTEER END============ -->

	<!-- =================USER TESTS START============ -->

	<section id="UserTestScores">
		<div class="container" id="UserTestContainer">


			<div class="col-sm-10">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-puzzle-piece"></i>
						Test Scores <span class="pull-right"
						style="margin-right: 10px; color: #0d7076;"><i
							class="fa fa-plus" id="add-user-test"></i></span></span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="" id="listOfUserTest"></div>
		</div>

	</section>

	<!-- =================USER TESTS END============ -->
	<!-- =================USER PATENT START============ -->

	<section id="UserPatent">
		<div class="container" id="UserPatentContainer">
			<div class="col-sm-10">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-lightbulb-o"></i>
						Patents <span class="pull-right"
						style="margin-right: 10px; color: #0d7076;"><i
							class="fa fa-plus" id="add-user-patent"></i></span></span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="" id="listOfUserPatent"></div>
		</div>

	</section>

	<!-- =================USER PATENT END============ -->
	<!-- =================USER CERTIFICATION START============ -->

	<section id="UserCertification">
		<div class="container" id="UserCertificationContainer">
			<div class="col-sm-10">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-newspaper-o"></i>
						Certifications <span class="pull-right"
						style="margin-right: 10px; color: #0d7076;"><i
							class="fa fa-plus" id="add-user-certification"></i></span></span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="" id="listOfUserCertification"></div>
		</div>

	</section>

	<!-- =================USER CERTIFICATION END============ -->
</div>

<jsp:include page="../user/popup-public-profile.jsp"></jsp:include>

<jsp:include page="../common/footer.jsp"></jsp:include>
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap-tagsinput.css"
	rel="stylesheet">

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobseekerExperienceDetails.jsp"></jsp:include>
<jsp:include page="JobseekerEducationDetails.jsp"></jsp:include>
<jsp:include page="JobseekerProjectDetails.jsp"></jsp:include>
<jsp:include page="JobseekerPublicationDetails.jsp"></jsp:include>
<jsp:include page="JobseekerVolunteerDetails.jsp"></jsp:include>
<jsp:include page="JobseekerTestScoreDetails.jsp"></jsp:include>
<jsp:include page="JobseekerPatentDetails.jsp"></jsp:include>
<jsp:include page="JobseekerCertificationDetails.jsp"></jsp:include>
<jsp:include page="JobseekerSkillDetails.jsp"></jsp:include>
<jsp:include page="JobseekerAwardDetails.jsp"></jsp:include>
<jsp:include page="JobseekerProfileHeader.jsp"></jsp:include>


<script type="text/javascript">
	$(function() {

		getJobExperienceDetails();
		getUserEducationDetails();
		getUserSkillDetails();
		getUserProjectDetails();
		getUserAwardDetails();
		getUserPublicationDetails();
		getUserCertificationDetails();
		getUserPatentDetails();
		getUserTestDetails();
		getUserVolunteerDetails();
		refreshAllUserProfileHeaderDetails();

		//view profile
		$("#JobSeekerBasic").on('click', '.link-public-profile', function(e) {
			getJobseekerResumePublic({
				"jobseekerId" : "${userId}"
			});
			$("#public-profile").fadeIn(300);
			$("body").addClass("no-scroll");
		});

		$("#public-profile .close").click(function() {
			console.log("close called");
			$("#public-profile").fadeOut(300);
			$("body").removeClass("no-scroll");
			hideAllProfileSections();
		});

	});
</script>