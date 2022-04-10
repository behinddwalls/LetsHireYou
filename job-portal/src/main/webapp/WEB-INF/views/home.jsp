<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html>
<jsp:include page="common/head.jsp"></jsp:include>
<style type="text/css">
nav#job-portal-nav-bar {
	background-color: transparent;
}

.navbar-default .navbar-nav>li>a:hover, .navbar-default .navbar-nav>li>a:focus
	{
	color: whitesmoke !important;
}

.navbar-brand span {
	color: #14b1bb;
	font-weight: bold;
}
</style>
<body>

	<!-- ============ SLIDES START ============ -->

	<jsp:include page="common/slider.jsp"></jsp:include>

	<!-- ============ SLIDES END ============ -->

	<!-- ============ JOBS START ============ -->

	<%-- 	<jsp:include page="common/recent-jobs.jsp"></jsp:include> --%>

	<!-- ============ JOBS END ============ -->

	<!-- ============ COMPANIES START ============ -->

	<jsp:include page="common/featured-companies.jsp"></jsp:include>

	<!-- ============ COMPANIES END ============ -->

	<!-- ============ STATS START ============ -->

	<%-- jsp:include page="common/stats.jsp"></jsp:include --%>

	<!-- ============ STATS END ============ -->

	<!-- ============ HOW DOES IT WORK START ============ -->

	<!-- section id="how">
		<div class="container">
			<div class="row">
				<div class="col-sm-12">
					<h2>How does it work</h2>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<p>Curabitur et lorem a massa tempus aliquam. Aenean aliquam
						volutpat gravida. Pellentesque in neque nec tortor sagittis tempor
						quis in lectus. Vestibulum vehicula aliquet elit ut porta. Sed
						ipsum felis, interdum blandit purus sed, volutpat ultricies sem.
						Maecenas feugiat, lectus vitae luctus feugiat, nulla nisl
						dignissim velit, nec malesuada ligula orci et metus. In vulputate
						laoreet luctus.</p>
					<p>Sed hendrerit ligula tortor, eget iaculis velit vestibulum
						a. Phasellus convallis nisl pretium nisi porttitor, eu scelerisque
						mauris consectetur. Fusce pretium, dui placerat laoreet
						consectetur, nibh diam accumsan enim, sed fringilla turpis quam
						quis ex. Mauris a rhoncus tortor, a cursus urna. Sed et
						condimentum quam. Nunc dictum erat ut ante aliquam porttitor.
						Donec diam eros, bibendum et scelerisque egestas, aliquet ut
						nulla.</p>
					<p>
						<a href="about.html" class="btn btn-primary">Learn More</a>
					</p>
				</div>
				<div class="col-sm-6">
					<div class="video-wrapper">
						<iframe src="https://player.vimeo.com/video/121698707"
							allowfullscreen></iframe>
					</div>
				</div>
			</div>
		</div>
	</section-->

	<!-- ============ HOW DOES IT WORK END ============ -->

	<!-- ============ CLIENTS START ============ -->

<%-- 	<jsp:include page="common/clients.jsp"></jsp:include> --%>

	<!-- ============ CLIENTS END ============ -->

<%-- 	<jsp:include page="common/testimonials.jsp"></jsp:include> --%>

	<!-- ============ TESTIMONIALS END ============ -->

	<!-- ============ BLOG START ============ -->

	<%-- 	<jsp:include page="common/blogs.jsp"></jsp:include> --%>

	<!-- ============ BLOG END ============ -->

	<!-- ============ CONTACT START ============ -->

	<%-- 	<jsp:include page="common/contacts.jsp"></jsp:include> --%>

	<!-- ============ CONTACT END ============ -->

	<!-- ============ FOOTER START ============ -->

	<jsp:include page="common/footer.jsp"></jsp:include>

	<!-- ============ FOOTER END ============ -->

	<!-- ============ LOGIN START ============ -->

	<jsp:include page="common/user-login-popup.jsp"></jsp:include>
	<jsp:include page="common/recruiter-login-popup.jsp"></jsp:include>

	<!-- ============ LOGIN END ============ -->

	<!-- ============ REGISTER START ============ -->

	<jsp:include page="common/user-register-popup.jsp"></jsp:include>
	<jsp:include page="common/recruiter-register-popup.jsp"></jsp:include>

	<!-- ============ REGISTER END ============ -->

	<script type="text/javascript">
        $(document).ready(function() {
            $(window).scroll(function() {
                var scroll = $(window).scrollTop();
                if (scroll > 50) {
                    $("nav#job-portal-nav-bar").css("background-color", "#14b1bb");
                    $(".navbar-brand span").css("color", "#fff");
                    $(".navbar-brand span").css("font-weight", "normal");
                } else {
                    $("nav#job-portal-nav-bar").css("background-color", "transparent");
                    $(".navbar-brand span").css("color", "#14b1bb");
                    $(".navbar-brand span").css("font-weight", "bold");
                }
            });
        });
    </script>
</body>
</html>
