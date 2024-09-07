<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="constants.Constants" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-2.2.4.min.js"
	integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css">
<title>Insert title here</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 0;
	background-color: #f4f4f4;
}

.container {
	width: 80%;
	margin: 20px auto;
	background-color: #fff;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h1 {
	text-align: center;
	color: #333;
}

.review-form {
	margin-top: 20px;
}

.review-form label {
	display: block;
	font-weight: bold;
	margin-bottom: 5px;
}

.review-form input[type="text"], .review-form textarea {
	width: 100%;
	padding: 8px;
	margin-bottom: 15px;
	border: 1px solid #ccc;
	border-radius: 5px;
	box-sizing: border-box;
}

.review-form input[type="submit"] {
	background-color: #4CAF50;
	color: white;
	padding: 10px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 16px;
}

.review-list {
	margin-top: 30px;
}

.review {
	border-bottom: 1px solid #ccc;
	padding-bottom: 10px;
	margin-bottom: 15px;
}

.review:last-child {
	border-bottom: none;
}

.review h3 {
	margin: 0;
	color: #333;
}

.review p {
	margin-top: 5px;
	color: #666;
}
</style>
<script type="text/javascript">
	$(document).ready(
			function() {
				console.log(getParameterByName("product"));

				document.getElementById("reviewSubmit").addEventListener(
						"click",
						function(event) {
							event.preventDefault();
							try {
								let form = $("#Reviews-page")[0];
								data = new FormData(form);
								data.append("productId",
										getParameterByName("productId"));
								data.append("star", document
										.getElementById("rating").value);
								console.log(data + "data");
								$.ajax({
									type : "POST",
									url : "Reviews",
									data : data,
									processData : false,
									contentType : false,
									success : function(res) {
										toastr.success(res, "Success");
										setTimeout(function() {
											document.getElementById("Reviews-page").reset();
											document.location.href = "<%= Constants.RUN_DEV %>"+"/ZKart/Home.jsp";
										}, 300);
										window.location 
									},
									error : function(error) {
										toastr.error(error, "error");
									}
								});
							} catch (e) {
								console.log(e, "catch");
							}
						});

			});
	function getParameterByName(name, url) {
		if (!url)
			url = window.location.href;
		name = name.replace(/[\[\]]/g, '\\$&');
		var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'), results = regex
				.exec(url);
		if (!results)
			return null;
		if (!results[2])
			return '';
		return decodeURIComponent(results[2].replace(/\+/g, ' '));
	}
</script>
</head>
<body>
	<div class="container">
		<h1>Product Reviews</h1>
		<div class="review-form">
			<h2>Add Your Review</h2>
			<form action="Reviews" method="post" id="Reviews-page"
				enctype="multipart/form-data">
				<label for="rating">Rating (1-5)</label> <input type="text"
					id="rating" name="rating" min="0" max="5" maxlength="1"
					type="number"> <label for="comment">Your Review</label>
				<textarea id="comment" name="comment" rows="4" required></textarea>
				<input type="button" value="Submit Review" id="reviewSubmit">
			</form>
		</div>
		<!-- 	<div class="review-list">
            <h2>Customer Reviews</h2>
            <div class="review">
                <h3>John Doe</h3>
                <p>Rating: 4</p>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla quis lorem ut libero malesuada feugiat.</p>
            </div>
            <div class="review">
                <h3>Jane Smith</h3>
                <p>Rating: 5</p>
                <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
            </div>
        </div>  -->

	</div>
</body>
</html>