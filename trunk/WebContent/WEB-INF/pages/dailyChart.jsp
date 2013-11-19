<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<title>销售日报</title>
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
				render:"lease",
				width:'100%',
				height:'100%',
				title:"销售日报",
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
					url:"getDailyReport.action",
					root:'data.data',							//返回的数据对象的名字
					total:'data.totalSize',
					showDetails:true,
					extraParam:{
						'date':'${DATE}'
					},
					afterLoading:function(){
						$.ajax({
							url:'getDailyReportSum.action',
							async:true,type:'post',
							data:{
								'date':$.trim($("#date").val()) ? $.trim($("#date").val()) : '${DATE}'
							},
							success:function(data){
								if(data.RESULT == 'FAILED'){
									$.dqgb.shadowLayer.alert({content:data.REASON});
									return;
								}
								var dt = data.data;
								$("#sumMsg").html("收入:&nbsp;￥" + dt.IN + "&nbsp;支出&nbsp;￥" + dt.OUT + "&nbsp;共计&nbsp;￥" + (parseInt(dt.IN) - parseInt(dt.OUT)) + "整");
								grid.header.setTitle("销售日报(" + ($.trim($("#date").val()) ? $.trim($("#date").val()) : '${DATE}') + ")");
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
								{title:'下单驻场',width:120,alias:'organizationName'},
								{title:'结单驻场',width:120,alias:'organizationNameSettle'},
								{title:'订单编号',width:180,alias:'serialNumber'},
								{title:'收入',width:100,alias:'income',
									beforeDataLoading:function(gridObj,obj,value,row,column){
										obj.html("￥" + value);
										return false;
									}
								},
								{title:'支出',width:100,alias:'outcome',
									beforeDataLoading:function(gridObj,obj,value,row,column){
										obj.html("￥" + value);
										return false;
									}
								},
								{title:'总计',width:100,alias:'total',
									beforeDataLoading:function(gridObj,obj,value,row,column){
										obj.html("￥" +(gridObj.options.store[row].income - gridObj.options.store[row].outcome)).css("color","fuchsia");
										return false;
									}
								},
								{title:'车牌号',width:120,alias:'carId'},
								{title:'起租日期',width:120,alias:'dateBegin',
									beforeDataLoading:function(gridObj,obj,value,row,column){
										obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
												"-" + parseInt(value.date));
										return false;
									}
								},
								{title:'截止日期',width:120,alias:'dateEnd',
									beforeDataLoading:function(gridObj,obj,value,row,column){
										obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
												"-" + parseInt(value.date));
										return false;
									}
								},
								{title:'续租截止日期',width:150,alias:'dateRelet',
									beforeDataLoading:function(gridObj,obj,value,row,column){
										if(!value || !value.year){
											return false;
										}
										obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
												"-" + parseInt(value.date));
										return false;
									}
								},
								{title:'租赁模式',width:100,alias:'leaseModelRemark'},
								{title:'租金',width:100,alias:'rentFee'},
								{title:'押金',width:100,alias:'foregiftFee'},
								{title:'预约金',width:100,alias:'bookingFee'},
								{title:'下单日期',width:150,alias:'orderDate',
									beforeDataLoading:function(gridObj,obj,value,row,column){
										obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
												"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds);
										return false;
									}
								},
								{title:'取车日期',width:180,alias:'fetchCarDate',
									beforeDataLoading:function(gridObj,obj,value,row,column){
										if(!value || !value.year){
											return false;
										}
										obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
												"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds)
												.css("color","fuchsia");
										return false;
									}
								},
								{title:'还车日期',width:180,alias:'giveBackCarDate',
									beforeDataLoading:function(gridObj,obj,value,row,column){
										if(!value || !value.year){
											return false;
										}
										obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
												"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds)
												.css("color","fuchsia");
										return false;
									}
								},
								{title:'承租人身份证',width:160,alias:'customerIdCardNumber'},
								{title:'订单状态',width:100,alias:'leaseStatusRemark'},
								{title:'业务员',width:120,alias:'transactor'},
								{title:'下单日期',width:150,alias:'orderDate',
									beforeDataLoading:function(gridObj,obj,value,row,column){
										obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
												"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds);
										return false;
									}
								},
								{title:'结单日期',width:150,alias:'dateSettle',
									beforeDataLoading:function(gridObj,obj,value,row,column){
										if(!value.year){
											return true;
										}
										obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
												"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds);
										return false;
									}
								},
								{title:'业务结算员',width:120,alias:'finisher'}
							]
				});
				
				grid.tbar.append($("#sumMsg").css("display","block").attr("title","点击查看明细").mouseover(function(){
					$("#sumMsg").css({"color":'blue',"text-decoration":"underline","cursor":"pointer"});
				}).mouseout(function(){
					$("#sumMsg").css({"color":'navy',"text-decoration":"none"});
				}).click(function(){
					$.dqgb.shadowLayer.loading({
						url:'gotoDailyReportDetails.action?date=' + ($.trim($("#date").val()) ? $.trim($("#date").val()) : '${DATE}'),
						width:700,height:400,
						closeBtn:true,title:'日销售明细(' + ($.trim($("#date").val()) ? $.trim($("#date").val()) : '${DATE}') + ')',
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
		</script>
	</head>
		
	<body>
		<div id="lease"></div>
		<div id="nav"></div>
		<div id="datePanel" style="width:400px; height:300px; padding:10px; font: 14px 微软雅黑; text-align: center; display: none; background-color: white;">
			请输入你要查询的日期:<input type="text" id="date" /><p>
			<input type="button" value="确认" style="width:50px;" id="confirm" />
		</div>
		<label id="sumMsg" style='float:right; line-height:30px; margin-right:5px; color:navy; font-size:14px; display: none;'></label>
	</body>
</html>