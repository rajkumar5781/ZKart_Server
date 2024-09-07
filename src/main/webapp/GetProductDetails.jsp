<%@page import="org.apache.catalina.connector.Response"%>
<%@page import="org.apache.coyote.Request"%>
<%@page import="servlets.ClientActions"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" info="This is first jsp page"
	errorPage="error.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
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
<title>Insert title here</title>
<style>
.product-details {
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
	background: antiquewhite;
	height: 100vh;
}

body {
	margin: 0px;
}

.submit-button {
	border: none;
	border-radius: 5px;
	background: black;
	height: 30px;
	cursor: pointer;
	color: white;
}
</style>
<style>
.header-container {
	height: 40px;
	width: 100%;
	width: 100%;
	display: flex;
	justify-content: center;
	align-items: center;
	border-bottom: 1px solid #f3f3f4;
	padding: 20px 0px 25px 0px;
}

.margin-l20 {
	margin-left: 20px;
}

.search-box {
	border: 0px;
	background: url('images/searchicon.png');
	background-position: 195px 7px;
	background-repeat: no-repeat;
	padding: 12px 20px 12px 40px;
	background-color: white;
	background-size: 16px;
}

.navigation-bar {
	width: 100%;
}

.nav-style {
	align-items: center;
	justify-content: center;
	display: flex;
	gap: 50px;
	height: 40px;
}

.margin-0 {
	margin: 0px;
}

li {
	display: block;
	width: 70px;
	height: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
}

.selected {
	background-color: darkseagreen;
}

.new-product-button {
	background: aliceblue;
	border: none;
	border-radius: 5px;
	height: 30px;
}

input:focus {
	outline: none;
}

.add-card {
	display: flex;
	align-items: center;
	margin: 0;
}

.parent-container {
	margin: auto;
	width: 1030px;
	background: #e7e9eb;
	height: 400px;
	border-radius: 30px;
}

.parent-header-block {
	display: flex;
	align-items: center;
	/* justify-content: space-between; */
	border-bottom: 2px solid #c8c8c8;
	margin: 0px;
}

li {
	white-space: nowrap;
	width: 50%;
	height: 40px;
}

.active-button {
	background: aliceblue;
}

.content-block {
	width: 100%;
	background: #e7e9eb;
	margin: auto;
	margin-top: 35px;
	height: 100%
}
</style>
<style>
.signin {
	width: 937px;
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
	height: 370px;
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

.divider-container {
	display: flex;
	justify-content: space-between;
}

.width50 {
	width: 50%
}

#dropZone {
	border: 2px dashed #ccc;
	padding: 20px;
	text-align: center;
	cursor: pointer;
	border-radius: 10px;
}

#dropZone.dragover {
	background-color: #f0f0f0;
}

.dropZone-container {
	height: 85%;
	display: flex;
	align-items: center;
	justify-content: center;
}

.flex {
	display: flex;
	align-items: center;
	justify-content: center;
}
/* Hide file input */
#fileInput {
	display: none;
}

.p-style {
	margin: 0 0 3px;
}
</style>
<script type="text/javascript">
	$(document).ready(
			function() {
				document.getElementById("single").classList
						.add("active-button");
				document.getElementById("singleFrom").style.display = "block";
				document.getElementById("bulkFrom").style.display = "none";
				document.getElementById("singleProductSubmit")
						.addEventListener(
								"click",
								function(event) {
									// Prevent default form submission behavior
									event.preventDefault();
									try {
										// Retrieve the form data
										let form = $("#singleFrom")[0];
										let data = new FormData(form);

										$.ajax({
											type : form.method,
											url : "Product",
											data : data,
											processData : false,
											contentType : false,
											success : function(res) {
												toastr.success(res, "Success");
												setTimeout(function() {
													document.getElementById(
															"singleFrom")
															.reset();
												}, 100);
											},
											error : function(error) {
												toastr.error(error, "error");
												console.log(error, "error");
											}
										});
									} catch (e) {
										console.log(e, "catch");
									}
								});

				document.getElementById("bulkProductSubmit").addEventListener(
						"click",
						function(event) {
							// Prevent default form submission behavior
							event.preventDefault();

							try {
								// Retrieve the form data
								let form = $("#bulkFrom")[0];
								let data = new FormData(form);

								$.ajax({
									type : form.method,
									url : "BulkProductUpdate",
									data : data,
									processData : false,
									contentType : false,
									success : function(res) {
										toastr.success(res, "Success");
										setTimeout(function() {
											document.getElementById("bulkFrom")
													.reset();
										}, 100);
									},
									error : function(error) {
										toastr.error(error, "error");
										console.log(error, "error");
									}
								});
							} catch (e) {
								console.log(e, "catch");
							}
						});
			});

	function singleUploadClick() {
		document.getElementById("single").classList.add("active-button");
		document.getElementById("bulk").classList.remove("active-button");
		document.getElementById("singleFrom").style.display = "block";
		document.getElementById("bulkFrom").style.display = "none";
	}
	function bulkUploadClick() {
		document.getElementById("single").classList.remove("active-button");
		document.getElementById("bulk").classList.add("active-button");
		document.getElementById("singleFrom").style.display = "none";
		document.getElementById("bulkFrom").style.display = "block";
	}

	function handleDragOver(event) {
		event.preventDefault();
		event.dataTransfer.dropEffect = 'copy';
		event.target.classList.add('dragover');
	}

	function handleFileDrop(event) {
		event.preventDefault();
		event.target.classList.remove('dragover');
		var file = event.dataTransfer.files[0];
		handleFile(file);
	}

	function handleFileSelect(event) {
		var file = event.target.files[0];
		handleFile(file);
		// Set input value to file name
		document.getElementById("fileInput").style.display = "block";

		document.getElementById("fileInput").files[0] = file;
		// Submit the for
	}

	function handleFile(file) {
		if (file) {
			if (file.type === 'text/csv') {
				// File is a CSV, you can proceed with handling it
				console.log('File selected:', file.name);
			} else {
				alert('Please select a CSV file.');
			}
		}
	}
