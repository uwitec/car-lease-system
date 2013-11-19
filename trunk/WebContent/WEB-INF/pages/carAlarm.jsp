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
		<title>车辆预警</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/component.3.0.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.query.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				var grid = $.xqb9528.grid.getGrid({
					title:'车辆预警管理',
					iconCls : 'customer',
					bbar : {
						url:"getCarAlarmInformation.action",
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
					checkBox : {
						singleModel : true
					},
					columnModel : [
		               	{title:'车牌号',width:180,alias:'car.carId',sortable:true},
		               	{title:'预警栏目',width:300,alias:'item',sortable:true},
		               	{title:'预警里程(KM)',width:150,alias:'nextKm'},
		               	{title:'预警日期',width:150,alias:'nextDate',
							beforeLoadingData : function(gridObj,row,col,value){
								if(!value || !value.year){
									return false;
								}
								col.dom.html((value.year + 1900) + "-" + (value.month + 1) + "-" + value.date);
								return false;
							}
						},
		               	{title:'处理预警',width:120,alias:'xx',
							beforeLoadingData : function(gridObj,row,col,value){
								var icon = $("<div title='处理预警' class='find'></div>")
								.appendTo(col.dom).click(function(e){
									if(row.getData().remindWay == 1){
										var date = row.getData().nextDate;
										date = new Date(date.year,date.month + row.getData().dateInterval,date.date);
										$("#msg").html("下次预计提示日期：" + (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate());
										box.showCenter();
									}else{
										var d = row.getData();
										$("#msg").html("下次预计提示里程：" + (d.car.km + d.kmInterval) + "km");
										box.showCenter();
									}
								}).css({
                                   	"margin-left":"0px",
                                   	"margin-top":"4px","cursor":"pointer",
                                   	"background-repeat":"no-repeat",
                                   	"width":"16px",
                                   	"height":"16px"
								});
								return false;
							}
						}
					],
					
					autoExpandColumn : 'idCardNumber' 
				});
				
				grid.acceptWindowManage(false).autoExpand().show();					//设置grid为自适应的
				grid.doLayout();	//纠正ie7下无法自动撑满全屏幕的bug
				
				var box = $("#msg").css({
					'text-indent':'2em','padding':'20px'
				}).xqbBox({
					width : 300,height : 200, iconCls : 'modify',
					title : '提示',model : true,closeBtn : true,
					buttons : {
						items : [{
							text : '确认',width : 60, click : function(){
								var m = $.xqb9528.shadowLayer.showMask("正在处理预警，请稍后....");
								$.ajax({
									url : 'handleAlarm.action',
									async : true, type : 'post',
									data : {
										id : grid.getSelectedRow().getData().id
									},success : function(data){
										if(data.RESULT != 'FAILED'){
											$.xqb9528.globalMsg.showMsg("数据提交成功",true);
											m.hide();
											box.hide();
											grid.refresh();
										}else{
											$.xqb9528.globalMsg.showMsg(data.REASON);
										}
									}
								});
							}
						},{
							text : '取消', width : 60, click : function(){
								box.hide();
							}
						}]
					}
				});
			});
		</SCRIPT>
	</head>
	<body>
		<div id="msg"></div>
	</body> 
</html>