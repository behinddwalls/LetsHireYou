<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobSeekerCommonUtils.jsp"></jsp:include>
<script type="text/javascript">
    // ===========================USER CERTIFICATION START==========================
    function initializeCertificationTypeahead() {
        genericTypeahead('input.userCertificationOrganisation.typeahead', 'organisationNames', bloodHoundOrganisation);
    }

    $('#listOfUserCertification').delegate('[class*=editUserCertification]', 'click', function() {
        console.log("editUserCertification is working ");
        addUserCertificationForm($(this).closest('div.work-experience').find('input.userCertificationId').val());
    });

    // adding jquery for new certification details
    var newUserCertificationForm = '<div class="addNewUserCertification basic-form col-sm-8"><form role="form" class="userCertificationForm"><div class="row"><div class="col-sm-6"><div class="form-group userCertificationNameGrp"><label for="userCertificationName">Title:</label> <input type="text"placeholder="Add Title" class="form-control UserCertificationName" name="certificationName"></div></div><div class="col-sm-6"><div class="form-group userOrganisationNameGrp"><label for="userOrganisationName">Organisation:</label> <input type="text"placeholder="Add Certifier Organisation" class="form-control typeahead userCertificationOrganisation" name="organisationName"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userCertificationStartDateGrp"><label for="userCertificationStartDate">Start Date:</label> <input type="date"class="form-control userCertificationStartDate" name="certificationStartDate"></div></div><div class="col-sm-6"><div class="form-group userCertificationEndDateGrp"><label for="userCertificationEndDate">End Date:</label> <input type="date"class="form-control userCertificationEndDate" name="certificationEndDate"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userCertificationUrlGrp"><label for="userCertificationUrl">URL:</label> <input type="text"placeholder="Add URL" class="form-control userCertificationUrl"name="certificationUrl"></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserCertification">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="submit" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserCertification">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button></div></form></div>';

    $("#add-user-certification").click(function() {
        console.log("zzzzz add user certification form")
        $('#listOfUserCertification').before(newUserCertificationForm);
        $(this).hide();
        $('#listOfUserCertification').children().hide();
        initializeCertificationTypeahead();
    });

    $("#UserCertification").delegate('.cancelUserCertification', 'click', function() {
        console.log("cancel button is working");
        clearHtmlDom($(this).closest('form').parent(), $);
        $('#add-user-certification').show();
        $('#listOfUserCertification').children().show();
    });

    $("#UserCertification").delegate('.saveUserCertification', 'click', function(e) {
        e.preventDefault();
        var data = $(this).closest('form').serialize();
        console.log("zzzzz data is");
        console.log(data);
        var divToClear = $(this).closest('form').parent();

        $.ajax({
            url : "${contextPath}/jobseeker/certification/save",
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
                $('#add-user-certification').show();
                refreshAllUserCertificationDetails();
            } else { //data.status == "Error"
                $('.userCertificationForm .formFieldError').detach();
                for ( var key in data.errorMap) {
                    var errorSpanForFormField = '<span style="color: red" class="formFieldError" id='+ key +'>' + data.errorMap[key] + '</span>';
                    $('.userCertificationForm [name^=' + key + ']').after(errorSpanForFormField);

                }
            }

        }).fail(function(xhr, status, errorThrown) {
            console.log("ajaxCallToSaveUserCertification:ERRORED OUT:DO SOME ERROR HANDLING HERE")
        }).always(function(xhr) {
            stopLoader();
        });

    });

    $("#UserCertification").delegate('.deleteUserCertification', 'click', function() {
        console.log("delete button is working");
        var userCertificationId = $(this).closest('form').find('input.userCertificationId').val();
        var divToClear = $(this).closest('form').parent();
        $.ajax({
            url : "${contextPath}/jobseeker/certification/remove/" + userCertificationId,
            type : "DELETE",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            clearHtmlDom(divToClear, $);
            $("#add-user-certification").show();
            refreshAllUserCertificationDetails();
        }).fail(function(xhr, status, errorThrown) {
        }).always(function(xhr) {
            stopLoader();
        });
    });

    function refreshAllUserCertificationDetails() {
        removeAllUserCertificationFromHTML();
        getUserCertificationDetails();
    }

    function removeAllUserCertificationFromHTML() {
        $('#listOfUserCertification div').detach();
    }

    function addUserCertificationForm(userCertificationId) {
        $.ajax({
            url : "${contextPath}/jobseeker/certification/viewById/" + userCertificationId,
            type : "GET",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            console.log("GET operation was successful: ");
            console.log(data)
            addHtmlForUserCertificationForm(data);
        }).fail(function(xhr, status, errorThrown) {
            console.log("ERROR DO SOME ERROR HANDLING");
        }).always(function(xhr) {
            stopLoader();
        });
    }

    function getUserCertificationDetails() {
        console.log("ajaxCalltoGetAllJObExeperienceDetails")
        $.ajax({
            url : "${contextPath}/jobseeker/certification/viewAll",
            type : "GET",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            console.log("GET operation was successful: ");
            console.log(data)
            addHtmlForUserCertification(data); // add html to the div
        }).fail(function(xhr, status, errorThrown) {
            console.log("ERROR DO SOME ERROR HANDLING");
        }).always(function(xhr) {
            stopLoader();
        });
    }

    function addHtmlForUserCertificationForm(certificationObj) {
        $('#listOfUserCertification').children().hide();
        $('#add-user-certification').hide();
        var formObj = '<div class="addNewUserCertification basic-form col-sm-8"><form role="form" class="userCertificationForm"><div class="form-group userCertificationIdGrp" style="display: none"><input type="text" class="form-control userCertificationId" name="certificationId"value=' + certificationObj.certificationId + '></div><div class="row"><div class="col-sm-6"><div class="form-group userCertificationNameGrp"><label for="userCertificationName">Title:</label> <input type="text"class="form-control UserCertificationName" name="certificationName"value="'
                + ((checkEmpty(certificationObj.certificationName)) ? "" : certificationObj.certificationName)
                + '"></div></div><div class="col-sm-6"><div class="form-group userOrganisationNameGrp"><label for="userOrganisationName">Organisation:</label> <input type="text"class="form-control typeahead userCertificationOrganisation" name="organisationName"value="'
                + ((checkEmpty(certificationObj.organisationName)) ? "" : certificationObj.organisationName)
                + '"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userCertificationStartDateGrp"><label for="userCertificationStartDate">Start Date:</label> <input type="date"class="form-control userCertificationCertificationStartDate" name="certificationStartDate"value='
                + ((checkEmpty(certificationObj.certificationStartDate)) ? "" : certificationObj.certificationStartDate)
                + '></div></div><div class="col-sm-6"><div class="form-group userCertificationEndDate"><label for="userCertificationEndDate">End Date:</label> <input type="date"class="form-control userCertificationCertificationEndDate" name="certificationEndDate"value='
                + ((checkEmpty(certificationObj.certificationEndDate)) ? "" : certificationObj.certificationEndDate)
                + '></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userCertificationUrlGrp"><label for="userCertificationUrl">URL:</label> <input type="url"class="form-control userCertificationCertificationUrl"name="certificationUrl" value='
                + ((checkEmpty(certificationObj.certificationUrl)) ? "" : certificationObj.certificationUrl)
                + '></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserCertification">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserCertification">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center deleteUserCertification">Remove<i style="margin-left: 4px; margin-right: 4px; color:rgb(236, 244, 216)" class="fa fa-trash-o"></i></button></div></form></div>';
        $('#listOfUserCertification').before(formObj);
        initializeCertificationTypeahead();
    }

    function addHtmlForUserCertification(data) {
        $
                .each(
                        data,
                        function(i, certificationObj) {
                            var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userCertificationId" type="hidden" name="certificationId" value=' + certificationObj.certificationId + ' ><h4>'
                                    + ((checkEmpty(certificationObj.certificationName)) ? "" : certificationObj.certificationName)
                                    + ((checkEmpty(certificationObj.organisationName)) ? "" : ("&nbsp;&nbsp;&nbsp;&nbsp;" + certificationObj.organisationName))
                                    + '</h4><p class="margin-0">'
                                    + ((checkEmpty(certificationObj.certificationStartDate)) ? "" : "<i class='fa fa-calendar'></i> " + getLocalizedDateString(certificationObj.certificationStartDate))
                                    + ((checkEmpty(certificationObj.certificationEndDate)) ? "" : ("- " + getLocalizedDateString(certificationObj.certificationEndDate)))
                                    + '</p>' + ((checkEmpty(certificationObj.certificationUrl)) ? "" : "Certification link:  ") + '<a target="_new" href='
                                    + ((checkEmpty(certificationObj.certificationUrl)) ? "" : certificationObj.certificationUrl)
                                    + '>'
                                    + ((checkEmpty(certificationObj.certificationUrl)) ? "" : certificationObj.certificationUrl)
                                    + '</a><p></p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserCertification"></i></div></div>';
                            $("#listOfUserCertification").append(formDetail);
                        });
    }
    //===========================USER CERTIFICATION END==========================
</script>