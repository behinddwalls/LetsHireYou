<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<jsp:include page="../common/head.jsp"></jsp:include>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<section>
	<div class="container">
		<div class="row">
			<div class="col-sm-12">
				<h2 class="pull-left ">My Candidates</h2>
				<a href="${contextPath}/recruiter/dashboard" class="pull-right"><i
					class="fa fa-arrow-left"></i> Go back to Dashboard</a>
			</div>
		</div>
		<div class="row">
			<ul class="nav nav-tabs">
				<li role="presentation" class=""><a
					href="${contextPath}/recruiter/candidate/search">My Candidates</a></li>
				<li role="presentation" class="active"><a href="">Wishlist
						Candidates</a></li>
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



		<!--  -->
		<!-- =================== Wishlisted Candidates list would be shown here -->
		<div class="row" id="search-result-panel">
			<div class="col-sm-12">
				<h3 class="pull-left">Result</h3>
				<jsp:include page="../common/pager.jsp"></jsp:include>
				<div class="candidates"></div>
				<jsp:include page="../common/pager.jsp"></jsp:include>
				<div class="clearfix"></div>
			</div>
		</div>
	</div>
</section>

<!-- End of Candidate list. -->
<!-- Candidate profile Viewer -->
<jsp:include page="../user/popup-public-profile.jsp"></jsp:include>
<!-- ============ FOOTER START ============ -->

