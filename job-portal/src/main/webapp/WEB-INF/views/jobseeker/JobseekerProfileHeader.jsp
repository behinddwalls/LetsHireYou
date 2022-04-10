<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobSeekerCommonUtils.jsp"></jsp:include>
<script type="text/javascript">
	// ===========================USER PROFILEHEADER START==========================
	$("#editUserProfileHeader").click(function() {
		addUserProfileHeaderForm();
		$(this).hide();
		$('#userProfileHeader').children().hide();
	});

	$("#JobSeekerProfileHeader").delegate('.editUserProfileHeader', 'click',
			function() {
				addUserProfileHeaderForm();
				$("#editUserProfileHeader").hide();
				$('#userProfileHeader').children().hide();
			});

	$("#userProfileHeaderForm").delegate('.cancelUserProfileHeader', 'click',
			function() {
				console.log("cancel button is working");
				clearHtmlDom($(this).closest('form').parent());
				$('#editUserProfileHeader').show();
				$('#JobSeekerBasic').show();
				refreshAllUserProfileHeaderDetails();
			});

	$("#userProfileHeaderForm")
			.on(
					'click',
					'.saveUserProfileHeader',
					function(e) {
						console.log("save button is working");
						e.preventDefault();
						// closest method checks the DOM element and its ancestors for the
						// selector		
						var data = $(this).closest('form').serialize();
						data=data+"&industryName="+$('.userIndustryName').find(":selected").text();
						data=data+"&jobFunction="+$('.userJobFunction').find(":selected").text();
						console.log(data);
						var divToClear = $(this).closest('form').parent();
						$
								.ajax(
										{
											url : "${contextPath}/jobseeker/profile/profileHeader/save",
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
											clearHtmlDom(divToClear);
											$('#editUserProfileHeader').show();
											$('#JobSeekerBasic').show();
											refreshAllUserProfileHeaderDetails();
											isProfileComplete("${contextPath}");
										})
								.fail(
										function(xhr, status, errorThrown) {
											console
													.log("ajaxCallToSaveUserProfileHeader:ERRORED OUT:DO SOME ERROR HANDLING HERE")
										}).always(function(xhr) {
									stopLoader();
								});

					});

	$("#JobSeekerProfileHeader").delegate('.editUserProfileImage', 'click',
			function() {
				addHtmlForUserProfileImageUploadForm();
				$("#editUserProfileHeader").hide();
				$('#userProfileHeader').children().hide();
			});

	$("#userProfileHeaderForm").delegate('.cancelUserProfileImage', 'click',
			function() {
				clearHtmlDom($(this).closest('form').parent());
				$('#editUserProfileHeader').show();
				$('#JobSeekerBasic').show();
				refreshAllUserProfileHeaderDetails();
			});

	$("#JobSeekerProfileHeader").delegate('.editUserProfileResume', 'click',
			function() {
				addHtmlForUserProfileResumeUploadForm();
				$("#editUserProfileHeader").hide();
				$('#userProfileHeader').children().hide();
			});

	$("#userProfileHeaderForm").delegate('.cancelUserProfileResume', 'click',
			function() {
				clearHtmlDom($(this).closest('form').parent());
				$('#editUserProfileHeader').show();
				$('#JobSeekerBasic').show();
				refreshAllUserProfileHeaderDetails();
			});

	function refreshAllUserProfileHeaderDetails() {
		removeAllUserProfileHeaderFromHTML();
		getUserProfileHeaderDetails();
	}

	function removeAllUserProfileHeaderFromHTML() {
		$('#userProfileHeader div').detach();
	}

	function addUserProfileHeaderForm(userProfileHeaderId) {
		$.ajax({
			url : "${contextPath}/jobseeker/profile/basic/view/edit",
			type : "GET",
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			console.log("GET operation was successful: ");
			console.log(data)
			addHtmlForUserProfileHeaderForm(data);
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	function getUserProfileHeaderDetails() {
		console.log("getProfileHeader")
		$.ajax({
			url : "${contextPath}/jobseeker/profile/basic/view",
			type : "GET",
			beforeSend : function() {
				startLoader();
			}
		}).done(function(data, status, xhr) {
			console.log("GET operation was successful: ");
			console.log(data)
			addHtmlForUserProfileHeader(data); // add html to the div
		}).fail(function(xhr, status, errorThrown) {
			console.log("ERROR DO SOME ERROR HANDLING");
		}).always(function(xhr) {
			stopLoader();
		});
	}

	// ====================== UPLOAD RESUME  ========================================
	$("#userProfileHeaderForm").on('submit', 'form.uploadResume',
			function(event) {
				event.preventDefault();
				event.stopPropagation();
				startLoader();
				var oMyForm = new FormData($(this)[0]);
				var divToClear = $(this).closest('form').parent();
				$.ajax({
					url : "${contextPath}/jobseeker/upload/resume",
					type : "POST",
					async : true,
					data : oMyForm,
					contentType : false,
					processData : false,
					xhrFields : {
						withCredentials : true
					}
				}).done(function(result) {
					if (result.successMessage != null) {
						toast(result.successMessage);
					} else {

						toast(result.successMessage)
					}
					clearHtmlDom(divToClear);
					$('#editUserProfileHeader').show();
					$('#JobSeekerBasic').show();
					refreshAllUserProfileHeaderDetails();

				}).fail(function(result) {
					toast('Some problem in uploading the Resume!!!');
					console.log("error" + result);
					//alert("Some error encountered");
				}).complete(function() {
					stopLoader();
				});
			});
	// ====================== END OF RESUME UPLOAD ========================================

	// ====================== UPLOAD PROFILE IMAGE  ========================================
	$("#userProfileHeaderForm").on('submit', 'form.userProfileImageForm',
			function(event) {

				//	if(!valiateFile()){
				//		toast("Please select the valide file formate from[.jpg,.png]");
				//	}
				//now enable the Dialog window.
				//$('.dialog-mask').attr("hidden", false);

				//alert("Upload rthe profile image");
				event.preventDefault();
				event.stopPropagation();
				startLoader();
				var oMyForm = new FormData($(this)[0]);
				var divToClear = $(this).closest('form').parent();
				$.ajax({
					url : "${contextPath}/jobseeker/upload/profileImage",
					type : "POST",
					async : true,
					data : oMyForm,
					contentType : false,
					processData : false,
					xhrFields : {
						withCredentials : true
					}
				}).done(function(result) {
					if (result.successMessage != null) {
						toast(result.successMessage);
						//not show the profile image also.
						$('.user-profile-img').attr('src', result.imageUrl);
					} else {
						toast(result.errorMessage)
					}
					console.log(divToClear);
					clearHtmlDom(divToClear);
					$('#editUserProfileHeader').show();
					$('#JobSeekerBasic').show();
					refreshAllUserProfileHeaderDetails();

				}).fail(function(result) {
					toast('Some problem in uploading the Profile image!!!');
					console.log("error" + result);
					//alert("Some error encountered");
				}).complete(function() {
					stopLoader();
				});
			});
	// ====================== END OF PROFILE IMAGE UPLOAD ========================================	

	/////    Validate file    /////
	function valiateFile() {
		alert("validating the image");
		var image = document.getElementById("image").value;
		if (image != '') {
			var checkimg = image.toLowerCase();
			if (!checkimg.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)) {
				alert("Please enter Image File Extensions .jpg,.png,.jpeg");
				document.getElementById("image").focus();
				return false;
			}
		}
		alert("Inmave var ius null");
		return true;
	}

	function addHtmlForUserProfileHeaderForm(userBasicObj) {
		console.log("addHtmlForProfileHeader")
		console.log(userBasicObj);
		$('#userProfileHeader').children().hide();
		$('#editUserProfileHeader').hide();
		$('#JobSeekerBasic').hide();
		var formObj = '<div class="basic-form col-sm-8"><form role="form" class="userProfileHeaderForm"><h5>Basic Information</h5><div class="row"><div class="col-sm-6"><div class="form-group userNameGrp"><label for="user-first-name">First Name </label><input type="text" class="form-control userName" placeholder="First Name (required)" name="firstName" value="'
				+ ((checkEmpty(userBasicObj.firstName)) ? ""
						: userBasicObj.firstName)
				+ '"></div></div><div class="col-sm-6"><div class="form-group userNameGrp"><label for="user-last-name">Last Name </label><input type="text" class="form-control userName" placeholder="Last Name (required)" name="lastName" value="'
				+ ((checkEmpty(userBasicObj.lastName)) ? ""
						: userBasicObj.lastName)
				+ '"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userCTCGrp"><label for="user-ctc">Salary (In Lakhs) </label><input type="text" class="form-control userCTC" placeholder="CTC in Lakh/annum" name="ctc" value="'
				+ ((checkEmpty(userBasicObj.ctc)) ? "" : userBasicObj.ctc)
				+ '"></div></div><div class="col-sm-8"><div class="form-group hideCtcGrp"><input type="checkbox" class="hideCtc" name="hideCtc" '+(userBasicObj.hideCtc==1?"checked value=1":"value=0")
				+'>Keep salary hidden from recruiter.<br></div></div></div><div class="row"><div class="col-sm-8"><div class="form-group userProfileHeadLineGrp"><label for="user-profile-headline">Profile Headline </label><input type="text" class="form-control userProfileHeadLine" placeholder="Headline" name="profileHeadline" value="'
				+ ((checkEmpty(userBasicObj.profileHeadline)) ? ""
						: userBasicObj.profileHeadline)
				+ '"></div></div></div><div class="row"><div class="col-sm-8"><div class="form-group userAddressGrp"><label for="user-addressgrp">Location </label><input type="text" class="form-control userAddress autocompleteAddress" name="address" placeholder="Location" value='
				+ ((checkEmpty(userBasicObj.address)) ? ""
						: userBasicObj.address)
				+ '></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userProfileSummaryGrp"><label for="user-summary">Summary </label><textarea maxlength="500" class="form-control userProfileSummary" placeholder="Summary" name="summary">'
				+ ((checkEmpty(userBasicObj.summary)) ? ""
						: userBasicObj.summary)
				+ '</textarea></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userJobFunctionGrp">'
				+'<label for="user-jobFunction">Job Function </label>'
				+   '<select class="form-control userJobFunction">'
				+	'<option value=""></option>'
				+	'<option value="Business Development" '+(checkEquals(userBasicObj.jobFunction,"Business Development") ? "selected"
					: "")+ '>Business Development</option>'
				+	'<option value="Finance" '+(checkEquals(userBasicObj.jobFunction,"Finance") ? "selected"
						: "")+ '>Finance</option>'
				+	'<option value="Human Resources" '+(checkEquals(userBasicObj.jobFunction,"Human Resources") ? "selected"
						: "")+ '>Human Resources</option>'
				+	'<option value="Information Technology" '+(checkEquals(userBasicObj.jobFunction,"Information Technology") ? "selected"
						: "")+ '>Information Technology</option>'
				+	'<option value="Marketing" '+(checkEquals(userBasicObj.jobFunction,"Marketing") ? "selected"
						: "")+ '>Marketing</option>'
				+	'<option value="Operations" '+(checkEquals(userBasicObj.jobFunction,"Operations") ? "selected"
						: "")+ '>Operations</option>'
				+	'<option value="Quality Assurance" '+(checkEquals(userBasicObj.jobFunction,"Quality Assurance") ? "selected"
						: "")+ '>Quality Assurance</option>'
				+	'<option value="Research and Analytics" '+(checkEquals(userBasicObj.jobFunction,"Research and Analytics") ? "selected"
						: "")+ '>Research and Analytics</option>'
				+	'<option value="Sales" '+(checkEquals(userBasicObj.jobFunction,"Sales") ? "selected"
						: "")+ '>Sales</option>'
				+	'<option value="Strategy/Planning" '+(checkEquals(userBasicObj.jobFunction,"Strategy/Planning") ? "selected"
						: "")+ '>Strategy/Planning</option>'
				+	'<option value="Supply Chain" '+(checkEquals(userBasicObj.jobFunction,"Supply Chain") ? "selected"
						: "")+ '>Supply Chain</option>'
				+   '</select>'
				//+'<div class="form-group userJobFunctionGrp"><input type="text" class="form-control userJobFunction" name="jobFunction" placeholder="Job Function" value="'
				//+ ((checkEmpty(userBasicObj.jobFunction)) ? ""
				//		: userBasicObj.jobFunction)+ '">'
				+'</div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userIndustryNameGrp">'
				+'<label for="user-industryName">Industry Name </label>'
				+'<select class="form-control userIndustryName" >'
				+'<option value=""></option>'
				+'<option value="Accounting/Auditing" '+(checkEquals(userBasicObj.industryName,"Accounting/Auditing") ? "selected"
						: "")+ '>Accounting/Auditing</option>'
				+'<option value="Advertising" '+(checkEquals(userBasicObj.industryName,"Advertising") ? "selected"
						: "")+ '>Advertising</option>'
				+'<option value="Analytics" '+(checkEquals(userBasicObj.industryName,"Analytics") ? "selected"
						: "")+ '>Analytics</option>'
				+'<option value="Art/Creative/Design" '+(checkEquals(userBasicObj.industryName,"Art/Creative/Design") ? "selected"
						: "")+ '>Art/Creative/Design</option>'
				+'<option value="Banking and Financial Services" '+(checkEquals(userBasicObj.industryName,"Banking and Financial Services") ? "selected"
						: "")+ '>Banking and Financial Services</option>'
				+'<option value="Consulting" '+(checkEquals(userBasicObj.industryName,"Consulting") ? "selected"
						: "")+ '>Consulting</option>'
				+'<option value="Education/Training" '+(checkEquals(userBasicObj.industryName,"Education/Training") ? "selected"
						: "")+ '>Education/Training</option>'
				+'<option value="Healthcare" '+(checkEquals(userBasicObj.industryName,"Healthcare") ? "selected"
						: "")+ '>Healthcare</option>'
				+'<option value="Human Resources" '+(checkEquals(userBasicObj.industryName,"Human Resources") ? "selected"
						: "")+ '>Human Resources</option>'
				+'<option value="Information Technology" '+(checkEquals(userBasicObj.industryName,"Information Technology") ? "selected"
						: "")+ '>Information Technology</option>'
				+'<option value="Legal" '+(checkEquals(userBasicObj.industryName,"Legal") ? "selected"
						: "")+ '>Legal</option>'
				+'<option value="Logistics" '+(checkEquals(userBasicObj.industryName,"Logistics") ? "selected"
						: "")+ '>Logistics</option>'
				+'<option value="Manufacturing" '+(checkEquals(userBasicObj.industryName,"Manufacturing") ? "selected"
						: "")+ '>Manufacturing</option>'
				+'<option value="Pharmaceuticals" '+(checkEquals(userBasicObj.industryName,"Pharmaceuticals") ? "selected"
						: "")+ '>Pharmaceuticals</option>'
				+'<option value="Public Relations" '+(checkEquals(userBasicObj.industryName,"Public Relations") ? "selected"
						: "")+ '>Public Relations</option>'
				+'</select>'
				//<input type="text" class="form-control userIndustryName" name="industryName" placeholder="Industry Name" value="'
				//+ ((checkEmpty(userBasicObj.industryName)) ? ""
				//		: userBasicObj.industryName)
				+ '</div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserProfileHeader">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserProfileHeader">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button></div></form></div>';
		$('#userProfileHeaderForm').append(formObj);
		console.log(userBasicObj.jobFunction);
		$('.hideCtc').click(function()
		{
			if($(this).prop("checked"))
			{
				$(this).val(1);
			}
			else
			{	
				$(this).val(0);
			}
		});
		
	}

	function addHtmlForUserProfileHeader(userBasicObj) {
		console.log("adding html for user award");
		console.log(userBasicObj);
		var formDetail = '<div class="work-experience"><div class=""><h2>'
				+ ((checkEmpty(userBasicObj.firstName)) ? ""
						: userBasicObj.firstName)
				+ ((checkEmpty(userBasicObj.lastName)) ? ""
						: ("&nbsp;" + userBasicObj.lastName))
				+ '</h2><h4 style="margin:0;" class="text-capitalize">'
				+ ((checkEmpty(userBasicObj.profileHeadline)) ? ""
						: userBasicObj.profileHeadline + " | ")
				+ ((checkEmpty(userBasicObj.address)) ? ""
						: userBasicObj.address)
				+ '</h4><p>'
				+ ((checkEmpty(userBasicObj.jobFunction)) ? ""
						: userBasicObj.jobFunction)
				+ ((checkEmpty(userBasicObj.industryName)) ? ""
						: ' | ' + userBasicObj.industryName)
				+ '</p>'
				+ ((checkEmpty(userBasicObj.summary)) ? ""
						: userBasicObj.summary)
				+ '</p></div></h6></div>'
				+ '<p class="margin-0 text-capitalize"><a class="editUserProfileHeader"><i class="fa fa-pencil-square-o"></i> Edit Basic Info <i style="font-size: 50%; margin-right: 4px; color:rgb(255, 00, 00);position:relative;top:-5px;" class="fa fa-asterisk"></i></a> | <a class="editUserProfileResume"> <i class="fa fa-upload"></i> Upload Resume </a>'
				+ '| <a class="link-public-profile"><i class="fa fa-eye"></i> Recruiter View</a></p>'
				+ '</div>';
		$("#userProfileHeader").append(formDetail);

	}

	function addHtmlForUserProfileImageUploadForm() {
		console.log("addHtmlForProfileHeader")
		$('#userProfileHeader').children().hide();
		$('#editUserProfileHeader').hide();
		$('#JobSeekerBasic').hide();
		var formObj = '<div class="basic-form col-sm-8"><form role="form" class="userProfileImageForm" method="post" enctype="multipart/form-data">'
				+ '<div class="form-group"><label class="control-label"><h5>Upload Image</h5></label>'
				+ '<input type="file" name="profileImageData" class="form-control" required="required" />'
				+ '</div><div class="form-group col-sm-6 col-xs-12 pull-right"><button id="btn-profileImage-cancel" type="button" class=" form-control btn btn-danger cancelUserProfileImage">'
				+ 'Cancel</button></div>'
				+ '<div class="form-group col-sm-6 col-xs-12 pull-right"><button id="btn-profileImage" type="submit" class=" form-control btn btn-primary">'
				+ '<i class="fa fa-upload"></i> Upload</button>'
				+ '</div></form></div>';
		$('#userProfileHeaderForm').append(formObj);
	}
	function checkEquals(str1,str2) {
	    if (str1==str2) {
	        return true;
	    }
	    return false;
	}
	function addHtmlForUserProfileResumeUploadForm() {
		console.log("addHtmlForProfileHeader")
		$('#userProfileHeader').children().hide();
		$('#editUserProfileHeader').hide();
		$('#JobSeekerBasic').hide();
		var formObj = '<div class="basic-form col-sm-8"><form role="form" class="uploadResume" method="post" enctype="multipart/form-data">'
				+ '<div class="form-group"><label class="control-label"><h5>Upload Resume</h5></label>'
				+ '<input type="file" name="resumeData" class="form-control" required="required" />'
				+ '</div><div class="form-group col-sm-6 col-xs-12 pull-right"><button id="btn-resume-cancel" type="button" class=" form-control btn btn-danger cancelUserProfileResume">'
				+ 'Cancel</button></div>'
				+ '<div class="form-group col-sm-6 col-xs-12 pull-right"><button id="btn-resume" type="submit" class=" form-control btn btn-primary">'
				+ '<i class="fa fa-upload"></i> Upload</button>'
				+ '</div></form></div>';
		$('#userProfileHeaderForm').append(formObj);
	}
	//===========================USER PROFILEHEADER END==========================
</script>