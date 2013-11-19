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
		<title>车辆管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.tree.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.navigation.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.query.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.ajaxFileUpload.js"></script>
	
		<SCRIPT type="text/javascript">
			$(function(){
				$("#foregift").keyup(function(){						//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#price").keyup(function(){							//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#pricePerHour").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#pricePerKm").keyup(function(){						//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#priceRM").keyup(function(){							//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#pricePerHourRM").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				var grid = "";
				
				var selectedCar = "";
				$("#modifyPrice").click(function(){
					$.ajax({
						url : 'modifyPrice.action',
						async : true, type : 'post',
						data : {
							'car.carId' : grid.getSelectedRow()[0].carId,
							'car.price' : $.trim($("#price").val()),
							'car.foregift' : $.trim($("#foregift").val()),
							'car.priceRM' : $.trim($("#priceRM").val()),
							'car.pricePerHour' : $.trim($("#pricePerHour").val()),
							'car.pricePerHourRM' : $.trim($("#pricePerHourRM").val()),
							'car.pricePerKm' : $.trim($("#pricePerKm").val())
						},
						success : function(data){
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
							}else{
								$.dqgb.shadowLayer.hide();
								grid.refresh();
							}
						}
					});
					
				});
				$.dqgb.button.modifyButtonStyle();
				var carBrand = "";								//车类(BMW)		
				var query = "";
				//修改树样式
				$.dqgb.tree.beforeCreateNode = function(obj){
					if(obj.getNodeAttribute().leaf){
						obj.getNodeAttribute().iconCls = 'modify';
					}
					obj.setNodeAttribute({
						dblclick : function(){
							if(carBrand == obj.getRoot()){
								$.dqgb.shadowLayer.hide();
								var attr = obj.getNodeAttribute();
								setTimeout(function(){
									grid.pagination.options.extraParam = {
										brandId : attr.id ? attr.id : ""	
									};
									grid.pagination.options.url = "getCarByCondition.action";			//条件查询
									if(grid.pagination.getPageIndex() == 0){
										grid.refresh();
									}else{
										grid.pagination.first.trigger("click");
									}
								},500);
							}
						}
					});
					
				};
				
				$.ajax({
					url:'getOrganization.action',
					type:'post',
					async:true,
					data:{
						pageIndex:0,pageSize:1000
					},success:function(data){
						var dt = data.data;
						for(var i = 0; i < data.totalSize; i++){
							$("<option value='" + dt[i].id + "'>" + dt[i].departmentName + "</option>").appendTo($("#orgs"));
							$("<option value='" + dt[i].id + "'>" + dt[i].departmentName + "</option>").appendTo($("#_orgs"));
						}
					}
				});
				
				var lrs = $.dqgb.grid.generateGrid({
					render:"order",
					width:600,
					height:400,
					title:"选择订单",
					iconCls:'modify',
					closeBtn:true,
					bbar:{
						url:"getLeaseRecordsToSupplement.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						manualLoad : true,							//默认是false	
						showDetails:true,
						totalCount:0,
						pageSize:1000
					},
					singleMode:true,								//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									new $.dqgb.grid.CheckRow(),
									{title:'车牌号',width:120,alias:'carId'},
									{title:'订单号',width:200,alias:'serialNumber'},
									{title:'状态',width:120,alias:'statusRemark'}
									],
					listeners:{
						rowChecked : function(gridObj,rowIndex,columnIndex){			//行被checked
							$.dqgb.shadowLayer.loading({
								url:'gotoSupplementFinance.action?serialNumber=' + gridObj.options.store[rowIndex].serialNumber,
								width:620,height:400,
								title:'费用补填',
								closeBtn:true,
								iconCls:'browse'
							});
						}
					}
				});
				lrs.body.hide();
				
				//=============
				grid = $.dqgb.grid.generateGrid({
					render:"user",
					width:'100%',
					height:'100%',
					title:"车辆管理",
					iconCls:'browse',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
							{text:'车辆查找',iconCls:'find',subNodes:[
									{text:'按车牌查找',iconCls:'modify',click:function(){
											$("#search").dqgbBox({
												width:250,height:100,
												title:"按车牌号查询",
												closeBtn:true,
												iconCls:'modify'
											});
										}
									},
									{text:'按品牌查找',iconCls:'browse',click:function(){
											$("#chooseType").dqgbBox({
												width:422,height:260,
												title:"按品牌查询&nbsp;(<font style='color:#E1793A;'>双击品牌查询<</font>)",
												closeBtn:true,
												iconCls:'browse'
											});
											$.ajax({
												url:"getBrandInformation.action",	//获取车品牌数据信息
												type:"post",
												async:true,
												data:{
													pageIndex:0,
													pageSize:1000
												},
												success:function(obj){
													if(obj == null || !obj.data || !obj.data.totalSize){
														return;
													}
													var data = obj.data;
													if(data.totalSize){
														var treeData = new Array();
														for(var i = 0; i < data.totalSize; i++){
															treeData[treeData.length] = new Object();
															treeData[treeData.length - 1].text = data.data[i].remark;
															treeData[treeData.length - 1].id = data.data[i].id;
														}
														carBrand = $.dqgb.tree.getDqgbTree({
															text:'现有品牌',
															container:$("#carBrands").html(""),
															staticTree : true,
															treeData:treeData,
															iconCls:'root'
														});
													}
												}
											});
										}
									},
									{text:'按驻场查找',iconCls:'modify',click:function(){
											$("#findByOrg").dqgbBox({
												width:320,height:160,title:'按驻场查找',modify:'browse',closeBtn:true
											});
										}
									},
									{text:'全部车型',iconCls:'find',click:function(){
											grid.pagination.options.extraParam = {};
											grid.pagination.options.url = "getCar.action";			
											if(grid.pagination.getPageIndex() == 0){
												grid.refresh();
											}else{
												grid.pagination.first.trigger("click");
											}
										}
									}
								]
							},'-',{text:'业务操作',iconCls:'modify',subNodes:[
									{text:'发车',iconCls:'browse',
										click:function(){
											var row = grid.getSelectedRow();
											if(row.length == 0){
												$.dqgb.globalMsg.showMsg('请指定您要发车的车辆！');
												return;
											}
										$.dqgb.shadowLayer.loading({
												url:'gotoSetOutCarPage.action?carId=' + encodeURI(row[0].carId),
												width:460,height:210,
												title:"发车",
												closeBtn:{
													afterClose:function(){
														grid.refresh();
													}
												},
												iconCls:'modify'
											});
										}
									},
									{text:'收车',iconCls:'browse',
										click:function(){
											var row = grid.getSelectedRow();
											if(row.length == 0){
												$.dqgb.globalMsg.showMsg('请指定您要执行收车操作的车辆！');
												return;
											}
											if(row[0].status == '3'){
												$.dqgb.globalMsg.showMsg('该车已经执行过收车操作，无需用次再执行！');
												return;
											}
											$.dqgb.shadowLayer.loading({
												url:'gotoGivebackCarPage.action?carId=' + encodeURI(row[0].carId),
												width:620,height:350,
												title:"收车",
												closeBtn:{
													afterClose:function(){
														grid.refresh();
													}
												},
												iconCls:'modify'
											});
										}
									},
									{text:'补填费用信息',iconCls:'browse',
										click:function(){
											var row = grid.getSelectedRow();
											if(row.length == 0){
												$.dqgb.globalMsg.showMsg('请指定您要补填费用的车辆！');
												return;
											}
											$("#order").hide().dqgbBox({
								    			width:600,height:400
								    		});
											if(lrs.options.bbar.manualLoad == true){
												lrs.pagination.options.extraParam = {
														carId:row[0].carId
												};
												lrs.refresh();
								    		}
											lrs.options.bbar.manualLoad = false;
										}
									}
								]}/* ,'-',{
									text:'维修管理',iconCls:'browse',subNodes:[{
										text:'车辆送修',
										iconCls:'modify',
										click:function(){
											var row = grid.getSelectedRow();
											if(row.length == 0){
												$.dqgb.globalMsg.showMsg('请指定您要送修的车辆！');
												return;
											}
											$.dqgb.shadowLayer.showMask({mask:'正在处理,请稍候...'});
											$.ajax({
												url:'repair.action',
												async:true,type:'post',
												data:{
													id:row[0].id
												},success:function(data){
													if(data.RESULT == "FAILED"){
														$.dqgb.shadowLayer.alert({
															content:data.REASON
														});
													}else{
														$.dqgb.shadowLayer.hideMask();
														$.dqgb.globalMsg.showMsg("数据处理成功");
														setTimeout(function(){
															grid.refresh();
														},600);
													}
												}
											});
										}
									},{
										text:'车辆入库',
										iconCls:'find',
										click:function(){
											var row = grid.getSelectedRow();
											if(row.length == 0){
												$.dqgb.globalMsg.showMsg('请指定您要入库的车辆！');
												return;
											}
											$.dqgb.shadowLayer.showMask({mask:'正在执行入库处理,请稍候...'});
											$.ajax({
												url:'giveBack.action',
												async:true,type:'post',
												data:{
													id:row[0].id
												},success:function(data){
													if(data.RESULT == "FAILED"){
														$.dqgb.shadowLayer.alert({
															content:data.REASON
														});
													}else{
														$.dqgb.shadowLayer.hideMask();
														$.dqgb.globalMsg.showMsg("入库成功");
														setTimeout(function(){
															grid.refresh();
														},600);
													}
												}
											});
										}
									}]
								} */
							]
						}).body
					},
					bbar:{
						url:"getCar.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						showDetails:true,
						totalCount:0,								//初始化当前数量
						pageSize:20
					},
					singleMode:true,								//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									new $.dqgb.grid.CheckRow(),
									{title:'车牌号',width:120,alias:'carId'},
									{title:'品牌',width:200,alias:'brandName',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var rec = gridObj.options.store[row];
											if(rec.carCategory )
												obj.html(rec.brandName + "—" + rec.carCategory);
											else
												obj.html(rec.brandName);
											return false;
										}
									},
									{title:'状态',width:120,alias:'statusRemark'},
									{title:'车主',width:120,alias:'ownerName'},
									{title:'驻场信息',width:120,alias:'departmentName'},
									{title:'车主电话',width:120,alias:'mobilePhone'},
									{title:'车主身份证',width:120,alias:'ownerIdCarNumber'},
									{title:'发动机编号',width:120,alias:'engineNumber'},	
									{title:'车源',width:120,alias:'carSourceRemark'},	
									{title:'车架号',width:120,alias:'bodyNumber'},	
									{title:'登记日期',width:120,alias:'registerDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(parseInt(value.year + 1900) + "年" + parseInt(value.month + 1) + 
													"月" + parseInt(value.date) + "日");
											return false;
										}
									},
									{title:'预览车辆',width:100,alias:'operation',
										beforeDataLoading : function(gridObj,obj,value,row,column){
											var car = gridObj.options.store[row];
											var icon = $("<div title='预览车辆' class='browse'></div>")
											.appendTo(obj).click(function(){
												$.dqgb.shadowLayer.loading({
													width:650,
													height:390,
													title : car.brandName + "&nbsp;" + car.category + "&nbsp;(" + car.carId + ")",
													closeBtn:true,
													url:'browseCar.action?id=' + car.id,
													iconCls:'browse'
												});
											}).css({
	                                        	"margin-left":"0px",
	                                        	"margin-top":"4px",
	                                        	"cursor":"pointer",
	                                        	"background-repeat":"no-repeat",
	                                        	"width":"16px",
	                                        	"height":"16px"
											});
											return false;			//客户化
										}
									},
									
									{title : '押金',width : 100, alias : 'foregift',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value) ;
											return false;
										}
									},
									{title : '租金',width : 100, alias : 'price',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value + "/天") ;
											return false;
										}
									},
									{title : '租金(限里程)',width : 150, alias : 'priceRM',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value + "/天") ;
											return false;
										}
									},
									{title : '超时费',width : 100, alias : 'pricePerHour',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value + "/小时") ;
											return false;
										}
									},
									{title : '超时费(限里程)',width : 160, alias : 'pricePerHourRM',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value + "/小时") ;
											return false;
										}
									},
									{title : '超驶费',width : 100, alias : 'pricePerKm',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value + "/公里") ;
											return false;
										}
									},
									/* {title : '超驶费(限里程)',width : 160, alias : 'pricePerKmRM',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value + "/公里") ;
											return false;
										}
									}, */
									{title : '行驶里程(km)',width : 140, alias : 'km',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(value + "公里") ;
											return false;
										}
									}
								]
				});

				$("#confirmToChangeOrg").click(function(){
					$.ajax({
						url:'modifyCarOrg.action',
						async:true,
						type:'post',
						data:{
							id:grid.getSelectedRow()[0].id,
							organizationId:$("#orgs").get(0).options[$("#orgs").get(0).selectedIndex].value
						},success:function(data){
							if(data.RESULT != 'FAILED'){
								$.dqgb.shadowLayer.hide();
								setTimeout(function(){
									grid.refresh();
								},500);
							}else{
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}
						}
					});
				});

				$("#confirmFindByOrg").click(function(){
					$.dqgb.shadowLayer.hide();
					setTimeout(function(){
						grid.pagination.options.extraParam = {
							organizationId:$("#_orgs").get(0).options[$("#_orgs").get(0).selectedIndex].value	
						};
						grid.pagination.options.url = "getCarByCondition.action";			//条件查询
	
						if(grid.pagination.getPageIndex() == 0){
							grid.refresh();
						}else{
							grid.pagination.first.trigger("click");
						}
					},500);
				});
				
				//=======按车牌号进行查找====如果不传入条件参数则全部查出==		
				$("#findCarId").click(function(){
					$.dqgb.shadowLayer.hide();
					setTimeout(function(){
						grid.pagination.options.extraParam = {
							carId:$.trim($("#specifiedCarId").val())	
						};
						grid.pagination.options.url = "getCarByCondition.action";			//条件查询
	
						if(grid.pagination.getPageIndex() == 0){
							grid.refresh();
						}else{
							grid.pagination.first.trigger("click");
						}
					},500);
				});
				
				
				$("#extraFinance").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$(".digital").keyup(function(){					//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				
				
				$("#km5").click(function(){
					$("#item5").show();
					$("#item_5").hide();
				});
				$("#date5").click(function(){
					$("#item5").hide();
					$("#item_5").show();
				});
				
				$("#km4").click(function(){
					$("#item4").show();
					$("#item_4").hide();
				});
				$("#date4").click(function(){
					$("#item4").hide();
					$("#item_4").show();
				});
				
				$("#km3").click(function(){
					$("#item3").show();
					$("#item_3").hide();
				});
				$("#date3").click(function(){
					$("#item3").hide();
					$("#item_3").show();
				});
				
				$("#km2").click(function(){
					$("#item2").show();
					$("#item_2").hide();
				});
				$("#date2").click(function(){
					$("#item2").hide();
					$("#item_2").show();
				});
				
				$("#km1").click(function(){
					$("#item1").show();
					$("#item_1").hide();
				});
				$("#date1").click(function(){
					$("#item1").hide();
					$("#item_1").show();
				});
			});
		</SCRIPT>
		<style type="text/css">
			#fee td{
				background-color: white;
				padding-left: 5px;
				height: 30px; line-height:30px;
			}
			#accessory td{
				background-color: white;
				height : 24px; line-height:24px;
			}
		</style>
	</head>
	<body style='font-size:14px; font-family:微软雅黑'>
		<div id="user"></div>
		<div id="order" style="position: absolute; left:0px; top:0px;"></div>
		<div id="nav"></div>
		<div style="display: none; width:250px; height:100px; text-align: center; padding: 10px;
			overflow:hidden; background-color: white;" id="search" >
			车牌号：<input type="text" maxLength="20" id="specifiedCarId"/><br></br>
			<input type="button" value="开始查询" style="width:60px;margin-top:10px;" id="findCarId"/>
		</div>
		
		<div style="width:400px; height:240px; padding:10px; display: none;background-color: white;" id="chooseType">
			<div style="padding-top:1px; width:380px;height:235px; overflow:auto; " id="carBrands"> 
			</div>
		</div>
		
		<div style="display: none; overflow: hidden; padding: 10px; text-align:center; background-color: white;" id="changeOrg" >
			<select id="orgs" style="width:180px;"></select>
			<input type="button" value="确认" style="width:60px;margin-top:10px;" id="confirmToChangeOrg"/>
		</div>
		
		<div style="display: none; overflow: hidden; padding: 10px; text-align:center; background-color: white;" id="findByOrg" >
			<select id="_orgs" style="width:180px;"></select>
			<input type="button" value="确认" style="width:60px;margin-top:10px;" id="confirmFindByOrg"/>
		</div>
		
		
		
	</body> 
</html>