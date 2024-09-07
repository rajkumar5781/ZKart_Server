<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="BulkProductUpdate" method="post"
		enctype="multipart/form-data">
		<p>Upload a Excel file to add products:</p>
		<input type="file" name="csvFile" accept=".csv">
		<button type="submit">Upload</button>
	</form>
</body>
</html>