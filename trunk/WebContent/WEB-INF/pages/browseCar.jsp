<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags" %>    
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<title>车辆预览</title>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript">
			$(function(){
				if($.trim($("#src").val())){
					$("#car").get(0).src = $.trim($("#src").val());
				}
			});
		</script>
	</head>
	<body>
		<img style="width:600px; height:360px; position: absolute; 
			top:50%; margin-top:-180px; left:50%; margin-left:-300px;" id="car" 
			src="upload/car/blank.jpg"/>
		<input type="hidden" value="<s:property value='#session.CAR.imagePath'/>" id="src"/>	
	</body> 
</html>