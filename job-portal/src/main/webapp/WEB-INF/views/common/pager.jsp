<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<div class="pull-right">
<ul class="pager ">
	<li class="next pull-right hidden">
		<form class="search-form next-page" action="#">
			<input type="hidden" name="pagination.pageNumber" value="0" /> <input
				type="hidden" name="pagination.paginationAction" value="NEXT" />
			<button type="submit" class="">Next »</button>
		</form>

	</li>
	<li class="previous pull-right hidden">
		<form class="search-form pre-page" action="#">
			<input type="hidden" name="pagination.pageNumber" value="0" /> <input
				type="hidden" name="pagination.paginationAction" value="PREV" />
			<button type="submit" class="">« Prev</button>
		</form>
	</li>

</ul>
</div>
<div class="clearfix"></div>

