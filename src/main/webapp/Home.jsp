<%@page import="jakarta.websocket.Session"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="constants.Constants" %>
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

<script src="https://code.jquery.com/jquery-1.12.4.min.js"> 
    </script>
<title>Insert title here</title>

<style>
.header-container {
	height: 40px;
	width: 100%;
	width: 100%;
	display: flex;
	justify-content: space-between;
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

.add-card {
	position: relative;
	display: inline-block;
}

.cart-item-count {
	position: absolute;
	top: -16px;
	right: -16px;
	background-color: #7971ea;
	color: white;
	border-radius: 50%;
	/* padding: 5px 8px; */
	text-align: center;
	width: 24px;
	height: 24px;
	line-height: 24px;
	font-size: 12px;
}

.visable-hidden {
	visibility: hidden;
}
</style>
 <script type="text/javascript">
var a = 10;
function changeColor(item,page) { 
    var listItems = document.querySelectorAll('li');
    listItems.forEach(function(li) {
        li.classList.remove('selected');
    });
    item.classList.add('selected');
    searchBox(page);
    $('#pages').load(page);
}
function searchBox(page){
	document.getElementById("search").value = "";
	document.getElementById("search").classList.add("visable-hidden");
    if(page=='Shopping.jsp'){
    	document.getElementById("search").classList.remove("visable-hidden");
    }
}
window.addEventListener("myCustomEvent", function() {
    var listItems = document.querySelectorAll('li');
    
    listItems.forEach(item=>item.classList.remove('selected'));
    if(listItems.length>=2){
    	listItems[1].classList.add('selected');
    	$('#pages').load('Shopping.jsp');
    }
});

window.addEventListener("productCard", function(event) {
	searchBox(event.details);
});

window.addEventListener("productCardClicked",function(event){
	console.log(event.detail);
	$('#pages').load('ProductDetails.jsp',event.detail);
})

window.addEventListener("buyNowClicked",function(event){
	console.log(event.detail);
	$('#pages').load('BuyProduct.jsp',event.detail);
})

window.onload = function(){
	console.log("jcnjcn");
	$('#pages').load('HomeDashboard.jsp');
}

$(document).ready(function() {
	
	<%if (session.getAttribute("userName") != null) {%>
		document.getElementById("signIn").style.display = "none";
		$.ajax({
            url: 'LoadAddToCartCount',
            type: 'GET',
            dataType: "json",
            success: function(response) {
                document.getElementById("cartItemCount").innerHTML = response.count;
            },
            error: function(xhr, status, error) {
                console.error('Error clearing session:', error);
            }
        });
		<%} else {%>
		document.getElementById("signOut").style.display = "none";
	<%}%>
    $(".gfg").click(function(){ 
    	document.location.href = "<%= Constants.RUN_DEV %>"+"/ZKart/GetProductDetails.jsp";
    }); 
    $(".signin").click(function(){
    	document.location.href = "<%= Constants.RUN_DEV %>"+"/ZKart/jsp/authentication/SignIn.jsp"; 

    });
    $(".signOut").click(function(){
    	$.ajax({
            url: 'ClearSession',
            type: 'GET',
            success: function(response) {
                document.getElementById("signOut").style.display = "none";
                document.getElementById("signIn").style.display = "block";
            },
            error: function(xhr, status, error) {
                console.error('Error clearing session:', error);
            }
        });
    });
    
    var timeout;
    $('#search').on('input', function() {
        clearTimeout(timeout); // Clear previous timeout
        timeout = setTimeout(filterProducts, 200); // Set a new timeout
    });
    
}); 

window.addEventListener("addToCardChange",function(event){
	$.ajax({
        url: 'LoadAddToCartCount',
        type: 'GET',
        dataType:"json",
        success: function(response) {
            document.getElementById("cartItemCount").innerHTML = response.count;
        },
        error: function(xhr, status, error) {
            console.error('Error clearing session:', error);
        }
    });
});

function filterProducts() {
    var searchText = $('#search').val().toLowerCase();
		var event = new CustomEvent("typeSearch",{
			bubble:true,
			detail:searchText,
		});
		window.parent.dispatchEvent(event);
}

function updateCartItemCount() {
    // Get the cart item count from your cart data (replace 0 with actual count)
    var cartItemCount = 0; // Replace 0 with actual count

    // Update the cart item count displayed in the HTML
    var cartItemCountElement = document.getElementById("cartItemCount");
    cartItemCountElement.textContent = cartItemCount;
}
function goAddToCard(){
	
}
</script>
</head>
<body style="margin: 0px;height:100vh">
	<div>
	<jsp:include page="dataFetcher.jsp" />
		<div class="header-container">
			<div>
			
				<input type="text" name="search" placeholder="Search"
					class="margin-l20 search-box" id="search" hidden>
				<div
					style="background-image: 'C:\Users\kumar\git\repository\ChatApplication\src\main\webapp\WEB-INF\images\searchicon.png'"></div>
			</div>
			<p style="display: contents; font-size: 22px;">Z-Kart</p>
			<div style="display: flex; gap: 16px; align-items: center;">
				<button type="button" class="signin new-product-button" id="signIn">SignIn
				</button>
				<button id="signOut" type="button"
					class="new-product-button signOut">SignOut</button>
				<button type="button" class="gfg new-product-button">New
					Product</button>
				<p class="add-card">
					<i class="icon-shopping-cart"></i> <span id="cartItemCount"
						class="cart-item-count">0</span>
				</p>
				<p id="geeks"></p>
			</div>
		</div>
		<%!String dynamicPage = "HomeDashboard.jsp";

	public static void changePage(String str) {

	}%>
		<%

		%>
		<nav class="navigation-bar">
			<ul class="nav-style margin-0">
				<li class="selected" onclick="changeColor(this,'HomeDashboard.jsp')">Home</li>
				<li onclick="changeColor(this,'Shopping.jsp')">Shop</li>
				<li onclick="changeColor(this,'GiftCards.jsp')">Gift cards</li>
				<li onclick="changeColor(this,'Account.jsp')">Account</li>
				<li onclick="changeColor(this,'Contact.jsp')">Contact</li>
			</ul>
		</nav>
		<div id="pages">
			<!--   <jsp:include page="HomeDashboard.jsp"></jsp:include>	-->
		</div>
	</div>
</body>
</html>