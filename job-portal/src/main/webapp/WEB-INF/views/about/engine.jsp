<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<title>EvenRank - Engine</title>
<jsp:include page="../common/head.jsp"></jsp:include>

<section>

	<div class="container">
		<div class="row">
			<div class="col-sm-12 col-sm-offset-0 ">
				<h2 class="pull-left margin-0">Our Intelligence Engine</h2>
			</div>
		</div>
		<div class="row">
			<br />
			<div class="col-sm-12 ">
				<h3>What is EvenScore?</h3>
				<p>EvenScore is a composite score that reflects the degree of
					match between your profile and a particular job requirement. Each
					opportunity streaming into our system generates a unique EvenScore,
					allowing us to rank and prioritize the best ones for you.  We
					consider numerous factors to generate your scores, ranging from
					your educational background, work experience and skills,
					demonstrated leadership and Awards and, your social profile, to
					name a few. More importantly, we know exactly how important each of
					these factors is, allowing us to weigh in just the right amount.</p>
				<h3>Not Just Keywords.</h3>
				<p>Keyword-based searches are simplistic. They look at job
					titles, company names, salary and experience and check your resume
					for certain elements indicated in a job description. Eve, our
					Intelligence Engine, can interpret the context of your resume and
					match it to the job requirement much more closely than traditional
					text-based matching methods. She understands that if you know HTML
					then CSS will not be a problem for you. Or, if you have worked in
					HR at a bank you can be a successful HR professional in most
					services firms.</p>
				<h3>Aspirations are important. So is potential.</h3>
				<p>
					All of us aspire to better ourselves. Whether it is a better role,
					designation, compensation or industry. Eve understands that. She
					applies herself to finding you something better. Each time. So you
					keep moving upward in your career. <b><i>Engine</i></b> can also
					determine your caliber by looking at your highest degree, your
					institutional background and your professional pedigree. She can
					also interpret your career trajectory till date and recommend a
					career change accordingly.
				</p>
				<h3>Cultural Fit</h3>
				<p>We use Big Data Analytics to understand what companies are
					looking for. We clearly understand their hiring patterns and
					drivers for their choice of candidates. This allows us to infer if
					you will be a good fit should you join this company. We care enough
					to bring together recruiters and candidates only if we know that
					they can work well together. This results in better engagement,
					lower attrition and a more fulfilling career.</p>
			</div>
		</div>
	</div>
</section>
<jsp:include page="../common/footer.jsp"></jsp:include>