</script>
</head>
<body>
	<%!static Date date = new Date();

	public static Date getDate() {
		return date;
	}%>
	<%
	String str = getServletInfo();
	ClientActions cA = null;
	%>

	<div class="header-container">
		<p style="display: contents; font-size: 22px;">Z-Kart</p>
	</div>

	<div class="parent-container">
		<ul class="parent-header-block">
			<li style="border-radius: 20px 0px 0px 0px;" id="single"
				onclick="singleUploadClick()">Individual Product Upload</li>
			<li style="border-radius: 0px 20px 0px 0px;"
				onclick="bulkUploadClick()" id="bulk">Bulk Upload</li>
		</ul>
		<div class="content-block">
			<form action="Product" method="post" id="singleFrom"
				enctype="multipart/form-data">
				<div class="signin">
					<div class="signin-body">
						<div class="divider-container">
							<div class="width50">
								<p style="padding-left: 50px; margin-top: 20px;" class="p-style">Name:</p>
								<input type="text" name="Name" class="input-box" id="Name">

								<p style="padding-left: 50px; margin-top: 20px;" class="p-style">Category:</p>
								<input type="text" name="Category" class="input-box"
									id="Category">

								<p style="padding-left: 50px; margin-top: 20px;" class="p-style">Discounts:</p>
								<input type="text" name="Discounts" class="input-box"
									id="Discounts">

								<p style="padding-left: 50px; margin-top: 20px;" class="p-style">Image:</p>
								<input type="file" name="image" accept="image/*"
									class="input-box" id="image">
							</div>
							<div class="width50">
								<p style="padding-left: 50px; margin-top: 20px;" class="p-style">Description:</p>
								<input type="text" name="Description" class="input-box"
									id="Description">

								<p style="padding-left: 50px; margin-top: 20px;" class="p-style">Price:</p>
								<input type="text" name="Actual_price" class="input-box"
									id="Actual_price">

								<p style="padding-left: 50px; margin-top: 20px;" class="p-style">Count:</p>
								<input type="text" name="productCount" class="input-box"
									id="productCount">
							</div>
						</div>
						<div class="signin-btn-wrapper" style="margin-top: 5px;">
							<button class="signin-btn gfg" id="singleProductSubmit">Submit</button>
						</div>
					</div>
				</div>
			</form>
			<form action="BulkProductUpdate" method="post"
				enctype="multipart/form-data" id="bulkFrom">
				<div class="signin">
					<div class="signin-body">
						<div class="dropZone-container">
							<div id="dropZone" ondragover="handleDragOver(event)"
								ondrop="handleFileDrop(event)">
								<p>Drag and drop CSV file here, or</p>
								<div>
									<input type="file" id="fileInput" accept=".csv"
										onchange="handleFileSelect(event)" name="fileInput">
								</div>
								<label for="fileInput">Browse</label>
							</div>

						</div>
						<div class="flex">
							<button class="signin-btn" id="bulkProductSubmit">Upload</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>

	<!-- 	<form action="Product" method="Post"
		class="form-margin product-details" enctype="multipart/form-data">
		<p class="form-margin">Enter your product details:</p>
		<br> Product Name:<input type="text" name="Name" required>
		<br> Product Discription:<input ="text" name="Description"
			required /> <br> Product Category:<input type="text"
			name="Category" required /><br>
		Product Price:<input type="number" name="Actual_price"
			class="form-margin" required> <br> Product Discounts:<input
			type="text" name="Discounts" required> <br> Product
		Count:<input type="number" name="productCount" required> <br>
		Product Image:<input type="file" name="image" accept="image/*">
		<br>
		<input type="submit"
			value="Update new product" class="submit-button">
	</form>  -->
</body>
</html>