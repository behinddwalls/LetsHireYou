	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<jsp:include page="../common/head.jsp"></jsp:include>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!-- ===================   End Of JOBS Details  Section.  ======================= -->

<style>
.search-items {
	border-top: 0px;
	margin-bottom: 0px;
}

.jobs {
	border-top: 1px solid #e7e7e7;
}
</style>

<!-- ============ Adding new section to show JOB details ============ -->
<section>

	<div class="container">
		<div class="row">
			<div class="col-sm-12 col-sm-offset-0 ">
				<h2 class="pull-left">My Jobs</h2>
				<a href="${contextPath}/recruiter/dashboard" class="pull-right"><i
					class="fa fa-arrow-left"></i> Go back to Dashboard</a>
			</div>
			<div class="responseMessage"></div>
		</div>
		<!--  
	<form class="quiz_filter_form" action="" method="get">
	-->
		<div class="row">
			<form action="${contextPath}/job/getJobs" method="post"
				id="search-form" class="search-form">
				<div class="form-group col-sm-6 col-xs-12 pull-left">
					<select name="jobStatus" id="job_status_filter"
						class="form-control">
						<option value="ALLJOBS" selected>All Jobs</option>
						<option value=ACTIVE>Active Jobs</option>
						<option value="CLOSED">Closed Jobs</option>
					</select>
				</div>

				<div class="form-group col-sm-6 col-xs-12 pull-left">
					<select name="jobSortCriteria" id="job_sort" class="form-control">
						<option value="newest" selected="selected">Sort by Newest</option>
						<option value="oldest">Sort by Oldest</option>
						<option value="nameaz">Sort by Title A-Z</option>
						<option value="nameza">Sort by Title Z-A</option>
						<option value="locaz">Sort by Location A-Z</option>
						<option value="locza">Sort by Location Z-A</option>
					</select>
				</div>
			</form>
			<div class="clearfix"></div>
			<div class="col-sm-12">
				<h3 class="pull-left">Job Results</h3>
				<jsp:include page="../common/pager.jsp"></jsp:include>
				<div class="jobs"></div>
				<jsp:include page="../common/pager.jsp"></jsp:include>
				<div class="clearfix"></div>
			</div>
		</div>
	</div>
</section>
<!-- ============ FOOTER START ============ -->

<jsp:include page="../common/footer.jsp"></jsp:include>

<!-- ============ FOOTER END ============ -->

