<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link
	href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.no-icons.min.css"
	rel="stylesheet">
<link
	href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css"
	rel="stylesheet">
</head>
<style>
.signin {
	width: 470px;
	margin: auto;
	display: flex;
	justify-content: center;
	flex-direction: column;
}

.flex {
	display: flex !important;
	align-items: center;
	justify-content: center;
}

.signin-bg {
	background: url(images/SignInBg.jpg);
	background-repeat: no-repeat;
	background-size: 100%;
}

.signin-body {
	background: white;
	border-radius: 15px;
}

.input-box {
	border: 0px;
	background-color: white;
	background-size: 16px;
	margin-left: 47px;
	width: -webkit-fill-available;
	margin-right: 47px;
}

.signin-btn {
	width: 295px;
	background: #1f51ff;
	border: none;
	height: 35px;
	border-radius: 10px;
	color: white;
	font-weight: bold;
}

.signin-btn-wrapper {
	display: flex;
	align-items: center;
	justify-content: center;
	margin-top: 22px;
}

.required {
	color: red;
}

.name-input {
	
}
</style>
<body style="margin: 0px; height: 100vh;" class="signin-bg">
	<%!public int number = 10;
	public String message = "Hello, World!";
	public boolean isValidUser = false;%>
	<form action="signup" method="post" id="signup" enctype="multipart/form-data">
		<div class="signin">
			<p style="font-size: 22px; margin-bottom: 36px; margin-top: 20px;"
				class="flex">Z-Kart</p>
			<p class="flex" style="font-weight: bold; margin-bottom: 12px;">Create
				your account</p>
			<div class="signin-body">
				<p style="padding-left: 50px; margin-top: 15px;">
					UserName <span class="required">*</span>
				</p>
				<input type="text" name="userName" class="input-box" required>
				<p style="padding-left: 50px; margin-top: 10px;">
					Password <span class="required">*</span>
				</p>
				<input name="password" class="input-box" type="password" required>
				<div class="name-input">
					<p style="padding-left: 50px; margin-top: 10px;">
						Name <span class="required">*</span>
					</p>
					<input type="text" name="name" class="input-box" required>


					<p style="padding-left: 50px; margin-top: 10px;">Last Name</p>
					<input type="text" name="lastName" class="input-box">
				</div>

				<p style="padding-left: 50px; margin-top: 10px;">
					Phone Number <span class="required">*</span>
				</p>
				<input type="number" name="phone" class="input-box" type="number"
					required>

				<!--  <p style="padding-left: 50px;
    margin-top: 10px;">Address <span class="required">*</span></p>
<input type="text" name="LastName" class="input-box" required>  -->

				<div class="signin-btn-wrapper">
					<button class="signin-btn" type="submit">Sign Up</button>
				</div>
				<div
					style="margin-top: 20px; display: flex; align-items: center; justify-content: center;">
				</div>
			</div>
		</div>
	</form>
</body>
</html>