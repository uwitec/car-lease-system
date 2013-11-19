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
		<title>新增车辆</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.tree.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.ajaxFileUpload.js"></script>
	
		<script type="text/javascript">
			$(function(){
				$("#mobilePhone").keyup(function(){					//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$("#km").keyup(function(){							//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$("#idCardNumber").keyup(function(){			    //中文输入控制
					$(this).val($(this).val().replace(/[\W]/g,'')); 
					$("#id").val("");
				});	
				$("#bodyNumber").keyup(function(){			    //中文输入控制
					$(this).val($(this).val().replace(/[\W]/g,'')); 
				});	
				$("#engineNumber").keyup(function(){			    //中文输入控制
					$(this).val($(this).val().replace(/[\W]/g,'')); 
				});	
				$.dqgb.button.modifyButtonStyle();

				var carBrand = "";								//车类(BMW)		
				
				$("#upload").click(function(){
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
		                fileElementId:'image',                        	//文件选择框的id属性
		                dataType: 'json',                               //服务器返回的格式，可以是json
		                success: function (data, status){            	//相当于java中try语句块的用法
		                	if(data.image){
			                	$.dqgb.shadowLayer.alert({content:"文件上传成功！"});
			                	$("#img").get(0).src = "upload/car/" + data.output.fileName;
			                	$("#fileName").val(data.output.fileName);  
		                	}else{
		                		$.dqgb.shadowLayer.alert({content:"连接超时,请重新登录..."});
		                	}
		                },
		                error: function (data, status, e){            //相当于java中catch语句块的用法
			                $.dqgb.shadowLayer.alert({content:"上传失败,请检查文件大小以及文件类型..."}); 
		                }
		            });
				});
				$.ajax({
					url:'getCarSource.action',
					type:'post',
					async:true,
					data:{
						
					},success:function(data){
						var dt = data.data;
						for(var i = 0; i < dt.length; i++){
							$("<option value='" + dt[i].id + "'>" + dt[i].remark + "</option>").appendTo($("#carSource"));
						}
					}
				});
				/*************************
				*
				*@TODO	保存个人配置
				*
				*************************/
				$("#save").click(function(){
					if(!$.trim($("#km").val())){
						$.dqgb.globalMsg.showMsg("请填写里程信息");
						return;
					}
					if(!$.trim($("#fileName").val())){
						$.dqgb.shadowLayer.confirm({
							content:'尚未上传图片信息，是否继续保存?',
							afterClose:function(){
								saveCar();
							}
						});
					}else{
						saveCar();
					}
				});

				function saveCar(){
					$.dqgb.shadowLayer.showMask({mask:"正在保存车辆信息,请稍候..."});
					$.ajax({
						url:'addCar.action',
						type:'post',
						asnyc:true,
						data:{
							imagePath: $.trim($("#fileName").val()) ? ("upload/car/" + $("#fileName").val()) : "",
							carId : $("#carId").val(),
							km : $.trim($("#km").val()),
							ownerIdCarNumber:$("#idCardNumber").val(),
							brand : $("#brand").val(),
							brandId : $("#brand").attr("brandId"),
							carCategory : $.trim($("#carCategory").val()),
							ownerName : $("#ownerName").val(),
							engineNumber : $("#engineNumber").val(),
							bodyNumber : $("#bodyNumber").val(),
							mobilePhone : $("#mobilePhone").val(),
							carSource:$("#carSource").get(0).options[$("#carSource").get(0).selectedIndex].value
						},
						success:function(data){
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
							}else{
								$.dqgb.shadowLayer.alert({
									content:"添加车辆信息成功!"
								});
							}
						}
					});
				};
				
				//修改树样式
				$.dqgb.tree.beforeCreateNode = function(obj){
					if(obj.getNodeAttribute().leaf){
						obj.getNodeAttribute().iconCls = 'modify';
						obj.setNodeAttribute({
							click:function(){
							},
							dblclick:function(){
								if(carBrand == obj.getRoot()){
									$("#brand").val(obj.getNodeAttribute().text).attr("brandId",obj.getNodeAttribute().id);
									$.dqgb.shadowLayer.hide();
								}
							}
						});
					}
				};
				//车型选择
				$("#brand").click(function(){
					$.ajax({
						url:"getBrandInformation.action",	//获取车品牌数据信息
						type:"post",
						async:true,
						data:{
							pageIndex:0,
							pageSize:1000
						},
						success:function(obj){
							if(obj == null || !obj.data || !obj.data.totalSize){
								return;
							}
							var data = obj.data;
							if(data.totalSize){
								var treeData = new Array();
								for(var i = 0; i < data.totalSize; i++){
									treeData[treeData.length] = new Object();
									treeData[treeData.length - 1].text = data.data[i].remark;
									treeData[treeData.length - 1].id = data.data[i].id;
								}
								carBrand = $.dqgb.tree.getDqgbTree({
									text:'库存车型',
									container:$("#carBrands").html(""),
									staticTree : true,
									treeData:treeData,
									iconCls:'root'
								});
							}
						}
					});
					var wd = 420;
					if($.browser.mozilla){
						wd = 422;
					}
					$("#chooseType").shieldRightMenu().shieldSelectStart().dqgbBox({
						width:wd,height:260,
						title:"车型选择",
						iconCls:'modify',
						closeBtn:true
					});
				}).keyup(function(){
					$(this).val("");
				});
			});
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
		</script>
		
		<style>
			td{
				background-color: white;
			}
		</style>
	</head>
	<body style="text-align: center; font-size: 14px; font-family: '微软雅黑'">
		<form action="uploadFile.action" method="post" enctype="multipart/form-data">
			<input type="hidden" id="fileName" />	<!-- 文件的名字  -->
			<table width="544px" height="323px" border="0" cellspacing="1" cellpadding="0" bgcolor="#D4D4D4"
			 style="position: absolute;left:50%;top:50%;margin-left:-272px; margin-top:-175px; font-size:14px; font-family:微软雅黑; ">
			  <tr>
			    <td width="105px" height="33px" >车型：</td>
			    <td width="168px"><input type="text" title="点击选择车型" id="brand" width="150px"/></td>
			    <td width="250px" rowspan="6" colspan="2">
			    	<img id="img" width="260px" height="200px" src="upload/car/blank.jpg"/>
			    </td>
			  </tr>
			  <tr>
			    <td height="33px">车系：</td>
			    <td><input type="text" id="carCategory"/></td>
			  </tr>
			  <tr>
			    <td height="33px">车牌：</td>
			    <td><input type="text" id="carId" width="150px"/></td>
			  </tr>
			  <tr>
			    <td height="34px">发动机编号：</td>
			    <td><input type="text" id="engineNumber" width="150px"/></td>
			  </tr>
			  <tr>
			    <td height="34px">车架号：</td>
			    <td><input type="text" id="bodyNumber" width="150px"/></td>
			  </tr>
			  <tr>
			    <td height="33px">车主：</td>
			    <td><input type="text" id="ownerName" width="150px" /></td>
			  </tr>
			  <tr>
			    <td height="33px">车主手机：</td>
			    <td height="33px"><input type="text" maxLength="15" id="mobilePhone" width="150px"/></td>
			    <td rowspan="2" colspan="2">
			    	<input type="file" width="140px" id="image" name="image"/>
			    	<input type="button" style="width:50px; margin-top:5px;" id="upload" value="上传"/>
			    </td>
			  </tr>
			  <tr>
			    <td height="33px">车源：</td>
			    <td height="33px">
			    	<select id="carSource" style="width:156px;">
			    
			    	</select>
			    </td>
			    
			  </tr>
			  <tr>
			    <td height="33px">证件号：</td>
			    <td><input type="text" maxLength="20" id="idCardNumber" width="150px"/></td>
			    <td height="33px" style="width:90px;">当前里程：</td>
			    <td style="padding: 0px;"><input type="text" maxLength="20" id="km" style="width: 130px; margin: 0px;" /></td>
			  </tr>
			  <tr>
			    <td colspan="4" height="40px"><input type="button" style="width:50px;" value="保存" id="save" /></td>
			  </tr>
			</table>
		</form>
		
		<div style="width:400px; height:240px; padding:10px; display: none;background-color: white;" id="chooseType">
			<div style="padding-top:1px; width:380px;height:235px;  overflow:auto; " id="carBrands"> 
			</div>
		</div>
	</body> 
</html>