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
		<title>新增保证金</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.query.js"></script>
		
		<script type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				$("#duration").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$("#money").keyup(function(){						//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$("#customerId").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$("#customerId").query({
					url:'queryCustomer.action',
					miniQueryLength:4,									//输入4个字符以后开始查询
					width:220,
					height:100,
					root:'data.data',									//数据根节点
					remindMsg:'没有找到相关客户信息，请确认您的输入是否正确!',	//没有找到数据时的提示
					param : {
						pageSize:5,
						pageIndex:0
					},
					keyField:'idCardNumber',							//核心字段
					fields:['idCardNumber','customerName']				//配置需要显示的字段
				});
				$("#confirm").click(function(){
					if(!$.trim($("#duration").val())){
						$.dqgb.globalMsg.showMsg("担保期限不能为空!");
						return;
					}
					if(!$.trim($("#money").val())){
						$.dqgb.globalMsg.showMsg("担保金额不能为空!");
						return;
					}
					if(!$.trim($("#customerId").val())){
						$.dqgb.globalMsg.showMsg("客户身份证号码不能为空!");
						return;
					}
					$.dqgb.shadowLayer.showMask({mask:'正在提交数据,请稍后....'});
					$.ajax({
						url:'addViolateSurety.action',
						type:'post',
						async:true,
						data:{
							'vs.money':$.trim($("#money").val()),
							'vs.carId':'xxx',
							'vs.customerId':$.trim($("#customerId").val()),
							duration:$.trim($("#duration").val())
						},
						success:function(data){
							if(data.RESULT != 'FAILED'){
								$.dqgb.shadowLayer.hideMask();
								$.dqgb.globalMsg.showMsg("数据提交成功！");
							}else{
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}
						}
					});
				});
			});
		</script>
		<style>
			td{
				padding-left:5px;
			}
		</style>
	</head>
	<body style="font: 14px 微软雅黑;">
		<table cellpadding="0" cellspacing="0" border="0" style="margin: auto;">
			<%-- <tr>
				<td>车牌号：</td><td><label id="carId">${carId}</label></td>
			</tr> --%>
			<tr>
				<td>客户身份证：</td><td><input type="text" id="customerId" maxlength="20"/></td>
			</tr>
			<tr>
				<td>担保期限(天)：</td><td><input type="text" value="30" id="duration"/></td>
			</tr>
			<tr>
				<td>金额：</td><td><input type="text" value="500" id="money"/></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="button" id="confirm" value="确定" style="width:50px; margin-top:15px;"/>
				</td>
			</tr>
		</table>
	</body>
</html>