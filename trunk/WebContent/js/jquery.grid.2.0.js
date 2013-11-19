/***********************************************************************************
 * 
 * @FILE		jquery.grid.js
 * @AUTHOR		9528
 * @DATE		2011-2-25
 * @COPY-RIGHT	大庆金桥成都分公司
 * 
 * @TODO		1、创建jquery grid
 * 		
 * 
 * @remark		目前尚存在如下bug
 * 				1、grid高度不能用百分比(%)表示
 * 				2、ie8 和 firefox中 grid的宽度和body的宽度不能同时使用百分比来表示，除非同时为100%
 * 				3、fireFox下拖拽功能尚未实现
 * 
 * @midified	2011-03-31 
 * 				高度比例可以为100%,其他百分比一概不支持,调用该js文件之前必须调用jquery.kenerl.2.0.js
 * 
 ***********************************************************************************/

(function($){
	
	$.extend($.dqgb,{						//创建名字空间
		grid : {}
	});
	
	$.extend($.dqgb.grid,{
		
		options : {	
			miniTitleBarWidth : 80,												//titlebar最小宽度
			gridHeaderHeight : 22,												//grid头部高度
			gridTitleBarHeight: 24,												//grid的title部分
			gridTitleBarCss:'__grid_title_bar',									//grid title bar 的样式名
			gridBodyCss:'__grid_body__',										//grid中存放实际内容的body区域的样式，用来筛选
			gridBodyContentRow:'___gridbodycontent_row__',						//存储内容的行的样式
			gridTitleBarWidth : "_gridTitleWidth__",							//存储grid title bar 的宽度的属性名
			titleTextPaddingLeft:20,											//title 部分文字的padding
			scrollOffset:30	
		},
		
		/**************************************
		 * 
		 * @TODO		生成序列号列配置对象
		 * 
		 *************************************/
		RowNumber : function(width){
			return {width:(width ? width : 30),alias:'rowNumber',title:''};
		},
		
		/**************************************
		 * 
		 *	@TODO		生成选择框列配置对象 
		 *
		 *************************************/
		CheckRow : function(){
			return {width:22,alias:'checkBox',title:''};
		},
		
		/******************************
		 * 
		 * @TODO	生成一个grid
		 * 
		 *****************************/
		generateGrid : function(options){
			var grid = {};
			var wnd = $(window);
			$.extend(grid,{
				options : options,
				body : $("#" + options.render).css({
					"margin":"0px", "padding":"0px", "overflow":"hidden", "width":options.width, "height":options.height
				}).shieldSelectStart().shieldRightMenu()									//grid的实体
			});
			
			var titleOptions = new Object();			//创建grid title的options参数
			$.extend(titleOptions,options);
			$.extend(titleOptions,{
				width : "100%",// closeBtn : false, afterClose : "",
				parent : grid.body,
				fontSize : "13px"					
			});
			
			
			$.extend(grid,{								//创建grid的header
				header : grid.options.title ? $.dqgb.panel.getPanelHeader(titleOptions) : ""
			});
			
			$.extend(grid,{								//创建grid的panelFrame
				pannelFrame : $("<div></div>").css({"margin":"0px","padding":"0px","overflow":"hidden"
							  }).appendTo(grid.body).addClass("global_border global_background")
			});
			
			$.extend(grid,{								//创建grid的内边线
				innerFrame : $("<div></div>").css({"background":"white","z-index":"0","margin":"0px",
								"margin":"6px","padding":"0px","position":"relative","overflow":"hidden"
							}).appendTo(grid.pannelFrame).addClass("global_border"),
				//布局管理函数
				doLayout : function(options){
					var wnd = $(window);
					if(options.height == "100%"){				//调整body尺寸
						if($.browser.msie && ($.browser.version == "6.0" || $.browser.version == "7.0") ){
							grid.body.css({ "height" : wnd.height() - 2 * 15 }).height(wnd.height() - 2 * 15);
						}else{
							grid.body.css({ "height" : wnd.height() - 18}).height(wnd.height() - 18);
						}
					}
					this.pannelFrame.css({						//调整内容区域尺寸
						"width" : this.body.width() - 2,
						"height" : this.body.height() - 2 - (this.header ? 22 : 0)
					});
					
					this.innerFrame.css({						//调整内边框区域尺寸
						"width" : this.pannelFrame.width() - 14,
						"height" : this.pannelFrame.height() - 14
					});
					
					this.content.css({									//调整内容区域的尺寸
						"height":this.innerFrame.height() - $.dqgb.grid.options.gridTitleBarHeight - (this.options.bbar ? 30 : 0) - (this.options.tbar ? (parseInt(this.options.tbar.height) + 1) : 0),
						"width":"100%", "overflow":"auto"
					}).trigger("scroll");
					
					if(this.shadowLayer.css('display') == "block"){		//调整shadow位置
						this.shadowLayer.width(this.innerFrame.width()).height(this.innerFrame.height());
					}
					
					if(this.mask.css('display') == "block"){			//调整mask位置
						this.mask.css({
							"left" : this.innerFrame.width()/2 - this.mask.width()/2 + "px",
							"top" : this.innerFrame.height()/2 - this.mask.height()/2 + "px",
							"position":"absolute","margin":"0px","padding":"0px"
						});
					}
					return this;		
				},
				/**************************
				 * 
				 * @TODO	获取选中列
				 * 
				 */
				getSelectedRow : function(){
					var rows = $("#" + this.options.render + " ." + $.dqgb.grid.options.gridBodyCss + " .checked");
					var data = [];
					for(var i = 0; i < rows.length; i++){
						var index = $(rows[i]).attr("rowIndex");
						data[data.length] = this.options.store[index];
					}
					return data;
				},
				/**************************
				 * 
				 * @TODO	设置数据
				 * 
				 */
				setStore : function(store){
					if($.browser.mozilla){
						this.content.get(0).scrollLeft = 0;
						this.content.get(0).scrollTop = 0;
					}
					//取消事件绑定，防止内存泄漏
					$("#"+this.options.render + " ." + $.dqgb.grid.options.gridBodyCss + " *").each(function(){
						$(this).unbind();
					});
					this.content.empty();
					this.content.get(0).innerHtml = "";
					this.options.store = store;
					this.generateContent();
					$("#" + this.options.render + " .grid-check-box-title").removeClass("unchecked").removeClass("checked").addClass("unchecked");
					this.content.trigger("scroll");							//修正滚动条bug
				},
				/**
				 * @TODO	显示蒙层
				 */
				showShadow : function(){
					this.shadowLayer.show().width(this.innerFrame.width()).height(this.innerFrame.height());
					if(this.mask.attr("initialized") != "initialized"){
						this.mask.attr("initialized","initialized");
						this.mask.css({
							"left" : "50%","top":"50%","margin-left" : - this.mask.width()/2,"margin-top" : -this.mask.outerHeight(true)/2
						}).show();
					}else{
						this.mask.css({
							"left" : this.innerFrame.width()/2 - this.mask.width()/2 + "px",
							"top" : this.innerFrame.height()/2 - this.mask.height()/2 + "px",
							"position":"absolute","margin":"0px","padding":"0px"
						}).show();
					}
				},
				/**
				 * @TODO	隐藏蒙层
				 */
				hideShadow : function(){
					this.mask.fadeOut(300);
					this.shadowLayer.fadeOut(300);
				},
				/******************************************
				 * 
				 * @TODO	生成grid 控件 的body区域
				 * 
				 *****************************************/
				generateGridBody : function(){
					//创建导航条区域
					$.extend(grid,{
						tbar : grid.options.tbar ? $("<div></div>").appendTo(grid.innerFrame).addClass("global_border_bottom dqgb_pagination_bar").css({
									"width":"100%","overflow-y":"visible","position":"relative","cursor":"default",
									"height" : this.options.tbar.height ? this.options.tbar.height : "29px"
								}) : ""
					});
					if(this.tbar){
						if(this.options.tbar.element){
							this.options.tbar.element.appendTo(this.tbar).css({"position":"absolute","top":"2px","left":"2px"});
						}
						this.options.tbar.height = this.options.tbar.height ? this.options.tbar.height : "29px";
					}
					//创建表格头部区域
					$.extend(grid,{
						titleBarContainer : $("<div class='dqgb_grid_titleBar_normal bg_repeatX'></div>").css({
												"width":"100%", "height":$.dqgb.grid.options.gridTitleBarHeight,
												"overflow":"hidden", "padding":"0px" 
											}).appendTo(grid.innerFrame)
					});
					$.extend(grid,{
						titleBar : $("<div class='dqgb_grid_titleBar_normal bg_repeatX'></div>").css({
										"height":$.dqgb.grid.options.gridTitleBarHeight, "padding":"0px"
									}).appendTo(grid.titleBarContainer).addClass($.dqgb.grid.options.gridTitleBarCss)
					});
					
					//创建titleBar
					var width = 0;
					for(var i = 0; i < this.options.columnModel.length; i++){
						this.getTitleColumn(i);
						width += this.options.columnModel[i].width;
					}
					this.titleBar.css("width",width + $.dqgb.grid.options.scrollOffset).attr($.dqgb.grid.options.gridTitleBarWidth,width);
					
					//创建内容区域
					$.extend(grid,{
						content : $("<div></div>").css({
										"height":"50px", "width":"100%", "overflow":"auto","cursor":"default"
									}).appendTo(grid.innerFrame).addClass($.dqgb.grid.options.gridBodyCss)
					});
					
					this.generateContent();		//生成内容
					
					//注册滚动事件
					this.content.scroll(function(){
						grid.titleBarContainer.get(0).scrollLeft = grid.content.scrollLeft();
					});
					if(grid.options.bbar){
						$.extend(grid,{
							bbar :  $("<div></div>").addClass("global_border_top dqgb_pagination_bar").css({
										"height":"29px", "overflow":"hidden","cursor":"default", "width":"100%"
									}).appendTo(grid.innerFrame),
							pagination : $.dqgb.pagination.getPagination({
										pageSize: grid.options.bbar.pageSize,
										totalCount: grid.options.bbar.totalCount,
										url : grid.options.bbar.url,					/** 数据获取连接 **/
										root : grid.options.bbar.root,
										total: grid.options.bbar.total,
										order : grid.options.bbar.order,
										manualLoad : grid.options.bbar.manualLoad,			//手动加载数据
										extraParam : grid.options.bbar.extraParam,
										showDetails:grid.options.bbar.showDetails,
										displayData : function(data,jqueryObj){			/** 数据处理函数 **/
											grid.setStore(data);
										},
										beforeLoading:function(){				/**	数据加载前 **/
											grid.showShadow();
										},
										afterLoading:function(){				/**	数据加载后 **/
											grid.hideShadow();
											if($.isFunction(grid.options.bbar.afterLoading)){
												grid.options.bbar.afterLoading.call();
											}
										}
									}),
							refresh : function(){
								this.pagination.refresh.trigger("click");
							}
						});
						grid.pagination.getDomElement().appendTo(grid.bbar).css({"margin-top":"3px"});
					}
				},
				/******************************************
				 * 
				 * @TODO	生成grid 控件 的title区域
				 * 
				 *****************************************/
				getTitleColumn : function(columnIndex){
					var column;
					if("checkBox" == this.options.columnModel[columnIndex].alias){
						column = $("<div title='" + (this.options.singleMode ? "单选模式" : "多选模式") + 
								"' class='grid-check-box-title unchecked'></div>").appendTo(this.titleBar);
						column.css({
							"width":this.options.columnModel[columnIndex].width,
							"height":$.dqgb.grid.options.gridTitleBarHeight, "cursor":"pointer", "float":"left"
						}).click(function(){
							if(!grid.options.singleMode){							//复选
								if($(this).hasClass("unchecked")){
									$(this).addClass("checked").removeClass("unchecked");
									$("#" + grid.options.render + " ." + $.dqgb.grid.options.gridBodyCss + " .grid-check-box-content")
										.removeClass("unchecked").removeClass("checked").addClass("checked");
								}else if($(this).hasClass("checked")){
									$(this).addClass("unchecked").removeClass("checked");
									$("#" + grid.options.render + " ." + $.dqgb.grid.options.gridBodyCss + " .grid-check-box-content")
										.removeClass("checked").removeClass("unchecked").addClass("unchecked");
								}else{
								
								}
							}else{
							}
						});
						var separator = $("<div class='dqgb_grid_titleBar_separator'></div>").appendTo(column);
						separator.css({
							"height":$.dqgb.grid.options.gridTitleBarHeight
						});
					}else if("rowNumber" == this.options.columnModel[columnIndex].alias){
						column = $("<div></div>").appendTo(this.titleBar);
						column.css({
							"width":this.options.columnModel[columnIndex].width,
							"height":$.dqgb.grid.options.gridTitleBarHeight,
							"float":"left"
						});
						
						var separator = $("<div class='dqgb_grid_titleBar_separator'></div>").appendTo(column);
						separator.css({
							"height":$.dqgb.grid.options.gridTitleBarHeight
						});
					}else{
						column = $("<div></div>").appendTo(this.titleBar);
						var timeout = "";
						column.css({
							"width":this.options.columnModel[columnIndex].width,
							"height":$.dqgb.grid.options.gridTitleBarHeight,
							"float":"left"
						}).mouseover(function(){
							clearTimeout(timeout);
							timeout = setTimeout(function(){
								column.addClass("dqgb_grid_titleBar_over");
								column.children(".button").show();
							},10);
						}).mouseout(function(){
							clearTimeout(timeout);
							timeout = setTimeout(function(){
								column.removeClass("dqgb_grid_titleBar_over");
								column.children(".button").removeClass("dqgb_grid_titleBar_button_over")
										.addClass("dqgb_grid_titleBar_button_normal").hide();
							},10);
						});
						//分隔符
						var separator = $("<div class='dqgb_grid_titleBar_separator'></div>").appendTo(column);
						separator.css({
							"height":$.dqgb.grid.options.gridTitleBarHeight,
							"cursor":"col-resize"
						});
						
						var button = $("<div class='button dqgb_grid_titleBar_button_normal'></div>").appendTo(column);
						button.css({
							"margin-top":"-1px",
							"height":$.dqgb.grid.options.gridTitleBarHeight,
							"display":"none"
						}).mouseover(function(){
							button.removeClass("dqgb_grid_titleBar_button_normal").addClass("dqgb_grid_titleBar_button_over");
						}).mouseout(function(){
							button.addClass("dqgb_grid_titleBar_button_normal").removeClass("dqgb_grid_titleBar_button_over");
						});
						
						var textBar = $("<div></div>").appendTo(column).css({
							"width" : column.width() - separator.width() - button.width() - $.dqgb.grid.options.titleTextPaddingLeft,
							"line-height": $.dqgb.grid.options.gridTitleBarHeight + "px",
							"height": $.dqgb.grid.options.gridTitleBarHeight,
							"overflow":"hidden",
							"font-size": "14px","color":"#006666","font-weight":"600",
							"padding-left": $.dqgb.grid.options.titleTextPaddingLeft + "px",
							"float": "left",
							"cursor": "default",
							"font-family": "微软雅黑"
						}).html(this.options.columnModel[columnIndex].title).attr("title",this.options.columnModel[columnIndex].title);
						if($.browser.msie){
							textBar.addClass("dqgb_overflow_ellipsis");
						}
						
						separator.unbind("mousedown").bind("mousedown",function(ev){
							if($.browser.msie){
								this.setCapture();
							}else if($.browser.mozilla){
								$(document.body).unbind("mouseup").bind("mouseup",function(ev){
									separator.attr("posTo",ev.pageX);
									var orgWidth = column.width();
									var cWidth = column.width() + parseInt(separator.attr("posTo") - separator.attr("posFrom"));
									cWidth = cWidth < $.dqgb.grid.options.miniTitleBarWidth ? $.dqgb.grid.options.miniTitleBarWidth : cWidth;
									
									//修正titleBar的尺寸
									var actualWidth = parseInt(grid.titleBar.attr($.dqgb.grid.options.gridTitleBarWidth)) + parseInt(cWidth - orgWidth);
									if(actualWidth > $.dqgb.getCssValue(grid.titleBar.css("width")))
										grid.titleBar.css("width",actualWidth + $.dqgb.grid.options.scrollOffset);
									grid.titleBar.attr($.dqgb.grid.options.gridTitleBarWidth,actualWidth);
									
									column.width(cWidth).css("width",cWidth);
									grid.options.columnModel[columnIndex].width = cWidth;
									textBar.css("width",cWidth - separator.width() - button.width() - 
											$.dqgb.grid.options.titleTextPaddingLeft + "px").width(
											cWidth - separator.width() - button.width() - $.dqgb.grid.options.titleTextPaddingLeft);
									
									var contentRow = $("#" + grid.options.render + " ." + $.dqgb.grid.options.gridBodyContentRow);
									contentRow.css("width",actualWidth + "px").width(actualWidth);
									
									$("#" + grid.options.render + " ." + $.dqgb.grid.options.gridBodyCss + " div[column = '" + 
											column.attr("column") + "']").each(function(index){
										$(this).width($(this).width() + parseInt(cWidth - orgWidth)).removeClass("dqgb_overflow_ellipsis");
										//.css("width", ($(this).width() + parseInt(cWidth - orgWidth)) + "px");
									});
									contentRow.trigger("scroll");
									$(this).unbind("mouseup");
								});
							}
							separator.attr("posFrom",ev.pageX);
						}).unbind("mouseup").bind("mouseup",function(ev){
							if($.browser.msie){
								separator.attr("posTo",ev.pageX);
								separator.trigger("mouseout");
								this.releaseCapture();
								var orgWidth = column.width();
								var cWidth = column.width() + parseInt(separator.attr("posTo") - separator.attr("posFrom"));
								cWidth = cWidth < $.dqgb.grid.options.miniTitleBarWidth ? $.dqgb.grid.options.miniTitleBarWidth : cWidth;
								
								//修正titleBar的尺寸
								var actualWidth = parseInt(grid.titleBar.attr($.dqgb.grid.options.gridTitleBarWidth)) + parseInt(cWidth - orgWidth);
								if(actualWidth > $.dqgb.getCssValue(grid.titleBar.css("width")))
									grid.titleBar.css("width",actualWidth + $.dqgb.grid.options.scrollOffset);
								grid.titleBar.attr($.dqgb.grid.options.gridTitleBarWidth,actualWidth);
								
								column.width(cWidth).css("width",cWidth);
								grid.options.columnModel[columnIndex].width = cWidth;
								textBar.css("width",cWidth - separator.width() - button.width() - $.dqgb.grid.options.titleTextPaddingLeft);
								
								var contentRow = $("#" + grid.options.render + " ." + $.dqgb.grid.options.gridBodyContentRow);
								contentRow.css("width",actualWidth + "px").width(actualWidth);
								
								$("#" + grid.options.render + " ." + $.dqgb.grid.options.gridBodyCss + " div[column = '" + column.attr("column") + "']")
								.each(function(){
									$(this).css("width", ($(this).width() + parseInt(cWidth - orgWidth)) + "px");
								});
								contentRow.trigger("scroll");
							}else if($.browser.mozilla){
								$(document.body).unbind("mouseup");
							}
						});
					}
					return column.shieldSelectStart().shieldRightMenu().attr("column",grid.options.columnModel[columnIndex].alias);
				},
				/**
				 * @TODO	触发数据的加载操作
				 */
				show : function(){
					if(grid.options.bbar.manualLoad == true){
						grid.refresh();
		    		}
					grid.options.bbar.manualLoad = false;
				},
				/**********
				 * 
				 * @TODO	生成一个内容部分的column
				 * 
				 */
				generateContent : function(){
					if(this.options.store && 0 == this.options.store.length && this.pagination.getPageIndex() != 0){
						this.refresh();
						return;
					}
					for(var i = 0; this.options.store && i < this.options.store.length; i++){
						this.content.append($("<div class='" + $.dqgb.grid.options.gridBodyContentRow + "'></div>"));
						var row = $("#" + this.options.render + " ." + $.dqgb.grid.options.gridBodyContentRow + ":last");
						row.css({
							"overflow":"hidden",
							"height" : $.dqgb.grid.options.gridTitleBarHeight + "px",
							"width" : this.titleBar.attr($.dqgb.grid.options.gridTitleBarWidth) + "px"
						});
						if(i % 2 == 0){
							row.addClass("even");
						}else{
							row.addClass("odd");
						}
						$(row).mouseover(function(){
							$(this).addClass("over");
						}).mouseout(function(){
							$(this).removeClass("over");
						}).click(function(){
							grid.content.children("." + $.dqgb.grid.options.gridBodyContentRow).removeClass("selected");
							$(this).addClass("selected");
							$(this).children(".grid-check-box-content").trigger("click");
							return true;
						});
						for(var j = 0; j < this.options.columnModel.length; j++){
							if("rowNumber" == this.options.columnModel[j].alias){
								var column;
								column = $("<div class='dqgb_grid_number'></div>").appendTo(row);
								column.css({
									"width":this.options.columnModel[j].width,
									"float":"left"
								});
								
								var separator = $("<div class='dqgb_grid_titleBar_separator'></div>").appendTo(column);
								separator.css({
									"height":$.dqgb.grid.options.gridTitleBarHeight
								});
								column.shieldSelectStart().shieldRightMenu()
									.html("<label style='margin-right:5px;'>"+ (i + 1) + "</label>");
							}else if("checkBox" == options.columnModel[j].alias){
								var column;
								column = $("<div class='grid-check-box-content unchecked'></div>").appendTo(row);
								column.css({
									"width":this.options.columnModel[j].width,
									"height":$.dqgb.grid.options.gridTitleBarHeight,
									"cursor":"pointer",
									"float":"left"
								}).click(function(){
									if(!grid.options.singleMode){							//多选模式
										var titleCheckbox = $("#" + grid.options.render + " .grid-check-box-title");
										if($(this).hasClass("unchecked")){
											$(this).addClass("checked").removeClass("unchecked");
											if($("#" + grid.options.render + " ." + $.dqgb.grid.options.gridBodyCss + " .unchecked").length == 0){
												titleCheckbox.addClass("checked").removeClass("unchecked");
											}
											if(grid.options.listeners && $.isFunction(grid.options.listeners.rowChecked)){
												grid.options.listeners.rowChecked.call(this,grid,parseInt($(this).attr("rowIndex")));
											}
										}else if($(this).hasClass("checked")){
											$(this).addClass("unchecked").removeClass("checked");
											titleCheckbox.addClass("unchecked").removeClass("checked");
										}
									}else{												//单选模式
										if($(this).hasClass("unchecked")){	
											$("#" + grid.options.render + " ." + $.dqgb.grid.options.gridBodyCss + " .checked")
												.addClass("unchecked").removeClass("checked");
											$(this).addClass("checked").removeClass("unchecked");
											if(grid.options.listeners && $.isFunction(grid.options.listeners.rowChecked)){
												grid.options.listeners.rowChecked.call(this,grid,parseInt($(this).attr("rowIndex")));
											}
										}else if($(this).hasClass("checked")){
											$(this).addClass("unchecked").removeClass("checked");
										}
									}
									return false;
								}).attr("rowIndex",i);
								column.shieldSelectStart().shieldRightMenu();
							}else{
								var column;
								column = $("<div></div>").appendTo(row);
								column.css({
									"width":grid.options.columnModel[j].width - $.dqgb.grid.options.titleTextPaddingLeft,
									"height":$.dqgb.grid.options.gridTitleBarHeight,
									"line-height": $.dqgb.grid.options.gridTitleBarHeight + "px",
									"font-size": "14px","overflow":"hidden",
									"padding-left": $.dqgb.grid.options.titleTextPaddingLeft + "px",
									"font-family": "微软雅黑",
									"float":"left"
								});
								
								var index = 0;
								if(grid.options.columnModel[0].alias == "rowNumber")
									index++;
								if(grid.options.columnModel[1].alias == "checkBox")
									index++;
								column.appendTo(row).shieldSelectStart().shieldRightMenu()
									.attr("column",grid.options.columnModel[j].alias);
								
								if($.isFunction(options.columnModel[j].beforeDataLoading)){
									if(options.columnModel[j].beforeDataLoading.call(this,grid,column,grid.options.store[i][grid.options.columnModel[j].alias],i, j - index)){
										column.html(grid.options.store[i][grid.options.columnModel[j].alias]);
									}
								}else{
									column.html(grid.options.store[i][grid.options.columnModel[j].alias]);
								}
								if($.browser.msie){
									column.addClass("dqgb_overflow_ellipsis");
								}
								if($.browser.mozilla){
									column.attr("title",grid.options.store[i][grid.options.columnModel[j].alias]);
								}
							}
						}
					}
				}
			});
			
			//扩展mask
			$.extend(grid,{
				mask : $("<div style='white-space:nowrap;'></div>").appendTo(grid.innerFrame).css({
							"background-color":"white", 'white-space':'nowrap', "height":"30px", "width":"100px", 
							"border":"1px solid #6593CF","position" : "absolute","cursor":"default",
							"z-index":$.dqgb.options.mask_Z_Index + 1
						}).addClass("dqgb_loading_mask").shieldRightMenu().shieldSelectStart(),
				shadowLayer : $("<div></div>").appendTo(grid.innerFrame).css({							//蒙层							
								"position":"absolute", "left":"0px", "z-index":$.dqgb.options.mask_Z_Index, "background-color":"black",
								"filter":"alpha(opacity=" + $.dqgb.options.opacity + ")", "-moz-opacity":"0." + $.dqgb.options.opacity,
								"display":"none", "opacity":"0." + $.dqgb.options.opacity, "top":"0px"
							}).shieldRightMenu().shieldSelectStart(),
				//初始化mask
				initializeMask : function(){
					var innerFrame = $("<div></div>").appendTo(this.mask).css({
						"margin":"2px", "width": this.mask.width() - 6, "height":this.mask.height() - 6, "background-color":"#FBFBFB", "border":"1px solid #A3BAD9"
					});
					var image = $("<div class='dqgb_loading_wait'></div>").appendTo(innerFrame).css({
						"width" : innerFrame.height(), "float" : "left", "height" : innerFrame.height()
					});
					var text = $("<div class='text'></div>").appendTo(innerFrame).css({
						"margin-left":"2px", "white-space":"nowrap",
						"float" : "left", "height" : innerFrame.height(), "line-height":innerFrame.height() - 2 + "px",
						"overflow":"visible", "width":"20px", "font-size":"13px", "font-family":"微软雅黑"
					});
					grid.options.loadingMask = grid.options.loadingMask ? grid.options.loadingMask : "正在加载数据,请稍后......";
					text.html("<span>" + grid.options.loadingMask + "</span>");
					var wd = text.children("span").get(0).offsetWidth;
					this.mask.css("width",wd + 2 + image.width() + 6 + 10);
					innerFrame.css("width",this.mask.width() - 6);
					this.mask.hide();
				}
			});
			// 注册resize事件
			$.dqgb.registerLayoutFn(function(){
				grid.doLayout(grid.options);
			});
			grid.initializeMask();
			grid.generateGridBody();
			
			return grid.doLayout(grid.options);
		}
	});
	
})(jQuery);