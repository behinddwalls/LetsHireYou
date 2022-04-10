<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!-- ============ NAVBAR START ============ -->

<nav id="job-portal-nav-bar"
	class="navbar navbar-default navbar-fixed-top"
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
				<li><a href="${contextPath}/">Home</a></li>
				<li><a href="${contextPath}/register/request/demo">Recruiter</a></li>
				<li><a href="${contextPath}/signin/jobseeker">Jobseeker</a></li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container-fluid -->
</nav>

<!-- ============ NAVBAR END ============ -->