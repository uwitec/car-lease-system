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
		<title>角色管理</title>
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
					render:"user",
					width:'100%',
					height:'100%',
					title:"权限管理",
					iconCls:'iconCls',
					bbar:{
						url:"getUser_Role.action",
						root:'data',							//返回的数据对象的名字
						total:'totalSize',
						showDetails:true,
						totalCount:0,
						pageSize:30
					},
					singleMode:true,							//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									{title:'帐号',width:150,alias:'userName'},
									{title:'姓名',width:120,alias:'realName'},
									{title:'年龄',width:80,alias:'age'},
									{title:'角色',width:120,alias:'role',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(gridObj.options.store[row].roleId == "2"){			//系统管理员
												obj.css({"color":"blue"});
											}else if(gridObj.options.store[row].roleId == "0"){
												obj.css({"color":"red"});
											}
											return true;
										}
									},
									{title:'部门名',width:200,alias:'departmentName'},
									{title:'性别',width:100,alias:'gender',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var icon;
											if("男" == value)
												icon = $("<div title='男性' class='male'></div>").appendTo(obj);
											else
												icon = $("<div title='女性' class='female'></div>").appendTo(obj);
											icon.css({
	                                        	"margin-left":"0px",
	                                        	"margin-top":"4px",
	                                        	"background-repeat":"no-repeat",
	                                        	"width":"16px",
	                                        	"height":"16px"
											});
											return false;
										}
									},
									{title:'角色管理',width:100,alias:'operation',
										beforeDataLoading : function(gridObj,obj,value,row,column){
											var icon = $("<div title='角色管理' class='addRole'></div>")
											.appendTo(obj).click(function(){
												$.dqgb.shadowLayer.loading({
													width:400,height:220,
													title:'角色管理——' + gridObj.options.store[row].realName +
															"(" + gridObj.options.store[row].userName + ")",
													closeBtn:{
														afterClose:function(){
															grid.refresh();
														}
													},
													url:'gotoAddRolePage.action?userName=' + 
														gridObj.options.store[row].userName,
													iconCls:'iconCls'
												});
											}).css({
	                                        	"margin-left":"0px",
	                                        	"margin-top":"4px",
	                                        	"cursor":"pointer",
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
		<div id="user">
		</div>
		<div id="nav"></div>
	</body> 
</html>