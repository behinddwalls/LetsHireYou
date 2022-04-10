<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<style>
.popup-profile-content {
	position: absolute;
	top: 10px;
	left: 0;
	right: 0;
	max-width: 950px;
	margin: 0 auto;
	padding-bottom: 20px;
}
</style>
<div class="popup container" id="public-profile">
	<div class="col-sm-12 popup-profile-content">
		<div class="popup-header">
			<h2 class="pull-left">Profile</h2>
			<a class="close pull-right"><i class="fa fa-remove fa-lg"></i></a> <a
				class="print pull-right hidden-xs"> <i class="fa fa-print fa-2x"
				style="color: #000;text-shadow: 0 1px 0 #fff;opacity: .2;margin-right: 20px;"></i>
			</a>
			<div class="clearfix"></div>
		</div>
		<jsp:include page="public-profile.jsp"></jsp:include>

	</div>
</div>

<script
	src="<%=request.getContextPath()%>/resources/js/CommonJavaScriptUtils.js"></script>

<script type="text/javascript">
	window.onload = function() {
		$('.print').click(function() {
			var w = window.open()
			w.document.write($('#profile-content').html());
			w.print();
			w.close();
		});
	}
</script>






