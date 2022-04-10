<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobSeekerCommonUtils.jsp"></jsp:include>
<script type="text/javascript">
    // ===========================USER PUBLICATION START==========================
    function initializePublicationTypeahead() {
        genericTypeahead('input.userPublicationOrganisation.typeahead', 'organisationNames', bloodHoundOrganisation);
    }

    $('#listOfUserPublication').delegate('[class*=editUserPublication]', 'click', function() {
        console.log("editUserPublication is working ");
        console.log("zzzzzz in edit")
        addUserPublicationForm($(this).closest('div.work-experience').find('input.userPublicationId').val());
    });

    // adding jquery for new publication details
    var newUserPublicationForm = '<div class="addNewUserPublication basic-form col-sm-8"><form role="form" class="userPublicationForm"><div class="row"><div class="col-sm-6"><div class="form-group userPublicationTitleGrp"><label for="userPublicationTitle">Publication Title:</label> <input type="text"class="form-control userPublicationTitle" name="publicationTitle"></div></div><div class="col-sm-6"><div class="form-group userPublicationDateGrp"><label for="userPublicationDate">Publication Date:</label> <input type="date"class="form-control userPublicationDate" name="publicationDate"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userPublicationOrganisationGrp"><label for="userPublicationOrganisation">Publication Organization:</label> <input type="text"class="form-control typeahead userPublicationOrganisation" name="publicationOrganisation"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userPublicationDescriptionGrp"><label for="userPublicationDescription">Publication Description:</label><textarea class="form-control userPublicationDescription"name="publicationDescription"></textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserPublication">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="submit" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserPublication">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button></div></form></div>';

    $("#add-user-publication").click(function() {
        // $(this).parent().parent().parent().before(newUserPublicationForm);
        console.log("zzzzz add user publication form")
        $('#listOfUserPublication').before(newUserPublicationForm);
        $(this).hide();
        $('#listOfUserPublication').children().hide();//hide all editable things.
        initializePublicationTypeahead();

    });

    $("#UserPublication").delegate('.cancelUserPublication', 'click', function() {
        console.log("cancel button is working");
        clearHtmlDom($(this).closest('form').parent(), $);
        $('#listOfUserPublication').children().show();
        $('#add-user-publication').show();
    });

    $("#UserPublication").delegate('.saveUserPublication', 'click', function(e) {
        console.log("save button is working");
        e.preventDefault();
        // closest method checks the DOM element and its ancestors for the
        // selector		 
        var data = $(this).closest('form').serialize();
        var divToClear = $(this).closest('form').parent();

        $.ajax({
            url : "${contextPath}/jobseeker/publication/save",
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
                $('#add-user-publication').show();
                refreshAllUserPublicationDetails();
            } else { //data.status == "Error"
                $('.userPublicationForm .formFieldError').detach();
                for ( var key in data.errorMap) {
                    var errorSpanForFormField = '<span style="color: red" class="formFieldError" id='+ key +'>' + data.errorMap[key] + '</span>';
                    $('.userPublicationForm [name^=' + key + ']').after(errorSpanForFormField);
                }
            }

        }).fail(function(xhr, status, errorThrown) {
            console.log("ERRORED OUT:DO SOME ERROR HANDLING HERE")
        }).always(function(xhr) {
            stopLoader();
        });

    });

    $("#UserPublication").delegate('.deleteUserPublication', 'click', function() {
        console.log("delete button is working");
        var userPublicationId = $(this).closest('form').find('input.userPublicationId').val();
        var divToClear = $(this).closest('form').parent()

        $.ajax({
            url : "${contextPath}/jobseeker/publication/remove/" + userPublicationId,
            type : "DELETE",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            clearHtmlDom(divToClear, $);
            $("#add-user-publication").show();
            refreshAllUserPublicationDetails();
        }).fail(function(xhr, status, errorThrown) {
        }).always(function(xhr) {
            stopLoader();
        });
    });

    function refreshAllUserPublicationDetails() {
        removeAllUserPublicationFromHTML();
        getUserPublicationDetails();
    }

    function removeAllUserPublicationFromHTML() {
        $('#listOfUserPublication div').detach();
    }

    function addUserPublicationForm(userPublicationId) {
        $.ajax({
            url : "${contextPath}/jobseeker/publication/viewById/" + userPublicationId,
            type : "GET",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            console.log("GET operation was successful: ");
            console.log(data)
            addHtmlForUserPublicationForm(data);
        }).fail(function(xhr, status, errorThrown) {
            console.log("ERROR DO SOME ERROR HANDLING");
        }).always(function(xhr) {
            stopLoader();
        });
    }

    function getUserPublicationDetails() {
        console.log("ajaxCalltoGetAllJObExeperienceDetails")
        $.ajax({
            url : "${contextPath}/jobseeker/publication/viewAll",
            type : "GET",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            console.log("GET operation was successful: ");
            console.log(data)
            addHtmlForUserPublication(data); // add html to the div
        }).fail(function(xhr, status, errorThrown) {
            console.log("ERROR DO SOME ERROR HANDLING");
        }).always(function(xhr) {
            stopLoader();
        });
    }

    function addHtmlForUserPublicationForm(publicationObj) {
        $('#listOfUserPublication').children().hide(); //hide all education list
        $('#add-user-publication').hide(); //hide adding feature when editing
        var formObj = '<div class="addNewUserPublication basic-form col-sm-8"><form role="form" class="userPublicationForm"><div class="form-group userPublicationIdGrp" style="display: none"><input type="text" class="form-control userPublicationId"name="publicationId" value=' + publicationObj.publicationId + '></div><div class="row"><div class="col-sm-6"><div class="form-group userPublicationTitleGrp"><label for="userPublicationTitle">Publication Title:</label> <input type="text"class="form-control userPublicationTitle"placeholder="Enter Publication Title" name="publicationTitle"value="'
                + ((checkEmpty(publicationObj.publicationTitle)) ? "" : publicationObj.publicationTitle)
                + '"></div></div><div class="col-sm-6"><div class="form-group userPublicationDateGrp"><label for="userPublicationDate">Publication Date:</label> <input type="date"class="form-control userPublicationDate" name="publicationDate"value='
                + ((checkEmpty(publicationObj.publicationDate)) ? "" : publicationObj.publicationDate)
                + '></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userPublicationOrganisationGrp"><label for="userPublicationOrganisation">Publication Organization:</label> <input type="text"class="form-control typeahead userPublicationOrganisation" placeholder="Enter Organisation Name" name="publicationOrganisation"value="'
                + ((checkEmpty(publicationObj.publicationOrganisation)) ? "" : publicationObj.publicationOrganisation)
                + '"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userPublicationDescriptionGrp"><label for="userPublicationDescription">Publication Description:</label><textarea class="form-control userPublicationDescription"placeholder="Enter Description" name="publicationDescription">'
                + ((checkEmpty(publicationObj.publicationDescription)) ? "" : publicationObj.publicationDescription)
                + '</textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserPublication">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserPublication">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center deleteUserPublication">Remove<i style="margin-left: 4px; margin-right: 4px; color:rgb(236, 244, 216)" class="fa fa-trash-o"></i></button></div></form></div>';
        $('#listOfUserPublication').before(formObj);
        initializePublicationTypeahead();
    }

    function addHtmlForUserPublication(data) {
        $
                .each(
                        data,
                        function(i, publicationObj) {
                            var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class="userPublicationId" type="hidden" name="publicationId"value=' + publicationObj.publicationId + '><h4>'
                                    + ((checkEmpty(publicationObj.publicationTitle)) ? "" : publicationObj.publicationTitle)
                                    + ((checkEmpty(publicationObj.publicationOrganisation)) ? "" : " - " + publicationObj.publicationOrganisation)
                                    + '</h4><p class="margin-0">'
                                    + ((checkEmpty(publicationObj.publicationDate)) ? "" :"<i class='fa fa-calendar'></i> "+ getLocalizedDateString(publicationObj.publicationDate))
                                    + '</p><p>'
                                    + ((checkEmpty(publicationObj.publicationDescription)) ? "" : publicationObj.publicationDescription)
                                    + '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserPublication"></i></div></div>';

                            $("#listOfUserPublication").append(formDetail);
                        });
    }
    //===========================USER PUBLICATION END==========================
</script>