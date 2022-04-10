<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section id="companies" style="margin-top: -30px">
	<div class="container">
		<div class="row">
			<div class="col-sm-12">
				<h2>Featured Clients</h2>
				<ul id="featured-companies" class="row">

					<li class="col-sm-4 col-md-4"><a href="#"> <img
							src="${contextPath}/resources/images/oneplus.png" alt=""
							height="100px" />
					</a></li>
					<li class="col-sm-4 col-md-4"><a href="#"> <img
							src="${contextPath}/resources/images/flexingit.png" alt=""
							height="100px" width="250px" />
					</a></li>
					<li class="col-sm-4 col-md-4"><a href="#"> <img
							src="${contextPath}/resources/images/oyo-rooms.png" alt=""
							height="100px" width="250px" />
					</a></li>

				</ul>
			</div>
		</div>
	</div>
</section>
