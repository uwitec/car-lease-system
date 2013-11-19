<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<title>日销售明细</title>
	    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.navigation.2.0.js"></script>
		<script type="text/javascript">
			$(function(){
				var grid = $.dqgb.grid.generateGrid({
					render:"details",
					width:714,
					height:414,
					iconCls:'browse',
					tbar:{},
					bbar:{
						url:"getDailyReportSumDetails.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						showDetails:true,
						extraParam:{
							'date':'${date}'
						},
						afterLoading:function(){
							$.ajax({
								url:'getDailyReportSum.action',
								async:true,type:'post',
								data:{
									'date':'${date}'
								},
								success:function(data){
									if(data.RESULT == 'FAILED'){
										$.dqgb.shadowLayer.alert({content:data.REASON});
										return;
									}
									var dt = data.data;
									$("#sumMsg").html("收入:&nbsp;￥" + dt.IN + "&nbsp;支出&nbsp;￥" + dt.OUT + "&nbsp;共计&nbsp;￥" + (parseInt(dt.IN) - parseInt(dt.OUT)) + "整");
								}
							});
						},
						totalCount:0,
						pageSize:200
					},
					singleMode:true,								//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									{title:'驻场',width:150,alias:'departmentName'},
									{title:'押金支出',width:100,alias:'foregiftOut',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value).css({
												'color':'maroon'
											});
											return false;
										}
									},
									{title:'租金支出',width:100,alias:'rentOut',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value).css({
												'color':'maroon'
											});
											return false;
										}
									},
									{title:'其它支出',width:100,alias:'otherOut',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var data = gridObj.options.store[row];
											obj.html("￥" + (data.out - data.rentOut - data.foregiftOut)).css({
												'color':'maroon'
											});
											return false;
										}
									},
									{title:'支出小计',width:100,alias:'out',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value).css({
												'color':'maroon'
											});
											return false;
										}
									},
									{title:'押金收入',width:100,alias:'foregiftIn',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},
									{title:'租金收入',width:100,alias:'rentIn',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},
									{title:'其它收入',width:100,alias:'otherIn',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var data = gridObj.options.store[row];
											obj.html("￥" + (data.ins - data.rentIn - data.foregiftIn));
											return false;
										}
									},
									{title:'收入小计',width:100,alias:'ins',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},
									{title:'财务总计',width:120,alias:'income',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value).css("color","fuchsia");
											return false;
										}
									}
								]
				});
				grid.tbar.append($("#sumMsg").css("display","block"));
				
			});
		
		</script>
	</head>
	<body>
		<div style="position: absolute;left:0px; top:0px; height:398px; width:698px; overflow: hidden;">
			<div id="details"  style="position: absolute;left:-8px; top:-8px;"></div>
		</div>
		<div id="nav"></div>
		<label id="sumMsg" style='float:right; line-height:30px; margin-right:5px; color:navy; font-size:14px; display: none;'></label>
	</body>
</html>