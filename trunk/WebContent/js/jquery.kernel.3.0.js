/***********************************************************************************
 * 
 * @FILE		jquery.kernel.js
 * @AUTHOR		9528
 * @DATE		2011-7-26
 * @COPY-RIGHT	大庆金桥成都分公司
 * 
 * @TODO		1、自动生成页面布局。
 * 				2、创建全局蒙层。支持多层蒙层。
 * 				3、创建panel	。支持panel的拖拽功能。
 * 				4、创建button
 * 				5、创建消息提示框
 * 		
 * 
 ***********************************************************************************/

(function($){
	if(!$.xqb9528){
		$.xqb9528 = {};
	}
	
	$.extend($.xqb9528,{												//创建名字空间
		layout : {},													//创建布局管理器名字空间	
		
		panel : {},														//面板名字空间
		
		button : {},													//button名字空间
		
		globalMsg : {},													//浮动消息框
		
		shadowLayer : {},												//蒙层名字空间
		
		timer : {},
		
		options : {
			apacity : 20
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
		
		/********************
		 * 
		 * window resize以后需要重新布局的对象,这些对象都必须提供一个doLayout函数来进行布局管理
		 *
		 *************/
		layoutObj : [],								
		
		/**
		 * 注册重新布局对象
		 * @param obj
		 */
		registerLayout : function(obj){
			this.layoutObj.push(obj);
		},
		
		/******
		 * 重新布局
		 */
		doLayout : function(){
			for(var i = 0; i < this.layoutObj.length; i++){
				if(!$.isFunction(this.layoutObj[i].doLayout)){
					continue;
				}
				this.layoutObj[i].doLayout();
			}
			$.xqb9528.shadowLayer.layerManager.doLayout();
		}
		
	});
	
	/**
	 * 
	 * (bug：	ie8以及firefox在resize时会触发滚动条的位置,偶尔会出现偏差)
	 * 采用延迟双调整方案(两次调整尺寸)。
	 * 
	 */
	$(window).bind("resize",function(ev){
		clearTimeout($.xqb9528.timer);
		$.xqb9528.timer = setTimeout(function(){			//延迟修正，提高代码执行效率
			$.xqb9528.doLayout();							//修正滚动条bug
			$.xqb9528.timer = setTimeout(function(){			
				$.xqb9528.doLayout();							
			},50);
		},50);
	}).scroll(function(){
		$(this).trigger("resize");
	});
	
	/*********************************
	 * 
	 * 防止由于js对象和dom元素之间的相互引用造成的内存泄漏
	 * 
	 **************************/
	window.onunload = function(){
		$("div").remove();
	};
	
	$.extend($.xqb9528.layout,{
		
		/****
		 * 获取一个border layout 的对象
		 * @returns {___anonymous3051_3052}
		 */
		borderLayout : function(options){
			options = $.extend({},options);
			var layout = {};
			$.extend(layout,{
				options : options,
				
				/***
				 * 生成dom元素
				 * @returns {___anonymous3153_3370}
				 */
				generateDomElement : function(){
					this.north = this.getNorth();									//上面
					this.middle = this.getMiddle();									//左右两边以及分隔符的容器
					this.south = this.getSouth();									//底部
					this.west = this.getWest();										//左边
					this.main = this.getMain();										//右边
					this.separator = this.getSeparator();							//分隔符	
					this.doLayout();
					if(this.options.main && this.options.main.url){
						this.setURL(this.options.main.url);
					}else{
						this.setURL("about:blank");
					}
					return this;
				},
				
				/**
				 * 重新布局
				 */
				doLayout : function(){
					if(!this.getNorth().isDisabled()){
						this.getNorth().doLayout();
					}
					this.getMiddle().doLayout();
					if(!this.getSouth().isDisabled()){
						this.getSouth().doLayout();
					}
					return this;
				},
				
				/**
				 * 获取北部对象
				 * @returns
				 */
				getNorth : function(){
					if(this.north){
						return this.north;
					}
					var north = {};
					$.extend(north,{
						options : $.extend({},layout.options.north),
						
						/***
						 * 生成dom元素
						 */
						generateDomElement : function(){
							$.extend(this,{
								dom : $("<div></div>").appendTo($(document.body)).css({
									'overflow' : 'hidden', 'position' : 'absolute', 'left' : '0px', 'top' : '0px'
								})
							});
							//如果高度为0则disable掉该区域
							if(!this.options.height || 0 == this.options.height){
								this.options.height = 0;
								this.disable();
							}
							return this;
						},
						
						/**
						 * window resize时布局
						 * @returns {___anonymous3713_3878}
						 */
						doLayout : function(){
							var wnd = $(window);
							this.dom.height(this.options.height).width(wnd.width());
							return this;
						},
						
						/**
						 * 获取北部高度
						 */
						getHeight : function(){
							return this.options.height ? this.options.height : 0;
						},
						
						/**
						 * disable北部区域，实际意义就是该部分区域始终 display:none,即没有这一块
						 * @returns {___anonymous3713_4068}
						 */
						disable : function(){
							this.disabled = true;
							return this;
						},
						
						/**
						 * 判断该组件是否disabled
						 * @returns
						 */
						isDisabled : function(){
							return this.disabled ? true : false;
						}
						
					});
					return north.generateDomElement();
				},
				
				/**
				 * 获取中部区域，中部区域不能disable
				 */
				getMiddle : function(){
					if(this.middle){
						return this.middle;
					}
					var middle = {};
					$.extend(middle,{
						options : {},
						
						/***
						 * 生成dom元素
						 */
						generateDomElement : function(){
							$.extend(this,{
								dom : $("<div></div>").appendTo($(document.body)).css({
									'overflow' : 'hidden', 'position' : 'absolute', 'left' : '0px'
								})
							});
							
							return this;
						},
						//布局
						doLayout : function(){
							var wnd = $(window);
							var top = layout.getNorth().getHeight();
							this.dom.width(wnd.width()).height(wnd.height() - top - layout.getSouth().getHeight()).css({
								'top' :  top + 'px'
							});
							/***
							 * 西部以及分隔符 doLayout
							 */
							if(!layout.getWest().isDisabled()){
								layout.getWest().doLayout();
								layout.getSeparator().doLayout();
							}
							/**
							 * main doLayout
							 */
							layout.getMain().doLayout();
							return this;
						},
						/**
						 * 获取高度
						 * @returns
						 */
						getHeight : function(){
							return this.dom.height();
						},
						/**
						 * 获取宽度
						 * @returns
						 */
						getWidth : function(){
							return this.dom.width();
						}
					});
					return  middle.generateDomElement();
				},
				/***
				 * 获取南部区域
				 * @param options
				 */
				getSouth : function(){
					if(this.south){
						return this.south;
					}
					var south = {};
					$.extend(south,{
						options : $.extend({},layout.options.south),
						
						/***
						 * 生成dom元素
						 */
						generateDomElement : function(){
							$.extend(this,{
								dom :  $("<div></div>").appendTo($(document.body)).css({
									'overflow' : 'hidden', 'position' : 'absolute', 'left' : '0px'
								})
							});
							//如果高度为0则disable掉该区域
							if(!this.options.height || 0 == this.options.height){
								this.options.height = 0;
								this.disable();
							}
							return this;
						},
						//布局
						doLayout : function(){
							var wnd = $(window);
							this.dom.height(this.options.height).width(wnd.width()).css({
								'top' : (layout.getNorth().getHeight() + layout.getMiddle().getHeight()) + 'px'
							});
							return this;
						},
						/**
						 * 获取南部高度
						 */
						getHeight : function(){
							return this.options.height ? this.options.height : 0;
						},
						
						/**
						 * disable掉南部区域
						 */
						disable : function(){
							this.disabled = true;
							return this;
						},
						
						/**
						 * 判断该组件是否disabled
						 * @returns
						 */
						isDisabled : function(){
							return this.disabled ? true : false;
						}
						
					});
					return south.generateDomElement();
				},
				/***
				 * 获取西部区域
				 * @param options
				 */
				getWest : function(){
					if(this.west){
						return this.west;
					}
					var west = {};
					$.extend(west,{
						options : $.extend({},layout.options.west),
						
						/***
						 * 生成dom元素
						 */
						generateDomElement : function(){
							$.extend(this,{
								dom : $("<div></div>").appendTo(layout.getMiddle().dom).css({
									'overflow' : 'auto', 'position' : 'absolute', 'left' : '0px', 'top' : '0px' 
								})
							});
							//如果宽度为0则disable掉该区域
							if(!this.options.width || 0 == this.options.width){
								this.options.width = 0;
								this.disable();
							}else{
								this.dom.width(this.options.width);
							}
							return this;
						},
						//布局
						doLayout : function(){
							this.dom.height(layout.getMiddle().getHeight());
							return this;
						},
						/**
						 * 获取西部宽度
						 */
						getWidth : function(){
							return this.options.width ? this.options.width : 0;
						},
						
						/**
						 * disable掉西部区域
						 */
						disable : function(){
							this.disabled = true;
							return this;
						},
						
						/**
						 * 判断该组件是否disabled
						 * @returns
						 */
						isDisabled : function(){
							return this.disabled ? true : false;
						}
						
					});
					return west.generateDomElement();
				},
				/***
				 * 获取主区域
				 * @param options
				 */
				getMain : function(){
					if(this.main){
						return this.main;
					}
					var main = {};
					$.extend(main,{
						options : $.extend({},layout.options.main),
						
						/***
						 * 生成dom元素
						 */
						generateDomElement : function(){
							$.extend(this,{
								dom : $("<div></div>").appendTo(layout.getMiddle().dom).css({
									'overflow' : 'hidden', 'position' : 'absolute', 'right' : '0px', 'top' : '0px' 
								}),
								iframe : $("<iframe frameBorder=0></iframe>").css("background-color","white")
							});
							this.iframe.appendTo(this.dom);
							
							return this;
						},
						//布局
						doLayout : function(){
							if(layout.getWest().isDisabled()){
								this.dom.width(layout.getMiddle().getWidth()).height(layout.getMiddle().getHeight());
							}else{
								this.dom.width(layout.getMiddle().getWidth() - 
										layout.getWest().dom.width() - layout.getSeparator().getWidth()).height(layout.getMiddle().getHeight());
							}
							this.iframe.width(this.dom.width()).height(this.dom.height());
							return this;
						},
						
						/**
						 * 设置iframe的地址
						 * @param url
						 */
						setURL : function(url){
							this.iframe.get(0).src = url;
						}
					});
					return main.generateDomElement();
				},
				//设置url
				setURL : function(url){
					this.getMain().setURL(url);
					return this;
				},
				
				/***
				 * 获取分隔符区域
				 * @param options
				 */
				getSeparator : function(){
					if(this.separator){
						return this.separator;
					}
					var separator = {};
					$.extend(separator,{
						/***
						 * 生成dom元素
						 */
						generateDomElement : function(){
							$.extend(this,{
								dom : $("<div></div>").appendTo(layout.getMiddle().dom).css({
									'overflow' : 'hidden', 'position' : 'absolute', 'left' : '0px', 'top' : '0px' 
								}).addClass("borderLayout_separator")
							});
							if(layout.getWest().isDisabled()){
								this.disable();
								return this;
							}else{
								this.dom.css({"left" : layout.getWest().getWidth() + "px"}).height(layout.getMiddle().getHeight());
							}
							$.extend(this,{
								sepDom : $("<div></div>").appendTo(this.dom).addClass("xqb_borderLayout_sep_left_n")
							});
							
							//分隔符鼠标效果
							this.sepDom.hover(function(){
								if($(this).hasClass("xqb_borderLayout_sep_left_n")){
									$(this).addClass("xqb_borderLayout_sep_left_o").removeClass("xqb_borderLayout_sep_left_n");
								}else if($(this).hasClass("xqb_borderLayout_sep_right_n")){
									$(this).addClass("xqb_borderLayout_sep_right_o").removeClass("xqb_borderLayout_sep_right_n");
								}
							},function(){
								if($(this).hasClass("xqb_borderLayout_sep_left_o")){
									$(this).addClass("xqb_borderLayout_sep_left_n").removeClass("xqb_borderLayout_sep_left_o");
								}else if($(this).hasClass("xqb_borderLayout_sep_right_o")){
									$(this).addClass("xqb_borderLayout_sep_right_n").removeClass("xqb_borderLayout_sep_right_o");
								}
							});
							
							//分隔符鼠标事件
							this.sepDom.click(function(){
								if(parseInt(separator.dom.css("left")) != 0){
									layout.getWest().dom.animate({
										'width' : '0px'
									},300);
									
									separator.dom.animate({
										'left' : '0px'
									},300);
									
									layout.getMain().dom.animate({
										'width' : (layout.getMiddle().getWidth() - separator.getWidth()) + 'px'
									},300);

									layout.getMain().iframe.animate({
										'width' : (layout.getMiddle().getWidth() - separator.getWidth()) + 'px'
									},300);
									
									$(this).addClass("xqb_borderLayout_sep_right_n").removeClass("xqb_borderLayout_sep_right_o xqb_borderLayout_sep_left_o xqb_borderLayout_sep_left_n");
								}else{
									layout.getWest().dom.animate({
										'width' : layout.getWest().getWidth() + 'px'
									},300);
									
									separator.dom.animate({
										'left' : layout.getWest().getWidth() + 'px'
									},300);
									
									layout.getMain().dom.animate({
										'width' : (layout.getMiddle().getWidth() - separator.getWidth() - layout.getWest().getWidth()) + 'px'
									},300);
									
									layout.getMain().iframe.animate({
										'width' : (layout.getMiddle().getWidth() - separator.getWidth() - layout.getWest().getWidth()) + 'px'
									},300);
									
									$(this).addClass("xqb_borderLayout_sep_left_n").removeClass("xqb_borderLayout_sep_right_o xqb_borderLayout_sep_left_o xqb_borderLayout_sep_right_n");
								}
							});
							
							return this;
						},
						/**
						 * 布局
						 */
						doLayout : function(){
							this.dom.height(layout.getMiddle().getHeight());
						},
						/**
						 * 获取分隔符宽度
						 */
						getWidth : function(){
							return this.dom.width();
						},
						/**
						 * disable掉南部区域
						 */
						disable : function(){
							this.disabled = true;
							this.dom.hide();
							return this;
						},
						
						/**
						 * 判断该组件是否disabled
						 * @returns
						 */
						isDisabled : function(){
							return this.disabled ? true : false;
						}
						
					});
					return separator.generateDomElement();
				}
			});
			
			$.xqb9528.registerLayout(layout);	//window resize注册事件
			return layout.generateDomElement();
		}
	});
	
	/****
	 * 扩展面板空间的函数
	 */
	$.extend($.xqb9528.panel,{
		
		/**
		 * 生成一个panel header
		 */
		getPanelHeader : function(options){
			var header = {};
			
			$.extend(header,{
				options : (options ? options : {}),
				mousedown : false,
				created : false,				//尚未创建dom元素
				/***
				 * 重新调整布局
				 */
				doLayout : function(){
					if(this.options.width){
						this.dom.width(this.options.width);
					}
					this.middle.width(this.dom.width() - 8);
					return this;
				},
				/***
				 * 创建dom元素
				 */
				generateDomElement : function(){
					$.extend(this,{
						dom : $("<div></div>").appendTo($(document.body)).css({'overflow':'hidden'}).height(22),
						left : $("<div></div>").appendTo($(document.body)).addClass("xqb_ph_left"),
						middle : $("<div></div>").appendTo($(document.body)).addClass("xqb_ph_middle"),
						right : $("<div></div>").appendTo($(document.body)).addClass("xqb_ph_right"),
						icon : $("<div></div>").appendTo($(document.body)).addClass("xqb_ph_icon").css("float","left"),
						text : $("<div></div>").appendTo($(document.body)).addClass("xqb_ph_text"),						
						close : $("<div></div>").appendTo($(document.body)).css({
							"float":"right","margin":"3px 0px","_margin":"3px 0px",
							"width":"16px","height":"16px","cursor":"pointer"
						}).attr("title","关闭")						
					});
					this.left.appendTo(this.dom);
					this.middle.appendTo(this.dom);
					this.right.appendTo(this.dom);
					
					//icon text 以及 close 按钮全部放在middle区域
					this.icon.appendTo(this.middle);
					this.text.appendTo(this.middle).shieldSelectStart();
					this.close.appendTo(this.middle);
					
					this.setTitle(this.options.title == null ? "" : this.options.title);
					
					/**
					 * icon的显示控制
					 */
					if(this.options.iconCls){
						this.icon.addClass(this.options.iconCls).show();
					}else{
						this.icon.hide();
					}
					
					/**
					 * close 按钮的控制
					 */
					if(this.options.closeBtn){
						this.close.addClass("xqb_ph_close_n").show().hover(function(){
							$(this).addClass("xqb_ph_close_o").removeClass("xqb_ph_close_n");
						},function(){
							$(this).addClass("xqb_ph_close_n").removeClass("xqb_ph_close_o");
						});
						if(this.options.closeBtn.click && $.isFunction(this.options.closeBtn.click)){
							this.close.unbind("click").click(function(){
								header.options.closeBtn.click.call(this,header);
							});
						}
					}else{
						this.close.hide();
					}
					
					/**
					 * dragable，分为ie和firefox分别处理
					 */
					if(this.options.dragable){
						header.dom.addClass("xqb_ph_dragable");
					}
					this.dom.unbind("mousedown").mousedown(function(e){
						if(!header.isDragable()){
							return true;
						}
						if($.browser.mozilla){
							header.registerDocMouseMove();
						}
						if(header.getOwner().isLoading()){
							header.getOwner().transparentLayer.show();
						}
						if($.browser.msie){
							header.dom.get(0).setCapture();
						}
						header.mousedown = true;
						header.orgX = e.clientX;
						header.orgY = e.clientY;
						$(document).data("header",header);
					}).unbind("mouseup").mouseup(function(e){
						if(!header.isDragable()){
							return true;
						}
						if($.browser.mozilla){
							header.detachDocMouseMove();
						}
						header.mousedown = false;
						if($.browser.msie){
							header.dom.get(0).releaseCapture();
						}
						if(header.getOwner().isLoading()){
							header.getOwner().transparentLayer.hide();
						}
					}).unbind("mousemove").mousemove(function(ev){
						if(!header.isDragable()){
							return true;
						}
						if(header.isValidMove()){
							header.getOwner().setOffset(ev.clientX - header.orgX,ev.clientY - header.orgY);
							header.orgX = ev.clientX;
							header.orgY = ev.clientY;
						}
						return false;
					});
					this.created = true;
					return this;
				},
				/**
				 * 注册document的mouse move事件，firefox才注册该事件
				 */
				registerDocMouseMove : function(){
					$(document).mousemove(function(ev){
						var hd = $(this).data("header");
						if(!hd){
							return true;
						}
						if(!hd.isDragable()){
							return true;
						}
						if(hd.isValidMove()){
							hd.getOwner().setOffset(ev.clientX - hd.orgX,ev.clientY - hd.orgY);
							hd.orgX = ev.clientX;
							hd.orgY = ev.clientY;
						}
						return true;
					}).mouseup(function(){
						var hd = $(this).data("header");
						if(!hd){
							return true;
						}
						hd.detachDocMouseMove();
						if(hd.getOwner().isLoading()){
							header.getOwner().transparentLayer.hide();
						}
						if(!hd.isDragable()){
							return true;
						}
						hd.mousedown = false;
					});
				},
				/**
				 * 卸载document上的事件
				 */
				detachDocMouseMove : function(){
					$(document).unbind("mousemove").unbind("mouseup");
				},
				/**
				 * 判断当前鼠标移动是否是合法移动，如果是的话则移动header 所在的body
				 * @returns {Boolean}
				 */
				isValidMove : function(){
					return this.mousedown;
				},
				
				/**
				 * 设置文本
				 */
				setTitle : function(text){
					this.text.html(text);
					return this;
				},
				
				/**
				 * 可关闭的,必须在dom元素已经创建以后调用才有效
				 * @param enable
				 */
				closable : function(enable){
					if(this.isCreated()){
						if(enable){
							this.close.show();
						}else{
							this.close.hide();
						}
						return this;
					}
					return this;
				},
				
				/**
				 * 设置icon的样式
				 * @param iconCls
				 */
				setIcon : function(iconCls){
					if(this.options.iconCls){
						this.icon.removeClass(this.options.iconCls);
					}
					this.options.iconCls = iconCls;
					this.icon.addClass(this.options.iconCls).show();
					return this;
				},
				
				/** 
				 * 显示header的dom元素,如果dom元素还没有创建，则先创建dom元素
				 */
				show : function(){
					if(!this.isCreated()){
						this.generateDomElement();
					}
					this.doLayout();
					return this;
				},
				/**
				 * 设置是否允许dragable
				 * @param enable
				 */
				dragable : function(enable){
					this.options.dragable = enable;
					if(enable && this.isCreated()){
						this.dom.addClass("xqb_ph_dragable");
					}else{
						this.dom.removeClass("xqb_ph_dragable");
					}
					return this;
				},
				/**
				 * 判断是否允许dragable
				 * @returns
				 */
				isDragable : function(){
					return this.options.dragable;
				},
				
				/**
				 * hide操作
				 */
				hide : function(){
					this.dom.hide();
				},
				
				/**
				 * 设置header的宽度
				 * @param wd
				 */
				setWidth : function(wd){
					this.options.width = wd;
					if(this.created){
						this.doLayout();
					}
					return this;
				},
				
				/**
				 * 判断header的dom元素是否已经创建
				 * @returns {Boolean}
				 */
				isCreated : function(){
					return this.created;
				},
				
				/**
				 * 设置header的parent对象
				 * @param owner
				 */
				setOwner : function(owner){
					this.owner = owner;
				},
				/**
				 * 获取header的parent对象
				 * @returns
				 */
				getOwner : function(){
					return this.owner;
				}
			});
			return header;
		}
		
		
	});
	/**
	 * 扩展蒙层
	 */
	$.extend($.xqb9528.shadowLayer,{
		
		options : {
			defaultZIndex : 3000
		},
		
		/**
		 * 蒙层管理器
		 */
		layerManager : {
			hideQueue : [],					//当前已经创建好并且处于hide(display:none)状态的蒙层组成的队列，
			showQueue : [],					//当前正处于show(display:none)状态的蒙层组成的队列
			defaultZIndex : 0,				//蒙层管理器中蒙层的默认高度
			
			/**
			 * 将蒙层对象层show队列剔除，放回hide队列,并且重置蒙层的z-index
			 * @param el
			 */
			hideElement : function(el){
				this.hideQueue.unshift(el);
				for(var i = 0; i < this.showQueue.length; i++){
					if(el == this.showQueue[i]){
						this.showQueue.splice(i, 1);
						break;
					}
				}
				el.setzIndex(this.defaultZIndex);
			},
			/**
			 * 将蒙层添加到show队列
			 * @param el
			 */
			showElement : function(el){
				var zIndex = this.getMaxIndex();
				this.showQueue.unshift(el);
				el.setzIndex(zIndex + 2);
			},
			
			/**
			 * 获取show队列中 zIndex 最高的蒙层的zIndex
			 */
			getMaxIndex : function(){
				var zIndex = this.defaultZIndex;
				if(this.showQueue.length == 0){
					return zIndex;
				}
				for(var i = 0; i < this.showQueue.length; i++){
					if(this.showQueue[i].zIndex > zIndex){
						zIndex = this.showQueue[i].zIndex;
					}
				}
				return zIndex;
			},
			
			
			/**
			 * 从蒙层管理器中获取一个蒙层,通过这种方式获取的蒙层将会被管理器管理
			 */
			getManagedLayer : function(){
				if(this.hideQueue.length != 0){
					return this.hideQueue.pop();
				}
				var layer = $.xqb9528.shadowLayer.getLayer();
				this.defaultZIndex = layer.zIndex;
				/**
				 * 托管layer
				 */
				layer.acceptManage();				//标识蒙层接受管理
				return layer;
			},
			
			/**
			 * 对显示的layer进行重新布局
			 */
			doLayout : function(){
				for(var i = 0; i < this.showQueue.length; i++){
					this.showQueue[i].doLayout();
				}
			}
			
		},
		
		/**
		 * 获取一个蒙层对象,默认z-index 为3000
		 * @returns {___anonymous6091_6092}
		 */
		getLayer : function(){
			var layer = {};
			$.extend(layer,{
				
				zIndex : $.xqb9528.shadowLayer.options.defaultZIndex,
				
				/**
				 * 生成dom元素
				 */		
				generateDomElement : function(){
					$.extend(layer,{
						dom : $("<div></div>").css({"margin":"0px","padding":"0px",				//蒙层							
							"left":"0px","top":"0px","background-color":"black",
							"filter":"alpha(opacity=" + $.xqb9528.options.apacity + ")", 
							"-moz-opacity":"0." + $.xqb9528.options.apacity,
							"display":"none", "opacity":"0." + $.xqb9528.options.apacity
						}).appendTo($(document.body)).shieldRightMenu().shieldSelectStart()
					});
					
					//如果接受管理则采用绝对定位
					if(this.isManaged()){
						this.dom.addClass("absolute");
					}
					if($.browser.msie && $.browser.version == "6.0"){
						$("<iframe></iframe>").appendTo(layer.dom).css({
							"position":"absolute", "left":"0px", "background-color":"black",
							"filter":"alpha(opacity=" + 0 + ")", "-moz-opacity":"0.00",
							"opacity":"0.00" , "top":"0px"
						});
					}
					this.created = true;
					return this;
				},
				
				/**
				 * 设置zIndex
				 * @param zIndex
				 */
				setzIndex : function(zIndex){
					this.zIndex = zIndex;
					this.dom.css("z-index",this.zIndex);
					if(this.element){
						this.element.setzIndex(this.zIndex + 1);
					}
					return this;
				},
				
				/**
				 * 设置蒙层接受蒙层管理器的管理
				 */
				acceptManage : function(){
					this.manage = true;
					return this;
				},
				
				/**
				 * 判断蒙层是否接受管理
				 */
				isManaged : function(){
					return this.manage;
				},
				
				/**
				 * window尺寸变化时调用，调整layer尺寸。对于组件内部使用的layer 不要使用该方法
				 */
				doLayout : function(){
					var wnd = $(window);
					this.dom.css({
						"width" : wnd.scrollLeft() + wnd.width(),
						"height" : wnd.scrollTop() + wnd.height()
					});
					if($.browser.msie && $.browser.version == "6.0"){
						this.dom.children("iframe").width(this.dom.width()).height(this.dom.height());
					}
					return this;
				},
				
				/**
				 * 显示dom元素
				 */
				show : function(){
					if(!this.created){
						this.generateDomElement();
					}
					this.dom.show();
					this.doLayout();
					this.setzIndex(this.zIndex);
					if(this.isManaged()){
						$.xqb9528.shadowLayer.layerManager.showElement(this);
					}
					return this;
				},

				/**
				 * 隐藏蒙层dom元素
				 */
				hide : function(){
					if(!this.created){
						return this;
					}
					this.dom.hide();
					if(this.isManaged()){
						$.xqb9528.shadowLayer.layerManager.hideElement(this);
					}
					return this;
				},
				
				/**
				 * 设置蒙层显示时需要显示的元素
				 * @param el
				 */
				setElement : function(el){
					this.element = el;
					return this;
				},
				
				/**
				 * 获取蒙层显示时需要显示的元素
				 * @returns
				 */
				getElement : function(){
					return this.element;
				}
			});
			
			return layer;
		},
		
		/**
		 * 窗口管理器。window 采用非模态形式的时候管理window的层高切换以及Z-index的范围控制在2000 - 3000之间
		 */
		windowManager : {
			windowQueue : [],														//窗口队列
			
			minZIndex : 2000,				//最小z-index
			
			currentZIndex : 2000,			//当前最高的层高	
			
			maxZIndex : 2000,				//最大z-index
			
			/**
			 * 添加接受管理的window
			 * @param el
			 */
			addElement : function(el){
				this.maxZIndex = $.xqb9528.shadowLayer.options.defaultZIndex;
				this.windowQueue.push(el);
			},
			
			/**
			 * 脱离窗口管理器的管理
			 * @param el
			 */
			removeElement : function(el){
				for(var i = 0; i < this.windowQueue.length; i++){
					if(el == this.windowQueue[i]){
						this.windowQueue.splice(i, 1);
						break;
					}
				}
			},
			
			/**
			 * 调整el的层高
			 * @param el
			 */
			doLayout : function(el){
				if(this.currentZIndex < this.maxZIndex){
					if(this.currentZIndex == el.getzIndex()){
						return;
					}
					this.currentZIndex++;
					el.setzIndex(this.currentZIndex);
				}else{
					if(this.currentZIndex == el.getzIndex()){
						return;
					}
					this.currentZIndex++;
					el.setzIndex(this.currentZIndex);
					this.resetZIndex();
				}
			},
			
			/**
			 * 重新调整现有层高数据, 快速排序法
			 */
			resetZIndex : function(){
				for(var i = 0; i < this.windowQueue.length; i++){
					for(var j = i + 1; j < this.windowQueue.length; j++){
						if(this.windowQueue[i].getzIndex() > this.windowQueue[j].getzIndex()){
							var temp = this.windowQueue[i];
							this.windowQueue[i] = this.windowQueue[j];
							this.windowQueue[j] = temp;
						}
					}
				}
				this.currentZIndex = this.minZIndex;
				for(var i = 0; i < this.windowQueue.length; i++){
					this.windowQueue[i].setzIndex(this.currentZIndex++);
				}
			}
		},
		
		/**
		 * 封装dom元素成为一个window对象
		 * @param options
		 * @returns {___anonymous12540_12541}
		 */
		xqbBox : function(options){
			var box = options.body.data("xqbBox");
			if(box){
				box.options = options;
				return box;
			}
			var box = {};
			
			$.extend(box,{
				options : options,
				
				/**		非模态的window默认接受窗口管理器的管理	**/
				acceptManage : true,
				/***
				 * 生成dom元素
				 */
				generateDomElement : function(){
					$.extend(this,{
						dom : $("<div style='position:absolute;'></div>").appendTo($(document.body)).css({
									'margin':'0px','padding':'0px','overflow':'hidden'
								}).width(this.options.width).height(this.options.height).shieldRightMenu(),
						header : $.xqb9528.panel.getPanelHeader({
									title : this.options.title,
									iconCls : this.options.iconCls,
									dragable : this.options.dragable,
									closeBtn : this.options.closeBtn ? {
										click : function(header){
											header.getOwner().hide();
											//回调 afterClose
											if(box.options.closeBtn && box.options.closeBtn.afterClose){
												if($.isFunction(box.options.closeBtn.afterClose)){
													box.options.closeBtn.afterClose.call(this,box);
												}
											}
										}
									} : false
								}),
						frame : $("<div></div>").appendTo($(document.body)).addClass("xqb_defaultBorder")
								.addClass("xqb_defaultBgColor").css({
									'margin':'0px','padding':'0px','overflow':'hidden'
								}).width(this.options.width - 2).height(this.options.height - 24),		
						body : this.options.body
						
						
					});
					
					//grid的body overflow时 只能hidden
					if(this.body.css("overflow") != "hidden"){
						this.body.css({"overflow":"auto"});
					}
					
					this.header.setWidth(this.options.width).setOwner(this);
					this.header.show();
					this.header.dom.appendTo(this.dom);
					this.frame.appendTo(this.dom);
					var borderWidth = 0;
					if(this.options.innerBorder){
						borderWidth = 2;
						this.body.addClass("xqb_defaultInnerBorderColor");
					}
					if(!this.body.css("background-color") || this.body.css("background-color") == "transparent"){
						this.body.css("background-color","white");
					}
					
					if(!this.options.buttons || this.isLoading()){
						this.body.css({"margin":'6px'}).appendTo(this.frame)
							.width(this.options.width - borderWidth - 14 - parseInt(this.body.css("padding-left")) - parseInt(this.body.css("padding-right")))
							.height(this.options.height - borderWidth - 22 - 14 -  parseInt(this.body.css("padding-top")) - parseInt(this.body.css("padding-bottom")));
					}else{
						this.body.css({"margin":'6px'}).appendTo(this.frame)
							.width(this.options.width - borderWidth - 14 - parseInt(this.body.css("padding-left")) - parseInt(this.body.css("padding-right")))
							.height(this.options.height - borderWidth - 22 - 14 - 34 -  parseInt(this.body.css("padding-top")) - parseInt(this.body.css("padding-bottom")));
						//如果有button则创建button
						
						var center = $("<div></div>").appendTo(this.frame).css({'padding-left' : '4px', 'padding-right' : '4px'});
						var table = $("<table style='margin:0px auto; overflow:hidden;' border='0' cellspacing='0' cellpadding='0' height='34'>" + "</table>");
						
						if(this.options.buttons.buttonAlign){
							var align = this.options.buttons.buttonAlign;
							if(align == "center"){
								
							}else if(align == "left" || align == "right"){
								table.css({
									'margin' : '0px',
									'float' : align
								});
							}
						}
						
						table.appendTo(center);
						var row = $.xqb9528.addRow(table.get(0));
						$(row).height(2);
						
						row = $.xqb9528.addRow(table.get(0));
						$(row).height(22);
						for(var i = 0; i < this.options.buttons.items.length; i++){
							var col = $.xqb9528.addColumn(row);
							var button = $.xqb9528.button.getXqbButton(this.options.buttons.items[i]).show();
							
							$(col).width(button.dom.width()).append(button.dom);
							if(i != this.options.buttons.items.length - 1){
								var col = $.xqb9528.addColumn(row);
								$(col).width(5);
							}
						}
						row = $.xqb9528.addRow(table.get(0));
						$(row).height(10);
					}
					this.dom.width(this.options.width).height(this.options.height);
					if(!this.options.model){
						if(this.isAcceptManage()){
							this.acceptWindowManage(true);
							this.dom.mousedown(function(){
								if(box.isAcceptManage()){
									$.xqb9528.shadowLayer.windowManager.doLayout(box);
								}
							});
						}
					}
					
					//添加透明蒙层
					if(this.isLoading()){
						$.extend(this,{
							transparentLayer : $("<div class='transparent relative'></div>").appendTo($(document.body)).css({
								'margin-top' : (-this.frame.outerHeight(true)) + "px"
							})
						});
						this.transparentLayer.appendTo(this.dom);
						this.transparentLayer.width(this.frame.outerWidth(true));
						this.transparentLayer.height(this.frame.outerHeight(true));
						this.transparentLayer.click(function(){
							$(this).hide();
						}).trigger("click");
					}
					
					
					this.created = true;
					return this;
				},
				/**
				 * 布局，每次show之后都调用了该方法
				 * @returns {___anonymous14943_22382}
				 */
				doLayout : function(){
					if(!this.created){
						return this;
					}
					/***
					 * 触发布局前置事件
					 */
					if(this.options.beforeLayout && $.isFunction(this.options.beforeLayout)){
						this.options.beforeLayout.call(this,this);
					}
					
					this.header.setWidth(this.options.width);
					this.frame.width(this.options.width - 2).height(this.options.height - 24);
					var borderWidth = 0;
					if(this.options.innerBorder){
						borderWidth = 2;
					}
					/**
					 * loading 对象由于是复用的，所以不允许定制button
					 */
					if(!this.options.buttons || this.isLoading()){
						this.body.width(this.options.width - borderWidth - 14 - parseInt(this.body.css("padding-left")) - parseInt(this.body.css("padding-right")))
							.height(this.options.height - borderWidth - 22 - 14 -  parseInt(this.body.css("padding-top")) - parseInt(this.body.css("padding-bottom")));
					}else{
						this.body.css({"margin":'6px'})
							.width(this.options.width - borderWidth - 14 - parseInt(this.body.css("padding-left")) - parseInt(this.body.css("padding-right")))
							.height(this.options.height - borderWidth - 22 - 14 - 34 -  parseInt(this.body.css("padding-top")) - parseInt(this.body.css("padding-bottom")));
					}
					this.dom.width(this.options.width).height(this.options.height);
					
					/***
					 * 触发布局后置事件,		grid组件专用事件
					 */
					if(this.options.afterLayout && $.isFunction(this.options.afterLayout)){
						this.options.afterLayout.call(this,this);
					}
					return this;
				},
				
				/**
				 * 设置title
				 */
				setTitle : function(t){
					this.header.setTitle(t);
					return this;
				},
				
				/**
				 * 设置window的size,下次show的时候才会起效
				 * @param size
				 */
				setSize : function(size){
					this.options.width = size.width;
					this.options.height = size.height;
					return this;
				},
				
				/***
				 * 接受窗口管理器的管理机制
				 */
				acceptWindowManage : function(accept){
					if(accept){
						this.acceptManage = true;
						$.xqb9528.shadowLayer.windowManager.addElement(this);
					}else{
						this.acceptManage = false;
						$.xqb9528.shadowLayer.windowManager.removeElement(this);
					}
					return this;
				},
				
				/**
				 * 判断窗口是否接受窗口管理器的管理
				 * @returns {Boolean}
				 */
				isAcceptManage : function(){
					return this.acceptManage;
				},
				
				/**
				 * 判断是不是loading页面
				 * @returns
				 */
				isLoading : function(){
					return this.options.loading ? true : false;
				},
				
				/***
				 * 显示box
				 */
				show : function(){
					if(this.options.beforeShow && $.isFunction(this.options.beforeShow)){
						this.options.beforeShow.call(this,this);
					}
					if(this.dom && this.dom.css("display") == "block"){
						return this;
					}
					if(!this.created){
						this.generateDomElement();
					}
					if(this.options.model){
						this.getLayer().show();
					}
					var left = this.endPos ? this.endPos.x : 0;
					var top = this.endPos ? this.endPos.y : 0;
					this.dom.show().css({'left' : left + 'px', 'top' :  top + 'px'});
					this.body.show();
					this.doLayout();
					if(this.options.afterShow && $.isFunction(this.options.afterShow)){
						this.options.afterShow.call(this,this);
					}
					this.showMode = '';
					return this;
				},
				
				/**
				 * 居中显示
				 */
				showCenter : function(){
					if(this.options.dragable){
						return this.center();
					}
					var wnd = $(window);
					this.show().dom.css({'left' : '50%', 'top' : '50%', 'margin-left' : (wnd.scrollLeft() - this.options.width / 2) + 'px',
						'margin-top' : (wnd.scrollTop() - this.options.height / 2) + 'px'});
					return this;
				},
				/**
				 * 居中显示，采用像素坐标定位
				 */
				center : function(){
					var wnd = $(window);
					this.show().dom.css({'left' : ((wnd.width() - this.options.width) / 2 + wnd.scrollLeft()) + 'px',
						'top' : ((wnd.height() - this.options.height) / 2  + wnd.scrollTop()) + 'px'});
					return this;
				},
				
				/**
				 * 动画的方式显示
				 * @param begin		起始位置,如果没有输入初始值则默认居中
				 */
				animateShow : function(begin,end){
					if(this.dom && this.dom.css("display") == "block"){
						return this;
					}
					this.show();
					var wnd = $(window);
					this.showMode = 'animate';					//标识show 方式是 animate
					var x = begin ? begin.x : (this.beginPos ? this.beginPos.x : 0);
					var y = begin ? begin.y : (this.beginPos ? this.beginPos.y : 0);
					this.beginPos = {x : x, y : y};
					var left = end ? end.x : (this.endPos ? this.endPos.x : ((wnd.width() - this.options.width) / 2));
					var top = end ? end.y : (this.endPos ? this.endPos.y : ((wnd.height() - this.options.height) / 2));
					this.endPos = {x : left, y : top};
					this.dom.hide().css({'left' : x + 'px','top' : y + 'px','width':'0px','height':'0px'});
					this.dom.animate({
						opacity : 'show',
						'left' : left + 'px',
						'top' : top + 'px',
						'width' : this.options.width,
						'height' : this.options.height
					},300);
					
					//非模态的且接受管理的 window拉到最上面
					if(!this.options.model && this.isAcceptManage())
						$.xqb9528.shadowLayer.windowManager.doLayout(this);
					return this;
				},
				
				/**
				 * 隐藏box
				 */
				hide : function(){
					if(!this.created){
						return this;
					}
					if(this.dom && this.dom.css("display") == "none"){
						return this;
					}
					this.endPos = {x : parseInt(this.dom.css("left")), y : parseInt(this.dom.css("top"))};
					if(this.showMode == "animate"){
						this.dom.animate({
							opacity : 'hide',
							'left' : this.beginPos.x + 'px',
							'top' : this.beginPos.y + 'px',
							'width' : '0px',
							'height' : '0px'
						},300,function(){
							if(box.options.model){
								box.layer.hide();
							}
						});
					}else{
						this.dom.hide();
						if(this.options.model){
							this.layer.hide();
						}
					}
					/**
					 * 如果是loading元素 则归还给loading管理器
					 */
					if(this.options.loading){
						$.xqb9528.shadowLayer.loadingManager.hide(this);
					}
					
					this.header.mousedown = false;
					return this;
				},
				
				/**
				 * 获取蒙层
				 * @returns
				 */
				getLayer : function(){
					if(!this.options.model){
						return null;
					}
					var layer = $.xqb9528.shadowLayer.layerManager.getManagedLayer();
					layer.setElement(this);
					this.layer = layer;
					return layer;
				},
				
				/**
				 * 获取window 的position
				 * @returns {___anonymous14619_14688}
				 */
				getPosition : function(){
					return {x : parseInt(this.dom.css("left")),y : parseInt(this.dom.css("top"))};
				},
				
				/**
				 * 调整window 的位置
				 * @param x
				 * @param y
				 */
				setPosition : function(x,y){
					this.dom.css({"left" : x + "px","top" : y + 'px'});
					return this;
				},
				
				/***
				 * 设置父窗口的zIndex
				 * @param zIndex
				 */
				setzIndex : function(zIndex){
					this.dom.css("z-index",zIndex);
					return this;
				},
				/**
				 * 获取层高
				 */
				getzIndex : function(){
					return parseInt(this.dom.css("z-index"));
				},
				/**
				 * 拖拽时的offset
				 * @param x
				 * @param y
				 */
				setOffset : function(x ,y){
					var wnd = $(window);
					var posX = parseInt(this.dom.css("left")) + x;
					var posY = parseInt(this.dom.css("top")) + y;
					var left = wnd.scrollLeft();
					var top = wnd.scrollTop();
					posX = posX > left ? posX : left;
					posY = posY > top ? posY : top;
					var maxX = left + wnd.width() - this.dom.width();
					var maxY = top + wnd.height() - this.dom.height();
					posX = posX > maxX ? maxX : posX;
					posY = posY > maxY ? maxY : posY;
					this.dom.css({'margin' : '0px','left' : posX + 'px', 'top' : posY + 'px'});
				},
				/**
				 * 判断当前的mousemove操作是否是合法操作，即是否应该触发窗口移动操作
				 * @returns
				 */
				isValidMove : function(){
					return this.header.isValidMove();
				}
			});
			
			box.options.body.data("xqbBox",box);
			return box;
		},
		
		/**
		 * 封装mask
		 * @param mask
		 */
		mask : function(text){
			var mask = {};
			$.extend(mask,{
				content : text,
				
				/**
				 * 生成dom
				 */
				generateDomElement : function(){
					$.extend(this,{
						dom : $("<div class='xqb_loading'></div>").appendTo($(document.body)).css({
									'overflow':'hidden','border' : '1px solid #6593CF','position' : 'absolute',
									'margin':'0px','padding':'0px','left' : '0px', 'top' : '0px'
								}),
						innerFrame : $("<div></div>").appendTo($(document.body)).css({
									'margin':'2px','padding':'0px','border':'1px solid #A3BAD9','background-color':'#FBFBFB','height':'25px'
								}),	
						icon : $("<div class='xqb_loading_icon'></div>").appendTo($(document.body)).css({
									'margin':'3px 3px', '_margin':'3px 1px','overflow':'hidden'
								}),
						text : $("<div></div>").appendTo($(document.body)).css({
									'line-height' : '20px', 'font-size' : '13px', 'margin' : '2px 4px',
									'float' : 'left', 'height' : '20px','white-space':'nowrap'
								}).addClass("xqb_default_font")
					});
					
					this.text.html(this.content);
					this.innerFrame.width(this.icon.outerWidth(true) + this.text.outerWidth(true) + 4);
					this.icon.appendTo(this.innerFrame);
					this.text.appendTo(this.innerFrame);
					
					this.dom.width(this.innerFrame.outerWidth(true));
					this.innerFrame.appendTo(this.dom);
					
					this.created = true;
					return this;
				},
				
				
				/**
				 * 变更显示内容
				 * @param text
				 * @returns
				 */
				setMask : function(text){
					this.content = text;
					if(this.created && this.dom.css("display") == "block"){
						return this.doLayout();
					}
					return this;
				},
				
				/**
				 * 重新布局
				 */
				doLayout : function(){
					this.dom.show();
					this.innerFrame.width(1000);
					this.dom.width(1010);
					this.text.html(this.content);
					this.innerFrame.width(this.icon.outerWidth(true) + this.text.outerWidth(true) + 4);
					this.dom.width(this.innerFrame.outerWidth(true));
					this.dom.css({'left' : '50%', 'top' : '50%', 'margin-left' : (- this.dom.outerWidth() / 2) + 'px',
						'margin-top' : (- this.dom.outerHeight() / 2) + 'px'});
					return this;
				},
				/**
				 * show,始终摆放在正中
				 */
				show : function(){
					if(this.dom && this.dom.css("display") == "block"){
						return this;
					}
					if(!this.created){
						this.generateDomElement();
					}
					if(this.model){
						this.getLayer().show();
					}
					this.doLayout();
					return this;
				},
				
				/**
				 * hide mask
				 */
				hide : function(){
					if(!this.created){
						return this;
					}
					this.dom.hide();
					if(this.model){
						this.layer.hide();
					}
					//归还mask对象到 mask管理器中
					$.xqb9528.shadowLayer.maskManager.hide(this);
					return this;
				},
				
				/***
				 * 设置父窗口的zIndex
				 * @param zIndex
				 */
				setzIndex : function(zIndex){
					this.dom.css("z-index",zIndex);
					return this;
				},
				
				/**
				 * 设置模态
				 * @param model
				 */
				setModel : function(model){
					this.model = model;
				},
				/**
				 * 获取蒙层
				 * @returns
				 */
				getLayer : function(){
					if(!this.model){
						return null;
					}
					var layer = $.xqb9528.shadowLayer.layerManager.getManagedLayer();
					layer.setElement(this);
					this.layer = layer;
					return layer;
				}
			});
			return mask;
		},
		
		/*****
		 *	mask管理器，负责管理创建好的mask对象 
		 */
		maskManager : {
			maskQueue : [], 				//存储已经创建好的mask
			
			/**
			 * 获取一个mask
			 */
			getMask : function(){
				if(this.maskQueue.length){
					return this.maskQueue.pop();
				}
				return $.xqb9528.shadowLayer.mask();
			},
			/**
			 * 回放mask到mask队列
			 * @param mask
			 */
			hide : function(mask){
				this.maskQueue.unshift(mask);
			}
		},
		/*****
		 * 
		 * 显示mask(对外API)
		 * @param text
		 */
		showMask : function(text){
			var mask = this.maskManager.getMask();
			mask.setModel(true);
			return mask.setMask(text).show();
		},
		
		/*****
		 *  loading对象管理器
		 */
		loadingManager : {
			loadingQueue : [],
			
			/**
			 * 从loading对象管理器中获取一个loading元素
			 * @param options
			 * @returns
			 */
			getLoading: function(options){
				if(this.loadingQueue.length){
					return this.loadingQueue.pop().setOption(options);
				}
				return $.xqb9528.shadowLayer.getLoading(options);
			},
			/***
			 * 归还loading
			 * @param loading
			 */
			hide : function(loading){
				this.loadingQueue.push(loading);
			}
			
		},
		/****************
		 * 
		 * 加载远程页面,生成一个loading 对象
		 * @param options
		 */
		getLoading : function(options){
			var iframe = $("<iframe frameBorder=0></iframe>");
			$.extend(options,{
				body : iframe,					//指定body
				innerBorder : true,
				afterShow : function(loadingObj){
					loadingObj.setURL(this.options.url);
				},
				loading : true					//标识该窗口是loading元素
			});
			var loading = $.xqb9528.shadowLayer.xqbBox(options);
			$.extend(loading,{
				options : options,
				
				/**
				 * 设置配置参数,该函数是在dom元素已经创建以后才调用，不推荐用户直接使用
				 * @param option
				 * @returns {___anonymous26850_26994}
				 */
				setOption : function(option){
					$.extend(this.options,option);
					
					this.setSize({width:option.width,height:option.height});
					this.header.setIcon(option.iconCls);
					this.header.setTitle(option.title);
					this.header.dragable(option.dragable ? true : false);
					this.header.closable(option.closeBtn ? true : false);
					this.options.closeBtn = option.closeBtn;
					this.options.model = option.model;
					if(option.mode){
						this.acceptWindowManage(true);					//接管
					}else{
						this.acceptWindowManage(false);					//脱管
					}
					return this;
				},
				
				/**
				 * 修改loading的地址
				 * @param url
				 */
				setURL : function(url){
					this.options.url = url;
					this.body.get(0).src = url;
				}
			});
			
			return loading;
		},
		
		/***
		 * 显示loading(对外API)
		 * @param options
		 * @returns
		 */
		loading : function(options){
			return this.loadingManager.getLoading(options);
		},
		
		/**
		 * 生成一个confirm对象,全局只有一个confirm对象,confirm 是模态对话框，没有close button。不可拖拽
		 */
		getConfirm : function(){
			if(this.confirmBox){
				return this.confirmBox;
			}else{
				var confirm = $.xqb9528.shadowLayer.xqbBox({
					dragable : false,
					model : true,
					confirm : true,									//标识是confirm框
					closeBtn : false,
					innerBorder : false,
					closeBtn : true,
					buttons : {
						items : [
								    {
								    	text : '确认',
								    	width : 50,
								    	click : function(){
								    		confirm.hide();
								    		if(confirm.options.afterConfirm){
								    			if(confirm.options.afterConfirm  && $.isFunction(confirm.options.afterConfirm)){
								    				confirm.options.afterConfirm.call();
												}
								    		}
								    	}
								    },
								    {
								    	text : '取消',
								    	width : 50,
								    	click : function(){
								    		confirm.hide();
								    	}
								    }
								]
					},
					width : 225,
					height : 125,
					body : $("<div></div>").appendTo($(document.body)).css({
						"text-indent" : "2em", "font-size" : "14px", "cursor" : "default"
					}).addClass("xqb_defaultBgColor xqb_default_font")
				});
				
				$.extend(confirm,{
					/**
					 * 设置内容
					 */
					setContent : function(content){
						this.options.content = content;
						this.body.html(this.options.content);
						return this;
					}
				});
				
				
				this.confirmBox = confirm;
				return this.confirmBox;
			}
		},
		/**
		 * 确认
		 * @param options
		 */
		confirm : function(options){
			var c = this.getConfirm().showCenter();
			$.extend(c.options,options);
			c.header.setTitle(options.title ? options.title : '提示');
			c.header.setIcon(options.iconCls ? options.iconCls : 'xqb_confirm_default_icon');
			c.setContent(options.content);
			return c;
		}
	});
	
	/***
	 * 扩展button
	 */
	$.extend($.xqb9528.button,{
		/**
		 * 修改默认的button风格
		 * @param obj
		 */
		modifyButtonStyle : function(obj){
			if(obj){
				obj.css({'border' : '1px solid gray', 'cursor' : 'pointer'}).addClass("xqb_btn_n").hover(function(){
					$(this).addClass("xqb_btn_o");
				},function(){
					$(this).removeClass("xqb_btn_o");
				});
			}else{
				$("input:button").css({'border' : '1px solid gray', 'cursor' : 'pointer'}).addClass("xqb_btn_n").hover(function(){
					$(this).addClass("xqb_btn_o");
				},function(){
					$(this).removeClass("xqb_btn_o");
				});
				
				$("input:submit").css({'border' : '1px solid gray', 'cursor' : 'pointer'}).addClass("xqb_btn_n").hover(function(){
					$(this).addClass("xqb_btn_o");
				},function(){
					$(this).removeClass("xqb_btn_o");
				});
			}
		},
		/***
		 * 获取一个div button
		 */
		getXqbButton : function(options){
			var button = {};
			$.extend(button,{
				options : options,
				/**
				 * 生成dom元素,			button 的icon 只能是16 x 16的图标
				 */
				generateDomElement : function(){
					$.extend(this,{
						dom : $("<div></div>").appendTo($(document.body)).css({
								'cursor' : 'pointer','overflow' : 'hidden'
							}).height(22).shieldSelectStart().shieldRightMenu(),
						left : $("<div class='xqb_div_btn_left_n'></div>").appendTo($(document.body)),
						right : $("<div class='xqb_div_btn_right_n'></div>").appendTo($(document.body)),
						middle : $("<div class='xqb_div_btn_middle_n'></div>").appendTo($(document.body)),
						icon : '',
						text : $("<div></div>").appendTo($(document.body)).height(22).addClass("xqb_default_font").css({
							'line-height' : '20px','font-size' : '14px', 'text-align' : 'center',
							'padding' : '0px', 'text-indent' : '0em', 'float' : 'left' 
						})
					});
					var iw = 0;				//icon的width
					if(this.options.iconCls){
						this.icon = $("<div></div>").addClass(this.options.iconCls).css({
								"overflow" : "hidden", 'width' : '16px', 'height' : '16px' ,'margin' : '3px 0px 3px 1px' , 'float' : 'left'
							});
						this.icon.appendTo(this.middle);
						iw = this.icon.outerWidth(true);
						if($.browser.msie && $.browser.version == '6.0'){
							iw += 1;
						}
					}
					
					this.text.html(this.options.text ? this.options.text : '').width(this.text.width() + 10);
					this.middle.width(iw + this.text.outerWidth(true));
					this.text.appendTo(this.middle);
					this.dom.width(this.middle.outerWidth(true) + 8);
					
					this.left.appendTo(this.dom);
					this.middle.appendTo(this.dom);
					this.right.appendTo(this.dom);
					
					
					//注册事件
					this.dom.hover(function(){
						button.left.addClass("xqb_div_btn_left_o").removeClass("xqb_div_btn_left_n");
						button.right.addClass("xqb_div_btn_right_o").removeClass("xqb_div_btn_right_n");
						button.middle.addClass("xqb_div_btn_middle_o").removeClass("xqb_div_btn_middle_n");
					},function(){
						button.left.addClass("xqb_div_btn_left_n").removeClass("xqb_div_btn_left_o");
						button.right.addClass("xqb_div_btn_right_n").removeClass("xqb_div_btn_right_o");
						button.middle.addClass("xqb_div_btn_middle_n").removeClass("xqb_div_btn_middle_o");
					}).mousedown(function(){
						$(document).data("button",button);
						button.registerDocMouseUp();							//注册doc事件
						button.left.addClass("xqb_div_btn_left_p");
						button.right.addClass("xqb_div_btn_right_p");
						button.middle.addClass("xqb_div_btn_middle_p");
						return true;
					}).mouseup(function(e){
						button.left.removeClass("xqb_div_btn_left_p");
						button.right.removeClass("xqb_div_btn_right_p");
						button.middle.removeClass("xqb_div_btn_middle_p");
						return true;
					}).click(function(e){
						if(button.options.click && $.isFunction(button.options.click)){
							button.options.click.call(this,e);
						}
					});
					this.doLayout();
					this.created = true;
					return this;
				},
				
				/**
				 * 注册doc的mouse up 事件
				 */
				registerDocMouseUp : function(){
					$(document).mouseup(function(){
						var button = $(this).data("button");
						if(!button){
							return true;
						}
						button.detachMouseUp();									//卸载doc事件
						button.dom.trigger("mouseup");
					});
				},
				/**
				 * 卸载doc的mouse up 事件
				 */
				detachMouseUp : function(){
					$(document).unbind("mouseup");
				},
				
				/**
				 * 设置button 的宽度
				 */
				setWidth : function(wd){
					this.options.width = wd;
					this.doLayout();
					return this;
				},
				
				/***
				 * 
				 * 重新布局button
				 * 
				 */
				doLayout : function(){
					this.dom.width(this.options.width);
					this.middle.width(this.dom.width() - 8);
					if(this.icon){
						this.text.width(this.middle.width() - this.icon.outerWidth(true));
						if($.browser.msie && $.browser.version == "6.0"){
							this.text.width(this.text.width() - 1);
						}
					}else{
						this.text.width(this.middle.width());
					}
					return this;
				},
				
				/**
				 * 判断dom是否已经创建
				 * @returns
				 */
				isCreated : function(){
					return this.created ? true : false;
				},
				
				/**
				 * 显示button
				 */
				show : function(){
					if(!this.isCreated()){
						this.generateDomElement();
					}
					this.dom.show();
					return this;
				},
				
				/**
				 * 隐藏button
				 */
				hide : function(){
					this.dom.hide();
					return this;
				}
			});
			return button;
		}
		
	});
	
	/****
	 * 扩展globalMsg
	 */
	$.extend($.xqb9528.globalMsg,{
		msgQueue : [],
		
		currentBox : '',				//当前最新的msgBox
		/***
		 * 获取一个msgBox 对象
		 */
		getMsgBox : function(msg){
			if(this.msgQueue.length > 0){
				return this.msgQueue.pop().setMessage(msg);
			}
			return this.generateMsgBox(msg);
		},
		/***
		 * 归还msgBox
		 * @param el
		 */
		hide : function(el){
			this.msgQueue.push(el);
		},
		
		/***
		 * 生成一个msgBox
		 * @param msg
		 * @returns {___anonymous41899_41900}
		 */
		generateMsgBox : function(msg){
			var box = {};
			$.extend(box,{
				msg : msg,
				
				/**
				 * 生成dom元素
				 */
				generateDomElement : function(){
					$.extend(this,{
						dom : $("<div class='xqb_globalMsg_bg'></div>").appendTo($(document.body)).shieldSelectStart().shieldRightMenu()
					});
					this.created = true;
					return this;
				},
				
				/**
				 * 设置消息内容
				 * @param msg
				 * @returns {___anonymous42230_42450}
				 */
				setMessage : function(msg){
					this.msg = msg;
					return this;
				},
				
				/**
				 * 显示msg,single为true时表示只有一行数据
				 */
				show : function(single){
					var wnd = $(window);
					if(!this.created){
						this.generateDomElement();
					}
					if(single){
						this.dom.addClass("xqb_single");
					}else{
						this.dom.removeClass('xqb_single');
					}
					var maxIndex = $.xqb9528.shadowLayer.layerManager.getMaxIndex();
					this.dom.html(this.msg).css({'right' : '6px', 'position' : 'absolute',
						'z-index' : maxIndex + 3000});
					
					
					if($.xqb9528.globalMsg.currentBox){
						this.dom.css({'bottom' : (parseInt($.xqb9528.globalMsg.currentBox.dom.css("bottom")) + 61) + 'px'});
					}else{
						this.dom.css({'bottom' : (0 - wnd.scrollTop()) + 'px'});
					}
					this.dom.hide().animate({
						opacity : 'show',
						'bottom' : (parseInt(this.dom.css("bottom")) + 50) + "px",
						'right' : '6px'
					},800);
					$.xqb9528.globalMsg.currentBox = this;
					setTimeout(function(){
						box.hide();
					},3000);
					return this;
				},
				/***
				 * 隐藏box
				 */
				hide : function(){
					this.dom.animate({
						opacity : 'hide',
						'bottom' : (parseInt(this.dom.css("bottom")) - 50) + "px",
						'right' : '6px'
					},800,function(){
						if(box == $.xqb9528.globalMsg.currentBox){
							$.xqb9528.globalMsg.currentBox = "";
						}
						box.dom.css({'bottom' : '-61px'});
						$.xqb9528.globalMsg.hide(box);
					}).fadeOut(2000);
				}
			});
			return box;
		},
		
		/***
		 * 对外API 显示msg
		 * @param msg
		 * @returns
		 */
		showMsg : function(msg,single){
			return single ? this.getMsgBox(msg).show(true) : this.getMsgBox(msg).show(false);
		}
	});
	
	
	/***
	 *	@TODO	注册屏蔽右键插件 
	 */
	$.fn.shieldRightMenu = function(){
		$.xqb9528.shieldRightMenu(this);
		return this;
	};
	/***
	 *	@TODO	注册屏蔽onselectstart事件插件 
	 */
	$.fn.shieldSelectStart = function(){
		$.xqb9528.shieldSelectStart(this);
		return this;
	};
	
	/**
	 * 封装div
	 */
	$.fn.xqbBox = function(options){
		$.extend(options,{
			body : this,
			innerBorder : true
		});
		return $.xqb9528.shadowLayer.xqbBox(options);
	};
})(jQuery);

