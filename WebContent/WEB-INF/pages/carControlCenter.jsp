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
		<title>车辆调度管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.tree.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.navigation.2.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				var carBrand = "";								//车类(BMW)		
				var carCategory = "";							//车系(7系)
				var systemOrg = "";								//驻场信息
				
				//修改树样式
				$.dqgb.tree.beforeCreateNode = function(obj){
					if(obj.getNodeAttribute().leaf){
						obj.getNodeAttribute().iconCls = 'modify';
					}
					obj.setNodeAttribute({
						dblclick : function(){
							if(carBrand == obj.getRoot()){			//车型总类处理
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
							}else if(systemOrg == obj.getRoot()){		//驻场查询
								var attr = obj.getNodeAttribute();
								if(!attr.leaf){
									return;
								}
								$.dqgb.shadowLayer.hide();
								setTimeout(function(){
									$.extend(grid.pagination.options.extraParam,{
										organizationId : attr.id
									});
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
				
				//=============
				var grid = $.dqgb.grid.generateGrid({
					render:"user",
					width:'100%',
					height:'100%',
					title:"预约管理",
					iconCls:'iconCls',
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
									{text:'按品牌查找',iconCls:'modify',click:function(){
											$("#chooseType").dqgbBox({
												width:422,height:260,
												title:"按品牌查询&nbsp;(<font style='color:#E1793A;'>双击品牌查询</font>)",
												closeBtn:true,
												iconCls:'iconCls'
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
									},{text:'全部车型',iconCls:'modify',click:function(){
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
							},'-',{text:'按驻场查找',iconCls:'modify',
								click:function(){
									$("#companyTree").dqgbBox({
										width:300,height:200,
										title:"按驻场查询&nbsp;(<font style='color:#E1793A;'>双击驻场查询</font>)",
										closeBtn:true,
										iconCls:'modify'
									});
									//获取组织树
									$.ajax({
										url:"getOrganization.action",
										type:'post',
										async:true,
										data:{
											pageIndex:0,
											pageSize:10000
										},
										success : function(data){
											if(!data || !data.totalSize){
												return;
											}
											var treeData = new Array();
											for(var i = 0; i < data.totalSize; i++){
												treeData[treeData.length] = new Object();
												treeData[treeData.length - 1].text = data.data[i].departmentName;
												treeData[treeData.length - 1].id = data.data[i].id;
												if(data.data[i].id == $("#id").val()){						//设置部门名称
													$("#departmentName").val(data.data[i].departmentName);
												}
											}
											systemOrg = $.dqgb.tree.getDqgbTree({
												text:'系统现有驻场',
												container:$("#companyTree").html(""),
												staticTree : true,
												treeData:treeData,
												iconCls:'root'
											});
										}
									});
								}
							},'-',{text:'闲置车辆',iconCls:'modify',
								click:function(){
									$.extend(grid.pagination.options.extraParam,{
										status : '3'
									});
									grid.pagination.options.url = "getCarByCondition.action";			//条件查询
				
									if(grid.pagination.getPageIndex() == 0){
										grid.refresh();
									}else{
										grid.pagination.first.trigger("click");
									}
								}
							}]
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
									{title:'车型',width:246,alias:'brandName',
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
									{title:'车辆状态',width:100,alias:'statusRemark'},
									{title:'驻场信息',width:200,alias:'departmentName'},
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
									{title:'预约',width:100,alias:'browseOrders',
										beforeDataLoading : function(gridObj,obj,value,row,column){
											var car = gridObj.options.store[row];
											var icon = $("<div title='查看近期预约记录' class='modify'></div>")
											.appendTo(obj).click(function(){
												$.dqgb.shadowLayer.loading({
													width:850,
													height:400,
													title : "近期预约记录-" + car.brandName + "&nbsp;" + car.category + "&nbsp;(" + car.carId + ")",
													closeBtn:{
														afterClose:function(){
															grid.refresh();
														}
													},
													url:'gotoBrowseOrderFormsPages.action?carId=' + encodeURI(car.carId),
													iconCls:'modify'
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
									{title:'当前预约',width:100,alias:'bookCount',
										beforeDataLoading : function(gridObj,obj,value,row,column){
											obj.html(value + "次");
											return false;
										}
									}
								]
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
				
			});
		</SCRIPT>
	</head>
	<body>
		<div id="user">
		</div>
		<div id="nav"></div>
		
		<div style="display: none; width:250px; height:100px; text-align: center; padding: 10px;
			overflow:hidden; background-color: white;" id="search" >
			车牌号：<input type="text" maxLength="20" id="specifiedCarId"/><br></br>
			<input type="button" value="开始查询" style="width:60px;margin-top:10px;" id="findCarId"/>
		</div>
		
		<div style="width:400px; height:240px; padding:10px; display: none;background-color: white;" id="chooseType">
			<div style="padding-top:1px; width:380px;height:235px; /* float: left; border:1px solid #ACA899; */ overflow:auto; " id="carBrands"> 
			</div>
			<!-- <div style="padding-top:1px; margin-left:15px; width:190px;height:235px; float: left; border:1px solid #ACA899;
				overflow:auto; " id="carCategorys"> 
			</div> -->
		</div>
		
		<div style="float:left; width:300px; overflow:auto;display:none;background-color:white;
				height:200px; text-align: center;"  id="companyTree" >
		</div>
	</body> 
</html>