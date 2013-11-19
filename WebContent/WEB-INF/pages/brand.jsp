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
		<title>品牌管理</title>
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
					title:"品牌管理",
					iconCls:'iconCls',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
								{
									text:'新增品牌',
									iconCls:'addRole',
									click:function(){
										$.dqgb.shadowLayer.loading({
											title:'新增品牌',
											closeBtn:{
												afterClose:function(){
													grid.refresh();
												}
											},
											url:'gotoAddBrandPage.action',
											iconCls:'addRole'
										});
									}
								},'-',{
									text:'修改信息',
									iconCls:'modify',
									click:function(){
										var obj = grid.getSelectedRow();
										if(obj.length == 0){
											$.dqgb.shadowLayer.alert({
												content:'请选择您要修改的品牌信息'
											});
										}else{
											$.dqgb.shadowLayer.loading({
												title:'修改品牌信息',
												closeBtn:{
													afterClose:function(){
														grid.refresh();
													}
												},
												url:'gotoModifyBrandPage.action?id=' + obj[0].id,
												iconCls:'modify'
											});
										}
									}}
								]
						}).body
					},
					bbar:{
						url:"getBrandInformation.action",
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
									{title:'品牌编号',width:100,alias:'id',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(gridObj.options.store[row].remark == "其它品牌"){
												obj.css("color","fuchsia");
											}
											return true;
										}
									},
									{title:'品牌名',width:200,alias:'remark',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(gridObj.options.store[row].remark == "其它品牌"){
												obj.css("color","fuchsia");
											}
											return true;
										}
									}/* ,
									{title:'删除品牌',width:150,alias:'operation',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(gridObj.options.store[row].remark == "其它品牌"){
												return true;
											}
											var icon = $("<div title='删除品牌' class='deleteUser'></div>").appendTo(obj).click(function(){
												$.dqgb.shadowLayer.confirm({
													title:"温馨提示",
													content:"确认要删除该品牌数据?",
													afterClose: function(){
														$.ajax({
															url:"deleteBrand.action",
															async:true,
															data:{
																id : gridObj.options.store[row].id
															},
															type:"post",
															success:function(data){
																if(data.RESULT != "FAILED"){
																	grid.refresh();
																}else{
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
									}*/
								]
				});

			});
		</SCRIPT>
		
		<style>
			.modifyDic{
				background: url("images/modify.gif") no-repeat;
			}
		</style>
	</head>
	<body>
		<div id="dictionary">
		</div>
		<div id="nav"></div>
	</body> 
</html>