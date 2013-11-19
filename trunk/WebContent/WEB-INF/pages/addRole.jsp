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
		<title>用户管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/index.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.tree.js"></script>
		
		<SCRIPT type="text/javascript">
			$(function(){
				$.dqgb.button.modifyButtonStyle();
				var systemRoles = "";				//系统角色根节点
				var personalRoles = "";				//用户角色根节点

				$.dqgb.tree.beforeCreateNode = function(obj){
					if(obj.getNodeAttribute().leaf){
						obj.getNodeAttribute().iconCls = 'iconCls';
						obj.setNodeAttribute({
							dblclick:function(){
								//左键双击处理角色添加
								if(systemRoles == obj.getRoot()){
									var subNodes = personalRoles.getSubNodes();				//不重复添加角色
									for(var i = 0; i < subNodes.length; i++){
										if(subNodes[i].getNodeAttribute().id == obj.getNodeAttribute().id){
											return;
										}
									}
									$.dqgb.tree.appendChildNode(personalRoles,
										obj.getNodeAttribute(),{staticTree : true});
								}else if(personalRoles == obj.getRoot()){
									$.dqgb.tree.removeChildNode(obj);
								}
							}
						});
					}else{
					}
				};
				
				$.ajax({
					url:"getSystemRole.action",							//获取角色数据(系统角色和职位角色)
					type:"post",
					async:true,
					data:{
						pageIndex:0,
						pageSize:1000
					},
					success:function(data){
						if(data.totalSize){
							var treeData = new Array();
							for(var i = 0; i < data.totalSize; i++){
								treeData[treeData.length] = new Object();
								treeData[treeData.length - 1].text = data.data[i].remark;
								treeData[treeData.length - 1].id = data.data[i].id;
							}
							systemRoles = $.dqgb.tree.getDqgbTree({
								text:'系统现有角色',
								container:$("#treePanel"),
								staticTree : true,
								treeData:treeData,
								iconCls:'root'
							});
						}
					}
				});

				$.ajax({
					url:"getPersonalRole.action",						//获取个人角色数据
					type:"post",
					async:true,
					data:{
						userName:$("#userName").val()
					},
					success:function(data){
						var treeData = new Array();
						if(data.totalSize){
							var treeData = new Array();
							for(var i = 0; i < data.totalSize; i++){
								treeData[treeData.length] = new Object();
								treeData[treeData.length - 1].text = data.data[i].role;
								treeData[treeData.length - 1].id = data.data[i].roleId;
							}
						}
						personalRoles = $.dqgb.tree.getDqgbTree({
							text:'用户现有角色',
							container:$("#personalRole"),
							staticTree : true,
							treeData:treeData,
							iconCls:'root'
						});
					}
				});

				$("#savePersonalRole").click(function(){
					$.dqgb.shadowLayer.showMask({
						mask:"正在保存配置,请稍后...."
					});
					$.ajax({
						url:"addRole.action",				//给用户添加角色
						type:"post",
						async:true,
						data:{
							id:getPersonalRoleId(personalRoles),
							userName:$("#userName").val()
						},
						success:function(data){
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({
									content:data.REASON
								});
							}else{
								$.dqgb.shadowLayer.alert({
									content:"配置保存成功!"
								});
							}
						}
					});
				});
				
			});
			/*************
			*
			*@TODO	获取个人角色id集合
			*
			**************/
			function getPersonalRoleId(root){
				var sub = root.getSubNodes();
				var id = "";
				for(var i = 0; i < sub.length; i++){
					id += sub[i].getNodeAttribute().id;
					id += ",";
				}
				return id;
			};
		</SCRIPT>
	</head>
	<body style="text-align: center;">
		<div id="treePanel" style="width: 175px; height:160px; float:left; overflow: auto;">
		</div>
		<div id="personalRole" style="width:175px; height:160px; overflow: auto; float:left">
		</div>
		<input type="hidden" id="userName" value="<s:property value='#session.modifiedUserName'/>"/>
		<input type="button" id="savePersonalRole" style="width:50px;" value="保存"/>
	</body> 
</html>