/***********************************************************************************
 * 
 * @FILE		jquery.navigation.js
 * @AUTHOR		9528
 * @DATE		2011-3-13
 * @COPY-RIGHT	大庆金桥成都分公司
 * 
 * @TODO		1、导航条
 * 
 * @MODIFIED	2011-04-01	增加了下拉导航菜单		
 * 
 ***********************************************************************************/
(function($){
	
	if(!$.dqgb){
		$.dqgb = {};
	}
	
	$.extend($.dqgb,{
		navigation:{}						//创建命名空间
	});
	
	$.extend($.dqgb.navigation,{
		options : {
			subItemHeight : 32					//2级菜单的高度
		},
		//生成一个导航条
		generateNavigation : function(options){
			var nav = {};
			$.extend(nav,{
				options : options
			});
			$.extend(nav,{
				body : $("#" + nav.options.render).css({"margin":"0px","padding":"0px"}).shieldRightMenu().shieldSelectStart()	//nav的body
			});
			$.extend(nav,{
				/**
				 * @TODO	获取dom元素
				 */
				getDomElement : function(){
					return nav.body;
				},
				
				ul : $("<ul></ul>").appendTo(nav.body).css({"width":"100px","overflow":"hidden",
						"list-style":"none", "margin":"0px", "padding":"0px"}),			//nav 的ul
				/**
				 * @TODO	布局navigation
				 */
				doLayout : function(){
					var li = this.ul.children("li");
					for(var i = 0; i < li.length; i++){
						var pre = $(li[i]).prev("li");
						if(pre.length == 0){
							$(li[i]).css({"left":"0px","top":"0px"});
						}else{
							$(li[i]).css({
								"left": $.dqgb.getCssValue(pre.css("left")) + pre.outerWidth(true),
								"top":"0px"
							});
						}
					}
					return this;
				},
				/**
				 * @TODO	初始化导航条
				 */
				initialize : function(){
					for(var i = 0; i < this.options.item.length; i++){
						var item = this.options.item[i];
						if(options.item[i] == "-"){
							li = $("<li class='dqgb_nav_separator'></li>").css({
								"background-position":"center", "list-style":"none","z-index":"1", "position":"absolute"
							}).width(2).height(26).appendTo(this.ul);
							continue;
						}
						var li = $("<li class='_dqgb_nav_item'></li>").css({"left":"0px","top":"0px","margin":"0px","padding":"0px",
							"list-style":"none", "z-index":"1", "position":"absolute","height":"26px", "cursor":"pointer"
						}).appendTo(this.ul);
						var liWD = 0;
						if(item.iconCls){		//图标
							var icons = $("<div></div>").addClass(item.iconCls).css({
								"position":"absolute", "left":"8px","top":"0px",
								"width":"16px","height":"26px","background-repeat":"no-repeat","background-position":"center"
							}).appendTo(li);
							liWD += icons.outerWidth(true);
						}
						if(item.text){			//文本
							var text = $("<div class='nav_text'></div>").css({
											"position":"absolute", "left":"8px","top":"0px","font-family":"微软雅黑",
											"margin":"0px","padding":"0px" ,"font-size":"13px","height":"26px","line-height":"26px"
										}).appendTo($(document.body)).append("<span>" + item.text + "</span>");
							var span = text.children("span").get(0);
							var width = span.offsetWidth;
							var height = span.offsetHeight;
							text.width(width + 1).appendTo(li);
							if(item.iconCls){
								text.css({
									"left":$.dqgb.getCssValue(text.prev("div").css("left")) + text.prev("div").outerWidth(true) + "px",
									"padding-left":"3px"
								});
							}
							liWD += text.outerWidth(true);
						}
						li.width(liWD + 16);
						if(item.subNodes){
							nav.generateSubNavigation(li,item.subNodes);
						}
					}
					
					//创建游标
					$.extend(this,{
						cursor : $("<li></li>").attr("showTime",0).css({
										"list-style":"none","z-index":"0","margin":"0px","padding":"0px",
										"position":"absolute","height":"26px", "cursor":"pointer","display":"none"
									}).appendTo(this.ul),
						//布局cursor
						layoutCursor : function(){
							this.cursor.children(".dqgb_nav_center").width(this.cursor.outerWidth(true) - 6);
							return this;
						},
						//构造cursor
						initializeCursor : function(){
							var left = $("<div class='dqgb_nav_left _btn'></div>").appendTo(this.cursor);
							var center = $("<div class='dqgb_nav_center _btn'></div>").appendTo(this.cursor);
							var right = $("<div class='dqgb_nav_right _btn'></div>").appendTo(this.cursor);
							return this;
						}
					});
					
					//注册事件
					this.ul.children("li").each(function(index){
						if($(this).hasClass("dqgb_nav_separator")){
							return;
						}
						var timeout = "";
						$(this).mouseover(function(){
							var obj = $(this);
							nav.cursor.attr("showTime",parseInt(nav.cursor.attr("showTime")) + 1);
							clearTimeout(timeout);
							obj.children(".subNavItem").show();
							setTimeout(function(){
								if(nav.cursor.attr("showTime") != 0){
									nav.cursor.show().width(obj.outerWidth(true)).css({
										"left":obj.css("left")
									});
									nav.layoutCursor();
								}
							},50);
						}).mouseout(function(){
							nav.cursor.attr("showTime",parseInt(nav.cursor.attr("showTime")) - 1);
							var obj = $(this);
							timeout = setTimeout(function(){					//延迟刷新
								obj.children(".subNavItem").hide();
							},50);
							setTimeout(function(){
								if(nav.cursor.attr("showTime") == 0){
									nav.cursor.hide();
								}
							},50);
						}).click(function(){
							nav.ul.children("li").removeClass("dqgb_nav_selected");
							$(this).addClass("dqgb_nav_selected");
							if($.isFunction(nav.options.item[index].click)){
								nav.options.item[index].click.call();
							}
						});
					});
					return this.initializeCursor().layoutCursor();
				},
				/*****
				 * 
				 * @TODO	创建2级导航菜单
				 * @param	obj	对应的li元素，item为2级导航菜单的数据配置信息
				 * 
				 */
				generateSubNavigation : function(obj,item){
					var area = $("<div class='subNavItem'></div>").appendTo(obj).css({
						"position":"absolute","left":"0px","z-index":"100","top":"26px","width":obj.outerWidth(true) - 2 + "px",
						"height":"0px","border":"1px solid #C0BFAD","overflow":"hidden"
					});
					area.click(function(){							//prevent event spreading
						return false;			
					});
					var ul = $("<ul></ul>").css({
						"margin":"0px","padding":"0px","list-style":"none"
					}).appendTo(area).width(area.width());
					for(var i = 0; i < item.length; i++){
						var li = $("<li></li>").css({"margin":"0px","position":"absolute","background-color":"#FFF8D4","color":"black",
									"padding":"0px","list-style":"none","width":"100px",
									"left":"0px","top":($.dqgb.navigation.options.subItemHeight * i) + "px"
								}).appendTo(ul);
						li.height($.dqgb.navigation.options.subItemHeight).addClass("dqgb_nav_sub_sp");

						if(item[i].iconCls){		//图标
							var icons = $("<div></div>").addClass(item[i].iconCls).css({
								"position":"absolute", "left":"8px","top":"0px",
								"width":"16px","height":$.dqgb.navigation.options.subItemHeight + "px",
								"background-repeat":"no-repeat","background-position":"center"
							}).appendTo(li);
						}
						if(item[i].text){			//文本
							var text = $("<div></div>").css({
											"position":"absolute", "left":"30px","top":"0px","font-family":"微软雅黑",
											"margin":"0px","padding":"0px" ,"font-size":"13px",
											"height":$.dqgb.navigation.options.subItemHeight + "px",
											"line-height":$.dqgb.navigation.options.subItemHeight + "px"
										}).appendTo($(document.body)).append("<span>" + item[i].text + "</span>");
							var span = text.children("span").get(0);
							var width = span.offsetWidth;
							var height = span.offsetHeight;
							text.width(width + 30).appendTo(li);
							if(area.width() < width + 54){
								area.width(width + 54);
								ul.children("li").width(area.width());
							}
							li.width(area.width());
							area.height(area.height() + $.dqgb.navigation.options.subItemHeight);
						}
					}
					ul.children("li").each(function(index){
						var timeout = "";
						$(this).click(function(){
							if($.isFunction(item[index].click)){
								area.hide();
								nav.cursor.hide();
								item[index].click.call();
								nav.ul.children("li").removeClass("dqgb_nav_selected");
								area.parent().addClass("dqgb_nav_selected");
							}
							return false;						//阻止事件冒泡
						}).mouseover(function(){
							clearTimeout(timeout);
							$(this).addClass("dqgb_nav_sub_mouseover");
						}).mouseout(function(){
							var object = $(this);
							timeout = setTimeout(function(){
								object.removeClass("dqgb_nav_sub_mouseover");
							},20);
						});
					});
					ul.children("li:last").removeClass("dqgb_nav_sub_sp");
					area.hide();
				}
				
			});
			return nav.initialize().doLayout();
		}
	});
	
})(jQuery);