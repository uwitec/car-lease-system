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
		<title>内部用车管理</title>
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
					render:"dictionary",
					width:'100%',
					height:'100%',
					title:"内部用车管理",
					iconCls:'iconCls',
					tbar:{
						element : $.dqgb.navigation.generateNavigation({
							render:"nav",
							item:[
							      {text : '发车',iconCls : 'modify', click : function(){
							    	  $("#setout").dqgbBox({
							    		 width:360,
							    		 height:180,
							    		 iconCls:'modify',
							    		 closeBtn : {
							    			 afterClose : function(){
							    				 grid.refresh();
							    			 }
							    		 },
							    		 title : '发车(内部用车)'
							    	  });
							      }},'-',{text : '收车', iconCls : 'find', click : function(){
							    	  if(grid.getSelectedRow().length == 0){
							    		  $.dqgb.globalMsg.showMsg("请选择您要收车的单据");
							    		  return;
							    	  }
							    	  $("#giveback").dqgbBox({
								    		 width:360,
								    		 height:120,
								    		 iconCls:'modify',
								    		 closeBtn : {
								    			 afterClose : function(){
								    				 grid.refresh();
								    			 }
								    		 },
								    		 title : '收车(内部用车)'
								    	  });
							      }},'-',{text:'查询',iconCls:'modify',click:function(){
							    	  $("#find").dqgbBox({
								    		 width:360,
								    		 height:120,
								    		 iconCls:'modify',
								    		 closeBtn : true,
								    		 title : '用车记录查询'
								    	  });
							      }},'-',{text:'违章验证',iconCls:'modify',click:function(){
							    	  $("#findVr").dqgbBox({
								    		 width:450,
								    		 height:150,
								    		 iconCls:'modify',
								    		 closeBtn : true,
								    		 title : '查找违章记录'
								    	  });
							      }}
								]
						}).body
					},
					bbar:{
						url:"getRunRecord.action",
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
									{title : '车牌号',width : 150,alias : 'carId'},
									{title : '用车人',width : 100,alias : 'username'},
									{title : '用车说明',width : 250,alias : 'remark'},
									{title : '发车人',width : 100,alias : 'transactorName'},
									{title : '收车人',width : 100,alias : 'finisherName'},
									{title : '发车里程',width : 100,alias : 'kmBegin',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											obj.html(value + "km");
											return false;
										}
									},
									{title : '收车里程',width : 100,alias : 'kmEnd',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(value != 0){
												obj.html(value + "km");
											}
											return false;
										}
									},
									{title : '发车日期',width : 150,alias : 'dateBegin',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(!value || !value.year){
												return false;
											}
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds);
											return false;
										}
									},
									{title : '收车日期',width : 150,alias : 'dateEnd',
										beforeDataLoading:function(gridObj,obj,value,row,column){
											if(!value || !value.year){
												return false;
											}
											obj.html(parseInt(value.year + 1900) + "-" + parseInt(value.month + 1) + 
													"-" + parseInt(value.date) + " " + value.hours + ":" + value.minutes + ":" + value.seconds);
											return false;
										}
									}
									
								]
				});

				$(".digital").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				
				$("#_violateDate").datepicker({
					showSecond: true, //显示秒
					timeFormat: 'hh:mm:ss',//格式化时间
					stepSecond: 10,
					dateFormat: 'yy-mm-dd',
					monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/'],
					onClose:function(text,inst){
					}
				});
				
				
				$("#confirm").click(function(){
					if(!$.trim($("#username").val())){
						$.dqgb.globalMsg.showMsg("请填写用车人信息");
						return;
					}
					if(!$.trim($("#remark").val())){
						$.dqgb.globalMsg.showMsg("请填写用车事由");
						return;
					}
					if(!$.trim($("#carId").val())){
						$.dqgb.globalMsg.showMsg("请填写车牌号");
						return;
					}
					
					$.ajax({
						url:'setOut.action',
						async:true,type:'post',
						data:{
							'nbrr.username':$.trim($("#username").val()),
							'nbrr.carId':$.trim($("#carId").val()),
							'nbrr.remark':$.trim($("#remark").val())
						},
						success : function(data){
							if(data.RESULT != 'FAILED'){
								$.dqgb.shadowLayer.hide();
								$.dqgb.globalMsg.showMsg("数据提交成功");
								grid.refresh();
							}else{
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}
						}
					});
				});
				$("#km").keyup(function(){						//数字控制
					this.value = this.value.replace(/\D/g,'');
				});
				$("#confirmBack").click(function(){
					if(!$.trim($("#km").val())){
						$.dqgb.globalMsg.showMsg("请填写当前里程信息");
						return;
					}
					$.ajax({
						url:'backCompany.action',
						async:true,type:'post',
						data:{
							rrId : grid.getSelectedRow()[0].id,
							km : $.trim($("#km").val())
						},
						success : function(data){
							if(data.RESULT != 'FAILED'){
								$.dqgb.shadowLayer.hide();
								$.dqgb.globalMsg.showMsg("收车成功");
								grid.refresh();
							}else{
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}
						}
					});
				});
				$("#confirmFind").click(function(){
					if(!$.trim($("#_carId").val())){
						$.dqgb.globalMsg.showMsg("请填写车牌号");
						return;
					}
					$.dqgb.shadowLayer.hide();
					grid.pagination.options.extraParam = {
						carId:$.trim($("#_carId").val())	
					};
					grid.pagination.options.url = "getRunRecorByCar.action";			//条件查询

					if(grid.pagination.getPageIndex() == 0){
						grid.refresh();
					}else{
						grid.pagination.first.trigger("click");
					}
				});
				$("#confirmFindVr").click(function(){
					if(!$.trim($("#violateCarId").val())){
						$.dqgb.globalMsg.showMsg("请填写车牌号");
						return;
					}
					if(!$.trim($("#_violateDate").val())){
						$.dqgb.globalMsg.showMsg("请填写违章日期");
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
					
					$.dqgb.shadowLayer.hide();
					grid.pagination.options.extraParam = {
						carId:$.trim($("#violateCarId").val()),
						'violateDate' : $.trim($("#_violateDate").val()) + " " + $.trim($("#hour").val()) + ":" + $.trim($("#minutes").val()) + ":" + $.trim($("#seconds").val())
					};
					grid.pagination.options.url = "getViolateRunRecord.action";			//条件查询

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
		<div id="dictionary">
		</div>
		<div id="nav"></div>
		
		<div style="font: 14px 微软雅黑; display: none; background-color: white;" id='setout'>
			<table style="margin: 20px auto;">
				<tr>
					<td>用车人:</td>
					<td><input type="text" id="username" maxlength="20"/></td>
				</tr>
				<tr>
					<td>车牌号:</td>
					<td><input type="text" id="carId" maxlength="20"/></td>
				</tr>
				<tr>
					<td>用车事由:</td>
					<td><input type="text" id="remark" maxlength="200"/></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" style="width: 50px;" id='confirm' value='确定'/></td>
				</tr>
			</table>
		</div>
		<div style="font: 14px 微软雅黑; display: none; background-color: white;" id='giveback'>
			<table style="margin: 20px auto;">
				<tr>
					<td>收车里程:</td>
					<td><input type="text" id="km" maxlength="20"/></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" style="width: 50px;" id='confirmBack' value='确定'/></td>
				</tr>
			</table>
		</div>
		<div style="font: 14px 微软雅黑; display: none; background-color: white;" id='find'>
			<table style="margin: 20px auto;">
				<tr>
					<td>车牌号:</td>
					<td><input type="text" id="_carId" maxlength="20"/></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" style="width: 50px;" id='confirmFind' value='确定'/></td>
				</tr>
			</table>
		</div>
		
		<div style="font: 14px 微软雅黑; display: none; background-color: white;" id='findVr'>
			<table style="margin: 20px auto;">
				<tr>
					<td align="left">车牌号:</td>
					<td><input type="text" id="violateCarId" maxlength="20" style="width: 300px;"/></td>
				</tr>
				<tr>
					<td align="left">违章日期:</td>
					<td>
						<input type="text" id="_violateDate" style="width: 120px;"/>
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
					<td colspan="2" align="center"><input type="button" style="width: 50px;" id='confirmFindVr' value='确定'/></td>
				</tr>
			</table>
		</div>
		
	</body> 
</html>