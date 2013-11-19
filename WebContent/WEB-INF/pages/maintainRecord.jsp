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
		<title>养护记录</title>
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
				    			title:'新增养护记录',
				    			closeBtn:{
				    				afterClose:function(){
				    					grid.refresh();
				    				}
				    			},
				    			url:'gotoAddMaintainRecordPage.action?carId=' + encodeURI(gridObj.options.store[rowIndex].carId),
				    			iconCls:'addRole',
				    			width:400,height:270
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
				
				var grid = $.dqgb.grid.generateGrid({
					render:"mr",
					width:'100%',
					height:'100%',
					title:"车辆养护记录",
					iconCls:'modify',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
							      {text:'新增养护记录',iconCls:'addRole',click:function(){
										$("#car").hide().dqgbBox({
											width:600,height:400
										});
										if(cars.options.bbar.manualLoad == true){
											cars.refresh();
										}
										cars.options.bbar.manualLoad = false;
							      }},'-',{text:'修改养护记录',iconCls:'modify',click:function(){
							    	  	var row = grid.getSelectedRow();
										if(row.length == 0){
											$.dqgb.globalMsg.showMsg('请指定您要修改的养护记录！');
											return;
										}
										$.dqgb.shadowLayer.loading({
											iconCls:'modify',
											title:'修改养护记录',
											width:400,
											height:270,
											closeBtn:{
												afterClose:function(){
													grid.refresh();
												}
											},
											url:'gotoModifyMaintainRecordPage.action?id=' + row[0].id
										});							    	  
							    	  
								  }},/* '-',{text:'删除养护记录',iconCls:'deleteRole',click:function(){
									  	var row = grid.getSelectedRow();
										if(row.length == 0){
											$.dqgb.globalMsg.showMsg('请指定您要删除的养护记录！');
											return;
										}
										
										$.dqgb.shadowLayer.showMask({mask:"正在删除养护信息,请稍候..."});
										$.ajax({
											url:'deleteMaintainRecord.action',
											async:true,type:'post',
											data:{
												id:row[0].id
											},
											success:function(data){
												if(data.RESULT == "FAILED"){
													$.dqgb.shadowLayer.alert({
														content:data.REASON
													});
												}else{
													$.dqgb.shadowLayer.hideMask();
													$.dqgb.globalMsg.showMsg("数据删除成功");
													setTimeout(function(){
														grid.refresh();
													},600);
												}
											}
										});
							      }}, */'-',{text:'查询养护记录',iconCls:'find',click:function(){
							    	  $("#querys").dqgbBox({
							    		 width:360,height:200,
							    		 title:'按车牌查找',iconCls:'find',closeBtn:true
							    	  });
							    	  $("#queryCarId").query({
											url:'getCarByCondition.action',
											miniQueryLength:2,									//输入4个字符以后开始查询
											width:260,
											height:100,
											root:'data.data',
											remindMsg:'没有找到相关车辆信息,请确认你的输入是否正确!',	//没有找到数据时的提示
											param : {
												carId:$("#queryCarId").val(),
												pageSize:5,
												pageIndex:0
											},
											keyField:'carId',							//核心字段
											fields:['carId','brandName']				//配置需要显示的字段
										});
							      }},'-',{text:'费用统计',iconCls:'modify',click:function(){
							    	  $("#feeSum").dqgbBox({
							    		 width:400,height:250,
							    		 title:'养护费用统计',closeBtn:true,iconCls:'modify'
							    	  });
							      }}
							]
						}).body
					},
					bbar:{
						url:"getMaintainRecord.action",
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
									{title:'养护日期',width:180,alias:'maintainDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(parseInt(value.year + 1900) + "年" + parseInt(value.month + 1) + 
													"月" + parseInt(value.date) + "日");
											return false;
										}
									},
									{title:'养护费用',width:120,alias:'money'},
									{title:'养护登记人',width:120,alias:'transactorName'},
									{title:'养护内容',width:400,alias:'remark'}
								]
				});
				
				$("#findCars").click(function(){
					if(!$.trim($("#queryCarId").val())){
						$.dqgb.globalMsg.showMsg("请输入车牌号");
						return;
					}
					$.dqgb.shadowLayer.hide();
					setTimeout(function(){
						grid.pagination.options.extraParam = {
							carId:$.trim($("#queryCarId").val())	
						};
						grid.pagination.options.url = "getMaintainRecordByCar.action";			//条件查询

						if(grid.pagination.getPageIndex() == 0){
							grid.refresh();
						}else{
							cars.pagination.first.trigger("click");
						}
					},500);
				});
				
				$("#querySum").click(function(){
					$.ajax({
						url:'getRepairFee.action',
						type:'post',
						async:true,
						data:{
							date:$("#year").get(0).options[$("#year").get(0).selectedIndex].value + "-" + $("#month").get(0).options[$("#month").get(0).selectedIndex].value + "-" + "1"
						},success:function(data){
							$("#result").html("您所查询的月份的养护费用总计￥" + data.data + "整").css("text-incedent","2em");
						}
					});
				});
				
				var today = '${DATE}';
				_year = today.split("-")[0];
				_month = today.split("-")[1];
				
				for(var i = 0; i < $("#year").get(0).options.length; i++){
					if($("#year").get(0).options[i].value == _year){
						$("#year").get(0).selectedIndex = i;
						break;
					}
				}
				for(var i = 0; i < $("#month").get(0).options.length; i++){
					if($("#month").get(0).options[i].value == _month){
						$("#month").get(0).selectedIndex = i;
						break;
					}
				}
				
			});
		</script>
	</head>
	<body>
		<div id="mr"></div>
		<div id="nav"></div>
		<div id="car" style="position: absolute; left:0px; top:0px;"></div>
		<div id="carNav" style="font:14px 微软雅黑;">
			车牌号&nbsp;<input type="text" id="carId"/>
			<input type="button" value="查找" style="width:50px" id="find"/>
		</div>
		
		<div id="querys" style="font:14px 微软雅黑; display: none; padding: 10px; text-align: center;background-color: white;">
			车牌号&nbsp;<input type="text" id="queryCarId" style="width:200px;"/><br>
			<input type="button" value="查找" style="width:50px;margin-top: 20px;" id="findCars"/>
		</div>
		
		<div id="feeSum" style="font:14px 微软雅黑; display: none; padding: 10px; text-align: center;background-color: white;">
			年度：<select id="year" style="width:80px;">
				<option value="2010">2010</option>
				<option value="2011">2011</option>
				<option value="2012">2012</option>
				<option value="2013">2013</option>
				<option value="2014">2014</option>
				<option value="2015">2015</option>
				<option value="2016">2016</option>
				<option value="2017">2017</option>
				<option value="2018">2018</option>
				<option value="2019">2019</option>
				<option value="2020">2020</option>
				<option value="2021">2021</option>
				<option value="2022">2022</option>
				<option value="2023">2023</option>
				<option value="2024">2024</option>
				<option value="2025">2025</option>
				<option value="2026">2026</option>
				<option value="2027">2027</option>
				<option value="2028">2028</option>
				<option value="2029">2029</option>
				<option value="2030">2030</option>
			</select>
			
			月份：<select id="month" style="width:80px;">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
				<option value="10">10</option>
				<option value="11">11</option>
				<option value="12">12</option>
			</select>
			<br>
			<input type="button" id="querySum" style="width:50px; margin-top:20px;" value="查询"/>
			
			<p id="result"></p>
		</div>
		
	</body>
</html>