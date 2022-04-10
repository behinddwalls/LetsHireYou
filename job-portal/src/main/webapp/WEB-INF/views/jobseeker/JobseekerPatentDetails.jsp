<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobSeekerCommonUtils.jsp"></jsp:include>
<script type="text/javascript">
    // ===========================USER PATENT START==========================
    function initializePatentTypeahead() {
        genericTypeahead('input.userPatentOffice.typeahead', 'patentOffices', bloodHoundPatentOffice);
    }

    $('#listOfUserPatent').delegate('[class*=editUserPatent]', 'click', function() {
        console.log("editUserPatent is working ");
        console.log("zzzzzz in edit")
        addUserPatentForm($(this).closest('div.work-experience').find('input.userPatentId').val());
    });

    // adding jquery for new patent details
    var newUserPatentForm = '<div class="addNewUserPatent basic-form col-sm-8"><form role="form" class="userPatentForm"><div class="row"><div class="col-sm-10"><div class="form-group userPatentTitleGrp"><label for="userPatentTitle">Patent Title:</label> <input placeholder="Add Patent Title" type="text" class="form-control userPatentTitle" name="patentTitle"> </div></div></div><div class="row"><div class="col-sm-8"><div class="form-group userPatentOfficeGrp"><label for="userPatentOffice">Patent Office:</label> <input type="text" placeholder="Add Patent Issueing Country" class="form-control typeahead userPatentOffice" name="patentOffice"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userPatentStatusGrp"><label for="userPatentStatus">Patent Status:</label><fieldset id="PatentStatus" class="form-group userPatentStatus"><div class="col-sm-6"><label><input type="radio" class="form-control userPatentStatus" name="patentStatus" value="Pending" checked="checked" >Patent Pending</label> </div><div class="cols-sm-=6"><label><input type="radio" class="form-control userPatentStatus" name="patentStatus" value="Issued">Patent Issued</label></div></fieldset></div></div></div><div class="row"><div class="col-sm-8"><div class="form-group userPatentApplicationNumberGrp"><label for="userPatentApplicationNumber">Patent/Application Number:</label> <input type="text" placeholder="Add Patent/Application Number" class="form-control userPatentApplicationNumber" name="patentApplicationNumber"></div></div><div class="col-sm-4"><div class="form-group userPatentFillingDateGrp"><label for="userPatentFillingDate">Issue/Filling Date:</label> <input type="date" class="form-control userPatentFillingDate" name="patentFillingDate"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userPatentUrlGrp"><label for="userPatentUrl">Patent URL:</label> <input type="text" placeholder="Add URL" class="form-control userPatentUrl" name="patentUrl"></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userPatentDescriptionGrp"><label for="userPatentDescription">Description:</label><textarea class="form-control userPatentDescription" placeholder="Add Description" name="patentDescription"></textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserPatent">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="submit" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserPatent">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button></div></form></div>';

    $("#add-user-patent").click(function() {
        console.log("zzzzz add user patent form")
        $('#listOfUserPatent').before(newUserPatentForm);
        $(this).hide();
        $('#listOfUserPatent').children().hide();
        initializePatentTypeahead();

    });

    $("#UserPatent").delegate('.cancelUserPatent', 'click', function() {
        console.log("cancel button is working");
        clearHtmlDom($(this).closest('form').parent(), $);
        $('#add-user-patent').show();
        $('#listOfUserPatent').children().show();
    });

    $("#UserPatent").delegate('.saveUserPatent', 'click', function(e) {
        console.log("save button is working");
        e.preventDefault();
        var data = $(this).closest('form').serialize();
        var divToClear = $(this).closest('form').parent();

        $.ajax({
            url : "${contextPath}/jobseeker/patent/save",
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
                $('#add-user-patent').show();
                refreshAllUserPatentDetails();
            } else { //data.status == "Error"
                $('.userPatentForm .formFieldError').detach();
                for ( var key in data.errorMap) {
                    var errorSpanForFormField = '<span style="color: red" class="formFieldError" id='+ key +'>' + data.errorMap[key] + '</span>';
                    $('.userPatentForm [name^=' + key + ']').after(errorSpanForFormField);
                }
            }

        }).fail(function(xhr, status, errorThrown) {
            console.log("ajaxCallToSaveUserProject:ERRORED OUT:DO SOME ERROR HANDLING HERE")
        }).always(function(xhr) {
            stopLoader();
        });

    });

    $("#UserPatent").delegate('.deleteUserPatent', 'click', function() {
        console.log("delete button is working");
        var userPatentId = $(this).closest('form').find('input.userPatentId').val();
        var divToClear = $(this).closest('form').parent();
        $.ajax({
            url : "${contextPath}/jobseeker/patent/remove/" + userPatentId,
            type : "DELETE",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            clearHtmlDom(divToClear, $);
            $("#add-user-patent").show();
            refreshAllUserPatentDetails();
        }).fail(function(xhr, status, errorThrown) {

        }).always(function(xhr) {
            stopLoader();
        });

    });

    function refreshAllUserPatentDetails() {
        removeAllUserPatentFromHTML();
        getUserPatentDetails();
    }

    function removeAllUserPatentFromHTML() {
        $('#listOfUserPatent div').detach();
    }

    function addUserPatentForm(userPatentId) {
        $.ajax({
            url : "${contextPath}/jobseeker/patent/viewById/" + userPatentId,
            type : "GET",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            console.log("GET operation was successful: ");
            console.log(data)
            addHtmlForUserPatentForm(data);
        }).fail(function(xhr, status, errorThrown) {
            console.log("ERROR DO SOME ERROR HANDLING");
        }).always(function(xhr) {
            stopLoader();
        });
    }

    function getUserPatentDetails() {
        console.log("getUserPatentDetails")
        $.ajax({
            url : "${contextPath}/jobseeker/patent/viewAll",
            type : "GET",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            console.log("GET operation was successful: ");
            console.log(data)
            addHtmlForUserPatent(data); // add html to the div
        }).fail(function(xhr, status, errorThrown) {
            console.log("ERROR DO SOME ERROR HANDLING");
        }).always(function(xhr) {
            stopLoader();
        });
    }

    function addHtmlForUserPatentForm(patentObj) {
        $('#listOfUserPatent').children().hide();
        $('#add-user-patent').hide();
        var formObj = '<div class="addNewUserPatent basic-form col-sm-8"><form role="form" class="userPatentForm"><div class="form-group userPatentIdGrp" style="display: none"><input type="text" class="form-control userPatentId" name="patentId" value=' + patentObj.patentId + '></div><div class="row"><div class="col-sm-10"><div class="form-group userPatentTitleGrp"><label for="userPatentTitle">Patent Title:</label> <input type="text" class="form-control userPatentTitle" name="patentTitle" value="'
                + ((checkEmpty(patentObj.patentTitle)) ? "" : patentObj.patentTitle)
                + '"></div></div></div><div class="row"><div class="col-sm-8"><div class="form-group userPatentOfficeGrp"><label for="userPatentOffice">Patent Office:</label> <input type="text" class="form-control typeahead userPatentOffice" name="patentOffice" value="'
                + ((checkEmpty(patentObj.patentOffice)) ? "" : patentObj.patentOffice)
                + '"></div></div></div><div class="row"><div class="col-sm-6"><div class="form-group userPatentStatusGrp"><label for="userPatentStatus">Patent Status:</label><fieldset id="PatentStatus" class="form-group"><div class="col-sm-6"><label><input type="radio" class="form-control userPatentStatus" name="patentStatus" value="Pending" >Patent Pending</label></div><div class="cols-sm-=6"><label><input type="radio" class="form-control userPatentStatus" name="patentStatus" value="Issued">Patent Issued</label></div></fieldset></div></div></div><div class="row"><div class="col-sm-8"><div class="form-group userPatentApplicationNumberGrp"><label for="userPatentApplicationNumber">Patent Application Name:</label> <input type="text" class="form-control userPatentApplicationNumber" name="patentApplicationNumber" value="'
                + ((checkEmpty(patentObj.patentApplicationNumber) ? "" : patentObj.patentApplicationNumber))
                + '"></div></div><div class="col-sm-4"><div class="form-group userPatentFillingDateGrp"><label for="userPatentFillingDate">Patent Date:</label> <input type="date" class="form-control userPatentFillingDate" name="patentFillingDate" value='
                + ((checkEmpty(patentObj.patentFillingDate)) ? "" : patentObj.patentFillingDate)
                + '></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userPatentUrlGrp"><label for="userPatentUrl">Patent URL:</label> <input type="text" class="form-control userPatentUrl" name="patentUrl" value='
                + ((checkEmpty(patentObj.patentUrl)) ? "" : patentObj.patentUrl)
                + '></div></div></div><div class="row"><div class="col-sm-10"><div class="form-group userPatentDescriptionGrp"><label for="userPatentDescription">Patent Description:</label><textarea class="form-control userPatentDescription" name="patentDescription">'
                + ((checkEmpty(patentObj.patentDescription)) ? "" : patentObj.patentDescription)
                + '</textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserPatent">Cancel<i style="margin-left: 4px; margin-right: 4px; color:rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserPatent">Save<i style="margin-left: 4px; margin-right: 4px; color:rgb(54, 207, 120)" class="fa fa-check"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center deleteUserPatent">Remove<i style="margin-left: 4px; margin-right: 4px; color:rgb(236, 244, 216)" class="fa fa-trash-o"></i></button></div></form></div>';
        $('#listOfUserPatent').before(formObj);
        initializePatentTypeahead();
        $('.userPatentForm [value^="' + patentObj.patentStatus + '"]').attr("checked", "checked");
    }

    function addHtmlForUserPatent(data) {
        $
                .each(
                        data,
                        function(i, patentObj) {
                            var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userPatentId" type="hidden" name="patentId" value=' + patentObj.patentId + ' ><h4 class="text-capitalize">'
                                    + ((checkEmpty(patentObj.patentTitle)) ? "" : patentObj.patentTitle)
                                    + '</h4><p class="margin-0 text-capitalize">'
                                    + ((checkEmpty(patentObj.patentFillingDate)) ? "" : "<i class='fa fa-calendar'></i> " + getLocalizedDateString(patentObj.patentFillingDate))
                                    + '</p><p class="margin-0"><span class=" text-capitalize">'
                                    + ((checkEmpty(patentObj.patentStatus)) ? "" : "Status: " + patentObj.patentStatus)
                                    + '</span></p><p class="margin-0"><span class=" text-capitalize">'
                                    + ((checkEmpty(patentObj.patentApplicationNumber)) ? "" : "Number: " +patentObj.patentApplicationNumber)
                                    + '</span>&nbsp;&nbsp; <span class=" text-capitalize">'
                                    + ((checkEmpty(patentObj.patentOffice)) ? "" : "Office: "+ patentObj.patentOffice)
                                    + '</span></p>'+ ((checkEmpty(patentObj.patentUrl)) ? "" : "Patent link: ") +'<a target="_new" href='
                                    + ((checkEmpty(patentObj.patentUrl)) ? "" : patentObj.patentUrl)
                                    + '>'
                                    + ((checkEmpty(patentObj.patentUrl)) ? "" : patentObj.patentUrl)
                                    + '</a><p>'
                                    + ((checkEmpty(patentObj.patentDescription)) ? "" : patentObj.patentDescription)
                                    + '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserPatent"></i></div></div>';
                            $("#listOfUserPatent").append(formDetail);
                        });
    }
    //===========================USER PATENT END==========================
</script>