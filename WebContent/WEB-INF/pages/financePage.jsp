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
		<title>财务结算单</title>
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
		</style>
	</head>
	
	<body style="text-align: center;font-family: '微软雅黑'; width:800px; margin:auto;">
	
		<div class="smallLog" style="position: absolute; left: 0px;top: -17px;">
		</div>
		<h2>&nbsp;</h2>
		<h2>&nbsp;</h2>
		<h1 align="center" >结算单</h1>
		<h3 align="left" style="width:65%; float:left;">合同号：<label id='serialNumber'><s:property value="#session.serialNumber" /></label>(<label id="leaseModelRemark"></label>)</h3>
		<h3 align="right" style="width:35%; float: right;"><label id='statusRemark'></label></h3>
		<input type="button" value='打印' id="print" style="position: absolute; right:15px; top:3px; width:50px;"/>
		<script type="text/javascript">
			window.onafterprint = function(){
				$("#print").show();
			};
			
			function printPage(){
				$("#print").hide();
				window.print();
			}
			
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				$("#print").click(function(){
					printPage();	
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
							var rr = data.data.RunRecord;				//出行记录
							$("#FetchOrgName").html(rr.FetchOrgName);
							$("#GivebackOrgName").html(rr.GivebackOrgName);
							$("#fetchDate").html(parseInt(rr.dateBegin.year + 1900) + "-" + parseInt(rr.dateBegin.month + 1) + 
									"-" + rr.dateBegin.date + " " + rr.dateBegin.hours + ":" + rr.dateBegin.minutes + ":" + rr.dateBegin.seconds);
							$("#givebackDate").html(parseInt(rr.dateEnd.year + 1900) + "-" + parseInt(rr.dateEnd.month + 1) + 
									"-" + rr.dateEnd.date + " " + rr.dateEnd.hours + ":" + rr.dateEnd.minutes + ":" + rr.dateEnd.seconds);
							
							var ci = data.data.customerInformation.data[0];		//客户资料
							$("#customerName").html(ci.customerName).css("color",'fuchsia');
							$("#customerName2").html(ci.customerName).css("color",'fuchsia');
							$("#cus").html(ci.customerName);
							$("#customerIdCarNumber").html(ci.idCardNumber).css("color",'fuchsia');
							$("#address").html(ci.address);
							$("#mobilePhone").html(ci.mobilePhone);
							$("#companyName").html(ci.company);
							$("#companyAddress").html(ci.companyAddress);
							$("#companyPhone").html(ci.companyPhone);
							
							var lri = data.data.data[0];						//订单资料
							$("#carId").html(lri.carId).css("color",'fuchsia');
							$("#leaseWayRemark").html(lri.leaseWayRemark).css("color",'fuchsia');
							$("#dateBegin").css("color",'fuchsia').html(parseInt(lri.dateBegin.year + 1900) + "-" + parseInt(lri.dateBegin.month + 1) + 
												"-" + lri.dateBegin.date);
							
							var de = (lri.dateRelet && lri.dateRelet.year) ? lri.dateRelet : lri.dateEnd;
							$("#dateEnd").css("color",'fuchsia').html(parseInt(de.year + 1900) + "-" + parseInt(de.month + 1) + 
									"-" + de.date);
							var status = "订单状态：" + lri.leaseRecordStatusRemark;
							if(lri.leaseRecordStatus == 23 || lri.leaseRecordStatus == 34){
								status += '(无效)';
							}else{
								status += '(有效)';
							}
							$("#statusRemark").html(status).css("color",'red').attr("leaseRecordStatus",lri.leaseRecordStatus);
							$("#leaseModelRemark").html("&nbsp;<font style='color:fuchsia;'>" + lri.leaseModelRemark + "</font>&nbsp;");
							
							var fi = data.data.finance;							//财务信息
							
							var ppdt = $("#prePayDetails").get(0);				//预付明细表
							var odt = $("#otherDetails").get(0);				//其它明细
							
							var ppt = 0;			//预收费	
							var discount = 0;		//优惠
							var acp = 0;			//实际租金
							var ot = 0;				//其它总计
							var discountRemark;
							for(var i = 0; i < fi.length; i++){
								if(fi[i].payType == 1){							//统计预收费
									var row = addRow(ppdt);
									var col = addColumn(row);
									if(fi[i].financeType != 26){
										$(col).css("color",'fuchsia').html("预付" + fi[i].financeRemark);
									}else{
										$(col).css("color",'fuchsia').html(fi[i].financeRemark);
									}
									
									col = addColumn(row);
									$(col).css("color",'fuchsia').html("￥" + fi[i].money);
									col = addColumn(row);
									$(col).css("color",'fuchsia').html(parseInt(fi[i].payDate.year + 1900) + "-" + parseInt(fi[i].payDate.month + 1) + 
											"-" + parseInt(fi[i].payDate.date) + " " + 
											fi[i].payDate.hours + ":" + fi[i].payDate.minutes + ":" + fi[i].payDate.seconds);
									ppt += fi[i].money;
								}
								if(fi[i].financeType == 24){					//统计实际租金
									if(fi[i].payDirection == 31){
										acp += fi[i].money;
									}else if(fi[i].payDirection == 30){
										acp -= fi[i].money;
									}
								}	
								if(fi[i].financeType == 100){
									discount += fi[i].money;
									discountRemark = fi[i].remark;
								}
								if(fi[i].financeType != 24 && fi[i].financeType != 25 && fi[i].financeType != 26 && fi[i].financeType != 100){		//其它费用
									ot += fi[i].money; 
									var row = addRow(odt);
									var col = addColumn(row);
									$(col).css("color",'fuchsia').html(fi[i].financeRemark);
									col = addColumn(row);
									$(col).css("color",'fuchsia').html("￥" + fi[i].money);
									col = addColumn(row);
									$(col).css("color",'fuchsia').html(parseInt(fi[i].payDate.year + 1900) + "-" + parseInt(fi[i].payDate.month + 1) + 
											"-" + parseInt(fi[i].payDate.date) + " " + 
											fi[i].payDate.hours + ":" + fi[i].payDate.minutes + ":" + fi[i].payDate.seconds);
									col = addColumn(row);
									$(col).css("color",'fuchsia').html(fi[i].remark ? fi[i].remark : fi[i].financeRemark);
								}
							}
							if($("#otherDetails").get(0).rows.length == 1){
								$("#otherDetails").empty();
							}
							$("#paymentTotal").html("￥" + acp + ".00");
							$("#otherTotal").html("￥" + ot + ".00");
							$("#prePayTotal").html("￥" + ppt + ".00");
							$("#paymentDescription").html("实际租期&nbsp;" + $("#fetchDate").html() + "&nbsp;到&nbsp;" + 
												$("#givebackDate").html() + "，共产生租金" + $("#paymentTotal").html());
							if(discount){
								$("#paymentDescription").html($("#paymentDescription").html() + "<p>" + "优惠&nbsp;￥" + discount + ".00(" + 
									(discountRemark ? discountRemark : '优惠') +	")<p>总计：￥" + (acp - discount) + ".00");
								$("#paymentTotal").html("￥" + (acp - discount) + ".00");
							}
							
							var dist = ppt - ot - acp + discount;
							$("#financeSum").html((dist > 0 ? "应退" : "<font style='color:red'>应补</font>") + "￥" + parseInt(Math.abs(dist)) + ".00");
							
							
							
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
							$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
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
		
		<table cellpadding="0" cellspacing="1" border="0" bgcolor="#999999" width="100%" style="text-align: left; margin-top:15px 0px; ">
			<tr>
				<td width="16%">承租方:</td><td colspan='2' width="34%"><label id="customerName"></label></td>
				<td width="16%" >租金明细:</td><td colspan='2' width="34%">总计：<label id="paymentTotal"></label></td>
			</tr>
			<tr>
				<td width="16%">联系方式:</td><td colspan='2' width="34%"><label id="mobilePhone"></label></td>
				<td width="16%" colspan='3' rowspan="4"><p id='paymentDescription' style="text-indent: 2em; margin: 5px;"></p></td>
			</tr>
			<tr>
				<td width="16%">车型:</td><td colspan='2' width="34%"><label id="carType"></label></td>
			</tr>
			<tr>
				<td width="16%">车牌号:</td><td colspan='2'width="34%"><label id="carId"></label></td>
				
			</tr>
			<tr>
				<td width="16%">取车日期:</td><td colspan='2' width="34%"><label id="fetchDate"></label></td>
			</tr>
			<tr>
				<td width="16%">还车日期:</td><td colspan='2' width="34%"><label id="givebackDate"></label></td>
				<td width="16%" >其它费用总计：</td><td colspan='2' width="34%"><label id="otherTotal"></label></td>
			</tr>
			<tr>
				<td width="16%">取车门店:</td><td colspan='2' width="34%"><label id="FetchOrgName"></label></td>
				<td width="16%" colspan='3' rowspan="8" style="text-align: left; vertical-align: top;">
					<table id="otherDetails" width="100%" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td>财务类别</td><td>金额</td><td>付费日期</td><td>备注</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="16%">还车门店:</td><td colspan='2' width="34%"><label id="GivebackOrgName"></label></td>
			</tr>
			<tr>
				<td width="16%">租期:</td><td colspan='2' width="34%"><label id="dateBegin"></label>&nbsp;至&nbsp;<label id="dateEnd"></label></td>
			</tr>
			<tr>
				<td width="16%">租金标准:</td>
				<td colspan='2' width="34%">
					<label id="priceStandard" style="float:right; margin-right: 5px;"></label>
				</td>
			</tr>
			<tr>
				<td width="16%" rowspan="2">超时计费:</td>
				<td colspan='2' width="34%">
					<label style="float:right; margin-right: 5px;" id="pricePerHour"></label><br>
			  	</td>
			</tr>
			<tr>
				<td colspan="2">
					<p style="clear: both;text-indent: 2em;font:14px 微软雅黑;margin: 5px;">
						备注：超时在6小时内按小时计算,超过6小时按天计算。
					</p>
				</td>
			</tr>
			<tr>
				<td width="16%">超驶计费:</td>
				<td colspan='2' width="34%">
					<label style="float:right; margin-right: 5px;" id="pricePerKm" ></label>
			  	</td>
			</tr>
			<tr>
				<td width="16%">押金标准:</td><td colspan='2' width="34%"><label id="foregiftStandard"></label></td>
			</tr>
			<tr>
				<td colspan="1"><b>预收费明细:</b></td><td colspan='2'><b>总计：<label id="prePayTotal"></label></b></td>
				<td colspan="3"><b>财务总计</b></td>
			</tr>
			<tr>
				<td colspan="3">
					<table id="prePayDetails" width="100%" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td>财务类别</td><td>金额</td><td>付费日期</td>
						</tr>
					</table>
				</td>
				<td colspan="3">
					<p style="text-indent: 2em; margin: 5px;" id="financeSum"></p>
				</td>
			</tr>
			<tr>
				<td colspan="6">
					<table width="100%" style="text-align: left;">
						<tr>
							<td><b>出租方：南宁万乘弘汽车租赁有限公司</b></td>
						</tr>
						<tr>	
							<td><b>承租方：</b><label id="customerName2"></label></td>
						</tr>
						<tr>	
							<td><b>业务员：<s:property value="session.CurrentUser.realName" />(<s:property value="session.CurrentUser.userName" />)</b></td>
						</tr>
						<tr>
							<td><b>日期:<label style="margin-left: 10px;" id='today'></label></b></td>
						</tr>
					</table>
					                         
				</td>
			</tr>
		</table>
		<p style="margin:30px 0px;"></p>
	</body> 
</html>