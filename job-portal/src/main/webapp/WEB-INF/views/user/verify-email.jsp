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
			<div class="col-sm-12 col-sm-offset-0 ">
				<h2 class="pull-left">Verify Your Email</h2>
			</div>
		</div>
		<c:if test="${not empty errorMessage}">
			<div
				class="col-sm-10 col-sm-offset-1 alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				${errorMessage}
			</div>

		</c:if>
		<c:if test="${not empty successMessage}">
			<div
				class="col-sm-10 col-sm-offset-1 alert alert-success alert-dismissable">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				${successMessage}
			</div>

		</c:if>
		<c:if test="${not empty verifyMessage}">
			<div class="col-sm-10 col-sm-offset-1 alert alert-warning">
				<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
				${verifyMessage}
			</div>

		</c:if>
		<div class="row">
			<div class="col-sm-12 ">
				<br>
			</div>

		</div>

	</div>
</section>



<!-- ============ FOOTER START ============ -->

<jsp:include page="../common/footer.jsp"></jsp:include>


