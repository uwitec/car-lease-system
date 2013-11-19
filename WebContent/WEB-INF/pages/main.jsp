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
		<title>租车管理系统</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.tree.js"></script>
		
		<style type="text/css">
			#fileType td{
				height: 30px;
			}
		</style>
	</head>
	
	
	
	<body>
		<div id="north" class="head_title">
			<div style="position: absolute; right:5px; cursor:pointer; padding-right:5px; bottom: 5px; " 
				class="normal" id="modifyPassword">修改密码
			</div>
			<div style="position: absolute; right:80px; cursor:pointer; padding-right:5px; bottom: 5px; " 
				class="normal" id="logout">注销
			</div>
			<div style="position: absolute; right:130px; cursor:pointer; padding-right:5px; bottom: 5px; " 
				class="normal" id="online_user">在线人员
			</div>
			<div style="position: absolute; right:210px; cursor:pointer; padding-right:5px; bottom: 5px; " 
				class="normal" id="alarmSetting">预警设置
			</div>
			<div style="position: absolute; right:285px; cursor:pointer; padding-right:5px; 
							width:16px; height:16px; bottom: 5px;" title='预警信息' class="find" id="alarmMsg">
			</div>
			
			<div class="head_left">
				<label style="position: absolute; right:10px; bottom: 5px;" class="normal">
					您好：<s:property value="#session.CurrentUser.realName"/>，今天是
					<s:property value="#session.DATE.YEAR"/>年
					<s:property value="#session.DATE.MONTH"/>月
					<s:property value="#session.DATE.DATE"/>日 星期
					<s:property value="#session.DATE.DAY"/>
				</label>
			</div>
		</div>
		<div id="south" style="text-align: center; line-height: 30px;  font-size: 14px;">
		<!--  		<marquee BEHAVIOR="SCROLL" DIRECTION="right" scrolldelay="150" ONMOUSEOUT=this.start() 
			ONMOUSEOVER=this.stop() SCROLLAMOUNT="10" width=800>-->
				@copyright：南宁市广文信息科技有限公司&nbsp;&nbsp;&nbsp; 联系电话：5772225
	<!-- 		</marquee> -->
		</div>
		<div id="west" >
		</div>
		<div id="center"></div>
		
		<div style="display: none; text-align: center; height:200px; width:300px; padding:10px;
			background-color: white; overflow: hidden;" id="modify">
			<table>
				<tr>
					<td>旧密码：</td>
					<td><input type="password" style="width:200px" id="oldPassword" /></td>
				</tr>
				<tr>
					<td>新密码：</td>
					<td><input type="password" style="width:200px" id="newPassword1" /></td>
				</tr>
				<tr>
					<td>确认密码：</td>
					<td><input type="password" style="width:200px" id="newPassword2" /></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center"><input type="button" id="savePassword" style="width:50px;" value="保存"/></td>
				</tr>
			</table>
			<div id="msg"></div>
		</div>
		
		<div id="personalACP" style="display: none; background-color: white; padding: 10px; text-align: center; font: 14px 微软雅黑;">
			<div style="position:relative; left:0px; top:0px; height:200px; width:346px;" id="panel"> 
				<ul class="tab_panel_head" style="width:346px; height:27px;">
					<li style="font-weight: bold;">档案预警</li>
					<li style="font-weight: bold;">订单预警</li>
					<li style="font-weight: bold;">保证金预警</li>
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
					<div class="item2" style="padding: 10px; text-align: left;">
						<input type="checkbox" id="vsAp" />保证金预警<br>
						<p>到期预警时间&nbsp;<input type="text" value="0" id="vsAlarmDuration" />&nbsp;天</p>
						<p>冻结预警时间&nbsp;<input type="text" value="0" id="vsFreezoneDuration" />&nbsp;天</p>
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
			
			 var globalAlarmMsgBox = "";			//预警框
			 var layout = "";
			/**
			*@TODO	initialize 函数
			**/	
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				//==初始化页面布局
				layout = $.dqgb.layout.borderLayout({
					north:{
						render:"north",
						height:87
					},
					south:{
						render:"south",
						height:30
					},
					west:{
						render:"west",
						frame:'true',
						width:180
					},
					center:{
						url:'welcome.action',
						render:'center'
					}
				});

				//======修改密码========
				$("#modifyPassword").mouseover(function(){
					$(this).css({
						"font-style":"italic"
					});
				}).mouseout(function(){
					$(this).css({
						"font-style":"normal"
					});
				}).click(function(){
					$("#modify").dqgbBox({
						title:"修改密码",
						closeBtn:true,
						iconCls:"iconCls"
					});
				});
				//========注销系统===========
				$("#logout").mouseover(function(){
					$(this).css({
						"font-style":"italic"
					});
				}).mouseout(function(){
					$(this).css({
						"font-style":"normal"
					});
				}).click(function(){
					$.dqgb.shadowLayer.confirm({
						title:'温馨提示',
						content:'确认要退出系统?',
						afterClose: function(){
							window.location = "logout.action";
						}
					});
				});
				//========预警设置===========
				$("#alarmSetting").mouseover(function(){
					$(this).css({
						"font-style":"italic"
					});
				}).mouseout(function(){
					$(this).css({
						"font-style":"normal"
					});
				}).click(function(){
					$("#personalACP").dqgbBox({
						title:'预警设置',
						iconCls:'browse',
						width:366,height:250,
						closeBtn:true
					});
					$.ajax({
						url:'getFileTypes.action',
						type:'post',
						async:true,
						data:{},
						success:function(data){
							var tb = $("#fileType").html("").get(0);
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
										}else if(dt[i].alarmType == '2'){			//订单预警
											if(dt[i].parameter.length){
												var p = dt[i].parameter[0];
												$("#orderAp").get(0).checked = true;
												$("#orderAlarmDuration").val(p.duration);
											}
										}else if(dt[i].alarmType == '3'){			//保证金预警
											if(dt[i].parameter.length){
												$("#vsAp").get(0).checked = true;
												for(var k = 0; k < dt[i].parameter.length; k++){
													var p = dt[i].parameter[k];
													if(p.paramValue == "0"){
														$("#vsAlarmDuration").val(p.duration);
													}else if(p.paramValue == "1"){
														$("#vsFreezoneDuration").val(p.duration);
													}
												}
											}
										}
									}
								}
							});
						}
					});
				});
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
					var fRadio = $(".item0 input:checked");
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
					//保证金预警
					var vsap = "";
					if($("#vsAp").get(0).checked == true){
						if((!$.trim($("#vsAlarmDuration").val()) || parseInt($.trim($("#vsAlarmDuration").val())) <= 0) && 
								(!$.trim($("#vsFreezoneDuration").val()) || parseInt($.trim($("#vsFreezoneDuration").val())) <= 0 )){
							$.dqgb.globalMsg.showMsg("保证金预警的持续时间不能为空!");
							return;
						}
						var tc = [];
						if(($.trim($("#vsAlarmDuration").val()) && parseInt($.trim($("#vsAlarmDuration").val())) > 0)){					//到期预警
							tc.push("{paramName:'到期预警',paramValue:'0',duration:'" + $.trim($("#vsAlarmDuration").val()) + "'}");
						}
						if(($.trim($("#vsFreezoneDuration").val()) && parseInt($.trim($("#vsFreezoneDuration").val())) > 0 )){					//冻结预警
							tc.push("{paramName:'冻结预警',paramValue:'1',duration:'" + $.trim($("#vsFreezoneDuration").val()) + "'}");
						}
						vsap = "{type:'3',apl:[" + tc.toString() + "]}";
					}
					
					
					var cfg = "";
					var cfgs = [];
					if(oap || fap || vsap){
						if(fap){
							cfgs.push(fap);
						}
						if(oap){
							cfgs.push(oap);
						}
						if(vsap){
							cfgs.push(vsap);
						}
						cfg += "{configuration:[" + cfgs.toString() + "]}";
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
				//修改密码
				$("#savePassword").click(function(){
					$.ajax({
						url:'modifyPassword.action',
						type:'post',
						data:{
							oldPassword : $("#oldPassword").val(),
							newPassword1 : $("#newPassword1").val(),
							newPassword2 : $("#newPassword2").val()
						},
						async:true,
						success : function(data){
							if(data.RESULT == "FAILED"){
								$("#msg").html(data.REASON);
							}else{
								$("#msg").html(data.RESULT);
							}
							$("#oldPassword").val("");
							$("#newPassword1").val("");
							$("#newPassword2").val("");
						}
					});
				});

				//=====在线人员查看=======
				$("#online_user").mouseover(function(){
					$(this).css({
						"font-style":"italic"
					});
				}).mouseout(function(){
					$(this).css({
						"font-style":"normal"
					});
				}).click(function(){
					$.dqgb.shadowLayer.loading({
						title:'系统在线人员',
						width:600,
						height:400,
						closeBtn:true,
						url:'gotoOnlineUser.action',
						iconCls:'iconCls'
					});
				});
				
				//=======渲染菜单====

				$.dqgb.tree.beforeCreateNode = function(obj){
					if(obj.getNodeAttribute().leaf){
						obj.getNodeAttribute().iconCls = 'leaf';
						obj.setNodeAttribute({
							click:function(){
								layout.setURL(obj.getNodeAttribute().url);
							}
						});
					}else{
					}
				};
				
				var rt = $.dqgb.tree.getDqgbTree({
					text:'租车管理系统',
					container:layout.west,
					root:'node',								//json数据的根节点
					iconCls:'root',
					url:'getTreeData.action'
				});
				rt.name.click(function(){
					layout.setURL("welcome.action");
				});
				
				getPersonalAI();
				
				$("#alarmMsg").click(function(){
					if(globalAlarmMsgBox.dialog && globalAlarmMsgBox.dialog.css("display") == "none"){
						globalAlarmMsgBox.dialog.animate({
							"top":"10px",
							opacity:'show'
						});
						$(this).blur();
					}
				});
			});
			/********
			*
			* @TODO		获取个人预警消息
			*
			**/
			function getPersonalAI(){
				//获取个人预警配置信息
				$.ajax({
					url:'getPersonalAPC.action',
					type:'post',
					async:true,
					data:{
					},
					success:function(data){
						var dt = data.data.data;
						var param = "{configuration:[";
						for(var i = 0; i < dt.length; i++){
							var pa = "{type:" + dt[i].alarmType + ",apl:[";
							for(var j = 0; j < dt[i].parameter.length; j++){
								var p = dt[i].parameter[j];
								pa += "{paramValue:" + p.paramValue + ",duration:" + p.duration+ "}";
								if(j != dt[i].parameter.length - 1){
									pa += ",";
								}
							}
							pa += "]}";
							param += pa;
							if(i != dt.length - 1){
								param += ",";
							}
						}
						param += "]}";
						$.ajax({
							url:'getPersonalAlarmInfor.action',
							asnyc:true,type:'post',data:{
								alarmSetting:param
							},
							success:function(data){
								var dt = data.data;
								dt.ORDERCOUNT = dt.ORDERCOUNT ? dt.ORDERCOUNT : 0;
								dt.FILECOUNT = dt.FILECOUNT ? dt.FILECOUNT : 0;
								dt.REMIND_VS = dt.REMIND_VS ? dt.REMIND_VS : 0;
								dt.FREEZONE_VS = dt.FREEZONE_VS ? dt.FREEZONE_VS : 0;
								dt.DEADLINE_ORDER = dt.DEADLINE_ORDER ? dt.DEADLINE_ORDER : 0;
								globalAlarmMsgBox = $.dqgb.shadowLayer.customEncapsulate($("#personalAi"),{
									title:'您有&nbsp;<font color:fuchsia;>' + 
											(dt.ORDERCOUNT + dt.FILECOUNT + dt.FREEZONE_VS + dt.REMIND_VS + dt.DEADLINE_ORDER) + '</font>&nbsp;条预警消息',
									floatBox : true,										//浮动框,表明是浮动框体
									iconCls:'modify',
									closeBtn:{
										close : function(obj){
											obj.animate({
												"top":-obj.outerHeight(true),
												opacity:'hide'
											});
										}
									}
								});
								globalAlarmMsgBox.dialog.css({"position":"absolute",
									"left":"auto","right":"6px","top":-globalAlarmMsgBox.dialog.outerHeight(true)}).hide();
								
								globalAlarmMsgBox.element.css({"overflow":"auto"});
								
								if(dt.ORDERCOUNT || dt.FILECOUNT || dt.REMIND_VS || dt.FREEZONE_VS || dt.DEADLINE_ORDER){
									if(dt.ORDERCOUNT){
										var msgs = $("<p style='margin:2px; padding:0px; text-indent:2em; font: 14px 微软雅黑;'></p>").html("近期共有" + 
												dt.ORDERCOUNT + "条订单被冻结，点击<span>这里</span>查看详情").appendTo($("#personalAi"));
										msgs.children("span").css({"color":"blue","cursor":"pointer"}).click(function(){
											layout.setURL("gotoOrderAlarmPage.action");
										}).mouseover(function(){
											$(this).css("text-decoration","underline");
										}).mouseout(function(){
											$(this).css("text-decoration","none");
										});
									}
									if(dt.FILECOUNT){
										var msgs = $("<p style='margin:2px; padding:0px; text-indent:2em; font: 14px 微软雅黑;'></p>").html("有" + 
												dt.FILECOUNT + "条文件档案即将过期，请尽快处理。点击<span>这里</span>查看详情").appendTo($("#personalAi"));
										msgs.children("span").css({"color":"blue","cursor":"pointer"}).click(function(){
											layout.setURL("gotoFileAlarmPage.action");
										}).mouseover(function(){
											$(this).css("text-decoration","underline");
										}).mouseout(function(){
											$(this).css("text-decoration","none");
										});
									}
									if(dt.REMIND_VS || dt.FREEZONE_VS){
										var msgs = $("<p style='margin:2px; padding:0px; text-indent:2em; font: 14px 微软雅黑;'></p>").html("有" + 
												(dt.REMIND_VS + dt.FREEZONE_VS) + "条保正金预警信息。点击<span>这里</span>查看详情").appendTo($("#personalAi"));
										msgs.children("span").css({"color":"blue","cursor":"pointer"}).click(function(){
											layout.setURL("gotoVsAlarmPage.action");
										}).mouseover(function(){
											$(this).css("text-decoration","underline");
										}).mouseout(function(){
											$(this).css("text-decoration","none");
										});
									}
									if(dt.DEADLINE_ORDER){
										var msgs = $("<p style='margin:2px; padding:0px; text-indent:2em; font: 14px 微软雅黑;'></p>").html("有" + 
												(dt.DEADLINE_ORDER) + "条订单即将到期。点击<span>这里</span>查看详情").appendTo($("#personalAi"));
										msgs.children("span").css({"color":"blue","cursor":"pointer"}).click(function(){
											layout.setURL("gotoDeadlineOrderPage.action");
										}).mouseover(function(){
											$(this).css("text-decoration","underline");
										}).mouseout(function(){
											$(this).css("text-decoration","none");
										});
									}
									
									globalAlarmMsgBox.dialog.animate({
										"top":"10px",
										opacity:'show'
									});
								}
							}
						});
					}
				});
			}
		</script>
		<div id='personalAi' style="width:300px; height:100px;background-color: #FDFDFD; padding:10px; display: none;">
		</div>
	</body>
</html>