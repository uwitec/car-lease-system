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
		<title>价格管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.navigation.2.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				//=============
				var grid = $.dqgb.grid.generateGrid({
					render:"dictionary",
					width:'100%',
					height:'100%',
					title:"价格管理",
					iconCls:'iconCls',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
								{
									text:'新增价格数据',
									iconCls:'addRole',
									click:function(){
										$.dqgb.shadowLayer.loading({
											iconCls:'addRole',
											title:'新增价格数据',
											width:530,
											height:350,
											closeBtn:{
												afterClose:function(){
													grid.refresh();
												}
											},
											url:'gotoAddPricePage.action'
										});
									}
								},'-',{
									text:'修改价格数据',
									iconCls:'modify',
									click:function(){
										var obj = grid.getSelectedRow();
										if(obj.length == 0){
											$.dqgb.shadowLayer.alert({
												content:'请选择您要修改的价格数据'
											});
										}else{
											$.dqgb.shadowLayer.loading({
												iconCls:'modify',
												title:'修改价格信息',
												width:530,
												height:350,
												closeBtn:{
													afterClose:function(){
														grid.refresh();
													}
												},
												url:'gotoModifyPricePage.action?id=' + obj[0].id
											});
										}
									}}
								]
						}).body
					},
					bbar:{
						url:"getPriceInformation.action",
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
									{title:'车型',width:250,alias:'remark',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var record = gridObj.options.store[row];
											var name = record.brandName;
											if(record.carCategory)
												name = record.brandName + "—" + record.carCategory;
											obj.html(name) ;
											if(record.price >= 1000){
												obj.css("color","fuchsia");
											}
											return false;
										}
									},
									{title:'价格',width:100,alias:'price',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var record = gridObj.options.store[row];
											obj.html("￥" + record.price + "/天") ;
											if(record.price >= 1000){
												obj.css("color","fuchsia");
											}
											return false;
										}
									},
									{title:'超时计费',width:100,alias:'pricePerHour',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var record = gridObj.options.store[row];
											obj.html("￥" + record.pricePerHour + "/小时") ;
											return false;
										}
									},
									/* {title:'超驶计费',width:100,alias:'pricePerKm',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var record = gridObj.options.store[row];
											obj.html("￥" + record.pricePerKm + "/公里") ;
											return false;
										}
									}, */
									{title:'限里程单价',width:120,alias:'priceRM',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var record = gridObj.options.store[row];
											obj.html("￥" + record.priceRM + "/天") ;
											if(record.price >= 1000){
												obj.css("color","fuchsia");
											}
											return false;
										}
									},
									{title:'限里程超时费',width:130,alias:'pricePerHourRM',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var record = gridObj.options.store[row];
											obj.html("￥" + record.pricePerHourRM + "/小时") ;
											return false;
										}
									},
									{title:'限里程超驶费',width:130,alias:'pricePerKmRM',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var record = gridObj.options.store[row];
											obj.html("￥" + record.pricePerKmRM + "/公里") ;
											return false;
										}
									},
									{title:'押金',width:120,alias:'foregift',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var record = gridObj.options.store[row];
											obj.html("￥" + record.foregift) ;
											return false;
										}
									}/* ,
									{title:'删除数据',width:120,alias:'operation',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var icon = $("<div title='删除价格数据' class='deleteUser'></div>").appendTo(obj).click(function(){
												$.dqgb.shadowLayer.confirm({
													title:"温馨提示",
													content:"确认要删除该数据?",
													afterClose: function(){
														$.ajax({
															url:"deletePrice.action",
															async:true,
															data:{
																id : gridObj.options.store[row].id
															},
															type:"post",
															success:function(data){
																if(data.RESULT != "FAILED")
																	grid.refresh();
																else{
																	$.dqgb.shadowLayer.alert({
																		title:'警告',
																		content:data.REASON
																	});
																}
															}
														});
													}
												});
											});
											return false;
										}
									} */
								]
				});

			});
		</SCRIPT>
	</head>
	<body>
		<div id="dictionary">
		</div>
		<div id="nav"></div>
	</body> 
</html>