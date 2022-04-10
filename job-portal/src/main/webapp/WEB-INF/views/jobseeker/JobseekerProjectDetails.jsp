<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobSeekerCommonUtils.jsp"></jsp:include>
<script type="text/javascript">
// ===========================USER PROJECT START==========================
$('#listOfUserProject').delegate('[class*=editUserProject]', 'click', function() {
    console.log("editUserProject is working ");
    console.log("zzzzzz in edit")
    addUserProjectForm($(this).closest('div.work-experience').find('input.userProjectId').val());
});

// adding jquery for new project details
var newUserProjectForm = '<div class="addNewUserProject basic-form col-sm-8"><form role="form" class="userProjectForm"><div class="row"><div class="col-sm-6"><div class="form-group userProjectNameGrp"><label for="userProjectName">Project Name:</label> <input type="text"placeholder="Add Project Name" class="form-control userProjectName" name="projectName"></div></div><div class="col-sm-6"><div class="form-group userProjectDateGrp"><label for="userProjectDate">Project Date:</label> <input type="date"class="form-control userProjectDate" name="projectDate"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userProjectURLGrp"><label for="userProjectURL">Project URL:</label> <input type="url"placeholder="Add Project URL" class="form-control userProjectURL" name="projectURL"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userProjectDescriptionGrp"><label for="userProjectDescription">Project Description:</label><textarea class="form-control userProjectDescription"placeholder="Add Description" name="projectDescription"></textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserProject">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="submit" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserProject">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button></div></form></div>';

$("#add-user-project").click(function() {
    $('#listOfUserProject').before(newUserProjectForm);
    $(this).hide();
    $('#listOfUserProject').children().hide();
});

$("#UserProject").delegate('.cancelUserProject', 'click', function() {
    console.log("cancel button is working");
    clearHtmlDom($(this).closest('form').parent(), $);
    $('#add-user-project').show();
    $('#listOfUserProject').children().show();
});

$("#UserProject").on('click', '.saveUserProject', function(e) {
    console.log("save button is working");
    e.preventDefault();
    // closest method checks the DOM element and its ancestors for the
    // selector		
    var data = $(this).closest('form').serialize();
    var divToClear = $(this).closest('form').parent();
    $.ajax({
        url : "${contextPath}/jobseeker/project/save",
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
            $('#add-user-project').show();
            refreshAllUserProjectDetails();
        } else { //data.status == "Error"
            $('.userProjectForm .formFieldError').detach();
            for ( var key in data.errorMap) {
                var errorSpanForFormField = '<span style="color: red" class="formFieldError" id='+ key +'>' + data.errorMap[key] + '</span>';
                $('.userProjectForm [name^=' + key + ']').after(errorSpanForFormField);
            }
        }

    }).fail(function(xhr, status, errorThrown) {
        console.log("ajaxCallToSaveUserProject:ERRORED OUT:DO SOME ERROR HANDLING HERE")
    }).always(function(xhr) {
        stopLoader();
    });

});

$("#UserProject").delegate('.deleteUserProject', 'click', function() {
    console.log("delete button is working");
    var userProjectId = $(this).closest('form').find('input.userProjectId').val();
    var divToClear = $(this).closest('form').parent();
    $.ajax({
        url : "${contextPath}/jobseeker/project/remove/" + userProjectId,
        type : "DELETE",
        beforeSend : function() {
            startLoader();
        }
    }).done(function(data, status, xhr) {
        clearHtmlDom(divToClear, $);
        $("#add-user-project").show();
        refreshAllUserProjectDetails();
    }).fail(function(xhr, status, errorThrown) {

    }).always(function(xhr) {
        stopLoader();
    });

});

function refreshAllUserProjectDetails() {
    removeAllUserProjectFromHTML();
    getUserProjectDetails();
}

function removeAllUserProjectFromHTML() {
    $('#listOfUserProject div').detach();
}

