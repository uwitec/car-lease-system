/***********************************************************************************
 * 
 * @FILE		jquery.model.js
 * @AUTHOR		9528
 * @DATE		2011-4-19
 * @COPY-RIGHT	大庆金桥成都分公司
 * 
 * @TODO		1、创建一个model面板
 * 		
 * 
 ***********************************************************************************/

(function($){
	
	//创建名字空间
	if(!$.dqgb){
		$.dqgb = {};
	}
	
	if(!$.dqgb.panel){
		$.extend($.dqgb,{
			panel : {}
		});
	}
	
	$.extend($.dqgb.panel,{
		/**
		 * @TODO	获取一个textPanel
		 */
		getTextPanel : function(options){
			var tp = new Object();
			
			//创建body
			$.extend(tp,{
				options : options,
				body : $("<div></div>").appendTo($(document.body)).css({"font-family":"微软雅黑"})
			});
			
			//定制额外的样式
			if(tp.options.css){
				tp.body.addClass(tp.options.css);
			}
			
			$.extend(tp,{
				header : $("<div></div>").appendTo(tp.body).addClass("dqgb_model_panel_header").width(tp.options.width - 2),
				contentBody : $("<div></div>").appendTo(tp.body).addClass("dqgb_model_panel_body").width(tp.options.width - 2)
			});
			
			if(tp.options.height){
				tp.contentBody.height(tp.options.height - 32);
			}
			
			$.extend(tp,{
				headerIcon : $("<div></div>").addClass("dqgb_model_panel_icon"),
				text :  $("<div></div>").addClass("dqgb_model_panel_text").html(tp.options.title)
			});
			
			tp.headerIcon.appendTo(tp.header);
			tp.text.appendTo(tp.header);
			
			$.extend(tp,{
				/**
				 * @TODO	获取header
				 */
				getHeader : function(){
					return this.header;
				},
				/**
				 * @TODO	获取body
				 */
				getBody : function(){
					return this.contentBody;
				},
				/**
				 * @TODO	获取dom元素
				 */
				getDomElement : function(){
					return this.body;
				}	
			});
			
			tp.header.unbind("click").click(function(){
				if(tp.getBody().css("display") == "block"){
					tp.getBody().hide();
					tp.headerIcon.addClass("dqgb_model_panel_icon_collapse");
				}else{
					tp.getBody().show();
					tp.headerIcon.removeClass("dqgb_model_panel_icon_collapse");
				}
			});
			
			if(tp.options.item){
				var ul = $.dqgb.panel.getPanelList(tp.options);
				ul.ul.appendTo(tp.getBody());
			}
			
			return tp;
		},
		/***********
		 * 
		 * @TODO	生成一个panel内部的list
		 * 
		 */
		getPanelList : function(options){
			var ulObj = {};
			$.extend(ulObj,{
				ul : $("<ul></ul>").appendTo($(document.body))
			});
			ulObj.ul.addClass("dqgb_model_panel_ul").css({"width":options.width - 22 + "px"});
			
			for(var i = 0; i < options.item.length; i++){
				var li = $("<li></li>").addClass("dqgb_model_panel_li").appendTo(ulObj.ul).attr("index",i);
				var title = $("<div></div>").css({"width":"100%","height":"20px"}).appendTo(li);
				
				//description
				if(options.item[i].description){
					var desc = $("<div class='desc'></div>").css({
						"width":"100%","height":"20px","line-height":"20px","font-size":"13px"
					}).addClass("dqgb_overflow_ellipsis").appendTo(li).hide();
					
					$("<label style='margin-left:20px;'></label>").html(options.item[i].description).appendTo(desc);
				}
				var ie6 = $.browser.mise || ($.browser.version == "6.0");
				//icon
				$("<div></div>").addClass("dqgb_model_panel_li_icon").appendTo(title).css({
					"margin-top":"7px","margin-left": (ie6 ? "3px" : "5px")
				});
				
				
				//text
				var _text = $("<div></div>").css({"line-height":"20px","height":"20px","float":"left",
					"font-size":"13px","margin-left":"5px",
					"width":options.width - 180 + "px","cursor":"pointer","color":"#4444F8"
				}).addClass("dqgb_overflow_ellipsis").html(options.item[i].text).appendTo(title);
				
				//remark
				$("<div></div>").css({
					"float":"right","line-height":"20px","height":"20px","font-size":"13px","margin-right":(ie6 ? "1px" : "2px")
				}).addClass("dqgb_overflow_ellipsis").html(options.item[i].remark ? options.item[i].remark : "").appendTo(title);
				
				li.mouseover(function(){
					if($(this).children(".desc").length != 0){
						$(this).height(40).children(".desc").show();
					}
					$(this).css({"background":"#DAF3FA"});
				}).mouseout(function(){
					$(this).height(20).children(".desc").hide();
					$(this).css({"background":"white"});
				}).click(function(){
					if($.isFunction(options.item[$(this).attr("index")].click)){
						options.item[$(this).attr("index")].click.call();
					}
				});
			}
			return ulObj;
		}
	});
})(jQuery);





