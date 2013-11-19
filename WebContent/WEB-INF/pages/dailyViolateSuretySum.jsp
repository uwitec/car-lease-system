<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<title>日担保金明细</title>
	    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/jquery-ui-1.8.12.custom.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-ui-1.8.12.custom.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.navigation.2.0.js"></script>
		<script type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				var grid = $.dqgb.grid.generateGrid({
					render:"details",
					width:'100%',
					height:'100%',
					title:'担保金统计',
					iconCls:'browse',
					tbar:{
						element:$.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
							      {text:'日期查询',iconCls:'find',click:function(){
							    		$("#datePanel").dqgbBox({
							    			width:400,height:200,title:'按日期查询',iconCls:'find',closeBtn:true
							    		});
							      	}
							      }
							]
						}).body
					},
					bbar:{
						url:"getDailyViolateSuretySum.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						showDetails:true,
						extraParam:{
							'date':'${date}'
						},
						afterLoading:function(){
							grid.header.setTitle("担保金统计(" + ($.trim($("#date").val()) ? $.trim($("#date").val()) : '${date}') + ")");
						},
						totalCount:0,
						pageSize:200
					},
					singleMode:true,								//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									{title:'驻场',width:180,alias:'departmentName'},
									{title:'收入(圆)',width:120,alias:'income',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},
									{title:'支出(圆)',width:120,alias:'outcome',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},
									{title:'总计(圆)',width:120,alias:'total',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value).css("color","fuchsia");
											return false;
										}
									},
									{title:'罚款总额(圆)',width:120,alias:'forfeit',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value).css("color","green");
											return false;
										}
									},
									{title:'手续费总额(圆)',width:140,alias:'poundage',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value).css("color","green");
											return false;
										}
									}
								]
				});

				$("#date").datepicker({
					dateFormat: 'yy-mm-dd',
					monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/'],
					onClose:function(text,inst){
					}
				});
				
				$("#confirm").click(function(){
					if(!$.trim($("#date").val())){
						$.dqgb.globalMsg.showMsg("查询日期不能为空！");
						return;
					}
					$.dqgb.shadowLayer.hide();
					setTimeout(function(){
						grid.pagination.options.extraParam = {
							'date':$.trim($("#date").val())
						};
						if(grid.pagination.getPageIndex() == 0){
							grid.refresh();
						}else{
							grid.pagination.first.trigger("click");
						}
					},500);
				});
				
			});
			function parseDate(date){
				var dt = date.split("-");
				return dt[0] + "/" + dt[1];
			}
		</script>
	</head>
	<body>
		<div id="details"></div>
		<div id="nav"></div>
		<div id="datePanel" style="width:400px; height:300px; padding:10px; font: 14px 微软雅黑; text-align: center; display: none; background-color: white;">
			请输入你要查询的日期:<input type="text" id="date" /><p>
			<input type="button" value="确认" style="width:50px;" id="confirm" />
		</div>
	</body>
</html>