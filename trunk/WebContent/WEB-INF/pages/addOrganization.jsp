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
		<title>组织管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				//=====初始化库文件========
				$.dqgb.button.modifyButtonStyle();
				
				//=============
				$("#addOrganization").click(function(){
					if(!$.trim($("#departmentName").val())){
						$.dqgb.shadowLayer.alert({content:"组织名称不能为空!"});
						return;
					}

					$.ajax({
						url:"addOrganization.action",
						type:'post',
						async:true,
						data:{
							departmentName:$("#departmentName").val()
						},
						success : function(data){
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
							}else{
								$.dqgb.shadowLayer.alert({
									content:'保存成功!'
								});
							}
						}
					});
				});
			});

		</SCRIPT>
	</head>
	<body style="text-align: center;">
		<div style="width:280px; background-color:white; height:60px; text-align: center;" id="modifyUser">
			<table width=280>
				<tr>
					<td style="text-align: left;">组织名称：</td>
					<td colspan="2">
						<input style="width:160px;" type="text" maxlength="100" id="departmentName"  />
					</td>
				</tr>
			</table>
		</div>
		<input type="button" id="addOrganization" style="width:50px;" value="保存" />
	</body> 
</html>