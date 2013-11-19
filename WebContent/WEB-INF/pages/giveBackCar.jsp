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
		<title>收车</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.ajaxFileUpload.js"></script>
		
		<script type="text/javascript">
			/**
			*@TODO	检查上传的文件的后缀名
			*/
			function checkSuffix(filePath){
				var suffix = filePath.split(".");
				var suf = suffix[suffix.length - 1];
				if(suf!= "jpg" && suf!= "bmp" && suf!= "png" && suf != "gif" && suf != "jpeg" && 
						suf!= "JPG" && suf!= "BMP" && suf!= "PNG" && suf != "GIF" && suf != "JPEG" ){
					return false;
				}
				return true;
			}
			$(function(){
				$("#km").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$.dqgb.button.modifyButtonStyle();
				//获取费用类别
				$.ajax({
					url:'getCMFeeType.action',
					async:true,
					type:'post',
					data:{},
					success:function(data){
						var dt = data.data;
						for(var i = 0; i < dt.length; i++){
							$("<option value='" + dt[i].id + "'>" + dt[i].remark + "</option>").appendTo($(".feeType"));
						}
					}
				});
				
				$(".money").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				
				//收车
				$("#confirmGivBack").click(function(){
					if(!$.trim($("#km").val())){
						$.dqgb.globalMsg.showMsg("请填写车辆行驶里程!");
						return;
					}
					if(!$("#image").attr("path")){
						$.dqgb.globalMsg.showMsg("请上传收车单！");
						return;
					}
					
					var fees = $("#fee .fee");
					var input = "[";
					for(var i = 0; i < fees.length; i++){
						var select = $(fees[i]).children("td").children(".feeType");
						input += "{financeType:'" + select.get(0).options[select.get(0).selectedIndex].value + "',";
						var money = $(fees[i]).children("td").children(".money");
						input += "money:'" + ($.trim(money.val()) ? $.trim(money.val()) : 0) + "',";
						var moneyRemark = $(fees[i]).children("td").children(".moneyRemark");
						input += "remark:'" + ($.trim(moneyRemark.val()) ? $.trim(moneyRemark.val()) : "") + "'}";
						if(i != fees.length - 1){
							input += ",";
						}
						//其它费用
						if(select.get(0).options[select.get(0).selectedIndex].value == 29){
							if($.trim(money.val()) && parseInt($.trim(money.val())) != 0 && !$.trim(moneyRemark.val())){
								$.dqgb.shadowLayer.alert({content:'"请填写其它费用的收费原因!"'});
								return;
							}
						}
					}
					input += "]";
					$.dqgb.shadowLayer.confirm({
						content:'确认所有财务信息已经备注完毕?',
						afterClose:function(){
							$.dqgb.shadowLayer.showMask({mask:'正在执行收车操作,请稍后.....'});
							$.ajax({
								url:'giveBackCar.action',
								async:true,
								type:'post',
								data:{
									carId:$("#carId").val(),
									input : input,
									km : $.trim($("#km").val()),
									accident : $("#accident").attr("checked") ? 1 : 0,
									backTable : $("#image").attr("path")
								},
								success:function(data){
									if(data.RESULT != 'FAILED'){
										$.dqgb.shadowLayer.hideMask();
										$.dqgb.globalMsg.showMsg("收车操作成功!");
									}else{
										$.dqgb.shadowLayer.alert({content:data.REASON});
									}
								}
							});
						}
					});
				});
				//添加列
				$("#addFee").click(function(){
					var row = $.dqgb.addRow($("#fee").get(0));
					$(row).addClass("fee");
					
					var col = $.dqgb.addColumn(row);
					var select = $("<select></select>").appendTo($(col)).addClass("feeType");
					var options = $("#feeType").get(0).options;
					for(var i = 0; i < options.length; i++){
						$("<option value='" + options[i].value + "'>" + options[i].text + "</option>").appendTo(select);
					}
					col = $.dqgb.addColumn(row);
					$("<input type='text' class='money' value='0'/>").appendTo($(col)).keyup(function(){					//数字控制
						$(this).val($(this).val().replace(/\D/g,''));
					});
					
					col = $.dqgb.addColumn(row);
					$("<input type='text' class='moneyRemark' style='width:265px;'/>").appendTo($(col));
				});
				
				$("#upLeaveTable").click(function(){
					$.dqgb.shadowLayer.showMask({mask:"正在上传文件..."});

					if($.trim($("#image").val()) == ""){
						$.dqgb.shadowLayer.alert({content:"请指定要上传的文件"});
						return;
					}else{
						if(!checkSuffix($.trim($("#image").val()))){
							$.dqgb.shadowLayer.alert({content:"上传文件只能是图片类型!"});
							return;
						}
					}
					$.ajaxFileUpload({
		                url:'uploadFile.action',            			//需要链接到服务器地址
		                secureuri:false,
		                fileElementId:'image',                     		//文件选择框的id属性
		                dataType: 'json',                               //服务器返回的格式，可以是json
		                success: function (data, status){            	//相当于java中try语句块的用法
		                	if(data.image){
		                		$.dqgb.shadowLayer.hideMask();
			                	$.dqgb.globalMsg.showMsg("文件上传成功！");
			                	$("#image").attr("path","upload/car/" + data.output.fileName);
		                	}else{
		                		$.dqgb.shadowLayer.alert({content:"连接超时,请重新登录..."});
		                	}
		                },
		                error: function (data, status, e){            //相当于java中catch语句块的用法
			                $.dqgb.shadowLayer.alert({content:"上传失败,请检查文件大小以及文件类型..."}); 
		                }
		            });
				});
			});
		</script>
		
		<style type="text/css">
			#fee td{
				background-color: white;
				text-align:left;
				padding-left: 5px;
				height: 30px; line-height:30px;
			}
		</style>
	</head>
	<body style="text-align: center; font-size: 14px; font-family: '微软雅黑'">
		<input type="hidden" id="carId" value="<s:property value='#session.carId' />" />
		<h4 align="center" style="color:fuchsia;">如有额外费用产生请填写</h4>
		<table width="595px" border="0" cellpadding="0" cellspacing="0" style="text-align: left; margin: 0px 0px 10px 0px;">
			<tr>
				<td >车辆当前行驶里程：</td>
				<td align="left"  height=25><input type="text" style="width: 200px;" id='km'/></td>
				<td colspan="2" align="right">
					<input type="checkbox" style="vertical-align: middle;" id="accident"/>事故收车
				</td>
			</tr>
			<tr>
				<td >收车单：</td>
				<td align="left" height=25 colspan="3">
					<form action="uploadFile.action" method="post" enctype="multipart/form-data" style="margin: auto;">
						<input type="file" id="image" name="image"/><input type="button" id="upLeaveTable" value='上传' style='width:50px; margin-left: 20px;'/>
					</form>
				</td>
			</tr>
		</table>
		<table id="fee" width="595px" border="0" cellpadding="0" cellspacing="1" bgcolor="#999999" style="text-align: left">
			<tr>
				<td width="160" style="background-color: #BFD4F0;font-weight: bold;">费用类别</td>
				<td width="160" style="background-color: #BFD4F0;font-weight: bold;">金额(圆)</td>
				<td width="275" style="background-color: #BFD4F0;font-weight: bold;">备注</td>
			</tr>
			<tr class="fee">
				<td>
					<select class="feeType" id="feeType">
					</select>
				</td>
				<td><input type="text" class="money" value='0'/></td>
				<td><input type="text" class="moneyRemark" style="width:265px;" /></td>
			</tr>
		</table>
		<input type="button" value="追加费用" style="width:60px;margin-top:10px;" id="addFee"/>
		<input type="button" value="确认" style="width:60px;margin-top:10px;" id="confirmGivBack"/>
	</body> 
</html>