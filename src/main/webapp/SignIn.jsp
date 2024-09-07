<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="constants.Constants" %>
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


<script src="https://code.jquery.com/jquery-2.2.4.min.js"
	integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css">
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
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$("#signinFrom").on("submit", function(event) {
			// Prevent default form submission behavior
			event.preventDefault();
			try {
				// Retrieve the form data
				let forms = $(this)[0];
				let data = new FormData(forms);
				console.log(data);
				$.ajax({
					type : forms.method,
					url : "signIn",
					data : data,
					processData : false,
					 contentType: false,
					success : function(res) {
						toastr.success(res, "Success");
						
						setTimeout(function() {
							document.getElementById("signinFrom").reset();
							document.location.href = "<%= Constants.RUN_DEV %>"+"/ZKart/Home.jsp";
						}, 300);
					},
					error : function(xhr, status, error) {
						var errorMessage = xhr.responseText;
						toastr.error(errorMessage, "Error");
						console.log(errorMessage);
					}
				});
			} catch (e) {
				console.log(e, "catch");
			}
		});
	});
</script>
<body style="margin: 0px; height: 100vh;" class="signin-bg">
	<%!public int number = 10;
	public String message = "Hello, World!";
	public boolean isValidUser = false;%>
	<jsp:include page="dataFetcher.jsp"></jsp:include>
	<form action="signIn" method="post" id="signinFrom" enctype="multipart/form-data">
		<div class="signin">
		<span>hello</span>
			<app-hello-world></app-hello-world>
			<p style="font-size: 22px; margin-bottom: 75px; margin-top: 10px;"
				class="flex">Z-Kart</p>
			<p class="flex"
				style="margin: 0px; letter-spacing: 2px; font-size: 25px; font-weight: 900; margin-bottom: 70px;">Good
				to see you again</p>
			<div class="signin-body">
				<p style="padding-left: 50px; margin-top: 20px;">UserName</p>
				<input type="text" name="userName" class="input-box" required>

				<p style="padding-left: 50px; margin-top: 20px;">Password</p>
				<input type="password" name="password" class="input-box" required>
				<div class="signin-btn-wrapper">
					<button class="signin-btn" type="submit" id="signin">Log
						In</button>
				</div>
				<div
					style="margin-top: 20px; display: flex; align-items: center; justify-content: center;">
					<a style="text-decoration: underline;"
						href="<%= Constants.RUN_DEV %>/ZKart/SignUp.jsp">Don't
						have an account?</a>
				</div>
			</div>
		</div>
	</form>
</body>
</html>