<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobSeekerCommonUtils.jsp"></jsp:include>
<script type="text/javascript">
	//===========================USER EDUCATION START==========================
	function initializeEducationTypeahead() {
		genericTypeahead('input.userEducationOrganisation.typeahead',
				'organisationNames', bloodHoundOrganisation);
		genericTypeahead('input.userEducationDegree.typeahead', 'degree',
				bloodHoundDegree);
	}

	$('#listOfUserEducation').delegate(
			'[class*=editUserEducation]',
			'click',
			function() {
				console.log("editUserEducation is working ");
				addUserEducationForm($(this).closest('div.work-experience')
						.find('input.userEducationId').val());

			});

	// adding jquery for new education details
	var newUserEducationForm = '<div class="addNewUserEducation basic-form col-sm-8"><form role="form" class="userEducationForm"><div class="row"><div class="col-sm-6"><div class="form-group userEducationOrganisationGrp"><label for="userEducationOrganisation">School:</label> <input required type="text" class="form-control typeahead userEducationOrganisation" placeholder="Enter School Name" name="organisationName"></div></div><div class="col-sm-6"><div class="form-group userEducationDegreeGrp"><label for="userEducationDegree">Degree:</label> <input type="text" class="form-control typeahead userEducationDegree" placeholder="Enter Degree" name="degreeValue"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userEducationMajorSubjectGrp"><label for="userEducationMajorSubject">Field of Study:</label><input type="text" class="form-control userEducationMajorSubject" placeholder="Add Field of Study" name="majorSubject" ></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userEducationStartDateGrp"><label for="userEducationStartDate">Start Date:</label> <input type="date" class="form-control userEducationStartDate" name="startDate"></div></div><div class="col-sm-6"><div class="form-group userEducationEndDateGrp"><label for="userEducationEndDate">End Date:</label> <input type="date" class="form-control userEducationEndDate" name="endDate"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userEducationDescriptionGrp"><label for="userEducationDescription">Description:</label><textarea class="form-control userEducationDescription" placeholder="Add Description" name="description"></textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserEducation">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="submit" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserEducation">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button></div></form></div>';

	$("#add-user-education").click(function() {
		// $(this).parent().parent().parent().before(newUserEducationForm);
		console.log("zzzzz add user education form")
		$('#listOfUserEducation').before(newUserEducationForm);
		$(this).hide();
		$('#listOfUserEducation').children().hide();//hide all editable things.
		initializeEducationTypeahead();
	});

	$("#UserEducation").delegate('.cancelUserEducation', 'click', function() {
		console.log("cancel button is working");
		clearHtmlDom($(this).closest('form').parent());
		$('#listOfUserEducation').children().show(); //had hidden all education list
		$('#add-user-education').show(); // had hidden the add education
		// option when clicked on a new job
		// education
	});

	$("#UserEducation")
			.on(
					'click',
					'.saveUserEducation',
					function(e) {
						e.preventDefault();
						var data = $(this).closest('form').serialize();
						var divToClear = $(this).closest('form').parent();
						$
								.ajax(
										{
											url : "${contextPath}/jobseeker/education/save",
											type : "POST",
											data : data,
											beforeSend : function() {
												startLoader();
											}
										})
								.done(
										function(data, status, xhr) {
											console
													.log("Post operation was successful: ");
											console.log(data)
											if (data.status == "Success") {
												clearHtmlDom(divToClear);
												$('#add-user-education').show();
												refreshAllUserEducationDetails();
											} else { //data.status == "Error"
												$(
														'.userEducationForm .formFieldError')
														.detach();
												for ( var key in data.errorMap) {
													var errorSpanForFormField = '<span style="color: red" class="formFieldError" id='+ key +'>'
															+ data.errorMap[key]
															+ '</span>';
													$(
															'.userEducationForm [name^='
																	+ key + ']')
															.after(
																	errorSpanForFormField);

												}
											}
										  isProfileComplete("${contextPath}");
										})
								.fail(
										function(xhr, status, errorThrown) {
											console
													.log("ajaxCallToSaveUserEducation:ERRORED OUT:DO SOME ERROR HANDLING HERE")
										}).always(function(xhr) {
									stopLoader();
								});
					});

	$("#UserEducation").delegate(
			'.deleteUserEducation',
			'click',
			function() {
				console.log("delete button is working");
				var userEducationId = $(this).closest('form').find(
						'input.userEducationId').val();
				var divToClear = $(this).closest('form').parent()
				$.ajax(
						{
							url : "${contextPath}/jobseeker/education/remove/"
									+ userEducationId,
							type : "DELETE",
							beforeSend : function() {
								startLoader();
							}
						}).done(function(data, status, xhr) {
					console.log($(this).closest('form').parent());
					clearHtmlDom(divToClear);
					$("#add-user-education").show();
					refreshAllUserEducationDetails();
					isProfileComplete("${contextPath}");
				}).fail(function(xhr, status, errorThrown) {
				}).always(function(xhr) {
					stopLoader();
				});

			});

	function refreshAllUserEducationDetails() {
		removeAllUserEducationFromHTML();
		getUserEducationDetails();
	}

	function removeAllUserEducationFromHTML() {
		$('#listOfUserEducation div').detach();
	}

	function addUserEducationForm(userEducationId) {
		$.ajax(
				{
					url : "${contextPath}/jobseeker/education/viewById/"
							+ userEducationId,
					type : "GET",
					beforeSend : function() {
						startLoader();
					}
				}).done(function(data, status, xhr) {
			console.log("GET operation was successful: ");
			console.log(data)
			addHtmlForUserEducationForm(data);
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function getUserEducationDetails() {
		console.log("ajaxCallToGetAllJobEducationDetails")
		$.ajax({
			url : "${contextPath}/jobseeker/education/viewAll",
			type : "GET",
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			console.log("GET operation was successful: ");
			console.log(data)
			addHtmlForUserEducation(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function addHtmlForUserEducationForm(educationObj) {
		$('#listOfUserEducation').children().hide(); //hide all education list
		$('#add-user-education').hide(); //hide adding feature when editing
		var formObj = '<div class="addNewUserEducation basic-form col-sm-8"><form role="form" class="userEducationForm"><div class="form-group userEducationIdGrp" style="display: none"><input type="text" class="form-control userEducationId"name="educationId" value=' + educationObj.educationId + '></div><div class="row"><div class="col-sm-6"><div class="form-group userEducationOrganisationGrp"><label for="userEducationOrganisation">School:</label> <input type="text" class="form-control typeahead userEducationOrganisation" required placeholder="Enter School Name" name="organisationName" value="'
				+ ((checkEmpty(educationObj.organisationName)) ? ""
						: educationObj.organisationName)
				+ '"></div></div><div class="col-sm-6"><div class="form-group userEducationDegreeGrp"><label for="userEducationDegree">Degree:</label> <input type="text" class="form-control typeahead userEducationDegree" placeholder="Enter Degree" name="degreeValue" value="'
				+ ((checkEmpty(educationObj.degreeValue)) ? ""
						: educationObj.degreeValue)
				+ '"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userEducationMajorSubjectGrp"><label for="userEducationMajorSubject">Field of Study:</label><input type="text" class="form-control userEducationMajorSubject" placeholder="Add Field of Study" name="majorSubject" value="'
				+ ((checkEmpty(educationObj.majorSubject)) ? ""
						: educationObj.majorSubject)
				+ '"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userEducationStartDateGrp"><label for="userEducationStartDate">Start Date:</label> <input type="date" class="form-control userEducationStartDate" name="startDate" value='
				+ ((checkEmpty(educationObj.startDate)) ? ""
						: educationObj.startDate)
				+ '></div></div><div class="col-sm-6"><div class="form-group userEndDate"><label for="userEndDate">End Date:</label> <input type="date" class="form-control userEducationEndDate" name="endDate" value='
				+ ((checkEmpty(educationObj.endDate)) ? ""
						: educationObj.endDate)
				+ '></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userEducationDescriptionGrp"><label for="userEducationDescription">Description:</label><textarea class="form-control userEducationDescription" placeholder="Add Description" name="description">'
				+ ((checkEmpty(educationObj.description)) ? ""
						: educationObj.description)
				+ '</textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserEducation">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserEducation">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center deleteUserEducation">Remove<i style="margin-left: 4px; margin-right: 4px; color:rgb(236, 244, 216)" class="fa fa-trash-o"></i></button></div></form></div>';
		$('#listOfUserEducation').before(formObj);
		initializeEducationTypeahead();
	}

	function addHtmlForUserEducation(data) {
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

							$("#listOfUserEducation").append(formDetail);
						});
	}

	//===========================USER EDUCATION END==========================
</script>