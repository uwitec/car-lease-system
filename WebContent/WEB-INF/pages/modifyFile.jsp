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
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/jquery-ui-1.8.12.custom.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-ui-1.8.12.custom.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.ajaxFileUpload.js"></script>
		
		
		<style type="text/css">
			td{
				padding-left: 5px;
				font: 14px 微软雅黑;
				height: 30px;
			}
		</style>
		
	</head>
	<body style="text-align: center; font: 14px 微软雅黑;">
		<table style='text-align: left; width:100%;' border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>车牌号：</td><td><label id="carId" ></label></td>
			</tr>
			<tr>
				<td>档案类型：</td><td><input type="hidden" id="fmId" value="<s:property value='#session.fileManagementID' />"/>
				<select  style="width:298px" id="fileType">
				</select>
			</tr>
			<tr>
				<td>档案编号：</td><td><input type="text" style="width:292px" id="fileId"/></td>
			</tr>
			<tr>
				<td>办理日期：</td><td><input type="text" style="width:292px"  id="dateCheckIn" /></td>
			</tr>
			<tr>
				<td>到期时间：</td><td><input type="text" style="width:292px"  id="usefulLife" /></td>
			</tr>
			<tr>
				<td >证件照片：</td>
				<td align="left" height=25>
					<form action="uploadFile.action" method="post" enctype="multipart/form-data" style="margin: auto;">
						<input type="file" id="image" style="width:229px" name="image"/><input type="button" id="upLeaveTable" value='上传' style='width:50px; margin-left: 20px;'/>
					</form>
				</td>
			</tr>
		</table>
		<input type="button" id="modifyFile" style="width:50px; margin-top:20px;" value="保存" />
		
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
				$("#dateCheckIn").datepicker({
					dateFormat: 'yy-mm-dd',autoSize:true,changeYear:true,
					monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/']
				});
				$("#usefulLife").datepicker({
					dateFormat: 'yy-mm-dd',autoSize:true,changeYear:true,
					monthNames : ['01/','02/','03/','04/','05/','06/','07/','08/','09/','10/','11/','12/']
				});
				$.dqgb.button.modifyButtonStyle();
				
				$.ajax({
					url:'getFileInformationByID.action',
					type:'post',
					async:true,
					data:{
						id:$("#fmId").val()
					},
					success:function(data){
						var dt = data.data;
						$("#carId").html(dt.carId);
						$("#fileId").val(dt.fileIdentifyNumber);
						$("#image").attr("path",dt.filePath ? dt.filePath : "");
						$("#dateCheckIn").val(dt.dateCheckIn.split("T")[0]);
						$("#usefulLife").val(dt.usefulLife.split("T")[0]);
						var remarkId = dt.fileType;
						$.ajax({
							url:'getFileTypes.action',
							type:'post',
							async:true,
							data:{},
							success:function(data){
								var dt = data.data;
								var index = 0;
								for(var i = 0; i < dt.length; i++){
									$("<option value='" + dt[i].id + "'>" + dt[i].remark + "</options>").appendTo($("#fileType"));
									if(dt[i].id == remarkId){
										index = i;
									}
								}
								$("#fileType").get(0).selectedIndex = index;
							}
						});
					}
				});
				$("#modifyFile").click(function(){
					if($.trim($("#dateCheckIn").val()) == ""){
						$.dqgb.globalMsg.showMsg("办理日期不能为空!");
						return;
					}
					if($.trim($("#fileId").val()) == ""){
						$.dqgb.globalMsg.showMsg("请输入档案编号!");
						return;
					}
					if($.trim($("#usefulLife").val()) == ""){
						$.dqgb.globalMsg.showMsg("到期时间不能为空!");
						return;
					}
					if(!$("#image").attr("path") && "41" == $("#fileType").get(0).options[$("#fileType").get(0).selectedIndex].value){
						$.dqgb.globalMsg.showMsg("请上传行驶证照片！");
						return;
					}
					$.dqgb.shadowLayer.showMask({mask:'正在修改文件信息,请稍后....'});
					$.ajax({
						url:'modifyFile.action',
						async:true,
						type:'post',
						data:{
							'file.id':$("#fmId").val(),
							'file.carId':$.trim($("#carId").html()),
							'file.fileType':$("#fileType").get(0).options[$("#fileType").get(0).selectedIndex].value,
							'file.fileIdentifyNumber':$.trim($("#fileId").val()),
							'file.dateCheckIn':$.trim($("#dateCheckIn").val()),
							'file.usefulLife':$.trim($("#usefulLife").val()),
							'file.filePath':$("#image").attr("path") ? $("#image").attr("path") : ""
						},
						success:function(data){
							if(data.RESULT != 'FAILED'){
								$.dqgb.shadowLayer.hideMask();
								$.dqgb.globalMsg.showMsg("文件信息修改成功!");
							}else{
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}
						}
					});
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
	</body> 
</html>