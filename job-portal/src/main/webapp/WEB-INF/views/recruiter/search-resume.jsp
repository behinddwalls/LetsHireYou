<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<jsp:include page="../common/head.jsp"></jsp:include>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<style type="text/css">
#skills .badge {
	margin: 2px !important;
}

#search-result-panel {
	display: none;
}

#job-panel, #filter-panel {
	border-bottom: 1px solid rgb(229, 225, 225);
	overflow: auto;
}

.search-job-fields span.badge {
	font-size: 12px !important;
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
</style>


<section>
	<div class="container">
		<div class="row">
			<div class="col-sm-12 col-sm-offset-0 ">
				<h2 class="pull-left">Search Candidates</h2>
				<a href="${contextPath}/recruiter/dashboard" class="pull-right"><i
					class="fa fa-arrow-left"></i> Go back to Dashboard</a>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<button type="button" class="btn btn-primary"
					id="search-panel-button" title="Search panel">
					<i class="fa fa-search "><span class="hidden-xs">&nbsp;Search
					</span></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-arrow-circle-down"></i>
				</button>
				&nbsp;&nbsp;&nbsp;
				<button type="button" class="btn btn-warning hidden"
					style="display: none" id="filter-panel-button" title="Filter panel">
					<i class="fa fa-filter"> <span class="hidden-xs">&nbsp;Filter
					</span>
					</i>&nbsp;&nbsp;&nbsp;<i class="fa fa-arrow-circle-right"></i>
				</button>
			</div>
		</div>
		<br class="hidden-xs" />
		<div class="row">
			<form action="${contextPath}/recruiter/search" method="post"
				id="search-form" class="search-form">

				<div id="search-panel" class="col-sm-12">
					<div id="job-panel">

						<div
							class="search-button form-group text-center col-sm-12 hidden-sm hidden-md hidden-lg hidden">
							<label for="search-button"></label>
							<button type="submit"
								class=" form-control btn btn-warning text-center">Search</button>
						</div>

						<div class="form-group col-sm-12">
							<label for="job-id">Select Job</label> <select
								class="form-control col-sm-6" id="job-id" required name="jobId">
								<c:if test="${not empty jobId}">
									<option value="" label="--- Select Job ---" />
								</c:if>
								<c:if test="${empty jobId}">
									<option value="" label="--- Select Job ---" selected="selected" />
								</c:if>
								<c:forEach items="${jobMap}" var="job">
									<c:if test="${jobId eq job.key}">
										<option selected="selected" value="${job.key}">${job.value}</option>
									</c:if>
									<c:if test="${jobId ne job.key}">
										<option value="${job.key}">${job.value}</option>
									</c:if>
								</c:forEach>
							</select>
						</div>

						<div class="form-group col-sm-6 hidden" id="job-title">
							<label for="job-title">Job Title</label>
							<div id="job-title-text" class="search-job-fields">
								<span></span>
							</div>
						</div>
						<div class="form-group col-sm-6 hidden" id="location">
							<label for="location">Location</label>
							<div id="location-text" class="search-job-fields">
								<span></span>
							</div>
						</div>
						<div class="form-group col-sm-6 hidden" id="organisation">
							<label for="organisation">Organisation</label>
							<div id="organisation-text" class="search-job-fields">
								<span></span>
							</div>
						</div>
						<div class="form-group col-sm-6 hidden" id="industry-name">
							<label for="industry-name">Industry Type</label>
							<div id="industry-name-text" class="search-job-fields">
								<span></span>
							</div>
						</div>
						<div class="form-group col-sm-6 hidden" id="skills">
							<label for="skills">Skills</label>
							<div id="skills-text" class="search-job-fields">
								<span class="badge">CSS</span>
							</div>
						</div>
						<div class="form-group col-sm-6 hidden" id="job-function">
							<label for="job-function">Job Function</label>
							<div id="job-function-text" class="search-job-fields">
								<span></span>
							</div>
						</div>
						<div id="search-button"
							class="search-button form-group text-center pull-right col-md-4 col-xs-12 hidden">
							<label for="search-button"></label>
							<button type="submit"
								class=" form-control btn btn-warning text-center">Search</button>
						</div>
					</div>


					<!-- filter panel -->

					<div id="filter-panel" style="display: none;" class="col-sm-12">
						<div
							class="search-button form-group text-center col-sm-12 hidden-sm hidden-md hidden-lg hidden">
							<label for="search-button"></label>
							<button type="submit"
								class=" form-control btn btn-warning text-center">Search</button>
						</div>
						<div class="form-group col-sm-6" id="years">
							<label for="industry-name">Experience in Years</label><span>
								(More than <b><span id="years-filter-lower">8</span></b> years)
							</span>
							<div id="years-filter"></div>

						</div>

						<div class="form-group col-sm-6" id="salary">
							<label for="industry-name">Salary</label> <span>(Between <b><span
									id="salary-filter-lower">8</span></b> and <b><span
									id="salary-filter-upper">8</span></b>)
							</span>
							<div id="salary-filter"></div>

						</div>
						<div
							class="search-button form-group text-center pull-right col-md-4 col-xs-12 ">
							<label for="search-button"></label>
							<button type="submit"
								class=" form-control btn btn-warning text-center">Search</button>
						</div>
					</div>

				</div>
			</form>
		</div>


		<!-- Search Result -->
		<div class="row" id="search-result-panel">
			<div class="col-sm-12">
				<h3 class="pull-left">Search Results</h3>
				<jsp:include page="../common/pager.jsp"></jsp:include>
				<div class="search-items">
					<div class="search-item">
						<div class="col-sm-12">
							<div style="float: none; width: 100%" class="row">
								<div class="pull-left col-sm-5 col-xs-12 ">
									<img height="100px" width="60px" style="margin: 0px;"
										src="http://simpleicon.com/wp-content/uploads/user-3.png"
										class="img-thumbnail hidden-xs hidden-sm pull-left">
									<div class="pull-left col-sm-12 col-md-8">
										<h5>Preetam Dwivedi</h5>
										<p>
											<i class="fa fa-chevron-circle-right"></i>Software Engineer
										</p>
										<p>
											<i class="fa fa-building-o"></i>Oracle Pvt ltd.
										</p>
										<p>
											<i class="fa fa-location-arrow"></i>Bangalore, Karnataka
										</p>
									</div>
								</div>
								<div class="pull-left col-sm-3 col-xs-10 ">
									<p class="hidden-xs">
										<i class="fa fa-inr"></i>20 lakh
									</p>
									<p class="hidden-xs">
										<i class="fa fa-trophy"></i>11 years
									</p>
									<div class="progress"
										style="margin-top: 5px; margin-bottom: 0px;">
										<div class="progress-bar progress-bar-success"
											role="progressbar" aria-valuenow="80" aria-valuemin="0"
											aria-valuemax="100" style="width: 80%">80% Match</div>
									</div>
								</div>
								<div
									class="pull-left text-center col-sm-4 col-xs-12 search-item-action"
									style="color: #000;">
									<form class="search-item-form">
										<input type="hidden" name="jobId" value="1" /> <input
											type="hidden" name="jobseekerId" value="2" />
									</form>
									<a href="button" class="btn btn-link primary-link" title="View">
										<i class="fa fa-eye"></i><span
										class="hidden-xs text-capitalize"> View</span>
									</a>
									<button type="button"
										class="btn btn-link success-link shortlist" title="Shortlist">
										<i class="fa fa-check"></i><span
											class="hidden-xs text-capitalize">Shortlist</span>
									</button>
									<button type="button" class="btn btn-link danger-link reject"
										title="Reject">
										<i class="fa fa-remove"></i><span
											class="hidden-xs text-capitalize"> Reject</span>
									</button>
									<button type="button" class="btn btn-link info-link wishlist"
										title="Whishlist">
										<i class="fa fa-heart"></i><span
											class="hidden-xs text-capitalize"> Wishlist</span>
									</button>
									<button type="button" class="btn btn-link download-link cv"
										title="CVDownload">
										<i class="fa fa-download"></i><span
											class="hidden-xs text-capitalize">Download CV</span>
									</button>
								</div>
							</div>
							<div class="clearfix"></div>
						</div>
					</div>


				</div>
				<jsp:include page="../common/pager.jsp"></jsp:include>

			</div>
		</div>


	</div>
</section>

<!-- ====== Public Profile ======== -->

<jsp:include page="../user/popup-public-profile.jsp"></jsp:include>

<!-- ============ FOOTER START ============ -->

<jsp:include page="../common/footer.jsp"></jsp:include>

<script type="text/javascript">
	(function($) {

		// Filter Sliders

		$('#years-filter').noUiSlider({
			start : [ 3 ],
			connect : "lower",
			step : 1,
			range : {
				'min' : 0,
				'max' : 15
			},
			format : wNumb({
				decimals : 0
			})

		});

		$("#years-filter").Link('lower').to($("#years-filter-lower"));

		$('#salary-filter').noUiSlider({
			start : [ 40000, 80000 ],
			connect : true,
			step : 1000,
			range : {
				'min' : 0,
				'max' : 150000
			},
			format : wNumb({
				decimals : 0,
			})
		});

		$("#salary-filter").Link('lower').to($("#salary-filter-lower"));
		$("#salary-filter").Link('upper').to($("#salary-filter-upper"));

		// ====================================================================

		$('#search-panel-button').on(
				'click',
				function(e) {
					$('#filter-panel').hide();
					$('#job-panel').fadeToggle();
					$('#filter-panel-button i:last').removeClass(
							'fa-arrow-circle-down');
					$('#filter-panel-button i:last').addClass(
							'fa-arrow-circle-right');
					$('#search-panel-button i:last').toggleClass(
							'fa-arrow-circle-right fa-arrow-circle-down', 200);
				});

		$('#filter-panel-button').on(
				'click',
				function(e) {
					$('#job-panel').hide();
					$('#filter-panel').fadeToggle();
					$('#search-panel-button i:last').removeClass(
							'fa-arrow-circle-down');
					$('#search-panel-button i:last').addClass(
							'fa-arrow-circle-right');
					$('#filter-panel-button i:last').toggleClass(
							'fa-arrow-circle-right fa-arrow-circle-down', 200);
				});

		$('#job-id').on('change', function(e) {
			populateSearchPanel(this.value);
		});

		//on change or select populate
		function populateSearchPanel(value) {

			$('.search-items').html('');
			$('#search-result-panel').hide();

			if (value) {
				startLoader();
				$.ajax({
					url : "${contextPath}/recruiter/job/" + value,
					type : "POST",
					xhrFields : {
						withCredentials : true
					}
				}).done(
						function(result) {

							var job = result.jobDataModel;
							if (job) {
								$('#job-title-text span').text(job.title);
								$('#location-text span').text(job.location);
								$('#organisation-text span').text(
										job.organisationName);
								$('#industry-name-text span').text(
										job.industryName);
								$('#job-function-text span').text(
										job.jobFunction);

								var skills = job.skills.split(",");

								$('#skills-text').html('');
								$.each(skills, function(index, skill) {
									$('#skills-text').append(
											"<span class='badge'>" + skill
													+ "</span>");
								});

								//set filters
								$('#salary-filter').val(
										[ job.minSalary, job.maxSalary ])

								$('#filter-panel-button').show();
								$('#job-title').removeClass('hidden');
								$('#location').removeClass('hidden');
								$('#organisation').removeClass('hidden');
								$('#industry-name').removeClass('hidden');
								$('#job-function').removeClass('hidden');
								$('#skills').removeClass('hidden');
								$('.search-button').removeClass('hidden');
							}

						}).fail(function(result) {
					console.log("error" + result);
				}).always(function() {
					stopLoader();
				});

			} else {
				$('#job-title').addClass('hidden');
				$('#location').addClass('hidden');
				$('#organisation').addClass('hidden');
				$('#industry-name').addClass('hidden');
				$('#skills').addClass('hidden');
				$('.search-button').addClass('hidden');
			}
		}

		//form submit for search
		$('form.search-form')
				.submit(
						function(event) {

							startLoader();
							$('.search-items').html('');
							// Stop form from submitting normally
							event.preventDefault();
							event.stopPropagation();

							var form = $(this);
							var searchform = $('form#search-form');
							var url = searchform.attr("action");
							console.log(url);
							var data = searchform.serialize();
							var pagerData = form.serialize();

							//hide next and prev
							$('.pager li.previous').addClass('hidden');
							$('.pager li.next').addClass('hidden');
							//post
							$
									.post(url, data + '&' + pagerData)
									.done(
											function(result) {
												var serachResults = result.searchCandidateResults;
												if (serachResults
														&& serachResults.length > 0) {
													console
															.log(serachResults.length);
													$
															.each(
																	serachResults,
																	function(
																			index,
																			data) {
																		$(
																				'.search-items')
																				.append(
																						getSearchResultEntry(data));
																	});
													var pagination = result.pagination;
													if (pagination
															&& (pagination.showPrevButton == true)) {
														$('.pager li.previous')
																.find(
																		'input[name="pagination.pageNumber"]')
																.val(
																		pagination.pageNumber);
														$('.pager li.previous')
																.removeClass(
																		'hidden');
													}
													if (pagination
															&& (pagination.showNextButton == true)) {
														$('.pager li.next')
																.find(
																		'input[name="pagination.pageNumber"]')
																.val(
																		pagination.pageNumber);
														$('.pager li.next')
																.removeClass(
																		'hidden');
													}
												} else {
													$('.search-items')
															.append(
																	"<span class='text-center'><h1>No Result found.</h1</span>");
												}
											})
									.fail(
											function(error) {
												$('.search-items')
														.append(
																"<span class='text-center'><h1>Search operation failed.</h1</span>");
											})
									.always(
											function(result) {
												$('#job-panel').hide();
												$('#filter-panel').hide();
												$('#search-panel-button i:last')
														.removeClass(
																'fa-arrow-circle-down');
												$('#search-panel-button i:last')
														.addClass(
																'fa-arrow-circle-right');
												$('#filter-panel-button i:last')
														.removeClass(
																'fa-arrow-circle-down');
												$('#filter-panel-button i:last')
														.addClass(
																'fa-arrow-circle-right');

												$('#search-result-panel')
														.show();
												stopLoader();
											});

						});

		//search-actions

		function getSearchActionFormData(view) {
			var form = $(view).closest('.search-item-action').find(
					'form.search-item-form');
			var formData = $(form).serializeObject();
			return formData;
		}

		function updateApplicationStatus(action, view) {
			return $
					.ajax({
						url : "${contextPath}/recruiter/jobapplicationstatus/"
								+ action,
						type : "POST",
						data : getSearchActionFormData(view),
						xhrFields : {
							withCredentials : true
						}
					});
		}
		//shortlist
		$('.search-items').on(
				'click',
				'.shortlist',
				function(e) {
					var view = this;
					startLoader();
					var errorMessage = "Shortlist candidate operation failed.";
					//update status
					updateApplicationStatus("shortlist", view).done(
							function(result) {
								if (result) {
									if (result.errorMessage) {
										toast(result.errorMessage);
									} else {
										$(view).closest(".search-item").css(
												"border-right",
												"3px solid #6ecf26");
										$(view).closest(".search-item").css(
												"border-left",
												"3px solid #6ecf26");
										$(view).find('span')
												.html("Shortlisted");
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

		//reject
		$('.search-items').on(
				'click',
				'.reject',
				function(e) {

					var view = this;
					startLoader();
					var errorMessage = "Reject candidate operation failed.";
					//update status
					updateApplicationStatus("reject", view).done(
							function(result) {
								if (result) {
									if (result.errorMessage) {
										toast(result.errorMessage);
									} else {
										$(view).closest(".search-item").css(
												"border-right",
												"3px solid #d9534f");
										$(view).closest(".search-item").css(
												"border-left",
												"3px solid #d9534f");
										$(view).find('span').html("Rejected");
										hideShortlisted($, view);
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

		//wishlist
		$('.search-items')
				.on(
						'click',
						'.wishlist',
						function(e) {

							var view = this;
							startLoader();
							var errorMessage = "Add to wishlist operation failed.";
							//update status
							updateApplicationStatus("wishlist", view)
									.done(
											function(result) {
												if (result) {
													if (result.errorMessage) {
														toast(result.errorMessage);
													} else {
														$(view)
																.closest(
																		".search-item")
																.css(
																		"border-right",
																		"3px solid #14b1bb");
														$(view)
																.closest(
																		".search-item")
																.css(
																		"border-left",
																		"3px solid #14b1bb");
														$(view)
																.find('span')
																.html(
																		"Wishlisted");
														$(view).removeClass(
																'wishlist');
														$(view).addClass(
																'unwishlist');
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
		$('.search-items')
				.on(
						'click',
						'.unwishlist',
						function(e) {

							var view = this;
							startLoader();
							var errorMessage = "Remove from wishlist operation failed.";
							//update status
							updateApplicationStatus("unwishlist", view).done(
									function(result) {
										if (result) {
											if (result.errorMessage) {
												toast(result.errorMessage);
											} else {
												$(view).closest(".search-item")
														.css("border-right",
																"0px");
												$(view).closest(".search-item")
														.css("border-left",
																"0px");
												$(view).find('span').html(
														"Wishlist");
												$(view).removeClass(
														'unwishlist');
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

		function hideShortlisted($, e) {
			$(e).parent().find('.shortlist').hide();

		}

		function hideRejected($, e) {
			$(e).parent().find('.reject').hide();
		}

		function hideWishlisted($, e) {
			$(e).parent().find('.wishlist').hide();
			$(e).parent().find('.unwishlist').hide();

		}

		function getSearchResultEntry(data) {
			var experience = null;
			if (data.experience && data.experience != null) {
				if (data.experience > 12) {
					experience = Math.floor(data.experience / 12) + " year(s)"
				} else {
					experience = data.experience + " month(s)"
				}
			}

			var searchData = {
				firstName : (data.firstName == null ? "N/A" : data.firstName),
				lastName : (data.lastName == null ? "" : data.lastName),
				profileHeadline : (data.profileHeadline == null ? "N/A"
						: data.profileHeadline),
				company : (data.company == null ? "N/A" : data.company),
				location : (data.location == null ? "N/A" : data.location),
				salary : (data.salary == null ? "N/A" : data.salary + " lakhs"),
				experience : (experience == null ? "N/A" : experience),
				isFallback : data.fallback ? "hidden" : ""
			};

			console.log(data);

			return "<div class='search-item'><div class='col-sm-12'><div style='float: none; width: 100%' class='row'><div class='pull-left col-sm-5 col-xs-12 '>"
					+ "<img height='100px' width='60px' style='margin: 0px;'src='"
					+ (data.profileImageUrl == null ? "http://simpleicon.com/wp-content/uploads/user-3.png"
							: data.profileImageUrl)
					+ "'class='img-thumbnail hidden-xs hidden-sm pull-left'>"
					+ "<div class='pull-left col-sm-12 col-md-8'><h5>"
					+ searchData.firstName
					+ " "
					+ searchData.lastName
					+ "</h5><p><i class='fa fa-chevron-circle-right'></i>"
					+ searchData.profileHeadline
					+ "</p>"
					+ "<p><i class='fa fa-building-o'></i>"
					+ searchData.company
					+ "</p><p><i class='fa fa-location-arrow'></i>"
					+ searchData.location
					+ "</p></div></div>"
					+ "<div class='pull-left col-sm-3 col-xs-10 '><!--p class='hidden-xs'><i class='fa fa-inr'></i>"
					+ searchData.salary
					+ "</p-->"
					+ "<p class='hidden-xs'><i class='fa fa-trophy'></i> "
					+ searchData.experience
					+ " </p><div class='progress " + searchData.isFallback + "' style='margin-top: 5px; margin-bottom: 0px;'>"
					+ "<div class='progress-bar progress-bar-success'role='progressbar' aria-valuenow='"
					+ data.match
					+ "' aria-valuemin='0'aria-valuemax='100' style='width: "
					+ data.match
					+ "%;'>"
					+ data.match
					+ "% Match</div></div></div>"
					+ "<div class='pull-left text-center col-sm-4 col-xs-12 search-item-action'style='color: #000;'><form class='search-item-form'>"
					+ "<input type='hidden' name='jobId' value='" + data.jobId + "' /> <input type='hidden' name='jobseekerId' value='" +data.jobseekerId + "' /></form>"
					+ "<a class='btn btn-link primary-link link-public-profile' title='View'><i class='fa fa-eye'></i><span class='text-capitalize'> View</span></a>"
					+ "<button type='button' class='btn btn-link success-link shortlist' title='Shortlist'><i class='fa fa-check'></i><span class='text-capitalize'>Shortlist</span></button>"
					+ "<button type='button' class='btn btn-link danger-link reject'title='Reject'><i class='fa fa-remove'></i><span class='text-capitalize'> Reject</span></button>"
					+ "<button type='button' class='btn btn-link info-link wishlist'title='Whishlist'><i class='fa fa-heart'></i><span class='text-capitalize'> Wishlist</span></button>"
					+ (data.userResumeLink == null ? ""
							: "<button type='button' class='btn btn-link download-link cv'title='CVDownload'><i class='fa fa-download'></i><a href="+data.userResumeLink+"><span class='text-capitalize'> Download CV</span></a></button></div></div>")
					+ "<div class='clearfix'></div></div></div>";
		}

		//view profile
		$(".search-items").on(
				'click',
				'.link-public-profile',
				function(e) {
					//var jsId = $(this).closest('.search-item-action').find('.search-item-form input[name="jobseekerId"]').val();
					var formData = $(this).closest('.search-item-action').find(
							'.search-item-form').serialize();
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

		$(document).ready(function() {
			<c:if test="${not empty jobId}">
			populateSearchPanel("${jobId}");
			$('form #search-button button').trigger('click');
			</c:if>
		});

		

	})(jQuery);
</script>

