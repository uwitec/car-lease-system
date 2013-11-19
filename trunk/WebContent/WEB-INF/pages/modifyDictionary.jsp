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
		<title>字典管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				
				$("#saveUser").click(function(){
					$.dqgb.shadowLayer.showMask({mask:"正在修改字典信息,请稍后...."});
					$.ajax({
						url:"modifyDictionaryInformation.action",
						async:true,
						type:'post',
						data:{
							id : $("#id").val(),
							remark : $("#remark").val(),
							groupId : $("#groupId").val(),
							groupRemark : $("#groupRemark").val()
						},
						success:function(data){
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
							}else{
								$.dqgb.shadowLayer.alert({
									content:"字典信息修改成功!"
								});
							}
						}
					});
				});
			});

		</SCRIPT>
	</head>
	<body style='font-size:14px; font-family: 微软雅黑'>
		<div style="width:300px; background-color:white; height:160px; padding:10px; text-align: center;" id="modifyUser">
			<table width=280px>
				<tr>
					<td style="text-align: left; width:120">属组ID：</td>
					<td colspan="2" style="text-align: left;"><input type="hidden" maxLength="20" id="groupId"
							value='<s:property value="#session.SpecifiedDictionary.groupId" />'/>
							<s:property value="#session.SpecifiedDictionary.groupId" />
					</td>
				</tr>
				<tr>
					<td style="text-align: left;">属组描述：</td>
					<td colspan="2" style="text-align: left;"><input type="hidden" maxLength="20" id="groupRemark"
							value='<s:property value="#session.SpecifiedDictionary.groupRemark" />'/>
							<s:property value="#session.SpecifiedDictionary.groupRemark" />
					</td>
				</tr>
				<tr>
					<td style="text-align: left;">字典ID：</td>
					<td colspan="2" style="text-align: left;">
						<input type="hidden" id="id" maxLength="20" value='<s:property value="session.SpecifiedDictionary.id" />'/>
							<s:property value="#session.SpecifiedDictionary.id" />
					</td>
				</tr>
				<tr>
					<td style="text-align: left;">字典名：</td>
					<td colspan="2" style="text-align: left;"><input style="width:160px;" type="text" id="remark"
							value='<s:property value="#session.SpecifiedDictionary.remark" />'/>
					</td>
				</tr>
			</table>
			<p></p>
			<input type="button" id="saveUser" style="width:50px;" value="保存" />
		</div>
	</body> 
</html>