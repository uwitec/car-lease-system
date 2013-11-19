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
		<title>财务结算</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		
		<style type="text/css">
			.title td{
				border-bottom: 1px gray dotted;
				font-weight: bold;
				color:fuchsia;
			}
			td{
				text-align:left;
				padding-left:40px;
			}
			#dis td{
				text-align:left;
				padding-left:0px;
			}
		</style>
	</head>
	<body style='width:600px; margin:0px;padding:0px; font-size:14px; font-family:微软雅黑; text-align: center; margin:auto;'>
		<input type="hidden" id="serialNumber" value='<s:property value="#session.LeaseReocordSerialNumber"/>'/>
		<div id="user">
		</div>
		<div id="nav"></div>
		<h3 align="center">财务结算</h3>
		<table id='finance' style="width:540px; margin-top:20px; text-align: left;" cellpadding="0" cellspacing="0" border="0">
			<tr class="title">
				<td style="width:120px;">财务类别</td>
				<td style="width:120px;">财务状态</td>
				<td style="width:120px;">财务金额</td>
				<td style="width:180px;">备注</td>
			</tr>
		</table>
		<table id='summary' style="width:540px; margin-top:20px; text-align: left;" cellpadding="0" cellspacing="0" border="0">
			<tr class="title">
				<td colspan="4">费用总计</td>
			</tr>
		</table>
		<SCRIPT type="text/javascript">
			/**********************************
			 * 
			 * @TODO	给table元素添加row
			 *
			 *********************************/
			function addRow(table){
				return $.browser.mozilla ? table.insertRow(-1) : table.insertRow();
			}
			/**********************************
			 * 
			 * @TODO	给row元素添加column
			 *
			 *********************************/
			 function addColumn(row){
				return $.browser.mozilla ? row.insertCell(-1) : row.insertCell();
			}
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				
				$.ajax({
					url:'getOrderPrintInfor.action',
					async:true,
					type:'post',
					data:{
						serialNumber:$("#serialNumber").val()
					},
					success:function(data){
						var f = data.data.finance;
						var tb = $("#finance").get(0);
						var sum = 0;
						for(var i = 0; i < f.length; i++){
							var row = addRow(tb);
							$(row).attr("financeType",f[i].financeType).attr("fid",f[i].id).mouseover(function(){
								$(this).css({"background-color":"#EEEEEE"});
							}).mouseout(function(){
								$(this).css({"background-color":"white"});
							});
							if(f[i].financeType == 100){
								$(row).attr("title","双击删除该优惠").css("cursor","pointer").bind("dblclick",function(){
									var id = $(this).attr("fid");
									$.dqgb.shadowLayer.showMask({mask:"正在删除优惠数据,请稍后片刻..."});
									$.ajax({
										url:'removeDiscount.action',
										type:'post',
										async:true,
										data:{
											id:id
										},
										success:function(data){
											if(data.RESULT == 'FAILED'){
												$.dqgb.shadowLayer.alert({content:data.REASON});
											}else{
												$.dqgb.shadowLayer.alert({content:"删除成功!",afterClose:function(){
													window.location.reload();
												}});
											}
										}
									});
								});
							}
							if(f[i].payDirection == 32){				//补
								sum += f[i].money;
								$(row).css("color","red");
							}else if(f[i].payDirection == 33){			//退
								sum -= f[i].money;
								$(row).css("color","blue");
							}
							var col = addColumn(row);
							if(f[i].payType == "1" && f[i].financeType != 26){
								$(col).css("color",'fuchsia').html("预付" + f[i].financeRemark);
							}else{
								$(col).css("color",'fuchsia').html(f[i].financeRemark);
							}
							
							col = addColumn(row);
							$(col).html(f[i].payDirectionRemark);
							
							col = addColumn(row);
							$(col).html("￥" + f[i].money);
							
							col = addColumn(row);
							if(f[i].payType == "1" && f[i].financeType != 26){
								$(col).css("color",'fuchsia').html(f[i].remark ? f[i].remark : ("预付" + f[i].financeRemark));
							}else{
								$(col).css("color",'fuchsia').html(f[i].remark ? f[i].remark : f[i].financeRemark);
							}
						}
						
						var tbSum = $("#summary").get(0);
						var row = addRow(tbSum);
						var col = addColumn(row);
						$(col).css("width","120px");
						col = addColumn(row);
						$(col).css("width","120px");
						col = addColumn(row);
						$(col).css("width","120px");
						col = addColumn(row);
						$(col).css("width","180px");
						if(sum >= 0){
							$(col).html("应补:&nbsp;￥" + sum);
						}else{
							$(col).html("应退:&nbsp;￥" + (0 - sum));
						}
					}
				});
				
				$("#settleFinance").click(function(){
					$.dqgb.shadowLayer.showMask({mask:'财务结算中,请稍后...'});
					$.ajax({
						url:'settleOrderForm.action',		//订单结算
						async:true,
						type:'post',
						data:{
							serialNumber:$("#serialNumber").val()
						},
						success:function(data){
							if(data.RESULT == 'FAILED'){
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}else{
								$.dqgb.shadowLayer.alert({content:"结算成功!",afterClose:function(){
									window.location = "gotoFinanceSettlePrintPage.action?serialNumber=" + encodeURI($("#serialNumber").val());
								}});
							}
						}
					});
				});
				
				//打折
				$("#discount").click(function(){
					$("#panel").dqgbBox({
						width:300,height:120,title:'请您输入优惠的金额',iconCls:'browse',
						closeBtn:true
					});
				});
				$("#discountMoney").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				
				$("#confirm").click(function(){
					if(!$.trim($("#discountMoney").val())){
						$.dqgb.globalMsg.showMsg("请输入您要优惠金额!");
						return;
					}
					$.dqgb.shadowLayer.showMask({mask:"正在提交优惠数据,请稍后片刻..."});
					$.ajax({
						url:'discountOrder.action',
						async:true,
						type:'post',
						data:{
							serialNumber:$("#serialNumber").val(),
							money:$.trim($("#discountMoney").val()),
							remark:$.trim($("#remark").val())
						},
						success:function(data){
							if(data.RESULT == 'FAILED'){
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}else{
								$.dqgb.shadowLayer.alert({content:"优惠成功!",afterClose:function(){
									window.location.reload();
								}});
							}
						}
					});
				});
			});
		</SCRIPT>
		<div id="panel" style="width:300px; background-color: white; display: none; text-align: center;">
			<table style="text-align: left;margin: 0px;padding: 0px; margin: 10px 10px;" border="0" id="dis">
				<tr><td align="left">优惠金额</td><td><input type="text" id="discountMoney" /></td></tr>
				<tr><td align="left">备注</td><td><input type="text" id="remark" /></td></tr>
			</table>
			<input type="button" value="确认" style="width:50px;" id="confirm"/>
		</div>
		<input type="button" value="优惠" style="width:50px; margin-top:40px;" id="discount"/>
		<input type="button" value="结算" style="width:50px; margin-top:40px;" id="settleFinance"/>
	</body> 
</html>