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
		<title>欠费统计</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/component.3.0.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.3.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				var grid = $.xqb9528.grid.getGrid({
					title:'欠费明细',
					iconCls : 'customer',
					bbar : {
						url:"getArrearage.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						pageSize : 20,
						result : 'RESULT',					
						failed : 'FAILED',					
						reason : 'REASON',
						showDetails : true
					},
					width : 600,
					height : 350,
					rowNumber : true,
					autoLoad : true,							//表格显示以后自动加载数据
					checkBox : false,
					columnModel : [
		               	{title:'订单编号',width:250,alias:'serialNumber',sortable:true},
		               	{title:'客户姓名',width:150,alias:'customerName',sortable:true},
						{title:'欠费金额',width:100,alias:'reletFeeWaiting',sortable:true,
							beforeLoadingData : function(gridObj,row,col,value){
								col.dom.html("￥" + value);
								return false;
							}
						},
						{title:'下单驻场',width:150,alias:'orgName'},
						{title:'下单日期',width:188,alias:'orderDate',
							beforeLoadingData : function(gridObj,row,col,value){
								if(!value || !value.year){
									return false;
								}
								col.dom.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
										"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds);
								return false;
							}
						}
					],
					autoExpandColumn : 'serialNumber',
					buttons : {
						items : [
							{text:'欠费总计',click:function(e){
								$.ajax({
									url:'getArrearage.action',async:true,type:'post',
									data:{},success : function(data){
										sumGrid.showCenter();
										sumGrid.getTopBarContainer().empty();
										$("<div></div>").html("总计欠费￥" + data.data.total + "圆整")
											.appendTo(sumGrid.getTopBarContainer()).css({
												'margin':'3px','font-size':'14px','font-family':'微软雅黑',
												'color':'fuchsia'
											});
									}
								});
							}}         
						]
					}
				});
				grid.acceptWindowManage(false).autoExpand().show();	//设置grid为自适应的
				grid.doLayout();									//纠正ie7下无法自动撑满全屏幕的bug
				
				var sumGrid = $.xqb9528.grid.getGrid({
					title:'欠费统计',
					iconCls : 'customer',
					tbar : {},
					bbar : {
						url:"getArrearageSummary.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						pageSize : 20,
						result : 'RESULT',					
						failed : 'FAILED',					
						reason : 'REASON',
						showDetails : true
					},
					width : 600,
					height : 350,
					rowNumber : true,
					autoLoad : true,							//表格显示以后自动加载数据
					model : true,
					closeBtn : true,
					checkBox : false,
					columnModel : [
		               	{title:'驻场名称',width:250,alias:'orgName',sortable:true},
		               	{title:'欠费总计',width:150,alias:'finance',sortable:true,
							beforeLoadingData : function(gridObj,row,col,value){
								col.dom.html("￥" + value);
								return false;
							}
						}
					],
					autoExpandColumn : 'serialNumber'
				});
				
			});
		</SCRIPT>
	</head>
	<body>
	</body> 
</html>