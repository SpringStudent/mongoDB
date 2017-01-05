<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form action="upload.jsp" method="post" id="upload">
		<input type="submit" value="上传文件">
	</form> 
	<br>
	<form action="fileServlet?method=showList" method="post" id="showList">
		<input type="submit" value="在线预览">
	</form>
	
</body>
</html>