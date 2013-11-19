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
		<title>员工产值统计</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/component.3.0.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.3.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				var grid = $.xqb9528.grid.getGrid({
					title:'员工产值(${year}年)',
					iconCls : 'customer',
					bbar : {
						url:"getOutputSummary.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						pageSize : 20,
						result : 'RESULT',					
						failed : 'FAILED',					
						reason : 'REASON',
						showDetails : true
					},
					width : 600,
					height : 350,
					rowNumber : true,
					autoLoad : true,							//表格显示以后自动加载数据
					checkBox : false,
					columnModel : [
						{title:'帐号',width:150,alias:'username',sortable : true},
						{title:'姓名',width:120,alias:'realName'},
						{title:'1月(总收入/租金)',width:155,alias:'_1'},
						{title:'2月(总收入/租金)',width:155,alias:'_2'},
						{title:'3月(总收入/租金)',width:155,alias:'_3'},
						{title:'4月(总收入/租金)',width:155,alias:'_4'},
						{title:'5月(总收入/租金)',width:155,alias:'_5'},
						{title:'6月(总收入/租金)',width:155,alias:'_6'},
						{title:'7月(总收入/租金)',width:155,alias:'_7'},
						{title:'8月(总收入/租金)',width:155,alias:'_8'},
						{title:'9月(总收入/租金)',width:155,alias:'_9'},
						{title:'10月(总收入/租金)',width:160,alias:'_10'},
						{title:'11月(总收入/租金)',width:160,alias:'_11'},
						{title:'12月(总收入/租金)',width:160,alias:'_12'},
						{title:'年产值',width:120,alias:'year'}
					],
					buttons : {
						items : [
							{text : '产值查询',click : function(e){
								box.animateShow({x:e.clientX,y:e.clientY});
							}}
						],
						buttonAlign : 'center'
					}
				});
				grid.acceptWindowManage(false).autoExpand().show();	//设置grid为自适应的
				grid.doLayout();									//纠正ie7下无法自动撑满全屏幕的bug
				
				var box = $("#yearDiv").xqbBox({
					title : '员工产值查询',
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
				       				grid.setTitle("员工产值(" + $.trim($("#year").val()) + "年)");
				       				box.hide();
			       				}
			       			},{text : '取消', width : '60',click : function(){
			       				box.hide();
			       			}}
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