<jsp:include page="../common/footer.jsp"></jsp:include>
<script type="text/javascript">
    //=============== Start of OnLoad document. ====================
    (function($) {
        var data;
        fetchDataModel("${contextPath}/recruiter/candidate/wishlist/getAll", data);

    })(jQuery);

    // ==============   JAVA SCRIPT COMMON FUNCTIONS START============================================
    //This 
    function fetchDataModel(urlPath, data) {
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
            globalJobdata = result.searchCandidateResults;
            //clera the 'jobs' Div.
            $('.candidates').empty();

            if (result.searchCandidateResults && result.searchCandidateResults.length > 0) {
                console.log("success");
                console.log(result);
                var searchCandidateResults = result.searchCandidateResults;

                $.each(searchCandidateResults, function(index, jobRelationshipAndUser) {
                    $('.candidates').append(getResult(jobRelationshipAndUser));
                    if (jobRelationshipAndUser.rejected) {
                        //alert("Inde reject to hide data");
                        //find by Id the HTML obj
                        var htmlRejectBtn = document.getElementById('reject' + jobRelationshipAndUser.jobRelationshipId);
                        var htmlShortlistBtn = document.getElementById('shortlist' + jobRelationshipAndUser.jobRelationshipId);
                        $(htmlRejectBtn).hide();
                        $(htmlShortlistBtn).hide();
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
                $('.candidates').append("<span class='text-center'><h1>No Result found.</h1</span>");
            }
        }).fail(function(result) {
            toast('No Job is found  !!!');
            console.log("error" + result);
            alert("Some error encountered");
        }).complete(function() {
            stopLoader();
        });
    }

    $('form.search-form').submit(function(event) {
        event.preventDefault();
        event.stopPropagation();
        var form = $(this);
        var searchform = $('form#search-form');
        var url = searchform.attr("action");
        var data = searchform.serialize();
        var pagerData = form.serialize();
        fetchDataModel("${contextPath}/recruiter/candidate/wishlist/getAll", data + '&' + pagerData);

    });

    //function
    function getResult(jobRelationshipAndUser) {

        var experience = null;
        if (jobRelationshipAndUser.pastExperienceMonths && jobRelationshipAndUser.pastExperienceMonths != null) {
            if (jobRelationshipAndUser.pastExperienceMonths > 12) {
                experience = Math.floor(jobRelationshipAndUser.pastExperienceMonths / 12) + " year(s)"
            } else {
                experience = jobRelationshipAndUser.pastExperienceMonths + " month(s)"
            }
        }
        var searchData = {
            firstName : (jobRelationshipAndUser.firstName == null ? "N/A" : jobRelationshipAndUser.firstName),
            lastName : (jobRelationshipAndUser.lastName == null ? "" : jobRelationshipAndUser.lastName),
            profileHeadline : (jobRelationshipAndUser.profileHeadline == null ? "N/A" : jobRelationshipAndUser.profileHeadline),
            company : (jobRelationshipAndUser.company == null ? "N/A" : jobRelationshipAndUser.company),
            location : (jobRelationshipAndUser.address == null ? "N/A" : jobRelationshipAndUser.address),
            salary : (jobRelationshipAndUser.salary == null ? "N/A" : jobRelationshipAndUser.salary + " lakhs"),
            experience : (experience == null ? "N/A" : experience),
            jobTitle : jobRelationshipAndUser.jobTitle,
            jobCreateDate : jobRelationshipAndUser.jobCreateDate,
            percentage : jobRelationshipAndUser.percentageMatch
        };
        console.log(jobRelationshipAndUser);
        return '<div class="row" id="search-result-panel"><div class="col-sm-12" style="padding-top:0px;"><div class="search-items"><div class="search-item"><div class="row"><div class="row"><div class="pull-left col-sm-5 col-xs-12 "><img class="hidden" height="100px" width="60px" style="margin: 0px;"src="http://simpleicon.com/wp-content/uploads/user-3.png"class="img-thumbnail hidden-xs hidden-sm pull-left"><div class="pull-left col-sm-12 col-md-8"><h5>'
                + searchData.firstName
                + ' '
                + searchData.lastName
                + '</h5><p><i class="fa fa-chevron-circle-right"></i>'
                + searchData.profileHeadline
                + '</p><p><i class="fa fa-building-o"></i>'
                + searchData.company
                + '</p><p><i class="fa fa-location-arrow"></i>'
                + searchData.location
                + '</p></div></div><div class="pull-left col-sm-3 col-xs-10 "><p class="hidden"><i class="fa fa-inr"></i>20 lakh</p><p class="hidden-xs"><i class="fa fa-trophy"></i>'
                + searchData.experience
                + '</p>'
                + '<div class="progress" style="margin-top: 5px; margin-bottom: 0px;">'
                + '<div class="progress-bar progress-bar-success"role="progressbar" aria-valuenow="'
                + searchData.percentage
                + '" aria-valuemin="0" aria-valuemax="100" style="width: '
                + searchData.percentage
                + '%;">'
                + searchData.percentage
                + '% Match</div>'
                + '</div>'
                + '</div><div class="pull-left text-center col-sm-4 col-xs-12 search-item-action"style="color: #000;"><form class="search-item-form"><input type="hidden" name="jobId" value="'+jobRelationshipAndUser.jobId+'" /> <input type="hidden" name="jobseekerId" value="'+jobRelationshipAndUser.jobseekerId+'" /></form><button type="button"class="btn btn-link success-link shortlist" title="Shortlist" id="shortlist'+jobRelationshipAndUser.jobRelationshipId+'"><i class="fa fa-check"></i><span class="text-capitalize">Shortlist</span></button><button type="button" class="btn btn-link danger-link reject "title="Reject" id="reject'+jobRelationshipAndUser.jobRelationshipId+'"><i class="fa fa-remove reject"></i><span class="text-capitalize"> Reject</span></button><button type="button" class="btn btn-link info-link unwishlist "title="Whishlisted"><i class="fa fa-heart"></i><span class=" text-capitalize"> Wishlisted</span></button><a class="btn btn-link primary-link link-public-profile" title="View"><i class="fa fa-eye"></i><span class=" text-capitalize"> View</span></a>'
                + (jobRelationshipAndUser.userResumeLink == null ? '':'<button type="button" class="btn btn-link download-link cv" title="CVDownload"><i class="fa fa-download"></i><a href='+jobRelationshipAndUser.userResumeLink+'><span class="text-capitalize"> Download CV</span></a></button>')
                +'<div class="col-sm-12"><b>'
                + searchData.jobTitle + '-' + searchData.jobCreateDate + '</b></div></div></div>' + '<div class="clearfix"></div></div></div>';
    }

    // ==============   JAVA SCRIPT COMMON FUNCTIONS START============================================

    //search-actions, get the required data from form serailization.
    function getSearchActionFormData(view) {
        var form = $(view).closest('.search-item-action').find('form.search-item-form');
        var formData = $(form).serializeObject();
        return formData;
    }

    function updateApplicationStatus(action, view) {
        return $.ajax({
            url : "${contextPath}/recruiter/jobapplicationstatus/" + action,
            type : "POST",
            data : getSearchActionFormData(view),
            xhrFields : {
                withCredentials : true
            }
        });
    }
	
    $('.candidates').on('click', '.reject', function(e) {
        var view = this;
        e.preventDefault();
        //propt confirmation
        swal({
            title : "Are you sure?",
            text : "Candidate will be rejected for the job!",
            type : "warning",
            showCancelButton : true,
            confirmButtonColor : "#DD6B55",
            confirmButtonText : "Yes, reject it!",
            closeOnConfirm : true
        }, function() {
            rejectCandidate(view);
        });
    });

    //fucntion to reject the Short-ltested candidate.
    function rejectCandidate(view) {
        startLoader();
        var errorMessage = "Reject candidate operation failed.";
        //alert("View:"+view);
        //update status
        updateApplicationStatus("reject", view).done(function(result) {
            if (result) {
                toast("Candidate is rejected for the Job!!!");
                $(view).closest(".search-item").css("border-right", "3px solid #d9534f");
                $(view).closest(".search-item").css("border-left", "3px solid #d9534f");
                $(view).find('span').html("Rejected");
                hideShortlisted($, view);
                $(view).removeClass('reject');
                $(view).addClass('rejected');
                if (result.errorMessage) {
                    toast(result.errorMessage);
                } else {

                }
            } else {
                //toast(errorMessage);
            }
        }).fail(function(error) {
            toast(errorMessage);
        }).complete(function(result) {
            stopLoader();
        });
    }

    $('.candidates').on('click', '.wishlist', function(e) {
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
    $('.candidates').on('click', '.unwishlist', function(e) {
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
    $('.candidates').on('click', '.shortlist', function(e) {
        var view = this;
        startLoader();
        var errorMessage = "Shortlist candidate operation failed.";
        //update status
        updateApplicationStatus("shortlist", view).done(function(result) {
            if (result) {
                if (result.errorMessage) {
                    toast(result.errorMessage);
                } else {
                    $(view).closest(".search-item").css("border-right", "3px solid #6ecf26");
                    $(view).closest(".search-item").css("border-left", "3px solid #6ecf26");
                    $(view).find('span').html("Shortlisted");
                    hideWishlisted($, view);
                    hideRejected($, view);
                    $(view).removeClass('shortlist');
                    $(view).addClass('shortlisted');
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

    function hideRejected($, e) {
        $(e).parent().find('.reject').hide();
    }
    function hideShortlisted($, e) {
        $(e).parent().find('.shortlist').hide();
    }

    function hideWishlisted($, e) {
        $(e).parent().find('.wishlist').hide();
        $(e).parent().find('.unwishlist').hide();

    }
    //view profile
    $(".candidates").on('click', '.link-public-profile', function(e) {
        //var jsId = $(this).closest('.search-item-action').find('.search-item-form input[name="jobseekerId"]').val();
        var formData = $(this).closest('.search-item-action').find('.search-item-form').serialize();
        getJobseekerResumePublic(formData);
        $("#public-profile").fadeIn(300);
        $("body").addClass("no-scroll");
    });

    $("#public-profile .close").click(function() {
        console.log("close called");
        $("#public-profile").fadeOut(300);
        $("body").removeClass("no-scroll");
        hideAllProfileSections();
    });

   
    // ============COMMON JS START=============

    // supplly the entire div that needs to be removed here
    function clearHtmlDom(htmlObj) {
        $(htmlObj).detach();
    }

    // ============COMMON JS END=============
</script>
<style type="text/css">
#job-panel, #filter-panel {
	border-bottom: 1px solid rgb(229, 225, 225);
	overflow: auto;
}

.search-job-fields span.badge {
	font-size: 14px !important;
	padding: 4px 8px;
}

.search-job-fields {
	box-shadow: none;
	-webkit-box-shadow: none;
	-mozilla-box-shadow: none;
	-ms-box-shadow: none;
	display: block;
	width: 100%;
	font-size: 14px;
	font-weight: bold;
	line-height: 1.71;
	color: #888;
	background-image: none;
	border-radius: 3px;
	-webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow
		ease-in-out .15s;
	-o-transition: border-color ease-in-out .15s, box-shadow ease-in-out
		.15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
}

.candidates {
	border-top: 1px solid #e7e7e7;
	margin-bottom: 25px;
}

.candidates span {
	display: inline-block;
	padding: 16px 0 0;
}

.search-items {
	border-top: 0px;
	margin-bottom: 0px;
}

.candidates {
	border-top: 1px solid #e7e7e7;
}
</style>
