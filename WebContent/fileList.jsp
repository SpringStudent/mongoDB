<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	String path = request.getContextPath() + "/"; 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<form >
		<c:if test="${empty gridFSDBFileList}">
           		 <div align="center"><h1><b><font color="red">无文件!</font></b></h1></div>
        </c:if>

		<c:if test="${!empty gridFSDBFileList}">
			<p><a href="<%=path %>downLoadZipServlet?id=${o.id}">全部打包下载</a></p>
			<c:forEach items="${gridFSDBFileList}" var="o">
				<img src="<%=path %>fileServlet?method=downloadFile&id=${o.id }" width="109" height="87" />
                <br/><a href="<%=path %>fileServlet?method=downloadFile&id=${o.id}">下载</a>&nbsp;&nbsp;<a href="<%=path %>fileServlet?method=delete&id=${o.id}">删除</a>
			</c:forEach>
		</c:if>
	</form>
</body>
</html>