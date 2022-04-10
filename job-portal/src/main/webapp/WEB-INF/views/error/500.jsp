<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<h1>Internal Server Error</h1>
<p>Application has encountered an error. Please contact support on
	support@evenrank.com.</p>


<!--
    Failed URL: ${url}
    Exception:  ${exception.message}
        <c:forEach items="${exception.stackTrace}" var="ste">    
        	${ste} 
    	</c:forEach>
    -->