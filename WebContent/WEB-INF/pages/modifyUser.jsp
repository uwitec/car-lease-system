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
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.tree.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				var systemOrg = "";				//系统组织根节点
				
				if($("#gender").val() == "男"){
					$(".mail").get(0).checked = true;
				}else if($("#gender").val() == "女"){
					$(".femail").get(0).checked = true;
				}

				$("#idCardNumber").keyup(function(){
					this.value = this.value.replace(/[\W]/g,''); 
				});
				$("#qq").keyup(function(){
					this.value = this.value.replace(/\D/g,''); 
				});
				
				$("#saveUser").click(function(){
					if(!$("#id").val()){
						$.dqgb.globalMsg.showMsg("请选择部门信息");
						return;
					}
					if(!$("#realName").val()){
						$.dqgb.globalMsg.showMsg("请填写用户姓名");
						return;
					}
					if(!$("#userName").val()){
						$.dqgb.globalMsg.showMsg("请填写帐号信息");
						return;
					}
					if(!$("#idCardNumber").val()){
						$.dqgb.globalMsg.showMsg("请填写身份证信息");
						return;
					}
					
					$.dqgb.shadowLayer.showMask({mask:"正在修改用户信息,请稍后...."});
					$.ajax({
						url:"modifyUserInformation.action",
						async:true,
						type:'post',
						data:{
							userName : $("#userName").val(),
							realName : $("#realName").val(),
							departmentId : $("#id").val(),
							age : $("#age").val(),
							gender : $(".gender[checked=true]").hasClass("mail") ? "男":"女",
							idCardNumber : $("#idCardNumber").val(),
							qq : $.trim($("#qq").val())
						},
						success:function(data){
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
							}else{
								$.dqgb.shadowLayer.alert({
									content:"用户信息修改成功!"
								});
							}
						}
					});
				});
				$("#age").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				
				//获取系统组织数据
				$.dqgb.tree.beforeCreateNode = function(obj){
					if(obj.getNodeAttribute().leaf){
						obj.getNodeAttribute().iconCls = 'iconCls';
						obj.setNodeAttribute({
							dblclick:function(){
								//左键双击设置部门信息
								$("#id").val(obj.getNodeAttribute().id);
								$("#departmentName").val(obj.getNodeAttribute().text);
							}
						});
					}else{
					}
				};
				$("#departmentName").keyup(function(){
					$(this).val("");
					$("#id").val("");
				});
				$.ajax({
					url:"getOrganization.action",
					type:'post',
					async:true,
					data:{
						pageIndex:0,
						pageSize:10000
					},
					success : function(data){
						if(!data || !data.totalSize){
							return;
						}
						var treeData = new Array();
						for(var i = 0; i < data.totalSize; i++){
							treeData[treeData.length] = new Object();
							treeData[treeData.length - 1].text = data.data[i].departmentName;
							treeData[treeData.length - 1].id = data.data[i].id;
							if(data.data[i].id == $("#id").val()){						//设置部门名称
								$("#departmentName").val(data.data[i].departmentName);
							}
						}
						systemOrg = $.dqgb.tree.getDqgbTree({
							text:'系统现有组织',
							container:$("#companyTree"),
							staticTree : true,
							treeData:treeData,
							iconCls:'root'
						});
					}
				});
			});

			function toJson(Obj){
				if(!Obj){
					return;
				}
				var str = '{';
				for(el in Obj){
					str += el.toString() + ":'" + Obj[el] + "',";
				}
				str = str.substr(0,str.length - 1);
				str += "}";
				return str;
			}
		</SCRIPT>
	</head>
	<body  style="text-align: center;">
		<div>
			<div style="width:240px; background-color:white; height:200px; float:left; text-align: center;" id="modifyUser">
				<table width=240>
					<tr>
						<td style="text-align: left;">部门：</td>
						<td colspan="2">
							<input type="hidden" id="id"  value="<s:property value='#session.SpecifiedUser.departmentId' />"/>
							<input style="width:160px;" type="text" maxlength="50" id="departmentName"  />
						</td>
					</tr>
					<tr>
						<td style="text-align: left;">帐号：</td>
						<td colspan="2" style="text-align: left;">
							<input type="hidden" id="userName" maxLength="20" value='<s:property value="session.SpecifiedUser.userName" />'/>
								<s:property value="#session.SpecifiedUser.userName" />
						</td>
					</tr>
					<tr>
						<td style="text-align: left;">年龄：</td>
						<td colspan="2"><input style="width:160px;" type="text" id="age" maxlength="5"
								value='<s:property value="#session.SpecifiedUser.age" />'/>
						</td>
					</tr>
					<tr>
						<td style="text-align: left;">姓名：</td>
						<td colspan="2"><input style="width:160px;" type="text" maxLength="20" id="realName"
								value='<s:property value="#session.SpecifiedUser.realName" />'/>
						</td>
					</tr>
					<tr>
						<td style="text-align: left;">身份证：</td>
						<td colspan="2"><input style="width:160px;" type="text" maxLength="20" id="idCardNumber"
								value='<s:property value="#session.SpecifiedUser.idCardNumber" />'/>
						</td>
					</tr>
					<tr>
						<td style="text-align: left;">QQ：</td>
						<td colspan="2"><input style="width:160px;" type="text" maxLength="20" id="qq"
								value='<s:property value="#session.SpecifiedUser.qq" />'/>
						</td>
					</tr>
					<tr>
						<td style="text-align: left;">性别：
							<input type="hidden" id="gender" 
							value='<s:property value="#session.SpecifiedUser.gender" />'/>
						</td>
						<td><input type="radio" class="mail gender"  name="gender"/>男</td>
						<td><input type="radio" class="femail gender" name="gender"/>女</td>
					</tr>
				</table>
			</div>
			
			<div style="float:left; width:200px; margin-left:20px;overflow:auto;
				height:200px; text-align: center;"  id="companyTree" >
			
			</div>
		</div>
		<input type="button" id="saveUser" style="width:50px;" value="保存" />
	</body> 
</html>