<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobSeekerCommonUtils.jsp"></jsp:include>
<script src="${contextPath}/resources/js/bootstrap-tagsinput.js"></script>
<script type="text/javascript">
	//===========================USER SKILL START==========================
	function addSkillNameTagTypeahead() {
		$('.userSkillNames')
				.tagsinput(
						{
							typeaheadjs : {
								name : 'skill-name',
								source : getBloodHoundObject("${contextPath}/skilldetail/search?skillName=")
							},
							freeInput : true,
							highlight : true,
							minLength : 1,
							maxTags : 10,
							trimValue : true

						});
	}

	$('#listOfUserSkill').delegate('[class*=editUserSkill]', 'click',
			function() {
				console.log("editUserSkill is working ");
				editOrAddSkills();
			});

	var newUserSkillForm = '<div class="addNewUserPublication basic-form col-sm-8"><form role="form" class="userSkillForm"><div class="row"><div class="col-sm-12"><div class="form-group userSkillNamesGrp"><label for="skill-name">Skills:</label> <input type="text" class="form-control tagsinput userSkillNames"id="skill-name" name="skills" /></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserSkill">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="submit" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserSkill">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button></div></form></div>';
	$(".add-user-skill").click(function() {
		console.log("zzzzz add user skill form")
		editOrAddSkills();
	});

	function editOrAddSkills() {
		$.ajax({
			url : "${contextPath}/jobseeker/skill/viewAll",
			type : "GET",
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			console.log("GET operation was successful: ");
			console.log(data)
			addHtmlForUserSkillForm(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	$("#UserSkill").delegate('.cancelUserSkill', 'click', function() {
		console.log("cancel button is working");
		clearHtmlDom($(this).closest('form').parent(), $);
		$('#listOfUserSkill').children().show();
		$('.add-user-skill').show();
	});

	/* $('#UserSkill').on('keyup', '.bootstrap-tagsinput input', function(e) {
		if (e.which == 13) {
			$($('.bootstrap-tagsinput input.tt-input')[0]).val("");
		}
	}); */

	$("#UserSkill").on('click', '.saveUserSkill', function(e) {
		console.log("zzzzzz save button is working");
		e.preventDefault();
		var data = $(this).closest('form').serialize();
		console.log(data);
		var divToClear = $(this).closest('form').parent();
		$.ajax({
			url : "${contextPath}/jobseeker/skill/save",
			type : "POST",
			data : data,
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			console.log("Post operation was successful: ");
			console.log(data)
			clearHtmlDom(divToClear, $);
			$('.add-user-skill').show();
			refreshAllUserSkillDetails();
			isProfileComplete("${contextPath}");
		}).fail(function(xhr, status, errorThrown) {
		}).always(function(xhr) {
			stopLoader();
		});
	});

	function getUserSkillDetails() {
		console.log("ajaxCalltoGetAllJObExeperienceDetails")
		$.ajax({
			url : "${contextPath}/jobseeker/skill/viewAll",
			type : "GET",
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			console.log("GET operation was successful: ");
			console.log(data)
			addHtmlForUserSkill(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function refreshAllUserSkillDetails() {
		removeAllUserSkillFromHTML();
		getUserSkillDetails();
	}

	function removeAllUserSkillFromHTML() {
		$('#listOfUserSkill div').detach();
	}
	function addHtmlForUserSkillForm(data) {
		console.log("zzzzzz addhtmlforskillforms");
		console.log(data)
		$('#listOfUserSkill').children().hide();
		$('.add-user-skill').hide();
		$('#listOfUserSkill').before(newUserSkillForm);
		addSkillNameTagTypeahead();
		var skillSpanList = [];
		var skills = data.split(",");
		$.each(skills, function(i, skill) {
			console.log(skill)
			console.log($('.userSkillForm input.userSkillNames'));
			$('.userSkillForm input.userSkillNames').tagsinput('add', skill);
		});
	}

	function addHtmlForUserSkill(data) {
		if (checkEmpty(data)) {
			console.log("empty data");
			return;
		}
		var skills = data.split(",");
		var skillSpan = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><div id="skills-text" class="search-job-fields">';
		$.each(skills, function(i, skill) {
			skillSpan = skillSpan + "<span class='badge'>" + skill + "</span>"
					+ "&nbsp;&nbsp;";
		});
		var skillHtmlFoot = '</div></div><div class="pull-left col-sm-1 col-xs-2" style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserSkill"></i></div></div>';
		$("#listOfUserSkill").append((skillSpan + skillHtmlFoot));
	}

	//===========================USER SKILL END==========================
</script>