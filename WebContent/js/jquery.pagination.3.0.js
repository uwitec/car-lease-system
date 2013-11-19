/***********************************************************************************
 * 
 * @FILE		jquery.pagination.js
 * @AUTHOR		9528
 * @DATE		2011-8-5
 * @COPY-RIGHT	大庆金桥成都分公司
 * 
 * @TODO		1、创建jquery 分页控件
 * 				2、增加了对假分页的支持，动态真分页时必须指定url参数，否则认为是假分页
 * 			
 * 
 ***********************************************************************************/

(function($){
	if(!$.xqb9528){
		$.xqb9528 = {};
	}
	$.extend($.xqb9528,{
		pagination:{}						//创建命名空间
	});
	
	$.extend($.xqb9528.pagination,{
		options:{
			defaultWidth : 400				//默认宽度
		},
		
		/*************************************************************************************
		*
		*	@DATE 		2011-3-1
		*	@TODO		检查输入的数据是否是数字
		*
		*************************************************************************************/
		checkNumber : function(str){
			str = $.trim(str);
			var len = str.length;
			for(var i = 0; i < len; i++){					//去掉首位的0字符
				if(str.substr(0,1) == "0"){
					str = str.substring(1,str.length);
				}
			}
			if(!str)
				return "";
			var num = "0123456789";
			for(var i = 0; i < str.length; i++){
				if(num.indexOf(str.substring(i,i+1)) == "-1"){
					return "";
				}
			}
			return str;
		},
		
		/******
		 * 获取一个分页控件对象
		 * @param options
		 * @returns {___anonymous1711_1712}
		 */
		getPagination : function(options){
			var pgt = {};
			options.totalCount = options.totalCount ? options.totalCount : 0;
			$.extend(pgt,{
				options : options,
				
				/**
				 * 生成dom元素
				 */
				generateDomElement : function(){
					$.extend(this,{
						pageIndex : 0,
						pageSize : this.options.pageSize,
						
						dom : $("<div></div>").appendTo($(document.body)).css({
							"height":"24px", "padding":"0px", "margin":"0px",
							"overflow":"hidden","line-height":"24px", "font-size":"13px"
						}).addClass("xqb_default_font"),
						tableDiv : $("<div style='float:left;overflow:hidden;'></div>").appendTo($(document.body)).css({
							"width": $.xqb9528.pagination.options.defaultWidth + "px"
						}),
						table : $("<table style='float:left; margin:0px; overflow:hidden;' border='0' cellspacing='0' cellpadding='0' height='24'>" + "</table>").appendTo($(document.body)).get(0)
					});
					this.options.totalCount = 0;
					
					this.tableDiv.appendTo(this.dom);
					$(this.table).appendTo(this.tableDiv);
					var row = this.addRow(this.table);
					
					var col = this.addColumn(row);
					
					$(col).css("width","4px");				//空白区域
					
					col = this.addColumn(row);	//首页
					this.firstDom = $("<input type='button' title='首页' class='xqb_pagination xqb_first' />").appendTo(col);
					
					col = this.addColumn(row); //上一页
					this.previousDom = $("<input type='button' title='上一页' class='xqb_pagination xqb_prev' />").appendTo(col);
					
					col = this.addColumn(row);
					$("<div class='xqb_pagination_label'>共</div>").appendTo(col);
					
					var pages = Math.ceil(options.totalCount/options.pageSize);
					pages = pages == 0 ? 1 : pages;
					this.pageCount = pages;
					
					col = this.addColumn(row); //页数
					this.pageCountDom = $("<div class='xqb_pagination_label'></div>").appendTo(col).html(pages);

					col = this.addColumn(row);
					$("<div class='xqb_pagination_label'>页,&nbsp;当前是第&nbsp;</div>").appendTo(col);
					
					col = this.addColumn(row); //页码
					this.pageIndexDom = $("<input type='text' style='width:30px; height:14px;' />").appendTo(col).val(1);
					
					col = this.addColumn(row);
					$("<div class='xqb_pagination_label'>&nbsp;页,&nbsp;共</div>").appendTo(col);
					
					col = this.addColumn(row); //记录条数
					this.recordCountDom = $("<div class='xqb_pagination_label'></div>").appendTo(col).html(options.totalCount);
					
					col = this.addColumn(row);
					$("<div class='xqb_pagination_label'>条记录</div>").appendTo(col);
					
					col = this.addColumn(row);	//下一页
					this.nextDom = $("<input type='button' title='下一页' class='xqb_pagination xqb_next' />").appendTo(col);
					
					col = this.addColumn(row); //尾页
					this.lastDom = $("<input type='button' title='尾页' class='xqb_pagination xqb_last' />").appendTo(col);
					
					col = this.addColumn(row); //刷新
					this.refreshDom = $("<input type='button' title='刷新' class='xqb_pagination xqb_refresh' />").appendTo(col);
					
					//右边的text区域
					this.rightTextDom = $("<span style='float:right; margin-right:6px;' class='xqb_pagination_label'></span>").appendTo(this.dom);
					
					this.firstDom.click(function(){
						pgt.first();
					});
					
					this.previousDom.click(function(){
						pgt.prev();
					});
					
					this.nextDom.click(function(){
						pgt.next();
					});
					
					this.lastDom.click(function(){
						pgt.last();
					});
					
					this.refreshDom.click(function(){
						pgt.refresh();
					});
					
					this.pageIndexDom.change(function(){
						pgt.setIndex(pgt.pageIndexDom.val());
					}).click(function(){
						$(this).trigger("focus");
					}).keyup(function(e){
						$(this).val($(this).val().replace(/\D/g,''));
						if(e.keyCode == 13){
							$(this).trigger("change");
						}
					});
					
					this.validateDom();
					
					this.baseParameter = {
						pageIndex : this.pageIndex,
						pageSize : this.options.pageSize
					};
					this.setStore(this.options.store);
					
					return this;
				},
				
				/**
				 * 第一页
				 */
				first : function(){
					this.setIndex(1);
					return this;
				},
				/**
				 * 前一页
				 */
				prev : function(){
					this.setIndex(this.pageIndex);
					return this;
				},
				/**
				 * 下一页
				 */
				next : function(){
					this.setIndex(this.pageIndex + 2);
					return this;
				},
				/**
				 * 最后一页
				 */
				last : function(){
					this.setIndex(this.pageCount);
					return this;
				},
				/***
				 * 刷新
				 */
				refresh : function(){
					this.loadData();
					return this;
				},
				/**
				 * 设置页码,从1开始
				 */
				setIndex : function(index){
					/**
					 * 页码相同则不再处理
					 */
					index = parseInt(index);
					if((index - 1) == this.pageIndex){
						return this;
					}
					/**
					 * 如果是非法输入，则恢复到上次的正确输入
					 */
					if(index > this.pageCount || index < 1){
						if(this.pageIndex < this.pageCount){
							this.pageIndexDom.val(this.pageIndex + 1);
						}else{
							this.last();
						}return this;
					}
					this.pageIndex = index - 1;
					this.pageIndexDom.val(index);
				//	this.validateDom();
					this.loadData();
					return this;
				},
				
				/**
				 * 强制请求指定页
				 */
				forceIndex : function(index){
					this.pageIndex = index - 1;
					this.loadData();
					return this;
				},
				
				/**
				 * 重新设置条数
				 * @param total
				 */
				setTotal : function(total){
					this.recordCountDom.html(total);
					this.totalSize = total;
					var pages = Math.ceil(total/this.options.pageSize);
					pages = pages == 0 ? 1 : pages;
					this.pageCount = pages;
					this.pageCountDom.html(this.pageCount);
					this.recordCountDom.html(total);
					/**
					 * 如果重新加载后 页数小于当前页码，则重新加载最后一页的数据
					 */
					if(this.pageIndex >= this.pageCount){
						this.last();
					}
					return this;
				},
				
				/**
				 * 渲染dom
				 */
				validateDom : function(){ 
					if(this.pageIndex == 0){
						this.firstDom.addClass("xqb_first_disabled").attr("disabled","disabled");
						this.previousDom.addClass("xqb_prev_disabled").attr("disabled","disabled");
					}else{
						this.firstDom.removeClass("xqb_first_disabled").attr("disabled","");
						this.previousDom.removeClass("xqb_prev_disabled").attr("disabled","");
					}
					if(this.pageIndex == this.pageCount - 1){
						this.nextDom.addClass("xqb_next_disabled").attr("disabled","disabled");
						this.lastDom.addClass("xqb_last_disabled").attr("disabled","disabled");
					}else{
						this.nextDom.removeClass("xqb_next_disabled").attr("disabled","");
						this.lastDom.removeClass("xqb_last_disabled").attr("disabled","");
					}
					return this;
				},
				
				/**
				 * 加载数据
				 */
				loadData : function(){
					if(this.options.beforeLoadingData && $.isFunction(this.options.beforeLoadingData)){
						this.options.beforeLoadingData.call();
					}
					$.extend(this.baseParameter,{
						pageIndex : this.pageIndex,
						pageSize : this.options.pageSize,
						cursor : this.pageIndex * this.options.pageSize
					});
					var param = {};
					param = $.extend(param,this.baseParameter);
					param = $.extend(param,this.extraParameter ? this.extraParameter : {});
					
					//远程动态加载数据
					if(this.options.url){
						$.ajax({
							url : this.options.url,
							async : true,
							type : 'post',
							data : param,
							success : function(data){
								var key = pgt.options.result ? data[pgt.options.result] : data.result;
								var value = pgt.options.failed ? pgt.options.failed : 'failed';
								var msg = pgt.options.reason ? data[pgt.options.reason] : data.reason;
								if(key == value){
									if(pgt.options.error && $.isFunction(pgt.options.error)){
										pgt.options.error.call(this,msg);
									}
									return;
								}
								var total = pgt.getTotalCount(data);
								pgt.setTotal(total);						//重新设置pagination参数信息
								
								pgt.pageIndexDom.val(pgt.pageIndex + 1);
								
								pgt.validateDom();
								//显示明细
								if(pgt.options.showDetails){
									pgt.showDetails(total);
								}
								var data = pgt.getData(data);
								
								//显示数据
								if(pgt.options.displayData && $.isFunction(pgt.options.displayData)){
									pgt.options.displayData.call(this,data);
								}
								
							}
						});
					}else{
						//静态加载数据
						if(!this.store){
							return this;
						}
						var total = pgt.getTotalCount(pgt.store);
						pgt.setTotal(total);
						
						pgt.pageIndexDom.val(pgt.pageIndex + 1);				//回置位页码
						
						pgt.validateDom();
						//显示明细
						if(pgt.options.showDetails){
							pgt.showDetails(total);
						}
						var data = pgt.getData(pgt.store);
						
						//显示数据
						if(pgt.options.displayData && $.isFunction(pgt.options.displayData)){
							pgt.options.displayData.call(this,data);
						}
					}
					
					return this;
				},
				/**
				 * 设置数据源，针对静态加载数据，假分页情况下使用
				 * @param store
				 */
				setStore : function(store){
					this.store = store;
					return this;
				},
				/**
				 * 获取当前数据的游标
				 */
				getCursor : function(){
					return this.pageIndex * this.pageSize;
				},
				/**
				 * 获取当前页的数据条数
				 */
				getCurrentPageCount : function(){
					if(this.pageIndex < this.pageCount - 1){
						return this.pageSize;
					}else{
						var ret = parseInt(this.recordCountDom.html()) % this.pageSize;
						return  ret == 0 ? this.pageSize : ret;
					}
				},
				/**
				 * 获取从后台传递过来的json数据中的totalSize的值
				 * @param data
				 */
				getTotalCount : function(data){
					var total = data;
					if(this.options.url){
						var depth = this.options.total.split(".");
						for(var i = 0; i < depth.length; i++){
							total = total[depth[i]];
						}
					}else{
						total = data.length;
					}
					return total ? total : 0;
				},
				/**
				 * 获取从后台传递过来的json数据中的实际数据信息
				 * @param data
				 */
				getData : function(data){
					var rData = data;
					if(this.options.url){
						var depth = this.options.root.split(".");
						for(var i = 0; i < depth.length; i++){
							rData = rData[depth[i]];
						}
					}else{
						var dt = [];
						for(var i = this.getCursor(); i < (this.getCursor() + this.pageSize) &&  i < data.length; i++){
							dt.push(data[i]);
						}
						return dt;
					}
					return rData ? rData : [];
				},
				
				/**
				 * 设置额外参数，loadData时该参数会和baseParameter一起传递到后台
				 * @param param
				 */
				setExtraParameter : function(param){
					this.extraParameter = param;
					return this;
				},
				
				/**
				 * 设置url
				 */
				setURL : function(url){
					this.options.url = url;
					return this;
				},
				/**
				 * 获取当前URL
				 */
				getURL : function(){
					return this.options.url;
				},
				/**
				 * 显示明细信息
				 * @param total，数据总条数
				 */
				showDetails : function(total){
					if(total == 0){
						if(!this.options.showDetails.emptyMsg){
							this.rightTextDom.css({"color":"fuchsia"}).html("此次查询没有找到相关数据记录");
						}else{
							this.rightTextDom.css({"color":"fuchsia"}).html(this.options.showDetails.emptyMsg);
						}
					}else{
						this.rightTextDom.css({"color":"black"}).html("第" + (this.getCursor() + 1) + "条——第" + (this.getCursor() + 
							this.getCurrentPageCount()) + "条/总计" + total + "条");
					}
					return this;
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
				}
			});
			
			return pgt.generateDomElement();
		}
	});
})(jQuery);


