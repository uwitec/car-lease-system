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
		<title>档案管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.navigation.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.query.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				var cars = $.dqgb.grid.generateGrid({
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
				    			title:'新增档案',
				    			closeBtn:{
				    				afterClose:function(){
				    					grid.refresh();
				    				}
				    			},
				    			url:'gotoAddFilePage.action?carId=' + encodeURI(gridObj.options.store[rowIndex].carId),
				    			iconCls:'addRole',
				    			width:420,height:270
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
				
				
				//=============
				var grid = $.dqgb.grid.generateGrid({
					render:"user",
					width:'100%',
					height:'100%',
					title:"档案管理",
					iconCls:'modify',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[{text:'新增档案',iconCls:'addRole',
							    	click:function(){
							    		$("#car").hide().dqgbBox({
							    			width:600,height:400
							    		});
							    		if(cars.options.bbar.manualLoad == true){
							    			cars.refresh();
							    		}
							    		cars.options.bbar.manualLoad = false;
							      	}
							      },'-',
							      {text:'修改档案',iconCls:'modify',
							    	click:function(){
							    		var row = grid.getSelectedRow();
							    		if(row.length == 0){
							    			$.dqgb.globalMsg.showMsg("请选择您要修改的档案信息!");
							    			return;
							    		}
							    		$.dqgb.shadowLayer.loading({
							    			title:'修改档案',
							    			closeBtn:{
							    				afterClose:function(){
							    					grid.refresh();
							    				}
							    			},
							    			url:'gotoModifyFilePage.action?id=' + row[0].id,
							    			iconCls:'modify',
							    			width:420,height:270
							    		});
							      	}
							      },'-',
							      {text:'档案查找',iconCls:'find',
								    	click:function(){
								    		$("#filefindwnd").dqgbBox({
								    			width:300,height:120,title:'档案查找',iconCls:'find',closeBtn:true
								    		});
								      	}
								  }
							]
						}).body
					},
					bbar:{
						url:"getFileInformation.action",
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
									{title:'车牌号',width:150,alias:'carId'},
									{title:'档案类型',width:120,alias:'fileTypeRemark'},
									{title:'档案编号',width:200,alias:'fileIdentifyNumber'},
									{title:'办理日期',width:150,alias:'dateCheckIn',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value && value.year){
												obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
														"-" + parseInt(value.date));
											}
											return false;
										}
									},
									{title:'到期时间',width:150,alias:'usefulLife',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value && value.year){
												obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
														"-" + parseInt(value.date));
											}
											return false;
										}
									},
									{title:'删除档案',width:150,alias:'operation',
										beforeDataLoading : function(gridObj,obj,value,row,column){
											var car = gridObj.options.store[row];
											var icon = $("<div title='点击删除档案信息' class='deleteUser'></div>")
											.appendTo(obj).click(function(){
												$.dqgb.shadowLayer.confirm({
													content:'该操作将会导致数据丢失,您确认要继续执行该操作?',
													afterClose:function(){
														$.dqgb.shadowLayer.showMask({mask:'正在删除档案信息,请稍后...'});
														$.ajax({
															url:'deleteFile.action',
															async:true,
															type:'post',
															data:{
																id:gridObj.options.store[row].id
															},
															success:function(data){
																if(data.RESULT == 'FAILED'){
																	$.dqgb.shadowLayer.alert({content:data.REASON});
																}else{
																	$.dqgb.shadowLayer.hideMask();
																	$.dqgb.globalMsg.showMsg("删除操作成功!");
																	grid.refresh();
																}
															}
														});
													}
												});
											}).css({
	                                        	"margin-left":"0px","margin-top":"4px","cursor":"pointer",
	                                        	"background-repeat":"no-repeat","width":"16px","height":"16px"
											});
											return false;			//客户化
										}
									},
									{title:'档案',width:100,alias:'filePath',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value){
												var icon = $("<div title='查看证件' class='browse'></div>")
												.appendTo(obj).click(function(){
													$("#lbTable").dqgbBox({
														width : 800,height : 400,
														iconCls : 'find',
														closeBtn : true,
														title : gridObj.options.store[row].fileTypeRemark
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
				
				$("#ffind").click(function(){
					if(!$.trim($("#fcarId").val())){
						$.dqgb.globalMsg.showMsg("请输入车牌号");
						return;
					}
					grid.pagination.options.extraParam = {
						carId:$.trim($("#fcarId").val())	
					};
					grid.pagination.options.url = "getFileByCar.action";			//条件查询

					if(grid.pagination.getPageIndex() == 0){
						grid.refresh();
					}else{
						grid.pagination.first.trigger("click");
					}
					$.dqgb.shadowLayer.hide();
				});
			});
		</SCRIPT>
	</head>
	<body>
		<div id="user">
		</div>
		<div id="nav"></div>
		
		<div id="car" style="position: absolute; left:0px; top:0px;"></div>
		<div id="carNav" style="font:14px 微软雅黑;">
			车牌号&nbsp;<input type="text" id="carId"/>
			<input type="button" value="查找" style="width:50px" id="find"/>
		</div>
		<div id="filefindwnd" style="font:14px 微软雅黑;display: none;background-color: white; width: 300px; height: 200px; padding: 20px;">
			车牌号&nbsp;<input type="text" id="fcarId"/>
			<input type="button" value="查找" style="width:50px" id="ffind"/>
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