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
		<title>费用补填</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		
		<style type="text/css">
			#fee td{
				background-color: white;
				text-align:left;
				padding-left: 5px;
				height: 30px; line-height:30px;
			}
		</style>
	</head>
	<body style="text-align: center; font-size: 14px; font-family: '微软雅黑'">
		<input type="hidden" id="carId" value="<s:property value='#session.carId' />" />
		<div style="width:100%;text-align: left; margin-bottom: 20px;">订单号：<label id="orderId">${orderFormId}</label></div>
		<table id="fee" width="595px" border="0" cellpadding="0" cellspacing="1" bgcolor="#999999" style="text-align: left">
			<tr>
				<td width="160" style="background-color: #BFD4F0;font-weight: bold;">费用类别</td>
				<td width="160" style="background-color: #BFD4F0;font-weight: bold;">金额(圆)</td>
				<td width="275" style="background-color: #BFD4F0;font-weight: bold;">备注</td>
			</tr>
			<%-- <tr class="fee">
				<td>
					<select class="feeType" id="feeType">
					</select>
				</td>
				<td><input type="text" class="money" value='0'/></td>
				<td><input type="text" class="moneyRemark" style="width:265px;" /></td>
			</tr> --%>
		</table>
		<input type="button" value="追加费用" style="width:60px;margin-top:10px;" id="addFee"/>
		<input type="button" value="确认" style="width:60px;margin-top:10px;" id="supplementFinance"/>
		
		
		<script type="text/javascript">
			$(function(){
				
				$.dqgb.button.modifyButtonStyle();
				
				//获取费用类别
				$.ajax({
					url:'getCMFeeType.action',
					async:true,
					type:'post',
					data:{},
					success:function(data){
						var dt = data.data;
						for(var i = 0; i < dt.length; i++){
							$("<option value='" + dt[i].id + "'>" + dt[i].remark + "</option>").appendTo($(".feeType"));
						}
						$.ajax({
							url:'getCMFee.action',
							async:true,
							type:'post',
							data:{
								serialNumber:$("#orderId").html()
							},
							success:function(data){
								if(data.totalSize == 0){
									var row = $.dqgb.addRow($("#fee").get(0));
									$(row).addClass("fee");
									
									var col = $.dqgb.addColumn(row);
									var select = $("<select></select>").appendTo($(col)).addClass("feeType");
									for(var i = 0; i < dt.length; i++){
										$("<option value='" + dt[i].id + "'>" + dt[i].remark + "</option>").appendTo(select);
									}
									col = $.dqgb.addColumn(row);
									$("<input type='text' class='money' value='0'/>").appendTo($(col)).keyup(function(){					//数字控制
										$(this).val($(this).val().replace(/\D/g,''));
									});
									
									col = $.dqgb.addColumn(row);
									$("<input type='text' class='moneyRemark' style='width:265px;'/>").appendTo($(col));
								}else{
									for(var i = 0; i < data.totalSize; i++){
										var row = $.dqgb.addRow($("#fee").get(0));
										$(row).addClass("fee");
										var col = $.dqgb.addColumn(row);
										var select = $("<select></select>").appendTo($(col)).addClass("feeType");
										var index = 0;
										for(var j = 0; j < dt.length; j++){
											$("<option value='" + dt[j].id + "'>" + dt[j].remark + "</option>").appendTo(select);
											if(dt[j].id == data.data[i].financeType){
												index = j;
											}
										}
										select.get(0).selectedIndex = index;
										col = $.dqgb.addColumn(row);
										$("<input type='text' class='money' value='" + data.data[i].money + "'/>").appendTo($(col)).keyup(function(){					//数字控制
											$(this).val($(this).val().replace(/\D/g,''));
										});
										
										col = $.dqgb.addColumn(row);
										$("<input type='text' class='moneyRemark' style='width:265px;' value='" + data.data[i].remark + "'/>").appendTo($(col));
									}
								}
							}
						});
					}
				});
				//添加列
				$("#addFee").click(function(){
					var row = $.dqgb.addRow($("#fee").get(0));
					$(row).addClass("fee");
					var col = $.dqgb.addColumn(row);
					var select = $("<select></select>").appendTo($(col)).addClass("feeType");
					var options = $("#fee select:first").get(0).options;
					for(var i = 0; i < options.length; i++){
						$("<option value='" + options[i].value + "'>" + options[i].text + "</option>").appendTo(select);
					}
					col = $.dqgb.addColumn(row);
					$("<input type='text' class='money' value='0'/>").appendTo($(col)).keyup(function(){					//数字控制
						$(this).val($(this).val().replace(/\D/g,''));
					});
					
					col = $.dqgb.addColumn(row);
					$("<input type='text' class='moneyRemark' style='width:265px;'/>").appendTo($(col));
				});
				$(".money").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				
				//追加费用
				$("#supplementFinance").click(function(){
					var fees = $("#fee .fee");
					var input = "[";
					for(var i = 0; i < fees.length; i++){
						var select = $(fees[i]).children("td").children(".feeType");
						input += "{financeType:'" + select.get(0).options[select.get(0).selectedIndex].value + "',";
						var money = $(fees[i]).children("td").children(".money");
						input += "money:'" + ($.trim(money.val()) ? $.trim(money.val()) : 0) + "',";
						var moneyRemark = $(fees[i]).children("td").children(".moneyRemark");
						input += "remark:'" + ($.trim(moneyRemark.val()) ? $.trim(moneyRemark.val()) : "") + "'}";
						if(i != fees.length - 1){
							input += ",";
						}
						//其它费用
						if(select.get(0).options[select.get(0).selectedIndex].value == 29){
							if($.trim(money.val()) && parseInt($.trim(money.val())) != 0 && !$.trim(moneyRemark.val())){
								$.dqgb.shadowLayer.alert({content:'"请填写其它费用的收费原因!"'});
								return;
							}
						}
					}
					input += "]";
					$.dqgb.shadowLayer.confirm({
						content:'确认所有财务信息已经备注完毕?',
						afterClose:function(){
							$.dqgb.shadowLayer.showMask({mask:'正在补填财务信息,请稍后.....'});
							$.ajax({
								url:'supplementFinance.action',
								async:true,
								type:'post',
								data:{
									serialNumber:$("#orderId").html(),
									input : input
								},
								success:function(data){
									if(data.RESULT != 'FAILED'){
										$.dqgb.shadowLayer.hideMask();
										$.dqgb.globalMsg.showMsg("费用补填成功!");
									}else{
										$.dqgb.shadowLayer.alert({content:data.REASON});
									}
								}
							});
						}
					});
				});
			});
		</script>
	</body>
</html>