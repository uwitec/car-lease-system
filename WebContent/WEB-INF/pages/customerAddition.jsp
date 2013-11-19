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
		<title>客户增量管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/component.3.0.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.3.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				var grid = $.xqb9528.grid.getGrid({
					title:'新增客户统计',
					treeGrid : {
						root : 'data.data',
						url : 'getCustomerByDate.action',	//加载本地数据时不配置该项
						keyFieldName : 'date',				//关键字，作为变量名传递给后台 	
						alias : 'customerName',				//row data对象中alias对应的值 作为keyFieldName的变量值
						result : 'RESULT',					
						failed : 'FAILED',					
						reason : 'REASON',
						afterFirstExpand : function(row,col,gridObj){
							col.setText(col.getCurrentValue() + " —— 新增客户人数：<font style='color:fuchsia'><b>" + 
									row.subRows.length + "</b></font>");
						},
						error : function(){
							$.xqb9528.globalMsg.showMsg("连接超时,请重新登录！",true);
						}
					},
					iconCls : 'customer',
					bbar : {
						url:"getCustomerAddition.action",
						root:'data',							//返回的数据对象的名字
						total:'totalSize',
						result : 'RESULT',					
						failed : 'FAILED',					
						reason : 'REASON',
						pageSize : 20,
						showDetails : true
					},
					width : 600,
					height : 350,
					rowNumber : true,
					autoLoad : true,							//表格显示以后自动加载数据
					checkBox : {
						singleModel : true
					},
					columnModel : [
		               	{title:'客户姓名',width:250,alias:'customerName',ecColumn : true},
						{title:'性别',width:100,alias:'gender',sortable:true},
						{title:'身份证号码',width:180,alias:'idCardNumber'},
						{title:'联系方式',width:150,alias:'mobilePhone'},
						{title:'客户单位',width:150,alias:'company'},
						{title:'单位电话',width:120,alias:'companyPhone'}
					],
					buttons : {
						items : [
							{text : '客户查找',click : function(e){
								box.animateShow({x:e.clientX,y:e.clientY});
							}}
						],
						buttonAlign : 'center'
					}
				});
				grid.acceptWindowManage(false).autoExpand().show();	//设置grid为自适应的
				grid.doLayout();									//纠正ie7下无法自动撑满全屏幕的bug
				
				var box = $("#yearDiv").xqbBox({
					title : '新增用户查询',
					closeBtn : true,
					iconCls : 'modify',
					model : true,
					width : 300,
					height: 160,
					buttons : {
						items :	[
			       			{text : '确认', width: '60', 
			       				click : function(e){
				       				grid.setExtraParam({
				       					year : $.trim($("#year").val())
				       				}).refresh().first();
				       				box.hide();
			       				}
			       			}
			       		]            
					}
				});
			});
		</SCRIPT>
	</head>
	<body>
		<div id="yearDiv" style="padding: 20px 10px;text-align: center;">
			<select id="year" style="width:180px">
				<option>2011</option>
				<option>2012</option>
				<option>2013</option>
				<option>2014</option>
				<option>2015</option>
				<option>2016</option>
				<option>2017</option>
				<option>2018</option>
				<option>2019</option>
				<option>2020</option>
				<option>2021</option>
				<option>2022</option>
				<option>2023</option>
				<option>2024</option>
				<option>2025</option>
				<option>2026</option>
				<option>2027</option>
				<option>2028</option>
				<option>2029</option>
				<option>2030</option>
			</select>
		</div>
	</body> 
</html>