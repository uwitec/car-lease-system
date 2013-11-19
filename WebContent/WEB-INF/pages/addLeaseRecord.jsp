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
		<title>订单管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/jquery-ui-1.8.12.custom.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-ui-1.8.12.custom.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.query.js"></script>
		<style type="text/css">
			.text{
				padding-left:10px; text-align: left; font-size:14px; font-family: '微软雅黑'
			}
		</style>
	</head>
	<body>
		<h3 align="center">南宁万乘弘汽车租赁有限公司车辆租赁登记表</h3>
		<input type="hidden" value='<s:property value="#session.carId" />' id='carId'/>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				if(!$("#carId").val()){
					$.dqgb.globalMsg.showMsg("没有找到相关车辆信息,可能是网络延迟导致,请刷新重试.");
				}else{
					$.ajax({
						url:'getCarByCondition.action',
						type:'post',
						async:true,
						data:{
							pageIndex:0,
							pageSize:10,
							carId:$("#carId").val()
						},
						success:function(data){
							var car = "";
							for(var i = 0; i < data.data.totalSize; i++){
								if(data.data.data[i].carId == $("#carId").val()){
									car = data.data.data[i];
									break;
								}
							}
							if(!car){
								$.dqgb.globalMsg.showMsg("没有找到相关车辆信息,可能是网络延迟导致,请刷新重试.");
								return;
							}
							$("#carCategory").html(car.brandName + (car.category ? ("-" + car.category):("")));
							$("#departmentName").html(car.departmentName);
							$("#engineNumber").html(car.engineNumber);
							$("#bodyNumber").html(car.bodyNumber);
							$("#defaultPrice").html("￥" + car.price + "/天").attr("defaultPrice",car.price);
							$("#defaultForegift").html("￥" + car.foregift);
							$("#foregift").val(car.foregift);
							$("#pricePerHour").html("￥" + car.pricePerHour + "/小时").attr("pricePerHour",car.pricePerHour);
							$("#pricePerKm").html("￥" + car.pricePerKm + "/公里").attr("pricePerKm",car.pricePerKm);
							$.ajax({
								url:'getLeaseModel.action',
								type:'post',
								async:true,
								data:{},
								success:function(data){
									var dt = data.data;
									$("#leaseModel").get(0).options.length = 0;
									for(var i = 0; i < dt.length; i++){
										var option = $("<option value='" + dt[i].id + "'>" + dt[i].remark + "</option>").appendTo($("#leaseModel"));
									}
									$("#leaseModel").change(function(){
										if($("#leaseModel").get(0).options[$("#leaseModel").get(0).selectedIndex].value == "52"){
											$("#pricePerKm").html("￥" + car.pricePerKm + "/公里").attr("pricePerKm",car.pricePerKm);
											$("#pricePerHour").html("￥" + car.pricePerHour + "/小时").attr("pricePerHour",car.pricePerHour);
											$("#defaultPrice").html("￥" + car.price + "/天").attr("defaultPrice",car.price);
										}else{
											$("#pricePerKm").html("￥" + car.pricePerKm + "/公里").attr("pricePerKm",car.pricePerKm);
											$("#pricePerHour").html("￥" + car.pricePerHourRM + "/小时").attr("pricePerHour",car.pricePerHourRM);
											$("#defaultPrice").html("￥" + car.priceRM + "/天").attr("defaultPrice",car.priceRM);
										}
										if($("#dateBegin").val() && $("#dateEnd").val()){
											getPrice($("#dateBegin").val(),$("#dateEnd").val());
										}
									});
								}
							});
						}
					});
				}
				$.ajax({
					url:'getLeaseWay.action',
					type:'post',
					async:true,
					data:{},
					success:function(data){
						var dt = data.data;
						$("#leaseWay").get(0).options.length = 0;
						for(var i = 0; i < dt.length; i++){
							var option = $("<option value='" + dt[i].id + "'>" + dt[i].remark + "</option>").appendTo($("#leaseWay"));
						}
					}
				});
				
				$("#idCardNumber").query({
					url:'queryCustomer.action',
					miniQueryLength:4,									//输入4个字符以后开始查询
					width:200,
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
				$("#dateBegin").datepicker({
												dateFormat: 'yy-mm-dd',
												monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/'],
												onClose:function(text,inst){
													if($("#dateEnd").val()){
														var ret = getPrice(text,$("#dateEnd").val());
														if(0 == ret){
															$("#dateBegin").val("");
															$.dqgb.shadowLayer.alert({content:'起始日期和截止日期不能为同一天!'});
														}
													}
												}
											});
				$("#dateEnd").datepicker({
												dateFormat: 'yy-mm-dd',
												monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/'],
												onClose :function(text,inst){
													if($("#dateBegin").val()){
														var ret = getPrice($("#dateBegin").val(),text);
														if(0 == ret){
															$("#dateEnd").val("");
															$.dqgb.shadowLayer.alert({content:'起始日期和截止日期不能为同一天!'});
														}
													}
												}
											});
				$("#price").keyup(function(){						//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#foregift").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#bookingFee").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#contactorNumber").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#driverId").keyup(function(){
					this.value = this.value.replace(/[\W]/g,''); 
				});
				//下订单
				$("#save").click(function(){
					if($.trim($("#dateEnd").val()) == ""){
						$.dqgb.globalMsg.showMsg("租期截止日期不能为空!");
						return;
					}
					if($.trim($("#dateBegin").val()) == ""){
						$.dqgb.globalMsg.showMsg("租期起始日期不能为空!");
						return;
					}
					if(!afterToday($.trim($("#dateBegin").val()))){
						$.dqgb.globalMsg.showMsg("租期起始日期不能小于当前日期!");
						return;
					} 
					if(!$.trim($("#price").val()) || parseInt($.trim($("#price").val())) < 0){
						$.dqgb.globalMsg.showMsg("租金金额数据非法,请检查!");
						return;
					}
					if(!$.trim($("#foregift").val()) || parseInt($.trim($("#foregift").val())) < 0){
						$.dqgb.globalMsg.showMsg("押金金额数据非法,请检查!");
						return;
					}
					if(!$.trim($("#bookingFee").val()) || parseInt($.trim($("#bookingFee").val())) < 0){
						$.dqgb.globalMsg.showMsg("预约金数据非法,请检查!");
						return;
					}
					/* if(!$.trim($("#idCardNumber").val())){
						$.dqgb.globalMsg.showMsg("请填写承租方身份证号码!");
						return;
					} */
					
					//下定单
					$.dqgb.shadowLayer.showMask({mask:'正在提交数据,请稍后.....'});
					$.ajax({
						url:'AddOrderForm.action',			//预添加订单
						async:true,
						type:'post',
						data:{
							'leaseRecords.carId':$("#carId").val(),
							'leaseRecords.customerIdCardNumber':$("#idCardNumber").val(),
							'leaseRecords.customerNameRemark':$.trim($("#customerName").val()),
							'leaseRecords.dateBegin':$("#dateBegin").val(),
							'leaseRecords.dateEnd':$("#dateEnd").val(),
							
							'leaseRecords.pricePerKm':$("#pricePerKm").attr("pricePerKm"),
							'leaseRecords.pricePerHour':$("#pricePerHour").attr("pricePerHour"),
							'leaseRecords.Price':$("#defaultPrice").attr("defaultPrice"),

							'leaseRecords.driverName':$("#driverName").val(),
							'leaseRecords.driverId':$("#driverId").val(),
							'leaseRecords.contactor':$("#contactor").val(),
							'leaseRecords.contactorNumber':$("#contactorNumber").val(),
							
							'leaseRecords.leaseWay':$("#leaseWay").get(0).options[$("#leaseWay").get(0).selectedIndex].value,
							'leaseRecords.leaseModel':$("#leaseModel").get(0).options[$("#leaseModel").get(0).selectedIndex].value,
							price:$("#price").val(),
							priceRemark:$("#priceRemark").val() ? $("#priceRemark").val() : "",
							foregift:$("#foregift").val(),
							foregiftRemark:$("#foregiftRemark").val() ? $("#foregiftRemark").val() : "",
							bookingFee:$("#bookingFee").val(),
							bookingFeeRemark:$("#bookingFeeRemark").val() ? $("#bookingFeeRemark").val() : ""
						},
						success:function(data){
							if(data.RESULT != 'FAILED'){
								$.dqgb.shadowLayer.alert({content:'预定成功!',afterClose:function(){
									window.location = 'gotoBrowseOrderFormsPages.action?carId=' + encodeURI($("#carId").val());
								}});
							}else{
								$.dqgb.shadowLayer.hideMask();
								$.dqgb.globalMsg.showMsg(data.REASON);
							}
						}
					});
				});
			});
			/**
			* @TODO	计算租金
			**/
			function getPrice(begin,end){
				try{
					var d1 = new Date(begin.split("-")[0],begin.split("-")[1] - 1,begin.split("-")[2]);
					var d2 = new Date(end.split("-")[0],end.split("-")[1] - 1,end.split("-")[2]);
					var dis = d2.getTime() - d1.getTime(); 
					dis = dis/1000/60/60/24;
					if(0 == dis){
						return 0;
						dis = 1;
					}
					$("#price").val($("#defaultPrice").attr("defaultPrice") * (dis));
				}catch(e){
					$.dqgb.globalMsg.showMsg(e.message);
				}
				return 1;
			}
			/**
			* @TODO	比较begin日期是否在今天以后
			**/
			function afterToday(begin){
				var d1 = new Date(begin.split("-")[0],begin.split("-")[1] - 1,begin.split("-")[2]);
				var today = new Date();
				var d2 = new Date(today.getYear(),today.getMonth(),today.getDate());
				return d1.getTime() >= d2.getTime() ? true : false;
				
			}
		</SCRIPT>
		
		<table border="0px" style="margin: auto;">
			<tr>
				<td class='text'>车牌号:</td>
				<td class='text'><s:property value="#session.carId" /></td>
				
				<td class='text'>车型:</td>
				<td class='text'><label id='carCategory'></label></td>
			</tr>
			<tr>
				<td class='text'>发动机编号:</td>
				<td class='text'><label id='engineNumber'></label></td>
				
				<td class='text'>车架号:</td>
				<td class='text'><label id='bodyNumber'></label></td>
			</tr>
			<tr>
				<td class='text'>租金单价:</td>
				<td class='text'><label id='defaultPrice'></label></td>
				
				<td class='text'>押金:</td>
				<td class='text'><label id='defaultForegift'></label></td>
			</tr>
			<tr>
				<td class='text'>超时费:</td>
				<td class='text'><label id='pricePerHour'></label></td>
				<td class='text'>当前驻场信息:</td>
				<td class='text'><label id='departmentName'></label></td>
			</tr>
			<tr>
				<td class='text'>超驶费:</td>
				<td class='text' colspan="3"><label id="pricePerKm"></label></td>
			</tr>
			<tr>
				<td class='text'>计费模式:</td>
				<td class='text'><select id='leaseModel' style='width:155px'></select></td>
				<td class='text'>租赁方式:</td>
				<td class='text'><select id='leaseWay' style='width:155px'></select></td>
			</tr>
			<tr>
				<td class='text'>驾驶员:</td>
				<td class='text'><input type="text" id="driverName" maxlength="20" /></td>
				<td class='text'>驾驶证号:</td>
				<td class='text'><input type="text" id="driverId"  maxlength="50"/></td>
			</tr>
			<tr>
				<td class='text'>紧急联系人:</td>
				<td class='text'><input type="text" id="contactor"  maxlength="20"/></td>
				<td class='text'>紧急联系方式:</td>
				<td class='text'><input type="text" id="contactorNumber"  maxlength="20"/></td>
			</tr>
			<tr>
				<td class='text'>承租方身份证:</td>
				<td class='text'><input type="text" id="idCardNumber" /></td>
				<td class='text'>客户备注:</td>
				<td class='text'><input type="text" id="customerName" /></td>
			</tr>
			<tr>
				<td class='text'>租期起始日期:</td>
				<td class='text'><input type='text' id='dateBegin'/></td>
				
				<td class='text'>租期截止日期:</td>
				<td class='text'><input type='text' id='dateEnd'/></td>
			</tr>
			<tr>
				<td class='text' style='color: orange;'>预约费:</td>
				<td class='text'><input type='text' value='0' id='bookingFee' /></td>
				
				<td class='text'>备注:</td>
				<td class='text'><input type='text' id='bookingFeeRemark'/></td>
			</tr>
			<tr>
				<td class='text' style='color: orange;'>预收租金:</td>
				<td class='text'><input type='text' id='price' value='0'/></td>
				<td class='text'>备注:</td>
				<td class='text'><input type='text' id='priceRemark' /></td>
			</tr>
			<tr>
				<td class='text' style='color: orange;'>押金:</td>
				<td class='text'><input type='text' id='foregift'/></td>
				<td class='text'>备注:</td>
				<td class='text'><input type='text' id='foregiftRemark'/></td>
			</tr>
			
		</table>
		<div style="text-align: center; margin-top: 15px;"><input type="button" value="确定" style="width:50px" id="save"/></div>
	</body> 
</html>