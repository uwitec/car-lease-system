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
		<title>打印订单</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		
		<style type="text/css">
			td{
				background-color: white;
				height:50px;line-height: 50px;
				font:16px 微软雅黑;
				padding-left:5px;
			}
			td label{
				background-color: white;
				height:50px;line-height: 50px;
				font:16px 微软雅黑;
				padding-left:5px;
			}
			#financeDetails td{
				padding-left:0px;
			}
		</style>
	</head>
	<body style="text-align: center;font-family: '微软雅黑'; width:800px; margin:auto;">
		
		<div class="smallLog" style="position: absolute; left: 0px;top: -17px;">
		</div>
	
		<h2>&nbsp;</h2>
		<h2>&nbsp;</h2>
		<h2 align="center" >车辆租赁预定单</h2>
		<h4 align="left" style="width:65%; float:left;">合同号：<label id='serialNumber'><s:property value="#session.serialNumber" /></label>(<label id="leaseModelRemark"></label>)</h4>
		<h4 align="right" style="width:35%; float: right;"><label id='statusRemark'></label></h4>
		<input type="button" value='打印' id="print" style="position: absolute; right:15px; top:3px; width:50px;"/>
		<script type="text/javascript">
			window.onafterprint = function(){
				$("#print").show();
				$("#statusRemark").show();
			};
			
			function printPage(){
				$("#print").hide();
				$("#statusRemark").hide();
				setTimeout(function(){
					window.print();
				},500); 
			}
			
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				$("#print").click(function(){
					//预填状态才修改状态
					if($("#statusRemark").attr("leaseRecordStatus") == '23'){
						$.dqgb.shadowLayer.confirm({content:'执行该操作以后该订单将不能再被修改,请确认是否要继续执行?',
							afterClose:function(){
								$.ajax({
									url:'changeOrderFormToBooking.action',					//修改订单状态为预定状态
									async:true,
									type:'post',
									data:{
										serialNumber:$("#serialNumber").html()
									},
									success:function(data){
										if(data.RESULT == 'FAILED'){
											$.dqgb.shadowLayer.alert({content:data.REASON});
										}else{
											loadData(function(){
												//打印单据
												printPage();	
											});
										}
									}
								});
							}
						});
					}else{
						//打印单据
						printPage();	
					}
				});
				loadData();
			});
			
			function loadData(callback){
				$.ajax({
					url:'getOrderPrintInfor.action',
					async:true,
					type:'post',
					data:{
						serialNumber:$("#serialNumber").html()
					},
					success:function(data){
						if(data.RESULT != 'FAILED'){
							var ci = data.data.customerInformation.data[0];		//客户资料
							var lri = data.data.data[0];						//订单资料
							
							
							if(ci){
								$("#customerName").html(ci.customerName).css("color",'fuchsia');
								$("#customerName2").html(ci.customerName).css("color",'fuchsia');
								$("#cus").html(ci.customerName);
								$("#customerIdCarNumber").html(ci.idCardNumber).css("color",'fuchsia');
								$("#address").html(ci.address);
								$("#mobilePhone").html(ci.mobilePhone);
								$("#companyName").html(ci.company);
								$("#companyAddress").html(ci.companyAddress);
								$("#companyPhone").html(ci.companyPhone);
							}else{
								$("#customerName").html(lri.customerNameRemark);
							}
							$("#carId").html(lri.carId).css("color",'fuchsia');
							$("#leaseWayRemark").html(lri.leaseWayRemark).css("color",'fuchsia');
							$("#dateBegin").css("color",'fuchsia').html(parseInt(lri.dateBegin.year + 1900) + "-" + parseInt(lri.dateBegin.month + 1) + 
												"-" + lri.dateBegin.date);
							$("#dateEnd").css("color",'fuchsia').html(parseInt(lri.dateEnd.year + 1900) + "-" + parseInt(lri.dateEnd.month + 1) + 
									"-" + lri.dateEnd.date);
							var status = "订单状态：" + lri.leaseRecordStatusRemark;
							if(lri.leaseRecordStatus == 23 || lri.leaseRecordStatus == 34){
								status += '(无效)';
							}else{
								status += '(有效)';
							}
							$("#driverId").html(lri.driverId);
							$("#driverName").html(lri.driverName);
							$("#contactor").html(lri.contactor);
							$("#contactorNumber").html(lri.contactorNumber);
							
							$("#statusRemark").html(status).css("color",'red').attr("leaseRecordStatus",lri.leaseRecordStatus);
							$("#leaseModelRemark").html("&nbsp;<font style='color:fuchsia;'>" + lri.leaseModelRemark + "</font>&nbsp;");
							
							var fi = data.data.finance;							//财务信息
							var tbf = $("#financeDetails").get(0);
							var len = tbf.rows.length;
							for(var i = 0; i < len - 1; i++){
								tbf.deleteRow(1);
							}
							for(var i = 0; i < fi.length; i++){
								var row = addRow(tbf);
								var col = addColumn(row);
								$(col).css("color",'fuchsia').html(parseInt(fi[i].payDate.year + 1900) + "-" + parseInt(fi[i].payDate.month + 1) + 
										"-" + parseInt(fi[i].payDate.date) + " " + 
										fi[i].payDate.hours + ":" + fi[i].payDate.minutes + ":" + fi[i].payDate.seconds);
								
								col = addColumn(row);
								if(fi[i].payType == "1" && fi[i].financeType != 26){
									$(col).css("color",'fuchsia').html("预付" + fi[i].financeRemark);
								}else{
									$(col).css("color",'fuchsia').html(fi[i].financeRemark);
								}
								

								col = addColumn(row);
								$(col).css("color",'fuchsia').html("￥" + fi[i].money);

								col = addColumn(row);
								if(fi[i].payType == "1" && fi[i].financeType != 26){
									$(col).css("color",'fuchsia').html(fi[i].remark ? fi[i].remark : ("预付" + fi[i].financeRemark));
								}else{
									$(col).css("color",'fuchsia').html(fi[i].remark ? fi[i].remark : fi[i].financeRemark);
								}
								
								
								col = addColumn(row);
								$(col).css("color",'fuchsia').html(fi[i].payDirectionRemark);
								
								col = addColumn(row);
								$(col).css("color",'fuchsia').html(fi[i].transactorName + "(" + fi[i].transactor + ")");
							}
							var carInf = data.data.carInformation[0];			//汽车资料
							$("#priceStandard").html("￥" + (lri.leaseModel == 52 ? (lri.price + "/天") : (lri.price + "/天/160公里"))).css("color",'fuchsia');
							$("#pricePerHour").html("￥" + (lri.pricePerHour) + "元/小时").css("color",'fuchsia');
							$("#pricePerKm").html("￥" + (lri.pricePerKm) + "元/公里").css("color",'fuchsia');
							$("#foregiftStandard").html("￥" + carInf.foregift + "/车").css("color",'fuchsia');
							$("#carType").css("color",'fuchsia').html(carInf.brandName + (carInf.category ? ("-" + carInf.category) : ""));
							$("#currentDepartmentName").html(carInf.departmentName);
							$("#engineNumber").html(carInf.engineNumber);
							$("#bodyNumber").html(carInf.bodyNumber);
							var _today = data.data.currentDate[0];
							$("#today").html(parseInt(_today.year + 1900) + "年" + parseInt(_today.month + 1) + 
									"月" + _today.date + "日" + 
									_today.hours + "时" + _today.minutes + "分" + _today.seconds + "秒");
							
							if($.isFunction(callback)){
								callback.call();
							}
						}else{
							$.dqgb.shadowLayer.alert({content:data.REASON});
						}
					}
				});
			}
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
		</script>
		
		<table cellpadding="0" cellspacing="1" border="0" bgcolor="#999999" width="100%" style="text-align: left; margin-top:15px;margin-bottom:20px; font-size:16px;">
			<tr>
				<td width="16%">承租方:</td><td colspan='2' width="34%"><label id="customerName"></label></td>
				<td width="16%">联系方式:</td><td colspan='2' width="34%"><label id="mobilePhone"></label></td>
			</tr>
			
			<tr>
				<td width="16%">联系地址:</td><td colspan='2' width="34%"><label id="address"></label></td>
				<td width="16%">身份证号:</td><td colspan='2' width="34%"><label id="customerIdCarNumber"></label></td>
			</tr>

			<tr>
				<td width="16%">驾驶员:</td><td colspan='2' width="34%"><label id="driverName"></label></td>
				<td width="16%">驾驶证号:</td><td colspan='2' width="34%"><label id="driverId"></label></td>
			</tr>

			<tr>
				<td width="16%">紧急联系人:</td><td colspan='2' width="34%"><label id="contactor"></label></td>
				<td width="16%">紧急联系方式:</td><td colspan='2' width="34%"><label id="contactorNumber"></label></td>
			</tr>

			<tr>
				<td width="16%">车型:</td><td colspan='2' width="34%"><label id="carType"></label></td>
				<td width="16%">取车日期:</td><td colspan='2' width="34%"><label></label></td>
			</tr>

			<tr>
				<td rowspan='2' width="16%">车牌号:</td><td colspan='2' rowspan='2' width="34%"><label id="carId"></label></td>
				<td width="16%">还车日期:</td><td colspan='2' width="34%"><label></label></td>
			</tr>
			<tr>
				<td width="16%">取车门店:</td><td colspan='2' width="34%"></td>
			</tr>
			<tr>
				<td width="16%">押金标准:</td><td colspan='2' width="34%"><label id="foregiftStandard"></label></td>
				<td width="16%">租赁方式:</td><td colspan='2' width="34%"><label id="leaseWayRemark"></label></td>
			</tr>
			<tr>
				<td width="16%">租金标准:</td>
											<td colspan='2' width="34%">
												<label id="priceStandard" style="float:right; margin-right: 5px;"></label>
											</td>
				<td width="16%">租期:</td><td colspan='2' width="34%"><label id="dateBegin"></label>&nbsp;至&nbsp;<label id="dateEnd"></label></td>
			</tr>
			
			<tr>
				<td width="16%" rowspan="2">超时计费:</td><td colspan='2' width="34%">
													<label style="float:right;" id="pricePerHour"></label><br>
											  </td>
				<td width="16%" rowspan="2">超驶计费:</td><td colspan='2' rowspan="2" width="34%"><label id="pricePerKm" ></label></td>
			</tr>
			<tr>
				<td colspan="2">
					<p style="clear: both;text-indent: 2em;font:16px 微软雅黑;margin: 5px;">
						备注：超时在6小时内按小时计算,超过6小时按天计算。
					</p>
				</td>
			</tr>
			
			<tr>
				<td colspan="6" style='color: fuchsia;text-align: center; padding: 0px;'><b>订单财务明细</b></td>
			</tr>
			<tr>
				<td colspan="6">
					<table id='financeDetails' width="100%" style="text-align: left; font-size:14px;">	
						<tr >
							<td style="width:150px;"><b>付款日期</b></td>
							<td style="width:100px;"><b>财务类型</b></td>
							<td style="width:100px;"><b>付款金额</b></td>
							<td style="width:190px;"><b>费用说明</b></td>
							<td style="width:100px;"><b>付款状况</b></td>
							<td style="width:150px;"><b>业务员</b></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="6">
					注意：<br>
					1、请确认以上内容，并确认及遵守我公司《汽车租赁合同书》中所有条款，文明行驶，爱护车辆，认真履行相关职责义务。<br>
					2、请您还车时不要忘记给油箱加满油，不低于93号无铅汽油。<br>
					3、当您需要在18:00时以后取车或还车，我公司将收取20元/次的服务费。<br>
					
					<table width="100%" style="text-align: left; font-size:14px;">
						<tr>
							<td width="60%">出租方：南宁万乘弘汽车租赁有限公司</td>
							<td width="40%">承租方：<label id="customerName2"></label></td>
						</tr>
						<tr>
							<td width="60%"><b>经办人：<s:property value='#session.CurrentUser.realName'/>(<s:property  value='#session.CurrentUser.userName'/>) </b></td>
							<td width="40%"><b>业务办理日期:<label style="margin-left: 10px;" id='today'></label></b></td>
						</tr>
					</table>
					                         
				</td>
			</tr>
		</table>
		
		<!-- <table width="100%" style="text-align: left; font-size:14px;">
			<tr>
				<td rowspan="3" colspan="1" width="100px;">
				<b><label style="margin-left: 20px;">承<br></br></label><label style="margin-left: 20px;">诺</label></b>
				</td>
				<td colspan="3" rowspan="3">
					<p style="margin:0px; padding: 0px; text-indent: 2em;">
					在行驶租赁车辆(<label>川A-9528</label>)期间，遵守交通规则、文明驾驶、安全驾驶、爱护车辆、履行《机动车辆保险条款》
					中规定的保险人的义务。出现交通意外时，即时报警、不私了、不逃逸。不得使用该车辆进行违法运输以及其它违法犯罪行为。认真执行
					《南宁万乘弘汽车租赁有限公司车辆租赁车辆使用规定》中的其它有关规定。并承担违反交通规则所发生的一切经济责任。
					</p>
				</td>
			</tr>
		</table> 
		<table width="100%" style="text-align: left; font-size:14px; margin-top:10px;">
			<tr>
				<td>
					1、承担方对本公司《汽车租赁合同书》中所有条款、《承担人须知》及本表所列项目确认无异议，
						并同意将<label style="text-decoration: underline; margin: 0px 10px; font-weight: bold;">身份证</label>(证件)
						和<label style="text-decoration: underline;  margin: 0px 10px; font-weight: bold;">押金</label>留做抵押。<br>
					2、请您在换车时不要忘记给车邮箱加满油，不低于93号无铅汽油。<br>
				</td>
			</tr>
		</table>
		
		<table width="100%" style="text-align: left; font-size:14px; margin-top:10px;">
			<tr>
				<td>
					<b>出租方：	南宁万乘弘汽车租赁有限公司</b>
				</td>
				<td>
					<b>承租方：<a id='cus'></a></b>
				</td>
			</tr>
			<tr>
				<td>
					<b>业务办理人：<s:property value='#session.CurrentUser.realName'/>(<s:property  value='#session.CurrentUser.userName'/>) </b>
				</td>
				<td>
					<b>业务办理日期:<label style="margin-left: 10px;" id='today'></label></b>
				</td>
			</tr>
		</table>-->
		
	</body> 
</html>