function addUserProjectForm(userProjectId) {
    $.ajax({
        url : "${contextPath}/jobseeker/project/viewById/" + userProjectId,
        type : "GET",
        beforeSend : function() {
            startLoader();
        }
    }).done(function(data, status, xhr) {
        console.log("GET operation was successful: ");
        console.log(data)
        addHtmlForUserProjectForm(data);
    }).fail(function(xhr, status, errorThrown) {
        console.log("ERROR DO SOME ERROR HANDLING");
    }).always(function(xhr) {
        stopLoader();
    });
}

function getUserProjectDetails() {
    console.log("ajaxCalltoGetAllJObExeperienceDetails")
    $.ajax({
        url : "${contextPath}/jobseeker/project/viewAll",
        type : "GET",
        beforeSend : function() {
            startLoader();
        }
    }).done(function(data, status, xhr) {
        console.log("GET operation was successful: ");
        console.log(data)
        addHtmlForUserProject(data); // add html to the div
    }).fail(function(xhr, status, errorThrown) {
        console.log("ERROR DO SOME ERROR HANDLING");
    }).always(function(xhr) {
        stopLoader();
    });
}

function addHtmlForUserProjectForm(projectObj) {
    $('#listOfUserProject').children().hide();
    $('#add-user-project').hide();
    var formObj = '<div class="addNewUserProject basic-form col-sm-8"><form role="form" class="userProjectForm"><div class="form-group userProjectIdGrp" style="display: none"><input type="text" class="form-control userProjectId"name="projectId" value=' + projectObj.projectId + '></div><div class="row"><div class="col-sm-6"><div class="form-group userProjectNameGrp"><label for="userProjectName">Project Name:</label> <input type="text"class="form-control userProjectName" name="projectName"value="'
            + ((checkEmpty(projectObj.projectName)) ? "" : projectObj.projectName)
            + '"></div></div><div class="col-sm-6"><div class="form-group userProjectDateGrp"><label for="userProjectDate">Project Date:</label> <input type="date"class="form-control userProjectDate" name="projectDate"value='
            + ((checkEmpty(projectObj.projectDate)) ? "" : projectObj.projectDate)
            + '></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userProjectURLGrp"><label for="userProjectURL">Project URL:</label> <input type="url"class="form-control userProjectURL" name="projectURL"value='
            + ((checkEmpty(projectObj.projectURL)) ? "" : projectObj.projectURL)
            + '></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userProjectDescriptionGrp"><label for="userProjectDescription">Project Description:</label><textarea class="form-control userProjectDescription"name="projectDescription">'
            + ((checkEmpty(projectObj.projectDescription)) ? "" : projectObj.projectDescription)
            + '</textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserProject">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserProject">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center deleteUserProject">Remove<i style="margin-left: 4px; margin-right: 4px; color:rgb(236, 244, 216)" class="fa fa-trash-o"></i></button></div></form></div>';
    $('#listOfUserProject').before(formObj);
}

function addHtmlForUserProject(data) {
    console.log("adding html for user project");
    console.log(data);
    $
            .each(
                    data,
                    function(i, projectObj) {
                        var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userProjectId" type="hidden" name="projectId" value=' + projectObj.projectId + ' ><h4>'
                                + ((checkEmpty(projectObj.projectName)) ? "" : projectObj.projectName)
                                + '</h4><div class="clearfix"></div><p class="margin-0">'
                                + ((checkEmpty(projectObj.projectDate)) ? "" : "<i class='fa fa-calendar'></i> " +getLocalizedDateString(projectObj.projectDate))
                                + '</p><p class="margin-0">'+ ((checkEmpty(projectObj.projectURL)) ? "" : "Project link: ") 
                                + '<a target="_new" href="'
                                + ((checkEmpty(projectObj.projectURL)) ? "" : projectObj.projectURL)
                                + '">'
                                + ((checkEmpty(projectObj.projectURL)) ? "" : projectObj.projectURL)
                                + '</a></p><p>'
                                + ((checkEmpty(projectObj.projectDescription)) ? "" : projectObj.projectDescription)
                                + '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserProject"></i></div></div>';
                        $("#listOfUserProject").append(formDetail);
                    });
}
//===========================USER PROJECT END==========================
</script>