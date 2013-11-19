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
					title:"事故管理",
					iconCls:'modify',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
							      {text:'修改记录',iconCls:'modify',
							    	click:function(){
							    		var row = grid.getSelectedRow();
							    		if(row.length == 0){
							    			$.dqgb.globalMsg.showMsg("请选择您要修改的事故记录信息!");
							    			return;
							    		}
							    		if(row[0].status == 1){
							    			$.dqgb.globalMsg.showMsg("单据已封存，无法被修改!");
							    			return;
							    		}
							    		$("#dalayFee").val(row[0].dalayFee);
							    		$("#frayFee").val(row[0].frayFee);
							    		$("#repairFee").val(row[0].repairFee);
							    		$("#insuranceFee").val(row[0].insuranceFee);
							    		$("#otherFee").val(row[0].otherFee);
							    		$("#remark").val(row[0].remark);
							    		
							    		$("#modify").dqgbBox({
							    			width:450,
							    			height:300,iconCls:'modify',
							    			title:'修改事故记录',
							    			closeBtn : true
							    		});
							      	}
							      },'-',{text : '封存记录',iconCls:'locked_s',click:function(){
							    	  var row = grid.getSelectedRow();
							    		if(row.length == 0){
							    			$.dqgb.globalMsg.showMsg("请选择您要封存的单据!");
							    			return;
							    		}
							    		if(row[0].status == 1){
							    			$.dqgb.globalMsg.showMsg("单据已封存!");
							    			return;
							    		}
							    		$.dqgb.shadowLayer.confirm({
							    			content:'执行该操作以后数据将被锁定，确定要继续执行？',
							    			afterClose : function(){
							    				$.ajax({
							    					url : 'lock.action',
							    					async:true,type:'post',data:{
							    						id : grid.getSelectedRow()[0].id
							    					},
							    					success:function(data){
							    						if(data.RESULT == "FAILED"){
															$.dqgb.shadowLayer.alert({content:data.REASON});
														}else{
															$.dqgb.shadowLayer.hide();
															grid.refresh();
															$.dqgb.globalMsg.showMsg("操作成功！");
														}
							    					}
							    				});
							    			}
							    		});
							      }}
							]
						}).body
					},
					bbar:{
						url:"getAccident.action",
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
									{title:'订单号',width:150,alias:'serialNumber'},
									{title:'事故日期',width:120,alias:'accidentDate',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value && value.year){
												obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
														"-" + parseInt(value.date));
											}
											return false;
										}
									},
									{title:'责任人',width:100,alias:'customerName'},
									{title:'停运费',width:100,alias:'dalayFee',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},
									{title:'车损费',width:100,alias:'frayFee',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},
									{title:'维修费',width:100,alias:'repairFee',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},
									{title:'保险索赔费',width:120,alias:'insuranceFee',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},
									{title:'其它费用',width:100,alias:'otherFee',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html("￥" + value);
											return false;
										}
									},									
									{title:'事故说明',width:200,alias:'remark'},									
									{title:'单据状态',width:100,alias:'status',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											var icon = $("<div title='未封存' class='unlocked_s'></div>")
											.appendTo(obj).css({
	                                        	"margin-left":"0px",
	                                        	"margin-top":"4px",
	                                        	"cursor":"pointer",
	                                        	"background-repeat":"no-repeat",
	                                        	"width":"16px",
	                                        	"height":"16px"
											});
											if(value == 1){
												icon.attr("title","已封存").addClass("locked_s");
											}
											return false;			//客户化 
										}
									},
									{title:'业务员',width:120,alias:'transactor'},
									{title:'封存人',width:120,alias:'finisher'}
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
	</body> 
</html>