<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<style type="text/css">
#profile-content {
	padding: 10px;
	background: #fff;
	overflow: auto;
	border-radius: 0 0 5px 5px;
}

#profile-content section {
	padding: 0;
}

@media ( min-width : 992px) {
	#profile-content .container {
		width: 890px;
	}
}

#profile-content section {
	padding-bottom: 0px;
}

#profile-content section h3 {
	margin: 0px auto;
}

#profile-content span.badge {
	padding: 4px 9px;
	font-size: 13px;
}

#profile-content hr.hr-top {
	margin-top: 0px;
}

#profile-content hr.hr-bottom {
	margin-bottom: 20px;
}

#profile-content .work-experience {
	/* 	overflow: auto; */
	
}

#profile-content .fa.fa-pencil {
	display: none;
}
</style>
<div id="profile-content">

	<!-- ============ USER BASIC DETAIL  START ============ -->
	<section id="UserBasicDetailsPublic" style="display: none">
		<div class="container" id="UserBasicDetailsContainerPublic">
			<div class="col-sm-10">
				<!-- h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-user "></i>
						Basic Detail</span>
				</h3>
				<hr-->
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfUserBasicDetailsPublic"></div>
		</div>

	</section>
	<!-- =================USER BASIC DETAIL END============ -->

	<!-- ============ USER CONATACT DETAIL START ============ -->
	<section id="UserContactDetailsPublic" style="display: none">
		<div class="container" id="UserContactDetailsContainerPublic">
			<div class="col-sm-10">
				<hr class="hr-top">
				<h3 class="margin-0">
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-mobile"></i> Contact
						Details</span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfUserContactDetailsPublic"></div>
		</div>

	</section>
	<!-- =================USER CONATACT DETAIL END============ -->

	<!-- =================USER SKILL START============ -->

	<section id="UserSkillPublic" style="display: none">
		<div class="container" id="UserSkillContainerPublic">

			<div class="col-sm-10" class="margin-0">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-bolt "></i> Skills </span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfUserSkillPublic"></div>
		</div>

	</section>

	<!-- =================USER SKILL END============ -->

	<!-- ============ UserJobExperience START ============ -->
	<section id="JobExperiencePublic" style="display: none">
		<div class="container" id="JobExperienceContainerPublic">
			<div class="col-sm-10" class="margin-0">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-briefcase"></i>
						Experience</span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfJobExperiencesPublic"></div>
		</div>

	</section>
	<!-- =================USERJOBEXPERIENCE END============ -->

	<!-- =================USER EDUCATION START============ -->

	<section id="UserEducationPublic" style="display: none">
		<div class="container" id="UserEducationContainerPublic">

			<div class="col-sm-10" class="margin-0">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-graduation-cap "></i>
						Education </span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfUserEducationPublic"></div>
		</div>

	</section>

	<!-- =================USER EDUCATION END============ -->

	<!-- =================USER PROJECT START============ -->

	<section id="UserProjectPublic" style="display: none">
		<div class="container" id="UserProjectContainerPublic">
			<div class="col-sm-10" class="margin-0">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-book "></i> Projects
					</span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfUserProjectPublic"></div>
		</div>

	</section>

	<!-- =================USER PROJECT END============ -->
	<!-- =================USER AWARD START============ -->

	<section id="UserAwardPublic" style="display: none">
		<div class="container" id="UserAwardContainerPublic">
			<div class="col-sm-10" class="margin-0">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-trophy "></i> Awards
					</span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfUserAwardPublic"></div>
		</div>

	</section>

	<!-- =================USER AWARD END============ -->

	<!-- =================USER PUBLICATION START============ -->

	<section id="UserPublicationPublic" style="display: none">
		<div class="container" id="UserPublicationContainerPublic">
			<div class="col-sm-10" class="margin-0">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-file-text "></i>
						Publications </span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfUserPublicationPublic"></div>
		</div>

	</section>

	<!-- =================USER PUBLICATION END============ -->

	<!-- =================USER VOLUNTEER START============ -->

	<section id="UserVolunteerPublic" style="display: none">
		<div class="container" id="UserVolunteerContainerPublic">

			<div class="col-sm-10" class="margin-0">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-angellist "></i>
						Volunteering Experience </span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfUserVolunteerPublic"></div>
		</div>

	</section>

	<!-- =================USER VOLUNTEER END============ -->

	<!-- =================USER TESTS START============ -->

	<section id="UserTestScoresPublic" style="display: none">
		<div class="container" id="UserTestContainerPublic">


			<div class="col-sm-10" class="margin-0">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-puzzle-piece"></i>
						Test Scores </span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfUserTestPublic"></div>
		</div>

	</section>

	<!-- =================USER TESTS END============ -->
	<!-- =================USER PATENT START============ -->

	<section id="UserPatentPublic" style="display: none">
		<div class="container" id="UserPatentContainerPublic">
			<div class="col-sm-10" class="margin-0">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-lightbulb-o "></i>
						Patents </span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfUserPatentPublic"></div>
		</div>

	</section>

	<!-- =================USER PATENT END============ -->
	<!-- =================USER CERTIFICATION START============ -->

	<section id="UserCertificationPublic" style="display: none">
		<div class="container" id="UserCertificationContainerPublic">
			<div class="col-sm-10" class="margin-0">
				<hr class="hr-top">
				<h3>
					<span style="margin-top: 10px;"><i
						style="color: rgb(13, 112, 118)" class="fa fa-newspaper-o "></i>
						Certifications </span>
				</h3>
				<hr class="hr-bottom">
			</div>
			<!-- DO NOT Touch this DIV, Many Jquery are attached to it -->
			<div class="container" id="listOfUserCertificationPublic"></div>
		</div>

	</section>

	<!-- =================USER CERTIFICATION END============ -->
	<br />
