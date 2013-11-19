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
		<title>事故管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/jquery-ui-1.8.12.custom.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-ui-1.8.12.custom.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.navigation.2.0.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				
				//=============
				var grid = $.dqgb.grid.generateGrid({
					render:"user",
					width:'100%',
					height:'100%',
					title:"事故统计${year}",
					iconCls:'modify',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
							      {text:'事故查询',iconCls:'find',click:function(){
							    		$("#datePanel").dqgbBox({
							    			width:400,height:200,title:'按年查询',iconCls:'find',closeBtn:true
							    		});
							      	}
							      },'-',{text:'费用统计',iconCls:'modify',click:function(){
							    	  $("#feeSum").dqgbBox({
								    		 width:400,height:250,
								    		 title:'养护费用统计',closeBtn:true,iconCls:'modify'
								    	  });
								      }}
							]
						}).body
					},
					bbar:{
						url:"getAccidentReport.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						extraParam:{
							date : '${year}-${month}-1'
						},
						showDetails:true,
						totalCount:0,
						pageSize:20
					},
					singleMode:true,								//单选模式
					columnModel:[
									new $.dqgb.grid.RowNumber(),//rowNumber和checkRow的位置必须这样放,也可以不要,alias不能使用rowNumber 和checkBox
									new $.dqgb.grid.CheckRow(),
									{title:'月份',width:120,alias:'yearmonth',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value.indexOf("/") != -1)
												return true;
											else{
												obj.html("年度总计").css('color','fuchsia');
											}
										}
									},
									{title:'停运费',width:100,alias:'delayFee',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											if(gridObj.options.store[row].yearmonth.indexOf("/") == -1){
												obj.css('color','fuchsia');
											}
											return false;
										}
									},
									{title:'车损费',width:100,alias:'frayFee',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											if(gridObj.options.store[row].yearmonth.indexOf("/") == -1){
												obj.css('color','fuchsia');
											}
											return false;
										}
									},
									{title:'维修费',width:100,alias:'repairFee',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											if(gridObj.options.store[row].yearmonth.indexOf("/") == -1){
												obj.css('color','fuchsia');
											}
											return false;
										}
									},
									{title:'保险索赔费',width:120,alias:'insuranceFee',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											if(gridObj.options.store[row].yearmonth.indexOf("/") == -1){
												obj.css('color','fuchsia');
											}
											return false;
										}
									},
									{title:'其它费用',width:100,alias:'otherFee',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											if(gridObj.options.store[row].yearmonth.indexOf("/") == -1){
												obj.css('color','fuchsia');
											}
											return false;
										}
									},
									{title:'事故次数',width:100,alias:'counts',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(gridObj.options.store[row].yearmonth.indexOf("/") == -1){
												obj.css('color','fuchsia');
											}
											return true;
										}
									}
								]
				});
				
				$("#confirm").click(function(){
					var row = grid.getSelectedRow()[0];
					$.ajax({
						url:'modifyAccident.action',async:true,type:'post',
						data:{
							'acc.id':row.id,
							'acc.accidentDate':$.trim($("#accidentDate").val()),
							'acc.dalayFee':$.trim($("#dalayFee").val()),
							'acc.frayFee':$.trim($("#frayFee").val()),
							'acc.repairFee':$.trim($("#repairFee").val()),
							'acc.insuranceFee':$.trim($("#insuranceFee").val()),
							'acc.remark':$.trim($("#remark").val()),
							'acc.carId':row.carId,
							'acc.serialNumber':row.serialNumber,
							'acc.status':0,
							'acc.otherFee':$.trim($("#otherFee").val())
						},success:function(data){
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}else{
								$.dqgb.shadowLayer.hide();
								grid.refresh();
								$.dqgb.globalMsg.showMsg("事故记录修改成功！");
							}
						}
					});
				});
				
				$("#accidentDate").datepicker({
					dateFormat: 'yy-mm-dd',
					monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/']
				});
				
				
				$("#confirmToFind").click(function(){
					if(!$.trim($("#date").val())){
						$.dqgb.globalMsg.showMsg("请您指定您要查询的年度信息!");
						return;
					}
					$.dqgb.shadowLayer.hide();
					grid.pagination.options.extraParam = {
						'date':$.trim($("#date").val()) + "-9-1"
					};
					if(grid.pagination.getPageIndex() == 0){
						grid.refresh();
					}else{
						grid.pagination.first.trigger("click");
					}
					grid.header.setTitle("事故统计" + $.trim($("#date").val()));
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
				var _year = '${year}';
				var _month = '${month}';
				
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
				
				$("#date").keyup(function(){						//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#dalayFee").keyup(function(){						//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#repairFee").keyup(function(){						//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#frayFee").keyup(function(){						//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#insuranceFee").keyup(function(){						//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#otherFee").keyup(function(){						//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
			});
		</SCRIPT>
	</head>
	<body>
		<div id="user">
		</div>
		<div id="nav"></div>
		
		<div style="font: 14px 微软雅黑;background: white; padding: 10px; display: none;" id="modify">
			<table style="margin: 0px auto;">
				<tr>
					<td>事故日期:</td>
					<td><input type="text" id="accidentDate"/></td>
				</tr>
				<tr>
					<td>事故描述:</td>
					<td><input type="text" id="remark"/></td>
				</tr>
				<tr>
					<td>停运费:</td>
					<td><input type="text" id="dalayFee"/></td>
				</tr>
				<tr>
					<td>维修费:</td>
					<td><input type="text" id="repairFee"/></td>
				</tr>
				<tr>
					<td>车损费:</td>
					<td><input type="text" id="frayFee"/></td>
				</tr>
				<tr>
					<td>保险索赔费:</td>
					<td><input type="text" id="insuranceFee"/></td>
				</tr>
				<tr>
					<td>其它费:</td>
					<td><input type="text" id="otherFee"/></td>
				</tr>
				<tr>
					<td align="center" colspan="2"><input type="button" id="confirm" value="确定" style="width:50px; margin-top:20px; ">  </td>
				</tr>
			</table>
		</div>
		
		<div id="datePanel" style="width:400px; height:300px; padding:10px; font: 14px 微软雅黑;
		 text-align: center; display: none; background-color: white;">
			请输入你要查询的年份:<select id="date" >
									<option>2011</option>
									<option>2012</option>
									<option>2013</option>
									<option>2014</option>
									<option>2015</option>
									<option>2016</option>
									<option>2017</option>
									<option>2018</option>
									<option>2019</option>
									<option>2020</option>
									<option>2021</option>
									<option>2022</option>
									<option>2023</option>
									<option>2024</option>
									<option>2025</option>
									<option>2026</option>
									<option>2027</option>
									<option>2028</option>
									<option>2029</option>
									<option>2030</option>
								</select><p>
			<input type="button" value="确认" style="width:50px;" id="confirmToFind" />
		</div>
		
		<div id="feeSum" style="font:14px 微软雅黑; display: none; padding: 10px; text-align: center;background-color: white;">
			年度：<select id="year" style="width:80px;">
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