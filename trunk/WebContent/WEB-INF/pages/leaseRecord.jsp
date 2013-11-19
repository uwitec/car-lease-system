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
		<title>订单管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.tree.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.navigation.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.query.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				var grid = $.dqgb.grid.generateGrid({
					render:"lease",
					width:'100%',
					height:'100%',
					title:"订单管理",
					iconCls:'browse',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
								{
									text:'订单查询',iconCls:'modify',subNodes:[
									{text:'按车牌查询',iconCls:'browse',
										click:function(){
											$("#searchByCarId").dqgbBox({
												width:250,height:100,
												title:"按车牌号查询订单",
												closeBtn:true,
												iconCls:'browse'
											});
										}
									},{text:'按订单号查询',iconCls:'browse',
										click:function(){
											$("#searchByOrderId").dqgbBox({
												width:420,height:220,
												title:"按订单号查询",
												closeBtn:true,
												iconCls:'browse'
											});
											$("#specifiedOrderId").query({
												url:'getLeaseRecord.action',
												miniQueryLength:4,									//输入4个字符以后开始查询
												width:304,
												height:100,
												root:'data.data',
												remindMsg:'没有找到相关订单信息，请确认您的输入是否正确!',	//没有找到数据时的提示
												param : {
													pageSize:5,
													pageIndex:0
												},
												keyField:'serialNumber',							//核心字段
												fields:['serialNumber','carId','leaseStatusRemark']				//配置需要显示的字段
											});
										}
									},{text:'查询全部',iconCls:'browse',
										click:function(){
											grid.pagination.options.extraParam = {};
        									grid.pagination.options.url = "getLeaseRecord.action";			//条件查询
        									if(grid.pagination.getPageIndex() == 0){
        										grid.refresh();
        									}else{
        										grid.pagination.first.trigger("click");
        									}
										}
									}]
								},'-',{
							   		text:'订单排序',iconCls:'modify',subNodes:[
	                            	{text:'按车牌排序',iconCls:'browse',
	                            		click:function(){
	                            			$.extend(grid.pagination.options.extraParam,{
	                            				order:'carId',
	                            				direction:'asc'
	                            			});
        									grid.pagination.options.url = "getLeaseRecord.action";			//条件查询
        									if(grid.pagination.getPageIndex() == 0){
        										grid.refresh();
        									}else{
        										grid.pagination.first.trigger("click");
        									}
	                            		}
	                            	},
	                            	{text:'按下单日期排序',iconCls:'browse',
	                            		click:function(){
	                            			$.extend(grid.pagination.options.extraParam,{
	                            				order:'orderDate',
	                            				direction:'desc'
	                            			});
	                            			grid.pagination.options.url = "getLeaseRecord.action";			//条件查询
        									if(grid.pagination.getPageIndex() == 0){
        										grid.refresh();
        									}else{
        										grid.pagination.first.trigger("click");
        									}
	                            		}
	                            	},
	                            	{text:'按状态排序',iconCls:'browse',
	                            		click:function(){
	                            			$.extend(grid.pagination.options.extraParam,{
	                            				order:'leaseRecordStatus',
	                            				direction:'asc'
	                            			});
	                            			grid.pagination.options.url = "getLeaseRecord.action";			//条件查询
        									if(grid.pagination.getPageIndex() == 0){
        										grid.refresh();
        									}else{
        										grid.pagination.first.trigger("click");
        									}
	                            		}
	                            	}
							   	]},'-',{text:'订单管理',iconCls:'modify',subNodes:[
	                            	{
    									text:'预览订单',iconCls:'browse',click:function(){
    										var row = grid.getSelectedRow();
    										if(row.length == 0){
    											$.dqgb.globalMsg.showMsg('请指定您要预览的订单信息！');
    											return;
    										}
    										$.dqgb.shadowLayer.loading({
												width:850,
												height:400,
												title : "订单预览(" + row[0].serialNumber + ")",
												closeBtn:true,
												url:"gotoPrintOrderForm.action?serialNumber=" + row[0].serialNumber,
												iconCls:'browse'
											});
    									}
	                            	},{text:'修改预约',iconCls:'modify',
								  		click:function(){
								  			var row = grid.getSelectedRow();
											if(row.length == 0){
												$.dqgb.globalMsg.showMsg('请指定您要修改的订单信息！');
												return;
											}
											window.location = 'gotoModifyOrderFormPage.action?serialNumber=' + encodeURI(row[0].serialNumber);
										}
									},{text:'订单结算',iconCls:'browse',
	                            		click:function(){
	                            			var row = grid.getSelectedRow();
											if(row.length == 0){
												$.dqgb.globalMsg.showMsg('请指定您要结算的订单！');
												return;
											}
											$.dqgb.shadowLayer.showMask({mask:'正在进行财务结算,请稍候....'});
											$.ajax({
												url:'financeSettle.action',	//财务结算向数据库中添加财务信息
												async:true,
												type:'post',
												data:{
													serialNumber:row[0].serialNumber
												},
												success:function(data){
													if(data.RESULT == 'FAILED'){
														$.dqgb.shadowLayer.alert({content:data.REASON});
													}else{
														$.dqgb.shadowLayer.loading({
															width:852,
															height:400,
															title : "财务结算" + "(" + row[0].serialNumber + ")",
															closeBtn:{
																afterClose:function(data){
																	grid.refresh();
																}
															},
															url:'gotoFinanceSettlePage.action?serialNumber=' + row[0].serialNumber,
															iconCls:'browse'
														});
													}
												}
											});
	                            		}
	                            	},{text:'订单财务',iconCls:'modify',click:function(){
	                            		var row = grid.getSelectedRow();
										if(row.length == 0){
											$.dqgb.globalMsg.showMsg('请指定您要查看财务信息的订单！');
											return;
										}
										$.dqgb.shadowLayer.loading({
											url:'gotoOrderFinancePage.action?serialNumber=' + row[0].serialNumber,
											width:600,
											height:400,
											title : "财务信息" + "(" + row[0].serialNumber + ")",
											closeBtn:true,
											iconCls:'modify'
										});
	                            	}},{text:'打印结算单',iconCls:'browse',click:function(){
	                            		$("#OrderIdToPrint").dqgbBox({
	                            			width:420,height:220,
											title:"打印结算单",
											closeBtn:true,
											iconCls:'browse'
	                            		});
	                            		$("#OrderIdToBePrint").query({
	                    					url:'getLeaseRecord.action',
	                    					miniQueryLength:4,									//输入4个字符以后开始查询
	                    					width:304,
	                    					height:100,
	                    					root:'data.data',
	                    					remindMsg:'没有找到相关订单信息，请确认您的输入是否正确!',	//没有找到数据时的提示
	                    					param : {
	                    						pageSize:5,
	                    						pageIndex:0
	                    					},
	                    					keyField:'serialNumber',							//核心字段
	                    					fields:['serialNumber','carId','leaseStatusRemark']				//配置需要显示的字段
	                    				});
	                            	}},
	                            	{
    									text:'冻结订单',iconCls:'browse',click:function(){
    										var row = grid.getSelectedRow();
    										if(row.length == 0){
    											$.dqgb.globalMsg.showMsg('请指定您要冻结的订单！');
    											return;
    										}
    										$.dqgb.shadowLayer.confirm({
												content:'该操作将会导致该订单永久性无效，您确认要这样做?',
												afterClose:function(){
													$.dqgb.shadowLayer.showMask({mask:'正在执行订单冻结操作,请稍后...'});
													$.ajax({
														url:'freezeOrder.action',
														async:true,
														type:'post',
														data:{
															serialNumber : row[0].serialNumber
														},
														success : function(data){
															if(data.RESULT != 'FAILED'){
																$.dqgb.shadowLayer.hideMask();
																$.dqgb.globalMsg.showMsg("订单已冻结成功!");
																grid.refresh();
															}else{
																$.dqgb.shadowLayer.alert({content:data.REASON});
															}
														}
													});
												}
											});
    									}
	                            	}
							   	]},'-',{text : '续租管理',iconCls:'modify',subNodes:[
							   		{text:'订单续租',iconCls:'browse',click:function(){
	                            		var row = grid.getSelectedRow();
										if(row.length == 0){
											$.dqgb.globalMsg.showMsg('请指定您要续租的订单！');
											return;
										}
	                            		$("#reletDiv").dqgbBox({
	                            			width:320,height:120,
											title:"订单续租",
											closeBtn:true,
											iconCls:'browse'
	                            		});
	                            	}},{text:'费用补缴',iconCls:'find',click:function(){
	                            		var row = grid.getSelectedRow();
										if(row.length == 0){
											$.dqgb.globalMsg.showMsg('请指定您要补缴续租费的订单！');
											return;
										}
	                            		$("#supplimentFee").dqgbBox({
	                            			width:300,
	                            			height:130,
	                            			iconCls:'find',
	                            			title:'补缴续租费用',
	                            			closeBtn:true
	                            		});
	                            	}},{text:'续租信息',iconCls:'modify',click:function(){
	                            		var row = grid.getSelectedRow();
										if(row.length == 0){
											$.dqgb.globalMsg.showMsg('请指定您要查看的订单！');
											return;
										}
										$.dqgb.shadowLayer.loading({
											url:'gotoReletDetailsPage.action?serialNumber=' + row[0].serialNumber,
											width:800,
											height:350,
											title : "续租信息" + "(" + row[0].serialNumber + ")",
											closeBtn:true,
											iconCls:'modify'
										});
	                            	}}
							   	
							   	]},'-',{text:'财务交接',iconCls:'modify',click:function(){
                            		$.dqgb.shadowLayer.loading({
                						url:'gotoDailyReportDetails.action?date=' + '${date}',
                						width:700,height:400,
                						closeBtn:true,title:'财务交接(${date})',
                						iconCls:'browse'
                					});
                            	}},'-',{
                            		text:'优惠单据',iconCls:'modify',click:function(){
                            			grid.pagination.options.url = "getDiscountedLeaseRecord.action";
                            			grid.refresh();
                            		}
                            	}
							]
						}).body
					},
					bbar:{
						url:"getLeaseRecord.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						showDetails:true,
						totalCount:0,
						pageSize:20
					},
					singleMode:true,								//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									new $.dqgb.grid.CheckRow(),
									{title:'车牌号',width:120,alias:'carId'},
									{title:'承租人',width:100,alias:'customerName',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value){
												return true;
											}
											obj.html(gridObj.options.store[row].customerNameRemark);
											return false;
										}
									},
									{title:'起租日期',width:150,alias:'dateBegin',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date));
											return false;
										}
									},
									{title:'截止日期',width:150,alias:'dateEnd',
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
									{title:'所欠租金',width:150,alias:'reletFeeWaiting',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(0 == value){
												obj.html("");
											}else{
												obj.html("￥" + value).css("color","fuchsia");
											}
											return false;
										}
									},
									{title:'取车门店',width:150,alias:'fetchDeptName'},
									{title:'还车门店',width:150,alias:'givebackDeptName'},
									{title:'租赁模式',width:100,alias:'leaseModelRemark'},
									{title:'承租人身份证',width:160,alias:'customerIdCardNumber'},
									{title:'订单状态',width:100,alias:'leaseStatusRemark'},
									{title:'业务员',width:120,alias:'transactorName'},
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
									{title:'业务结算员',width:120,alias:'finisherName'},
									{title:'订单编号',width:350,alias:'serialNumber'},
									{title:'发车人',width:150,alias:'sender'},
									{title:'整备人',width:150,alias:'maker'},
									{title:'发车单',width:80,alias:'leaveTable',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value){
												var icon = $("<div title='查看发车单' class='browse'></div>")
												.appendTo(obj).click(function(){
													$("#lbTable").dqgbBox({
														width : 800,height : 400,
														iconCls : 'find',
														closeBtn : true,
														title : '发车单'
													});
													$("#tablepath").get(0).src = value;
												}).css({
		                                        	"margin-left":"0px",
		                                        	"margin-top":"4px",
		                                        	"cursor":"pointer",
		                                        	"background-repeat":"no-repeat",
		                                        	"width":"16px",
		                                        	"height":"16px"
												});	
											}
											return false;
										}
									},
									{title:'收车单',width:80,alias:'backTable',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value){
												var icon = $("<div title='查看收车单' class='modify'></div>")
												.appendTo(obj).click(function(){
													$("#lbTable").dqgbBox({
														width : 800,height : 400,
														iconCls : 'find',
														closeBtn : true,
														title : '发车单'
													});
													$("#tablepath").get(0).src = value;
												}).css({
		                                        	"margin-left":"0px",
		                                        	"margin-top":"4px",
		                                        	"cursor":"pointer",
		                                        	"background-repeat":"no-repeat",
		                                        	"width":"16px",
		                                        	"height":"16px"
												});	
											}
											return false;
										}
									}
									
								]
				});
				
				//=======按车牌号进行查找====	
				$("#findByCarId").click(function(){
					if(!$.trim($("#specifiedCarId").val())){
						$.dqgb.globalMsg.showMsg("请输入车牌号信息!");
						return;
					}
					$.dqgb.shadowLayer.hide();
					setTimeout(function(){
						grid.pagination.options.extraParam = {
							carId:$.trim($("#specifiedCarId").val())	
						};
						grid.pagination.options.url = "getLeaseRecord.action";			//条件查询
	
						if(grid.pagination.getPageIndex() == 0){
							grid.refresh();
						}else{
							grid.pagination.first.trigger("click");
						}
					},500);
				});
				
				$("#reletDays").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$("#reletFee").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				
				//=======按订单号进行查找====	
				$("#findByOrderSN").click(function(){
					if(!$.trim($("#specifiedOrderId").val())){
						$.dqgb.globalMsg.showMsg("请输订单编号信息!");
						return;
					}
					$.dqgb.shadowLayer.hide();
					setTimeout(function(){
						grid.pagination.options.extraParam = {
							serialNumber:$.trim($("#specifiedOrderId").val())	
						};
						grid.pagination.options.url = "getLeaseRecord.action";			//条件查询
	
						if(grid.pagination.getPageIndex() == 0){
							grid.refresh();
						}else{
							grid.pagination.first.trigger("click");
						}
					},500);
				});
				
				$("#confirmRelet").click(function(){
					if(!$.trim($("#reletDays").val()) || !parseInt($.trim($("#reletDays").val())) || parseInt($.trim($("#reletDays").val())) <= 0){
						$.dqgb.globalMsg.showMsg("请输入正确的续租天数!");
						return;
					}
					$.dqgb.shadowLayer.showMask({mask:'正在办理续租业务,请稍候....'});
					var row = grid.getSelectedRow();
					$.ajax({
            			url:'addReletBusiness.action',
            			type:'post',
            			async:true,
            			data:{
            				reletDays:$.trim($("#reletDays").val()),
            				orderFormId:row[0].serialNumber
            			},
            			success:function(data){
            				if(data.RESULT == 'FAILED'){
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}else{
								$.dqgb.shadowLayer.hideMask();
								$.dqgb.globalMsg.showMsg('续租业务办理成功！');
								grid.refresh();
							}
            			}
            		});
				});
				
				
				//打印订单
				$("#confirmToPrint").click(function(){
					if(!$("#OrderIdToBePrint").val()){
						$.dqgb.globalMsg.showMsg("请输入您要打印的订单号");
						return;
					}
					$.dqgb.shadowLayer.loading({
						url:"gotoFinanceSettlePrintPage.action?serialNumber=" + $("#OrderIdToBePrint").val(),
						width:852,
						height:400,
						title : "结算单" + "(" + $("#OrderIdToBePrint").val() + ")",
						closeBtn:true,
						iconCls:'modify'
					});
				});
				
				$("#confirmToSupply").click(function(){
					if(!$.trim($("#reletFee").val())){
						$.dqgb.globalMsg.showMsg("请输入补缴金额");
						return;
					}
					if($.trim($("#reletFee").val()) == "0"){
						$.dqgb.globalMsg.showMsg("补缴金额不能为0");
						return;
					}
					$.ajax({
						url : 'supplyReletFee.action',
						aysnc:true,type:'post',
						data:{
							money : $.trim($("#reletFee").val()),
							orderId : grid.getSelectedRow()[0].serialNumber
						},success:function(data){
							if(data.RESULT == 'FAILED'){
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}else{
								$.dqgb.shadowLayer.hide();
								$.dqgb.globalMsg.showMsg('费用续交成功！');
								grid.refresh();
							}
						}
					});
				});
			});
		</SCRIPT>
	</head>
	<body>
		<div id="lease">
		</div>
		<div id="nav"></div>
		
		<!-- 按车牌查找 -->
		<div style="display: none; width:250px; height:100px; text-align: center; padding: 10px;
			overflow:hidden; background-color: white; font: 14px 微软雅黑;" id="searchByCarId" >
			车牌号：<input type="text" maxLength="20" id="specifiedCarId"/><br></br>
			<input type="button" value="开始查询" style="width:60px;margin-top:10px;" id="findByCarId"/>
		</div>
		
		<!-- 按订单号查找 -->
		<div style="display: none; width:420px; height:220px; text-align: center; padding: 10px;
			overflow:hidden; background-color: white;  font: 14px 微软雅黑;" id="searchByOrderId" >
			订单号：<input type="text" maxLength="20" style="width:300px;" id="specifiedOrderId"/><br></br>
			<input type="button" value="开始查询" style="width:60px;margin-top:10px;" id="findByOrderSN"/>
		</div>
		
		<!-- 查找订单来打印-->
		<div style="display: none; width:420px; height:220px; text-align: center; padding: 10px;
			overflow:hidden; background-color: white;  font: 14px 微软雅黑;" id="OrderIdToPrint" >
			订单号：<input type="text" maxLength="20" style="width:300px;" id="OrderIdToBePrint"/><br></br>
			<input type="button" value="确认" style="width:50px;margin-top:10px;" id="confirmToPrint"/>
		</div>
		
		<div style="display: none; width:200px; height:100px; text-align: center; padding: 10px;
			overflow:hidden; background-color: white; font:微软雅黑 14px;" id="reletDiv" >
			续租天数：<input type="text" id="reletDays" style="width:150px;" value="1"/>
			<input type="button"  style="width:50px;" id="confirmRelet" value="确认"/>
		</div>
		
		
		<div id='lbTable' style="display: none; background: white;font: 14px 微软雅黑; width : 800px; height: 400px; overflow: auto;">
			<table style="margin:10px auto;">
				<tr>
					<td>
						<img alt="" src="#"  id='tablepath'/>
					</td>
				</tr>
			</table>
		</div>
		
		<div style="display: none; text-align: center; padding: 20px; overflow:hidden; background-color: white; font:微软雅黑 14px;" id="supplimentFee" >
			金额:<input type="text" id='reletFee'/><p>
			<input type="button" id="confirmToSupply" value="确认" style="width:50px;"/>
		</div>
	</body> 
</html>