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
		<title>用户管理</title>
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
					title:"用户管理",
					iconCls:'iconCls',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[{text:'新建用户',iconCls:'addRole',click:function(){
										$.dqgb.shadowLayer.loading({
											title:'新建用户',
											width:500,
											height:270,
											closeBtn:{
												afterClose:function(){
													grid.refresh();
												}
											},
											url:'gotoAddUserPage.action',
											iconCls:'addRole'
										});
									}
								},
								"-",
								{text:'维护用户',iconCls:'modify',click:function(){
									var obj = grid.getSelectedRow();
									if(obj.length == 0){
										$.dqgb.shadowLayer.alert({
											content:'请选择您要维护的用户'
										});
									}else{
										$.dqgb.shadowLayer.loading({
											title:'维护用户信息',
											width:500,
											height:270,
											closeBtn:{
												afterClose:function(){
													grid.refresh();
												}
											},
											url:'gotoModifyUserPage.action?userName=' + obj[0].userName,
											iconCls:'modify'
										});
									}
								}}
							]
						}).body
					},
					bbar:{
						url:"getSystemUser.action",
						root:'data',							//返回的数据对象的名字
						total:'totalSize',
						showDetails:true,
						totalCount:0,
						pageSize:30
					},
					singleMode:true,							//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									new $.dqgb.grid.CheckRow(),
									{title:'帐号',width:150,alias:'userName'},
									{title:'姓名',width:120,alias:'realName'},
									{title:'身份证号',width:220,alias:'idCardNumber'},
									{title:'年龄',width:80,alias:'age'},
									{title:'性别',width:80,alias:'gender',
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
									{title:'部门名',width:200,alias:'departmentName'},
									{title:'删除用户',width:100,alias:'level',
										beforeDataLoading : function(gridObj,obj,value,row,column){
											var icon = $("<div title='删除该用户' class='deleteUser'></div>")
											.appendTo(obj).click(function(){
												$.dqgb.shadowLayer.confirm({
													title:"温馨提示",
													content:"确认要删除该用户?",
													afterClose: function(){
														$.ajax({
															url:"invalidUser.action",
															async:true,
															data:{
																user : toJson(gridObj.options.store[row])
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
											return false;			//客户化
										}}
								]
				});
			});

			function toJson(Obj){
				if(!Obj){
					return;
				}
				var str = '{';
				for(el in Obj){
					str += el.toString() + ":'" + Obj[el] + "',";
				}
				str = str.substr(0,str.length - 1);
				str += "}";
				return str;
			}
		</SCRIPT>
	</head>
	<body>
		<div id="user">
		</div>
		<div id="nav"></div>
	</body> 
</html>