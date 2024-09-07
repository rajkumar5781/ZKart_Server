<%@page import="constants.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Insert title here</title>

<style>
.switch {
      position: relative;
    display: inline-block;
    width: 45px;
    height: 24px;
}

.switch input { 
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
    content: "";
    height: 16px;
    width: 16px;
    left: 4px;
    bottom: 4px;
    background-color: white;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(22px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}
.switch-wrapper{
display: flex;
    align-items: center;
    justify-content: end;
    margin-right: 30px;
    margin-top: 5px;
    gap: 5px;
}
</style>
<script type="text/javascript">
$(document).ready(function() {
	
	document.getElementById("switch-content").innerHTML="<%= Constants.CURRENT_DATA_RETREIVAL_MODE%>";
	if("<%= Constants.CURRENT_DATA_RETREIVAL_MODE%>" == "SQL"){
		document.getElementById("switch").checked = true;
	}
	else{
		document.getElementById("switch").checked = false;
	}

	$("#switch").click(function(){
		if(document.getElementById("switch-content").innerHTML == "XML"){
			document.getElementById("switch-content").innerHTML = "SQL";
		}
		else{
			document.getElementById("switch-content").innerHTML = "XML";
		}
		let data ={};
		if(document.getElementById("switch").checked){
			data ={"toggle": "on"};
		}
		else{
			data ={"toggle": "off"};
		}
		$.ajax({
			url:"DataFetcher",
			type:"POST",
			data : data,
			success:function(res){
				console.log("Successfully changed...");
			},
			error:function(err){
				console.log(err);
			}
		})
    	$.ajax({
            url: 'ClearSession',
            type: 'GET',
            success: function(response) {
                window.document.getElementById("signOut").style.display = "none";
                window.document.getElementById("signIn").style.display = "block";
            },
            error: function(xhr, status, error) {
                console.error('Error clearing session:', error);
            }
        });
	})
});
</script>
</head>
<body>
<div class="switch-wrapper">
<label class="switch">
  <input type="checkbox" id="switch">
  <span class="slider round"></span>
</label>
<p id="switch-content"></p>
</div>
</body>
</html>