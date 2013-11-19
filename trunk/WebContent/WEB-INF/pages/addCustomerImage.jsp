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
		<title>客户证件</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.ajaxFileUpload.js"></script>
		
		<SCRIPT type="text/javascript">
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
				$.dqgb.button.modifyButtonStyle();
				
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
				$("#confirm").click(function(){
					if(!$("#image").attr("path")){
						$.dqgb.globalMsg.showMsg("请上传身份证！");
						return;
					}
					$.ajax({
						url:'addIdCarImage.action',
						type:'post',
						async:true,
						data:{
							customerId : '${customerId}',
							image : $("#image").attr("path")
						},
						success:function(data){
							if(data.RESULT != "FAILED"){
								$.dqgb.globalMsg.showMsg("保存成功!");
							}else{
								$.dqgb.shadowLayer.alert({content:data.REASON});
							}
						}
					});
				});
			});
		</SCRIPT>
	</head>
	<body>
		<div style="width:420px; height:140px; overflow:hidden; background-color: white;">
			<form action="uploadFile.action" method="post" enctype="multipart/form-data">
				<table style="font-size:14px; font-family:微软雅黑; margin:10px auto;">
					<tr>
						<td align="left">身份证</td>
						<td align="left"><input type="file" id="image" name="image"/><input type="button" id="upLeaveTable" value='上传' style='margin-left : 20px; width:50px;'/></td>
					</tr>
					<tr>
						<td colspan="2" align="center"><input type="button" value="确认" style="width:60px;margin-top:10px;" id="confirm"/></td>
					</tr>
				</table>
			</form>
		</div>
	</body> 
</html>