<!-- View Jobs for Jobseeker which are having the actions from jobseeker
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<jsp:include page="../common/head.jsp"></jsp:include>
<style type="text/css">
.search-items {
	border-top: 0px !important;
}

.jobs {
	border-top: 1px solid #e7e7e7;
}
</style>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!-- ===================   End Of JOBS Details  Section.  ======================= -->

<!-- ============ Adding new section to show JOB details ============ -->
<section>
	<div class="container">
		<div class="row">
			<div class="col-sm-12 col-sm-offset-0 ">
				<h2 class="pull-left">My JOBS</h2>
				<a href="${contextPath}/jobseeker/dashboard" class="pull-right"><i
					class="fa fa-arrow-left"></i> Go back to Dashboard</a>
			</div>
			<div class="responseMessage"></div>
		</div>
		<!--  
	<form class="quiz_filter_form" action="" method="get">
	-->
		<div class="row">
			<ul class="nav nav-tabs">
				<li role="presentation" class=""><a
					href="${contextPath}/jobseeker/job/recommendedjob">Recommended
						Jobs</a></li>
				<li role="presentation" class="active"><a href="">My Jobs</a></li>
			</ul>
			<c:if test="${not empty errorMessage}">
				<div class="col-sm-12 alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert"
						aria-hidden="true">&times;</button>
					${errorMessage}
				</div>

			</c:if>
			<c:if test="${not empty successMessage}">
				<div class="col-sm-12 alert alert-success alert-dismissable">
					<button type="button" class="close" data-dismiss="alert"
						aria-hidden="true">&times;</button>
					${successMessage}
				</div>

			</c:if>
		</div>
		<br />
		<div class="row">
			<form action="${contextPath}/job/getJobs" method="post"
				id="search-form" class="search-form">
				<div class="form-group col-sm-4">
					<select name="jobStatus" id="job_status_filter"
						class="form-control col-sm-6 ">
						<option value="APPLIED" selected>APPLIED JOBS</option>
						<option value="REJECTED">REJECTED JOBS</option>
						<option value="WISHLISTED">WISHLISTED JOBS</option>
					</select>
				</div>

				<div class="form-group col-sm-4 ">
					<select name="jobSortCriteria" id="job_sort"
						class="form-control col-sm-6">
						<option value="newest" selected="selected">Sort by Newest</option>
						<option value="oldest">Sort by Oldest</option>
						<option value="nameaz">Sort by Title A-Z</option>
						<option value="nameza">Sort by Title Z-A</option>
						<option value="locaz">Sort by Location A-Z</option>
						<option value="locza">Sort by Location Z-A</option>
					</select>
				</div>
			</form>

			<!-- Start of Actual section for having JOBS info.  -->
			<div class="clearfix"></div>
			<div class="col-sm-12">
				<h3 class="pull-left">Job Results</h3>
				<jsp:include page="../common/pager.jsp"></jsp:include>
				<div class="jobs">
					<!--  Sample-->

					<!-- Sample end -->
				</div>
				<jsp:include page="../common/pager.jsp"></jsp:include>
				<div class="clearfix"></div>
			</div>
		</div>

	</div>
</section>


<!-- ============ End of new Section. ============ -->

<!-- ====== Public Job ======== -->