</div>

<script>
	function hideAllProfileSections() {
		$('#JobExperiencePublic').hide();
		$('#UserEducationPublic').hide();
		$('#UserSkillPublic').hide();
		$('#UserCertificationPublic').hide();
		$('#UserPatentPublic').hide();
		$('#UserTestScoresPublic').hide();
		$('#UserVolunteerPublic').hide();
		$('#UserPublicationPublic').hide();
		$('#UserAwardPublic').hide();
		$('#UserProjectPublic').hide();
	}

	function getJobseekerResumePublic(data) {
		console.log("yeah called")
		refreshUserBasicDetailsPublic(data);
		refreshUserContactDetailsPublic(data);
		refreshJobExperienceDetailsPublic(data);
		refreshAllUserEducationDetailsPublic(data);
		refreshAllUserSkillDetailsPublic(data);
		refreshAllUserProjectDetailsPublic(data);
		refreshAllUserAwardDetailsPublic(data);
		refreshAllUserPublicationDetailsPublic(data);
		refreshAllUserCertificationDetailsPublic(data);
		refreshAllUserPatentDetailsPublic(data);
		refreshAllUserTestDetailsPublic(data);
		refreshAllUserVolunteerDetailsPublic(data);
	}

	function refreshUserBasicDetailsPublic(data) {
		removeAllUserBasicDetailFromHTMLPublic();
		getUserBasicDetailsPublic(data); // then add new data
	}

	function removeAllUserBasicDetailFromHTMLPublic() {
		clearHtmlDom($('#listOfUserBasicDetailsPublic div'), $)
	}

	function refreshUserContactDetailsPublic(data) {
		removeAllUserContactDetailsFromHTMLPublic();
		getUserContactDetailsPublic(data); // then add new data
	}

	function removeAllUserContactDetailsFromHTMLPublic() {
		clearHtmlDom($('#listOfUserContactDetailsPublic div'), $)
	}

	function refreshJobExperienceDetailsPublic(data) {
		removeAllJobExperienceFromHTMLPublic();
		getJobExperienceDetailsPublic(data); // then add new data
	}

	function removeAllJobExperienceFromHTMLPublic() {
		clearHtmlDom($('#listOfJobExperiencesPublic div'), $)
	}

	function refreshAllUserEducationDetailsPublic(data) {
		removeAllUserEducationFromHTMLPublic();
		getUserEducationDetailsPublic(data);
	}

	function removeAllUserEducationFromHTMLPublic() {
		clearHtmlDom($('#listOfUserEducationPublic div'), $);
	}

	function refreshAllUserProjectDetailsPublic(data) {
		removeAllUserProjectFromHTMLPublic();
		getUserProjectDetailsPublic(data);
	}

	function removeAllUserProjectFromHTMLPublic() {
		clearHtmlDom($('#listOfUserProjectPublic div'), $)
	}

	function refreshAllUserPublicationDetailsPublic(data) {
		removeAllUserPublicationFromHTMLPublic();
		getUserPublicationDetailsPublic(data);
	}

	function removeAllUserPublicationFromHTMLPublic() {
		clearHtmlDom($('#listOfUserPublicationPublic div'), $)
	}

	function refreshAllUserVolunteerDetailsPublic(data) {
		removeAllUserVolunteerFromHTMLPublic();
		getUserVolunteerDetailsPublic(data);
	}

	function removeAllUserVolunteerFromHTMLPublic() {
		clearHtmlDom($('#listOfUserVolunteerPublic div'), $)
	}

	function refreshAllUserTestDetailsPublic(data) {
		removeAllUserTestFromHTMLPublic();
		getUserTestDetailsPublic(data);
	}

	function removeAllUserTestFromHTMLPublic() {
		clearHtmlDom($('#listOfUserTestPublic div'), $)
	}

	function refreshAllUserPatentDetailsPublic(data) {
		removeAllUserPatentFromHTMLPublic();
		getUserPatentDetailsPublic(data);
	}

	function removeAllUserPatentFromHTMLPublic() {
		clearHtmlDom($('#listOfUserPatentPublic div'), $)
	}

	function refreshAllUserCertificationDetailsPublic(data) {
		removeAllUserCertificationFromHTMLPublic();
		getUserCertificationDetailsPublic(data);
	}

	function removeAllUserCertificationFromHTMLPublic() {
		clearHtmlDom($('#listOfUserCertificationPublic div'), $)
	}

	function refreshAllUserSkillDetailsPublic(data) {
		removeAllUserSkillFromHTMLPublic();
		getUserSkillDetailsPublic(data);
	}

	function removeAllUserSkillFromHTMLPublic() {
		clearHtmlDom($('#listOfUserSkillPublic div'), $)
	}

	function refreshAllUserAwardDetailsPublic(data) {
		removeAllUserAwardFromHTMLPublic();
		getUserAwardDetailsPublic(data);
	}

	function removeAllUserAwardFromHTMLPublic() {
		clearHtmlDom($('#listOfUserAwardPublic div'), $)
	}

	function getUserBasicDetailsPublic(requestData) {
		console.log("ajaxCalltoGetUserBasicDetails")
		console.log("contextPath =" + "${contextPath}");
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/basicdetail/viewByJsId/",
			type : "GET",
			data : requestData,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			addHtmlForUserBasicDetailsPublic(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserBasicDetailsPublic(data) {
		if (checkEmpty(data)) {
			return;
		}
		console.log(data)
		var basicData = {
			firstName : data.firstName == null ? "" : data.firstName,
			lastName : data.lastName == null ? "" : data.lastName,
			profileHeadline : data.profileHeadline == null ? ""
					: data.profileHeadline,
			address : data.address == null ? "" : data.address,
			summary : data.summary == null ? "N/A" : data.summary,
			separator : data.address == null ? "" : "|"
		};

		var formDetail = '<h2 class="text-capitalize c-black margin-bottom-0"style="margin-top: 10px;">'
				+ basicData.firstName
				+ ' '
				+ basicData.lastName
				+ '</h2><h4 class="text-capitalize c-black margin-bottom-0"><span class="padding-0">'
				+ basicData.profileHeadline
				+ ' </span><span class="hidden-xs padding-0">'
				+ (basicData.separator)
				+ '</span> <span class="hidden-sm hidden-lg hidden-md"><br /></span><span class="padding-0">'
				+ basicData.address
				+ '</span></h4><h4 class="text-capitalize c-black margin-bottom-0">Summary</h4><p>'
				+ basicData.summary + '</p>';
		$("#listOfUserBasicDetailsPublic").html(formDetail);
		$('#UserBasicDetailsPublic').show();
	}

	function getUserContactDetailsPublic(requestData) {
		console.log("ajaxCalltoGetUserContactDetails")
		console.log("contextPath =" + "${contextPath}");
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/contactdetail/viewByJsId/",
			type : "GET",
			data : requestData,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			addHtmlForUserContactDetailsPublic(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserContactDetailsPublic(data) {
		if (checkEmpty(data)) {
			return;
		}
		console.log(data)
		//prepare emailIds
		var emailIds = "";
		var isEmailIdPresent = data.emailId == null ? "" : data.emailId;
		var isWorkEmailIdPresent = data.workEmailId == null ? ""
				: data.workEmailId;
		if (isEmailIdPresent && isWorkEmailIdPresent) {
			emailIds = data.emailId + ", ", +data.workEmailId;
		} else if (isEmailIdPresent) {
			emailIds = data.emailId;
		} else if (isWorkEmailIdPresent) {
			emailIds = data.workEmailId;
		} else {
			emailIds = "N/A";
		}

		//prepare phone numbers
		var phoneNumbers = "";
		var isMobilePhonePresent = data.mobileNumber == null ? ""
				: data.mobileNumber;
		var isOtherPhonePresent = data.otherContactDetails == null ? ""
				: data.otherContactDetails;
		if (isMobilePhonePresent && isOtherPhonePresent) {
			phoneNumbers = data.mobileNumber + ", ", +data.otherContactDetails;
		} else if (isMobilePhonePresent) {
			phoneNumbers = data.mobileNumber;
		} else if (isOtherPhonePresent) {
			phoneNumbers = data.otherContactDetails;
		} else {
			phoneNumbers = "N/A";
		}

		var formDetail = '<div class="col-sm-10 col-sm-offset-0 user-basic-details"><div class="clearfix"></div><div class="pull-left col-sm-6 col-xs-12"><h5 class="text-capitalize c-black margin-bottom-0">Emails</h5><p>'
				+ emailIds
				+ '</p></div><div class="pull-left col-sm-6"><h5 class="text-capitalize c-black margin-bottom-0">Phone Numbers</h5><p>'
				+ phoneNumbers + '</p></div></div>';
		$("#listOfUserContactDetailsPublic").html(formDetail);

		$('#UserContactDetailsPublic').show();
	}

	function getJobExperienceDetailsPublic(requestData) {
		console.log("ajaxCalltoGetAllJObExeperienceDetails")
		console.log("contextPath =" + "${contextPath}");
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/experience/viewByJsId/",
			type : "GET",
			data : requestData,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			addHtmlForJobExperiencePublic(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForJobExperiencePublic(data) {
		if (checkEmpty(data)) {
			console.log("zzzz no experience data found")
			return;
		}
		$
				.each(
						data,
						function(i, expObj) {
							var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="jobExpId" type="hidden" name="experienceId" value='
                                + expObj.experienceId
                                + ' ><h4>'
									+ ((checkEmpty(expObj.companyName)) ? ""
											: expObj.companyName)
									+ ((checkEmpty(expObj.roleName)) ? ""
											: (" - " + expObj.roleName))
									+ '</h4><p class="margin-0">'
									+ ((checkEmpty(expObj.startDate)) ? ""
											: "<i class='fa fa-calendar'></i> "
													+ getLocalizedDateString(expObj.startDate))
									+ ((checkEmpty(expObj.endDate)) ? " to  Present"
											: (" to " + getLocalizedDateString(expObj.endDate)))
									+ '</p><div class="clearfix"></div><p class="margin-0"><span>'
									+ ((checkEmpty(expObj.location)) ? ""
											: '<i class="fa fa-map-marker"></i> ' + expObj.location)
									+ '</span></p><p>'
									+ ((checkEmpty(expObj.description)) ? ""
											: expObj.description)
									+ '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editJobExp"></i></div></div>';
							$("#listOfJobExperiencesPublic").append(formDetail);
						});
		$('#JobExperiencePublic').show();
	}

	function getUserEducationDetailsPublic(requestData) {
		console.log("ajaxCallToGetAllJobEducationDetails")
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/education/viewByJsId/",
			type : "GET",
			data : requestData,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			console.log("zzzzz educationdata");
			console.log(data);
			addHtmlForUserEducationPublic(data);
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserEducationPublic(data) {
		if (checkEmpty(data)) {
			return;
		}
		$
				.each(
						data,
						function(i, educationObj) {
							var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class="userEducationId" type="hidden" name="educationId"value=' + educationObj.educationId + '><h4>'
									+ ((checkEmpty(educationObj.organisationName)) ? ""
											: educationObj.organisationName)
									+ ((checkEmpty(educationObj.degreeValue)) ? ""
											: (" - " + educationObj.degreeValue))
									+ '</h4><div class="clearfix"></div><p class="margin-0">'
									+ ((checkEmpty(educationObj.startDate)) ? ""
											: "<i class='fa fa-calendar'></i> "
													+ getLocalizedDateString(educationObj.startDate))
									+ ((checkEmpty(educationObj.endDate)) ? ""
											: ("- " + getLocalizedDateString(educationObj.endDate)))
									+ '</p><p class="margin-0">'
									+ ((checkEmpty(educationObj.majorSubject)) ? ""
											: educationObj.majorSubject)
									+ '</p><p>'
									+ ((checkEmpty(educationObj.description)) ? ""
											: educationObj.description)
									+ '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserEducation"></i></div></div>';

							$("#listOfUserEducationPublic").append(formDetail);
						});
		$('#UserEducationPublic').show();
	}

	function getUserSkillDetailsPublic(requestData) {
		console.log("ajaxCalltoGetAllJObExeperienceDetails")
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/skill/viewByJsId",
			type : "GET",
			data : requestData,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			addHtmlForUserSkillPublic(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserSkillPublic(data) {
		if (checkEmpty(data)) {
			console.log("empty data");
			return;
		}
		var skills = data.split(",");
		var skillSpan = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-10 col-xs-10 pull-left"><div id="skills-text" class="search-job-fields">';
		$.each(skills, function(i, skill) {
			skillSpan = skillSpan + "<span class='badge'>" + skill + "</span>"
					+ "&nbsp;&nbsp;";
		});
		var skillHtmlFoot = '</div><br/></div></div>';
		$("#listOfUserSkillPublic").append((skillSpan + skillHtmlFoot));
		$('#UserSkillPublic').show();
	}

	function getUserProjectDetailsPublic(data) {
		console.log("ajaxCalltoGetAllJObExeperienceDetails")
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/project/viewByJsId",
			type : "GET",
			data : data,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			addHtmlForUserProjectPublic(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserProjectPublic(data) {
		if (checkEmpty(data)) {
			return;
		}
		$
				.each(
						data,
						function(i, projectObj) {
							var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userProjectId" type="hidden" name="projectId" value=' + projectObj.projectId + ' ><h4>'
									+ ((checkEmpty(projectObj.projectName)) ? ""
											: projectObj.projectName)
									+ '</h4><div class="clearfix"></div><p class="margin-0">'
									+ ((checkEmpty(projectObj.projectDate)) ? ""
											: "<i class='fa fa-calendar'></i> "
													+ getLocalizedDateString(projectObj.projectDate))
									+ '</p><p class="margin-0">'
									+ ((checkEmpty(projectObj.projectURL)) ? ""
											: "Project link: ")
									+ '<a target="_new" href="'
									+ ((checkEmpty(projectObj.projectURL)) ? ""
											: projectObj.projectURL)
									+ '">'
									+ ((checkEmpty(projectObj.projectURL)) ? ""
											: projectObj.projectURL)
									+ '</a></p><p>'
									+ ((checkEmpty(projectObj.projectDescription)) ? ""
											: projectObj.projectDescription)
									+ '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserProject"></i></div></div>';
							$("#listOfUserProjectPublic").append(formDetail);
						});
		$('#UserProjectPublic').show();
	}

	function getUserAwardDetailsPublic(data) {
		console.log("getAwardDetails with data");
		console.log(data);
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/award/viewByJsId",
			type : "GET",
			data : data,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			addHtmlForUserAwardPublic(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserAwardPublic(data) {
		if (checkEmpty(data)) {
			return;
		}
		$
				.each(
						data,
						function(i, awardObj) {
							var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userAwardId" type="hidden" name="awardId" value=' + awardObj.awardId + ' ><h4>'
									+ ((checkEmpty(awardObj.title)) ? ""
											: awardObj.title)
									+ ((checkEmpty(awardObj.organisationName)) ? ""
											: (" - " + awardObj.organisationName))
									+ '</h4><div class="clearfix"></div><p class="margin-0">'
									+ ((checkEmpty(awardObj.date)) ? ""
											: "<i class='fa fa-calendar'></i> "
													+ getLocalizedDateString(awardObj.date))
									+ '</p><br/></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserAward"></i></div></div>';
							$("#listOfUserAwardPublic").append(formDetail);
						});
		$('#UserAwardPublic').show();
	}

	function getUserPublicationDetailsPublic(data) {
		console.log("ajaxCalltoGetAllJObExeperienceDetails")
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/publication/viewByJsId",
			type : "GET",
			data : data,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			addHtmlForUserPublicationPublic(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserPublicationPublic(data) {
		if (checkEmpty(data)) {
			return;
		}
		$
				.each(
						data,
						function(i, publicationObj) {
							var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class="userPublicationId" type="hidden" name="publicationId"value=' + publicationObj.publicationId + '><h4>'
									+ ((checkEmpty(publicationObj.publicationTitle)) ? ""
											: publicationObj.publicationTitle)
									+ ((checkEmpty(publicationObj.publicationOrganisation)) ? ""
											: " - "
													+ publicationObj.publicationOrganisation)
									+ '</h4><p class="margin-0">'
									+ ((checkEmpty(publicationObj.publicationDate)) ? ""
											: "<i class='fa fa-calendar'></i> "
													+ getLocalizedDateString(publicationObj.publicationDate))
									+ '</p><p>'
									+ ((checkEmpty(publicationObj.publicationDescription)) ? ""
											: publicationObj.publicationDescription)
									+ '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserPublication"></i></div></div>';
							$("#listOfUserPublicationPublic")
									.append(formDetail);
						});
		$('#UserPublicationPublic').show();
	}

	function getUserCertificationDetailsPublic(data) {
		console.log("ajaxCalltoGetAllJObExeperienceDetails")
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/certification/viewByJsId",
			type : "GET",
			data : data,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			addHtmlForUserCertificationPublic(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserCertificationPublic(data) {
		if (checkEmpty(data)) {
			return;
		}
		$
				.each(
						data,
						function(i, certificationObj) {
							var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userCertificationId" type="hidden" name="certificationId" value=' + certificationObj.certificationId + ' ><h4>'
									+ ((checkEmpty(certificationObj.certificationName)) ? ""
											: certificationObj.certificationName)
									+ ((checkEmpty(certificationObj.organisationName)) ? ""
											: ("&nbsp;&nbsp;&nbsp;&nbsp;" + certificationObj.organisationName))
									+ '</h4><p class="margin-0">'
									+ ((checkEmpty(certificationObj.certificationStartDate)) ? ""
											: "<i class='fa fa-calendar'></i> "
													+ getLocalizedDateString(certificationObj.certificationStartDate))
									+ ((checkEmpty(certificationObj.certificationEndDate)) ? ""
											: ("- " + getLocalizedDateString(certificationObj.certificationEndDate)))
									+ '</p>'
									+ ((checkEmpty(certificationObj.certificationUrl)) ? ""
											: "Certification link:  ")
									+ '<a target="_new" href='
									+ ((checkEmpty(certificationObj.certificationUrl)) ? ""
											: certificationObj.certificationUrl)
									+ '>'
									+ ((checkEmpty(certificationObj.certificationUrl)) ? ""
											: certificationObj.certificationUrl)
									+ '</a><p></p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserCertification"></i></div></div>';
							$("#listOfUserCertificationPublic").append(
									formDetail);
						});
		$('#UserCertificationPublic').show();
	}

	function getUserPatentDetailsPublic(data) {
		console.log("getUserPatentDetails")
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/patent/viewByJsId",
			type : "GET",
			data : data,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			addHtmlForUserPatentPublic(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserPatentPublic(data) {
		if (checkEmpty(data)) {
			return;
		}
		$
				.each(
						data,
						function(i, patentObj) {
							var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userPatentId" type="hidden" name="patentId" value=' + patentObj.patentId + ' ><h4 class="text-capitalize">'
									+ ((checkEmpty(patentObj.patentTitle)) ? ""
											: patentObj.patentTitle)
									+ '</h4><p class="margin-0 text-capitalize">'
									+ ((checkEmpty(patentObj.patentFillingDate)) ? ""
											: "<i class='fa fa-calendar'></i> "
													+ getLocalizedDateString(patentObj.patentFillingDate))
									+ '</p><p class="margin-0"><span class=" text-capitalize">'
									+ ((checkEmpty(patentObj.patentStatus)) ? ""
											: "Status: "
													+ patentObj.patentStatus)
									+ '</span></p><p class="margin-0"><span class=" text-capitalize">'
									+ ((checkEmpty(patentObj.patentApplicationNumber)) ? ""
											: "Number: "
													+ patentObj.patentApplicationNumber)
									+ '</span>&nbsp;&nbsp; <span class=" text-capitalize">'
									+ ((checkEmpty(patentObj.patentOffice)) ? ""
											: "Office: "
													+ patentObj.patentOffice)
									+ '</span></p>'
									+ ((checkEmpty(patentObj.patentUrl)) ? ""
											: "Patent link: ")
									+ '<a target="_new" href='
									+ ((checkEmpty(patentObj.patentUrl)) ? ""
											: patentObj.patentUrl)
									+ '>'
									+ ((checkEmpty(patentObj.patentUrl)) ? ""
											: patentObj.patentUrl)
									+ '</a><p>'
									+ ((checkEmpty(patentObj.patentDescription)) ? ""
											: patentObj.patentDescription)
									+ '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserPatent"></i></div></div>';
							$("#listOfUserPatentPublic").append(formDetail);
						});
		$('#UserPatentPublic').show();
	}

	function getUserTestDetailsPublic(data) {
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/test/viewByJsId",
			type : "GET",
			data : data,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			addHtmlForUserTestPublic(data);
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserTestPublic(data) {
		if (checkEmpty(data)) {
			return;
		}
		$
				.each(
						data,
						function(i, testObj) {
							var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userTestId" type="hidden" name="testId" value=' + testObj.testId + ' ><h4 class="text-capitalize">'
									+ ((checkEmpty(testObj.testName)) ? ""
											: testObj.testName)
									+ '</h4><p class="margin-0">'
									+ ((checkEmpty(testObj.testDate)) ? ""
											: "<i class='fa fa-calendar'></i> "
													+ getLocalizedDateString(testObj.testDate))
									+ '</p><p class="margin-0">'
									+ ((checkEmpty(testObj.testScore)) ? ""
											: "Score: " + testObj.testScore)
									+ '</p><p>'
									+ ((checkEmpty(testObj.testDescription)) ? ""
											: testObj.testDescription)
									+ '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserTest"></i></div></div>';
							$("#listOfUserTestPublic").append(formDetail);
						});
		$('#UserTestScoresPublic').show();
	}

	function getUserVolunteerDetailsPublic(data) {
		console.log("ajaxCalltoGetAllJObVolunteering")
		$.ajax({
			url : "${contextPath}/user/getJobSeeker/volunteer/viewByJsId",
			type : "GET",
			data : data,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			console.log("volunteer")
			console.log(data)
			addHtmlForUserVolunteerPublic(data);
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserVolunteerPublic(data) {
		if (checkEmpty(data)) {
			return;
		}
		console.log(data)
		$
				.each(
						data,
						function(i, volunteerObj) {
							var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userVolunteerId" type="hidden" name="volunteerId" value=' + volunteerObj.volunteerId + ' ><h4 class="text-capitalize">'
									+ ((checkEmpty(volunteerObj.organisationName)) ? ""
											: volunteerObj.organisationName)
									+ ((checkEmpty(volunteerObj.roleName)) ? ""
											: (" - " + volunteerObj.roleName))
									+ '</h4><p class="margin-0">'
									+ ((checkEmpty(volunteerObj.volunteerStartDate)) ? ""
											: "<i class='fa fa-calendar'></i> "
													+ getLocalizedDateString(volunteerObj.volunteerStartDate))
									+ ((checkEmpty(volunteerObj.volunteerEndDate)) ? ""
											: (" - " + (getLocalizedDateString(volunteerObj.volunteerEndDate))))
									+ '</p><p class="margin-0 text-capitalize">'
									+ ((checkEmpty(volunteerObj.volunteerCause)) ? ""
											: volunteerObj.volunteerCause)
									+ '</p><p>'
									+ ((checkEmpty(volunteerObj.volunteerDescription)) ? ""
											: volunteerObj.volunteerDescription)
									+ '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserVolunteer"></i></div></div>';
							$("#listOfUserVolunteerPublic").append(formDetail);
						});
		$('#UserVolunteerPublic').show();
	}
</script>