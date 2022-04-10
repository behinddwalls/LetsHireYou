<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobSeekerCommonUtils.jsp"></jsp:include>
<script type="text/javascript">

// ===========================JOB EXPERIENCE START==========================
function initializeExperienceTypeahead() {
    genericTypeahead('input.jobCompany.typeahead', 'organisationNames', bloodHoundOrganisation);
}

$('#listOfJobExperiences').delegate('[class*=editJobExp]', 'click', function() {
    console.log("editJobExp is working ");
    addJobExperienceForm($(this).closest('div.work-experience').find('input.jobExpId').val());

});

// adding jquery for new experience details
var newJobExperience = '<div class="addNewJobExperience basic-form col-sm-8"><form role="form" class="userExperienceForm"><div class="row"><div class="col-sm-6"><div class="form-group jobTitleGrp"><label for="jobTitle">Title:</label> <input type="text" placeholder="Add Job Title" class="form-control jobTitle" name="roleName"></div></div><div class="col-sm-6"><div class="form-group jobCompanyGrp"><label for="jobCompany">Company Name:</label> <input type="text" placeholder="Add Company Name" class="form-control typeahead jobCompany" name="companyName"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group jobStartDateGrp"><label for="jobStartDate">Start Date:</label> <input type="date" class="form-control jobStartDate" name="startDate"></div></div><div class="col-sm-6"><div class="form-group jobEndDateGrp"><label for="jobEndDate">End Date:</label> <input type="date" class="form-control jobEndDate" name="endDate"></div></div></div><div class="row"><div class="col-sm-10"><label for="id_current"><input type="checkbox" name="current" id="id_current"> I currently work here </label></div></div><div class="row"><div class="col-sm-10"><div class="form-group jobLocationGrp"><label for="jobLocation">Location:</label> <input type="text" placeholder="Add Location" class="form-control jobLocation autocompleteAddress" name="location"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group jobDescriptionGrp"><label for="jobDescription">Description:</label><textarea class="form-control jobExpDescription" placeholder="Add Description" name="description"></textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelJobExp">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="submit" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveJobExp">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button></div></form></div>';

$("#add-job-experience").click(function() {
    console.log("zzzzz add job experience form")
    $('#listOfJobExperiences').before(newJobExperience);
    $("#id_current").click(function() {
        if( $(this).prop("checked")) {
            $(".jobEndDateGrp").hide();
            $(".jobEndDate").val("");
        } else {
        	$(".jobEndDateGrp").show();
        }
    }); 

    $(this).hide();
    $('#listOfJobExperiences').children().hide();
    initializeGoogleAutoFillPlaces();
    initializeExperienceTypeahead();
});

$("#JobExperience").delegate('.cancelJobExp', 'click', function() {
    console.log("cancel button is working");
    clearHtmlDom($(this).closest('form').parent());
    $('#add-job-experience').show();
    $('#listOfJobExperiences').children().show();
});

$("#JobExperience").delegate('.saveJobExp', 'click', function(e) {
    console.log("save button is working");
    e.preventDefault();
    var data = $(this).closest('form').serialize();
    var divToClear = $(this).closest('form').parent();
    $.ajax({
        url : "${contextPath}/jobseeker/experience/save",
        type : "POST",
        data : data,
        beforeSend : function() {
            startLoader();
        }
    }).done(function(data, status, xhr) {
        if (data.status == "Success") {
            clearHtmlDom(divToClear);
            $('#add-job-experience').show();
            refreshJobExperienceDetails();
        } else { // data.status == "Error"
            $('.userExperienceForm .formFieldError').detach();
            for ( var key in data.errorMap) {
                var errorSpanForFormField = '<span style="color: red" class="formFieldError" id=' + key + '>' + data.errorMap[key] + '</span>';
                $('.userExperienceForm [name^=' + key + ']').after(errorSpanForFormField);

            }
        }
        isProfileComplete("${contextPath}");
    }).fail(function(xhr, status, errorThrown) {
        console.log("ajaxCallToSaveUserEducation:ERRORED OUT:DO SOME ERROR HANDLING HERE")
    }).always(function(xhr) {
        stopLoader();
    });

});

$("#JobExperience").delegate('.deleteJobExp', 'click', function() {
    console.log("delete button is working");
    var jobExpId = $(this).closest('form').find('input.jobExpId').val();
    var divToClear = $(this).closest('form').parent();
    $.ajax({
        url : "${contextPath}/jobseeker/experience/remove/" + jobExpId,
        type : "DELETE",
        beforeSend : function() {
            startLoader();
        }
    }).done(function(data, status, xhr) {
        clearHtmlDom(divToClear);
        $('#add-user-experience').show();
        refreshJobExperienceDetails();
        isProfileComplete("${contextPath}");
    }).fail(function(xhr, status, errorThrown) {
    }).always(function(xhr) {
        stopLoader();
    });

});

function refreshJobExperienceDetails() {
    removeAllJobExperienceFromHTML();
    getJobExperienceDetails(); // then add new data
}

function removeAllJobExperienceFromHTML() {
    $('#listOfJobExperiences div').detach();
}

function saveJobExperienceDetail(formObject) {
    var data = formObject.serialize();
    console.log("called to add new jobExp ");
    $.post("${contextPath}/jobseeker/experience/save", data, function(result, status, httpRequestObj) {
        if (status == "success") {
            refreshJobExperienceDetails();
        } else {
            console.log("ajaxCallToSaveJobExp:ERRORED OUT:DO SOME ERROR HANDLING HERE")// dor

        }

    });
}

