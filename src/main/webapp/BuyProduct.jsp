<%@page import="loader.CustomerDetails"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-2.2.4.min.js"
	integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css">
<style>
.buyProductDailog {
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
	background-image: linear-gradient(to top, #a18cd1 0%, #fbc2eb 100%);
	background-repeat: no-repeat;
	background-size: 100%;
	margin: 0px;
}

.signin-body {
	background: white;
	border-radius: 15px;
	margin: 50px 0px 30px 0px;
}

.input-box {
	border: 0px;
	background-color: white;
	background-size: 16px;
	margin-left: 47px;
	width: -webkit-fill-available;
	margin-right: 47px;
}

.signin-btns {
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

.in-de-button {
	height: 25px;
	width: 25px;
	border: 1px solid black;
}

.flex {
	display: flex;
	align-items: center;
	justify-content: center;
}

.dec-button {
	border-radius: 5px 0px 0px 5px;
	margin: -1;
}

.inc-button {
	border-radius: 0px 5px 5px 0px;
	margin: -1;
}

.total-count {
	display: flex;
	gap: 20px;
}

.product-summary {
	background: white;
	width: 470px;
	margin: auto;
	border-radius: 10px;
}

.product-details {
	padding-left: 50px;
	margin-top: 10px;
}

.product-payment {
	background: white;
	width: 470px;
	margin: auto;
	border-radius: 10px;
}

.payment-input {
	border: none !important;
	border-bottom: 2px solid green !important;
	border-radius: 0px !important;
	box-shadow: none !important;
}

.container {
	width: 100%;
	display: flex;
	align-items: center;
	flex-direction: column;
}

.payment-button {
	height: 35px;
	border-radius: 10px;
	color: white;
	border: none;
	background: #1fd655;
	width: 120px !important;
}

.exp-wrapper {
	display: flex;
}

.toggle-container {
	display: inline-block;
	position: relative;
	display: flex;
	align-items: center;
	height: 40px;
	background: aliceblue;
	width: 20%;
	border-radius: 10px;
	margin: auto;
	margin-bottom: 10px;
	justify-content: space-around;
}

input[type="radio"] {
	display: none;
}

.toggle {
	position: absolute;
	top: 0;
	left: 0;
	width: 50%;
	height: 40px;
	background-color: #ccc;
	border-radius: 10px;
	transition: all 0.3s ease;
	background-color: #007bff;
}

#net-banking:checked+label[for="net-banking"]+.toggle {
	background-color: #007bff;
	/* Change background color when net banking is selected */
	transform: translateX(100%);
}

payment-lable {
	display: inline-block;
	padding: 10px 20px; /* Adjust padding as needed */
	background-color: #fff;
	border: 1px solid #ccc;
	border-radius: 20px;
	z-index: 2;
	cursor: pointer;
}

#net-banking:checked+label[for="net-banking"] {
	color: #fff; /* Change text color when net banking is selected */
}

label {
	margin: 0;
}

input:invalid {
  border-bottom: 2px dashed red !important;
}
</style>
<script type="text/javascript">
	function increseCount() {
		var countElement = document.getElementById("count");
		var count = parseInt(countElement.innerText);
		countElement.innerText = count + 1;
		var totalElement = document.getElementById("price");
		totalElement.innerText = parseInt(totalElement.innerHTML)
				+
<%=Integer.parseInt(request.getParameter("productPrice"))%>
	;
	}
	function decreseCounts() {
		var countElement = document.getElementById("count");
		var count = parseInt(countElement.innerText);
		if (count > 1) {
			countElement.innerText = count - 1;
			var totalElement = document.getElementById("price");
			totalElement.innerText = parseInt(totalElement.innerHTML)
					-
<%=Integer.parseInt(request.getParameter("productPrice"))%>
	;
		}
	}
</script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						document.getElementById("net-banking-board").style.display = "none";
						var countElement = document.getElementById("count");
						var count = parseInt(countElement.innerText);
						countElement.innerText =
<%=request.getParameter("productCount")%>
	;
						var totalElement = document.getElementById("price");
						totalElement.innerText =
