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
		<title>系统预警设置</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
	</head>
	<body style="text-align: center; font: 14px 微软雅黑;">
		<div>
			<div style="position:relative; left:0px; top:0px; height:200px; width:346px;" id="panel"> 
				<ul class="tab_panel_head" style="width:346px; height:27px;">
					<li style="font-weight: bold;">文件预警</li>
					<li style="font-weight: bold;">订单预警</li>
				</ul>
				<div class="bodys" style='width:344px; height:171px; overflow:hidden; border-left:1px solid #AACBEE; border-right:1px solid #AACBEE;'>
					<div class="item0" style="padding: 10px; text-align: left; overflow: auto;">
						<table id="fileType" width="100%" cellpadding="0" cellspacing="0" border="0">
						</table>
					</div>
					<div class="item1" style="padding: 10px; text-align: left;">
						<input type="checkbox" id="orderAp" />订单预警(被冻结的订单)<br>
						<p>预警持续时间&nbsp;<input type="text" value="0" id="orderAlarmDuration" />&nbsp;天</p>
					</div>
				</div>
				<div class="tab_panel_bottom"></div>
			</div>
			<input type="button" style="width:60px; margin-top: 6px;" value="确定" id="save"/>
		</div>
		<script type="text/javascript">
			/**********************************
			 * 
			 * @TODO	给table元素添加row
			 *
			 *********************************/
			function addRow(table){
				return $.browser.mozilla ? table.insertRow(-1) : table.insertRow();
			}
			/**********************************
			 * 
			 * @TODO	给row元素添加column
			 *
			 *********************************/
			 function addColumn(row){
				return $.browser.mozilla ? row.insertCell(-1) : row.insertCell();
			}
			
			/**
			*@TODO	initialize 函数
			**/
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				$(".tab_panel_head").children("li").each(function(index){
					$(this).mouseover(function(){
						$(".tab_panel_head li").removeClass("li_over");
						$("#panel .bodys").children("div").hide();
						$(this).addClass("li_over");
						$(".item" + index).show();
					});
				});
				$(".tab_panel_head").children("li:first").trigger("mouseover");
				
				//保存配置
				$("#save").click(function(){
					//文件预警检测
					var fRadio = $(".fileRadio[checked=true]");
					var fap = "";
					if(fRadio.length){
						fap = "{type:'1',apl:[";
						for(var i = 0; i < fRadio.length; i++){
							var duration = $("#fileType input[id='" + $(fRadio[i]).attr("id") + "'][type='text']");
							if($.trim(duration.val()) == "" || parseInt($.trim(duration.val())) <= 0){
								$.dqgb.globalMsg.showMsg(duration.attr("remark") + "的预警持续时间不能小于0!");
								return;
							}
							fap += "{paramValue:'" + $(fRadio[i]).attr("id") + "',duration:'" + $.trim(duration.val()) + "'}";
							if(i != fRadio.length - 1){
								fap += ',';
							}
						}
						fap += "]}";
					}
					//订单预警检测
					var oap = "";
					if($("#orderAp").get(0).checked == true){
						var duration = $("#orderAlarmDuration");
						if($.trim(duration.val()) == "" || parseInt($.trim(duration.val())) <= 0){
							$.dqgb.globalMsg.showMsg("订单预警的持续时间不能小于0!");
							return;
						}
						oap = "{type:'2',apl:[{paramValue:'34',duration:'" + $.trim(duration.val()) + "'}]}";				//订单冻结的状态为34
					}
					var cfg = "";
					if(!oap && !fap){
						
					}else{
						if(fap){
							cfg += "{configuration:[" + fap;
							if(oap){
								cfg += "," + oap ;
							}
							cfg += "]}";
						}else{
							cfg += "{configuration:[" + oap + "]}";
						}
					}
					
					$.ajax({
						url:'saveAlarmSetting.action',
						type:'post',
						async:true,
						data:{
							'alarmSetting': cfg
						},
						success:function(data){
							if(data.RESULT != 'FAILED'){
								$.dqgb.globalMsg.showMsg("预警配置设定成功!");
							}else{
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}
						}
					});
				});
				$.ajax({
					url:'getFileTypes.action',
					type:'post',
					async:true,
					data:{},
					success:function(data){
						var tb = $("#fileType").get(0);
						var dt = data.data;
						for(var i = 0; i < dt.length; i++){
							var row = addRow(tb);
							var col = addColumn(row);
							$("<input type='checkbox' class='fileRadio'/>").attr("id",dt[i].id).appendTo($(col));
							$("<a>" + dt[i].remark + "</a>").appendTo($(col));
							col = addColumn(row);
							$("<a>预警持续时间&nbsp;</a>").appendTo($(col));
							$("<input type='text' style='width:100px;' value='0'/>").attr("id",dt[i].id).attr("remark",dt[i].remark).appendTo($(col));
							$("<a>&nbsp;天</a>").appendTo($(col));
						}
						
						$(".bodys input[type='text']").keyup(function(){
							this.value = this.value.replace(/\D/g,''); 
						});
						
						//获取个人预警配置信息
						$.ajax({
							url:'getPersonalAPC.action',
							type:'post',
							async:true,
							data:{
							},
							success:function(data){
								var dt = data.data.data;
								for(var i = 0; i < dt.length; i++){
									if(dt[i].alarmType == '1'){					//文件预警
										for(var j = 0; j < dt[i].parameter.length; j++){
											var p = dt[i].parameter[j];
											$("#fileType input[type='checkbox'][id='" + p.paramValue + "']").get(0).checked = true;
											$("#fileType input[type='text'][id='" + p.paramValue + "']").val(p.duration);
										}
									}else if(dt[i].alarmType == '2'){
										if(dt[i].parameter.length){
											var p = dt[i].parameter[0];
											$("#orderAp").get(0).checked = true;
											$("#orderAlarmDuration").val(p.duration);
										}
									}
								}
							}
						});
					}
				});
				
			});
		</script>
	</body>
</html>