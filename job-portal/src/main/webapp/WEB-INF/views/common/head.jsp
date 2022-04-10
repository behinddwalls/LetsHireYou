<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="description" content="evenrank">
<meta name="author" content="everank">
<title>EvenRank</title>
<link rel="shortcut icon"
	href="<%=request.getContextPath()%>/resources/images/favicon2.png">
<!-- Start of Sweet alert resources -->
<!-- 
LINKS:
http://www.bitrepository.com/stylish-javascript-dialog-boxes.html
http://t4t5.github.io/sweetalert/
 -->

<!-- Google Analytics -->
<!-- Google Analytics -->
<script>
    window.ga = window.ga || function() {
        (ga.q = ga.q || []).push(arguments)
    };
    ga.l = +new Date;
    ga('create', 'UA-69248864-1', 'auto');
    ga('send', 'pageview');
</script>
<script async src='//www.google-analytics.com/analytics.js'></script>
<!-- End Google Analytics -->


<script
	src="<%=request.getContextPath()%>/resources/js/sweetalert.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/sweetalert.css">
<!-- End of Sweet alert resources -->

<!-- Main Stylesheet -->
<link href="<%=request.getContextPath()%>/resources/css/style.css"
	rel="stylesheet">

<!-- HTML5 shiv and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
		<script src="<%=request.getContextPath()%>/resources/js/html5shiv.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/respond.min.js"></script>
		<![endif]-->

</head>

<!-- ============ PAGE LOADER START ============ -->

<div id="loader" style="display: none">
	<i class="fa fa-spinner fa-4x fa-spin"></i>
</div>

<!-- ============ PAGE LOADER END ============ -->

<!-- ============ NAVBAR START ============ -->

<c:choose>
	<c:when test="${isRecruiter}">
		<jsp:include page="recruiter-navbar.jsp"></jsp:include>
	</c:when>
	<c:when test="${isJobseeker}">
		<jsp:include page="jobseeker-navbar.jsp"></jsp:include>
	</c:when>
	<c:otherwise>
		<jsp:include page="navbar.jsp"></jsp:include>
	</c:otherwise>
</c:choose>

<!-- ============ NAVBAR END ============ -->


<!-- TOAST -->
<div id="toast"
	class="col-sm-3 col-xs-12 alert alert-info alert-dismissable"
	style="position: fixed; right: 5px; display: none; z-index: 20000; background-color: rgb(2, 2, 2); color: #fff;">
	<button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
	<span></span>
</div>
