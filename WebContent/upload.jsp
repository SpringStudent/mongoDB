<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<body>
	<form id="fileUpload" action="fileServlet?method=upload" method="post" enctype="multipart/form-data">
		<input type="file" name="upload"><br>
		<input type="file" name="upload"><br>
		<input type="submit" value="submit">
	</form>
</body>
</html>