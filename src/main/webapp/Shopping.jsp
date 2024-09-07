<%@page import="org.json.JSONArray"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-2.2.4.min.js"
	integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
	crossorigin="anonymous"></script>
<title>Insert title here</title>
<script type="text/javascript">
var productDetailList = [];
var totalproductCount = 0;
var pageCount = 0;
var currentPage = 1;
var search = "";
function productCardClick(){
	var event = new CustomEvent("productCard", { bubbles: true,details:"product" });
    window.parent.dispatchEvent(event);
}


	$(document).ready(function() {
		 console.log("ready");
		
		 	productCount("");
			productList("");
	/*	$.ajax({
			url:"ProductCount",
			type:"GET",
			success:function(res){
				console.log(res);
				totalproductCount = res;
				pageCount = (totalproductCount/10)+(totalproductCount%10 > 0 ? 1 : 0);
			},
			error:function(error){
				console.log(error);
			}
		})
		
		
		$.ajax({
			url : "ProductList",
			type : "GET",
			dataType : "json",
			success : function(response) {
				productCarts(response);
				productDetailList = response;
			},
			error : function(xhr, status, error) {
				console.error("AJAX request failed: " + status + ", " + error);
			}
		})  */
		
	}); 
	
	
	window.addEventListener("typeSearch",function(event){
		let word = event.detail.toLowerCase();	
		search = word;
		console.log(word+"56778");
		productCount(word);
		productList(word);
		// productCarts(productDetailList.filter((product)=>(product.Name).toLowerCase().includes(word)));
	}); 
	
	
	
/*	window.addEventListener("searchInput",function(event){
		console.log(event.detail);
		productCount(event.detail);
		productList(event.detail);
	}) */
	
	
/*	searchInput.addEventListener('input', function() {
	    clearTimeout(delayTimer);
	    const keyword = this.value.trim();

	    // Start a new timer when the user stops typing
	    delayTimer = setTimeout(() => {
	        if (keyword) {
	            searchAPI(keyword);
	        } else {
	            searchResults.innerHTML = ''; // Clear search results if input is empty
	        }
	    }, 500); // Adjust the delay time as needed (in milliseconds)
	}); */
	
	
	
	function productCount(search) {
		 updatePagination(1);
		 
        $.ajax({
            url: "ProductCount",
            type: "GET",
            dataType: "json",
            data: {
            	searchWord:search,
            },
            success: function(res) {
                totalproductCount = res.count;
                pageCount = parseInt(totalproductCount / 10) + ((totalproductCount % 10 > 0) ? 1 : 0);
                console.log(pageCount);
            },
            error: function(error) {
                console.log(error);
            }
        });
    }

    function productList(val) {
    	console.log(val);
        $.ajax({
            url: "ProductList",
            type: "GET",
            dataType: "json",
            data: {
                searchWord:val,
                pageNumber:currentPage,
            },
            success: function(response) {
                productCarts(response);
                productDetailList = response;
            },
            error: function(xhr, status, error) {
                console.error("AJAX request failed: " + status + ", " + error);
            }
        });
    }
	
	function productCardClicked(product) {
		productCardClick();
		var event = new CustomEvent("productCardClicked", {
			bubble : true,
			detail : product,
		});
		window.parent.dispatchEvent(event);
	}
	
	
	function productCarts(data) {
		var container = document.getElementById("demo");
		container.innerHTML = '';
		if(data.length<1){
			document.getElementById("pagination").style.display = "none";
		}
		else{
			document.getElementById("pagination").style.display = "flex";
		}
		for (var i = 0; i < data.length; i++) {
			let product = data[i];
			var card = document.createElement("div");
			var image = product.Product_image;
			var discountedPrice = Math.round(product.Actual_price
					- ((product.Actual_price) * (product.Discounts / 100)));
			card.id = "product" + i;
			card.className = "product-card";
			card.innerHTML = "<div class='product-card-content'>"
					+ "<img src='images/"+image+"' alt='Girl in a jacket' width='50' height='60' class='productImg'>"
					+ "<h3 class='elipces'>" + product.Name + "</h3>"
					+ "<p class='elipces description'>" + product.Description
					+ "</p>" + "<p class='elipces'>$" + discountedPrice
					+ " |<span class='discount'>" + product.Discounts
					+ "% off</span>" + "<span class='actual-price'>$"
					+ product.Actual_price + "</span>" + "</p>" + "</div>";
			$(document).on("click", "#product" + i, function() {
				productCardClicked(product);
			});
			container.appendChild(card);
		}
	} 
	
	
	
	var prevButton = document.getElementById("prevPage");
	var nextButton = document.getElementById("nextPage");
	

	function updatePagination(val = 1) {
		var currentPageElement = document.getElementById("pagination-number");
	    currentPageElement.textContent = val;
	    currentPage = val;
	}

	prevButton.addEventListener("click", function() {
	    if (currentPage > 1) {
	        currentPage--;
	        updatePagination(currentPage);
	        productList(search);
	    }
	});

	nextButton.addEventListener("click", function() {
		console.log(totalproductCount);
		if(pageCount>currentPage){
	    currentPage++;
	    updatePagination(currentPage);
	    productList(search);
		}
	});

	updatePagination(); 
	
</script>
<style>
.demo1 {
	display: flex;
	flex-wrap: wrap;
	padding-left: 190px;
}

.productImg {
	width: -webkit-fill-available;
	height: 120px;
}

.productCard {
	width: 50px;
	height: 50px;
	display: flex;
}

#productContainer {
	display: flex;
	flex-wrap: wrap;
	justify-content: space-between;
}

.product-card {
	border: 1px solid #ccc;
	border-radius: 5px;
	padding: 10px;
	margin: 10px;
	width: calc(25% - 120px); /* Set width to 25% minus margin */
}

.elipces {
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	font-size: 12.5px;
	line-height: normal;
}

.description {
	display: -webkit-box;
	-webkit-line-clamp: 3;
	-webkit-box-orient: vertical;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: normal;
	font-size: 9.5px;
	color: gray;
}

.discount {
	margin-left: 8px;
	font-size: 9.5px;
	color: red;
	font-weight: bold;
}

.actual-price {
	margin-left: 8px;
	font-size: 9.5px;
	color: gray;
	font-weight: bold;
	text-decoration: line-through;
}

@media ( max-width : 768px) {
	.product-card {
		width: calc(50% - 20px); /* Adjust width for smaller screens */
	}
	@media ( max-width : 576px) {
		.product-card {
			width: calc(100% - 20px); /* Adjust width for even smaller screens */
		}
	}
}
.pagination {
	display: flex;
	justify-content: center;
	align-items: center;
}

.pagination button {
	padding: 2px 6px;
	margin: 0 5px;
	cursor: pointer;
	border: 1px solid #ccc;
	background-color: #f9f9f9;
	border: 2px solid #59e659;
}

.pagination button:hover {
	background-color: #ddd;
}

.current-page {
	font-weight: bold;
	color: darkolivegreen;
}
</style>
</head>
<body>
	<div id="demo" class="demo1"></div>
	<div class="pagination" id="pagination">
		<button id="prevPage"><</button>
		<span id="currentPage" class="current-page">You are on the <span
			id="pagination-number">1</span></span>
		<button id="nextPage">></button>
	</div>
</body>
</html>