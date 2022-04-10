<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<title>EvenRank - About Us</title>
<jsp:include page="../common/head.jsp"></jsp:include>

<section>

	<div class="container">
		<div class="row">
			<div class="col-sm-12 col-sm-offset-0 ">
				<h2 class="pull-left margin-0">About Us</h2>
			</div>
		</div>
		<div class="row">
			<br />
			<div class="col-sm-12 ">
				<p>
					<b>EvenRank</b> introduces an entirely new way for you to find the
					best career opportunities. For the first time, you can now see
					opportunities from multiple sources in a single place. We don't
					stop here. <b><i>Our Intelligence Engine</i></b> matches your
					profile against relevant opportunities and generates an EvenScore
					for each of these, giving you a rank-ordered list of jobs you
					should be considering for your next move.
				</p>
				<p>We deploy a combination of advanced statistical techniques
					and data science methods, an infinitely scalable cloud platform and
					rapid iterative machine learning techniques to match the
					requirement to your professional, personal and social profile to
					generate a relevancy score.</p>
				<p>What does this mean? It means we've taken care of the
					technology so you can focus on the most important task at
					hand-advancing your career.</p>
				<p>EvenRank is based in Bangalore, India and has an exceptional
					team of Algorithm Coders, Data Scientists and Technologists with a
					singular focus to create the best Recruitment Technology the world
					has ever seen and experienced. Our core team comprises former
					McKinsey consultants with a passion for Recruitment, Tech Heads
					from Amazon and top-ranking code chef Hackers.</p>

				<p>
					<a href="${contextPath}/about/engine">Our Intelligence Engine</a><br />
					<a href="${contextPath}/about/tnc">Terms of Use</a><br /> 
<!-- 					<a -->
<%-- 						href="${contextPath}/about/privacy">Privacy</a> --%>
				</p>
			</div>
		</div>
	</div>
</section>
<jsp:include page="../common/footer.jsp"></jsp:include>