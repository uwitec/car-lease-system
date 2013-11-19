/***********************************************************************************
 * 
 * @FILE		jquery.kernel.js
 * @AUTHOR		9528
 * @DATE		2011-3-24
 * @COPY-RIGHT	大庆金桥成都分公司
 * 
 * @TODO		1、自动生成页面布局。
 * 				2、创建全局蒙层。
 * 				3、创建panel	
 * 				4、创建button
 * 		
 * @MODIFIED	1、修改了布局的方式，较以前方式更为稳定、简介，页面元素更少。同时增加更多的API,优化了整体代码
 * 				2、修改了shadowLayer类的窗口生成方式以及内部窗体的布局管理方式。
 * 				3、采用了代码的插件管理方式，不再需要手动初始化
 * 
 ***********************************************************************************/

(function($){
	
	$.extend($,{
		dqgb : {}														//扩展$对象,创建dqgb名字空间
	});
	
	$.extend($.dqgb,{
		layout : {},													//创建布局管理器名字空间	
		
		panel : {},														//面板名字空间
		
		button : {},													//button名字空间
		
		globalMsg : {},													//浮动消息框
		
		shadowLayer : {},												//蒙层名字空间
		
		/**********************************
		 * 
		 * @TODO	给table元素添加row
		 *
		 *********************************/
		addRow : function(table){
			return $.browser.mozilla ? table.insertRow(-1) : table.insertRow();
		},
		/**********************************
		 * 
		 * @TODO	给row元素添加column
		 *
		 *********************************/
		addColumn : function(row){
			return $.browser.mozilla ? row.insertCell(-1) : row.insertCell();
		},
		
		//创建dqgb名字空间的全局变量
		options : {
			opacity:25,													//蒙层透明度
			mask_Z_Index:999,											//mask 的z-index
			speed:400,													//蒙层显示的速度 500ms
			
			defaultLoadingFrameID : '__dqgb_default__loading_frame_id',	//loading框架默认的id
			defaultAlertID : '__dqgb_default__alert_id',				//alert框架默认的id
			defaultConfirmID : '__dqgb_default__confirm_id',			//confirm框架默认的id
			defaultLoadingMaskID : '__dqgb_default__mask_id',			//mask框架默认的id
			
			defaultButtonHeight: 22,									//默认button高度
			defaultButtonWidth: 50,										//默认button宽度
			divButtonCls:"___dqgb_div_button",							//默认divbutton的样式
			msgBoxContentSpanCls : "__dqgbContentSpanCls"				//msgBox，confirmBox保存内容的span的样式，
		},
		
		/***
		 *	
		 *	@TODO	屏蔽右键菜单
		 *	@DATE	2011-2-23
		 * 
		 */
		shieldRightMenu : function(obj){
			if($.browser.mozilla){
				obj.get(0).oncontextmenu = function(){ 
				     return false; 
				}; 
			}else if($.browser.msie){
				obj.get(0).oncontextmenu = new Function("event.returnValue=false;");
			}
		},
		
		/***
		 *	
		 *	@TODO	屏蔽onselectstart 事件
		 *	@DATE	2011-2-23
		 * 
		 */
		shieldSelectStart : function(obj){
			if($.browser.mozilla){
				obj.css("-moz-user-select","-moz-none");
			}else if($.browser.msie){
				obj.get(0).onselectstart = new Function("event.returnValue=false;");
			}
		},
		
		/***
		 * 
		 * @TODO	把一个css样式数据转化成整数，如412px --> 412
		 * 
		 */
		getCssValue : function(value){
			return parseInt(value.substr(0,value.indexOf("px")));
		},
		/***
		 * 
		 * @TODO	解析百分数，50%-->0.5
		 * 
		 */
		parsePercent : function(value){
			value = value.substr(0,value.indexOf("%"));
			return value/100;
		},
		
		resizeFn : [],								//存储wnd resize的时候可以调用的方法
		
		/***
		 * @TODO	重新布局,在wnd尺寸发生变化的时候触发
		 */
		doLayout : function(){
			var len = $.dqgb.resizeFn.length;
			for(var i = 0; i < len; i++){
				$.dqgb.resizeFn[i].call();
			}
		},
		
		/**
		 * @TODO	注册布局函数,先注册的后被执行,在wnd尺寸发生变化的时候这些函数将会被执行
		 */
		registerLayoutFn : function(fn){
			if($.isFunction(fn)){
				$.dqgb.resizeFn.unshift(fn);		//unshift,push
			}
		}
	});
	
	/**
	 * 
	 * (bug：	ie8以及firefox在resize时会触发滚动条的位置,偶尔会出现偏差)
	 * 采用延迟双调整方案(两次调整尺寸)。
	 * 
	 */
	$(window).bind("resize",function(ev){
		clearTimeout($.dqgb.timer);
		$.dqgb.doLayout();
		$.dqgb.timer = setTimeout(function(){			//延迟修正，提高代码执行效率
			$.dqgb.doLayout();							//修正滚动条bug
		},200);
	});
	
	$.extend($.dqgb.layout,{
		/************************************************************
		 * 
		 * @TODO		布局管理函数,兼容ie6,ie7,ie8以及firefox
		 * @DATE		2011-2-22
		 * @return		布局管理对象，用来设置center中的页面(setURL)
		 * @MODIFIED	2011-3-24
		 * 
		 ***********************************************************/
		borderLayout : function(options){
			$(document.body).css({"margin":"0px","padding":"0px"});	
			var layout = {};
			$.extend(layout,{
				options : options,
				north 	: (options.north ? $("#" + options.north.render) : null),
				south 	: (options.south ? $("#" + options.south.render) : null),
				west  	: (options.west ? $("#" + options.west.render) : null),
				center	: ($("#" + options.center.render)),
				middle	: $("<div></div>").appendTo($(document.body)),					//west 和 center 的容器
				iframe  : $("<iframe frameBorder=0></iframe>").css("background-color","white"),
				
				initialized : false,													//布局管理器初始化阀门			
				
				/******************
				 * 
				 * @TODO	调整布局
				 * 
				 *****************/
				doLayout : function(){
					var wnd = $(window);
					if(this.north){																				//调整北部
						this.north.css({"left":"0px", "top":"0px", "width":wnd.width() - 2}).height(this.options.north.height);
					}
					
					this.middle.addClass("global_background global_border").css({							//调整中部
						"left":"0px", "border-top":"0px", "border-bottom":"0px", "position":"absolute", "overflow":"hidden",
						"top" : (this.north ? this.north.outerHeight(true) : 0) + "px"
					}).width(wnd.width() - 2).height(wnd.height() - 
							(this.north ? this.north.outerHeight(true) : 0) - 
							(this.south ? this.south.outerHeight(true) : 0));
					
					if(this.south){																				//调整南部
						this.south.css({"left":"0px", "width":wnd.width() - 2, 
							"top":(this.north ? this.north.outerHeight(true) : 0) + this.middle.outerHeight(true) + "px"
						}).height(this.options.south.height);
					}
					
					this.mainLayout();
					
					return this;
				},
				
				/******************
				 * 
				 * @TODO	调整center区域的布局
				 * 
				 *****************/
				mainLayout : function(){
					if(this.west){
						if(!this.initialized){							//初始化原始宽度
							this.initialized = true;
							this.westContainer.width(this.options.west.width + 9 + 6);
							this.west.width(this.options.west.width).addClass("global_border").css({
								"background-color":"white", "position":"absolute", "left":"6px", "top":"0px","overflow":"auto"
							});
						}
						this.westContainer.height(this.middle.height() - 12);
						this.west.height(this.westContainer.height() - 2);
						this.center.css({
							"left": layout.westContainer.outerWidth(true) + "px"
						}).width(this.middle.width() - 8 - this.westContainer.outerWidth(true)).addClass("global_border");
					}else{
						this.center.css({"left": "6px"}).width(this.middle.width() - 14).addClass("global_border");
					}
					this.center.height(layout.middle.height() - 14);
					this.iframe.width(this.center.width()).height(this.center.height());
				},
				
				/******************
				 * 
				 * @TODO	设置center区域页面连接
				 * 
				 *****************/
				setURL : function(url){
					this.iframe.get(0).src = url;
				}
			});
			
			$.extend(layout,{
				westContainer : (layout.west ? $("<div></div>").appendTo(layout.middle) : null),	//西部区域容器
				//分隔符
				separator : (layout.west ? $("<div class='dqgb_layout_separator layout_separator_left_normal'></div>").appendTo($(document.body)) : null)
			});
			
			//北部区域
			if(layout.north){
				layout.north.css({"overflow":"hidden","position":"absolute"}).addClass("global_border");
			}
			
			//南部
			if(layout.south){
				layout.south.css({"overflow":"hidden","position":"absolute"}).addClass("global_border").addClass("global_background");
			}

			layout.middle.css({"position":"absolute","overflow":"hidden"}).addClass("global_background");		//中间区域
			
			//西部区域
			if(layout.westContainer){
				layout.westContainer.css({"left":"0px", "top":"6px", "overflow":"hidden", "position":"absolute"}).append(layout.west);
				//注册分隔符鼠标事件
				layout.separator.appendTo(layout.westContainer).click(function(){
					if(layout.westContainer.width() != 6){
						layout.westContainer.animate({
							width : 6
						},300,function(){
							layout.mainLayout();
							layout.separator.addClass("layout_separator_right_normal").removeClass("layout_separator_left_over").removeClass("layout_separator_left_normal").removeClass("layout_separator_right_over");
						});
					}else{
						layout.westContainer.animate({
							width : layout.options.west.width + 9 + 6
						},300,function(){
							layout.mainLayout();
							layout.separator.addClass("layout_separator_left_normal").removeClass("layout_separator_left_over").removeClass("layout_separator_right_normal").removeClass("layout_separator_right_over");
						});
					}
					
				}).mouseover(function(){
					if($(this).hasClass("layout_separator_left_normal")){
						$(this).addClass("layout_separator_left_over").removeClass("layout_separator_left_normal");
					}else{
						$(this).addClass("layout_separator_right_over").removeClass("layout_separator_right_normal");
					}
				}).mouseout(function(){
					if($(this).hasClass("layout_separator_left_over")){
						$(this).removeClass("layout_separator_left_over").addClass("layout_separator_left_normal");
					}else if($(this).hasClass("layout_separator_right_over")){
						$(this).removeClass("layout_separator_right_over").addClass("layout_separator_right_normal");
					}
				});
			}
	
			//中心区域
			layout.center.css({
				"top":"6px", "overflow":"hidden", "position":"absolute"
			}).appendTo(layout.middle).append(layout.iframe.get(0));
			
			//注册布局管理函数，以便窗口尺寸发生变化时调整布局
			$.dqgb.registerLayoutFn(function(){
				layout.doLayout();
			});
			
			if(layout.options.center.url){
				layout.setURL(layout.options.center.url);
			}
			
			return layout.doLayout();
		}
	});
	
	
	//扩展panel名字空间
	
	$.extend($.dqgb.panel,{
		/*****************
		 * 
		 * @TODO	生成一个panel 的头部，可以用于 grid dialog等窗口
		 * 
		 */
		getPanelHeader : function(options){
			var header = {};
			$.extend(header,{
				options : options,
				iconCls : '',						//icon的样式
				initialized : false,
				headerBar : $("<div></div>").appendTo(options.parent ? options.parent : $(document.body)).css({
					"height" : "22px",
					"width" : '100%',
					"text-indent":"0em", "overflow":"hidden", "margin":"0px", "padding":"0px"
				}).shieldRightMenu().shieldSelectStart()
			});
			
			$.extend(header,{
				leftCorner  : $("<div class='dqgb_grid_head dqgb_grid_head_left'></div>").appendTo(header.headerBar),			//左边区域
				
				middle : $("<div class='dqgb_grid_head dqgb_grid_head_middle bg_repeatX'></div>").css({"position":"relative"}).appendTo(header.headerBar),	//中间区域
				
				rightCorner : $("<div class='dqgb_grid_head dqgb_grid_head_right'></div>").css("float","right").appendTo(header.headerBar),			//右边区域
				
				/***
				 * 
				 * @TODO	重新布局，当页面元素配置发生更改时调用,重新注册关闭函数时也需要调用该函数
				 * 
				 */
				doLayout : function(options){
					options.parent = options.parent ? options.parent : this.options.parent;
					this.options = options;
					
					this.resize();			
					
					//定制icon
					if(this.options.iconCls){
						if(this.iconCls){
							this.icon.removeClass(this.iconCls);
						}
						this.iconCls = this.options.iconCls;
						this.icon.addClass(this.options.iconCls).show();
						this.text.html(this.options.title);
					}else{
						this.icon.hide();
						this.text.css("margin-left","3px").html(this.options.title);
					}
					//客户化字体
					if(this.options.fontSize){
						this.text.css("font-size",this.options.fontSize);
					}
					
					//定制btn
					if(this.options.closeBtn){
						this.closeBtn.show();
						header.closeBtn.unbind("click").click(function(){
							if(options.floatBox){
								if($.isFunction(header.options.closeBtn.close)){
									header.options.closeBtn.close.call(this,header.options.parent);
								}
							}else{
								$.dqgb.shadowLayer.globalLayer.hideLayer();
								$.dqgb.shadowLayer.getDialog().animate({									
									left:"0px",
									opacity:'hide'
								},$.dqgb.options.speed,function(){
									if($.isFunction(header.options.closeBtn.afterClose)){
										header.options.closeBtn.afterClose.call();
									}
								});
							}
						});
					}else{
						this.closeBtn.hide();
					}
					return this;
				},
				/*********
				 * 
				 * @TODO	重新调整页面元素尺寸
				 * 
				 */
				resize : function(){
					this.headerBar.css({"width" : (this.options.parent ? this.options.parent.css("overflow","hidden").width() : 100)});
					this.middle.css("width","0px").width(0);		//修正ie6,7,百分比和整数搭配时的 错位bug
					this.middle.css("width",$.dqgb.getCssValue(this.headerBar.css("width")) - 8).width(this.headerBar.width() - 8);
					if(!this.initialized){
						this.icon.css({"background-repeat":"no-repeat","background-position":"center"});
						if($.browser.msie && $.browser.version == "6.0"){									//纠正ie6的bug
							this.icon.css({"margin-top":"0px","margin-left":"2px"});
						}
						this.initialized = true;
					}
					return this;
				}
			});
			$.extend(header,{
				icon : $("<div class='dqgb_grid_head_icon'></div>").appendTo(header.middle),			  //icon区域
				
				text : $("<div class='dqgb_grid_head_text'></div>").appendTo(header.middle).css({"cursor":"default","overflow":"hidden"}),			  //text区域
				
				closeBtn : $("<div title='关闭' class='dqgb_panel_close'></div>").appendTo(header.middle)
							.mouseover(function(){								//close 按钮
								$(this).addClass("dqgb_panel_close_over");
							}).mouseout(function(){
								$(this).removeClass("dqgb_panel_close_over");
							}) ,
				/**
				 * @TODO	设置表头title
				 * @param   title
				 */			
				setTitle:function(title){
					this.text.html(title);
				}
			});
			
			$.dqgb.registerLayoutFn(function(){
				header.resize();
			});
			
			return header.doLayout(options);
		}
	});
	
	//扩展蒙层对象
	$.extend($.dqgb.shadowLayer,{
		globalLayer  : 	"",								
						
		dialog : "",						//蒙层上显示的对话框

		dialogOption : {},					//对话框的配置信息
		
		msgBoxList : [],					//存储封装好的页面元素的js对象,以防重复渲染页面中元素,页面中的元素必须以id调用dqgbBox否则无法存储
		
		/***
		 * 
		 * @TODO	从msgBoxList中获取缓存下来的页面元素，如果没有找到元素则返回""
		 * @DATE	2011-03-26
		 * 
		 */
		getDialogFromList : function(id){
			for(var i = 0; i < this.msgBoxList.length; i++){
				if(this.msgBoxList[i].id == id){
					return this.msgBoxList[i];
				}
			}
			return "";
		},
		
		/****
		 * @TODO	获取阴影层
		 * @DATE	2011-03-26
		 */
		getGlobalLayer : function(){
			if(!this.globalLayer){
				var layer = {};
				$.extend(layer,{
					shadow : $("<div></div>").css({							//蒙层							
						"position":"absolute", "left":"0px", "z-index":$.dqgb.options.mask_Z_Index, "background-color":"black",
						"filter":"alpha(opacity=" + $.dqgb.options.opacity + ")", "-moz-opacity":"0." + $.dqgb.options.opacity,
						"display":"none", "opacity":"0." + $.dqgb.options.opacity, "top":"0px"
					}).appendTo($(document.body)).shieldRightMenu().shieldSelectStart(),
					
					/***
					 * @TODO	显示全局蒙层
					 */
					showLayer :  function(){
						this.resizeLayer().shadow.fadeIn($.dqgb.options.speed);
						return layer;
					},
					/**
					 * @TODO	调整layer尺寸
					 */
					resizeLayer : function(){
						var wnd = $(window);
						layer.shadow.css({
							"width" : wnd.scrollLeft() + wnd.width(),
							"height" : wnd.scrollTop() + wnd.height()
						});
						if($.browser.msie && $.browser.version == "6.0"){
							this.shadow.children("iframe").width(this.shadow.width()).height(this.shadow.height());
						}
						return layer;
					},
					hideLayer : function(){
						layer.shadow.fadeOut($.dqgb.options.speed,function(){
							$(window).trigger("resize");
						});
					}
				});
				if($.browser.msie && $.browser.version == "6.0"){
					$("<iframe></iframe>").appendTo(layer.shadow).css({
						"position":"absolute", "left":"0px", "background-color":"black",
						"filter":"alpha(opacity=" + 0 + ")", "-moz-opacity":"0.00",
						"opacity":"0.00" , "top":"0px"
					});
				}
				//注册窗口resize事件
				$.dqgb.registerLayoutFn(function(){
					var wnd = $(window);
					var timeout = "";
					clearTimeout(timeout);
					timeout = setTimeout(function(){
						if($.dqgb.shadowLayer.globalLayer && $.dqgb.shadowLayer.globalLayer.shadow.css("display") == "block"){
							$.dqgb.shadowLayer.globalLayer.resizeLayer();
						}
						if($.dqgb.shadowLayer.getDialog() && $.dqgb.shadowLayer.getDialog().css("display") == "block"){
							var left = (wnd.width() - $.dqgb.shadowLayer.getDialog().outerWidth(true)) / 2 + wnd.scrollLeft();
							left = left > 0 ? left : 0;
							var top = (wnd.height() - $.dqgb.shadowLayer.getDialog().outerHeight(true)) / 2 + wnd.scrollTop();
							top = top > 0 ? top : 0;
							$.dqgb.shadowLayer.getDialog().css({
								"left": left + "px",
								"top": top + "px"
							});
						}
					},50);
					
				});
				
				var timeout = "";
				//注册窗口滚动事件
				$(window).unbind("scroll").scroll(function(){
					clearTimeout(timeout);
					timeout = setTimeout(function(){
						if($.dqgb.shadowLayer.globalLayer && $.dqgb.shadowLayer.globalLayer.shadow.css("display") == "block"){
							$.dqgb.shadowLayer.globalLayer.resizeLayer();
						}
						if($.dqgb.shadowLayer.getDialog() && $.dqgb.shadowLayer.getDialog().css("display") == "block"){
							var wnd = $(window);
							if(wnd.height() > $.dqgb.shadowLayer.getDialog().outerHeight(true)){			//2011-2-24 增添判别
								$.dqgb.shadowLayer.getDialog().css({
									"top":(wnd.height() - $.dqgb.shadowLayer.getDialog().outerHeight(true)) / 2 + wnd.scrollTop()
								});
							}
							if(wnd.width() > $.dqgb.shadowLayer.getDialog().outerWidth(true)){			//2011-2-24 增添判别
								$.dqgb.shadowLayer.getDialog().css({
									"left":(wnd.width() - $.dqgb.shadowLayer.getDialog().outerWidth(true)) / 2 + wnd.scrollLeft()
								});
							}
						}
					},10);
				});
				
				this.globalLayer = layer;
			}
			return this.globalLayer;
		},
		
		/***
		 * @TODO	封装弹出框对象,封装成js对象
		 */
		generateDialog : function(element,options){
			if(!this.dialog){
				this.dialog = {};
			}
			this.dialog.dialog = element;
			return this.dialog;
		},
		/****
		 * @TODO	获取对话框对象的dom(jQuery)对象
		 */
		getDialog : function(){
			return this.dialog ? this.dialog.dialog : "";
		},
		/**********************************************************
		 * 
		 * @TODO		生成对话框
		 * @PARAM		element：对话框对象，options：对话框配置参数信息
		 * @author		9528
		 * @DATE		2011-3-26
		 * 
		 ********************************************************/
		dqgbBox : function(element,options){
			if(this.getDialog()){
				this.getDialog().hide();
			}
			var wnd = $(window);
			this.dialogOption = options;										//保存options选项
		
			var _dialog = this.getDialogFromList(element.get(0).id);			//从系统列表中获取数据

			this.dialog = _dialog ? _dialog.doLayout(options) : (options.title ? this.customEncapsulate(element,options) : this.encapsulateMask(element,options));
			
			this.getGlobalLayer().showLayer();
			
			this.getDialog().css({
				"position":"absolute",
				"top":(wnd.height() - this.getDialog().outerHeight(true)) / 2 + wnd.scrollTop(),
				"left":0,
				"z-index":parseInt($.dqgb.options.mask_Z_Index) + 1
			});
			
			//如果自定义了动画则调用自定义动画
			if($.isFunction(this.dialogOption.animateShow)){
				this.dialogOption.animateShow.call(this,this.getDialog());
			}else{
				this.getDialog().animate({
					left:(wnd.width() - this.getDialog().outerWidth(true)) / 2 + wnd.scrollLeft(),
					opacity:'show'
				},$.dqgb.options.speed);											//显示对话框
			}
		},
		
		/*****
		 * 
		 * @TODO	封装mask元素
		 * 
		 */
		encapsulateMask : function(element,options){
			var dialog = {};
			$.extend(dialog,{
				id : element.get(0).id,
				dialog : element,
				options : options,
				doLayout : function(options){return this;}
			});
			this.msgBoxList[this.msgBoxList.length] = dialog;
			return dialog;
		},
		/***
		 * 
		 * @TODO		封装对话框，封装之前会可以通过覆盖该函数($.dqgb.shadowLayer.customEncapsulate)来实现客户自定义的对话框样式
		 * 
		 */
		customEncapsulate : function(element,options){
			if(!options.system){
				element.css({"position":"absolute","left":"0px","top":"0px"});
			}
			var dialog = {};
			options.width = options.width ? options.width : element.outerWidth(true);
			options.height = options.height ? options.height : element.outerHeight(true);
			$.extend(dialog,{
				id : element.get(0).id,
				dialog : $("<div class='dialog_frame'></div>").css({				//窗体
							"position":"absolute", "left":"0px", "top":"0px","overflow":"hidden","display":"none",
							"width":options.width, "height":options.height
						}).appendTo($(document.body)),
				options : options,
				header : $.dqgb.panel.getPanelHeader(options)
			});
			
			dialog.header.headerBar.appendTo(dialog.dialog);
			options.parent = dialog.dialog;				//修改option 设置header的父窗口
		
			$.extend(dialog,{
				innerFrame : $("<div></div>").css({
					"overflow":"hidden"
				}).appendTo(dialog.dialog).addClass("global_border global_background"),
				/******
				 * @TODO	重新布局
				 */			
				doLayout : function(options){
					options.width = options.width ? options.width : this.options.width;
					options.height = options.height ? options.height : this.options.height;
					this.options = options;
					if(!this.options.system){
						this.element.css({"position":"static"}).show();
					}
					this.dialog.width(this.options.width + 14).height(this.options.height + 24 + 14);
					this.innerFrame.width(this.dialog.width() - 14).height(this.dialog.height() - 24 - 14).css({
						"padding" : "6px"}).addClass("global_border");
					var padding = 2 * $.dqgb.getCssValue(this.element.css("padding"));
					if(!this.options.system){												//系统框 包括alert框和confirm框						
						this.element.css({"width" : this.options.width - padding - 2,
							"height" : this.options.height - padding - 2
						}).addClass("global_border");
					}else{
						this.element.css({"width" : this.options.width - padding,
							"height" : this.options.height - padding
						}).removeClass("global_border");
					}
					this.header.doLayout(options);
					this.dialog.css("display","none");
					return this;
				}
			});
			$.extend(dialog,{
				element :  (dialog.options.system ? element.css({"display":"block"}).appendTo(dialog.innerFrame) : element.appendTo(dialog.innerFrame))//确保封装后内容是可见的
			});
			dialog.doLayout(options);
			
			if(!options.floatBox)				//浮动框不管理		
				this.msgBoxList[this.msgBoxList.length] = dialog;		
			
			return dialog;
		},
		/********
		 * 
		 * @TODO	隐藏对话框	
		 * 
		 */
		hide : function(){
			function close(){
				$.dqgb.shadowLayer.globalLayer.hideLayer();
				if($.isFunction($.dqgb.shadowLayer.dialogOption.animateHide)){					//如果自定义关闭动画则调用自定义关闭动画
					$.dqgb.shadowLayer.dialogOption.animateHide.call();
				}else{
					$.dqgb.shadowLayer.getDialog().animate({									//默认动画
						left:"0px",
						opacity:'hide'
					},$.dqgb.options.speed,function(){
						if($.dqgb.shadowLayer.dialogOption && $.dqgb.shadowLayer.dialogOption.afterClose && $.isFunction($.dqgb.shadowLayer.dialogOption.afterClose)){
							$.dqgb.shadowLayer.dialogOption.afterClose.call();
						}
					});
				}
			};
			//如果注册了 beforeClose 事件,beforeClose事件函数返回true则执行close，否则不执行close
			if(this.dialogOption && this.dialogOption.beforeClose && $.isFunction(this.dialogOption.beforeClose)){
				if(this.dialogOption.beforeClose.call()){
					close();
				}
			}else{
				close();
			}
		},
		
		iframeContainer : "",								//存储loadingFrame的dom元素
		
		getLoadingFrame : function(options){
			options.width = options.width ? options.width : 352;
			options.height = options.height ? options.height : 202;
			if(!this.iframeContainer){
				this.iframeContainer = $("<div id = '" + $.dqgb.options.defaultLoadingFrameID + "'></div>").css("display","none").css({
					"overflow" : "hidden","margin":"0px","padding":"0px","background-color":"white",
					"width" : options.width,
					"height" : options.height		
				}).addClass("dqgb_border").appendTo($(document.body));
				var iframe = $("<iframe frameBorder=0></iframe>").appendTo(this.iframeContainer);
				iframe.get(0).width = options.width - 2;
				iframe.get(0).height = options.height - 2;
				iframe.get(0).src = options.url;
			}else{
				var iframe = this.iframeContainer.children("iframe");
				iframe.get(0).width = options.width - 2;
				iframe.get(0).height = options.height - 2;
				iframe.get(0).src = options.url;
			}
			//注册动画显示函数
			$.extend(options,{
				animateShow : function(Obj){
					var wnd = $(window);
					Obj.css({
						"left":(wnd.width() - Obj.outerWidth(true)) / 2 + wnd.scrollLeft()
					});
					Obj.fadeIn($.dqgb.options.speed);
				}
			});
			return this.iframeContainer;
		},
		/**********************************
		 * 
		 * @TODO	加载页面,生成一个包含iframe的div,由默认方法来包装
		 * @DATE	2011-3-14
		 * @AUTHOR	9528
		 */
		loading : function(options){
			this.getLoadingFrame(options).dqgbBox(options);
		},
		
		alertContainer : "",			//存储alert框的dom(jQuery)对象
		
		//封装alert对象的div框
		getAlertFrame : function(options){
			if(!this.alertContainer){
				this.alertContainer = $("<div id = '" + $.dqgb.options.defaultAlertID + "'></div>").css({
					"overflow" : "hidden","margin":"0px","padding":"0px",
					"width" : options.width ? options.width : 200,"position":"relative",
					"height" : options.height ? options.height : 80	,
					"font-size":"14px", "font-family":"微软雅黑", "text-align":"center"		
				}).appendTo($(document.body)).append($("<span></span>")).shieldSelectStart().shieldRightMenu();
				this.alertContainer.children("span").html(options.content);
				var btn = $.dqgb.button.getDivButton({
					text : options.yesText ? options.yesText : "确认",
					click : function(){
						$.dqgb.shadowLayer.hide();					//回调函数交由shadowLayer的hide函数回调
					}
				});
				this.resize(this.alertContainer,options);
				this.alertContainer.hide();
				btn.css({"position":"absolute","left":"50%","bottom":"3px","margin-left":-btn.outerWidth(true)/2 + "px"}).appendTo(this.alertContainer);
			}else{
				this.alertContainer.css({"width":"200px", "height":"80px"}).width(200).height(80);
				var frame = this.alertContainer.parent().parent().show();
				this.alertContainer.removeClass("dqgb_content_text");
				this.resize(this.alertContainer,options);
				frame.hide();
			}
			return this.alertContainer;
		},
		
		/*******************************
		 * 
		 * @TODO	调整alert框的尺寸
		 * 
		 *******************************/
		resize : function(obj,options){
			obj.children("span").html(options.content);
			var span = obj.children("span").get(0);
			var width = span.offsetWidth;
			var height = span.offsetHeight;
			if(width > 200 || height > 30){				//行数大于1
				obj.addClass("dqgb_content_text");
				width = span.offsetWidth;
				height = span.offsetHeight;
			}
			if(width > obj.width()){
				obj.width(obj.width() + 30);
				this.resize(obj,options);
			}
			if(height >= (width / 2 - 30) && height > 30){	//高度大于宽度的1/2则调整比例,且行数大于1
				obj.width(obj.width() + 30);
				this.resize(obj,options);
			}else{
				if(height > 50)
					obj.height(height + 30);
				$.extend(options,{
					width:obj.width(),
					height:obj.height()
				});
			}
			
		},
		
		/****************************
		 * 
		 * @TODO	生成alert框
		 * 
		 ***************************/
		alert : function(options){
			$.extend(options,{
				closeBtn : false,				//强制关闭close button
				system : true,
				iconCls : options.iconCls ? options.iconCls : "dqgb_panel_header_msg_icon",
				title : options.title ? options.title : "温馨提示"
			});
			this.getAlertFrame(options).dqgbBox(options);
		},
		
		confirmContainer : "",						//confirm框的dom(jQuery)的容器
		
		//获取confirm框的div dom元素
		getConfirmFrame : function(options){
			if(!this.confirmContainer){
				this.confirmContainer = $("<div id = '" + $.dqgb.options.defaultConfirmID + "'></div>").css({
					"overflow" : "hidden","margin":"0px","padding":"0px",
					"width" : options.width ? options.width : 200,"position":"relative",
					"height" : options.height ? options.height : 80	,
					"font-size":"14px", "font-family":"微软雅黑", "text-align":"center"		
				}).appendTo($(document.body)).append($("<span></span>")).shieldSelectStart().shieldRightMenu();
				this.confirmContainer.children("span").html(options.content);
				var yes = $.dqgb.button.getDivButton({text : options.yesText ? options.yesText : "确认", 
					click : function(){
						$.dqgb.shadowLayer.hide();					//回调函数交由shadowLayer的hide函数回调
					}
				});
				var cancel = $.dqgb.button.getDivButton({ text : options.cancelText ? options.cancelText : "取消",
					click : function(){
						$.dqgb.shadowLayer.globalLayer.hideLayer();	
						$.dqgb.shadowLayer.getDialog().animate({									//默认动画
							left:"0px",
							opacity:'hide'
						},$.dqgb.options.speed);
					}
				});
				var btnContainer = $("<div></div>").css({"position":"absolute","left":"50%","bottom":"3px","height":"30px",
					"width" : yes.outerWidth(true) + cancel.outerWidth(true) + 10 + "px",
					"margin-left": 0 - (yes.outerWidth(true) + cancel.outerWidth(true) + 10)/2 + "px"
				}).appendTo(this.confirmContainer);
				
				yes.css({"position":"absolute","left":"0px","bottom":"0px"}).appendTo(btnContainer);
				cancel.css({"position":"absolute","right":"0px","bottom":"0px"}).appendTo(btnContainer);
				
				this.resize(this.confirmContainer,options);
				this.confirmContainer.hide();
			}else{
				this.confirmContainer.css({"width":"200px", "height":"80px"}).width(200).height(80);
				var frame = this.confirmContainer.parent().parent().show();
				this.confirmContainer.removeClass("dqgb_content_text");
				this.resize(this.confirmContainer,options);
				frame.hide();
			}
			return this.confirmContainer;
		},
		/****************************
		 * 
		 * @TODO	生成confirm框
		 * 
		 ***************************/
		confirm : function(options){
			$.extend(options,{
				closeBtn : false,				//强制关闭close button
				system : true,
				iconCls : options.iconCls ? options.iconCls : "dqgb_panel_header_confirm_icon",
				title : options.title ? options.title : "温馨提示"
			});
			this.getConfirmFrame(options).dqgbBox(options);
		},
		
		maskContainer : "",
		/********
		 * 
		 * @TODO:	生成一个等待的mask
		 */
		showMask : function(options){
			if(!this.maskContainer){
				var panel = $("<div id='" + $.dqgb.options.defaultLoadingMaskID + "' style='white-space:nowrap;'></div>").appendTo($(document.body)).css({
					"background-color":"white", 'white-space':'nowrap', "height":"30px", "width":"100px", "border":"1px solid #6593CF"
				}).addClass("dqgb_loading_mask");
				
				var innerFrame = $("<div></div>").appendTo(panel).css({
					"margin":"2px", "width":panel.width() - 6, "height":panel.height() - 6, "background-color":"#FBFBFB", "border":"1px solid #A3BAD9"
				});
				var image = $("<div class='dqgb_loading_wait'></div>").appendTo(innerFrame).css({
					"width" : innerFrame.height(), "float" : "left", "height" : innerFrame.height()
				});
				var text = $("<div class='text'></div>").appendTo(innerFrame).css({
					"margin-left":"2px", "white-space":"nowrap",
					"float" : "left", "height" : innerFrame.height(), "line-height":innerFrame.height() - 2 + "px",
					"overflow":"visible", "width":"20px", "font-size":"13px", "font-family":"微软雅黑"
				});
				text.html("<span>" + options.mask + "</span>");
				var wd = text.children("span").get(0).offsetWidth;
				panel.css("width",wd + 2 + image.width() + 6 + 10);
				innerFrame.css("width",panel.width() - 6);
				this.maskContainer = panel.shieldSelectStart().shieldRightMenu();
				$.extend(options,{
					title : "",
					animateShow : function(obj){
						var wnd = $(window);
						this.maskContainer.css({
							left:(wnd.width() - this.maskContainer.outerWidth(true)) / 2 + wnd.scrollLeft()
						});
						this.maskContainer.fadeIn($.dqgb.options.speed);
					}
				});
			}else{
				this.maskContainer.css({"display":"block"});
				var image = this.maskContainer.children("div").children(".dqgb_loading_wait");
				var span = this.maskContainer.children("div").children(".text").children("span").html(options.mask);
				var wd = span.get(0).offsetWidth;
				this.maskContainer.children("div").css("width",wd + 2 + image.width() + 10).width(wd + 2 + image.width() + 10);
				this.maskContainer.css("width",wd + 2 + image.width() + 6 + 10).width(wd + 2 + image.width() + 6 + 10);
				$.extend(options,{
					title : "",
					animateShow : function(obj){
						var wnd = $(window);
						this.maskContainer.css({
							left:(wnd.width() - this.maskContainer.outerWidth(true)) / 2 + wnd.scrollLeft()
						});
						this.maskContainer.fadeIn($.dqgb.options.speed);
					}
				});
			}
			this.maskContainer.css({"display":"none"}).dqgbBox(options);
		},
		/**
		 * @TODO	设置mask
		 */
		setMask : function(options){
			var image = this.maskContainer.children("div").children(".dqgb_loading_wait");
			var span = this.maskContainer.children("div").children(".text").children("span").html(options.mask);
			var wd = span.get(0).offsetWidth;
			this.maskContainer.children("div").css("width",wd + 2 + image.width() + 10).width(wd + 2 + image.width() + 10);
			this.maskContainer.css("width",wd + 2 + image.width() + 6 + 10).width(wd + 2 + image.width() + 6 + 10);
			var wnd = $(window);
			this.maskContainer.css({
				left:(wnd.width() - this.maskContainer.outerWidth(true)) / 2 + wnd.scrollLeft()
			});
		},
		//隐藏mask
		hideMask : function(){
			this.maskContainer.fadeOut($.dqgb.options.speed);
			this.getGlobalLayer().hideLayer();
		}
	});
	
	//扩展button区域
	$.extend($.dqgb.button,{
		/***
		 * 
		 * @TODO	修改button的样式
		 * @param	obj：指定button的jquery对象，为空时整个当前页面的button的样式都会被修改
		 * 
		 */
		modifyButtonStyle : function(obj){
			if(!obj){
				$("input[type='button']").addClass("dqgb_button_default").addClass("dqgb_button_normal")
				.unbind("mouseover").bind("mouseover",function(){
					$(this).addClass("dqgb_button_over");
				}).unbind("mouseout").bind("mouseout",function(){
					$(this).removeClass("dqgb_button_over");
				});
				$("input[type='submit']").addClass("dqgb_button_default").addClass("dqgb_button_normal")
				.unbind("mouseover").bind("mouseover",function(){
					$(this).addClass("dqgb_button_over");
				}).unbind("mouseout").bind("mouseout",function(){
					$(this).removeClass("dqgb_button_over");
				});
			}else{
				obj.addClass("dqgb_button_default").addClass("dqgb_button_normal")
				.unbind("mouseover").bind("mouseover",function(){
					$(this).addClass("dqgb_button_over");
				}).unbind("mouseout").bind("mouseout",function(){
					$(this).removeClass("dqgb_button_over");
				});
			}
		},
		
		/***
		 * 
		 * @TODO	创建一个button 效果的 div(jquery)对象 
		 * 
		 */
		getDivButton : function(options){
			var btnPanel = $("<div class='" + $.dqgb.options.divButtonCls + "'></div>").appendTo($(document.body));		//创建btn面板
			btnPanel.css({
				"float":"left", "overflow":"hidden",
				"width":options.width ? options.width : $.dqgb.options.defaultButtonWidth,
				"height":options.height ? options.height : $.dqgb.options.defaultButtonHeight
			});
			btnPanel.append("<div class='dqgb_divButton dqgb_divButton_left_normal'></div>");
			var leftBorder = btnPanel.children(".dqgb_divButton_left_normal");
			leftBorder.css({
				"float":"left",
				"left":"0px",
				"top":"0px"
			});
			
			btnPanel.append("<div class='bg_repeatX dqgb_divButton_center_normal'></div>");
			var center = btnPanel.children(".dqgb_divButton_center_normal");
			center.css({
				"float":"left", "top":"0px", "text-indent":"0em", "text-align":"center", "width":"1000px", "height":btnPanel.height(),
				"font-size":"14px", "cursor":"pointer", "font-family":"微软雅黑",
				"line-height": (btnPanel.height() - 3) + "px",
				"left":leftBorder.width()
			}).html("<span>" + options.text + "</span>");
	
			if(center.children("span").get(0).offsetWidth > $.dqgb.options.defaultButtonWidth - 2 * leftBorder.width()){
				center.css("width",center.children("span").get(0).offsetWidth + 10);
			}else{
				center.css("width",btnPanel.width() - 2 * leftBorder.width());
			}
			btnPanel.css("width",center.outerWidth(true) + 2 * leftBorder.width());
			
			btnPanel.append("<div class='dqgb_divButton dqgb_divButton_right_normal'></div>");
			var rightBorder = btnPanel.children(".dqgb_divButton_right_normal");
			rightBorder.css({
				"float":"left",
				"right":"0px",
				"top":"0px"
			});
			var timeout = "";
			btnPanel.mouseover(function(){
				clearTimeout(timeout);
				timeout = setTimeout(function(){
					leftBorder.addClass("dqgb_divButton_left_over");
					rightBorder.addClass("dqgb_divButton_right_over");
					center.addClass("dqgb_divButton_center_over");
				},50);
			}).mouseout(function(){									//延迟50ms刷新
				timeout = setTimeout(function(){
					leftBorder.removeClass("dqgb_divButton_left_over");
					rightBorder.removeClass("dqgb_divButton_right_over");
					center.removeClass("dqgb_divButton_center_over");
					if($.browser.mozilla){
						btnPanel.trigger("mouseup");
					}
				},50);
			}).mouseup(function(){
				var ct = $(this).children(".dqgb_divButton_center_normal");
				ct.css({
					"padding-left":"0px",
					"width":ct.outerWidth(true),
					"line-height": (ct.height() - 3) + "px"
				});
				if($.browser.msie){								//firefox不兼容该方法
					this.releaseCapture();
				}else if($.browser.mozilla){
				}
			}).mousedown(function(e){
				var ct = $(this).children(".dqgb_divButton_center_normal");
				ct.css({
					"padding-left":"2px",
					"width":ct.width() - 2,
					"line-height": ct.height() + "px"
				});
				if($.browser.msie){
					this.setCapture();
				}else if($.browser.mozilla){
				}
			}).click(function(){
				if($.isFunction(options.click)){
					options.click.call();
				}
			});
			return btnPanel.shieldRightMenu().shieldSelectStart();
		}
	});
	
	//扩展浮动框
	$.extend($.dqgb.globalMsg,{
		
		msgQueue : [],								//消息框队列
		
		currentMsgBox : "",							//当前正在动画中的消息框
		
		/***
		 * @TODO	获取一个全局消息框
		 */
		generateMsgbox : function(options){
			if($.dqgb.globalMsg.msgQueue.length != 0){
				var first = $.dqgb.globalMsg.msgQueue.shift();
				first.domElement.css({"top":"-80px"});
				return first;
			}
			var msgBox = {};
			$.extend(msgBox,{
				/**
				 * @TODO	dom(jQuery)元素
				 */
				domElement : $("<div></div>").appendTo($(document.body)).css({
					"position":"absolute","z-index":$.dqgb.options.mask_Z_Index + 2,"display":"none","cursor":"pointer",
					"font-family":"微软雅黑","font-size":"14px","padding":"5px","border":"1px solid #246DA0","overflow":"hidden",
					"width" : "140px", "height" : "70px","background-color":"#F9F9F9","right":"12px","top":"-80px","text-align":"left"
				}),
				/***
				 * @TODO	显示函数
				 * @param	top : msgBox的起始位置
				 */
				show : function(options){
					$.dqgb.globalMsg.currentMsgbox = this;
					if(options.top)
						this.domElement.css("top",options.top + "px");
					else{
						options.top = 0;
					}
					this.domElement.html(options.msg);
					this.domElement.animate({"top":(50 + options.top) + "px","opacity":"show"},800);
					this.domElement.delay(1000);
					var top = $.dqgb.getCssValue($.dqgb.globalMsg.currentMsgbox.domElement.css("top")) - $.dqgb.globalMsg.currentMsgbox.domElement.outerHeight(true);
					top = top <= 0 ? 0 : top;
					this.domElement.animate({"top": top + "px","opacity":"hide"},800);
					this.domElement.queue(function(){
						$.dqgb.globalMsg.currentMsgbox = "";
						$.dqgb.globalMsg.msgQueue.push(msgBox);
						msgBox.domElement.dequeue();
					});
					return this;
				},
				/**
				 * @TODO	布局消息框。
				 */
				doLayout : function(){
					return this;
				}
			});
			return msgBox;
		},
		/**
		 * @TODO	显示全局消息
		 */
		showMsg : function(msg){
			if(!msg){
				return;
			}
			if(this.currentMsgbox){
				this.generateMsgbox().show({"top":$.dqgb.getCssValue(this.currentMsgbox.domElement.css("top")) + 
					this.currentMsgbox.domElement.outerHeight(true),msg:msg});
			}else{
				this.generateMsgbox().show({msg:msg});
			}
		},
		
		/**
		 * @TODO	从队列中取出消息框
		 */
		getMsgbox : function(){
			
		}
	});
	
	
	
	/***
	 *	@TODO	注册屏蔽右键插件 
	 */
	$.fn.shieldRightMenu = function(){
		$.dqgb.shieldRightMenu(this);
		return this;
	};
	/***
	 *	@TODO	注册屏蔽onselectstart事件插件 
	 */
	$.fn.shieldSelectStart = function(){
		$.dqgb.shieldSelectStart(this);
		return this;
	};
	
	$.fn.dqgbBox = function(options){
		$.dqgb.shadowLayer.dqgbBox(this,options);
	};
})(jQuery);

