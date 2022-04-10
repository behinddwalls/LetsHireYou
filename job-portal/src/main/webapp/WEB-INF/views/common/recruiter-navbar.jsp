<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!-- ============ NAVBAR START ============ -->

<nav class="navbar navbar-default navbar-fixed-top"
	style="min-height: 65px;">
	<div class="container">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle navbar-inverse collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class="sr-only">Toggle navigation</span> <i
					class="fa fa-bars fa-lg"></i>
			</button>
			<a class="navbar-brand" href="${contextPath}/"><span>EvenRank</span></a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav navbar-right">
				<li><a href="${contextPath}/">Dashboard</a></li>
				<li><a href="${contextPath}/recruiter/profile">My Profile</a></li>
				<li class="hidden-sm hidden-md hidden-lg"><a
					href="${contextPath}/account">Account Settings</a></li>
				<li class="hidden-sm hidden-md hidden-lg"><a
					href="${contextPath}/signout">Signout</a></li>
				<li class="dropdown hidden-xs"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown" role="button"
					aria-haspopup="true" aria-expanded="false"><i
						class="fa fa-cog fa-lg"></i> <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="${contextPath}/recruiter/job/post-job">Post
								Job</a></li>
						<li><a href="${contextPath}/recruiter/search">Search
								Resume</a></li>
						<li><a href="${contextPath}/recruiter/job/viewJob">My
								Jobs</a></li>
						<li role="separator" class="divider"></li>
						<li><a href="${contextPath}/account">Account Settings</a></li>
						<li role="separator" class="divider"></li>
						<li><a href="${contextPath}/signout">Signout</a></li>
					</ul></li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container-fluid -->
</nav>

<!-- ============ NAVBAR END ============ -->