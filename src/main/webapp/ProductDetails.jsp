<%@page import="jakarta.websocket.Session"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.ArrayList"%>
<%@ page import="constants.Constants"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-2.2.4.min.js"
	integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
	crossorigin="anonymous"></script>

<script src="https://code.jquery.com/jquery-1.12.4.min.js"> </script>
<title>Insert title here</title>
<style>
.productContainer {
	margin-top: 20px;
	background: blanchedalmond;
	margin-left: 20px;
	margin-right: 20px;
	dispaly: flex;
	height: 500px;
}

.product-details {
	padding: 0px 20px 0px 20px;
}

.button-style {
	height: 40px;
	width: 150px;
	color: #fff;
	background-color: #7971ea;
	border-color: #7971ea;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}

.price-font {
	font-size: 25px;
	font-weight: 900;
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

body {
	font-family: Arial, sans-serif;
}

/* .star-rating {
	direction: rtl;
	display: flex;
	font-size: 2rem;
	padding: 10px;
} 

.star-rating input[type="radio"] {
	display: none;
}

.star-rating label {
	color: #ddd;
	cursor: pointer;
	transition: color 0.2s ease-in-out;
}

.star-rating input[type="radio"]:checked ~ label {
	color: #ffcc00;
}

.star-rating label:hover, .star-rating label:hover ~ label {
	color: #ffcc00;
} */

.star {
	font-size: 24px;
}

body {
	font-family: Arial, sans-serif;
}

.star-rating {
	display: inline-block;
	font-size: 2rem;
	position: relative;
	unicode-bidi: bidi-override;
	color: #ddd;
}

.star-rating .star {
	display: inline-block;
	position: relative;
	padding: 0 2px;
}

.star-rating .star::before {
	content: "\2605"; /* Full star */
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	overflow: hidden;
}

.star-color{
color: #ffcc00;
}
.star-rating[data-rating="1"] .star:nth-child(1)::before, .star-rating[data-rating="1.5"] .star:nth-child(1)::before,
	.star-rating[data-rating="2"] .star:nth-child(-n+2)::before,
	.star-rating[data-rating="2.5"] .star:nth-child(-n+2)::before,
	.star-rating[data-rating="3"] .star:nth-child(-n+3)::before,
	.star-rating[data-rating="3.5"] .star:nth-child(-n+3)::before,
	.star-rating[data-rating="4"] .star:nth-child(-n+4)::before,
	.star-rating[data-rating="4.5"] .star:nth-child(-n+4)::before,
	.star-rating[data-rating="5"] .star:nth-child(-n+5)::before {
	width: 100%;
}

.star-rating[data-rating="1.5"] .star:nth-child(2)::before, .star-rating[data-rating="2.5"] .star:nth-child(3)::before,
	.star-rating[data-rating="3.5"] .star:nth-child(4)::before,
	.star-rating[data-rating="4.5"] .star:nth-child(5)::before {
	width: 50%;
}

.add-product-review {
	padding-top: 20px;
	display: flex;
	justify-content: end;
}
.flex{
    display: flex;
    align-items: center;
    justify-content: center;
}
</style>
<script type="text/javascript">
    // JavaScript function to increase count
    function increseCount() {
        var countElement = document.getElementById("count");
        var count = parseInt(countElement.innerText);
        countElement.innerText = count + 1;
    }

    // JavaScript function to decrease count
    function decreseCount() {
        var countElement = document.getElementById("count");
        var count = parseInt(countElement.innerText);
        if (count > 0) {
            countElement.innerText = count - 1;
        }
    }
    
</script>
</head>
<body>
	<div class="productContainer" style="display: flex">
		<img src='images/<%=request.getParameter("Product_image")%>'
			alt='Girl in a jacket' width='500px' height='600px'>
		<div class="product-details">
			<h1><%=request.getParameter("Name")%></h1>
			<p><%=request.getParameter("Description")%></p>
			<script type="text/javascript">
    $(document).ready(function(){
    	<%String userName = (String) session.getAttribute("userName");%>
    	$('#addToCardButton').click(function(){
    		let user = '<%=userName%>' || null;
    	   	<%if (session.getAttribute("userName") != null) {%>
    	   	var countElement = document.getElementById("count");
    	   	console.log("123456789","<%=request.getParameter("Name")%>");
    	   	$.ajax({
        		url: "AddToCards",
                type: "POST",
                data: {
                    "product":"<%=request.getParameter("Name")%>",
                    "productCount":parseInt(countElement.innerText),
                    "productPrice":"<%=request.getParameter("Actual_price")%>",
                    "productId":"<%=request.getParameter("id")%>",
                    "image":"<%=request.getParameter("Product_image")%>"
                },
                success: function(res) {
                	var event = new CustomEvent("addToCardChange", {
                        bubble: true,
                    });
                    window.parent.dispatchEvent(event);
                },
                error: function(xhr, status, error) {
                    var errorMessage = xhr.responseText;
                    console.log(errorMessage);
                }
        	});
        	<%} else {%>
    	   		window.location.href = "<%=Constants.RUN_DEV%>"+"/ZKart/SignIn.jsp";
    	   		<%}%>
    	});


        $("#buyNow").click(function(){
            var user = '<%=session.getAttribute("userName")%>';
            if (user !== "null") {
                buyNowClicked();
            } else {
                window.location.href = "<%=Constants.RUN_DEV%>/ZKart/SignIn.jsp";
            }
        });
    });
    
    
	function fetchReviews(search) {	
       let data = {
    		   "productId":"<%=request.getParameter("id")%>"
       }
       
       $.ajax({
   		url: "Reviews",
           type: "GET",
           data:data,
           dataType: "json",
           success: function(res) {
           	console.log(res);
           	setStarRatings(res);
           },
           error: function(xhr, status, error) {
               var errorMessage = xhr.responseText;
               console.log(errorMessage);
           }
   	});
   }
    

    function buyNowClicked(){
    	let productCount = parseInt(document.getElementById("count").innerText)<1 ? 1 : parseInt(document.getElementById("count").innerText);
        var product = getProductDetails();
					console.log("buyNowClicked", product);
					var event = new CustomEvent("buyNowClicked", {
						bubble : true,
						detail : product,
					});
					window.parent.dispatchEvent(event);
				}
    function redirectToReview(){
    	var queryString = $.param(getProductDetails()); // Convert event detail to URL query string
        window.location.href = 'AddReview.jsp?' + queryString;
    }
    
    function getProductDetails(){
    	let productCount = parseInt(document.getElementById("count").innerText)<1 ? 1 : parseInt(document.getElementById("count").innerText);
    	return {
            "product": "<%=request.getParameter("Name")%>",
            "productCount": productCount,
            "productPrice": "<%=request.getParameter("Actual_price")%>",
            "productId": "<%=request.getParameter("id")%>"
					};
				}

    function setStarRatings(data) {
        const container = document.getElementById('reviews-container');
        data.forEach(review => {
            const reviewElement = document.createElement('div');
            reviewElement.className = 'review-container';

            const userDetails = document.createElement('div');
            userDetails.classList.add('flex');
            
            const userAvatarElement = document.createElement("img");
            userAvatarElement.src = "images/Login_default_Avatar.png";
            userAvatarElement.alt = "User Avatar";
            userAvatarElement.width = 30;
            userAvatarElement.height = 100;
            userDetails.appendChild(userAvatarElement);
            userDetails.style.justifyContent= "start";
            
            const nameElement = document.createElement('h4');
            nameElement.textContent = review.name;
            userDetails.appendChild(nameElement);
            
            reviewElement.appendChild(userDetails);
            
           

            const starRatingElement = document.createElement('div');
            starRatingElement.className = 'star-rating';
            starRatingElement.setAttribute('data-rating', review.star);

            for (let i = 1; i <= 5; i++) {
            	const starElement = document.createElement('span');
                starElement.className = 'star';
                if (i <= review.rating) {
                    starElement.classList.add('filled');
                }
                starElement.innerHTML = '&#9733;'; // Unicode for star
                starRatingElement.appendChild(starElement);
            	
        //  //  const starElement = document.createElement('span');
            //    starElement.className = 'star';
             //   starElement.innerHTML = '&#9733;';
            //    starRatingElement.appendChild(starElement); 
            }

            
            reviewElement.appendChild(starRatingElement);
            
            const commentElement = document.createElement('p');
            commentElement.textContent = review.comment;
            reviewElement.appendChild(commentElement);
            
            
            
            container.appendChild(reviewElement);
        });

        // Adjust star widths based on rating
        const starRatings = document.querySelectorAll('.star-rating');
        starRatings.forEach(starRating => {
            const rating = parseFloat(starRating.getAttribute('data-rating'));
            console.log(rating,"rating");
            const stars = starRating.querySelectorAll('.star');
            stars.forEach((star, index) => {
                if(index<rating){
                	star.classList.add("star-color");
                }
            });
        });
    }
    
			</script>

			<div style="display: flex; align-items: center; gap: 20px">
				<span class="price-font">$<%=request.getParameter("Actual_price")%></span>
				<div class="flex">
					<div onclick="decreseCount()" class="in-de-button flex dec-button">-</div>
					<div class="in-de-button flex" id="count">1</div>
					<div onclick="increseCount()" class="in-de-button flex inc-button">+</div>
				</div>
				<button class="button-style" id="addToCardButton">Add to
					card</button>
				<button class="button-style" id="buyNow">Buy Now</button>
			</div>
			<div id="add-product-review" class="add-product-review">
				<button onclick="redirectToReview()">Write a product review</button>
			</div>
			<div class="flex"><span id="read-reviews" onclick="fetchReviews()">Read Reviews</span></div>
			<div id="reviews-container" style="max-height:130px;overflow-y: scroll;"></div>
			 
		</div>

	</div>
</body>
</html>