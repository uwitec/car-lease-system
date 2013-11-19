<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link rel="stylesheet" href="css/main.css" />
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/jquery.kernel.2.0.js"></script>
		<title>租车管理系统</title>
		
		<script type="text/javascript">
			$(function(){
				$("#submit").click(function(){
					if(!$.trim($("#userName").val())){
						$.dqgb.globalMsg.showMsg("帐号信息不能为空！");
						return;
					}
					if(!$.trim($("#password").val())){
						$.dqgb.globalMsg.showMsg("密码信息不能为空！");
						return;
					}
					$.ajax({
						url:'login.action',
						type:'post',
						async:true,
						data:{
							'user.userName' : $.trim($("#userName").val()),
							'user.password' : $.trim($("#password").val())
						},
						success:function(data){
							if(data.RESULT == 'FAILED'){
								$.dqgb.globalMsg.showMsg(data.REASON);
							}else{
								if(data.redirect){
									window.location = data.redirect;
								}else{
									window.location = 'toMain.action';
								}
							}
						}
					});
				});
			});
		
		</script>
		
		<style type="text/css">
			.button_1{
					background:url(images/button_1.jpg) no-repeat; height:26px; width:62px; border:0; cursor:pointer;}
		</style>
	</head>
	<body bgcolor="#26498D">
			<table width="660" border="0" cellpadding="0" cellspacing="0"
				align="center">
				<tr>
					<td align="center" height="25"></td>
				</tr>
			</table>
			<table id="__01" width="660" height="510" border="0" cellpadding="0"
				cellspacing="0" align="center">
				<tr>
					<td background="images/login_01.png" width="320" height="127"></td>
					<td background="images/login_02.png" width="340" height="127"></td>
				</tr>
				<tr>
					<td background="images/login_03.png" width="320" height="191"></td>
					<td background="images/login_04.png" width="340" height="191"></td>
				</tr>
				<tr>
					<td background="images/login_05.png" width="320" height="192"></td>
					<td background="images/login_06.png" width="340" height="192"><table
							align="center" width="80%" border="0">
							<tr>
								<td>帐号</td><td align="right"><input type="text" id="userName" style="width:150px;margin-right: 60px;" /></td>
							</tr>
							<tr>
								<td>密码</td><td align="right"><input type="password" id="password" style="width:150px;margin-right: 60px;" /></td>
							</tr>
							<tr>
								<td colspan="2" align="right"><input value="" type="button" class="button_1" style="margin-right: 60px;" id="submit"/></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<table width="660" border="0" cellpadding="0" cellspacing="0"
				align="center">
				<tr>
					<td align="center" height="25"><span class="STYLE4">&copy;南宁市广文信息科技有限公司&nbsp;&nbsp;&nbsp;联系电话：5772225</span>
					</td>
				</tr>
			</table>
	</body>
</html>