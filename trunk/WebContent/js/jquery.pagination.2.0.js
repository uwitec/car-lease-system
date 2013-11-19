/***********************************************************************************
 * 
 * @FILE		jquery.pagination.js
 * @AUTHOR		9528
 * @DATE		2011-3-1
 * @COPY-RIGHT	大庆金桥成都分公司
 * 
 * @TODO		1、创建jquery 分页控件
 * 		
 * @MODIFIED	2011-3-31
 * 				修改了整体架构，同时暴露了数据ajax加载的方法，
 * 				开发人员可以覆盖该方法实现自己的数据加载方式,
 * 
 * 
 ***********************************************************************************/

(function($){
	if(!$.dqgb){
		$.dqgb = {};
	}
	$.extend($.dqgb,{
		pagination:{}						//创建命名空间
	});
	
	$.extend($.dqgb.pagination,{
		
		options:{
			defaultWidth : 400				//默认宽度
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
		/*********************************
		 * 
		 * @TODO	创建分页控件
		 *
		 ********************************/
		getPagination : function(options){
			//定义分页控件的div元素
			var jqueryObj = {};
			var pgt = $("<div></div>").appendTo($(document.body));
			pgt.css({
				"height":"24px", "padding":"0px", "margin":"0px", "overflow":"hidden",
				"line-height":"24px", "font-size":"13px", "font-family":"微软雅黑"
			}).shieldSelectStart().shieldRightMenu().attr("pageIndex",0);
			
			$.extend(options,{
				pageIndex : 0					//标识当前的页面数
			});
			
			$.extend(jqueryObj,{
				options : options,
				pagination : pgt				//分页元素的dom(jQuery)对象
			});
			
			var temp = $("<div style='float:left;overflow:hidden;'></div>").appendTo(jqueryObj.pagination).css({
				"width": this.options.defaultWidth + "px"
			});
			
			//添加table元素存储分页控件
			var tb = $("<table style='float:left; margin:0px; overflow:hidden;' border='0' cellspacing='0' cellpadding='0' height='24'>" +
					"</table>").appendTo(temp).get(0);
			
			var row = this.addRow(tb);
			
			var col = this.addColumn(row);
			
			$(col).css("width","4px");				//空白区域
			
			col = this.addColumn(row);	//首页
			jqueryObj.first = $("<input type='button' title='首页' class='dqgb_pagination dqgb_first' />").appendTo(col);
			
			col = this.addColumn(row); //上一页
			jqueryObj.previous = $("<input type='button' title='上一页' class='dqgb_pagination dqgb_prev' />").appendTo(col);
			
			col = this.addColumn(row);
			$("<div class='dqgb_pagination_label'>共</div>").appendTo(col);
			
			var pages = Math.ceil(options.totalCount/options.pageSize);
			pages = pages == 0 ? 1 : pages;
			
			col = this.addColumn(row); //页数
			jqueryObj.pageCount = $("<div class='dqgb_pagination_label'></div>").appendTo(col).html(pages);

			col = this.addColumn(row);
			$("<div class='dqgb_pagination_label'>页,&nbsp;当前是第&nbsp;</div>").appendTo(col);
			
			col = this.addColumn(row); //页码
			jqueryObj.pageIndex = $("<input type='text' style='width:30px; height:14px;' />").appendTo(col).val(1);
			
			col = this.addColumn(row);
			$("<div class='dqgb_pagination_label'>&nbsp;页,&nbsp;共</div>").appendTo(col);
			
			col = this.addColumn(row); //记录条数
			jqueryObj.recordCount = $("<div class='dqgb_pagination_label'></div>").appendTo(col).html(options.totalCount);
			
			col = this.addColumn(row);
			$("<div class='dqgb_pagination_label'>条记录</div>").appendTo(col);
			
			col = this.addColumn(row);	//下一页
			jqueryObj.next = $("<input type='button' title='下一页' class='dqgb_pagination dqgb_next' />").appendTo(col);
			
			col = this.addColumn(row); //尾页
			jqueryObj.last = $("<input type='button' title='尾页' class='dqgb_pagination dqgb_last' />").appendTo(col);
			
			col = this.addColumn(row); //刷新
			jqueryObj.refresh = $("<input type='button' title='刷新' class='dqgb_pagination dqgb_refresh' />").appendTo(col);
			
			//右边的text区域
			jqueryObj.rightText = $("<span style='float:right; margin-right:6px;' class='dqgb_pagination_label'></span>").appendTo(jqueryObj.pagination);
			
			
			
			jqueryObj.first.click(function(){											//注册 "首页" 事件
				jqueryObj.pageIndex.val("1");
				jqueryObj.pageIndex.trigger("change");	
			});
			jqueryObj.previous.click(function(){										//注册 "上一页" 事件
				jqueryObj.pageIndex.val(jqueryObj.pageIndex.val() - 1);
				jqueryObj.pageIndex.trigger("change");	
			});
			jqueryObj.next.click(function(){											//注册 "下一页" 事件
				jqueryObj.pageIndex.val(parseInt(jqueryObj.pageIndex.val()) + 1);
				jqueryObj.pageIndex.trigger("change");	
			});
			jqueryObj.last.click(function(){											//注册 "尾页" 事件
				jqueryObj.pageIndex.val(jqueryObj.pageCount.html());
				jqueryObj.pageIndex.trigger("change");	
			});
			
			jqueryObj.refresh.click(function(){											//注册 "刷新" 事件
				jqueryObj.options.pageIndex = 0;
				jqueryObj.pageIndex.trigger("change");	
			});
			
			jqueryObj.pageIndex.change(function(){
				var pIndex = $.dqgb.pagination.checkNumber($.trim(jqueryObj.pageIndex.val()));
				if(pIndex == ""){										//非法输入				
					jqueryObj.pageIndex.val(jqueryObj.options.pageIndex);
					return;
				}else{
					if(pIndex == jqueryObj.options.pageIndex){
						return;
					}
					if(pIndex > parseInt(jqueryObj.pageCount.html())){
						jqueryObj.pageIndex.val(jqueryObj.pageCount.html());
						if(jqueryObj.options.pageIndex == jqueryObj.pageCount.html()){
							return;
						}
					}
				}
				pIndex = jqueryObj.pageIndex.val();
				if(pIndex == 1){
					jqueryObj.first.addClass("dqgb_first_disabled").attr("disabled","disabled");
					jqueryObj.previous.addClass("dqgb_prev_disabled").attr("disabled","disabled");
				}
				if(pIndex == jqueryObj.pageCount.html()){
					jqueryObj.next.addClass("dqgb_next_disabled").attr("disabled","disabled");
					jqueryObj.last.addClass("dqgb_last_disabled").attr("disabled","disabled");
				}
				if(pIndex != 1){
					jqueryObj.first.removeClass("dqgb_first_disabled").attr("disabled","");
					jqueryObj.previous.removeClass("dqgb_prev_disabled").attr("disabled","");
				}
				if(pIndex != jqueryObj.pageCount.html()){
					jqueryObj.next.removeClass("dqgb_next_disabled").attr("disabled","");
					jqueryObj.last.removeClass("dqgb_last_disabled").attr("disabled","");
				}
				jqueryObj.options.pageIndex = pIndex;
				
				//加载数据前调用
				if($.isFunction(jqueryObj.options.beforeLoading)){			
					jqueryObj.options.beforeLoading.call(this,jqueryObj.options.pageIndex,jqueryObj.options.pageSize);
				}
				
				if(jqueryObj.options.extraParam){
					$.extend(jqueryObj.options.extraParam,{
						pageIndex : parseInt(jqueryObj.options.pageIndex) - 1,
						pageSize : jqueryObj.options.pageSize
					});
				}else{
					jqueryObj.options.extraParam = {
						pageIndex : parseInt(jqueryObj.options.pageIndex) - 1,
						pageSize : jqueryObj.options.pageSize
					};
				}
				jqueryObj.loadingData(jqueryObj.options);
				
			}).click(function(){
				$(this).trigger("focus");
			}).keyup(function(e){
				if(e.keyCode == 13){
					$(this).trigger("change");
				}
			});
			
			$.extend(jqueryObj,{
				/************************************
				 * 
				 * @TODO	获取实际的div元素
				 * 
				 */
				getDomElement : function(){
					return jqueryObj.pagination;
				},
				/*********
				 * 
				 * @TODO	刷新页码
				 * 
				 */
				refreshParam : function(pageSize,totalCount){
					try{
						var pages = Math.ceil(totalCount/pageSize);
						pages = pages == 0 ? 1 : pages;
						$.extend(jqueryObj.options,{
							pageSize : pageSize,
							totalCount : totalCount
						});
						jqueryObj.pageCount.html(pages);
						jqueryObj.recordCount.html(totalCount);
						if(totalCount){
							pIndex = jqueryObj.pageIndex.val();
							if(pIndex == 1){
								jqueryObj.first.addClass("dqgb_first_disabled").attr("disabled","disabled");
								jqueryObj.previous.addClass("dqgb_prev_disabled").attr("disabled","disabled");
							}
							if(pIndex == jqueryObj.pageCount.html()){
								jqueryObj.next.addClass("dqgb_next_disabled").attr("disabled","disabled");
								jqueryObj.last.addClass("dqgb_last_disabled").attr("disabled","disabled");
							}
							if(pIndex != 1){
								jqueryObj.first.removeClass("dqgb_first_disabled").attr("disabled","");
								jqueryObj.previous.removeClass("dqgb_prev_disabled").attr("disabled","");
							}
							if(pIndex != jqueryObj.pageCount.html()){
								jqueryObj.next.removeClass("dqgb_next_disabled").attr("disabled","");
								jqueryObj.last.removeClass("dqgb_last_disabled").attr("disabled","");
							}
						}
					}catch(e){
					//	$.dqgb.shadowLayer.alert({content:e.message});
						return;
					}
				},
				
				/*********
				 * 
				 * @TODO	配置分页控件
				 * 
				 */
				configPagination : function(pageSize,totalCount){
					var pages = Math.ceil(totalCount/pageSize);
					pages = pages == 0 ? 1 : pages;
					$.extend(jqueryObj.options,{
						pageSize : pageSize,
						pageIndex : 0,
						totalCount : totalCount
					});
					jqueryObj.pageIndex.val("1");
					jqueryObj.pageCount.html(pages);
					jqueryObj.recordCount.html(totalCount);
					jqueryObj.refresh.trigger("click");
				},
				
				/******
				 * @TODO	加载数据函数，通过覆盖该函数实现不同方式的数据加载。
				 */
				loadingData : function(options){
					$.post(options.url,options.extraParam,function(data){
						try{
							if(data.RESULT == "FAILED"){
								$.dqgb.shadowLayer.alert({content:data.REASON});
								return;
							}
							if($.isFunction(jqueryObj.options.displayData)){
								var depth = jqueryObj.options.root.split(".");
								var result = data;
								if(result){
									for(var i = 0; i < depth.length; i++){
										result = result[depth[i]];
									}
									var depth2 = jqueryObj.options.total.split(".");
									var _total = data;									//获取记录总数
									for(var i = 0; i < depth2.length; i++){
										_total = _total[depth2[i]];
									}
									jqueryObj.refreshParam(jqueryObj.options.pageSize,_total);		//刷新分页控件
									jqueryObj.options.displayData.call(this,result,jqueryObj);
									if(jqueryObj.options.showDetails){
										if(_total){
											jqueryObj.rightText.css({"color":"black"}).html("第" + jqueryObj.getCursor() + "条——第" + (jqueryObj.getCursor() + 
												(result.length ? result.length : 1) - 1) + "条/总计" + jqueryObj.getRecordCount() + "条");
										}else{
											if(!jqueryObj.options.showDetails.emptyMsg){
												jqueryObj.rightText.css({"color":"fuchsia"}).html("此次查询没有找到相关数据记录");
											}else{
												jqueryObj.rightText.css({"color":"fuchsia"}).html(jqueryObj.options.showDetails.emptyMsg);
											}
										}
									}
								}
							}
							if($.isFunction(options.afterLoading)){
								jqueryObj.options.afterLoading.call(this,jqueryObj.options.pageIndex,jqueryObj.options.pageSize);
							}
						}catch(e){
						//	$.dqgb.shadowLayer.alert({content:e.message});
							return;
						}
					},'json');
				},
				/********************************
				 * 
				 * @TODO	获取页码，从0开始
				 * 
				 */
				getPageIndex : function(){
					return parseInt(jqueryObj.options.pageIndex - 1);
				},
				/********************************
				 * 
				 * @TODO	获取记录条数的cursor
				 * 
				 */
				getCursor : function(){
					var dom = jqueryObj.getDomElement();
					return (parseInt(jqueryObj.options.pageIndex) - 1) * parseInt(jqueryObj.options.pageSize) + 1;
				},
				/********************************
				 * 
				 * @TODO	获取记录条数
				 * 
				 */
				getRecordCount : function(){
					return parseInt(jqueryObj.options.totalCount);
				},
				/********************************
				 * 
				 * @TODO	获取页数
				 * 
				 */
				getPages : function(){
					return parseInt(jqueryObj.pageCount.html());
				}
			});
			if(!jqueryObj.options.manualLoad){
				jqueryObj.refresh.trigger("click");
			}
			return jqueryObj;
		},
		
		/***
		 * @TODO	渲染一个Div构建的分页控件。
		 * @param 	options
		 */
		generateDivPagination : function(options){
			var pgt = {};
			$.extend(pgt,{
				options : options,
				
				pageIndex : 0,
				
				container : $("#" + options.container).shieldSelectStart().shieldRightMenu(),
				/**
				 * @TODO	截取数据信息
				 */
				getData : function(options,data){
					var dt = {};
					var depth = options.root.split(".");
					var result = data;
					if(result){
						for(var i = 0; i < depth.length; i++){
							result = result[depth[i]];
						}
						dt.data = result;
						var depth2 = options.total.split(".");
						var _total = data;									//获取记录总数
						for(var i = 0; i < depth2.length; i++){
							_total = _total[depth2[i]];
						}
						dt.totalCount = _total;
					}
					return dt;
				},
				/**
				 * @TODO	生成分页控件
				 */
				generatePgt : function(pageCount,pageIndex,start){
					$("<a class='un_dot'>&lt首页</a>").appendTo(this.container).click(function(){
						if(start == 0){
							if(pgt.pageIndex != 0){
								$(this).next("a").next("a").trigger("click");
								pgt.pageIndex = 0;
							}
						}else{
							pgt.pageIndex = 0;
							pgt.loadData(true,0);
						}
					});
					$("<a class='un_dot'>上一页</a>").appendTo(this.container).click(function(){
						var pre = pgt.container.children(".dqgb_pagination_selected_item").prev(".item");
						if(pre.length != 0){
							pgt.pageIndex = parseInt(pgt.pageIndex) - 1;
							pre.trigger("click");
						}else{
							if(start != 0){
								start = start - pgt.options.dotIndex;
								start = (start > 0) ? start : 0;
								pgt.pageIndex = pgt.pageIndex - 1;
								pgt.loadData(true,start);
							}
						}
					});
					if(pageCount < options.pageNumber){
						for(var i = 0; i < pageCount; i++){
							var a = $("<a class='un_dot item'>" + (i + 1) + "</a>").appendTo(this.container).unbind("click").click(function(){
								pgt.container.children(".dqgb_pagination_selected_item").removeClass("dqgb_pagination_selected_item");
								$(this).addClass("dqgb_pagination_selected_item");
								pgt.pageIndex = $(this).html() - 1;
								pgt.loadData(false,0);
							});
							if(i == pageIndex){
								a.addClass("dqgb_pagination_selected_item");
							}
						}
					}else{
						var pIndex = pageIndex;
						if(pageIndex + pgt.options.pageNumber >= pageCount - 1){
							pageIndex = pageCount - 1 - pgt.options.pageNumber;
						}
						for(var i = 0; i < pgt.options.dotIndex; i++){
							var a = $("<a class='un_dot item'>" + (start + 1 + i) + "</a>").appendTo(this.container).unbind("click").click(function(){
								pgt.container.children(".dqgb_pagination_selected_item").removeClass("dqgb_pagination_selected_item");
								$(this).addClass("dqgb_pagination_selected_item");
								pgt.pageIndex = $(this).html() - 1;
								pgt.loadData(false,start);
							});
							if(i == 0){
								if(start != 0){
									a.unbind("click").click(function(){
										start = start - pgt.options.dotIndex + 1;
										start = (start > 0) ? start : 0;
										pgt.pageIndex = $(this).html() - 1;
										pgt.loadData(true,start);
									});
								}
							}
							if((start + i) == pIndex){
								a.addClass("dqgb_pagination_selected_item");
							}
							if(start + pgt.options.pageNumber < pageCount - 1){
								if(i == pgt.options.dotIndex - 1){
									$("<a class='dot'>...</a>").appendTo(this.container);
									a.unbind("click").click(function(){
										if(start < pageCount - pgt.options.pageNumber){
											start = $(this).html() - 1;
											start = (start > pageCount - pgt.options.pageNumber) ? (pageCount - pgt.options.pageNumber) : start;
											pgt.pageIndex = $(this).html() - 1;
											pgt.loadData(true,start);
										}
									});
								}
							}
						}
						//绘制最后两个按钮
						for(var i = 0; i < pgt.options.pageNumber - pgt.options.dotIndex; i++){
							var a = $("<a class='un_dot item'>" + (pageCount + i - 1) + "</a>").appendTo(this.container).unbind("click").click(function(){
								pgt.container.children(".dqgb_pagination_selected_item").removeClass("dqgb_pagination_selected_item");
								$(this).addClass("dqgb_pagination_selected_item");
								pgt.pageIndex = $(this).html() - 1;
								if(start != pageCount - pgt.options.pageNumber){
									pgt.loadData(true,pageCount - pgt.options.pageNumber);
								}else{
									pgt.loadData(false,start);
								}
							});
							if((a.html() - 1) == pIndex){
								a.addClass("dqgb_pagination_selected_item");
							}
						}
					}
					$("<a class='un_dot'>下一页</a>").appendTo(this.container).click(function(){
						var next = pgt.container.children(".dqgb_pagination_selected_item").next(".item");
						if(next.length != 0){
							pgt.pageIndex = parseInt(pgt.pageIndex) + 1;
							next.trigger("click");
						}else{
							if(start < pageCount - pgt.options.pageNumber){
								start = start + pgt.options.dotIndex;
								start = (start > pageCount - pgt.options.pageNumber) ? (pageCount - pgt.options.pageNumber) : start;
								pgt.pageIndex = pgt.pageIndex + 1;
								pgt.loadData(true,start);
							}
						}
						
					});
					$("<a class='un_dot'>尾页&gt</a>").appendTo(this.container).click(function(){
						if(pgt.pageIndex != pageCount - 1){
							pgt.pageIndex = pageCount - 1;
							$(this).prev("a").prev("a").trigger("click");
						}
					});
					pgt.container.children(".un_dot").addClass("dqgb_div_pagination").mouseover(function(){
						$(this).addClass("dqgb_div_pagination_over");
					}).mouseout(function(){
						$(this).removeClass("dqgb_div_pagination_over");
					});
				},
				/**
				 * @TODO	数据加载,redraw为true时表示重新渲染分页控件,start:起始index
				 */
				loadData : function(redraw,start){
					if(options.url){
						//扩展请求参数
						var extraParam = {};
						if(options.extraParam){
							$.extend(extraParam,options.extraParam);
						}
						$.extend(extraParam,{
							pageIndex : pgt.pageIndex,
							pageSize:options.pageSize
						});
						if($.isFunction(options.beforeDataLoading)){
							options.beforeDataLoading.call();
						}
						$.dqgb.shadowLayer.showMask({mask:'正在加载数据,请稍后...'});
						//请求数据 
						$.ajax({
							url:options.url,
							type:'post',
							async:true,
							data:extraParam,
							success:function(data){
								var dt = pgt.getData(options,data);
								var pageCount = Math.ceil(dt.totalCount/options.pageSize);
								pageCount = (pageCount == 0) ? 1 : pageCount;
								if(redraw){
									pgt.container.children("a").remove();
									pgt.generatePgt(pageCount,pgt.pageIndex,start);
								}
								if($.isFunction(pgt.options.displayData)){
									pgt.options.displayData.call(this,dt.data);
								}
								$.dqgb.shadowLayer.hideMask();
							}
						});
					}
				}
			});
			//修正ie6,ie7 "relative"定位在窗口尺寸发生变化时的bug
			if($.browser.msie && ($.browser.version == "6.0" || $.browser.version == "7.0")){
				if(pgt.container.css("position") != "absolute"){
					pgt.container.css("position","relative");
					$.dqgb.registerLayoutFn(function(){
						var wd = pgt.container.width();
						pgt.container.width(0).width(wd);
					});
				}
			}
			pgt.loadData(true,0);
			
			return pgt;
		}
	});
})(jQuery);





