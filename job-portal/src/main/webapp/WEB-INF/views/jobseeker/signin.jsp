<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<jsp:include page="../common/head.jsp"></jsp:include>
<section>
	<div class="clearfix"></div>

	<div class="col-sm-12 col-xs-12 col-md-6 col-md-offset-3 auth-form">
		<h2 class="col-sm-12">Jobseeker Signin</h2>
		<jsp:include page="../common/user-login-form.jsp"></jsp:include>
	</div>

	<div class="clearfix"></div>
</section>
<!-- ============ FOOTER START ============ -->

<jsp:include page="../common/footer.jsp"></jsp:include>

<!-- ============ FOOTER END ============ -->