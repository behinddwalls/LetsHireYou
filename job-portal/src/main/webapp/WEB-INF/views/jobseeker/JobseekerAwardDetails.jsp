<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobSeekerCommonUtils.jsp"></jsp:include>
<script type="text/javascript">
    // ===========================USER AWARD START==========================
    function initializeAwardTypeahead() {
        genericTypeahead('input.userAwardOrganisationName.typeahead', 'organisationNames', bloodHoundOrganisation);
    }

    $('#listOfUserAward').delegate('[class*=editUserAward]', 'click', function() {
        console.log("editUserAward is working ");
        console.log("zzzzzz in edit")
        addUserAwardForm($(this).closest('div.work-experience').find('input.userAwardId').val());
    });

    // adding jquery for new award details
    var newUserAwardForm = '<div class="addNewUserAward basic-form col-sm-8"><form role="form" class="userAwardForm"><div class="row"><div class="col-sm-6"><div class="form-group userAwardNameGrp"><label for="userAwardName">Award Name:</label> <input type="text"placeholder="Add Award Name" class="form-control userAwardName" name="title"></div></div><div class="col-sm-6"><div class="form-group userAwardDateGrp"><label for="userAwardDate">Award Date:</label> <input type="date"class="form-control userAwardDate" name="date"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userAwardOrganisationNameGrp"><label for="userAwardOrganisationName">Organisation Name:</label> <input type="text" placeholder="Add Organisation Name"class="form-control typeahead userAwardOrganisationName" name="organisationName"></div> </div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserAward">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="submit" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserAward">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button></div></form></div>';

    $("#add-user-award").click(function() {
        $('#listOfUserAward').before(newUserAwardForm);
        $(this).hide();
        $('#listOfUserAward').children().hide();
        initializeAwardTypeahead();
    });

    $("#UserAward").delegate('.cancelUserAward', 'click', function() {
        console.log("cancel button is working");
        clearHtmlDom($(this).closest('form').parent(), $);
        $('#add-user-award').show();
        $('#listOfUserAward').children().show();
    });

    $("#UserAward").on('click', '.saveUserAward', function(e) {
        console.log("save button is working");
        e.preventDefault();
        // closest method checks the DOM element and its ancestors for the
        // selector		
        var data = $(this).closest('form').serialize();
        var divToClear = $(this).closest('form').parent();
        $.ajax({
            url : "${contextPath}/jobseeker/award/save",
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
                $('#add-user-award').show();
                refreshAllUserAwardDetails();
            } else { //data.status == "Error"
                $('.userAwardForm .formFieldError').detach();
                for ( var key in data.errorMap) {
                    var errorSpanForFormField = '<span style="color: red" class="formFieldError" id='+ key +'>' + data.errorMap[key] + '</span>';
                    $('.userAwardForm [name^=' + key + ']').after(errorSpanForFormField);
                }
            }

        }).fail(function(xhr, status, errorThrown) {
            console.log("ajaxCallToSaveUserAward:ERRORED OUT:DO SOME ERROR HANDLING HERE")
        }).always(function(xhr) {
            stopLoader();
        });

    });

    $("#UserAward").delegate('.deleteUserAward', 'click', function() {
        console.log("delete button is working");
        var userAwardId = $(this).closest('form').find('input.userAwardId').val();
        var divToClear = $(this).closest('form').parent();
        $.ajax({
            url : "${contextPath}/jobseeker/award/remove/" + userAwardId,
            type : "DELETE",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            clearHtmlDom(divToClear, $);
            $("#add-user-award").show();
            refreshAllUserAwardDetails();
        }).fail(function(xhr, status, errorThrown) {

        }).always(function(xhr) {
            stopLoader();
        });

    });

    function refreshAllUserAwardDetails() {
        removeAllUserAwardFromHTML();
        getUserAwardDetails();
    }

    function removeAllUserAwardFromHTML() {
        $('#listOfUserAward div').detach();
    }

    function addUserAwardForm(userAwardId) {
        $.ajax({
            url : "${contextPath}/jobseeker/award/viewById/" + userAwardId,
            type : "GET",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            console.log("GET operation was successful: ");
            console.log(data)
            addHtmlForUserAwardForm(data);
        }).fail(function(xhr, status, errorThrown) {
            console.log("ERROR DO SOME ERROR HANDLING");
        }).always(function(xhr) {
            stopLoader();
        });
    }

    function getUserAwardDetails() {
        console.log("ajaxCalltoGetAllJObExeperienceDetails")
        $.ajax({
            url : "${contextPath}/jobseeker/award/viewAll",
            type : "GET",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            console.log("GET operation was successful: ");
            console.log(data)
            addHtmlForUserAward(data); // add html to the div
        }).fail(function(xhr, status, errorThrown) {
            console.log("ERROR DO SOME ERROR HANDLING");
        }).always(function(xhr) {
            stopLoader();
        });
    }

    function addHtmlForUserAwardForm(awardObj) {
        $('#listOfUserAward').children().hide();
        $('#add-user-award').hide();
        var formObj = '<div class="addNewUserAward basic-form col-sm-8"><form role="form" class="userAwardForm"><div class="form-group userAwardIdGrp" style="display: none"><input type="text" class="form-control userAwardId"name="awardId" value=' + awardObj.awardId + '></div><div class="row"><div class="col-sm-6"><div class="form-group userAwardNameGrp"><label for="userAwardName">Award Name:</label> <input type="text"class="form-control userAwardName" name="title"value="'
                + ((checkEmpty(awardObj.title)) ? "" : awardObj.title)
                + '"></div></div><div class="col-sm-6"><div class="form-group userAwardDateGrp"><label for="userAwardDate">Award Date:</label> <input type="date"class="form-control userAwardDate" name="date"value='
                + ((checkEmpty(awardObj.date)) ? "" : awardObj.date)
                + '></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userAwardOrganisationNameGrp"><label for="userAwardOrganisationName">Organisation Name:</label> <input type="text" class="form-control typeahead userAwardOrganisationName" name="organisationName" value="'
                + ((checkEmpty(awardObj.organisationName)) ? "" : awardObj.organisationName)
                + '"></div> </div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserAward">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserAward">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center deleteUserAward">Remove<i style="margin-left: 4px; margin-right: 4px; color:rgb(236, 244, 216)" class="fa fa-trash-o"></i></button></div></form></div>';
        $('#listOfUserAward').before(formObj);
        initializeAwardTypeahead();
    }

    function addHtmlForUserAward(data) {
        console.log("adding html for user award");
        console.log(data);
        $
                .each(
                        data,
                        function(i, awardObj) {
                            var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userAwardId" type="hidden" name="awardId" value=' + awardObj.awardId + ' ><h4>'
                                    + ((checkEmpty(awardObj.title)) ? "" : awardObj.title)
                                    + ((checkEmpty(awardObj.organisationName)) ? "" : (" - " + awardObj.organisationName))
                                    + '</h4><div class="clearfix"></div><p class="margin-0">'
                                    + ((checkEmpty(awardObj.date)) ? "" : "<i class='fa fa-calendar'></i> "+ getLocalizedDateString(awardObj.date))
                                    + '</p><br/></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserAward"></i></div></div>';
                            $("#listOfUserAward").append(formDetail);
                        });
    }
    //===========================USER AWARD END==========================
</script>