function addJobExperienceForm(jobExpId) {
    $.ajax({
        url : "${contextPath}/jobseeker/experience/viewById/" + jobExpId,
        type : "GET",
        beforeSend : function() {
            startLoader();
        }
    }).done(function(data, status, xhr) {
        console.log("GET operation was successful: ");
        console.log(data)
        addHtmlForJobExperienceForm(data);
    }).fail(function(xhr, status, errorThrown) {
        console.log("ERROR DO SOME ERROR HANDLING");
    }).always(function(xhr) {
        stopLoader();
    });
}

function getJobExperienceDetails() {
    console.log("ajaxCalltoGetAllJObExeperienceDetails")
    $.ajax({
        url : "${contextPath}/jobseeker/experience/viewAll",
        type : "GET",
        beforeSend : function() {
            startLoader();
        }
    }).done(function(data, status, xhr) {
        console.log("GET operation was successful: ");
        console.log(data)
        addHtmlForJobExperience(data); // add html to the div
    }).fail(function(xhr, status, errorThrown) {
        console.log("ERROR DO SOME ERROR HANDLING");
    }).always(function(xhr) {
        stopLoader();
    });
}
function addHtmlForJobExperienceForm(expObj) {
    $('#listOfJobExperiences').children().hide();
    $('#add-user-experience').hide();
    var formObj = '<div class="addNewJobExperience"><form role="form" class="userExperienceForm basic-form col-sm-8"><div class="form-group jobExpIdGrp" style="display: none"><input type="text" class="form-control jobExpId" name="experienceId" value='
            + expObj.experienceId
            + '></div><div class="row"><div class="col-sm-6"><div class="form-group jobTitleGrp"><label for="jobTitle">Title:</label> <input type="text" class="form-control jobExpTitle" name="roleName" value="'
            + ((checkEmpty(expObj.roleName)) ? "" : expObj.roleName)
            + '"></div></div><div class="col-sm-6"><div class="form-group jobCompanyGrp"><label for="jobCompany">Company Name:</label> <input type="text" class="form-control typeahead jobExpCompany" name="companyName" value="'
            + ((checkEmpty(expObj.companyName)) ? "" : expObj.companyName)
            + '"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group jobStartDateGrp"><label for="jobStartDate">Start Date:</label> <input type="date" class="form-control jobExpStartDate" name="startDate" value='
            + ((checkEmpty(expObj.startDate)) ? "" : expObj.startDate)
            + '></div></div><div class="col-sm-6"><div class="form-group jobEndDate"><label for="jobEndDate">End Date:</label> <input type="date" class="form-control jobExpEndDate" name="endDate" value='
            + ((checkEmpty(expObj.endDate)) ? "" : expObj.endDate)
            + '></div></div></div><div class="row"><div class="col-sm-10"><label for="id_current"><input type="checkbox" name="current" class="id_current"> I currently work here </label></div></div><div class="row"><div class="col-sm-10"><div class="form-group jobLocationGrp"><label for="jobLocation">Location:</label> <input type="text" class="form-control jobExpLocation autocompleteAddress" name="location" value="'
            + ((checkEmpty(expObj.location)) ? "" : expObj.location)
            + '"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group jobDescriptionGrp"><label for="jobDescription">Description:</label><textarea class="form-control jobExpDescription" name="description">'
            + ((checkEmpty(expObj.description)) ? "" : expObj.description)
            + '</textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelJobExp">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveJobExp">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center deleteJobExp">Remove<i style="margin-left: 4px; margin-right: 4px; color:rgb(236, 244, 216)" class="fa fa-trash-o"></i></button></div></form></div>';
    $('#listOfJobExperiences').before(formObj);
    if((checkEmpty(expObj.endDate)))
    		{
    			$(".jobEndDate").hide();
    			$(".id_current").prop('checked', true);
    		}
    else
    	{
	    	$(".jobEndDate").show();
			$(".id_current").prop('checked', false);
    	}
    $(".id_current").click(function() {
        if( $(this).prop("checked")) {
            $(".jobEndDate").hide();
            $(".jobExpEndDate").val("");
        } else {
        	$(".jobEndDate").show();
        }
    }); 
    initializeGoogleAutoFillPlaces();
    initializeExperienceTypeahead();
}

function addHtmlForJobExperience(data) {
    $
            .each(
                    data,
                    function(i, expObj) {
                        var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="jobExpId" type="hidden" name="experienceId" value='
                                + expObj.experienceId
                                + ' ><h4>'
                                + ((checkEmpty(expObj.companyName)) ? "" : expObj.companyName)
                                + ((checkEmpty(expObj.roleName)) ? "" : (" - " + expObj.roleName))
                                + '</h4><p class="margin-0">'
                                + ((checkEmpty(expObj.startDate)) ? "" : "<i class='fa fa-calendar'></i> " + getLocalizedDateString(expObj.startDate))
                                + ((checkEmpty(expObj.endDate)) ? " to  Present" : (" to " + getLocalizedDateString(expObj.endDate)))
                                + '</p><div class="clearfix"></div><p class="margin-0"><span>'
                                + ((checkEmpty(expObj.location)) ? "" : '<i class="fa fa-map-marker"></i> ' + expObj.location)
                                + '</span></p><p>'
                                + ((checkEmpty(expObj.description)) ? "" :  expObj.description)
                                + '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editJobExp"></i></div></div>';
                        $("#listOfJobExperiences").append(formDetail);
                    });
}
// ===========================JOB EXPERIENCE END==========================
</script>