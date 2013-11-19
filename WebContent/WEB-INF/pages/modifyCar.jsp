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
		<title>车辆预览</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.ajaxFileUpload.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.tree.js"></script>
		
		<script type="text/javascript">
			$(function(){
				if($.trim($("#fileName").val()) == ""){
					$("#img").get(0).src = "upload/car/blank.jpg";
				}else{
					$("#img").get(0).src = $("#fileName").val();
				}
				$("#km").keyup(function(){							//数字控制
					$(this).val($(this).val().replace(/\D/g,''));
				});
				$("#mobilePhone").keyup(function(){					//数字控制
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
				var brands = $("#brandName").val();
				$("#brand").val(brands);

				
				
				
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
			                	$("#fileName").val("upload/car/" + data.output.fileName);  
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
						var id = $("#carSourceId").val();
						var index = 0;
						for(var i = 0; i < dt.length; i++){
							$("<option value='" + dt[i].id + "'>" + dt[i].remark + "</option>").appendTo($("#carSource"));
							if(dt[i].id == id){
								index = i;
							}
						}
						$("#carSource").get(0).selectedIndex = index;
					}
				});
				/*************************
				*
				*@TODO	保存个人配置
				*
				*************************/
				$("#save").click(function(){
					saveCar();
				});

				function saveCar(){
					$.dqgb.shadowLayer.showMask({mask:"正在保存车辆信息,请稍候..."});
					$.ajax({
						url:'modifyCarInformation.action',
						type:'post',
						asnyc:true,
						data:{
							id:$("#keyId").val(),
							imagePath: $.trim($("#fileName").val()) ? $("#fileName").val() : "",
							carId : $("#carId").val(),
							brand : $("#brand").val(),
							carCategory : $.trim($("#carCategory").val()),
							ownerName : $("#ownerName").val(),
							ownerIdCarNumber:$("#idCardNumber").val(),
							engineNumber : $("#engineNumber").val(),
							bodyNumber : $("#bodyNumber").val(),
							carSource:$("#carSource").get(0).options[$("#carSource").get(0).selectedIndex].value,
							mobilePhone : $("#mobilePhone").val()
						},
						success:function(data){
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
							}else{
								$.dqgb.shadowLayer.alert({
									content:"车辆信息修改成功!"
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
					$("#chooseType").shieldRightMenu().shieldSelectStart().dqgbBox({
						width:420,height:260,
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
	<body style="text-align: center;">
		
		<form action="uploadFile.action" method="post" enctype="multipart/form-data">
			
			<input type="hidden" id="fileName" value="<s:property value='#session.CAR.imagePath'/>" />	<!-- 文件的名字  -->
			<input type="hidden" id="keyId" value="<s:property value='#session.CAR.id'/>" />	<!-- 主键  -->
			
			<input type="hidden" id="brandName" value="<s:property value='#session.BRAND_NAME'/>" />
			<input type="hidden" id="category" value="<s:property value='#session.CATEGORY'/>" />
			
			
			<table width="544px" height="323px" border="0" cellspacing="1" cellpadding="0" bgcolor="#D4D4D4"
			 style="position: absolute;left:50%;top:50%;margin-left:-272px; margin-top:-175px; font-size:14px; font-family:微软雅黑; ">
			  <tr>
			    <td width="105px" height="33px">车型：</td>
			    <td width="168PX"><input type="text" id="brand" title="点击选择车型" /></td>
			    <td width="250px" rowspan="6"  colspan="2">
			    	<img id="img" width="260px" height="200px" />
			    </td>
			  </tr>
			  <tr>
			    <td height="33px">车系：</td>
			    <td><input type="text" id="carCategory" value="<s:property value='#session.CAR.carCategory'/>"/></td>
			  </tr>
			  <tr>
			    <td height="33px">车牌：</td>
			    <td><input type="text" id="carId" value="<s:property value='#session.CAR.carId'/>"/></td>
			  </tr>
			  <tr>
			    <td height="34px">发动机编号：</td>
			    <td><input type="text" id="engineNumber" width="150px" value="<s:property value='#session.CAR.engineNumber'/>" /></td>
			  </tr>
			  <tr>
			    <td height="34px">车架号：</td>
			    <td><input type="text" id="bodyNumber" width="150px" value="<s:property value='#session.CAR.bodyNumber'/>" /></td>
			  </tr>
			  <tr>
			    <td height="33px">车主：</td>
			    <td><input type="text" id="ownerName" value="<s:property value='#session.CAR.ownerName'/>"/></td>
			  </tr>
			  <tr>
			    <td height="33px">车主手机：</td>
			    <td height="33px"><input type="text" maxLength="15"  id="mobilePhone" value="<s:property value='#session.CAR.mobilePhone'/>" /></td>
			    <td rowspan="2"  colspan="2">
			    	<input type="file" width="140px" id="image" name="image"/>
			    	<input type="button" style="width:50px; margin-top:5px;" id="upload" value="上传"/>
			    </td>
			  </tr>
			   <tr>
			    <td height="33px">车源：<input type="hidden" id='carSourceId' value="<s:property value='#session.CAR.carSource' />" /></td>
			    <td height="33px">
			    	<select id="carSource" style="width:156px;">
			    
			    	</select>
			    </td>
			  </tr>
			  <tr>
			    <td height="33px">证件号：</td>
			    <td><input type="text" maxLength="20" id="idCardNumber" width="150px"
			    	value="<s:property value='#session.CAR.ownerIdCarNumber' />"/></td>
			    <td height="33px" style="width:90px;">
			    	当前里程：
			    </td>
			    <td>
			    	<s:property value='#session.CAR.km' />km
			    </td>
			  </tr>
			  <tr>
			    <td colspan="4" height="40px"><input type="button" style="width:50px;" value="保存" id="save" /></td>
			  </tr>
			</table>
		</form>
		
		
		<div style="width:400px; height:240px; padding:10px; display: none;background-color: white;" id="chooseType">
			<div style="padding-top:1px; width:380px;height:220px; overflow:auto; " id="carBrands"> 
			</div>
		</div>
	</body> 
</html>