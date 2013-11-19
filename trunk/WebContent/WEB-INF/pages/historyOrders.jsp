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
		<title>历史订单</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<style>
			.gridIcon{
				margin-left:0px;
               	margin-top:4px;
               	background-repeat:no-repeat;
               	width:16px;
               	height:16px;
			}
		</style>
	</head>
	<body>
		<div style="position: absolute;left:0px; top:0px; height:398px; width:798px; overflow: hidden;">
			<div id="user"  style="position: absolute;left:-8px; top:-8px;"></div>
		</div>
		<input type="hidden" id="idCardNumber" value="<s:property value='#session.idCardNumber'/>"/>
		
		<SCRIPT type="text/javascript">
			$(function(){
				//=============
				var grid = $.dqgb.grid.generateGrid({
					render:"user",
					width:814,
					height:414,
					iconCls:'iconCls',
					bbar:{
						url:"getHistoryOrderByCustomer.action",
						root:'data.data',							//返回的数据对象的名字
						extraParam:{
							idCardNumber:$("#idCardNumber").val()
						},
						total:'data.totalSize',
						showDetails:true,
						totalCount:0,
						pageSize:10
					},
					singleMode:true,							//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									{title:'车牌号',width:120,alias:'carId'},
									{title:'承租人',width:100,alias:'customerName'},
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
									{title:'取车日期',width:180,alias:'fetchCarDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(!value || !value.year){
												return false;
											}
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds)
													.css("color","fuchsia");
											return false;
										}
									},
									{title:'还车日期',width:180,alias:'giveBackCarDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(!value || !value.year){
												return false;
											}
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds)
													.css("color","fuchsia");
											return false;
										}
									},
									{title:'租赁模式',width:100,alias:'leaseModelRemark'},
									{title:'承租人身份证',width:160,alias:'customerIdCardNumber'},
									{title:'是否超期',width:100,alias:'isBeyondDeadline',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value == '1'){
												obj.html("<font color:'fuchsia;'>超期结单</font>");
											}else if(value == '0'){
												obj.html("正常结单");
											}else{
												return true;
											}
											return false;
										}
									},
									{title:'订单状态',width:100,alias:'leaseStatusRemark'},//leaseRecordStatus
									{title:'下单日期',width:150,alias:'orderDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds);
											return false;
										}
									},
									{title:'结单日期',width:150,alias:'dateSettle',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(!value.year){
												return true;
											}
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds);
											return false;
										}
									}
								]
				});
			});
		</SCRIPT>
	</body> 
</html>