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
		<title>客户消费管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/component.3.0.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.pagination.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.grid.3.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.query.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				var leaseGrid = $.xqb9528.grid.getGrid({
					title:'客户订单',
					iconCls : 'customer',
					bbar : {
						url:"getCustomerOrder.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						pageSize : 20,
						result : 'RESULT',					
						failed : 'FAILED',					
						reason : 'REASON',
						showDetails : true
					},
					width : 800,
					height : 350,
					model : true,
					rowNumber : true,
					dragable : true,
					closeBtn : true,
					autoLoad : true,								//表格显示以后自动加载数据
					checkBox : false,
					columnModel : [
		               	{title:'车牌号',width:120,alias:'carId'},
						{title:'承租人',width:100,alias:'customerName'},
						{title:'租金收入',width:120,alias:'rent',
							beforeLoadingData : function(gridObj,row,col,value){
								col.dom.html("￥" + value);
								return false;
							}
						},
						{title:'总收入',width:120,alias:'totalIn',
							beforeLoadingData : function(gridObj,row,col,value){
								col.dom.html("￥" + value);
								return false;
							}
						},
						{title:'起租日期',width:150,alias:'dateBegin',
							beforeLoadingData : function(gridObj,row,col,value){
								col.dom.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
										"-" + parseInt(value.date));
								return false;
							}
						},
						{title:'截止日期',width:150,alias:'dateEnd',
							beforeLoadingData : function(gridObj,row,col,value){
								col.dom.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
										"-" + parseInt(value.date));
								return false;
							}
						},
						{title:'续租截止日期',width:150,alias:'dateRelet',
							beforeLoadingData : function(gridObj,row,col,value){
								if(!value || !value.year){
									return false;
								}
								col.dom.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
										"-" + parseInt(value.date));
								return false;
							}
						},
						{title:'取车日期',width:180,alias:'fetchCarDate',
							beforeLoadingData : function(gridObj,row,col,value){
								if(!value || !value.year){
									return false;
								}
								col.dom.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
										"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds)
										.css("color","fuchsia");
								return false;
							}
						},
						{title:'还车日期',width:180,alias:'giveBackCarDate',
							beforeLoadingData : function(gridObj,row,col,value){
								if(!value || !value.year){
									return false;
								}
								col.dom.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
										"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds)
										.css("color","fuchsia");
								return false;
							}
						},
						
						{title:'订单编号',width:350,alias:'serialNumber'}
					],
					
					autoExpandColumn : 'carId',
					
					buttons : {
						items : [
							{text : '消费统计',click : function(e){
								$.ajax({
									url : 'getConsumeSummary.action',
									type:'post',async:true,
									data:{
										customerId : grid.getSelectedRow().getColumn("idCardNumber").getCurrentValue()
									},success : function(data){
										var _data = data.data.data[0];
										var div = $("<div></div>").html("总计收入:￥" + _data.finance + "，其中租金收入:￥" + _data.rent + 
												"，优惠:￥" + _data.discount + "，其它收入：￥" + (_data.finance - _data.rent + parseInt(_data.discount))).css({
											'font-size' : '14px','font-family':'微软雅黑','padding':'10px','text-indent':'2em'
										});
										var bx = div.xqbBox({
											title : '消费统计',
											iconCls : 'modify',
											width : 380,
											model : true,				//模态弹出框
											dragable : true,
											height : 200,
											closeBtn : {
												afterClose : function(){
													div.remove();
												}
											},
											buttons : {					
												items : [
													{text : '确认', width : 60, click : function(){
														bx.hide();
														bx.dom.remove();
													}}
												]
											}
										}).showCenter();
									}
								});
								
							}}
						],
						buttonAlign : 'center'
					}
				});
				
				
				var grid = $.xqb9528.grid.getGrid({
					title:'客户消费统计',
					iconCls : 'customer',
					bbar : {
						url:"getCustomers.action",
						root:'data.data',							//返回的数据对象的名字
						total:'data.totalSize',
						pageSize : 20,
						result : 'RESULT',					
						failed : 'FAILED',					
						reason : 'REASON',
						showDetails : true
					},
					width : 600,
					height : 350,
					rowNumber : true,
					autoLoad : true,							//表格显示以后自动加载数据
					checkBox : {
						singleModel : true
					},
					columnModel : [
		               	{title:'客户姓名',width:120,alias:'customerName',sortable:true}, 
						{title:'消费历史',width:100,alias:'History',
							beforeLoadingData : function(gridObj,row,col,text){
								var icon = $("<div title='查看消费历史' class='find'></div>")
								.appendTo(col.dom).click(function(e){
									leaseGrid.animateShow({x:e.clientX,y:e.clientY})
										.setExtraParam({'idCardNumber' :  row.getColumn("idCardNumber").getCurrentValue()})
										.setTitle('客户消费历史—' + row.getColumn("customerName").getCurrentValue());
									leaseGrid.refresh();
								}).css({
	                                     	"margin-left":"0px",
	                                     	"margin-top":"4px","cursor":"pointer",
	                                     	"background-repeat":"no-repeat",
	                                     	"width":"16px",
	                                     	"height":"16px"
								});
								return false;			//客户化
							}
						}	               ,
						{title:'性别',width:100,alias:'gender',sortable:true,
		               		beforeLoadingData : function(gridObj,row,col,text){
								var icon;
								if("男" == text)
									icon = $("<div title='男性' class='male'></div>").appendTo(col.dom);
								else
									icon = $("<div title='女性' class='female'></div>").appendTo(col.dom);
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
						{title:'身份证号码',width:180,alias:'idCardNumber',sortable:true},
						{title:'联系方式',width:150,alias:'mobilePhone'},
						{title:'客户单位',width:150,alias:'company'},
						{title:'单位电话',width:120,alias:'companyPhone'}
					],
					
					autoExpandColumn : 'idCardNumber',
					
					buttons : {
						items : [
							{text : '客户查找',click : function(e){
								var box = $("#findCustomer").xqbBox({
									title : '客户查找(输入客户证件号查找)',
									iconCls : 'modify',
									width : 400,
									model : true,				//模态弹出框
									height : 150,
									closeBtn : true,
									buttons : {					
										items : [
											{text : '确认', width : 60, click : function(){
												if(!$.trim($("#idCardNumber").val())){
													$.xqb9528.globalMsg.showMsg('请输入证件号码',true);
													return;
												}
												grid.setExtraParam({
													idCardNumber:$.trim($("#idCardNumber").val())	
												});
												grid.setURL('queryCustomer.action').refresh().first();
												box.hide();
											}},
											{text : '取消', width : 60, click : function(){
												box.hide();
											}}
										]
										
										
									}
								}).showCenter();
							}}
						],
						buttonAlign : 'center'
					}
				});
				
				grid.acceptWindowManage(false).autoExpand().show();					//设置grid为自适应的
				grid.doLayout();	//纠正ie7下无法自动撑满全屏幕的bug
				
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
			});
		</SCRIPT>
	</head>
	<body>
		<div id="user">
		</div>
		<div id="nav"></div>
		<div style='padding:15px 10px; font:14px 微软雅黑; text-align: center; display: none; ' id="findCustomer">
			证件号：<input type="text" id="idCardNumber" style='width:200px;'/>
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