<!--  ===============  JAVA Script Sections ========================== -->
<script type="text/javascript">
    //////  Global variable for storing the data in  it.
    var globalJobdata;
    var pagination;
    //=============== Start of OnLoad document. ====================
    (function($) {
        var searchform = $('form#search-form');
        var form = $('form.search-form');
        var url = searchform.attr("action");
        console.log(url);
        var data = searchform.serialize();
        var pagerData;
        fetchJobDataModel("${contextPath}/recruiter/job/getJobs", data + '&' + pagerData);

    })(jQuery);

    // =============== End of OnLoad document.  ====================

    $('#job_sort').on('change', function(e) {
        var form = $(this);
        var searchform = $('form#search-form');
        var url = searchform.attr("action");
        console.log(url);
        var data = searchform.serialize();
        fetchJobDataModel("${contextPath}/recruiter/job/getJobs", data + '&' + pagination);
    });

    $('#job_status_filter').on('change', function(e) {
        var form = $(this);
        var searchform = $('form#search-form');
        var url = searchform.attr("action");
        console.log(url);
        var data = searchform.serialize();
        fetchJobDataModel("${contextPath}/recruiter/job/getJobs", data + '&' + pagination);
    });

    $('form.search-form').submit(function(event) {
        event.preventDefault();
        event.stopPropagation();
        var form = $(this);
        var searchform = $('form#search-form');
        var url = searchform.attr("action");
        var data = searchform.serialize();
        var pagerData = form.serialize();
        fetchJobDataModel("${contextPath}/recruiter/job/getJobs", data + '&' + pagerData);

    });

    // ==============   JAVA SCRIPT COMMON FUNCTIONS START============================================
    //This 
    function fetchJobDataModel(urlPath, data) {
        $('.pager li.previous').addClass('hidden');
        $('.pager li.next').addClass('hidden');
        startLoader();
        $.ajax({
            url : urlPath,
            type : "POST",
            data : data,
            xhrFields : {
                withCredentials : true
            }
        }).done(function(result) {
            //set the global var value
            globalJobdata = result.jobDataModelList;
            //clera the 'jobs' Div.
            $('.jobs').empty();

            if (result.jobDataModelList && result.jobDataModelList.length > 0) {
                $.each(result.jobDataModelList, function(index, jobDataModel) {
                    $('.jobs').append(getSearchResultEntry(jobDataModel));
                    //Now hide the buttons that not not required.
                    if (jobDataModel.jobClosed) {
                        //find by Id the HTML obj
                        var htmlEditBtn = document.getElementById('edit' + jobDataModel.jobId);
                        var htmlDeleteBtn = document.getElementById('delete' + jobDataModel.jobId);
                        var htmlSearchBtn = document.getElementById('search' + jobDataModel.jobId);
                        $(htmlEditBtn).hide();
                        $(htmlDeleteBtn).hide();
                        $(htmlSearchBtn).hide();
                    }
                });
                pagination = result.pagination;
                console.log(result.pagination);
                if (pagination && (pagination.showPrevButton == true)) {
                    $('.pager li.previous').find('input[name="pagination.pageNumber"]').val(pagination.pageNumber);
                    $('.pager li.previous').removeClass('hidden');
                }
                if (pagination && (pagination.showNextButton == true)) {
                    $('.pager li.next').find('input[name="pagination.pageNumber"]').val(pagination.pageNumber);
                    $('.pager li.next').removeClass('hidden');
                }
            } else {
                $('.jobs').append("<span class='text-center'><h1>No Result found.</h1</span>");
            }
        }).fail(function(result) {
            toast('No Job is found  !!!');
            console.log("error" + result);
            //alert("Some error encountered");
        }).complete(function() {
            stopLoader();
        });
    }

    ///fuction for populating the content dynamically.
    function getSearchResultEntry(jobDataModel) {
        return '<div class="row" id="search-result-panel"><div class="col-sm-12" style="padding-top:0px;"><div class="search-items"><div class="search-item"><div class="col-sm-12"><div style="float: none; width: 100%" class="row"><div class="pull-left col-sm-5 col-xs-12 "><p class="pull-left"><i class="fa fa-chevron-circle-right"></i></p><h5 class="pull-left" style="margin-top:5px;">'
                + jobDataModel.title
                + '</h5><div class="clearfix"></div><p><i class="fa fa-building-o"></i>'
                + jobDataModel.organisationName
                + '</p><p><i class="fa fa-location-arrow"></i>'
                + jobDataModel.location
                + '</p></div><button type="button"class="btn btn-link info-link search-candidates pull-right" title="SearchCandidate" value="'+jobDataModel.jobId+'" id="search'+jobDataModel.jobId+'"><i class="fa fa-search"></i><span class=" text-capitalize">Search Candidate</span></button><button type="button" class="btn btn-link primary-link edit pull-right" title="Edit" value="'+jobDataModel.jobId+'" id="edit'+jobDataModel.jobId+'"><i class="fa fa-pencil"></i><span class="text-capitalize">Edit Job</span></button><button type="button"class="btn btn-link success-link shortlistedcandidate pull-right" title="ShortlistedCandidate" value="'+jobDataModel.jobId+'"><i class="fa fa-check"></i><span class=" text-capitalize">View Candidates</span></button><button type="button" class="btn btn-link danger-link delete pull-right"title="Delete" value="'+jobDataModel.jobId+'" id="delete'+jobDataModel.jobId+'"><i class="fa fa-remove"></i><span class="text-capitalize">Close Job</span></button></div></div><div class="clearfix"></div></div></div></div>';
    }

    // ==============   JAVA SCRIPT COMMON FUNCTIONS START============================================

    $('.jobs').delegate('.shortlistedcandidate', 'click', function() {
        //redirect it to  shortlisted jobseeker.
        location.href = "${contextPath}/recruiter/candidate/search/shortlist/" + this.value;
    });

    $('.jobs').delegate('.search-candidates', 'click', function() {
        //redirect it to  search page
        location.href = "${contextPath}/recruiter/search/" + this.value;
    });

    $('.jobs').delegate('.btn-searchcandidate', 'click', function() {
        //Redirect it to the serach candidate page.
        //location.href="${contextPath}/recruiter/candidate/search/shortlist/"+this.value;
    });

    $('.jobs').delegate('.delete', 'click', function(event) {
        event.preventDefault();
        event.stopPropagation();
        jobId = this.value;
        var htmlObj = $(this);
        //propt confirmation
        swal({
            title : "Are you sure?",
            text : "Job will be closed permanently.",
            type : "warning",
            showCancelButton : true,
            confirmButtonColor : "#DD6B55",
            confirmButtonText : "Yes, Close Job!",
            closeOnConfirm : true
        }, function() {
            //Delete JOB detail.
            ajaxCallToDeleteJobDetail(jobId);
            //Remove the closest associated DIV.
            clearHtmlDom(htmlObj.closest('div').parent().parent());
        });
    });
    //  });

    //redirecting to JobPost pag for editing the JOB.
    $('.jobs').delegate('.edit', 'click', function() {
        location.href = "${contextPath}/recruiter/job/edit/" + this.value;
    });

    //Function for deleting the JOB detail.
    function ajaxCallToDeleteJobDetail(jobId) {
        //alert("AJAX delet:" + jobId);
        startLoader();
        $.post("${contextPath}/recruiter/job/delete/" + jobId, function(result, status) {
            if (status == "success") {
                toast('Job is closed successfully !!!');
            } else {
                toast('Some problem in closing the Job  !!!');
                //	alert("ajaxCallToDeleteJobDetails:ERRORED OUT:DO SOME ERROR HANDLING HERE");
                console.log("ajaxCallToDeleteJobDetails:ERRORED OUT:DO SOME ERROR HANDLING HERE")
            }
            //stop loader
            stopLoader();
        });
    }

    // ============COMMON JS START=============

    // supplly the entire div that needs to be removed here
    function clearHtmlDom(htmlObj) {
        $(htmlObj).detach();
    }

    $('.job-organisationName.typeahead').typeahead({
        hint : true,
        highlight : true,
        minLength : 1
    }, {
        name : 'job-organisationName',
        source : getBloodHoundObject("${contextPath}/organisation/search?orgName=")
    });

    $('.skill-name.typeahead').typeahead({
        hint : true,
        highlight : true,
        minLength : 1,
        maxTags : 10
    }, {
        name : 'skill-name',
        source : getBloodHoundObject("${contextPath}/skilldetail/search?skillName=")
    });

    // ============COMMON JS END=============
</script>

<!--  ===============  JAVA Script Sections ========================== -->

<div class="clearfix"></div>