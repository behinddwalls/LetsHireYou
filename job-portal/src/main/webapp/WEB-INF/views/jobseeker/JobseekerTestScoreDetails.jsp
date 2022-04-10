<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<jsp:include page="JobSeekerCommonUtils.jsp"></jsp:include>
<script type="text/javascript">
    // ===========================USER TEST START==========================
    $('#listOfUserTest').delegate('[class*=editUserTest]', 'click', function() {
        console.log("editUserTest is working ");
        console.log("zzzzzz in edit")

        addUserTestForm($(this).closest('div.work-experience').find('input.userTestId').val());
    });

    // adding jquery for new test details
    var newUserTestForm = '<div class="addNewUserTest basic-form col-sm-8"><form role="form" class="userTestForm"><div class="row"><div class="col-sm-6"><div class="form-group userName"><label for="userTestName">Test Name:</label> <input type="text" placeholder="Add Test Name" class="form-control userTestName" name="testName"></div></div><div class="col-sm-6"><div class="form-group userTestScoreGrp"><label for="userTestScore">Score:</label> <input type="text" placeholder="Add Test Score" class="form-control userTestScore" name="testScore"></div></div></div><div class="row"><div class="col-sm-4"><div class="form-group userTestDateGrp"><label for="userTestDate">Date:</label> <input type="date" class="form-control userTestDate" name="testDate"></div></div><div class="col-sm-8"><div class="form-group userTestDescriptionGrp"><label for="userTestDescription">Description:</label><textarea class="form-control userTestDescription" placeholder="Add Description"name="testDescription"></textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserTest">Cancel<i style="margin-left: 4px; margin-right: 4px; color: rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="submit" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserTest">Save<i style="margin-left: 4px; margin-right: 4px; color: rgb(54, 207, 120)" class="fa fa-check"></i></button></div></form></div>';

    $("#add-user-test").click(function() {
        // $(this).parent().parent().parent().before(newUserTestForm);
        console.log("zzzzz add user test form")
        $('#listOfUserTest').before(newUserTestForm);
        $(this).hide();
    });

    $("#UserTestScores").delegate('.cancelUserTest', 'click', function() {
        console.log("cancel button is working");
        clearHtmlDom($(this).closest('form').parent(), $);
        $('#add-user-test').show();
        $('#listOfUserTest').children().show();
    });

    $("#UserTestScores").delegate('.saveUserTest', 'click', function(e) {
        console.log("save button is working");
        e.preventDefault();
        var data = $(this).closest('form').serialize();
        var divToClear = $(this).closest('form').parent();
        $.ajax({
            url : "${contextPath}/jobseeker/tests/save",
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
                $('#add-user-test').show();
                refreshAllUserTestDetails();
            } else { //data.status == "Error"
                $('.userTestForm .formFieldError').detach();
                for ( var key in data.errorMap) {
                    var errorSpanForFormField = '<span style="color: red" class="formFieldError" id='+ key +'>' + data.errorMap[key] + '</span>';
                    $('.userTestForm [name^=' + key + ']').after(errorSpanForFormField);
                }
            }

        }).fail(function(xhr, status, errorThrown) {
            console.log("ajaxCallToSaveUserVolunteer:ERRORED OUT:DO SOME ERROR HANDLING HERE")
        }).always(function(xhr) {
            stopLoader();
        });

    });

    $("#UserTestScores").delegate('.deleteUserTest', 'click', function() {
        console.log("delete button is working");
        var userTestId = $(this).closest('form').find('input.userTestId').val();
        var divToClear = $(this).closest('form').parent();
        $.ajax({
            url : "${contextPath}/jobseeker/tests/remove/" + userTestId,
            type : "DELETE",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            clearHtmlDom(divToClear, $);
            $("#add-user-test").show();
            refreshAllUserTestDetails();
        }).fail(function(xhr, status, errorThrown) {

        }).always(function(xhr) {
            stopLoader();
        });

    });

    function refreshAllUserTestDetails() {
        removeAllUserTestFromHTML();
        getUserTestDetails();
    }

    function removeAllUserTestFromHTML() {
        $('#listOfUserTest div').detach();
    }

    function addUserTestForm(userTestId) {
        $.ajax({
            url : "${contextPath}/jobseeker/tests/viewById/" + userTestId,
            type : "GET",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            console.log("GET operation was successful: ");
            console.log(data)
            addHtmlForUserTestForm(data);
        }).fail(function(xhr, status, errorThrown) {
            console.log("ERROR DO SOME ERROR HANDLING");
        }).always(function(xhr) {
            stopLoader();
        });
    }

    function getUserTestDetails() {
        console.log("getUserTestDetails")
        $.ajax({
            url : "${contextPath}/jobseeker/tests/viewAll",
            type : "GET",
            beforeSend : function() {
                startLoader();
            }
        }).done(function(data, status, xhr) {
            console.log("GET operation was successful: ");
            console.log(data)
            addHtmlForUserTest(data);
        }).fail(function(xhr, status, errorThrown) {
            console.log("ERROR DO SOME ERROR HANDLING");
        }).always(function(xhr) {
            stopLoader();
        });
    }

    function addHtmlForUserTestForm(testObj) {
        $('#listOfUserTest').children().hide();
        $('#add-user-test').hide();
        var formObj = '<div class="addNewUserTest"><form role="form" class="userTestForm basic-form col-sm-8"><div class="form-group userTestIdGrp" style="display: none"><input type="text" class="form-control userTestId" name="testId" value=' + testObj.testId + '></div><div class="row"><div class="col-sm-6"><div class="form-group userTestNameGrp"><label for="userTestName">Test Name:</label> <input type="text" class="form-control userTestName" name="testName" value="'
                + ((checkEmpty(testObj.testName)) ? "" : testObj.testName)
                + '"></div></div><div class="col-sm-6"><div class="form-group userTestScoreGrp"><label for="userTestScore">Score:</label> <input type="text" class="form-control userTestScore" name="testScore" value="'
                + ((checkEmpty(testObj.testScore)) ? "" : testObj.testScore)
                + '"></div></div></div><div class="row"><div class="col-sm-4"><div class="form-group userTestDateGrp"><label for="userTestDate">Date:</label> <input type="date" class="form-control userTestDate" name="testDate" value='
                + ((checkEmpty(testObj.testDate)) ? "" : testObj.testDate)
                + '></div></div><div class="col-sm-8"><div class="form-group userTestDescriptionGrp"><label for="userTestDescription">Description:</label><textarea class="form-control userTestDescription" name="testDescription">'
                + ((checkEmpty(testObj.testDescription)) ? "" : testObj.testDescription)
                + '</textarea></div></div></div><div class="form-group text-center"><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center cancelUserTest">Cancel<i style="margin-left: 4px; margin-right: 4px; color: rgb(190, 35, 35)" class="fa fa-remove"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center saveUserTest">Save<i style="margin-left: 4px; margin-right: 4px; color: rgb(54, 207, 120)" class="fa fa-check"></i></button><button type="button" style="margin-left: 10px; margin-right: 10px" class="btn btn-primary text-center deleteUserTest">Remove<i style="margin-left: 4px; margin-right: 4px; color: rgb(236, 244, 216)" class="fa fa-trash-o"></i></button></div></form></div>';
        $('#listOfUserTest').before(formObj);
    }

    function addHtmlForUserTest(data) {
        $
                .each(
                        data,
                        function(i, testObj) {
                            var formDetail = '<div class="col-sm-10 col-sm-offset-0 work-experience"><div class="col-sm-11 col-xs-10 pull-left"><input class ="userTestId" type="hidden" name="testId" value=' + testObj.testId + ' ><h4 class="text-capitalize">'
                                    + ((checkEmpty(testObj.testName)) ? "" : testObj.testName)
                                    + '</h4><p class="margin-0">'
                                    + ((checkEmpty(testObj.testDate)) ? "" : "<i class='fa fa-calendar'></i> " + getLocalizedDateString(testObj.testDate))
                                    + '</p><p class="margin-0">' + ((checkEmpty(testObj.testScore)) ? "" : "Score: " + testObj.testScore)
                                    + '</p><p>'
                                    + ((checkEmpty(testObj.testDescription)) ? "" : testObj.testDescription)
                                    + '</p></div><div class="pull-left col-sm-1 col-xs-2"style="color: grey; font-size: 1.2em;"><i class="pull-right fa fa-pencil editUserTest"></i></div></div>';
                            $("#listOfUserTest").append(formDetail);
                        });
    }
    //===========================USER TEST END==========================
</script>