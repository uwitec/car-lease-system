<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link rel="stylesheet" href="css/main.css" />
		<link rel="stylesheet" href="css/index.css" />
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/jquery.kernel.2.0.js"></script>
		<script type="text/javascript" src="js/jquery.query.js"></script>
		<title>租车管理系统</title>
		<script type="text/javascript">
			$(function(){
				pname();
				var div = $.dqgb.shadowLayer.customEncapsulate($("#sx"),{
					title:'测试浮动框',
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
				div.dialog.css({"position":"absolute","left":"auto","right":"10px","top":-div.dialog.outerHeight(true)}).hide();
				div.dialog.animate({
					"top":"10px",
					opacity:'show'
				});
				
				$("#idcard").query({
					url:'queryCustomer.action',
					miniQueryLength:4,									//输入4个字符以后开始查询
					width:200,
					height:100,
					remindMsg:'没有找到相关客户信息，请确认您的输入是否正确!',										//没有找到数据时的提示
					param : {
						pageSize:5,
						pageIndex:0
					},
					keyField:'idCardNumber',							//核心字段
					fields:['idCardNumber','customerName']				//配置需要显示的字段
				});
				
				
				$(".tab_panel_head").children("li").mouseover(function(){
					$(".tab_panel_head li").removeClass("li_over");
					$(this).addClass("li_over");
				});
			});
			function pp(p1,p2){
				alert(p1 + p2);
			}
			function pname(){
				pp.call(this,'s' ,'s');
				pp.apply(this,["sx2",'sx']);
			}
		</script>
	</head>
	<body>
		<form action="login.action" method="post">
			帐号:<input type="text" name="user.userName" />
			密码:<input type="text" name="user.password" />
			<input type="submit" value="submit" />
		</form>	
		<div id='sx' style="width:300px; height:100px;background-color: #EEEEEE; padding:10px; display: none;">
			<input type="text" value="sx" />
		</div>
		
		<div style="background-color:fuchsia; width:300px; height:230px; position:fixed; left:0px; top: 200px;">
		
		</div>
		
		<input type='text' id='idcard' />
		<div style="position: absolute;left:350px; top:160px; height:300px; width:346px;"> 
			<ul class="tab_panel_head" style="width:346px; height:27px;">
				<li>文件预警</li>
				<li>订单预警</li>
			</ul>
			<div style='width:344px; height:271px; overflow:hidden; border-left:1px solid #AACBEE; border-right:1px solid #AACBEE;'>
				<div class="item1">
					<input type="text"  value="1111"/>
				</div>
				<div class="item2">
					<input type="text"  value="222222"/>
				</div>
			</div>
			<div class="tab_panel_bottom">
			</div>
		</div>
		
		
	</body>
</html>