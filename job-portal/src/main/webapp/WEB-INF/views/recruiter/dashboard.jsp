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
				<h2 class="margin-0">Dashboard</h2>
			</div>
		</div>
		<div class="row">

			<div class="col-xs-12 col-sm-4 col-lg-4" id="created-job"
				onclick="document.location='${contextPath}/recruiter/job/post-job'">
				<div class="dashModule margin-top-25 ">
					<div class="row margin-top-25 text-center">
						<i class="fa fa-file fa-fw fa-5x"></i>
						<h4>Create New Job</h4>
						<a href="#" data-toggle="tooltip"
							title="Hire
							by posting jobs">Hire by posting jobs</a>
					</div>
				</div>
			</div>


			<div class="col-xs-12 col-sm-4 col-lg-4"
				onclick="document.location='${contextPath}/recruiter/job/viewJob'">
				<div class="dashModule margin-top-25 ">
					<div class="row margin-top-25 text-center">
						<i class="fa fa-th-list fa-fw fa-5x"></i>
						<h4>My Jobs</h4>
						<a href="#" data-toggle="tooltip" title="Check your posted jobs">Check
							your posted jobs</a>
					</div>
				</div>
			</div>
			<div class="col-xs-12 col-sm-4 col-lg-4"
				onclick="document.location='${contextPath}/recruiter/candidate/search'">
				<div class="dashModule margin-top-25 ">
					<div class="row margin-top-25 text-center">
						<i class="fa fa-users fa-fw fa-5x"></i>
						<h4>My Candidates</h4>
						<a href="#" data-toggle="tooltip" title="Check your posted jobs">Check
							your short-listed candidates</a>
					</div>
				</div>
			</div>
			<div class="col-xs-12 col-sm-4 col-lg-4 "
				onclick="document.location='${contextPath}/recruiter/search'">
				<div class="dashModule margin-top-25 ">
					<div class="row margin-top-25 text-center">
						<i class="fa fa-search fa-fw fa-5x"></i>
						<h4>Search Resumes</h4>
						<a href="#" data-toggle="tooltip" title="Search
							resumes ">Search
							resumes for your posted jobs</a>
					</div>
				</div>
			</div>
			<div class="col-xs-12 col-sm-4 col-lg-4 "
				onclick="document.location='${contextPath}/account'">
				<div class="dashModule margin-top-25 ">
					<div class="row margin-top-25 text-center">
						<i class="fa fa-cogs fa-fw fa-5x"></i>
						<h4>Settings</h4>
						<a href="#" data-toggle="tooltip" title="Manage your account">Manage
							your account</a>
					</div>
				</div>
			</div>

			<div class="col-xs-12 col-sm-4 col-lg-4"
				onclick="document.location='${contextPath}/recruiter/profile'">
				<div class="dashModule margin-top-25 ">
					<div class="row margin-top-25 text-center">
						<i class="fa fa-user fa-fw fa-5x"></i>
						<h4>My Profile</h4>
						<a href="#" data-toggle="tooltip" title="Manage your profile">Manage
							your profile</a>
					</div>
				</div>
			</div>

		</div>
	</div>
</section>



<!-- ============ FOOTER START ============ -->

<jsp:include page="../common/footer.jsp"></jsp:include>

<!-- ============ FOOTER END ============ -->