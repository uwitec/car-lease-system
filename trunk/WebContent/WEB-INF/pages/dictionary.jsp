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
		<title>字典管理</title>
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
					title:"字典数据管理",
					iconCls:'iconCls',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
								{
									text:'新建角色',iconCls:'addRole',
									click:function(){
										$.dqgb.shadowLayer.loading({
											title:'新建角色',
											width:350,height:200,
											closeBtn:{
												afterClose:function(){
													grid.refresh();
												}
											},
											url:'gotoCreateRole.action',
											iconCls:'addRole'
										});
									}
								},'-',{
									text:'修改名称',iconCls:'modify',
									click:function(){
										var obj = grid.getSelectedRow();
										if(obj.length == 0){
											$.dqgb.shadowLayer.alert({
												content:'请选择您要修改的字典数据'
											});
										}else{
											$.dqgb.shadowLayer.loading({
												width:350,height:200,
												title:'修改字典信息',iconCls:'modify',
												closeBtn:{
													afterClose:function(){
														grid.refresh();
													}
												},
												url:'gotoModifyDictionaryPage.action?id=' + obj[0].id,
												iconCls:'iconCls'
											});
										}
									}}
								]
						}).body
					},
					bbar:{
						url:"getDictionaryInformation.action",
						root:'data',							//返回的数据对象的名字
						total:'totalSize',
						showDetails:true,
						totalCount:0,
						pageSize:20
					},
					singleMode:true,							//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									new $.dqgb.grid.CheckRow(),
									{title:'字典编号',width:100,alias:'id',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											return modifyColor(gridObj,obj,value,row,column);
										}
									},
									{title:'字典名',width:150,alias:'remark',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											return modifyColor(gridObj,obj,value,row,column);
										}
									},
									{title:'属组编号',width:150,alias:'groupId',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											return modifyColor(gridObj,obj,value,row,column);
										}
									},
									{title:'属组描述',width:150,alias:'groupRemark',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											return modifyColor(gridObj,obj,value,row,column);
										}
									},
									{title:'删除职位',width:150,alias:'operation',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(gridObj.options.store[row].groupId != 2){
												return true;
											}
											var icon = $("<div title='删除职位' class='deleteUser'></div>").appendTo(obj).click(function(){
												$.dqgb.shadowLayer.confirm({
													title:"温馨提示",
													content:"确认要删除该数据?",
													afterClose: function(){
														$.ajax({
															url:"deletePosition.action",
															async:true,
															data:{
																id : gridObj.options.store[row].id
															},
															type:"post",
															success:function(data){
																if(data.RESULT == "SUCCESS")
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
									}
								]
				});

			});
			/***
			*@TODO	修改字体颜色
			***/
			function modifyColor(gridObj,obj,value,row,column){
				if(gridObj.options.store[row].groupId == 1){
					obj.css("color","blue");
				}else if(gridObj.options.store[row].groupId == 2){
					obj.css("color","green");
				}else if(gridObj.options.store[row].groupId == 3){
					obj.css("color","fuchsia");
				}else if(gridObj.options.store[row].groupId == 4){
					obj.css("color","maroon");
				}else if(gridObj.options.store[row].groupId == 5){
					obj.css("color","olive");
				}
				return true;
			};
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