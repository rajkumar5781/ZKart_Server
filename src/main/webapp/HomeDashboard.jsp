<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
.home-background{
    width: 100%;
    height: 500px;
    background: url(images/Home-background.png);
    background-color: darkseagreen;
    background-repeat: no-repeat;
    background-size: 65%;
    background-position-x: right;
}
.home-background-content{
    padding:100px 0px 0px 150px
}
.home-header-content{
font-family: sans-serif;
    font-weight: 100;}
    p{
    font-family: cursive;}
    .start-shop-button{
    border-radius: 5px;
    letter-spacing: 2.0px;
    border: none;
    color: white;
    background: #465899;
    height: 27px;
    cursor: pointer;
    }
</style>
<script type="text/javascript">
function startShopping(){
	var event = new CustomEvent("myCustomEvent", { bubbles: true });
    window.parent.dispatchEvent(event);
}
</script>
</head>
<body>
<div class="home-background">
<div class="home-background-content">
<h1 class="home-header-content">Welcome Home</h1>
<p>We are so happy you're here</p>
<p>Great deals Grab now</p>
<button onclick="startShopping()" class="start-shop-button">START SHOPPING</button>
</div> 
</div>
</body>
</html>