<jsp:include page="../job/popup-public-job.jsp"></jsp:include>


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
        //alert("Data:" + data + " pager:" + pagerData);
        fetchJobDataModel("${contextPath}/jobseeker/job/getappliedjobs", data + '&' + pagerData);

    })(jQuery);

    // =============== End of OnLoad document.  ====================
    $('#job_sort').on('change', function(e) {
        var form = $(this);
        var searchform = $('form#search-form');
        var url = searchform.attr("action");
        console.log(url);
        var data = searchform.serialize();
        fetchJobDataModel("${contextPath}/jobseeker/job/getappliedjobs", data + '&' + pagination);
    });

    $('#job_status_filter').on('change', function(e) {
        var form = $(this);
        var searchform = $('form#search-form');
        var url = searchform.attr("action");
        console.log(url);
        var data = searchform.serialize();
        var pagerData = form.serialize();
        //alert("Data:" + data + " pager:" + pagerData);
        fetchJobDataModel("${contextPath}/jobseeker/job/getappliedjobs", data + '&' + pagination);
    });

    $('form.search-form').submit(function(event) {
        event.preventDefault();
        event.stopPropagation();
        var form = $(this);
        var searchform = $('form#search-form');
        var url = searchform.attr("action");
        var data = searchform.serialize();
        var pagerData = form.serialize();
        fetchJobDataModel("${contextPath}/jobseeker/job/getappliedjobs", data + '&' + pagerData);

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
            globalJobdata = result.jobsResult;
            //clera the 'jobs' Div.
            $('.jobs').empty();

            if (result.jobSeekerJobsDataModelList && result.jobSeekerJobsDataModelList.length > 0) {
                $.each(result.jobSeekerJobsDataModelList, function(index, jobDataModel) {
                    $('.jobs').append(getSearchResultEntry(jobDataModel));
                    var selected = $('#job_status_filter option:selected').val();
                    if (selected == "WISHLISTED") {
                    	var htmlWishlistBtn = document.getElementById('wishlist' + jobDataModel.jobRelationshipId);
                    	$(htmlWishlistBtn).find('span').html("Wishlisted");
                    	$(htmlWishlistBtn).removeClass('wishlist');
                        $(htmlWishlistBtn).addClass('unwishlist');
                        var htmlApplyBtn = document.getElementById('apply' + jobDataModel.jobRelationshipId);
                        var htmlRejectBtn = document.getElementById('reject' + jobDataModel.jobRelationshipId);
                        if(jobDataModel.jobseekerJobApplicationStatusRejected)
                        {
                            $(htmlApplyBtn).hide();
                            $(htmlRejectBtn).closest(".search-item").css("border-right", "3px solid #d9534f");
                            $(htmlRejectBtn).closest(".search-item").css("border-left", "3px solid #d9534f");
                        	$(htmlRejectBtn).find('span').html("Rejected");
                        	$(htmlRejectBtn).removeClass('reject');
                            $(htmlRejectBtn).addClass('rejected');
                        }
                    } else if (selected == "APPLIED") {
                        var htmlWishlistBtn = document.getElementById('wishlist' + jobDataModel.jobRelationshipId);
                        var htmlApplyBtn = document.getElementById('apply' + jobDataModel.jobRelationshipId);
                        $(htmlWishlistBtn).hide();
                        $(htmlApplyBtn).hide();
                    } else if (selected == "REJECTED") {
                    	if(jobDataModel.jobWishlistedByUser)
                    	{
                    		var htmlWishlistBtn = document.getElementById('wishlist' + jobDataModel.jobRelationshipId);
                    		$(htmlWishlistBtn).closest(".search-item").css("border-right", "3px solid #14b1bb");
                            $(htmlWishlistBtn).closest(".search-item").css("border-left", "3px solid #14b1bb");
                    		$(htmlWishlistBtn).find('span').html("Wishlisted");
                        	$(htmlWishlistBtn).removeClass('wishlist');
                            $(htmlWishlistBtn).addClass('unwishlist');
                    	}
                    	var htmlApplyBtn = document.getElementById('apply' + jobDataModel.jobRelationshipId);
                        $(htmlApplyBtn).hide();
                        var htmlRejectBtn = document.getElementById('reject' + jobDataModel.jobRelationshipId);
                        $(htmlRejectBtn).hide();
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
        console.log(jobDataModel);
        var isFallback=jobDataModel.fallback?'hidden':'';
        return '<div class="row" id="search-result-panel"><div class="col-sm-12"><div class="search-items"><div class="search-item"><div class="row"><div style="float: none; width: 100%" class="row"><div class="pull-left col-sm-5 col-xs-12 "><div class="pull-left col-sm-12 col-md-8 col-sm-12"><p><i class="fa fa-chevron-circle-right pull-left"></i></p><h5>'
                + jobDataModel.jobTitle
                + '</h5><i class="fa fa-building-o"></i>'
                + jobDataModel.jobOrganisationName
                + '</p><p><i class="fa fa-location-arrow"></i>'
                + jobDataModel.jobLocation
                + '</p></div></div>'
                + '<div class="pull-left col-sm-3 col-xs-10"><div class="progress ' + isFallback + '" style="margin-top: 5px; margin-bottom: 0px;">'
                + '<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="'
                + jobDataModel.percentageMatch
                + '" aria-valuemin="0" aria-valuemax="100" style="width: '
                + jobDataModel.percentageMatch
                + '%;">'
                + jobDataModel.percentageMatch
                + '% Match</div></div></div>'
                +'<div class="pull-right text-center col-sm-4 col-xs-12 search-item-action"style="color: #000;"><form class="search-item-form"><input type="hidden" name="jobId" value="'+jobDataModel.jobId+'" /></form><a class="btn btn-link primary-link link-public-job" title="View"><i class="fa fa-eye"></i><span class=" text-capitalize"> View</span></a><button type="button" class="btn btn-link info-link wishlist" title="addToWishlist" id="wishlist'+jobDataModel.jobRelationshipId+'"><i class="fa fa-heart"></i><span class="text-capitalize">Wishlist</span></button><button type="button" class="btn btn-link success-link applyJob"title="Apply" link_to_site="'+jobDataModel.link_to_site+'" id="apply'+jobDataModel.jobRelationshipId+'"><i class="fa fa-check"></i><span class="text-capitalize">Apply For JOB</span></button><button type="button" class="btn btn-link danger-link reject" title="reject Job" id="reject'+jobDataModel.jobRelationshipId+'"><i class="fa fa-remove"></i><span class="text-capitalize">Reject Job</span></button></div></div></div><div class="clearfix"></div></div></div></div>';
    }

    // ==============   JAVA SCRIPT COMMON FUNCTIONS START============================================

    //redirecting to JobPost pag for editing the JOB.
    $('.jobs').delegate('.edit', 'click', function() {
        location.href = "${contextPath}/recruiter/job/edit/" + this.value;
    });

    //Apply the JOB for the Candidate 
    $('.jobs').on('click', '.applyJob', function(e) {
        var view = this;
        startLoader();
        //update status
        updateApplicationStatus("apply", view).done(function(result) {
            if (result) {
                if (result.errorMessage) {
                    toast(result.errorMessage);
                } else {
                	$(view).closest(".search-item").css("border-right", "3px solid #6ecf26");
                    $(view).closest(".search-item").css("border-left", "3px solid #6ecf26");
                    $(view).find('span').html("Applied");
                    hideWishlisted($, view);
                    hideRejected($, view);
                    $(view).removeClass('applyJob');
                    $(view).addClass('jobapplied');
                    toast(result.successMessage);
                    if($(view).attr('link_to_site')!=null && $(view).attr('link_to_site')!="")
                    window.open($(view).attr('link_to_site'), '_blank');
                }
            } else {
                toast(errorMessage);
            }
        }).fail(function(error) {
            toast(errorMessage);
        }).complete(function(result) {
            stopLoader();
        });
    });

    //Wishlist the candidate 
    $('.jobs').on('click', '.wishlist', function(e) {
        var view = this;
        startLoader();
        //update status
        updateApplicationStatus("wishlist", view).done(function(result) {
            if (result) {
                if (result.errorMessage) {
                    toast(result.errorMessage);
                } else {
                    $(view).closest(".search-item").css("border-right", "3px solid #14b1bb");
                    $(view).closest(".search-item").css("border-left", "3px solid #14b1bb");
                    $(view).find('span').html("Wishlisted");
                    $(view).removeClass('wishlist');
                    $(view).addClass('unwishlist');
                    toast(result.successMessage);
                }
            } else {
                toast(errorMessage);
            }
        }).fail(function(error) {
            toast(errorMessage);
        }).complete(function(result) {
            stopLoader();
        });
    });

    //un-wishlist
    $('.jobs').on('click', '.unwishlist', function(e) {
        var view = this;
        startLoader();
        var errorMessage = "Remove from wishlist operation failed.";
        //update status
        updateApplicationStatus("unwishlist", view).done(function(result) {
            if (result) {
                if (result.errorMessage) {
                    toast(result.errorMessage);
                } else {
                    $(view).closest(".search-item").css("border-right", "0px");
                    $(view).closest(".search-item").css("border-left", "0px");
                    $(view).find('span').html("Wishlist");
                    $(view).removeClass('unwishlist');
                    $(view).addClass('wishlist');
                    toast(result.successMessage);
                }
            } else {
                toast(errorMessage);
            }
        }).fail(function(error) {
            toast(errorMessage);
        }).complete(function(result) {
            stopLoader();
        });

    });

    //un-wishlist
    $('.jobs').on('click', '.reject', function(e) {
        var view = this;
        startLoader();
        var errorMessage = "Failed to Remove JOB from the dashboard.";
        //update status
        updateApplicationStatus("reject", view).done(function(result) {
            if (result) {
                if (result.errorMessage) {
                    toast(result.errorMessage);
                } else {
                	$(view).closest(".search-item").css("border-right", "3px solid #d9534f");
                    $(view).closest(".search-item").css("border-left", "3px solid #d9534f");
                    $(view).find('span').html("Rejected");
                    hideApplied($, view);
                    hideWishlisted($, view);
                    $(view).removeClass('reject');
                    $(view).addClass('rejected');
                    toast(result.successMessage);
                }
            } else {
                toast(errorMessage);
            }
        }).fail(function(error) {
            toast(errorMessage);
        }).complete(function(result) {
            stopLoader();
        });

    });

    //search-actions, get the required data from form serailization.
    function getSearchActionFormData(view) {
        var form = $(view).closest('.search-item-action').find('form.search-item-form');
        var formData = $(form).serialize();
        //alert();
        return formData;
    }

    function updateApplicationStatus(action, view) {
        return $.ajax({
            url : "${contextPath}/jobseeker/job/" + action,
            type : "POST",
            data : getSearchActionFormData(view),
            xhrFields : {
                withCredentials : true
            }
        });
    }
	
    function hideApplied($, e) {
        $(e).parent().find('.applyJob').hide();

    }

    function hideRejected($, e) {
        $(e).parent().find('.reject').hide();
    }

    function hideWishlisted($, e) {
        $(e).parent().find('.wishlist').hide();
        $(e).parent().find('.unwishlist').hide();

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

    //view job

    //view profile
    $(".jobs").on('click', '.link-public-job', function(e) {
        //var jsId = $(this).closest('.search-item-action').find('.search-item-form input[name="jobseekerId"]').val();
        var form = $(this).closest('.search-item-action').find('.search-item-form');
        getJobDataModel(form);
        $("#public-job").fadeIn(300);
        $("body").addClass("no-scroll");
    });

    $("#public-job .close").click(function() {
        console.log("close called");
        $("#public-job").fadeOut(300);
        $("body").removeClass("no-scroll");
        clearJobDetailPopup();
    });

    // ============COMMON JS END=============
</script>

<!--  ===============  JAVA Script Sections ========================== -->

<div class="clearfix"></div>