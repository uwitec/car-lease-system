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
		<title>保证金财务明细</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				var grid = $.dqgb.grid.generateGrid({
					render:"user",
					width:664,
					height:354,
					iconCls:'iconCls',
					bbar:{
						url:"getVsFinance.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						showDetails:true,
						extraParam : {
							id : '${id}'
						},
						totalCount:0,
						pageSize:20
					},
					singleMode:true,							//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									{title:'发生日期',width:150,alias:'givebackDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(!value || !value.year){
												return false;
											}
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds);
											return false;
										}
									},
									{title:'办理驻场',width:120,alias:'deptName'},
									{title:'金额',width:80,alias:'money',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(gridObj.options.store[row].financeType == 1){
												return true;
											}
											obj.html("-" + value);
											return false;
										}
									},
									{title:'财务说明',width:120,alias:'description'},
									{title:'业务员',width:120,alias:'realName'}
								]
				});
			});
		</SCRIPT>
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
		<div style="position: absolute;left:0px; top:0px; height:338px; width:648px; overflow: hidden;">
			<div id="user"  style="position: absolute;left:-8px; top:-8px;"></div>
		</div>
		<input type="hidden" id="userName" value="<s:property value='#session.CurrentUser.userName'/>"/>
	</body> 
</html>