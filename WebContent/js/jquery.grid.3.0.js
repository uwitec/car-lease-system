/***********************************************************************************
 * 
 * @FILE		jquery.grid.js
 * @AUTHOR		9528
 * @DATE		2011-8-7
 * @COPY-RIGHT	大庆金桥成都分公司
 * 
 * @TODO		1、创建jquery grid
 * 		
 * @remark		较2.0相比，该版本的整体的架构有了较大的改动。所有组件，部件都
 * 				采用面向对象的思想架构，提供了更多的API以及更友好的扩展接口以
 * 				及更高效的接口方法
 * 
 ***********************************************************************************/

(function($){
	
	$.extend($.xqb9528,{						//创建名字空间
		grid : {}
	});
	
	$.extend($.xqb9528.grid,{
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
		CheckColumn : function(){
			return {width:22,alias:'checkColumn',title:''};
		},
		
		/**
		 * column model
		 */
		columnModel : {
			/**
			 * 获取title row number 列
			 * @param options
			 */
			getTitleRowNumberColumn : function(options){
				var rowNumCol = {};
				$.extend(rowNumCol,{
					options : options,
					/**
					 * 生成dom
					 */
					generateDomElement : function(){
						$.extend(this,{
							dom : $("<div></div>").appendTo($(document.body)).addClass("xqb_grid_title_col xqb_grid_title_n xqb_overflow_ellipsis").css({
								'text-align' : 'right', 'margin':'0px','padding':'0px','overflow':'hidden'
							}),
							sep : $("<div></div>").appendTo($(document.body)).addClass("xqb_grid_sep")
						});
						this.dom.width(this.options.width).append(this.sep);
						return this;
					},
					
					/**
					 * 获取宽度
					 */
					getWidth : function(){
						return this.options.width;
					},
					
					/**
					 * 获取别名
					 */
					getAlias : function(){
						return this.options.alias;
					},
					
					/**
					 * 存储grid对象
					 * @param grid
					 */
					setGrid : function(grid){
						this.grid = grid;
						return this;
					},
					
					/**
					 * 不做任何操作
					 * @param text
					 */
					setText : function(text){
						return this;
					},
					/**
					 * 获取组件原始数据，在没有注册beforeLoadingData事件的情况下该值就是getCurrentValue返回的值
					 * @returns
					 */
					getOriginalValue : function(){
						return this.orgValue;
					},
					
					/**
					 * 设置orgValue的值
					 * @param value
					 */
					setOriginalValue : function(value){
						this.orgValue = value;
						return this;
					},
					/**
					 * 设置column 所在的row
					 * @param row
					 */
					setRow : function(row){
						this.row = row;
						return this;
					}
				});
				return rowNumCol.generateDomElement();
			},
			
			/***
			 * 获取内容区域的行号对象
			 * @param options
			 */
			getContentRowNumberColumn : function(options){
				var rowNumCol = {};
				$.extend(rowNumCol,{
					options : options,
				
					/**
					 * 生成dom
					 */
					generateDomElement : function(){
						$.extend(this,{
							dom : $("<div></div>").appendTo($(document.body)).addClass("xqb_number_content xqb_overflow_ellipsis").css({
								'text-align' : 'right','margin':'0px','padding':'0px','overflow':'hidden'
							}),
							label : $("<label style = 'margin-right : 5px;'></label>").appendTo($(document.body))
						});
						this.dom.width(this.options.width).append(this.label);
						return this;
					},
						
					/**
					 * 设置文本
					 * @param text
					 */
					setText : function(text){
						this.label.html(text);
						return this;
					},
					/**
					 * 获取组件原始数据，在没有注册beforeLoadingData事件的情况下该值就是getCurrentValue返回的值
					 * @returns
					 */
					getOriginalValue : function(){
						return this.orgValue;
					},
					
					/**
					 * 设置orgValue的值
					 * @param value
					 */
					setOriginalValue : function(value){
						this.orgValue = value;
						return this;
					},
					/**
					 * 获取别名
					 */
					getAlias : function(){
						return this.options.alias;
					},
					
					/**
					 * 获取宽度
					 */
					getWidth : function(){
						return this.options.width;
					},
					/**
					 * 存储grid对象
					 * @param grid
					 */
					setGrid : function(grid){
						this.grid = grid;
						return this;
					},
					
					/**
					 * 设置column 所在的row
					 * @param row
					 */
					setRow : function(row){
						this.row = row;
						return this;
					}
				});
				return rowNumCol.generateDomElement();
			},
			
			/**
			 * 获取title部分的checkBox
			 */
			getTitleCheckColumn : function(options){
				var checkColumn = {};
				$.extend(checkColumn,{
					options : options,
					
					/**
					 * 生成dom
					 */
					generateDomElement : function(){
						$.extend(this,{
							dom : $("<div></div>").appendTo($(document.body)).addClass("xqb_check_column_normal").css({
								'margin':'0px','padding':'0px','overflow':'hidden'
							}),
							sep : $("<div></div>").appendTo($(document.body)).addClass("xqb_grid_sep")
						});
						this.dom.append(this.sep);
						
						if(this.options.checkModel != "single"){
							this.dom.click(function(){
								/**
								 * 触发 beforeClick 事件，选中和取消选中时都会触发,传递参数checkColumn对象，以及是否checked，checked时为true，反之为false
								 */
								if(checkColumn.options.beforeClick && $.isFunction(checkColumn.options.beforeClick)){
									checkColumn.options.beforeClick.call(this, checkColumn, checkColumn.isChecked(), checkColumn.grid);
								}
								if(checkColumn.isChecked()){
									checkColumn.unCheck();
								}else{
									checkColumn.check();
								}
								/**
								 * 触发 afterClick 事件，选中和取消选中时都会触发,传递参数checkColumn对象，以及是否checked，checked时为true，反之为false
								 */
								if(checkColumn.options.afterClick && $.isFunction(checkColumn.options.afterClick)){
									checkColumn.options.afterClick.call(this, checkColumn, checkColumn.isChecked(), checkColumn.grid);
								}
							}).attr("title","多选模式");
						}else{
							this.dom.attr("title","单选模式");
						}
						return this;
					},
					
					/**
					 * 选中checkBox
					 */
					check : function(){
						if(!this.dom.hasClass("xqb_check_column_checked")){
							this.dom.addClass("xqb_check_column_checked");
							this.row.setCheck(true);
						}
						return this;
					},
					
					/**
					 * 取消选中
					 */
					unCheck : function(){
						this.dom.removeClass("xqb_check_column_checked");
						this.row.setCheck(false);
						return this;
					},
					
					/**
					 * 判断是否被选中
					 */
					isChecked : function(){
						return this.dom.hasClass("xqb_check_column_checked") ? true : false;
					},
					
					/**
					 * 获取别名
					 */
					getAlias : function(){
						return this.options.alias;
					},
					
					/**
					 * 获取宽度
					 */
					getWidth : function(){
						return 22;
					},
					
					/**
					 * 存储grid对象
					 * @param grid
					 */
					setGrid : function(grid){
						this.grid = grid;
						return this;
					},
					
					/**
					 * 不做任何操作
					 * @param text
					 */
					setText : function(text){
						return this;
					},
					/**
					 * 获取组件原始数据，在没有注册beforeLoadingData事件的情况下该值就是getCurrentValue返回的值
					 * @returns
					 */
					getOriginalValue : function(){
						return this.orgValue;
					},
					
					/**
					 * 设置orgValue的值
					 * @param value
					 */
					setOriginalValue : function(value){
						this.orgValue = value;
						return this;
					},
					/**
					 * 设置column 所在的row
					 * @param row
					 */
					setRow : function(row){
						this.row = row;
						return this;
					}
				});
				return checkColumn.generateDomElement();
			},
			
			/**
			 * 获取content部分的checkBox
			 */
			getContentCheckColumn : function(options){
				var checkColumn = {};
				$.extend(checkColumn,{
					options : options,
					
					/**
					 * 生成dom
					 */
					generateDomElement : function(){
						$.extend(this,{
							dom : $("<div></div>").appendTo($(document.body)).addClass("xqb_check_column_normal").css({
								'margin':'0px','padding':'0px','overflow':'hidden'
							})
						});
						this.dom.click(function(){
							/**
							 * 触发 beforeClick 事件，选中和取消选中时都会触发,传递参数checkColumn对象，以及是否checked，checked时为true，反之为false
							 */
							if(checkColumn.options.beforeClick && $.isFunction(checkColumn.options.beforeClick)){
								checkColumn.options.beforeClick.call(this, checkColumn, checkColumn.isChecked(), checkColumn.grid);
							}
							
							if(checkColumn.isChecked()){
								checkColumn.unCheck();
							}else{
								checkColumn.check();
							}
							/**
							 * 触发 afterClick 事件，选中和取消选中时都会触发,传递参数checkColumn对象，以及是否checked，checked时为true，反之为false
							 */
							if(checkColumn.options.afterClick && $.isFunction(checkColumn.options.afterClick)){
								checkColumn.options.afterClick.call(this,checkColumn,checkColumn.isChecked(), checkColumn.grid);
							}
							return false;
						});
						return this;
					},
					
					/**
					 * 选中checkBox
					 */
					check : function(){
						if(!this.dom.hasClass("xqb_check_column_checked")){
							this.dom.addClass("xqb_check_column_checked");
							this.row.setCheck(true);
						}
						return this;
					},
					
					/**
					 * 取消选中
					 */
					unCheck : function(){
						if(this.dom.hasClass("xqb_check_column_checked")){
							this.dom.removeClass("xqb_check_column_checked");
							this.row.setCheck(false);
						}
						return this;
					},
					
					/**
					 * 判断是否被选中
					 */
					isChecked : function(){
						return this.dom.hasClass("xqb_check_column_checked") ? true : false;
					},
					
					/**
					 * 获取别名
					 */
					getAlias : function(){
						return this.options.alias;
					},
					
					/**
					 * 获取宽度
					 */
					getWidth : function(){
						return 22;
					},
					
					/**
					 * 存储grid对象
					 * @param grid
					 */
					setGrid : function(grid){
						this.grid = grid;
						return this;
					},
					
					/**
					 * 不做任何操作
					 * @param text
					 */
					setText : function(text){
						return this;
					},
					/**
					 * 获取组件原始数据，在没有注册beforeLoadingData事件的情况下该值就是getCurrentValue返回的值
					 * @returns
					 */
					getOriginalValue : function(){
						return this.orgValue;
					},
					
					/**
					 * 设置orgValue的值
					 * @param value
					 */
					setOriginalValue : function(value){
						this.orgValue = value;
						return this;
					},
					/**
					 * 设置column 所在的row
					 * @param row
					 */
					setRow : function(row){
						this.row = row;
						return this;
					}
				});
				return checkColumn.generateDomElement();
			},
			
			/**
			 * 获取title部分的普通column
			 * @param options
			 */
			getTitleColumn : function(options){
				var titleColumn = {};
				
				$.extend(titleColumn,{
					options : options,
					
					/**
					 * 生成dom元素，注册dom元素的事件
					 */
					generateDomElement : function(){
						$.extend(this,{
							dom : $("<div></div>").appendTo($(document.body)).addClass("xqb_grid_title_col").css({
								'margin':'0px','padding':'0px','overflow':'hidden','float' : 'left'
							}),
							
							timer : '',			//hover timer
							
							menuButton : $("<div></div>").appendTo($(document.body)).addClass("xqb_title_button_n"),	//下拉菜单的button
							
							text : $("<div></div>").appendTo($(document.body)).css({"float":'left','cursor' : 'default','margin' : '0px'}),
							
							sep : $("<div></div>").appendTo($(document.body)).addClass("xqb_grid_sep").css({'cursor' : 'col-resize'})
						});
						this.sep.appendTo(this.dom);
						this.menuButton.appendTo(this.dom);
						this.menuButton.hide();
						this.text.appendTo(this.dom).addClass("xqb_overflow_ellipsis xqb_default_font").css({
							'height': '24px', 'line-height': '23px','color' : '#006666', 'font-weight' : '600',
							'font-size' : '14px','padding-left' : '20px'
						});
						this.setWidth(this.options.width);
						
						//鼠标滑动效果
						this.dom.mouseover(function(){
							clearTimeout(titleColumn.timer);
							titleColumn.timer = setTimeout(function(){
								titleColumn.dom.addClass("xqb_grid_title_o");
								titleColumn.menuButton.show();
							},10);
							return true;
						}).mouseout(function(){
							clearTimeout(titleColumn.timer);
							titleColumn.timer = setTimeout(function(){
								titleColumn.dom.removeClass("xqb_grid_title_o");
								titleColumn.menuButton.hide();
							},10);
							return true;
						});
						
						//鼠标滑动效果
						this.menuButton.hover(function(){
							titleColumn.menuButton.addClass("xqb_title_button_o");
							if(titleColumn.options.sortable){
								titleColumn.menuButton.css({"cursor":"pointer"});
							}else{
								titleColumn.menuButton.css({"cursor":"default"});
							}
							return true;
						},function(){
							titleColumn.menuButton.removeClass("xqb_title_button_o");
							return true;
						}).click(function(){
							if(titleColumn.options.sortable){
								titleColumn.options.sort.call(this,titleColumn.grid,titleColumn);
							}
						});
						
						//拖拽事件
						this.sep.mousedown(function(e){
							$(document).data("titleColumn",titleColumn);
							if($.browser.msie){
								titleColumn.sep.get(0).setCapture();
							}else if($.browser.mozilla){
								titleColumn.registerDocMouseUp();
							}
							titleColumn.orgX = e.clientX;
							return true;
						}).mouseup(function(e){
							if($.browser.msie){
								titleColumn.sep.get(0).releaseCapture();
							}
							titleColumn.sep.trigger("mouseout");
							var dis = e.clientX - titleColumn.orgX;
							titleColumn.setWidth(titleColumn.getWidth() + dis);
							return true;
						});
						
						
						this.created = true;
						
						return this;
					},
					
					/**
					 * 设置排序的顺序
					 * @param asc
					 */
					setAsc : function(asc){
						this.asc = asc;
					},
					
					/**
					 * 检测是否处于升序排列
					 * @returns
					 */
					isAsc : function(){
						return this.asc ? true : false;
					},
					
					/**
					 * 布局
					 */
					doLayout : function(){
						this.text.width(this.dom.width() - 1 - 14 - 20);
						this.text.html(this.options.title);
						return this;
					},
					/***
					 * 注册鼠标up事件
					 */
					registerDocMouseUp : function(){
						$(document).one("mouseup",function(e){
							var titleColumn = $(document).data("titleColumn");
							if(!titleColumn){
								return ;
							}
							titleColumn.sep.trigger("mouseout");
							var dis = e.clientX - titleColumn.orgX;
							titleColumn.setWidth(titleColumn.getWidth() + dis);
							return true;
						});
					},
					
					
					/**
					 * 获取别名
					 */
					getAlias : function(){
						return this.options.alias;
					},
					
					/**
					 * 获取宽度
					 */
					getWidth : function(){
						return this.dom.outerWidth(true);
					},
					
					/**
					 * 设置宽度
					 * @param width
					 */
					setWidth : function(width){
						this.dom.width(width > 80 ? width : 80);
						this.doLayout();
						/**
						 * 触发调整尺寸以后的事件
						 */
						if(this.options.afterLayout && $.isFunction(this.options.afterLayout)){
							this.options.afterLayout.call(this,this.grid,this);
						}
						return this;
					},
					
					/**
					 * show column
					 */
					show : function(){
						if(!this.created){
							this.generateDomElement();
						}
						this.dom.show();
						this.visible = true;
						return this;
					},
					
					/**
					 * hide column
					 */
					hide : function(){
						this.dom.hide();
						this.visible = false;
						return this;
					},
					
					/**
					 * 判断列对象是否可见
					 * @returns
					 */
					isVisible : function(){
						return this.visible ? true : false;
					},
		
					/**
					 * 设置文本
					 * @param text
					 */
					setText : function(text){
						this.text.html(text);
						return this;
					},
					
					/**
					 * 获取组件原始数据，在没有注册beforeLoadingData事件的情况下该值就是getCurrentValue返回的值
					 * @returns
					 */
					getOriginalValue : function(){
						return this.orgValue;
					},
					
					/**
					 * 设置orgValue的值
					 * @param value
					 */
					setOriginalValue : function(value){
						this.orgValue = value;
						return this;
					},
					
					/**
					 * 存储grid对象
					 * @param grid
					 */
					setGrid : function(grid){
						this.grid = grid;
						return this;
					},
					
					/**
					 * 设置column 所在的row
					 * @param row
					 */
					setRow : function(row){
						this.row = row;
						return this;
					}
				});
				
				return titleColumn;
			},
			
			/**
			 * 获取一个普通的content column
			 */
			getContentColumn : function(options){
				var column = {};
				
				$.extend(column,{
					options : options,
					
					/**
					 * 生成dom
					 */
					generateDomElement : function(){
						$.extend(this,{
							dom : $("<div></div>").appendTo($(document.body)).addClass("xqb_overflow_ellipsis xqb_default_font").css({
									'height': '24px', 'line-height': '23px','margin' : '0px', 'padding' : '0px','float' : 'left',
									'font-size' : '13px','padding-left' : '20px'
								})
						});
						this.setWidth(this.options.width);
						this.created = true;
						return this;
					},
					
					/**
					 * 设置宽度
					 * @param width
					 */
					setWidth : function(width){
						this.dom.width(width - 20);
						return this;
					},
					
					/**
					 * 获取宽度
					 * @returns
					 */
					getWidth : function(){
						return this.dom.outerWidth(true);
					},
					
					/**
					 * 获取别名
					 */
					getAlias : function(){
						return this.options.alias;
					},
					/**
					 * show column
					 */
					show : function(){
						if(!this.created){
							this.generateDomElement();
						}
						this.dom.show();
						return this;
					},
					
					/**
					 * hide column
					 */
					hide : function(){
						this.dom.hide();
						return this;
					},
					
					/**
					 * 设置文本
					 * @param text
					 */
					setText : function(text){
						this.dom.empty();				//清除原有的数据以及dom对象包括dom对象的事件(因为row和column是一直复用的所以该操作是必须的)
						if(this.options.beforeLoadingData && $.isFunction(this.options.beforeLoadingData)){
							if(this.options.beforeLoadingData.call(this,this.grid,this.row,this,text)){
								this.dom.html(text);
							}
						}else{
							this.dom.html(text);
						}
						return this;
					},
					
					/**
					 * 获取当前column中的text信息
					 * @returns
					 */
					getCurrentValue : function(){
						return this.dom.html();
					},
					
					/**
					 * 获取组件原始数据，在没有注册beforeLoadingData事件的情况下该值就是getCurrentValue返回的值
					 * @returns
					 */
					getOriginalValue : function(){
						return this.orgValue;
					},
					
					/**
					 * 设置orgValue的值
					 * @param value
					 */
					setOriginalValue : function(value){
						this.orgValue = value;
						return this;
					},
					
					/**
					 * 存储grid对象
					 * @param grid
					 */
					setGrid : function(grid){
						this.grid = grid;
						return this;
					},
					
					/**
					 * 设置column 所在的row
					 * @param row
					 */
					setRow : function(row){
						this.row = row;
						return this;
					}
				});
				
				return column;
			},
			
			/*****
			 * 生成一个tree grid 的column, 从普通Content继承而来。重写setWidth函数
			 */
			getTreeColumn : function(options){
				var column = this.getContentColumn(options);
				$.extend(column,{
					/**
					 * 设置宽度，并且重置tree中所有相同别名的column的宽度
					 */
					setWidth : function(width){
						this.dom.width(width - parseInt(this.dom.css("padding-left")));
						for(var i = 0; this.row && i < this.row.subRows.length; i++){
							var sr = this.row.subRows[i];
							sr.getColumn(this.getAlias()).setWidth(width);				//调节子列的宽度，一直递归下去
							sr.setWidth(sr.getColumnsWidth());							//调节子行的宽度，一直递归下去
						}	
						return this;
					}
				});
				return column;
			},
			/**
			 * 生成一个tree grid 的 expand collapse column，该column 具备展开关闭功能  继承至getTreeColumn
			 * @param options
			 */
			getTreeECColumn : function(options){
				var column = this.getTreeColumn(options);
				$.extend(column,{
					/***
					 * 覆盖重写dom的生成方法
					 */
					generateDomElement : function(){
						$.extend(this,{
							/**节点的dom元素**/
							dom : $("<div></div>").appendTo($(document.body)).addClass("xqb_overflow_ellipsis xqb_default_font").css({
									'height': '24px', 'line-height': '23px','margin' : '0px', 
									'padding' : '0px','float' : 'left','font-size' : '13px'
								}),
							nodeDom : $("<div></div>").width(10000).height(24),					//tree node所在的dom
							ecItem : '',														//expand Collapse Item
							clsIcon : $("<div class='xqb_tg_arrow'></div>"),					//样式
							checkBox : '',														//复选框区域
							text : $("<div style='float:left;'></div>")							//文本区域
						});
						this.nodeDom.appendTo(this.dom);
						
						//创建space区域
						for(var i = 0; i < this.getDepth(); i++){
							$("<div class='xqb_tg_arrow'></div>").appendTo(this.nodeDom);
						}
						if(!this.options.leaf){
							//给行注册展开事件
							this.row.currentRow.css({'cursor':'pointer'}).click(function(){
								column.ecItem.trigger("click");
							});
							this.ecItem = $("<div class='xqb_tg_arrow xqb_tg_arrow_collapse_n'></div>");
							this.ecItem.appendTo(this.nodeDom).css({"cursor":"pointer"}).click(function(){
								if(column.row.status == 'expanded'){
									column.row.collapse();
									$(this).addClass("xqb_tg_arrow_collapse_n").removeClass("xqb_tg_arrow_expand_n");
									if($(this).hasClass("xqb_tg_arrow_expand_o")){
										$(this).addClass("xqb_tg_arrow_collapse_o").removeClass("xqb_tg_arrow_expand_o");
									}
									column.clsIcon.removeClass("xqb_folder_open");
								}else if(column.row.status == 'collapsed'){
									column.row.expand();
									$(this).addClass("xqb_tg_arrow_expand_n").removeClass("xqb_tg_arrow_collapse_n");
									if($(this).hasClass("xqb_tg_arrow_collapse_o")){
										$(this).addClass("xqb_tg_arrow_expand_o").removeClass("xqb_tg_arrow_collapse_o");
									}
									column.clsIcon.addClass("xqb_folder_open");
								}
								return false;
							}).hover(function(){
								if(column.row.status == 'collapsed'){
									$(this).addClass("xqb_tg_arrow_collapse_o");
								}else if(column.row.status == 'expanded'){
									$(this).addClass("xqb_tg_arrow_expand_o");
								}
							},function(){
								if(column.row.status == 'collapsed'){
									$(this).removeClass("xqb_tg_arrow_collapse_o");
								}else if(column.row.status == 'expanded'){
									$(this).removeClass("xqb_tg_arrow_expand_o");
								}
							});
						}else{
							$("<div class='xqb_tg_arrow'></div>").appendTo(this.nodeDom);
						}
						
						//配置iconCls的样式
						if(this.options.leaf){
							this.clsIcon.addClass("xqb_tg_leaf");
							if(this.options.iconCls){
								this.clsIcon.removeClass("xqb_tg_leaf").addClass(this.options.iconCls);
							}
						}else{
							this.clsIcon.addClass("xqb_folder_close");
						}
						this.clsIcon.appendTo(this.nodeDom);
						
						this.text.html("").appendTo(this.nodeDom);
						
						this.created = true;
						return this;
					},
					
					
					/**
					 * 获取column所在的row在tree中深度
					 */
					getDepth : function(){
						return this.options.depth ? this.options.depth : 0;
					},
					
					/**
					 * 获取当前column中的text信息
					 * @returns
					 */
					getCurrentValue : function(){
						return this.text.html();
					},
					
					
					/**
					 * 覆盖setText函数
					 * @param text
					 */
					setText : function(text){
						this.text.html(text);
						return this;
					},
					/**
					 * 覆盖setRow函数,设置tree grid 的row的ecColumn
					 * 
					 */
					setRow : function(row){
						this.row = row;
						row.ecColumn = this;
						return this;
					}
				});
				return column;
			}
			
		},
		
		/****
		 * row model
		 */
		rowModel : {
			
			/**
			 * 获取一个普通的行对象
			 */
			getRow : function(options){
				var row = {};
				$.extend(row,{
					options : $.extend({},options),
					
					columns : [],						//存储column 对象
					
					rowNumber : 0,						//行号
					/**
					 * 生成dom元素
					 * @returns {___anonymous2903_3708}
					 */
					generateDomElement : function(){
						$.extend(this,{
							dom : $("<div></div>").appendTo($(document.body)).height(24).css({
								'margin' : '0px', 'padding' : '0px', 'overflow' : 'hidden'
							}).hover(function(){
								$(this).addClass("xqb_over");
							},function(){
								$(this).removeClass("xqb_over");
							}).click(function(){
								row.selectRow();
							})
						});
						this.created = true;
						return this;
					},
					
					/**
					 * 标识当前row是title
					 * @param isTitle
					 */
					setTitleRow : function(isTitle){
						this.isTitleRow = isTitle;
						return this;
					},
					
					/**
					 * 设置自己被选中
					 */
					selectRow : function(){
						if(this.isTitleRow){
							return this;
						}
						if(this.grid.selectedRow){
							this.grid.selectedRow.unSelect();
						}
						this.grid.selectedRow = this;
						this.dom.addClass("xqb_selected");
						$(document).keydown(function(e){
							if(!row.isTitleRow){
								if(e.keyCode == 40){	//向下
									var next = row.next();
									if(next){
										next.selectRow();
										if($.browser.mozilla){
											next.dom.get(0).scrollIntoView(false);
										}else{
											next.dom.trigger("focus");
										}
									}
								}else if(e.keyCode == 38){	//向上
									var prev = row.prev();
									if(prev){
										prev.selectRow();
										if($.browser.mozilla){
											prev.dom.get(0).scrollIntoView(false);
										}else{
											prev.dom.trigger("focus");
										}
									}
									
								}
							}
							return false;
						});
						
						return this;
					},
					/**
					 * 取消row的选中状态
					 */
					unSelect : function(){
						$(document).unbind("keydown");
						this.dom.removeClass("xqb_selected");
						this.grid.selectedRow = "";
						return this;
					},
					
					/**
					 * set 奇偶行
					 */
					setEvenOdd : function(){
						if(this.rowNumber % 2 == 0){
							this.dom.removeClass("xqb_even xqb_odd").addClass("xqb_even");
						}else{
							this.dom.removeClass("xqb_even xqb_odd").addClass("xqb_odd");
						}
						return this;
					},
					/**
					 * 设置行号
					 */
					setRowNumber : function(num){
						this.rowNumber = num;
						if(this.getColumn("rowNumber")){
							this.getColumn("rowNumber").setText(num + 1);
						}
						return this.setEvenOdd();
					},
					
					/**
					 * 获取行号
					 * @returns {Number}
					 */
					getRowNumber : function(){
						return this.rowNumber ? this.rowNumber : 0;
					},
					
					/**
					 * 获取所有column的总长
					 */
					getColumnsWidth : function(){
						var width = 0;
						for(var i = 0; i < this.columns.length; i++){
							width += this.columns[i].getWidth();
						}
						return width;
					},
					
					/**
					 * 设置宽度
					 * @param width
					 */
					setWidth : function(width){
						this.dom.width(width);
						return this;
					},
					
					/**
					 * 根据别名获取当前行中列对象
					 * @param alias
					 */
					getColumn : function(alias){
						for(var i = 0; i < this.columns.length; i++){
							if(this.columns[i].getAlias() == alias){
								return this.columns[i];
							}
						}
						return "";
					},
					
					/**
					 * 添加列对象,调用该方法之前应该确保已经调用过generateDomElement方法或者show方法
					 * @param options
					 */
					addColumn : function(column){
						this.columns.push(column);
						column.setRow(this);
						column.dom.appendTo(this.dom);
						return this;
					},
					
					/**
					 * 设置数据
					 * @param data
					 */
					setData : function(data){
						this.rowData = data;
						for(var i = 0; i < this.columns.length; i++){
							var col = this.columns[i];
							var dept = col.getAlias().split(".");
							var text = data;
							var terminated = false;
							for(var j = 0; j < dept.length; j++){
								text = text[dept[j]];
								if(!text){
									col.setText("");
									col.setOriginalValue("");
									terminated = true;
									break;
								}
							}
							if(!terminated){
								col.setText(text);
								col.setOriginalValue(text);
							}
						}
						return this;
					},
					
					/**
					 * 获取当前行的数据
					 */
					getData : function(){
						return this.rowData;
					},
					
					/**
					 * 根据别名从 行数据中获取值
					 * @param alias
					 */
					getValue : function(alias){
						var depth = alias.split(".");
						var v = this.getData();
						for(var i = 0; i < depth; i++){
							v = v[depth[i]];
							if(!v){
								return "";
							}
						}
						return v;
					},
					
					/**
					 * 隐藏列,根据别名
					 */
					hideColumn : function(alias){
						
						return this;
					},
					
					/**
					 * 显示列,根据别名
					 */
					showColumn : function(alias){
						
						return this;
					},
					
					/**
					 * show row
					 */
					show : function(){
						if(!this.created){
							this.generateDomElement();
						}
						this.dom.show();
						this.visible = true;
						return this;
					},
					
					/**
					 * hide row
					 */
					hide : function(){
						this.dom.hide();
						this.visible = false;
						return this;
					},
					
					/**
					 * 判断行对象是否可见
					 * @returns
					 */
					isVisible : function(){
						return this.visible ? true : false;
					},
					
					/**
					 * check row
					 */
					check : function(){
						var column = this.getColumn("checkColumn");
						if(column){
							column.check();
							this.checked = true;
						}
						return this;
					},
					
					/**
					 * 通过触发事件来check row
					 */
					checkRow : function(){
						var column = this.getColumn("checkColumn");
						if(column){
							if(!column.isChecked()){
								column.dom.trigger("click");
							}
						}
						return this;
					},
					
					/**
					 * 设置row的check状态
					 */
					setCheck : function(status){
						this.checked = status;
						return this;
					},
					
					/**
					 * 取消行的check状态
					 */
					unCheck : function(){
						var column = this.getColumn("checkColumn");
						if(column){
							column.unCheck();
							this.checked = false;
						}
						return this;
					},
					/**
					 * 通过触发事件来unCheck row
					 */
					unCheckRow : function(){
						var column = this.getColumn("checkColumn");
						if(column){
							if(column.isChecked()){
								column.dom.trigger("click");
							}
						}
						return this;
					},
					/**
					 * 判断当前row 是否checked
					 */
					isChecked : function(){
						return this.checked ? true : false;
					},
					
					/**
					 * 存储grid对象
					 * @param grid
					 */
					setGrid : function(grid){
						this.grid = grid;
						return this;
					},
					/**
					 * 获取当前行的前一行
					 */
					prev : function(){
						if(this.getRowNumber() == 0){
							return "";
						}
						return this.grid.rowQueue[this.getRowNumber() - 1];
					},
					/**
					 * 获取当前行的下一行
					 */
					next : function(){
						if(this.getRowNumber() == this.grid.vrCount - 1){
							return "";
						}
						return this.grid.rowQueue[this.getRowNumber() + 1];
					},
					
					/**
					 * 把row 的dom元素放到el内部。
					 * @param el
					 */
					appendTo : function(el){
						this.dom.appendTo(el);
						return this;
					}
				});
				return row;
			},
			
			/**
			 * 获取一个treeGrid 的row		从普通row继承过来
			 */
			getTreeRow : function(){
				var row = this.getRow();
				$.extend(row,{
					
					rowNumber : 0,
					
					columns : [],				//列对象
					
					subRows : [],				//子行对象
					
					depth : 0,					//默认深度
					
					status : 'collapsed',		//默认是闭合的装状态
					/**
					 * 生成dom元素
					 */
					generateDomElement : function(){
						$.extend(this,{
							
							//dom和子行的容器
							dom : $("<div></div>").appendTo($(document.body)).css({'cursor':'default'}),
							
							//当前行的容器
							currentRow : $("<div></div>").appendTo($(document.body)).height(23).css({
								'margin' : '0px', 'padding' : '0px', 'overflow' : 'hidden','border-bottom' : '1px solid #EDEDED',
								'background-color':'#FDFDFD'
							}).hover(function(){
								$(this).addClass("xqb_selected");
							},function(){
								$(this).removeClass("xqb_selected");
							}),
							
							//子行的容器
							subRowDom : ''
							
						});
						
						this.currentRow.appendTo(this.dom);
						
						this.created = true;
						return this;
					},
					
					//获取row深度
					getDepth : function(){
						return this.depth;
					},
					
					//设置row深度
					setDepth : function(depth){
						this.depth = depth;
					},
					
					/**
					 * set 奇偶行
					 */
					setEvenOdd : function(){
						return this;
					},
					
					/**
					 * 设置宽度
					 * @param width
					 */
					setWidth : function(width){
						this.dom.width(width);
						this.currentRow.width(width);
						if(this.subRowDom){
							this.subRowDom.width(width);
						}
						return this;
					},
					/**
					 * 添加列对象,调用该方法之前应该确保已经调用过generateDomElement方法或者show方法
					 * @param options
					 */
					addColumn : function(column){
						this.columns.push(column);
						column.setRow(this);
						column.dom.appendTo(this.currentRow);
						return this;
					},
					
					/****
					 * 添加子行
					 * @param r
					 */
					addSubRow : function(r){
						this.subRows.push(r);
						r.setDepth(this.getDepth() + 1);
						return this;
					},
					
					/**
					 * 第一次展开时调用
					 */
					firstExpand : function(){
						if(this.subRowData.length == 0){
							return;
						}
						this.subRowDom = $("<div></div>").appendTo($(document.body));
						for(var i = 0; i < this.subRowData.length; i++){
							var leaf = false;
							if(!this.grid.options.treeGrid.url){				//本地数据通过subRow属性判断
								if(!this.subRowData[i].subRow){
									leaf = true;
								}
							}else{												//远程数据直接指出
								leaf = this.subRowData[i].leaf;
							}
							this.addSubRow(this.grid.generateTreeGridRow(leaf,this.getDepth() + 1).show().setData(this.subRowData[i]).appendTo(this.subRowDom));
						}
						this.subRowsCreated = true;
						row.status = "expanded";
						this.subRowDom.appendTo(this.dom).hide();
						this.subRowDom.slideDown(100,function(){
							row.status = "expanded";
							if(row.grid.options.treeGrid.afterFirstExpand && $.isFunction(row.grid.options.treeGrid.afterFirstExpand)){
								row.grid.options.treeGrid.afterFirstExpand.call(this,row,row.getEcColumn(),row.grid);
							}
						});
					},
					
					/***
					 * 展开行，第一次展开的时候检查是否创建过子行，如果没有则创建子行
					 */
					expand : function(){
						if(this.status != 'collapsed'){				//判断当前row的折叠、闭合状态
							return this;
						}
						this.status = "expanding";
						if(!this.subRowsCreated){
							if(row.grid.options.treeGrid.beforeFirstExpand && $.isFunction(row.grid.options.treeGrid.beforeFirstExpand)){
								row.grid.options.treeGrid.beforeFirstExpand.call(this,row,row.getEcColumn(),row.grid);
							}
							if(this.grid.options.treeGrid.url){
//								var dt = {};
								var rData = this.getValue(this.grid.options.treeGrid.alias);	
								rData[this.grid.options.treeGrid.keyFieldName] = rData[this.grid.options.treeGrid.alias];
								$.ajax({
									url : this.grid.options.treeGrid.url,
									async : true,
									data : rData,
									type : 'post',
									error : function(data){
										if(row.grid.options.treeGrid.error && $.isFunction(row.grid.options.treeGrid.error)){
											row.grid.options.treeGrid.error.call(this,"数据请求错误：" + data.statusText);
											var col = row.getEcColumn();
											col.options.leaf = true;
											col.ecItem.removeClass("xqb_tg_arrow_expand_n xqb_tg_arrow_expand_o xqb_tg_arrow_collapse_n xqb_tg_arrow_collapse_o");
											col.clsIcon.removeClass("xqb_folder_close xqb_folder_open")
												.addClass("xqb_tg_leaf");
											if(row.grid.options.treeGrid.afterFirstExpand && $.isFunction(row.grid.options.treeGrid.afterFirstExpand)){
												row.grid.options.treeGrid.afterFirstExpand.call(this,row,col,row.grid);
											}
											return;
										}
									},
									success : function(data){
										var key = row.grid.options.treeGrid.result ? data[row.grid.options.treeGrid.result] : data.result;
										var value = row.grid.options.treeGrid.failed ? row.grid.options.treeGrid.failed : 'failed';
										var msg = row.grid.options.treeGrid.reason ? data[row.grid.options.treeGrid.reason] : data.reason;
										if(key == value){
											if(row.grid.options.treeGrid.error && $.isFunction(row.grid.options.treeGrid.error)){
												row.grid.options.treeGrid.error.call(this,msg);
												var col = row.getEcColumn();
												col.options.leaf = true;
												col.ecItem.removeClass("xqb_tg_arrow_expand_n xqb_tg_arrow_expand_o xqb_tg_arrow_collapse_n xqb_tg_arrow_collapse_o");
												col.clsIcon.removeClass("xqb_folder_close xqb_folder_open")
													.addClass("xqb_tg_leaf");
												if(row.grid.options.treeGrid.afterFirstExpand && $.isFunction(row.grid.options.treeGrid.afterFirstExpand)){
													row.grid.options.treeGrid.afterFirstExpand.call(this,row,col,row.grid);
												}
												return;
											}
										}
										var depth = row.grid.options.treeGrid.root.split(".");
										for(var i = 0; i < depth.length; i++){ 
											data = data[depth[i]];
										}
										row.subRowData = data;
										if(data.length){
											row.firstExpand();
										}else{
											//如果没有子节点则强制转换为叶子节点
											var col = row.getEcColumn();
											col.options.leaf = true;
											col.ecItem.removeClass("xqb_tg_arrow_expand_n xqb_tg_arrow_expand_o xqb_tg_arrow_collapse_n xqb_tg_arrow_collapse_o");
											col.clsIcon.removeClass("xqb_folder_close xqb_folder_open")
												.addClass("xqb_tg_leaf");
											if(row.grid.options.treeGrid.afterFirstExpand && $.isFunction(row.grid.options.treeGrid.afterFirstExpand)){
												row.grid.options.treeGrid.afterFirstExpand.call(this,row,col,row.grid);
											}
										}
									}
								});
								return this;
							}else{
								this.subRowData = this.getData().subRow;
								this.firstExpand();
								return this;
							}
						}else{
							this.subRowDom.slideDown(100,function(){
								row.status = "expanded";
							});
						}
						return this;
					},
					
					/***
					 * 折叠
					 */
					collapse : function(){
						if(this.subRowsCreated){
							if(this.status != 'expanded'){
								return this;
							}
							this.status = "collapsing";
							this.subRowDom.slideUp(100,function(){
								row.status = "collapsed";
							});
						}
						return this;
					},
					/**
					 *	获取tree grid 的row中的折叠列
					 */
					getEcColumn : function(){
						return this.ecColumn;
					}
				});
				
				return row;
			}
		},
		
		/**
		 * 获取grid
		 * @param options
		 */
		getGrid : function(options){
			$.extend(options,{
				/**
				 * beforeShow 事件：生成grid的dom元素		在xqbBox的show方法中触发
				 */
				beforeShow : function(grid){
					/**
					 * 如果grid 的dom已经创建好了就直接显示
					 */
					if(this.gridDomCreated){
						return;
					}
					
					//show前渲染title，show后渲染内容

					if(grid.options.tbar){
						grid.generateTbar(this.options.tbar);
					}
					grid.generateTitleRow();
					grid.generateContentBody();
					if(grid.options.bbar){
						grid.generateBbar(this.options.bbar);
					}
					//生成蒙层
					grid.getShadowMask();
					grid.gridDomCreated = true;
				},
				/**
				 * afterShow 事件：加载数据,调整body尺寸
				 */
				afterShow : function(grid){
					if(grid.options.autoLoad == true && !grid.loaded){
						grid.loadData();
					}
				},
				
				/**
				 * xqbBox layout 布局以后 调整grid的组件的布局
				 */
				afterLayout : function(grid){
					this.gridBody.width(grid.body.width());		//调整grid body宽度
					grid.titleRowBar.width(grid.body.width());	//调整标题bar的宽度
					
					//调整grid body高度
					grid.gridBody.height(grid.body.height()  - 24);
					
					//调整分页组件的容器宽度 以及grid body高度
					if(grid.bbarContainer){
						grid.bbarContainer.width(grid.body.width());
						grid.gridBody.height(grid.gridBody.height() - grid.bbarContainer.outerHeight(true));
					}
					
					//调整tbar的宽度以及grid body高度
					if(grid.tbarContainer){
						grid.tbarContainer.width(grid.body.width());
						grid.gridBody.height(grid.gridBody.height() - grid.tbarContainer.outerHeight(true));
					}
					
					//调整蒙层尺寸
					if(grid.loadingLayer.created){
						grid.loadingLayer.doLayout();
					}
					//调整mask位置
					if(grid.mask.created){
						grid.mask.doLayout();
					}
					grid.gridBody.trigger("scroll");
					
					//自适应column
					grid.autoExpandColumn();
				}
			});
			var grid = $("<div></div>").css({'overflow' : 'hidden'}).appendTo($(document.body)).xqbBox(options);
			
			/***
			 * 扩展grid方法
			 */
			$.extend(grid,{
				/***
				 * 行对象管理器,管理所有的内容行
				 */
				rowQueue : [],
				
				//可见的row的count
				vrCount : 0,
				
				/**
				 * 获取所有的row
				 */
				getAllRows : function(){
					return this.rowQueue;
				},
				
				/**
				 * 隐藏当前所有的列对象,取消他们的check状态以及selected状态
				 */
				hideAllRows : function(){
					for(var i = 0; i < this.rowQueue.length; i++){
						this.rowQueue[i].hide();
					}
					return this;
				},
				
				/**
				 * 获取被checked的row
				 */
				getCheckedRows : function(){
					var rows = [];
					for(var i = 0; i < this.rowQueue.length; i++){
						if(this.rowQueue[i].isChecked()){
							rows.push(this.rowQueue[i]);
						}
					}
					return rows;
				},
				
				/**
				 * 返回被clicked的row
				 * @returns
				 */
				getSelectedRow : function(){
					return this.selectedRow;
				},
				
				/**
				 * uncheck 所有的row 包括不可见的row
				 */
				uncheckRows : function(){
					for(var i = 0; i < this.rowQueue.length; i++){
						if(this.rowQueue[i].isChecked()){
							this.rowQueue[i].unCheck();
						}
					}
					return this;
				},
				
				/**
				 * 自动调节column 以撑满gridbody,原本设置的width 将失去效用
				 */
				autoExpandColumn : function(){
					// 如果没有配置该列 或者 配置别名错误 都直接返回
					if(!this.options.autoExpandColumn || !this.titleRow.getColumn(this.options.autoExpandColumn)){
						return this;
					}
					//获取当前title的宽度
					var wd = this.titleRow.getColumnsWidth();
					
					var column = this.titleRow.getColumn(this.options.autoExpandColumn);
					var autoWd = this.gridBody.width() - wd + column.getWidth();
					autoWd = autoWd > 80 ? autoWd : 80;
					column.setWidth(autoWd);
					return this;
				},
				
				/**
				 * 只check 所有可见的row
				 */
				checkRows : function(){
					for(var i = 0; i < this.rowQueue.length; i++){
						if(this.rowQueue[i].isVisible() && !this.rowQueue[i].isChecked()){
							this.rowQueue[i].check();
						}
					}
					return this;
				},
				
				/**
				 * 检测是否所有的 可见的row 都被checked
				 */
				isAllRowsChecked : function(){
					for(var i = 0; i < this.rowQueue.length; i++){
						if(this.rowQueue[i].isVisible() && !this.rowQueue[i].isChecked()){
							return false;
						}
					}
					return true;
				},
				
				/**
				 * 获取一个内容行对象，先从队列中查找不可见的row，如果没有则生成一个
				 */
				getContentRow : function(leaf){
					var row = "";
					//tree grid
					if(this.options.treeGrid){						
						row = this.generateTreeGridRow(leaf);
						row.appendTo(this.gridBody);
					}else{
						row = this.generateContentRow();
					}
					return row;
				},
				
				/**
				 * 生成title的行对象
				 */
				generateTitleRow : function(){
					this.titleRow = $.xqb9528.grid.rowModel.getRow().show();
					this.titleRow.setTitleRow(true);		// 标识当前row是title
					//渲染row number column
					if(this.options.rowNumber){
						var column = $.xqb9528.grid.columnModel.getTitleRowNumberColumn($.xqb9528.grid.RowNumber());
						this.titleRow.addColumn(column);
					}
					
					//渲染check box number column
					if(!this.options.treeGrid && this.options.checkBox){
						var cb = $.xqb9528.grid.CheckColumn();
						$.extend(cb,{
							checkModel : this.options.checkBox.singleModel ? 'single' : '',
							afterClick : function(col, isChecked, gridObject){
								if(isChecked){
									gridObject.checkRows();
								}else{
									gridObject.uncheckRows();
								}
							}		
						});
						var column = $.xqb9528.grid.columnModel.getTitleCheckColumn(cb);
						column.setGrid(this);
						this.titleRow.addColumn(column);
					}
					
					for(var i = 0; i < this.options.columnModel.length; i++){
						var op = $.extend({
							//注册当column调整以后触发的事件
							afterLayout : function(gridObj,colObj){
								
								//调整title 所在的 div 的宽度
								var wd = gridObj.titleRow.getColumnsWidth();
								gridObj.titleRow.setWidth(wd + 2000);
								
								//调整当前列的所有div 的宽度 以及所有内容行的宽度
								for(var i = 0; i < gridObj.rowQueue.length; i++){
									gridObj.rowQueue[i].setWidth(wd);
									gridObj.rowQueue[i].getColumn(colObj.getAlias()).setWidth(colObj.getWidth());
								}
								if(gridObj.sbar){
									gridObj.sbar.width(wd);
								}
								if(gridObj.gridBody){
									gridObj.gridBody.trigger("scroll");
								}
							},
							
							//注册排序事件
							sort : function(gridObj,colObj){
								//如果当前排序的列不是自己则升序排列,否则进行倒序
								if(colObj.getAlias() != gridObj.getSortAlias()){
									gridObj.sortAsc(colObj.getAlias());
								}else{
									gridObj.reverse();
								}
							}
						},this.options.columnModel[i]);
						
						var column = $.xqb9528.grid.columnModel.getTitleColumn(op);
						column.setGrid(this);
						this.titleRow.addColumn(column.show());
					}
					//调整宽度
					this.titleRow.setWidth(this.titleRow.getColumnsWidth() + 2000);
					
					this.titleRowBar = $("<div></div>").css({'overflow':'hidden'}).appendTo(this.options.body).height(24);
					
					this.titleRow.dom.addClass("xqb_grid_title_n").appendTo(this.titleRowBar);
					return this.titleRow;
				},
				
				/**
				 * 生成一个行对象
				 */
				generateContentRow : function(){
					var row;
					row = $.xqb9528.grid.rowModel.getRow().show();
					row.setGrid(this);
					//渲染row number column
					if(this.options.rowNumber){
						var column = $.xqb9528.grid.columnModel.getContentRowNumberColumn($.xqb9528.grid.RowNumber());
						row.addColumn(column);
					}
					
					//渲染check box number column
					if(this.options.checkBox){
						var cb = $.xqb9528.grid.CheckColumn();
						$.extend(cb,{
							checkModel : this.options.checkBox.singleModel ? 'single' : '',
							beforeClick : function(col, isChecked, gridObject){
								if(!isChecked){
									if(gridObject.options.checkBox.singleModel){
										gridObject.uncheckRows();
									}
								}
							},
							afterClick : function(col, isChecked, gridObject){
								if(!gridObject.options.checkBox.singleModel){
									if(isChecked){
										if(gridObject.isAllRowsChecked()){
											gridObject.titleRow.check();
										}
									}else{
										gridObject.titleRow.unCheck();
									}
								}
							}
						});
						var column = $.xqb9528.grid.columnModel.getContentCheckColumn(cb);
						column.setGrid(this);
						row.addColumn(column);
					}
					
					for(var i = 0; i < this.options.columnModel.length; i++){
						var column;
						column = $.xqb9528.grid.columnModel.getContentColumn(this.options.columnModel[i]);
						column.setGrid(this);
						column.setRow(row);
						row.addColumn(column.show().setWidth(this.titleRow.getColumn(this.options.columnModel[i].alias).getWidth()));
					}
					//调整行宽度
					row.setWidth(row.getColumnsWidth());
					
					row.appendTo(this.gridBody);
					
					this.rowQueue.push(row);						//存放到队列当中
					
					return row;
				},
				/**
				 * 生成tree grid row
				 * leaf ：叶行或者是子行,
				 * 行的深度
				 */
				generateTreeGridRow : function(leaf,depth){
					var row;
					row = $.xqb9528.grid.rowModel.getTreeRow().show();
					row.setGrid(this);
					//渲染row number column
					if(this.options.rowNumber){
						var column = $.xqb9528.grid.columnModel.getContentRowNumberColumn($.xqb9528.grid.RowNumber());
						row.addColumn(column);
					}
					for(var i = 0; i < this.options.columnModel.length; i++){
						var column;
						if(this.options.columnModel[i].ecColumn){
							$.extend(this.options.columnModel[i],{
								leaf : leaf,
								depth : depth ? depth : 0
							});
							column = $.xqb9528.grid.columnModel.getTreeECColumn(this.options.columnModel[i]);			//折叠列
						}else{
							column = $.xqb9528.grid.columnModel.getTreeColumn(this.options.columnModel[i]);				//非折叠列
						}
						column.setGrid(this);
						column.setRow(row);
						row.addColumn(column.show().setWidth(this.titleRow.getColumn(this.options.columnModel[i].alias).getWidth()));
					}
					
					row.setWidth(row.getColumnsWidth());
					this.rowQueue.push(row);						//存放到队列当中
					return row;
				},
				/**
				 * 生成存放grid内容的body, 该函数在第一次show以后被调用
				 */
				generateContentBody : function(){
					$.extend(this,{
						gridBody : $("<div></div>").appendTo(this.options.body).css({
							'overflow' : 'auto', 'background' : 'transparent', 'margin' : '0px', 'padding':'0px' 
						}).scroll(function(){
							grid.titleRowBar.get(0).scrollLeft = $(this).scrollLeft();
						}),
						/**
						 * 用来在没有内容时撑出滚动条
						 */
						sbar : $("<div></div>").css({'background' : 'transparent',
							'margin-top' : '-100px'}).appendTo($(document.body)).height(100)
					});
					this.gridBody.width(this.options.body.width()).height(100);
					this.sbar.appendTo(this.gridBody).width(this.titleRow.getColumnsWidth());
					return this;
				},
				
				/**
				 * 数据加载前
				 */
				beforeLoading : function(){
					this.loadingLayer.show();				//显示蒙层
					this.mask.show();						//显示laoding mask
					this.titleRow.unCheck();				//取消title row的check状态
					this.setSortAlias("");					//清除当前排序的信息
					this.rowQueue = [];
					this.gridBody.empty();
				},
				/**
				 * 数据加载后
				 */
				afterLoading : function(){
					this.autoExpandColumn();
					this.loadingLayer.hide();
					this.mask.hide();
					this.gridBody.trigger("scroll");
				},
				/***
				 * 生成分页组件
				 */
				generateBbar : function(op){
					//扩展数据显示的回调函数
					var options = {};
					$.extend(options,op);
					$.extend(options,{
						displayData : function(data){
							grid.beforeLoading();
							grid.setStore(data);
							grid.afterLoading();
						},
						//异常处理函数
						error : function(msg){
							$.xqb9528.globalMsg.showMsg(msg);
						},
						beforeLoadingData : function(){	
							//蒙层调整到点击按钮后立刻显示，防止ajax请求时无响应就不出现蒙层了
							grid.loadingLayer.show();				//显示蒙层
							grid.mask.show();						//显示laoding mask
						},
						afterLoadingData : function(){
							
						}
					});
					
					this.pagination = $.xqb9528.pagination.getPagination(options);
					$.extend(this,{
						bbarContainer : $("<div></div>").addClass("xqb_defaultBorder xqb_grid_bbar").css({
							'border-right' : '0px', 'border-left' : '0px', 'border-bottom' : '0px',
							'margin' : '0px', 'padding' : '0px', 'overflow' : 'hidden' 
						})
					});
					this.pagination.dom.appendTo(this.bbarContainer).css("margin-top","3px");
					this.bbarContainer.appendTo(this.options.body);
					return this.pagination;
				},
				/**
				 * 生成导航条
				 */
				generateTbar : function(options){
					$.extend(this,{
						tbarContainer : $("<div></div>").addClass("xqb_defaultBorder xqb_grid_bbar").css({
							'border-right' : '0px', 'border-left' : '0px', 'border-top' : '0px',
							'margin' : '0px', 'padding' : '0px', 'overflow' : 'hidden' 
						}).height(27)
					});
					this.tbarContainer.appendTo(this.options.body);
				},
				
				/**
				 * 获取tbar的容器
				 * @returns
				 */
				getTopBarContainer : function(){
					return this.tbarContainer ? this.tbarContainer : null;
				},
				
				/**s
				 * 设置数据源
				 * @param store
				 */
				setStore : function(store){
					if(!this.gridDomCreated){
						this.options.store = store;
						return this;
					}
					if(!this.options.bbar){
						this.beforeLoading();
					}
					for(var i = 0; i < store.length; i++){
						var leaf = "false";
						if(this.options.treeGrid){
							if(!this.options.treeGrid.url){				//本地数据通过subRow属性判断
								if(!store[i].subRow){
									leaf = true;
								}
							}else{										//远程数据直接指出
								leaf = store[i].leaf;
							}
						}
						var row = this.getContentRow(leaf == "false" ? false : true);
						row.show();
						row.setData(store[i]).setRowNumber(i);
					}
					this.vrCount = store.length;
					
					if(!this.options.bbar){
						this.afterLoading();
					}
					return this;
				},
				
				/**
				 * sort by column,按照当前列中显示的内容 从小到大排序, 快速排序法
				 * @param alias
				 */
				sortAsc : function(alias){
					this.setSortAlias(alias);
					for(var i = 0; i < this.vrCount; i++){
						var flag = false;
						for(var j = 0; j < this.vrCount - i - 1; j++){
							if(this.rowQueue[j].getColumn(alias).getCurrentValue() >  this.rowQueue[j + 1].getColumn(alias).getCurrentValue()){
								this.exchange(j,j + 1);
								flag = true;
							}
						}
						if(!flag){
							break;
						}
					}
					return this;
				},
				
				
				/**
				 * 设置当前排序的column 的别名
				 * @returns
				 */
				setSortAlias : function(alias){
					this.sortAlias = alias;
					return this;
				},
				
				/**
				 * 获取当前排序的column 的别名
				 * @returns
				 */
				getSortAlias : function(){
					return this.sortAlias;
				},
				
				/**
				 * 倒序排列
				 */
				reverse : function(alias){
					for(var i = 0; i < this.vrCount / 2; i++){
						this.exchange(i,this.vrCount - i - 1);
					}
					return this;
				},
				
				/**
				 * 改变url
				 */
				setURL : function(url){
					if(this.pagination){
						this.pagination.setURL(url);
					}else{
						this.options.bbar.url = url;
					}
					return this;
				},
				
				getURL : function(){
					if(this.pagination){
						return this.pagination.getURL();
					}else{
						return this.options.bbar.url;
					}
				},
				
				/**
				 * 设置参数
				 * @param param
				 */
				setExtraParam : function(param){
					this.pagination.setExtraParameter(param);
					return this;
				},
				/**
				 * 刷新当前页
				 */
				refresh : function(){
					if(!grid.gridDomCreated){
						return this;
					}
					this.pagination.refresh();
					return this;
				},
				/**
				 * 跳转到第一页
				 */
				first : function(){
					if(!grid.gridDomCreated){
						return this;
					}
					this.pagination.first();
					return this;
				},
				/**
				 * 跳转到最后一页
				 */
				last : function(){
					if(!grid.gridDomCreated){
						return this;
					}
					this.pagination.last();
					return this;
				},
				
				/**
				 * 强制最后一页，添加数据后希望跳转到最后一页推荐使用该方法。
				 */
				forceLast : function(){
					if(!grid.gridDomCreated){
						return this;
					}
					if(this.pagination.totalSize % this.pagination.pageSize == 0){
						this.pagination.forceIndex(this.pagination.pageCount + 1);
					}else{
						if(this.pagination.pageIndex == this.pagination.pageCount - 1){
							this.refresh();
						}else{
							this.last();
						}
					}
					return this;
				},
				
				/**
				 * 使得grid自动布局,必须在grid show以后执行
				 * padding : 离4周的距离
				 */
				autoExpand : function(padding){
					var p = padding ? padding : 10;
					$.extend(this.options,{
						
						/**
						 * 布局前置事件
						 */
						beforeLayout : function(grid){
							var wnd = $(window);
							grid.dom.css({
								'left' : p + 'px', 'top' : p + 'px'
							});
							grid.setSize({width : wnd.width() - 2 * p, height : wnd.height() - 2 * p});
						}
					});
					
					$.xqb9528.registerLayout(this);
					
					return this;
				},
				
				/**
				 * 交换行i 和 j的位置
				 */
				exchange : function(i,j){
					if(i == j){
						return;
					}
					//交换dom位置
					if(this.rowQueue[j].getRowNumber() > this.rowQueue[i].getRowNumber()){	//R2的dom元素的位置在R1的dom元素的后面.
						var pre = this.rowQueue[j].prev();
						this.rowQueue[j].dom.insertBefore(this.rowQueue[i].dom);
						if(pre != this.rowQueue[i]){
							this.rowQueue[i].dom.insertAfter(pre.dom);
						}
					}else{
						var pre = this.rowQueue[i].prev();
						this.rowQueue[i].dom.insertBefore(this.rowQueue[j].dom);
						if(pre != this.rowQueue[j]){
							this.rowQueue[j].dom.insertAfter(pre.dom);
						}
					}
					//rowQueue中的数据的位置交换
					var temp = this.rowQueue[i];
					var rn1 = this.rowQueue[i].getRowNumber();
					var rn2 = this.rowQueue[j].getRowNumber();
					this.rowQueue[i] = this.rowQueue[j];
					this.rowQueue[i].setRowNumber(rn1);
					this.rowQueue[j] = temp;
					this.rowQueue[j].setRowNumber(rn2);
				},
				
				/**
				 * 创建grid 的阴影蒙层,以及loading mask
				 */
				getShadowMask : function(){
					var layer = $.xqb9528.shadowLayer.getLayer();
					$.extend(layer,{
						
						/**
						 * 覆盖$.xqb9528.shadowLayer提供的默认doLayout 方法
						 */
						doLayout : function(){
							if(this.dom.css("display") == "none"){
								return this;
							}
							grid.frame.addClass("relative");
							this.dom.css({
								"width" : grid.options.body.width() + 2,
								"height" : grid.options.body.height() + 2,
								'position' : 'absolute','left' : '6px', 'top' : '6px'
							});
							this.setzIndex(this.zIndex);
							if($.browser.msie && $.browser.version == "6.0"){
								this.dom.children("iframe").width(this.dom.width()).height(this.dom.height());
							}
							/**
							 * 第一次layout的时候把layer的dom放入grid中
							 */
							if(!this.layerAppended){
								this.dom.appendTo(grid.frame);
								this.layerAppended = true;
							}
							return this;
						},
						
						/**
						 * 覆盖hide函数
						 */
						hide : function(){
							if(!this.created){
								return this;
							}
							this.dom.animate({
								opacity : 'hide'
							},500);
							return this;
						}
					});
					this.loadingLayer = layer;
					
					//获取mask
					var mask = $.xqb9528.shadowLayer.mask(grid.options.loadingMask ? grid.options.loadingMask : '数据加载中,请稍后....');
					
					/***
					 * 覆盖重写 show hide doLayout函数
					 */
					$.extend(mask,{
						
						//显示mask
						show : function(){
							if(this.dom && this.dom.css("display") == "block"){
								return this;
							}
							if(!this.created){
								this.generateDomElement();
							}
							this.dom.show();
							this.doLayout();
							return this;
						},
						
						//hide mask
						hide : function(){
							if(!this.created){
								return this;
							}
							this.dom.animate({
								opacity : 'hide'
							},500);
							return this;
						},
						
						//重新布局
						doLayout : function(){
							if(this.dom.css("display") == "none"){
								return this;
							}
							/**
							 * 第一次layout的时候把layer的dom放入grid中
							 */
							if(!this.maskAppended){
								this.dom.appendTo(grid.frame);
								this.maskAppended = true;
							}
							this.setzIndex(grid.loadingLayer.zIndex + 1);
							this.center();
							return this;
						},
						
						/**
						 * 居中摆放mask
						 */
						center : function(){
							this.dom.css({
								'left' : ((grid.frame.outerWidth(true) - this.dom.outerWidth(true)) / 2) + "px",
								'top' : (grid.body.outerHeight(true) - this.dom.outerHeight(true) + 2) / 2 + 'px'
							});
							return this;
						}
					
					});
					
					this.mask = mask;
					
					return this;
				},
				/**
				 * 加载数据
				 */
				loadData : function(){
					
					//如果有分页组件,则从分页组件获取数据，否则直接从store中获取数据
					if(this.options.bbar){
						this.pagination.loadData();
					}else{
						this.hideAllRows();	
						this.titleRow.unCheck();
						this.setStore(this.options.store);
					}
					
					this.loaded = true;						//修改标识符，表明grid已经加载过数据，第一次显示时有用
					return this;
				}
			});
			
			return grid;
		}
			
	});
})(jQuery);
