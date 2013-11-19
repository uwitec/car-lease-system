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
		<title>违章记录</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/jquery-ui-1.8.12.custom.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-ui-1.8.12.custom.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.navigation.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.query.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				var grid = $.dqgb.grid.generateGrid({
					render:"vr",
					width:'100%',
					height:'100%',
					title:"违章记录管理",
					iconCls:'modify',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[{text:'新增记录',iconCls:'addRole',
							    	click:function(){
							    		$("#vrContent").dqgbBox({
							    			width : 450,
							    			height : 200,
							    			iconCls : 'addRole',
							    			title : '新增记录',
							    			closeBtn : {
							    				afterClose : function(){
							    					grid.refresh();
							    				}
							    			}
							    		});
							      	}
							      },'-',
							      {text:'查找记录',iconCls:'find',subNodes:[
							       		{text:'按客户查找',iconCls : 'modify',click:function(){
									    	  $("#findVr").dqgbBox({
									    		  width : 320,height:100,
									    		  iconCls : 'modify', title : '按客户查找(请输入客户身份证)',
									    		  closeBtn : true
									    	  });
									      }
							       		},
							       		{text:'按车牌查找',iconCls : 'browse',click:function(){
									    	  $("#findVrByCar").dqgbBox({
									    		  width : 320,height:100,
									    		  iconCls : 'modify', title : '按车牌查找(请输入车牌号)',
									    		  closeBtn : true
									    	  });
									      }
							       		}
							     	
							       ]
							      }
							]
						}).body
					},
					bbar:{
						url:"getViolateRecord.action",
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
									{title:'违章车辆',width:120,alias:'carId'},
									{title:'责任人',width:100,alias:'customerName'},
									{title:'订单号',width:180,alias:'serialNumber',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(!value){
												obj.html("----------");
												return false;
											}
											return true;
										}
									},
									{title:'罚款金额',width:100,alias:'violateMoney',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},
									{title:'手续费',width:100,alias:'poundage',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},
									{title:'违章日期',width:150,alias:'violateDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds);
											return false;
										}
									},
									{title:'用车类型',width:100,alias:'innerUsage',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value==0){
												obj.html("业务用车");
											}else{
												obj.html("内部用车").css("color","fuchsia");
											}
											return false;
										}
									},
									{title:'违章内容',width:250,alias:'remark'},
									{title:'业务员',width:100,alias:'transactorName'}
								]
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
				
				$("#vlDate").datepicker({
					showSecond: true, //显示秒
					timeFormat: 'hh:mm:ss',//格式化时间
					stepSecond: 10,
					dateFormat: 'yy-mm-dd',
					monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/'],
					onClose:function(text,inst){
					}
				});
				
				$("#vMoney").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$(".digital").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$("#poundage").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$("#confirm").click(function(){
					if(!$.trim($("#vMoney").val())){
						$.dqgb.globalMsg.showMsg("请填写费用信息");
						return;
					}
					if(!$.trim($("#poundage").val())){
						$.dqgb.globalMsg.showMsg("请填写手续费");
						return;
					}
					if(!$.trim($("#serialNumber").val())){
						$.dqgb.globalMsg.showMsg("请填写单据号");
						return;
					}
					if(!$.trim($("#remark").val())){
						$.dqgb.globalMsg.showMsg("请填写违章内容");
						return;
					}
					if(!$.trim($("#vlDate").val())){
						$.dqgb.globalMsg.showMsg("请注明违章日期");
						return;
					}
					if(!$.trim($("#seconds").val())){
						$("#seconds").val(00);
					}
					if(!$.trim($("#minutes").val())){
						$("#minutes").val(00);
					}
					if(parseInt($.trim($("#seconds").val())) > 59){
						$.dqgb.globalMsg.showMsg("秒数不能超过59");
						return;
					}else if(parseInt($.trim($("#seconds").val())) < 10){
						$("#seconds").val("0" + parseInt($.trim($("#seconds").val())));
					}
					if(parseInt($.trim($("#minutes").val())) > 59){
						$.dqgb.globalMsg.showMsg("秒数不能超过59");
						return;
					}else if(parseInt($.trim($("#minutes").val())) < 10){
						$("#minutes").val("0" + parseInt($.trim($("#minutes").val())));
					}
					$.dqgb.shadowLayer.showMask({mask : "正在提交数据信息,请稍后..."});
					$.ajax({
						url:'addViolateRecord.action',
						async : true, type : 'post',
						data : {
							'vr.carId' : $.trim($("#serialNumber").val()),
							'vr.violateDate' : $.trim($("#vlDate").val()) + " " + $.trim($("#hour").val()) + ":" + $.trim($("#minutes").val()) + ":" + $.trim($("#seconds").val()),
							'vr.remark' : $.trim($("#remark").val()),
							'vr.violateMoney' : $.trim($("#vMoney").val()),
							'vr.poundage' : $.trim($("#poundage").val())
						},success : function(data){
							$.dqgb.shadowLayer.hideMask();
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
							}else{
								grid.pagination.options.extraParam = {
	    						};
	    						grid.pagination.options.url = "getViolateRecord.action";			//条件查询
								grid.refresh();
							}
						}
					});
				});
				
				$("#find").click(function(){
					if(!$.trim($("#idCardNumber").val())){
						$.dqgb.globalMsg.showMsg("请输入客户身份证号码");
						return;
					}
					$.dqgb.shadowLayer.hide();
					/* $.dqgb.shadowLayer.showMask({mask:'正在查找违章信息，请稍后'}); */
					grid.pagination.options.extraParam = {
						customerId : $.trim($("#idCardNumber").val())	
					};
					grid.pagination.options.url = "getViolateRecordByCustomer.action";			//条件查询

					if(grid.pagination.getPageIndex() == 0){
						grid.refresh();
					}else{
						grid.pagination.first.trigger("click");
					}
				});
				$("#findByCar").click(function(){
					if(!$.trim($("#fCarId").val())){
						$.dqgb.globalMsg.showMsg("请输入车牌号");
						return;
					}
					$.dqgb.shadowLayer.hide();
					grid.pagination.options.extraParam = {
						carId : $.trim($("#fCarId").val())	
					};
					grid.pagination.options.url = "getViolateRecordByCar.action";			//条件查询

					if(grid.pagination.getPageIndex() == 0){
						grid.refresh();
					}else{
						grid.pagination.first.trigger("click");
					}
				});
			});
		</SCRIPT>
		
		<style type="text/css">
			#vrContent td{
				background-color: white;
				height: 25px;
			}
		
		</style>
	</head>
	<body>
		<div id="vr">
		</div>
		<div id="nav"></div>
		
		<div id="vrContent" style="background-color: white; font:14px 微软雅黑; display: none;">
			<table cellpadding="0" cellspacing="0" style="margin: 10px auto;">
				<tr>
					<td>车牌号</td>
					<td><input type="text" id="serialNumber" style="width: 300px;"/></td>
				</tr>
				<tr>
					<td>违章日期&nbsp;&nbsp;</td>
					<td>
						<input type="text" id="vlDate" style="width: 120px;"/>
						<select id="hour"  style="width: 50px;">
							<option>0</option>
							<option>1</option>
							<option>2</option>
							<option>3</option>
							<option>4</option>
							<option>5</option>
							<option>6</option>
							<option>7</option>
							<option>8</option>
							<option>9</option>
							<option>10</option>
							<option>11</option>
							<option>12</option>
							<option>13</option>
							<option>14</option>
							<option>15</option>
							<option>16</option>
							<option>17</option>
							<option>18</option>
							<option>19</option>
							<option>20</option>
							<option>21</option>
							<option>22</option>
							<option>23</option>
						</select>&nbsp;:&nbsp;
						<input type="text" id="minutes" class="digital" style="width: 41px;" maxlength="2" value="0"/>&nbsp;:&nbsp;
						<input type="text" id="seconds" class="digital" style="width: 41px;" maxlength="2" value="0"/>
					</td>
				</tr>
				<tr>
					<td>违章记录&nbsp;&nbsp;</td>
					<td><input type="text" id="remark" style="width: 300px;"/></td>
				</tr>
				<tr>
					<td>罚款金额&nbsp;&nbsp;</td>
					<td><input type="text" id="vMoney" style="width: 300px;"/></td>
				</tr>
				<tr>
					<td>手续费&nbsp;&nbsp;</td>
					<td><input type="text" id="poundage" style="width: 300px;"/></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="button" id="confirm" value="确认" style="width:50px; margin-top: 15px;"/>
					</td>
				</tr>
			</table>
		</div>
		
		<div id="findVr" style="text-align:center; background-color: white; font:14px 微软雅黑; display: none; padding: 20px;">
			<input type="text" id="idCardNumber"/>
			<input type="button" id="find" value="确认" style="width:50px;"/>
		</div>
		<div id="findVrByCar" style="text-align:center; background-color: white; font:14px 微软雅黑; display: none; padding: 20px;">
			<input type="text" id="fCarId"/>
			<input type="button" id="findByCar" value="确认" style="width:50px;"/>
		</div>
	</body> 
</html>