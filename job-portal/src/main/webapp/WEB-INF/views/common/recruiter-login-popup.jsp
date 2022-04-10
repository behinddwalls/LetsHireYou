<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="popup" id="recruiter-login">
	<div class="authentication-form col-sm-12 col-xs-12 col-md-6 col-md-offset-3">
		<div class="popup-header">
			<a class="close"><i class="fa fa-remove fa-lg"></i></a>
			<h2>Login</h2>
		</div>
		<jsp:include page="recruiter-login-form.jsp"></jsp:include>
	</div>
</div>