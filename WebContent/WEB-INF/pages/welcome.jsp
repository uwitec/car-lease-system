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
		<title>欢迎光临</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/component.3.0.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.3.0.js"></script>
		<script type="text/javascript">
			$(function(){
				$.ajax({
					url:'getAccessoryAI.action',
					async : true,type:'post',data : {
						
					},success:function(data){
						if(data.RESULT != 'FAILED'){
							if(data.totalSize != 0){
								var box = $("<div></div>").css({
									'text-indent':'2em','padding':'20px','font-size':'14px','font-family':'微软雅黑'
								}).html("您有&nbsp;<font color='fuchsia'>" + data.totalSize + 
								"</font>&nbsp;条车辆预警信息，点击<span>这里</span>查看").appendTo($(document.body)).xqbBox({
									width : 350,height:180,
									closeBtn : true,
									dragable : true,
									title : '车辆养护预警',
									iconCls : 'modify'
								}).animateShow({x:0,y:0});
								
								box.body.children("span").css({
									'color':'blue',
									'text-decoration':'underline','cursor':'pointer'
								}).click(function(){
									window.location = 'gotoCarAlarmPage.action';
								});
							}
						}else{
							$.dqgb.shadowLayer.alert({content:data.REASON});
						}
					}
				});
			});
		</script>
	</head>
	<body>
		<div class="welcome"></div>
	</body> 
</html>