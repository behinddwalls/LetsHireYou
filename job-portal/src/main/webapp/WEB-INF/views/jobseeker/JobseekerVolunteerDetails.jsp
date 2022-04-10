<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobSeekerCommonUtils.jsp"></jsp:include>
<script type="text/javascript">
//===========================USER VOLUNTEER START==========================

function initializeVolunteerTypeahead() {
    genericTypeahead('input.userVolunteerCause.typeahead', 'volunteerCauses', bloodHoundVolunteerCauses);
    genericTypeahead('input.userVolunteerOrganisation.typeahead', 'organisationNames', bloodHoundOrganisation);
}

$('#listOfUserVolunteer').delegate('[class*=editUserVolunteer]', 'click', function() {
    console.log("editUserVolunteer is working ");
    console.log("zzzzzz in edit")

    addUserVolunteerForm($(this).closest('div.work-experience').find('input.userVolunteerId').val());
});

// adding jquery for new volunteer details
var newUserVolunteerForm = '<div class="addNewUserVolunteer basic-form col-sm-8"><form role="form" class="userVolunteerForm"><div class="row"><div class="col-sm-6"><div class="form-group userRoleName"><label for="userVolunteerRoleName">Role:</label> <input placeholder="Add Your Role" type="text" class="form-control userVolunteerRoleName" name="roleName"></div></div><div class="col-sm-6"><div class="form-group dropdown userVolunteerCauseGrp"><label for="userVolunteerCause">Cause:</label> <input placeholder="Add Volunteering Cause" name="volunteerCause" class="form-control typeahead userVolunteerCause"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userVolunteerStartDateGrp"><label for="userVolunteerStartDate">Start Date:</label> <input type="date" class="form-control userVolunteerStartDate" name="volunteerStartDate"></div></div><div class="col-sm-6"><div class="form-group userVolunteerEndDateGrp"><label for="userVolunteerEndDate">End Date:</label> <input type="date" class="form-control userVolunteerEndDate" name="volunteerEndDate"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userVolunteerOrganisationGrp"><label for="userVolunteerOrganisation">Organisation:</label> <input placeholder="Add Organisation" type="text" class="form-control typeahead userVolunteerOrganisation" name="organisationName"></div></div><div class="col-sm-6"><div class="form-group userVolunteerDescriptionGrp"><label for="userVolunteerDescription">Description:</label><textarea placeholder="Add Description" class="form-control userVolunteerDescription" name="volunteerDescription"></textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserVolunteer">Cancel<i style="margin-left: 4px; margin-right: 4px; color: rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="submit" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserVolunteer">Save<i style="margin-left: 4px; margin-right: 4px; color: rgb(54, 207, 120)" class="fa fa-check"></i></button></div></form></div>';

$("#add-user-volunteer").click(function() {
    // $(this).parent().parent().parent().before(newUserVolunteerForm);
    $('#listOfUserVolunteer').before(newUserVolunteerForm);
    $(this).hide();
    initializeVolunteerTypeahead();
});

$("#UserVolunteer").delegate('.cancelUserVolunteer', 'click', function() {
    console.log("cancel button is working");
    clearHtmlDom($(this).closest('form').parent(), $);
    $('#add-user-volunteer').show();
    $('#listOfUserVolunteer').children().show();
});

$("#UserVolunteer").delegate('.saveUserVolunteer', 'click', function(e) {
    console.log("save button is working");
    e.preventDefault();
    var data = $(this).closest('form').serialize();
    var divToClear = $(this).closest('form').parent();
    $.ajax({
        url : "${contextPath}/jobseeker/volunteer/save",
        type : "POST",
        data : data,
        beforeSend : function() {
            startLoader();
        }
    }).done(function(data, status, xhr) {
        console.log("Post operation was successful: ");
        console.log(data)
        if (data.status == "Success") {
            clearHtmlDom(divToClear, $);
            $('#add-user-volunteer').show();
            refreshAllUserVolunteerDetails();
        } else { //data.status == "Error"
            $('.userVolunteerForm .formFieldError').detach();
            for ( var key in data.errorMap) {
                var errorSpanForFormField = '<span style="color: red" class="formFieldError" id='+ key +'>' + data.errorMap[key] + '</span>';
                $('.userVolunteerForm [name^=' + key + ']').after(errorSpanForFormField);
            }
        }

    }).fail(function(xhr, status, errorThrown) {
        console.log("ajaxCallToSaveUserVolunteer:ERRORED OUT:DO SOME ERROR HANDLING HERE")
    }).always(function(xhr) {
        stopLoader();
    });

});

$("#UserVolunteer").delegate('.deleteUserVolunteer', 'click', function() {
    console.log("delete button is working");
    var userVolunteerId = $(this).closest('form').find('input.userVolunteerId').val();
    var divToClear = $(this).closest('form').parent();
    $.ajax({
        url : "${contextPath}/jobseeker/volunteer/remove/" + userVolunteerId,
        type : "DELETE",
        beforeSend : function() {
            startLoader();
        }
    }).done(function(data, status, xhr) {
        clearHtmlDom(divToClear, $);
        $("#add-user-volunteer").show();
        refreshAllUserVolunteerDetails();
    }).fail(function(xhr, status, errorThrown) {

    }).always(function(xhr) {
        stopLoader();
    });

});

function refreshAllUserVolunteerDetails() {
    removeAllUserVolunteerFromHTML();
    getUserVolunteerDetails();
}

function removeAllUserVolunteerFromHTML() {
    $('#listOfUserVolunteer div').detach();
}

