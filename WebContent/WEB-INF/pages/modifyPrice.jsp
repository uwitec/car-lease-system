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
		<title>价格管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.tree.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				
				$("#brand").keyup(function(){				//不允许手动输入品牌
					this.value = "";
					$("#brandId").val();
				});
				$("#price").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#foregift").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#pricePerHour").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#pricePerKm").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#priceRM").keyup(function(){							//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#pricePerHourRM").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#pricePerKmRM").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				//修改树样式
				$.dqgb.tree.beforeCreateNode = function(obj){
					if(obj.getNodeAttribute().leaf){
						obj.getNodeAttribute().iconCls = 'modify';
						obj.setNodeAttribute({
							click:function(){
								$("#brand").val(obj.getNodeAttribute().text);
								$("#brandId").val(obj.getNodeAttribute().id);
							}
						});
					}
				};
				//======获取品牌信息==========
				$.ajax({
					url:'getBrandInformation.action',
					type:'post',
					async:true,
					data:{
						pageIndex:0,
						pageSize:9999
					},
					success:function(obj){
						if(!obj || !obj.data ||!obj.data.totalSize){
							return;
						}
						var data = obj.data;

						var treeData = new Array();
						for(var i = 0; i < data.totalSize; i++){
							treeData[treeData.length] = new Object();
							treeData[treeData.length - 1].text = data.data[i].remark;
							treeData[treeData.length - 1].id = data.data[i].id;
						}
						systemOrg = $.dqgb.tree.getDqgbTree({
							text:'品牌信息',
							container:$("#brandContainer"),
							staticTree : true,
							treeData:treeData,
							iconCls:'root'
						});
					}
				});

				$("#save").click(function(){
					if($.trim($("#brand").val()) == ""){
						$.dqgb.globalMsg.showMsg('品牌信息不能为空!');
						return;
					}else if("" == $.trim($("#price").val())){
						$.dqgb.globalMsg.showMsg('价格不能为空!');
						return;
					}
					/* else if("" == $.trim($("#pricePerKm").val())){
						$.dqgb.globalMsg.showMsg('超驶费不能为空!');
						return;
					} */
					else if("" == $.trim($("#foregift").val())){
						$.dqgb.globalMsg.showMsg('押金信息不能为空!');
						return;
					}else if("" == $.trim($("#pricePerHour").val())){
						$.dqgb.globalMsg.showMsg('超时费不能为空!');
						return;
					}else if("" == $.trim($("#priceRM").val())){
						$.dqgb.globalMsg.showMsg('限里程模式下的价格不能为空!');
						return;
					}else if("" == $.trim($("#pricePerKmRM").val())){
						$.dqgb.globalMsg.showMsg('限里程模式下的超驶费不能为空!');
						return;
					}else if("" == $.trim($("#pricePerHourRM").val())){
						$.dqgb.globalMsg.showMsg('限里程模式下的超时费不能为空!');
						return;
					}
					$.dqgb.shadowLayer.showMask({mask:'正在保存数据信息,请稍后....'});
					$.ajax({
						url:'modifyPrice.action',
						async:true,
						type:'post',
						data:{
							id:$("#priceId").val(),
							brandId:$("#brandId").val(),
							carCategory:$("#carCategory").val(),
							price:$("#price").val(),
							pricePerHour:$("#pricePerHour").val(),
							pricePerKm:$("#pricePerKmRM").val(),
							priceRM:$("#priceRM").val(),
							pricePerHourRM:$("#pricePerHourRM").val(),
							pricePerKmRM:$("#pricePerKmRM").val(),
							foregift:$("#foregift").val()
						},
						success:function(data){
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
							}else{
								$.dqgb.shadowLayer.alert({
									content:"价格信息修改成功!"
								});
							}
						}
					});
				});
			});
		</SCRIPT>
	</head>
	<body>
		<div style="text-align: center; font: 14px 微软雅黑;">
			<div style="width:250px; height:227px; float:left; padding:0px 10px; text-align: center;">
					<input type="hidden" id="priceId" value="<s:property value='#session.PRICE.id'/>" /><!-- 主键id -->
					<input type="hidden" id="brandId" value="<s:property value='#session.PRICE.brandId'/>" /><!-- 品牌id -->
				<table>
					<tr>
						<td align="left">品牌：</td><td><input type="text" style="width:120px;" id="brand" value="<s:property value='#session.brandName' />"/></td>
					</tr>
					<tr>
						<td align="left">系列：</td><td><input type="text" style="width:120px;" id="carCategory" maxLength=50 value="<s:property value='#session.PRICE.carCategory' />"/></td>
					</tr>
					<tr>
						<td align="left">押金：</td><td><input type="text" style="width:120px;" id="foregift" value="<s:property value='#session.PRICE.foregift'/>" /></td>
					</tr>
					<tr>
						<td align="left">价格：</td><td><input type="text" style="width:120px;" id="price" value="<s:property value='#session.PRICE.price' />"/></td>
					</tr>
					<tr>
						<td align="left">超时计费：</td><td><input type="text" style="width:120px;" id="pricePerHour" value="<s:property value='#session.PRICE.pricePerHour'/>" /></td>
					</tr>
					<%-- 
					<tr>
						<td align="left">超驶计费：</td><td><input type="text" style="width:120px;" id="pricePerKm" value="<s:property value='#session.PRICE.pricePerKm'/>" /></td>
					</tr>
					 --%>
					<tr>
						<td align="left">限里程单价：</td><td><input type="text" style="width:120px;" id="priceRM" value="<s:property value='#session.PRICE.priceRM' />"/></td>
					</tr>
					<tr>
						<td align="left">限里程超时费：</td><td><input type="text" style="width:120px;" id="pricePerHourRM" value="<s:property value='#session.PRICE.pricePerHourRM'/>" /></td>
					</tr>
					<tr>
						<td align="left">限里程超驶费：</td><td><input type="text" style="width:120px;" id="pricePerKmRM" value="<s:property value='#session.PRICE.pricePerKmRM'/>" /></td>
					</tr>
				</table>
				
			</div>
			<div id="brandContainer" style="width:210px; height:227px; border:1px solid groove; border-right:2px; border-bottom:2px; 
				padding: 10px; padding-right:2px; padding-left:2px; overflow: auto;">
				
			</div>
			<input type="button" id="save" style="margin-top:20px; width:50px;" value="保存" />
		</div>
	</body> 
</html>