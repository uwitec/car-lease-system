<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<title>销售月报</title>
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
				/* var detail = $.dqgb.grid.generateGrid({
					render:"details",
					width:750,
					height:400,
					title:"销售月报",
					iconCls:'modify',
					closeBtn:true,
					bbar:{
						url:"getMensalReportByBrand.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						manualLoad : true,							//默认是false	
						showDetails:true,
						totalCount:0,
						pageSize:20
					},
					singleMode:true,								//单选模式
					columnModel:[
						new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
						new $.dqgb.grid.CheckRow(),
						{title:'车牌号',width:120,alias:'carId'},
						{title:'车源',width:80,alias:'carSource'},
						{title:'收入',width:100,alias:'in',
							beforeDataLoading:function(gridObj,obj,value,row,column){
								obj.html("￥" + value);
								return false;
							}
						},
						{title:'支出',width:100,alias:'out',
							beforeDataLoading:function(gridObj,obj,value,row,column){
								obj.html("￥" + value);
								return false;
							}
						},
						{title:'总计',width:100,alias:'total',
							beforeDataLoading:function(gridObj,obj,value,row,column){
								obj.html("￥" + value).css("color","fuchsia");
								return false;
							}
						},
						{title:'订单号',width:200,alias:'orderId'}
					]
				});
				detail.body.hide(); */
				
				var grid = $.dqgb.grid.generateGrid({
					render:"lease",
					width:'100%',
					height:'100%',
					title:"销售月报",
					iconCls:'browse',
					tbar:{
						element:$.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
							      {text:'月份查询',iconCls:'find',click:function(){
							    		$("#datePanel").dqgbBox({
							    			width:400,height:200,title:'按月份查询',iconCls:'find',closeBtn:true
							    		});
							      	}
							      }
							]
						}).body
					},
					bbar:{
						url:"getMensalReportByBrand.action",//"getMensalReport.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						showDetails:true,
						extraParam:{
							'date':'${DATE}'
						},
						afterLoading:function(){
							$.ajax({
								url:'getMensalReportSum.action',
								async:true,type:'post',
								data:{
									'date':$.trim($("#date").val()) ? $.trim($("#date").val()) : '${DATE}'
								},
								success:function(data){
									if(data.RESULT == 'FAILED'){
										$.dqgb.shadowLayer.alert({content:data.REASON});
										return;
									}
									grid.header.setTitle("销售月报(" + ($.trim($("#date").val()) ? parseDate($.trim($("#date").val())) : parseDate('${DATE}')) + ")");
									var dt = data.data.data;
									if(dt && dt.length){
										var total = 0;
										var text = "";
										for(var i = 0; i < dt.length; i++){
											total += parseInt(dt[i].total);
											text += dt[i].remark + "车租赁收入:￥<font style='color:fuchsia;'>" + dt[i].total + "</font>，&nbsp;";
										}
										$("#sumMsg").html(text + "总计:￥<font style='color:fuchsia;'>" + total + "</font>，冲红:￥<font style='color:fuchsia;'>" + Math.abs(dt[0].ch) + "</font>");
									}else{
										$("#sumMsg").html("<font style='color:fuchsia;'>当前月暂无任何财务信息</font>");
									}
								}
							});
						},
						totalCount:0,
						pageSize:20
					},
					singleMode:true,								//单选模式
					columnModel:[
						new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
						new $.dqgb.grid.CheckRow(),
						{title:'车牌',width:120,alias:'carId'},
						{title:'车源',width:100,alias:'carSource'},
						{title:'品牌',width:180,alias:'category'},
						{title:'收入',width:120,alias:'in',
							beforeDataLoading:function(gridObj,obj,value,row,column){
								obj.html("￥" + value);
								return false;
							}
						},
						{title:'支出',width:120,alias:'out',
							beforeDataLoading:function(gridObj,obj,value,row,column){
								obj.html("￥" + value);
								return false;
							}
						},
						{title:'总计',width:120,alias:'total',
							beforeDataLoading:function(gridObj,obj,value,row,column){
								obj.html("￥" + value).css("color","fuchsia");
								return false;
							}
						},
						{title:'租金总收入',width:150,alias:'totalRent',
							beforeDataLoading:function(gridObj,obj,value,row,column){
								obj.html("￥" + value).css("color","fuchsia");
								return false;
							}
						},
						{title:'出勤天数',width:100,alias:'days',
							beforeDataLoading:function(gridObj,obj,value,row,column){
								obj.html(value + "天");
								return false;
							}
						},
						{title:'出勤率',width:100,alias:'rate',
							beforeDataLoading:function(gridObj,obj,value,row,column){
								var days = gridObj.options.store[row].days;
								if(days == 0){
									obj.html(0);
									return false;
								}
								var date = new Date();
								date.setDate(0);
								var rate = days/date.getDate() * 100;
								obj.html(rate.toString().substr(0,5) + "%");
								return false;
							}
						}
					]/* ,
					listeners:{
						rowChecked : function(gridObj,rowIndex,columnIndex){			//行被checked
							$("#details").dqgbBox({
								width:750,height:400,iconCls:'modify',closeBtn:true
							});
							var needFresh = false;
							if(detail.pagination.options.extraParam && detail.pagination.options.extraParam.brand != gridObj.getSelectedRow()[0].brand){
								needFresh = true;
							}
							detail.pagination.options.extraParam = {
								'date':($.trim($("#date").val()) ? $.trim($("#date").val()) : '${DATE}'),
								brand:gridObj.getSelectedRow()[0].brand	
							};
							detail.show();
							if(needFresh){
								detail.refresh();
							}
							detail.header.setTitle(gridObj.getSelectedRow()[0].brandName +  "(" + ($.trim($("#date").val()) ? parseDate($.trim($("#date").val())) : parseDate('${DATE}')) + ")");
						 }
					} */
				});
				
				grid.tbar.append($("#sumMsg").css("display","block").attr("title","点击查看明细").mouseover(function(){
					$("#sumMsg").css({"color":'blue',"text-decoration":"underline","cursor":"pointer"});
				}).mouseout(function(){
					$("#sumMsg").css({"color":'navy',"text-decoration":"none"});
				}).click(function(){
					$.dqgb.shadowLayer.loading({
						url:'gotoMensalReportSumDetails.action?date=' + ($.trim($("#date").val()) ? $.trim($("#date").val()) : '${DATE}'),
						width:600,height:400,
						closeBtn:true,title:'月报统计明细(' + ($.trim($("#date").val()) ? parseDate($.trim($("#date").val())) : parseDate('${DATE}')) + ')',
						iconCls:'browse'
					});
				}));
				
				$("#date").datepicker({
					dateFormat: 'yy-mm-dd',
					monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/'],
					onClose:function(text,inst){
					}
				});
				
				$("#confirm").click(function(){
					if(!$.trim($("#date").val())){
						$.dqgb.globalMsg.showMsg("请您指定您要查询的月份信息!");
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
		<div id="lease"></div>
		<div id="nav"></div>
		<div id="details" style="position: absolute; left:0px; top:0px;"></div>
		<div id="datePanel" style="width:400px; height:300px; padding:10px; font: 14px 微软雅黑; text-align: center; display: none; background-color: white;">
			请输入你要查询的月份:<input type="text" id="date" /><p>
			<input type="button" value="确认" style="width:50px;" id="confirm" />
		</div>
		<label id="sumMsg" style='float:right; line-height:30px; margin-right:5px; color:navy; font-size:14px; display: none;'></label>
	</body>
</html>