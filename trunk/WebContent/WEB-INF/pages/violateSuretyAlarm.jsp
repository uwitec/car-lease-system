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
		<title>保证金预警管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		
		<script type="text/javascript">
			$(function(){
				var grid = $.dqgb.grid.generateGrid({
					render:"vs",
					width:'100%',
					height:'100%',
					title:"违章保证金预警信息",
					iconCls:'browse',
					bbar:{
						url:"getVsAlarmInformation.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						showDetails:true,
						totalCount:0,
						pageSize:20
					},
					singleMode:true,							//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									new $.dqgb.grid.CheckRow(),
									{title:'车牌号',width:120,alias:'carId'},
									{title:'客户身份证',width:180,alias:'customerId'},
									{title:'保证金缴付日期',width:150,alias:'payDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date));
											return false;
										}
									},
									{title:'应当退还日期',width:150,alias:'deadlineDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(!value || !value.year){
												return false;
											}
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date));
											return false;
										}
									},
									{title:'预警原因',width:120,alias:'vsStatus',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value == "0"){
												obj.html("<font style='color:fuchsia;'>即将到期</font>");
											}else if(value == "1"){
												obj.html("<font style='color:navy;'>被冻结</font>");
											}
											return false;
										}
									},
									{title:'业务员',width:120,alias:'transactorName'},
									{title:'结单日期',width:150,alias:'givebackDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(!value || !value.year){
												return false;
											}
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes);
											return false;
										}
									},
									{title:'保证金',width:120,alias:'money'}
									
								]
				});
			});
		</script>
	</head>
	<body>
		<div id="vs"></div>
	</body>
</html>