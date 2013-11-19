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
		
		<SCRIPT type="text/javascript">
			$(function(){
				var grid = $.dqgb.grid.generateGrid({
					render:"user",
					width:614,
					height:414,
					iconCls:'iconCls',
					bbar:{
						url:"getOnlineUser.action",
						root:'data',							//返回的数据对象的名字
						total:'totalSize',
						showDetails:true,
						totalCount:0,
						pageSize:20
					},
					singleMode:true,							//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									{title:'姓名',width:120,alias:'realName'},
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
									{title:'联系同事',width:120,alias:'qq',
										beforeDataLoading : function(gridObj,obj,value,row,column){
											if(!value){
												return false;
											}
											if($.trim($("#userName").val()) == gridObj.options.store[row].userName){
												return false;
											}
											var icon = $('<div class="gridIcon"></div>');
											var val = "<a target=blank href=http://wpa.qq.com/msgrd?V=1&Uin=" + value + 
													"&Site=南宁广文& Menu=yes><img border='0' SRC=http://wpa.qq.com/pa?p=1:" + 
															value + ":4 alt='给" + value + "发送消息'/></a>";
											icon.html(val).appendTo(obj);				
											return false;
										}
									},
									{title:'登录地点',width:150,alias:'loginIp'}
								]
				});
			});
		</SCRIPT>
		<style>
			.gridIcon{
				margin-left:0px;
               	margin-top:4px;
               	background-repeat:no-repeat;
               	width:16px;
               	height:16px;
			}
		</style>
	</head>
	<body>
		<div style="position: absolute;left:0px; top:0px; height:398px; width:598px; overflow: hidden;">
			<div id="user"  style="position: absolute;left:-8px; top:-8px;"></div>
		</div>
		<input type="hidden" id="userName" value="<s:property value='#session.CurrentUser.userName'/>"/>
	</body> 
</html>