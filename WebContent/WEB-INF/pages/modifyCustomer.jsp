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
		<title>修改用户</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/jquery-ui-1.8.12.custom.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-ui-1.8.12.custom.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				var systemOrg = "";				//系统组织根节点
				
				if($("#gender").val() == "男"){
					$(".mail").get(0).checked = true;
				}else if($("#gender").val() == "女"){
					$(".femail").get(0).checked = true;
				}
				
				if($("#blackList").val() == "1"){
					$(".yes").get(0).checked = true;
				}else{
					$(".no").get(0).checked = true;
				}

				/* $("#registerDate").val($("#registerDate").val().split(" ")[0]);
				$("#endDate").val($("#endDate").val().split(" ")[0]); */
				

				$("#registerDate").datepicker({
					dateFormat: 'yy-mm-dd',
					monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/'],
					onClose:function(text,inst){
					}
				});
				$("#endDate").datepicker({
					dateFormat: 'yy-mm-dd',
					monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/'],
					onClose:function(text,inst){
					}
				});
				
				$("#idCardNumber").keyup(function(){
					this.value = this.value.replace(/[\W]/g,''); 
				});

				$("#companyPhone").keyup(function(){
					this.value = this.value.replace(/[\W]/g,''); 
				});
				
				$("#mobilePhone").keyup(function(){
					this.value = this.value.replace(/\D/g,'');
				});
				
				$("#saveUser").click(function(){
					if(!$.trim($("#idCardNumber").val())){
						$.dqgb.shadowLayer.alert({content:'客户身份证号码不能为空！'});
						return;
					}
					$.dqgb.shadowLayer.showMask({mask:"正在修改客户数据信息,请稍后...."});
					$.ajax({
						url:"modifyCustomer.action",
						async:true,
						type:'post',
						data:{
							id : $("#cid").val(),
							company : $("#company").val(),
							customerName : $("#customerName").val(),
							mobilePhone : $("#mobilePhone").val(),
							gender : $(".gender:checked").hasClass("mail") ? "男":"女",
							idCardNumber : $("#idCardNumber").val(),
							companyPhone : $("#companyPhone").val(),
							companyAddress : $("#companyAddress").val(),
							introducer : $("#introducer").val(),
							warrantor : $("#warrantor").val(),
							
							registerDate : $("#registerDate").val(),
							endDate : $("#endDate").val(),
							blacklist : $(".blackList:checked").hasClass("yes") ? "1":"0",
							customerLevel : $("#customerLevel").val(),
							
							address : $("#address").val()
						},
						success:function(data){
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
							}else{
								$.dqgb.shadowLayer.alert({
									content:"客户信息修改成功!"
								});
							}
						}
					});
				});
			});

		</SCRIPT>
	</head>
	<body  style="text-align: center; font-family: 微软雅黑; font-size:14px;">
		<table width='100%'>
			<tr>
				<td style="text-align: left;">客户姓名：</td>
				<td colspan="2"><input style="width:160px;" type="text" maxLength="20" id="customerName"
						value='<s:property value="#session.CUSTOMER.customerName" />'/>
				</td>
				<td style="text-align: left;">身份证：</td>
				<td colspan="2"><input style="width:160px;" type="text" maxLength="20" id="idCardNumber"
						value='<s:property value="#session.CUSTOMER.idCardNumber" />'/>
				</td>
			</tr>
			<tr>
				<td style="text-align: left;">客户单位：</td>
				<td colspan="2">
					<input type="hidden" id="cid"  value="<s:property value='#session.CUSTOMER.id' />" />
					<input type="text" id="company" maxLength="100" style="width:160px;" value="<s:property value='#session.CUSTOMER.company' />"/>
				</td>
				<td style="text-align: left;">单位地址：</td>
				<td colspan="2">
					<input type="text" id="companyAddress" maxLength="100" style="width:160px;" value="<s:property value='#session.CUSTOMER.companyAddress' />"/>
				</td>
			</tr>
			<tr>
				<td style="text-align: left;">客户联系方式：</td>
				<td colspan="2"><input style="width:160px;" type="text" maxLength="20" id="mobilePhone"
						value='<s:property value="#session.CUSTOMER.mobilePhone" />'/>
				</td>
				<td style="text-align: left;">单位电话：</td>
				<td colspan="2"><input style="width:160px;" type="text" maxLength="20" id="companyPhone"
						value='<s:property value="#session.CUSTOMER.companyPhone" />'/>
				</td>
			</tr>
			<tr>	
				<td style="text-align: left;">客户家庭住址：</td>
				<td colspan="2"><input style="width:160px;" type="text" maxLength="100" id="address"
						value='<s:property value="#session.CUSTOMER.address" />'/>
				</td>
				<td style="text-align: left;">担保人：</td>
				<td colspan="2"><input style="width:160px;" type="text" maxLength="20" id="warrantor"  value='<s:property value="#session.CUSTOMER.warrantor"/>'/>
				</td>
			</tr>
			
			
			<tr>	
				<td style="text-align: left;">合同签订日期：</td>
				<td colspan="2"><input style="width:160px;" type="text" id="registerDate"
					 value='<s:property value="#session.r" />'
					/>
				</td>
				<td style="text-align: left;">合同到期日期：</td>
				<td colspan="2"><input style="width:160px;" type="text" id="endDate"
					 value='<s:property value="#session.e" />'
					/>
				</td>
			</tr>
			
			
			
			
			
			
			
			
			<tr>
				<td style="text-align: left;">客户等级：</td>
				<td colspan="2"><input style="width:160px;" type="text" maxLength="100" id="customerLevel" value='<s:property value="#session.CUSTOMER.customerLevel" />'/>
				</td>
				<td style="text-align: left;">介绍人：</td>
				<td colspan="2"><input style="width:160px;" type="text" maxLength="20" id="introducer" value='<s:property value="#session.CUSTOMER.introducer" />'/>
				</td>
			</tr>

			<tr>
				<td style="text-align: left;">黑名单：
					<input type="hidden" id="blackList" 
					value='<s:property value="#session.CUSTOMER.blacklist" />'/>
				</td>
				<td><input type="radio"  class="yes blackList" name="blackList"/>是</td>
				<td><input type="radio"  class="no blackList" name="blackList"/>否</td>
				
				<td style="text-align: left;">性别：
					<input type="hidden" id="gender" 
					value='<s:property value="#session.CUSTOMER.gender" />'/>
				</td>
				<td><input type="radio" class="mail gender"  name="gender"/>男</td>
				<td><input type="radio" class="femail gender" name="gender"/>女</td>
			</tr>
		</table>
		<input type="button" id="saveUser" style="margin-top:15px;width: 50px;" value="保存" />
	</body> 
</html>