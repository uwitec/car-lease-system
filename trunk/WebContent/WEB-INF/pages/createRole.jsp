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
		<title>新建角色</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				$("#addRole").click(function(){
					if("" == $.trim($("#roleName").val())){
						$.dqgb.shadowLayer.alert({
							content:"角色名不能为空!"
						});
					}else{
						$.dqgb.shadowLayer.showMask({
							mask:"正在创建角色信息,请稍后....."
						});
						$.ajax({
							url:"createPositionRole.action",
							async:true,
							type:"post",
							data:{
								roleName:$("#roleName").val()
							},
							success : function(data){
								if(data.RESULT == "FAILED"){
									$.dqgb.shadowLayer.alert({
										content:data.REASON
									});
								}else{
									$.dqgb.shadowLayer.alert({
										content:"角色创建成功!"
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
					<td style="text-align: left;">角色名：</td>
					<td colspan="2">
						<input style="width:200px;" type="text" maxlength="20" id="roleName"  />
					</td>
				</tr>
			</table>
			<p></p>
			<input type="button" id="addRole" style="width:50px;" value="保存" />
		</div>
	
	</body> 
</html>