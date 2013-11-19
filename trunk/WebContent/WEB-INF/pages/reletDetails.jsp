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
		<title>订单明细信息</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		
	</head>
	<body>
		<input type="hidden" id="serialNumber" value="<s:property value='#session.ORDERFORMID' />" />
		<div style="position: absolute;left:0px; top:0px; height:348px; width:798px; overflow: hidden;">
			<div id="user"  style="position: absolute;left:-8px; top:-8px;"></div>
		</div>
		<script type="text/javascript">
			$(function(){
				var grid = $.dqgb.grid.generateGrid({
					render:"user",
					width:814,
					height:364,
					iconCls:'iconCls',
					bbar:{
						url:"getReletDetails.action",
						root:'data',							//返回的数据对象的名字
						total:'totalSize',
						showDetails:true,
						extraParam:{
							serialNumber:$("#serialNumber").val()
						},
						totalCount:0,
						pageSize:100
					},
					singleMode:true,							//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									{title:'订单号',width:180,alias:'orderFormId'},
									{title:'续租起始日期',width:150,alias:'dateBegin',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(value.split("T")[0]);
											return false;
										}
									},
									{title:'续租截止日期',width:150,alias:'dateEnd',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(value.split("T")[0]);
											return false;
										}
									},
									{title:'业务办理日期',width:150,alias:'bussinessDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(value.split("T")[0]);
											return false;
										}
									},
									{title:'业务员',width:120,alias:'transactor'}
								]
				});
			});
		
		</script>
	</body> 
</html>