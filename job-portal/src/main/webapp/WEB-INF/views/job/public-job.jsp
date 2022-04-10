<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<style type="text/css">
#job-content {
	padding: 10px;
	background: #fff;
	overflow: auto;
	border-radius: 0 0 5px 5px;
}

#job-content section {
	padding: 0;
}

#job-content .job-detail-item-head {
	font-size: 18px;
	line-height: 1;
	margin-top: 6px;
	margin-right: 20px;
	min-width: 200px;
	font-weight: bold;
}

#job-content .job-detail-item-head-text {
	font-size: 17px;
	line-height: 1;
	margin-top: 6px;
	/*font-weight: bold;*/
}

#job-content .item {
	padding: 3px;
}
</style>
<div id="job-content">

	<!-- ============ UserJobExperience START ============ -->
	<section>
		<div class="col-sm-12">
			<div class="col-sm-12 col-sm-offset-0 item hidden">
				<div class="pull-left job-detail-item-head col-sm-3 col-sx-12">
					<span>Job Title</span>
				</div>
				<div class="pull-left job-detail-item-head-text col-sm-8 job-title">
					<span></span>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="clearfix"></div>
			<div class="col-sm-12 col-sm-offset-0 item hidden">
				<div class="pull-left job-detail-item-head col-sm-3 col-sx-12">
					<span>Company </span>
				</div>
				<div
					class="pull-left job-detail-item-head-text col-sm-8 job-company">
					<span></span>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="clearfix"></div>
			<div class="col-sm-12 col-sm-offset-0 item hidden">
				<div class="pull-left job-detail-item-head col-sm-3 col-sx-12">
					<span>Industry Type </span>
				</div>
				<div
					class="pull-left job-detail-item-head-text col-sm-8 job-industry-type">
					<span></span>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="clearfix"></div>
			<div class="col-sm-12 col-sm-offset-0 item hidden">
				<div class="pull-left job-detail-item-head col-sm-3 col-sx-12">
					<span>Job Function </span>
				</div>
				<div
					class="pull-left job-detail-item-head-text col-sm-8 job-function-name">
					<span></span>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="clearfix"></div>
			<div class="col-sm-12 col-sm-offset-0 item hidden">
				<div class="pull-left job-detail-item-head col-sm-3 col-sx-12">
					<span>Employment Type </span>
				</div>
				<div
					class="pull-left job-detail-item-head-text col-sm-8 job-employment-type">
					<span></span>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="clearfix"></div>
			<div class="col-sm-12 col-sm-offset-0 item hidden">
				<div class="pull-left job-detail-item-head col-sm-3 col-sx-12">
					<span>Skills </span>
				</div>
				<div class="pull-left job-detail-item-head-text col-sm-8 job-skills"></div>
				<div class="clearfix"></div>
			</div>
			<div class="clearfix"></div>
			<div class="col-sm-12 col-sm-offset-0 item hidden">
				<div class="pull-left job-detail-item-head col-sm-3 col-sx-12">
					<span>Experience </span>
				</div>
				<div
					class="pull-left job-detail-item-head-text col-sm-8 job-experience">
					<span></span>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="clearfix"></div>
			<div class="col-sm-12 col-sm-offset-0 item hidden">
				<div class="pull-left job-detail-item-head col-sm-3 col-sx-12">
					<span>Location </span>
				</div>
				<div
					class="pull-left job-detail-item-head-text col-sm-8 job-location">
					<span></span>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="clearfix"></div>
			<div class="col-sm-12 col-sm-offset-0 item hidden">
				<div class="pull-left job-detail-item-head col-sm-3 col-sx-12">
					<span>Description </span>
				</div>
				<div
					class="pull-left job-detail-item-head-text col-sm-8 job-description">
					<span></span>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="clearfix"></div>
			<br /> <br />
		</div>

	</section>
	<!-- =================USERJOBEXPERIENCE END============ -->
</div>

<script>
	function getJobDataModel(form) {
		console.log(form)
		var data = $(form).serializeObject();
		console.log(data)
		startLoader();
		$.ajax({
			url : "${contextPath}/jobseeker/job/" + data.jobId,
			type : "POST",
			data : {},
			xhrFields : {
				withCredentials : true
			}
		}).done(function(result) {
			if (result.status == "success") {
				populateJobDataInPopup(result.jobDataModel);
				console.log(result);
			} else {
				toast('No Job is found  !!!');
				console.log("error");
				console.log(result);
			}
		}).fail(function(result) {
			toast('No Job is found  !!!');
			console.log("error");
			console.log(result);
		}).complete(function() {
			stopLoader();
		});
	}

	function populateJobDataInPopup(jobData) {

		if (jobData.title != null && jobData.title != "") {
			$('#job-content .job-title').parent().removeClass('hidden');
		}
		if (jobData.organisationName != null && jobData.organisationName != "") {
			$('#job-content .job-company').parent().removeClass('hidden');
		}
		if (jobData.industryName != null && jobData.industryName != "") {
			$('#job-content .job-industry-type').parent().removeClass('hidden');
		}
		if (jobData.jobFunction != null && jobData.jobFunction != "") {
			$('#job-content .job-function-name').parent().removeClass('hidden');
		}
		if (jobData.employmentType != null && jobData.employmentType != "") {
			$('#job-content .job-employment-type').parent().removeClass(
					'hidden');
		}
		if (jobData.skills != null && jobData.skills != "") {
			$('#job-content .job-skills').parent().removeClass('hidden');
		}
		if (jobData.jobExperience != null && jobData.jobExperience != "") {
			$('#job-content .job-experience').parent().removeClass('hidden');
		}
		if (jobData.location != null && jobData.location != "") {
			$('#job-content .job-location').parent().removeClass('hidden');
		}
		if (jobData.jobDescription != null && jobData.jobDescription != "") {
			$('#job-content .job-description').parent().removeClass('hidden');
		}

		$('#job-content .job-title span').html(jobData.title);
		$('#job-content .job-company span').html(jobData.organisationName);
		$('#job-content .job-industry-type span').html(jobData.industryName);
		$('#job-content .job-function-name span').html(jobData.jobFunction);
		$('#job-content .job-employment-type span')
				.html(jobData.employmentType);
		if (jobData.skills) {
			skills = jobData.skills.split(",")
			$.each(skills, function(index, skill) {
				$('#job-content .job-skills').append(
						"<span class='badge'>" + skill
								+ "</span><span> </span> ");
			});
		}
		var experience;
		if (jobData.jobExperience > 1) {
			experience = jobData.jobExperience + " years";
		} else {
			experience = jobData.jobExperience * 12 + " months";
		}
		$('#job-content .job-experience span').html(experience);
		$('#job-content .job-location span').html(jobData.location);
		$('#job-content .job-description span').html(jobData.jobDescription);
	}

	function clearJobDetailPopup() {
		$('#job-content .job-title span').html("");
		$('#job-content .job-company span').html("");
		$('#job-content .job-industry-type span').html("");
		$('#job-content .job-function-name span').html("");
		$('#job-content .job-employment-type span').html("");
		$('#job-content .job-skills').html("");
		$('#job-content .job-experience span').html("");
		$('#job-content .job-location span').html("");
		$('#job-content .job-description span').html("");
	}
</script>