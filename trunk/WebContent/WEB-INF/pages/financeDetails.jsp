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
		<title>财务明细管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/component.3.0.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.query.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				var grid = $.xqb9528.grid.getGrid({
					title:'财务明细',
					iconCls : 'customer',
					bbar : {
						url:"getFinance.action",
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
					checkBox : {
						singleModel : true
					},
					columnModel : [
		               	{title:'订单号',width:160,alias:'serialNumber',sortable:true},
		               	{title:'支付日期',width:120,alias:'payDate',sortable:true,
		               		beforeLoadingData : function(gridObj,row,col,value){
								col.dom.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
										"-" + parseInt(value.date));
								return false;
							}
		               	},
		               	{title:'财务类别',width:120,alias:'remark',sortable:true},
		               	{title:'财务流向',width:120,alias:'payDirection',sortable:true,
		               		beforeLoadingData : function(gridObj,row,col,value){
		               			if(value == 31){
		               				col.dom.html("收入").css("color","fuchsia");
		               			}else{
		               				col.dom.html("支出");
		               			}
								return false;
							}
		               	},
		               	{title:'金额',width:120,alias:'money',
		               		beforeLoadingData : function(gridObj,row,col,value){
								col.dom.html("￥" + value);
								return false;
							}
		               	},
		               	{title:'业务员',width:180,alias:'transactor',sortable:true,
		               		beforeLoadingData : function(gridObj,row,col,value){
								col.dom.html(value + "(" + row.getData().transactorName + ")");
								return false;
							}
		               	},
		               	{title:'部门信息',width:120,alias:'deptName',sortable:true}
						
					],
					
					autoExpandColumn : 'serialNumber'
				});
				
				grid.acceptWindowManage(false).autoExpand().show();					//设置grid为自适应的
				grid.doLayout();	//纠正ie7下无法自动撑满全屏幕的bug
			});
		</SCRIPT>
	</head>
	<body>
	</body> 
</html>