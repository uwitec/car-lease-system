<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<title>订单财务信息</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		
		<script type="text/javascript">
			$(function(){
				var grid = $.dqgb.grid.generateGrid({
					render:"finance",
					width:614,
					height:414,
					iconCls:'browse',
					bbar:{
						url:"getFinanceByLeaseRecord.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						extraParam:{
							serialNumber:'${serialNumber}'
						},
						showDetails:true,
						totalCount:0,
						pageSize:200
					},
					singleMode:true,								//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									new $.dqgb.grid.CheckRow(),
									{title:'财务类型',width:120,alias:'financeTypeRemark',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(gridObj.options.store[row].financeType == 100){
												obj.css("color","fuchsia");
											}
											if(gridObj.options.store[row].financeType != "26" && gridObj.options.store[row].payType == 1){
												obj.html("预付" + value);
												return false;
											}
											return true;
										}
									},
									{title:'支付状态',width:100,alias:'statusRemark'},
									{title:'金额',width:100,alias:'money'},
									{title:'业务员',width:100,alias:'transactorOverName'},
									{title:'支付日期',width:150,alias:'payDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date));
											return false;
										}
									},
									{title:'备注',width:100,alias:'remark'}
									
								]
				});
			});
		</script>
	</head>
		
	<body>
		<div style="position: absolute;left:0px; top:0px; height:398px; width:598px; overflow: hidden;">
			<div id="finance"  style="position: absolute;left:-8px; top:-8px;"></div>
		</div>
	</body>
</html>