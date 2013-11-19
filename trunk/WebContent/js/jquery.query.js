/***********************************************************************************
 * 
 * @FILE		jquery.query.js
 * @AUTHOR		9528
 * @DATE		2011-4-30
 * @COPY-RIGHT	大庆金桥成都分公司
 * 
 * @TODO		1、创建一个查询结果面板
 * 		
 * 
 ***********************************************************************************/

(function($){
	if(!$.dqgb){
		$.dqgb = {};
	}
	if(!$.dqgb.query){
		$.extend($.dqgb,{
			query : {}
		});
	}
	$.extend($.dqgb.query,{
		/**
		 * @TODO	获取一个查询面板对象
		 * @param options
		 * @returns {___anonymous524_525}
		 */
		getQueryPanel : function(options){
			var query = {};
			options.miniQueryLength = options.miniQueryLength ? options.miniQueryLength : 3; 
			options.remindMsg = options.remindMsg ? options.remindMsg : '没有找到相关记录信息...';
			$.extend(query,{
				options : options,
				body : $("<div></div>").css({
					'height':(options.height ? options.height : 100) + "px",
					'background-color':'#FAFAFA',
					'width':(options.width ? options.width : options.parent.width() - 2) + "px",'z-index':'99999',
					'border':'1px solid gray','position':'absolute','left':'0px','top':'0px','display':'none'
				}).appendTo($(document.body))
			});
			
			$.extend(query,{
				/**
				 * @TODO	给table添加row
				 */
				addRow : function(table){
					return $.browser.mozilla ? table.insertRow(-1) : table.insertRow();
				},
				/**
				 * @TODO	给row添加column
				 */
				addColumn : function(row){
					return $.browser.mozilla ? row.insertCell(-1) : row.insertCell();
				},
				/**
				 * @TODO	面板渲染策略
				 * @param 	data json字符串
				 */
				strategy : function(data){
					this.body.html("");
					var tb = $("<table style='margin:0px; text-align:left; overflow:hidden; font-size:13px; font-family:微软雅黑;' width='100%' border='0' cellspacing='0' cellpadding='0'>" +
							"</table>").appendTo(this.body).keyup(function(e,data){
								if(query.body.css("display") == "block"){
									if(data.keyCode == 40){
										if($(this).children("tbody").children(".selected").length == 0){
											$(this).children("tbody").children("tr:first").trigger("selected");
										}else{
											$(this).children("tbody").children(".selected").next().trigger("selected");
										}
									}else if(data.keyCode == 38){
										if($(this).children("tbody").children(".selected").length == 0){
											$(this).children("tbody").children("tr:first").trigger("selected");
										}else{
											$(this).children("tbody").children(".selected").prev().trigger("selected");
										}
									}
								}
							}).get(0);
					
					for(var i = 0; i < data.length; i++){
						var row = this.addRow(tb);
						
						$(row).css({"height":"20px","line-height":"20px","margin":"0px","padding":"0px"}).attr("index",i).mouseover(function(){
							$(this).trigger("selected");
						}).click(function(){
							query.body.hide();
						}).bind("selected",function(){
							$(tb).children("tbody").children("tr").css("background-color","#FAFAFA").removeClass("selected");
							$(this).css("background-color","#DDDDDD").trigger("focus").addClass("selected");
							clearTimeout(query.options.blurTimer);
							query.options.parent.val(data[$(this).attr("index")][query.options.keyField]);
						});
						
						for(var j = 0; j < this.options.fields.length; j++){
							var col = this.addColumn(row);
							$(col).css({"padding-left":"2px","margin":"0px"}).html(data[i][this.options.fields[j]]);
						}
					}
					this.body.css({"left":query.options.postion.x + "px","top":query.options.postion.y + "px"}).show();
				},
				/***
				 * @TODO	找寻父级窗口中有"absolute"或者"relative"定位的窗口，并且该窗口的父级窗口要是body元素
				 */
				getParent : function(obj){
					var parent = obj.parent();
					if(parent.get(0).tagName != "BODY" ){
						if(parent.css("position") != "relative" && parent.css("position") != "absolute"){
							return this.getParent(parent); 
						}else{
							if(parent.parent().get(0).tagName != "BODY"){	
								return this.getParent(parent);
							}
							return parent;
						}
					}else{
						return "";
					}
				}
			});
			/**
			 * @TODO	找寻父级绝对/相对定位的窗口
			 */
			var pstWnd = query.getParent(options.parent);
			if(pstWnd){
				$.extend(query.options,{
					positionWnd : pstWnd
				});
				query.body.appendTo(pstWnd);
			}
			
			query.options.blurTimer = "";		//下拉列表消失timer
			/**
			 * @TODO	给父窗口绑定单击事件，定位body应该显示的位置
			 */
			options.parent.click(function(ev){
				var x = ev.pageX;			//鼠标坐标
				var y = ev.pageY;
				var rx = 0;					//相对偏移
				var ry = 0;
				if($.browser.mozilla){
					rx = ev.layerX;				//相对偏移
					ry = ev.layerY;
				}else if($.browser.msie){
					rx = ev.offsetX;				//相对偏移
					ry = ev.offsetY;
				}
				query.options.postion = {};
				query.options.postion.rx = rx;
				query.options.postion.ry = ry;
				query.options.postion.x = x - rx - 2;	//父窗口在body上的坐标。	-2是为了修正input框的bug
				query.options.postion.y = y - ry + options.parent.height();
				if($.browser.mozilla){
					query.options.postion.y += 2;
				}else if($.browser.msie){
					query.options.postion.y += 3;
				}
				if(query.options.positionWnd){
					var pWnd = query.options.positionWnd;
					query.options.postion.x -= $.dqgb.getCssValue(pWnd.css("left"));	//父窗口在body上的坐标。	-2是为了修正input框的bug
					query.options.postion.y -= $.dqgb.getCssValue(pWnd.css("top"));
				}
				return true;
			}).blur(function(){
				query.options.blurTimer = setTimeout(function(){
					query.body.hide();
				},100);
			});
			
			/**
			 * @TODO	注册onchange事件
			 */
			query.options.timer = "";					//添加定时器timer
			options.parent.keyup(function(e){
				//如果少于最少输入字符数，则跳过
				clearTimeout(query.options.timer);
				if($(this).val().length < options.miniQueryLength){	
					return;
				}
				if(e.keyCode == 13){
					query.body.hide();
					return;
				}
				if(query.body.css("display") == "block" && (e.keyCode == 40 || e.keyCode == 38)){
					query.body.children("table").trigger("keyup",{keyCode:e.keyCode});//children("tbody").children("tr:first").trigger("selected");
					return;
				}
				query.options.timer = setTimeout(function(){
					query.options.param[query.options.keyField] = query.options.parent.val(); 
					$.ajax({
						url:query.options.url,
						aysnc:true,
						type:'post',
						data:query.options.param,
						success:function(data){
							if(data.RESULT == 'FAILED'){
								$.dqgb.globalMsg.showMsg(data.REASON);
								return;
							}
							if(data.data.totalSize == 0){
								if(typeof $.dqgb.globalMsg == "object")
									$.dqgb.globalMsg.showMsg(query.options.remindMsg);
								else if(typeof $.xqb9528.globalMsg.showMsg == "function"){
									$.xqb9528.globalMsg.showMsg(query.options.remindMsg);
								}
							}else{
								var result = data;
								var depth = query.options.root.split(".");
								for(var i = 0; i < depth.length; i++){
									result = result[depth[i]];
								}
								query.strategy(result);
							}
						}
					});
				},options.delay ? options.delay : 1000);
			});
			return query;
		}
	});
	
	/**
	 * @TODO	注册查询插件
	 */
	$.fn.query = function(options){
		if(this.attr("hasQueried"))
			return;
		this.attr("hasQueried",true);
		options.parent = $(this);
		return $.dqgb.query.getQueryPanel(options);
	};
})(jQuery);