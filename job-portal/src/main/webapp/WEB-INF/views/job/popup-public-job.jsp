<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<style>
.popup-job-content {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	width: 900px;
	max-width: 100%;
	margin: 0 auto;
}
</style>
<div class="popup container" id="public-job">
	<div class="col-sm-12 popup-job-content">
		<div class="popup-header" style="margin-top: 20px;">
			<a class="close"><i class="fa fa-remove fa-lg"></i></a>
			<h2>Job Detail</h2>
		</div>
		<jsp:include page="public-job.jsp"></jsp:include>

	</div>
</div>
<script
	src="<%=request.getContextPath()%>/resources/js/CommonJavaScriptUtils.js"></script>






