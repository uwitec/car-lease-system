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
		<title>组织管理</title>
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
					render:"Organization",
					width:'100%',
					height:'100%',
					title:"系统组织管理",
					iconCls:'iconCls',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
								render:"nav",
								item:[{text:'新增组织',iconCls:'addRole',click:function(){
										$.dqgb.shadowLayer.loading({
											title:'新增组织',
											width:300,
											height:150,
											closeBtn:{
												afterClose:function(){
													grid.refresh();
												}
											},
											url:'gotoAddOrganizationPage.action',
											iconCls:'iconCls'
										});
									}}]
							}).body
					},
					bbar:{
						url:"getOrganization.action",
						root:'data',							//返回的数据对象的名字
						total:'totalSize',
						showDetails:true,
						totalCount:0,
						pageSize:20
					},
					singleMode:true,							//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									{title:'组织编号',width:150,alias:'id'},
									{title:'组织名称',width:250,alias:'departmentName'},
									{title:'修改组织信息',width:150,alias:'level',
										beforeDataLoading : function(gridObj,obj,value,row,column){
											var icon = $("<div title='修改组织信息' class='modify'></div>")
											.appendTo(obj).click(function(){
												$.dqgb.shadowLayer.loading({
													title:'修改组织信息',
													width:300,
													height:150,
													closeBtn:{
														afterClose:function(){
															grid.refresh();
														}
													},
													url:'gotoModifyOrganizationPage.action?id=' + gridObj.options.store[row].id,
													iconCls:'iconCls'
												});
											}).css({
												"cursor":"pointer",
	                                        	"margin-left":"0px",
	                                        	"margin-top":"4px",
	                                        	"background-repeat":"no-repeat",
	                                        	"width":"16px",
	                                        	"height":"16px"
											});
											return false;			//客户化
										}}
								]
				});
			});
		</SCRIPT>
	</head>
	<body>
		<div id="Organization">
		</div>
		<div id="nav"></div>
	</body> 
</html>