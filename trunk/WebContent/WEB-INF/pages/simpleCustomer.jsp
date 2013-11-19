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
		<title>客户管理</title>
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
					render:"user",
					width:'100%',
					height:'100%',
					title:"客户管理",
					iconCls:'customer',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[{text:'增加客户',iconCls:'addRole',click:function(){
										$.dqgb.shadowLayer.loading({
											iconCls:'male',
											title:'新增客户',
											width:600,
											height:270,
											closeBtn:{
												afterClose:function(){
													grid.refresh();
												}
											},
											url:'gotoShortAddCustomerPage.action'
										});
									}
								},'-',{
									text:'查找客户',iconCls:'find',subNodes:[
																			{
																				text:'按证件查找',iconCls:'browse',click:function(){
																					$("#findCustomer").dqgbBox({
																						width:400,height:200,
																						title:"客户查询(<font style='color:fuchsia;'>请输入客户的证件号码</font>)",
																						closeBtn:true,
																						iconCls:'browse'
																					});
																					$("#idCardNumber").query({
																						url:'queryCustomer.action',
																						miniQueryLength:4,									//输入4个字符以后开始查询
																						width:350,
																						height:100,
																						root:'data.data',
																						remindMsg:'没有找到相关订单信息，请确认您的输入是否正确!',	//没有找到数据时的提示
																						param : {
																							pageSize:5,
																							pageIndex:0
																						},
																						keyField:'idCardNumber',							//核心字段
																						fields:['idCardNumber','customerName']				//配置需要显示的字段
																					});
																				}
																			},{
																				text:'按姓名查找',iconCls:'modify',click:function(){
																					$("#findCustomerBN").dqgbBox({
																						width:400,height:200,
																						title:"客户查询(<font style='color:fuchsia;'>请输入客户的姓名</font>)",
																						closeBtn:true,
																						iconCls:'browse'
																					});
																				}
																			}                                     
																		                               
																		                                     
																		                                     
																		                                     
																		]
								}, '-',{
									text:'证件管理',iconCls:'modify',subNodes :[
										{text:'身份证管理',iconCls : 'find',click:function(){
												var row = grid.getSelectedRow();
												if(row.length == 0){
													$.dqgb.globalMsg.showMsg('请选择客户');
													return;
												}
												$.dqgb.shadowLayer.loading({
													iconCls:'male',
													title:'身份证管理',
													width:450,
													height:180,
													closeBtn:{
														afterClose:function(){
															grid.refresh();
														}
													},
													url:'gotoAddImagePathPage.action?customerId=' + row[0].idCardNumber
												});
											}	
										},{text:'驾驶证管理',iconCls:'modify',click:function(){
											var row = grid.getSelectedRow();
											if(row.length == 0){
												$.dqgb.globalMsg.showMsg('请选择客户');
												return;
											}
											$.dqgb.shadowLayer.loading({
												iconCls:'modify',
												title:'驾驶证管理',
												width:450,
												height:180,
												closeBtn:{
													afterClose:function(){
														grid.refresh();
													}
												},
												url:'gotoAddDriverLicensePage.action?customerId=' + row[0].idCardNumber
											});
										}}                                        
									]
								}
							]
						}).body
					},
					bbar:{
						url:"getCustomers.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						showDetails:true,
						totalCount:0,								//初始化当前数量
						pageSize:20
					},
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									new $.dqgb.grid.CheckRow(),
									{title:'客户姓名',width:120,alias:'customerName'},
									{title:'性别',width:100,alias:'gender',
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
									{title:'身份证号码',width:180,alias:'idCardNumber'},
									{title:'联系方式',width:150,alias:'mobilePhone'}, 
									{title:'合同签订日期',width:150,alias:'registerDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value && value.year){
												obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
														"-" + parseInt(value.date));
											}
											return false;
										}
									},
									{title:'合同到期时间',width:150,alias:'endDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value && value.year){
												obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
														"-" + parseInt(value.date));
											}
											return false;
										}
									},
									/* {title:'介绍人',width:150,alias:'introducer'},  */
									{title:'身份证',width:80,alias:'idImgPath',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value){
												var icon = $("<div title='查看身份证' class='browse'></div>")
												.appendTo(obj).click(function(){
													$("#lbTable").dqgbBox({
														width : 800,height : 400,
														iconCls : 'find',
														closeBtn : true,
														title : '客户身份证'
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
									{title:'驾驶证',width:80,alias:'driverLicensePath',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value){
												var icon = $("<div title='查看驾驶证' class='modify'></div>")
												.appendTo(obj).click(function(){
													$("#lbTable").dqgbBox({
														width : 800,height : 400,
														iconCls : 'find',
														closeBtn : true,
														title : '驾驶证'
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
									{title:'客户等级',width:100,alias:'customerLevel'},
									{title:'黑名单',width:100,alias:'blacklist',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value == "1"){
												obj.html("是").css("color","fuchsia");
											}else{
												obj.html("否");
											}
											return false;
										}
									}
								]
				});

				
				$("#find").click(function(){
					$.dqgb.shadowLayer.hide();
					grid.pagination.options.url = 'queryCustomer.action';
					grid.pagination.options.extraParam = {
						idCardNumber:$.trim($("#idCardNumber").val())	
					};
					if(grid.pagination.getPageIndex() == 0){
						grid.refresh();
					}else{
						grid.pagination.first.trigger("click");
					}
				});
				
				$("#findBN").click(function(){
					$.dqgb.shadowLayer.hide();
					grid.pagination.options.url = 'queryCustomerByName.action';
					grid.pagination.options.extraParam = {
						customerName : $.trim($("#customerName").val())	
					};
					if(grid.pagination.getPageIndex() == 0){
						grid.refresh();
					}else{
						grid.pagination.first.trigger("click");
					}
				});
			});
		</SCRIPT>
	</head>
	<body>
		<div id="user">
		</div>
		<div id="nav"></div>
		<div style='width:400px; height:200px; background-color: white; padding: 10px; display: none;' id="findCustomer">
			<input type="text" id="idCardNumber" style='width:300px;'/>
			<input type="button" value="确认" style='width:50px;'id="find"/>
		</div>
		<div style='width:400px; height:200px; background-color: white; padding: 10px; display: none;' id="findCustomerBN">
			<input type="text" id="customerName" style='width:300px;'/>
			<input type="button" value="确认" style='width:50px;'id="findBN"/>
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
	</body> 
</html>