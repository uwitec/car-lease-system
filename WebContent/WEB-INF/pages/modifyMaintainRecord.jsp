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
		<title>修改养护记录</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/jquery-ui-1.8.12.custom.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-ui-1.8.12.custom.min.js"></script>
	
		<script type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				$("#money").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				
				$("#maintainDate").datepicker({
					dateFormat: 'yy-mm-dd',autoSize:true,changeYear:true,
					monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/']
				});
				
				$("#maintainDate").val($("#maintainDate").val().split(" ")[0]);
				
				$("#save").click(function(){
					if(!$.trim($("#remark").val())){
						$.dqgb.globalMsg.showMsg("请填写养护内容");
						return;
					}
					if(!$.trim($("#money").val())){
						$.dqgb.globalMsg.showMsg("请输入养护费用");
						return;
					}
					$.dqgb.shadowLayer.showMask({mask:"正在提交养护信息,请稍候..."});
					$.ajax({
						url:'modifyMaintainRecord.action',
						async:true,type:'post',
						data:{
							'mr.money' : $.trim($("#money").val()),
							'mr.id' : '${MaintainRecord.id}',
							'mr.maintainDate' : $.trim($("#maintainDate").val()),
							'mr.remark' : $.trim($("#remark").val())
						},
						success:function(data){
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
							}else{
								$.dqgb.shadowLayer.hideMask();
								$.dqgb.globalMsg.showMsg("数据提交成功");
							}
						}
					});
				});
			});
		</script>
		<style type="text/css">
			td{
				padding-left:5px;
				text-align: left;
				width:250px;
				height: 30px;
			}
		</style>
	</head>
	<body style="text-align: center; font-size: 14px; font-family: '微软雅黑'">
		<table cellpadding="0" cellspacing="0" border="0">
			<tr><td>车牌号:</td><td>${MaintainRecord.carId}</td></tr>
			<tr><td>养护内容:</td><td><input type="text" id="remark" style="width: 200px;" value="${MaintainRecord.remark}"/></td></tr>
			<tr><td>养护日期:</td><td><input type="text" id="maintainDate" style="width: 200px;" value="${MaintainRecord.maintainDate}"/></td></tr>
			<tr><td>养护费用:</td><td><input type="text" id="money" style="width: 200px;"  value="${MaintainRecord.money}"/></td></tr>
			<tr><td colspan="2" style="text-align: center;"><input type="button" id="save" value="确认" style="margin-top:15px; width:50px;"/></td></tr>
		</table>
	</body> 
</html>