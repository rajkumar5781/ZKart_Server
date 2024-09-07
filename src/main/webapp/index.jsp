<%@page import="org.apache.catalina.connector.Response"%>
<%@page import="org.apache.coyote.Request"%>
<%@page import="servlets.ClientActions"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" info="This is first jsp page" errorPage="error.jsp" %>
    <%
    String redirectURL = "http://localhost:8081/ChatApplication/Home.jsp";
    response.sendRedirect(redirectURL);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.form-margin{
margin-bottom: 5px;
}
</style>
</head>
<body>
<%!static Date date = new Date();
public static Date getDate(){
	return date;
}
%>
<% String str = getServletInfo();
ClientActions cA = null;
%>
<br>
<center>
<form action="Product" method="Post" class="form-margin">
<p class="form-margin">Enter your product details:</p>
<br>
Product Name:<input type="text" name="productName" required>
<br>
Product Count:<input type="number" name="productCount" required>
<br>
Product Price:<input type="number" name="productPrice" class="form-margin" required>
<br>
<input type="submit" value="Update new product"> 
</form>
<form action="ProductList" method="get">
<input type="submit" value="productList">
</form>
</center>
</body>
</html>