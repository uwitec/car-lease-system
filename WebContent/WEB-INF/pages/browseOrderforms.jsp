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
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.tree.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.navigation.2.0.js"></script>
		
	</head>
	<body style='font-size:14px;'>
		<div style="position: absolute;left:0px; top:0px; height:398px; width:848px; overflow: hidden;">
			<div id="user"  style="position: absolute;left:-8px; top:-8px;"></div>
		</div>
		<div id="nav"></div>
		<input type="hidden" id="carId" value="<s:property value='#session.carId'/>"/>
		<input type="hidden" id="currentUserName" value='<s:property value="#session.CurrentUser.userName"/>'/>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				var grid = $.dqgb.grid.generateGrid({
					render:"user",
					width:864,
					height:414,
					iconCls:'iconCls',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
							  	{text:'新增预约',iconCls:'addRole',
							  		click:function(){
										window.location = 'gotoAddOrderFormPage.action?carId=' + encodeURI($("#carId").val());
									}
								},"-",{text:'修改预约',iconCls:'modify',
							  		click:function(){
							  			var row = grid.getSelectedRow();
										if(row.length == 0){
											$.dqgb.globalMsg.showMsg('请指定您要修改的订单信息！');
											return;
										}
										window.location = 'gotoModifyOrderFormPage.action?serialNumber=' + encodeURI(row[0].serialNumber);
									}
								},'-',{
									text:'预览订单',iconCls:'browse',click:function(){
										var row = grid.getSelectedRow();
										if(row.length == 0){
											$.dqgb.globalMsg.showMsg('请指定您要预览的订单信息！');
											return;
										}
										window.location = "gotoPrintOrderForm.action?serialNumber=" + encodeURI(row[0].serialNumber);
									}
								}
							]
						}).body
					},
					bbar:{
						url:"getBookRecord.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						extraParam:{
							carId:$("#carId").val()
						},
						showDetails:true,
						totalCount:0,
						pageSize:10
					},
					singleMode:true,
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									new $.dqgb.grid.CheckRow(),
									{title:'承租人',width:100,alias:'customerName',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value){
												return true;
											}
											obj.html(gridObj.options.store[row].customerNameRemark);
											return false;
										}
									},
									{title:'承租人身份证',width:160,alias:'customerIdCardNumber'},
									{title:'订单状态',width:100,alias:'leaseStatusRemark'},//leaseRecordStatus
									{title:'租赁模式',width:100,alias:'leaseModelRemark'},
									{title:'起租日期',width:150,alias:'dateBegin',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date));
											return false;
										}
									},
									{title:'截止日期',width:150,alias:'dateEnd',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date));
											return false;
										}
									},
									{title:'续租截止日期',width:150,alias:'dateRelet',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(!value || !value.year){
												return false;
											}
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date));
											return false;
										}
									},
									{title:'取消预约',width:100,alias:'delete',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var lr = gridObj.options.store[row];
											if(lr.transactor == $("#currentUserName").val() && lr.leaseRecordStatus == '23'){
												var icon = $("<div title='取消预约' class='deleteRole'></div>")
												.appendTo(obj).click(function(){
													$.dqgb.shadowLayer.showMask({mask:'正在取消预约,请稍后....'});
													var lr = gridObj.options.store[row];
													$.ajax({
														url:'deleteLeaseRecord.action',
														async:true,
														type:'post',
														data:{
															serialNumber : lr.serialNumber
														},
														success:function(data){
															$.dqgb.shadowLayer.hideMask();
															if(data.RESULT == 'FAILED'){
																$.dqgb.globalMsg.showMsg(data.REASON);
															}else{
																$.dqgb.globalMsg.showMsg("成功取消预约单！");
																grid.refresh();
															}
														}
													});
												}).css({
		                                        	"margin-left":"0px",
		                                        	"margin-top":"4px",
		                                        	"cursor":"pointer",
		                                        	"background-repeat":"no-repeat",
		                                        	"width":"16px",
		                                        	"height":"16px"
												});
											}
											return false;			//客户化
										}
									},
									{title:'业务员',width:100,alias:'transactorName'},
									{title:'预约日期',width:150,alias:'orderDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds);
											return false;
										}
									},
									{title:'订单编号',width:350,alias:'serialNumber'}
									
								]
				});
			});
		</SCRIPT>
		
	</body> 
</html>