function addUserVolunteerForm(userVolunteerId) {
    $.ajax({
        url : "${contextPath}/jobseeker/volunteer/viewById/" + userVolunteerId,
        type : "GET",
        beforeSend : function() {
            startLoader();
        }
    }).done(function(data, status, xhr) {
        console.log("GET operation was successful: ");
        console.log(data)
        addHtmlForUserVolunteerForm(data);
    }).fail(function(xhr, status, errorThrown) {
        console.log("ERROR DO SOME ERROR HANDLING");
    }).always(function(xhr) {
        stopLoader();
    });
}

function getUserVolunteerDetails() {
    console.log("ajaxCalltoGetAllJObExeperienceDetails")
    $.ajax({
        url : "${contextPath}/jobseeker/volunteer/viewAll",
        type : "GET",
        beforeSend : function() {
            startLoader();
        }
    }).done(function(data, status, xhr) {
        console.log("GET operation was successful: ");
        console.log(data)
        addHtmlForUserVolunteer(data);
    }).fail(function(xhr, status, errorThrown) {
        console.log("ERROR DO SOME ERROR HANDLING");
    }).always(function(xhr) {
        stopLoader();
    });
}

function addHtmlForUserVolunteerForm(volunteerObj) {
    $('#listOfUserVolunteer').children().hide();
    $('#add-user-volunteer').hide();
    var formObj = '<div class="addNewUserVolunteer basic-form col-sm-8"><form role="form" class="userVolunteerForm"><div class="form-group userVolunteerIdGrp" style="display: none"><input type="text" class="form-control userVolunteerId" name="volunteerId" value=' + volunteerObj.volunteerId + '></div><div class="row"><div class="col-sm-6"><div class="form-group userRoleName"><label for="userVolunteerRoleName">Role:</label> <input type="text" class="form-control userVolunteerRoleName" name="roleName" value="'
            + ((checkEmpty(volunteerObj.roleName)) ? "" : volunteerObj.roleName)
            + '"></div></div><div class="col-sm-6"><div class="form-group userVolunteerCauseGrp"><label for="userVolunteerCause">Cause:</label> <input name="volunteerCause" class="form-control typeahead userVolunteerCause" value="'
            + ((checkEmpty(volunteerObj.volunteerCause)) ? "" : volunteerObj.volunteerCause)
            + '"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userVolunteerStartDateGrp"><label for="userVolunteerStartDate">Start Date:</label> <input type="date" class="form-control userVolunteerStartDate" name="volunteerStartDate" value='
            + ((checkEmpty(volunteerObj.volunteerStartDate)) ? "" : volunteerObj.volunteerStartDate)
            + '></div></div><div class="col-sm-6"><div class="form-group userEndDate"><label for="userEndDate">End Date:</label> <input type="date" class="form-control userVolunteerEndDate" name="volunteerEndDate" value='
            + ((checkEmpty(volunteerObj.volunteerEndDate)) ? "" : volunteerObj.volunteerEndDate)
            + '></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userVolunteerOrganisationGrp"><label for="userVolunteerOrganisation">Organisation:</label> <input type="text" class="form-control typeahead userVolunteerOrganisation" name="organisationName" value="'
            + ((checkEmpty(volunteerObj.organisationName)) ? "" : volunteerObj.organisationName)
            + '"></div></div><div class="col-sm-6"><div class="form-group userVolunteerDescriptionGrp"><label for="userVolunteerDescription">Description:</label><textarea class="form-control userVolunteerDescription" name="volunteerDescription">'
            + ((checkEmpty(volunteerObj.volunteerDescription)) ? "" : volunteerObj.volunteerDescription)
            + '</textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px"class="btn btn-primary text-center cancelUserVolunteer">Cancel<i style="margin-left: 4px; margin-right: 4px; color: rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserVolunteer">Save<i style="margin-left: 4px; margin-right: 4px; color: rgb(54, 207, 120)" class="fa fa-check"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center deleteUserVolunteer">Remove<i style="margin-left: 4px; margin-right: 4px; color: rgb(236, 244, 216)" class="fa fa-trash-o"></i></button></div></form></div>';
    $('#listOfUserVolunteer').before(formObj);
    initializeVolunteerTypeahead();
}

function addHtmlForUserVolunteer(data) {
    $
            .each(
                    data,
                    function(i, volunteerObj) {
                        var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userVolunteerId" type="hidden" name="volunteerId" value=' + volunteerObj.volunteerId + ' ><h4 class="text-capitalize">'
                                + ((checkEmpty(volunteerObj.organisationName)) ? "" : volunteerObj.organisationName)
                                + ((checkEmpty(volunteerObj.roleName)) ? "" : (" - " + volunteerObj.roleName))
                                + '</h4><p class="margin-0">'
                                + ((checkEmpty(volunteerObj.volunteerStartDate)) ? "" : "<i class='fa fa-calendar'></i> " + getLocalizedDateString(volunteerObj.volunteerStartDate))
                                + ((checkEmpty(volunteerObj.volunteerEndDate)) ? "" : (" - " + (getLocalizedDateString(volunteerObj.volunteerEndDate))))
                                + '</p><p class="margin-0 text-capitalize">'
                                + ((checkEmpty(volunteerObj.volunteerCause)) ? "" : volunteerObj.volunteerCause)
                                + '</p><p>'
                                + ((checkEmpty(volunteerObj.volunteerDescription)) ? "" : volunteerObj.volunteerDescription)
                                + '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserVolunteer"></i></div></div>';
                        $("#listOfUserVolunteer").append(formDetail);
                    });
}
//===========================USER VOLUNTEER END==========================

</script>