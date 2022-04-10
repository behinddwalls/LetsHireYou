<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<style type="text/css">
div#slider {
	position: initial !important;
}
</style>

<div id="slider" class="sl-slider-wrapper">

	<div class="sl-slider">

		<div class="sl-slide" data-orientation="horizontal"
			data-slice1-rotation="3" data-slice2-rotation="3"
			data-slice1-scale="2" data-slice2-scale="1">
			<div class="sl-slide-inner">
				<div class="bg-img bg-img-3"></div>
				<div class="tint"></div>
				<div class="slide-content">
					<!-- 					<h2>Evolving your career?</h2> -->
					<h2>Employ Intelligence</h2>
					<p>
						<a href="${contextPath}/register/request/demo"
							class="btn btn-lg btn-default">Request Demo</a>
					</p>
				</div>
			</div>
		</div>


	</div>

	<nav id="nav-arrows" class="nav-arrows hidden">
		<span class="nav-arrow-prev">Previous</span> <span
			class="nav-arrow-next">Next</span>
	</nav>

	<nav id="nav-dots" class="nav-dots hidden">
		<span class="nav-dot-current"></span> <span></span> <span></span> <span></span>
	</nav>

</div>

<%-- DIVIDER --%>

<!-- div class="video-container  hidden-xs">
	<div id="home-video-wrapper">
		<video style="position: relative; height: 100%; width: 100%;"
			preload="auto" autoplay="autoplay" loop="loop">
			<source id="home-video-mp4"
				src="<%=request.getContextPath()%>/resources/video/home_vid_cropped.mp4"
				type="video/mp4">
		</video>
	</div>
	<div class="animated fadeInUp video-overlay homepage-hero-overlay">
		<div class="container">
			<div class="row">
				<div class="col-md-9">
					<h2>Employ Intelligence.</h2>
				</div>
				<div class="col-md-3">
					<a class="animated fadeInUp btn btn-primary" href="#"> Request
						a demo</a>
				</div>
			</div>
		</div>
	</div>
</div>


<div class="container hidden-md hidden-lg hidden-sm">
	<img
		src="<%=request.getContextPath()%>/resources/images/mobile_home_img.jpg"
		height="100" width="100%" />
</div-->
