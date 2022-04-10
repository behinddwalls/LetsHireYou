<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<script	src="<%=request.getContextPath()%>/resources/js/CommonJavaScriptUtils.js"></script>
<script type="text/javascript">
    var bloodHoundOrganisation = getBloodHoundObject("${contextPath}/organisation/search?orgName=");
    var bloodHoundDegree = getPreFetchBloodHoundObject("${contextPath}/jobseeker/profile/Degree/viewAll")
    var bloodHoundVolunteerCauses = getPreFetchBloodHoundObject("${contextPath}/jobseeker/volunteer/causes/viewAll")
    var bloodHoundPatentOffice = getPreFetchBloodHoundObject("${contextPath}/jobseeker/patent/patentOffice/viewAll")
</script>