<%=Integer.parseInt(request.getParameter("productCount"))
		* Integer.parseInt(request.getParameter("productPrice"))%>
	;
	
	document.getElementById("product-price").innerText = <%=Integer.parseInt(request.getParameter("productPrice"))%>;
	document.getElementById("product-name").innerText = "<%=(request.getParameter("product"))%>";
	
						$.ajax({
							url : "CutomerDetail",
							type : "GET",
							dataType : "json",
							success : function(response) {
								var totalElement = document
										.getElementById("phone");
								totalElement.value = response[0].phone;
							},
							error : function(xhr, status, error) {
								// Handle errors
								console.error("AJAX request failed: " + status
										+ ", " + error);
							}
						});
					
						$("#buyProduct").on("submit", function(event) {
				            event.preventDefault();
				            
				            
				         var element = document.getElementById('net-banking-board');
				         var formElement = "cart";
				         // Check if the element exists and is visible
				         if (element && window.getComputedStyle(element).display !== 'none') {
				             // Element is visible
				             formElement = "NetBanking";
				             console.log("Element is visible");
				         }
				            
				            var addressElement = document.getElementById("address");
				            var isValidInAddressInput = addressElement.checkValidity();
				            if(!isValidInAddressInput){
				            	addressElement.reportValidity();
				            	return;
				            }
				            try {
				            	if(!formElement){
				            		return;
				            	}
				            	
				                let data = null;
				                if(formElement=="cart"){
	                        		data = new FormData(document.getElementById('Cart-details'));
	                        	}else{
	                        		data = new FormData(document.getElementById('net-banking-board'));
	                        	}
				                var countElement = document.getElementById("count");
				        		var count = parseInt(countElement.innerText);
				                data.append("productCount", count);
								data.append("totalAmount",document.getElementById("price").innerHTML);
				                let sessionValue = '<%=session.getAttribute("Id")%>';
				                data.append("customerId", sessionValue);
				                data.append("productId",'<%=request.getParameter("productId")%>');
								data.append("address",document.getElementById("address").value);
								data.append("paymentWay",formElement);
								
				                $.ajax({
				                    type: "POST",
				                    url: "ProductBuying",
				                    data: data,
				                    processData: false,
				                    contentType: false,
				                    success: function(res) {
				                        toastr.success(res, "Success");
				                        setTimeout(function() {
				                        	if(formElement=="cart"){
				                        		document.getElementById('Cart-details').reset();
				                        	}else{
				                        		document.getElementById('net-banking-board').reset();
				                        	}
				                        	addressElement.value = '';
				                        }, 300);
				                    },
				                    error: function(xhr, status, error) {
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
	
	const monthInput = document.querySelector('#month');
	const yearInput = document.querySelector('#year');

	const focusSibling = function(target, direction, callback) {
	  const nextTarget = target[direction];
	  nextTarget && nextTarget.focus();
	  // if callback is supplied we return the sibling target which has focus
	  callback && callback(nextTarget);
	}

	// input event only fires if there is space in the input for entry. 
	// If an input of x length has x characters, keyboard press will not fire this input event.
	monthInput.addEventListener('input', (event) => {

	  const value = event.target.value.toString();
	  // adds 0 to month user input like 9 -> 09
	  if (value.length === 1 && value > 1) {
	      event.target.value = "0" + value;
	  }
	  // bounds
	  if (value === "00") {
	      event.target.value = "01";
	  } else if (value > 12) {
	      event.target.value = "12";
	  }
	  // if we have a filled input we jump to the year input
	  2 <= event.target.value.length && focusSibling(event.target, "nextElementSibling");
	  event.stopImmediatePropagation();
	});

	yearInput.addEventListener('keydown', (event) => {
	  // if the year is empty jump to the month input
	  if (event.key === "Backspace" && event.target.selectionStart === 0) {
	    focusSibling(event.target, "previousElementSibling");
	    event.stopImmediatePropagation();
	  }
	});

	const inputMatchesPattern = function(e) {
	  const { 
	    value, 
	    selectionStart, 
	    selectionEnd, 
	    pattern 
	  } = e.target;
	  
	  const character = String.fromCharCode(e.which);
	  const proposedEntry = value.slice(0, selectionStart) + character + value.slice(selectionEnd);
	  const match = proposedEntry.match(pattern);
	  
	  return e.metaKey || // cmd/ctrl
	    e.which <= 0 || // arrow keys
	    e.which == 8 || // delete key
	    match && match["0"] === match.input; // pattern regex isMatch - workaround for passing [0-9]* into RegExp
	};

	document.querySelectorAll('input[data-pattern-validate]').forEach(el => el.addEventListener('keypress', e => {
	  if (!inputMatchesPattern(e)) {
	    return e.preventDefault();
	  }
	}));
	
	document.querySelectorAll('input[data-pattern-validate]').forEach(el => el.addEventListener('keypress', e => {
		  if (!inputMatchesPattern(e)) {
		    return e.preventDefault();
		  }
		}));
	
	
	document.getElementById("address").addEventListener("keypress", function(event) {
        var charCode = event.charCode || event.keyCode;
        var charStr = String.fromCharCode(charCode);

        const specialCharsRegex = /[`~!@#$%^&*()_|+\-=?;:'"<>\{\}\[\]\\\/]/g;
        if (specialCharsRegex.test(charStr)) {
            event.preventDefault();
        }
    });
	
	function togglePayment(option) {
	    if (option === 'net-banking') {
	    	document.getElementById("Cart-details").style.display = "none";
	        document.getElementById("net-banking-board").style.display = "flex";
	    } else {
	        document.getElementById("Cart-details").style.display = "flex";
	        document.getElementById("net-banking-board").style.display = "none";
	    }
	}
	
	function preventUnwantedKeys(event, regex) {
	    let key = String.fromCharCode(event.keyCode);
	    console.log(regex.test(key),"--->",regex);
	    if (!regex.test(key) && !event.ctrlKey && !event.altKey) {
	        event.preventDefault();
	    }
	}
	
	
	
	
</script>
<body style="margin: 0px; height: 100%;" class="signin-bg">
	<%!public int number = 10;
	public String message = "Hello, World!";
	public boolean isValidUser = false;%>
	<div class="signin-bg" id="buyProduct" enctype="multipart/form-data"
		action="process_payment.jsp" method="post">
		<div style="display: flex;">
			<div class="buyProductDailog">
				<div class="signin-body">
					<div>
						<p style="padding-left: 50px; margin-top: 10px;">
							Address: <span class="required">*</span>
						</p>
						<input type="text" name="name" class="input-box" required
							id="address">
					</div>

					<p style="padding-left: 50px; margin-top: 10px;">
						Phone Number <span class="required">*</span>
					</p>
					<input type="text" name="phone" class="input-box" type="number"
						required id="phone">

					<!-- 	<div class="signin-btn-wrapper">
					<button class="signin-btns" id="myAnchor">Buy Now</button>
				</div> -->
					<div
						style="margin-top: 20px; display: flex; align-items: center; justify-content: center;">
					</div>
				</div>
			</div>
			<div class="product-summary">
				<div class="product-details">
					Product name:<span id="product-name"></span>
				</div>
				<div class="product-details">
					Product Price:<span id="product-price"></span>
				</div>
				<div class="total-count">
					<p style="padding-left: 50px; margin-top: 10px;">productCount:</p>
					<div class="flex">
						<div onclick="decreseCounts()"
							class="in-de-button flex dec-button">-</div>
						<div class="in-de-button flex" id="count">1</div>
						<div onclick="increseCount()" class="in-de-button flex inc-button">+</div>
					</div>
				</div>
				<p style="padding-left: 50px; margin-top: 10px;">
					Total Price:<span id="price"></span>
				</p>
			</div>

		</div>
		<div class="toggle-container">
			<input type="radio" id="carts" name="payment" checked> <label
				for="carts" class="payment-lable" style="z-index: 2"
				onclick="togglePayment('carts')">Carts</label> <input type="radio"
				id="net-banking" name="payment"> <label for="net-banking"
				class="payment-lable" style="z-index: 2"
				onclick="togglePayment('net-banking')">Net Banking</label>
			<div class="toggle"></div>
		</div>

		<div class="product-payment">
			<form id="Cart-details" class="container" enctype="multipart/form-data"
		action="process_payment.jsp" method="post">
				<h2>Credit Cart Payment</h2>
				<div>
					<div
						style="display: flex; align-items: center; justify-content: center; gap: 20px;">
						<div>
							<label for="card_number">Card Number:</label> <input type="text"
								id="card_number" name="card_number"
								placeholder="Enter card number" required class="payment-input"
								maxlength="16" pattern="[0-9]*" inputmode="numerical"
								placeholder="card number" type="text" data-pattern-validate>
						</div>
						<div>
							<label for="expiry_date">Expiry Date:</label>
							<div class="exp-wrapper">
								<input autocomplete="off" class="exp payment-input" id="month"
									maxlength="2" pattern="[0-9]*" inputmode="numerical"
									placeholder="MM" type="text" data-pattern-validate
									style="width: 25px" required/> <input autocomplete="off"
									class="payment-input" id="year" maxlength="2" pattern="[0-9]*"
									inputmode="numerical" placeholder="YY" type="text"
									data-pattern-validate style="width: 30px" required />
							</div>

						</div>
					</div>
					<div
						style="display: flex; align-items: center; justify-content: center; gap: 20px;">
						<div>
							<label for="cvv">CVV:</label> <input type="text" id="cvv"
								max="999" name="cvv" placeholder="Enter CVV" required
								class="payment-input" style="width: 70px;" maxlength="3"
								pattern="[0-9]*" inputmode="numerical" type="text"
								data-pattern-validate>
						</div>
						<div>
							<label for="name">Cardholder Name:</label> <input type="text"
								id="name" name="name" placeholder="Enter cardholder name"
								required class="payment-input">
						</div>
					</div>
					<div class="flex">
						<input value="Pay Now" class="payment-button" type="submit">
					</div>
				</div>
			</form>
			<form id="net-banking-board" class="container" enctype="multipart/form-data"
		action="process_payment.jsp" method="post">
				<h2>Net Banking Payment</h2>
				<div>
					<div
						style="display: flex; align-items: center; justify-content: center; gap: 20px;">
						<div>
							<label for="card_number">User Name:</label> <input type="text"
								id="card_number" name="card_number"
								placeholder="Enter card number" required class="payment-input"
								placeholder="User name" type="text"
								onkeypress="preventUnwantedKeys(event, /^[a-zA-Z0-9]{0,20}$/)" minlength="6" maxlength="20">
						</div>
						<div>
							<label for="expiry_date">Password:</label>
							<div class="exp-wrapper">
								<input autocomplete="off" class="payment-input" type="text"
									style="width: 60px; padding: 4px 2px;" required
									placeholder="password" onkeypress="preventUnwantedKeys(event, /^[a-zA-Z0-9]{0,8}$/)" minlength="6" maxlength="8"/>
							</div>
						</div>
					</div>
					<div class="flex">
						<input value="Pay Now" class="payment-button" type="submit">
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>