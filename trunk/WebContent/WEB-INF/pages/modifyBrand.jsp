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
		<title>修改品牌</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				$("#modifyBrand").click(function(){
					if("" == $.trim($("#brandName").val())){
						$.dqgb.shadowLayer.alert({
							content:"品牌名不能为空!"
						});
					}else{
						$.dqgb.shadowLayer.showMask({
							mask:"正在修改品牌信息,请稍后....."
						});
						$.ajax({
							url:"modifyBrand.action",
							async:true,
							type:"post",
							data:{
								brandName:$("#brandName").val(),
								id:$("#brandId").val()
							},
							success : function(data){
								if(data.RESULT == "FAILED"){
									$.dqgb.shadowLayer.alert({
										content:data.REASON
									});
								}else{
									$.dqgb.shadowLayer.alert({
										content:"品牌信息修改成功!"
									});
								}
							}
						});
					}

				});
			});
		</SCRIPT>
	</head>
	<body>
		<div style="width:300px; background-color:white; height:120px; padding:10px; text-align: center;" id="modifyUser">
			<table width=280px>
				<tr>
					<td style="text-align: left;">品牌名：</td>
					<td colspan="2">
						<input style="width:200px;" type="text" maxlength="50" id="brandName" value="<s:property value='#session.BRAND.remark' />" />
						<input type="hidden" maxlength="50" id="brandId" value="<s:property value='#session.BRAND.id' />" />
					</td>
				</tr>
			</table>
			<p></p>
			<input type="button" id="modifyBrand" style="width:50px;" value="保存" />
		</div>
	
	</body> 
</html>