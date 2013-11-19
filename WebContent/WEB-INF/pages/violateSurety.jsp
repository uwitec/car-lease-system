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
		<title>保证金管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.navigation.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.query.js"></script>
		
		<script type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				/* var cars = $.dqgb.grid.generateGrid({
					render:"car",
					width:600,
					height:400,
					title:"选择车辆",
					iconCls:'modify',
					closeBtn:true,
					tbar:{
						element:$("#carNav")
					},
					bbar:{
						url:"getCar.action",
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
									{title:'车型',width:200,alias:'brandName',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var rec = gridObj.options.store[row];
											obj.html(rec.brandName + "—" + rec.category);
											if(rec.category )
												obj.html(rec.brandName + "—" + rec.category);
											else
												obj.html(rec.brandName);
											return false;
										}
									},
									{title:'状态',width:120,alias:'statusRemark'},
									{title:'车主',width:120,alias:'ownerName'}
									],
					listeners:{
						rowChecked : function(gridObj,rowIndex,columnIndex){			//行被checked
							$.dqgb.shadowLayer.loading({
								width:400,
								height:200,
								url:'gotoAddViolateSuretyPage.action?carId=' + encodeURI(gridObj.options.store[rowIndex].carId),
								iconCls:'addRole',
								closeBtn:{
				    				afterClose:function(){
				    					grid.refresh();
				    				}
				    			},
								title:'新增保证金'
							});
						}
					}
				});
				$("#carId").query({
					url:'getCarByCondition.action',
					miniQueryLength:2,									//输入4个字符以后开始查询
					width:300,
					height:100,
					root:'data.data',
					remindMsg:'没有找到相关车辆信息,请确认你的输入是否正确!',	//没有找到数据时的提示
					param : {
						carId:$("#carId").val(),
						pageSize:5,
						pageIndex:0
					},
					keyField:'carId',							//核心字段
					fields:['carId','brandName']				//配置需要显示的字段
				});
				cars.body.hide(); 
				$("#find").click(function(){
					if(!$.trim($("#carId").val())){
						$.dqgb.globalMsg.showMsg("请输入车牌号");
						return;
					}
					cars.pagination.options.extraParam = {
						carId:$.trim($("#carId").val())	
					};
					cars.pagination.options.url = "getCarByCondition.action";			//条件查询

					if(cars.pagination.getPageIndex() == 0){
						cars.refresh();
					}else{
						cars.pagination.first.trigger("click");
					}
				});
				*/
				var grid = $.dqgb.grid.generateGrid({
					render:"violateSurety",
					width:'100%',
					height:'100%',
					title:"违章保证金",
					iconCls:'browse',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[{
									text:'新增保证金',iconCls:'addRole',click:function(){
										/* $("#car").hide().dqgbBox({
							    			width:600,height:400
							    		});
							    		if(cars.options.bbar.manualLoad == true){
							    			cars.refresh();
							    		}
							    		cars.options.bbar.manualLoad = false; */
										$.dqgb.shadowLayer.loading({
											width:400,
											height:200,
											url:'gotoAddViolateSuretyPage.action?carId=' + 'xx',//encodeURI(gridObj.options.store[rowIndex].carId),
											iconCls:'addRole',
											closeBtn:{
							    				afterClose:function(){
							    					grid.refresh();
							    				}
							    			},
											title:'新增保证金'
										});
									}
								},'-',{
									text:'结单',iconCls:'modify',click:function(){
										var row = grid.getSelectedRow();
										if(row.length == 0){
											$.dqgb.globalMsg.showMsg("请选择您要结单的单据!");
											return;
										}
										
										$.dqgb.shadowLayer.confirm({
											content : '请您确认要执行该操作?',
											afterClose : function(){
												$.dqgb.shadowLayer.showMask({mask:'业务处理中,请稍后....'});
												setTimeout(function(){
													var row = grid.getSelectedRow();
													$.ajax({
														url:'givebackViolateSurety.action',
														async:true,
														type:'post',
														data:{
															id:row[0].id,
															money:grid.getSelectedRow()[0].balance
														},
														success:function(data){
															if(data.RESULT != 'FAILED'){
																$.dqgb.shadowLayer.hideMask();
																$.dqgb.globalMsg.showMsg("处理成功！");
																grid.refresh();
															}else{
																$.dqgb.shadowLayer.alert({content:data.REASON});
															}
														}
													});
												},500);
											}
										});
									}
								},'-',{
									text:'车辆续保',iconCls:'find',click:function(){
										var row = grid.getSelectedRow();
										if(row.length == 0){
											$.dqgb.globalMsg.showMsg("请选择您要续保的订单!");
											return;
										}
										if(row[0].givebackDate && row[0].givebackDate.year){
											$.dqgb.globalMsg.showMsg("保证金已经退还!无法续保!");
											return;
										}
										$.dqgb.shadowLayer.showMask({mask:'业务处理中,请稍后....'});
										$.ajax({
											url:'addSuretyDuration.action',
											async:true,
											type:'post',
											data:{
												id:row[0].id,
												duration:'30'
											},
											success:function(data){
												if(data.RESULT != 'FAILED'){
													$.dqgb.shadowLayer.hideMask();
													$.dqgb.globalMsg.showMsg("处理成功！");
													grid.refresh();
												}else{
													$.dqgb.shadowLayer.alert({content:data.REASON});
												}
											}
										});
									}
								},'-',{
									text:'冻结单据',iconCls:'browse',click:function(){
										var row = grid.getSelectedRow();
										if(row.length == 0){
											$.dqgb.globalMsg.showMsg("请选择您要冻结的单据!");
											return;
										}
										if(row[0].money != row[0].balance){
											$.dqgb.globalMsg.showMsg("该保证金已经发生财务信息,无法被冻结。");
											return;
										}
										
										$.dqgb.shadowLayer.confirm({
											content:'该操作将会导致该订单永久性无效，您确认要这样做?',
											afterClose:function(){
												$.dqgb.shadowLayer.showMask({mask:'业务处理中,请稍后....'});
												$.ajax({
													url:'freezoneViolateSurety.action',
													async:true,
													type:'post',
													data:{
														id:row[0].id
													},
													success:function(data){
														if(data.RESULT != 'FAILED'){
															$.dqgb.shadowLayer.hideMask();
															$.dqgb.globalMsg.showMsg("处理成功！");
															grid.refresh();
														}else{
															$.dqgb.shadowLayer.alert({content:data.REASON});
														}
													}
												});
											}
										});
									}
								},'-',{
									text:'查找单据',iconCls:'browse',click:function(){
										$("#queryByCustomerId").css("background-color","white").dqgbBox({
											width:300,height:120,title:'查找保证金',closeBtn:true,
											iconCls:'browse'
										});
										$("#idCardNumber").query({
											url:'queryCustomer.action',
											miniQueryLength:4,									//输入4个字符以后开始查询
											width:200,
											height:100,
											root:'data.data',									//数据根节点
											remindMsg:'没有找到相关客户信息，请确认您的输入是否正确!',	//没有找到数据时的提示
											param : {
												pageSize:5,
												pageIndex:0
											},
											keyField:'idCardNumber',							//核心字段
											fields:['idCardNumber','customerName']				//配置需要显示的字段
										});
									}
								},'-',{text : '补缴费用',iconCls : 'find', click : function(){
									var row = grid.getSelectedRow();
									if(row.length == 0){
										$.dqgb.globalMsg.showMsg("请选择您要补交费用的单据!");
										return;
									}
									$("#givebackVs").dqgbBox({
										width : 300,
										height : 150,
										iconCls : 'find',
										closeBtn : true,
										title : '补缴费用'
									});
									
								}},'-',{text : '财务明细',iconCls : 'modify', click : function(){
									var row = grid.getSelectedRow();
									if(row.length == 0){
										$.dqgb.globalMsg.showMsg("请选择您要查看的单据!");
										return;
									}
									$.dqgb.shadowLayer.loading({
										width:650,
										height:340,
										title : "财务明细",
										closeBtn:true,
										url:'gotoVsFinancePage.action?id=' + row[0].id,
										iconCls:'modify'
									});
								}}
							]
						}).body
					},
					bbar:{
						url:"getViolateSurety.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						showDetails:true,
						totalCount:0,
						pageSize:20
					},
					singleMode:true,							//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									new $.dqgb.grid.CheckRow(),
									/* {title:'车牌号',width:120,alias:'carId'}, */
									{title:'客户姓名',width:180,alias:'customerName'},
									{title:'客户身份证',width:180,alias:'customerId'},
									{title:'缴付金额',width:120,alias:'money'},
									{title:'余额',width:120,alias:'balance'},
									{title:'保证金缴付日期',width:150,alias:'payDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date));
											return false;
										}
									},
									{title:'应当退还日期',width:150,alias:'deadlineDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(!value || !value.year){
												return false;
											}
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date));
											return false;
										}
									},
									{title:'缴费驻场',width:120,alias:'payOrgName'},
									{title:'业务办理人',width:120,alias:'transactorName'}
									
								]
				});
				
				$("#findVs").click(function(){
					if(!$.trim($("#idCardNumber").val())){
						$.dqgb.shadowLayer.globalMsg.showMsg("请输入身份证号码!");
						return;
					}
					$.dqgb.shadowLayer.hide();
					setTimeout(function(){
						grid.pagination.options.extraParam = {
							customerId : $.trim($("#idCardNumber").val())	
						};
						grid.pagination.options.url = "findViolateSurety.action";			//条件查询
	
						if(grid.pagination.getPageIndex() == 0){
							grid.refresh();
						}else{
							grid.pagination.first.trigger("click");
						}
					},500);
				});
				$("#vsMoney").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$("#confirmGiveback").click(function(){
					if(!$.trim($("#vsMoney").val())){
						$.dqgb.globalMsg.showMsg("请输入补交的金额");
						return;
					}
					$.dqgb.shadowLayer.showMask({mask:'业务处理中,请稍后....'});
					$.ajax({
						url:'addVsMoney.action',
						async:true,
						type:'post',
						data:{
							id:grid.getSelectedRow()[0].id,
							money : $.trim($("#vsMoney").val())
						},
						success:function(data){
							if(data.RESULT != 'FAILED'){
								$.dqgb.shadowLayer.hideMask();
								$.dqgb.globalMsg.showMsg("处理成功！");
								grid.refresh();
							}else{
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}
						}
					});
				});
			});
		</script>
	</head>
	<body style="font: 14px 微软雅黑;">
		<div id="violateSurety"></div>
		<div id="nav"></div>
		<div id="car" style="position: absolute; left:0px; top:0px;"></div>
		<!-- <div id="carNav" style="font:14px 微软雅黑;">
			车牌号&nbsp;<input type="text" id="carId"/>
			<input type="button" value="查找" style="width:50px" id="find"/>
		</div> -->
		<div id="queryByCustomerId" style="font:14px 微软雅黑; padding: 10px; text-align: center; display: none;">
			身份证号&nbsp;<input type="text" style="width:200px;" id="idCardNumber"/>
			<input type="button" value="查找" style="width:50px; margin-top:15px;" id="findVs"/>
		</div>
		
		<div id="givebackVs" style="display: none; font:14px 微软雅黑; padding:20px; text-align: center; background-color: white;">
			金额&nbsp;<input type="text" style="width:200px;" id="vsMoney" value="0" maxlength="8"/>
			<input type="button" value="确认" style="width:50px; margin-top:15px;" id="confirmGiveback"/>
		</div>
		
	</body>
</html>