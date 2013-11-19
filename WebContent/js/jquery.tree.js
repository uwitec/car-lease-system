/***********************************************************************************
 * 
 * @FILE		jquery.tree.js
 * @AUTHOR		9528
 * @DATE		2011-3-3
 * @COPY-RIGHT	大庆金桥成都分公司
 * 
 * @TODO		1、创建jquery tree
 * 		
 * 
 ***********************************************************************************/

/*************************
 * 
 * @remark		NODE 属性，节点id：唯一标识一个菜单，leaf：标识该节点是否有子节点(boolean)
 * 				parentNodeId：父节点id。level：节点深度。
 * 
 */


/**
 * @return
 */
(function($){
	if(!$.dqgb){
		$.dqgb = {};
	}
	
	$.extend($.dqgb,{						//创建名字空间
		tree : {}
	});
	
	$.extend($.dqgb.tree,{
		
		options :  {
				textMargin : 4,						//文本和icon之间的间隙
				rootNodeID : 'root'					//根节点默认id,
			},
		
		/*********************
		 *@TODO		生成一个根节点 
		 *@PARAM	options:节点配置
		 */
		createRootNode : function(options){
			var jqueryObj = new Object();
			$.extend(jqueryObj,{
				options : options,
				nodeAttribute: {
					id : $.dqgb.tree.options.rootNodeID,					//节点id
					expanded : options.staticTree ? true : false,			//折叠状态
					level : 0,												//节点深度
					leaf : false,											//不是叶子节点
					text : options.text,									//文本
					parentId : $.dqgb.tree.options.rootNodeID				//父节点id
				},
				subNodes : [],
				
				parentNode : '',											//父节点的treeNode对象
				
				treeNode : [],												//root下的所有节点包括根节点
				/********************
				 * 
				 * @TODO	获取根节点
				 * 
				 */
				getRoot : function(){
					return jqueryObj;
				},
				/********************
				 * 
				 * @TODO	取消被选中
				 * 
				 */
				removeSelected : function(){
					var list = jqueryObj.getRoot().treeNode;
					for(var i = 0; i < list.length; i++){
						var node =list[i].getDomElement().children(".text");
						if(node.hasClass("dqgb_tree_text_click")){
							node.removeClass("dqgb_tree_text_click");
							return;
						}
					}
				},
				/********************
				 * 
				 * @TODO	获取子节点对象数组
				 * 
				 */
				getSubNodes : function(){
					return jqueryObj.subNodes;
				},
				/********************
				 * 
				 * @TODO	获取父节点
				 * 
				 */
				getParentNode : function(){
					return jqueryObj.parentNode;
				},
				/********************
				 * 
				 * @TODO	添加子节点
				 * 
				 */
				addSubNode : function(node){
					jqueryObj.subNodes[jqueryObj.subNodes.length] = node;
					return jqueryObj.subNodes;
				},
				/********************
				 * 
				 * @TODO	删除子节点
				 * 
				 */
				removeSubNode : function(node){
					var len = jqueryObj.subNodes.length;
					for(var i = 0; i < len; i++){
						if(jqueryObj.subNodes[i].getNodeAttribute().id == node.getNodeAttribute().id){
							jqueryObj.subNodes = jqueryObj.subNodes.slice(0,i).concat(jqueryObj.subNodes.slice(i + 1,len));
							return;
						}
					}
				},
				/*****************************
				 * 
				 * @TODO	获取节点的属性
				 * 
				 */
				getNodeAttribute : function(){
					return jqueryObj.nodeAttribute;
				},
				
				/*****************************
				 * 
				 * @TODO	设置、修改节点的属性
				 * 
				 */
				setNodeAttribute : function(attr){
					$.extend(jqueryObj.nodeAttribute,attr);
					return jqueryObj.nodeAttribute;
				},
				
				/*****************************
				 * 
				 * @TODO	获取节点的dom元素
				 * 
				 */
				getDomElement : function(){
					return root;
				}
			});
			/***********
			 * @TODO	折叠闭合操作
			 */
			function expannd_collapse(){
				if(jqueryObj.getNodeAttribute().leaf == true){				//叶子节点不做任何操作
					return;
				}
				if(options.staticTree){				  //静态树					
					if(jqueryObj.getNodeAttribute().expanded){
						$.dqgb.tree.collapseNode(jqueryObj);
					}else{
						$.dqgb.tree.expandNode(jqueryObj);
					}
				}else{								  //动态树
					if(jqueryObj.getNodeAttribute().expanded){
						$.dqgb.tree.collapseNode(jqueryObj);
					}else{
						if(options.async){				  //指明每次加载数据时重新刷新
							if(jqueryObj.getNodeAttribute().status != "loading"){
								jqueryObj.setNodeAttribute({								//防止网络延时时用户重复点击
									status:'loading'
								});
								$.ajax({
									url:options.url,
									type:'post',
									async:true,
									data:{
										ID:jqueryObj.getNodeAttribute().id
									},
									success:function(data){
										var sub = jqueryObj.getDomElement().next("div[parentNodeId='" + jqueryObj.getNodeAttribute().id + "']");
										if(sub.length == 1){
											sub.get(0).innerHTML = "";
											sub.html("");
										}
										$.dqgb.tree.getStaticDqgbTree(jqueryObj,data[options.root],options);
										$.dqgb.tree.expandNode(jqueryObj);
										jqueryObj.setNodeAttribute({
											status:'loaded'
										});
									}
								});
							}
						}else{							//动态加载，只加载一次
							if(jqueryObj.getNodeAttribute().status != "loaded"){
								if(jqueryObj.getNodeAttribute().status != "loading"){
									jqueryObj.setNodeAttribute({								//防止网络延时时用户重复点击
										status:'loading'
									});
									$.ajax({
										url:options.url,
										type:'post',
										async:true,
										data:{
											ID:jqueryObj.getNodeAttribute().id
										},
										success:function(data){
											$.dqgb.tree.getStaticDqgbTree(jqueryObj,data[options.root],options);
											$.dqgb.tree.expandNode(jqueryObj);
											jqueryObj.setNodeAttribute({
												status:'loaded'
											});
										}
									});
								}
							}else{
								$.dqgb.tree.expandNode(jqueryObj);
							}
						}
					}
				}
			};
			var root = $("<div class='dqgb_tree_node' style='width:100%;white-space:nowrap;'></div>")
							.appendTo(options.container ? options.container : $(document.body));
			
			this.beforeCreateNode(jqueryObj);
			
			jqueryObj.exp_coll = $("<div class='dqgb_tree_icon'></div>")
							.appendTo(root).addClass("dqgb_tree_11").click(function(){
								expannd_collapse();
							});

			jqueryObj.icon = $("<div class='dqgb_tree_icon'></div>")
						.appendTo(root).addClass(options.iconCls ? options.iconCls : "dqgb_tree_default_icon")
						.dblclick(function(){
							expannd_collapse();
						});
			
			jqueryObj.name = $("<div style='margin-left:" + $.dqgb.tree.options.textMargin + "px; white-space:nowrap;" +
								"float:left; cursor:pointer;'></div>").appendTo(root)
								.addClass("dqgb_tree_node text").html(options.text)
								.click(function(){
									jqueryObj.removeSelected();
									$(this).addClass("dqgb_tree_text_click");
									if($.isFunction(jqueryObj.getNodeAttribute().click)){
										jqueryObj.getNodeAttribute().click.call();
									}
								}).dblclick(function(){
									expannd_collapse();
									if($.isFunction(jqueryObj.getNodeAttribute().dblclick)){
										jqueryObj.getNodeAttribute().dblclick.call();
									}
								});
			
			this.resizeParent(jqueryObj,options.container);
			
			jqueryObj.treeNode[0] = jqueryObj;
			
			return jqueryObj;
		},
		
		/******************
		 * 
		 * @TODO	生成一个树节点
		 * @PARAM	parent:父节点(函数对象，非dom jquery对象),attr:属性
		 * 
		 */
		createTreeNode : function(parent,attr,options){
			var jqueryObj = new Object();
			
			var node = $("<div class='dqgb_tree_node' style='width:100%;white-space:nowrap; display:none;'></div>");
			
			$.extend(jqueryObj,{
				
				nodeAttribute: {},
				
				subNodes : [],				//存储直属子节点
				
				parentNode : parent,
				
				/********************
				 * 
				 * @TODO	获取子节点对象数组
				 * 
				 */
				getSubNodes : function(){
					return jqueryObj.subNodes;
				},
				/********************
				 * 
				 * @TODO	获取父节点
				 * 
				 */
				getParentNode : function(){
					return jqueryObj.parentNode;
				},
				/********************
				 * 
				 * @TODO	获取根节点
				 * 
				 */
				getRoot : function(){
					function getParent(Obj){
						var root;
						if(Obj.parentNode){
							root = getParent(Obj.parentNode);
						}else{
							root = Obj;
						}
						return root;
					};
					return getParent(jqueryObj);
				},
				/********************
				 * 
				 * @TODO	取消被选中
				 * 
				 */
				removeSelected : function(){
					var list = jqueryObj.getRoot().treeNode;
					for(var i = 0; i < list.length; i++){
						var node =list[i].getDomElement().children(".text");
						if(node.hasClass("dqgb_tree_text_click")){
							node.removeClass("dqgb_tree_text_click");
							return;
						}
					}
				},
				
				/********************
				 * 
				 * @TODO	添加子节点
				 * 
				 */
				addSubNode : function(node){
					jqueryObj.subNodes[jqueryObj.subNodes.length] = node;
					return jqueryObj.subNodes;
				},
				/********************
				 * 
				 * @TODO	删除子节点
				 * 
				 */
				removeSubNode : function(node){
					var len = jqueryObj.subNodes.length;
					for(var i = 0; i < len; i++){
						if(jqueryObj.subNodes[i].getNodeAttribute().id == node.getNodeAttribute().id){
							jqueryObj.subNodes = jqueryObj.subNodes.slice(0,i).concat(jqueryObj.subNodes.slice(i + 1,len));
							return;
						}
					}
				},
				
				/*****************************
				 * 
				 * @TODO	获取节点的属性
				 * 
				 */
				getNodeAttribute : function(){
					return jqueryObj.nodeAttribute;
				},
				
				/*****************************
				 * 
				 * @TODO	设置、修改节点的属性
				 * 
				 */
				setNodeAttribute : function(attr){
					$.extend(jqueryObj.nodeAttribute,attr);
					return jqueryObj.nodeAttribute;
				},
				
				/*****************************
				 * 
				 * @TODO	获取节点的dom元素
				 * 
				 */
				getDomElement : function(){
					return node;
				}
			});

			if(attr.leaf == "false"){
				attr.leaf = false;
			}else if(attr.leaf == "true"){
				attr.leaf = true;
			}
			jqueryObj.setNodeAttribute({leaf:true});
			jqueryObj.setNodeAttribute(attr);
			jqueryObj.setNodeAttribute({
				level : parent.getNodeAttribute().level + 1,
				parentId : parent.getNodeAttribute().id,
				id : attr.id ? attr.id : parent.getNodeAttribute().id + "_" + parent.getSubNodes().length,
				iconCls : attr.iconCls ? attr.iconCls : "dqgb_tree_default_icon",
				expanded : options.staticTree ? true : false							//折叠状态
			});
			
			this.beforeCreateNode(jqueryObj);
			
			for(var i = 0; i < jqueryObj.getNodeAttribute().level; i++){
				$("<div class='dqgb_tree_icon space'></div>").appendTo(node);
			}
			
			/***********
			 * @TODO	折叠闭合操作
			 */
			function expannd_collapse(){
				if(jqueryObj.getNodeAttribute().leaf == true){				//叶子节点不做任何操作
					return;
				}
				if(options.staticTree){				  //静态树					
					if(jqueryObj.getNodeAttribute().expanded){
						$.dqgb.tree.collapseNode(jqueryObj);
					}else{
						$.dqgb.tree.expandNode(jqueryObj);
					}
				}else{								  //动态树
					if(jqueryObj.getNodeAttribute().expanded){
						$.dqgb.tree.collapseNode(jqueryObj);
					}else{
						if(options.async){				  //指明每次加载数据时重新刷新
							if(jqueryObj.getNodeAttribute().status != "loading"){
								jqueryObj.setNodeAttribute({								//防止网络延时时用户重复点击
									status:'loading'
								});
								$.ajax({
									url:options.url,
									type:'post',
									async:true,
									data:{
										ID:jqueryObj.getNodeAttribute().id
									},
									success:function(data){
										var sub = jqueryObj.getDomElement().next("div[parentNodeId='" + jqueryObj.getNodeAttribute().id + "']");
										if(sub.length == 1){
											sub.get(0).innerHTML = "";
											sub.html("");
										}
										$.dqgb.tree.getStaticDqgbTree(jqueryObj,data[options.root],options);
										$.dqgb.tree.expandNode(jqueryObj);
										jqueryObj.setNodeAttribute({
											status:'loaded'
										});
									}
								});
							}
						}else{							//动态加载，只加载一次
							if(jqueryObj.getNodeAttribute().status != "loaded"){
								if(jqueryObj.getNodeAttribute().status != "loading"){
									jqueryObj.setNodeAttribute({								//防止网络延时时用户重复点击
										status:'loading'
									});
									$.ajax({
										url:options.url,
										type:'post',
										async:true,
										data:{
											ID:jqueryObj.getNodeAttribute().id
										},
										success:function(data){
											$.dqgb.tree.getStaticDqgbTree(jqueryObj,data[options.root],options);
											$.dqgb.tree.expandNode(jqueryObj);
											jqueryObj.setNodeAttribute({
												status:'loaded'
											});
										}
									});
								}
							}else{
								$.dqgb.tree.expandNode(jqueryObj);
							}
						}
					}
				}
			};
			
			jqueryObj.exp_coll = $("<div class='dqgb_tree_icon'></div>")
				.appendTo(node).addClass("dqgb_tree_11")
				.click(function(){
					expannd_collapse();
				});
			
			jqueryObj.icon = $("<div class='dqgb_tree_icon'></div>")
					.appendTo(node).addClass(jqueryObj.getNodeAttribute().iconCls)
					.dblclick(function(){
						expannd_collapse();
					});
			
			jqueryObj.name = $("<div style='margin-left:" + $.dqgb.tree.options.textMargin + "px; white-space:nowrap;" +
						"float:left; cursor:pointer;'></div>").appendTo(node)
						.addClass("dqgb_tree_node text").html(attr.text)
						.click(function(){
							jqueryObj.removeSelected();
							$(this).addClass("dqgb_tree_text_click");
							if($.isFunction(jqueryObj.getNodeAttribute().click)){
								jqueryObj.getNodeAttribute().click.call();
							}
						}).dblclick(function(){
							expannd_collapse();
							if($.isFunction(jqueryObj.getNodeAttribute().dblclick)){
								jqueryObj.getNodeAttribute().dblclick.call();
							}
						});
			
			return jqueryObj;
		},
		
		/*******************************
		 * 
		 * @TODO	支持用户自定义菜单的行为
		 * 
		 */
		beforeCreateNode : function(obj){

		},
		/**********************************
		 * 
		 * @TODO	删除tree节点
		 * 
		 */
		removeChildNode : function(obj){
			if(obj.getNodeAttribute().leaf){
				var sub = obj.getDomElement().next("div[parentNodeId='" + obj.getNodeAttribute().id + "']");
				sub.remove();
			}
			var pre = $.dqgb.tree.getPreviousNode(obj); 
			var pNode = obj.parentNode;
			pNode.removeSubNode(obj);
			obj.getDomElement().remove();
			pre ? $.dqgb.tree.redrawNode(pre) : "";
			$.dqgb.tree.redrawNode(pNode);
		},
		/****************
		 * 
		 * @TODO	调整tree窗口的尺寸
		 * 
		 */
		resizeParent : function(obj,container){
			var icon = obj.getDomElement().children(".dqgb_tree_icon");
			var width = 0;
			for(var i = 0; i < icon.length; i++){
				width += $(icon[i]).width();
			}
			var tex = obj.getDomElement().children(".text");
			width += tex.width();
			
			var pw = obj.getDomElement().parent().width();
			if(pw == "auto" || pw < width){
			//	obj.getDomElement().parent().width(width + $.dqgb.tree.options.textMargin + 10);
				obj.getDomElement().width(width + $.dqgb.tree.options.textMargin + 10);
				container.children(".subNode").width(width + $.dqgb.tree.options.textMargin + 10);
			}
			return width;
		},
		/******************************************************
		 * 
		 * @TODO		添加静态树子节点
		 * @PARAM		father：父节点，attr：子节点的属性,
		 * 				options:节点配置
		 * 
		 *****************************************************/
		appendChildNode : function(father,attr,options){
			var son = this.createTreeNode(father,attr,options);
			var children = father.getDomElement().next("div[parentNodeId='" + father.getNodeAttribute().id + "']");
			if(children && children.length == 1){
				children.append(son.getDomElement());
			}else{
				var child = $("<div class='subNode'></div>").insertAfter(father.getDomElement()).attr("parentNodeId",father.getNodeAttribute().id);
				child.append(son.getDomElement());
			}
			var list = son.getRoot().treeNode;			//向root中存储节点信息
			list[list.length] = son;
			
			if(!options.staticTree){
				father.addSubNode(son);
				$.dqgb.tree.redrawNode(son);
				$.dqgb.tree.getPreviousNode(son) ? $.dqgb.tree.redrawNode($.dqgb.tree.getPreviousNode(son)) : "";
				father.getNodeAttribute().leaf = false;
				$.dqgb.tree.redrawNode(father);
				son.getDomElement().slideDown(100,function(){
					$.dqgb.tree.resizeParent(son,options.container);
				});
			}else{
				son.getDomElement().show();
				this.resizeParent(son,options.container);
				father.addSubNode(son);
				this.redrawNode(son);
				this.getPreviousNode(son) ? this.redrawNode(this.getPreviousNode(son)) : "";
				father.getNodeAttribute().leaf = false;
				this.redrawNode(father);
			}
			return son;
		},
		/****************************
		 * 
		 * @TODO	展开某个节点
		 * 
		 ***************************/
		expandNode : function(node){
			function afterExpand(){
				node.setNodeAttribute({
					expanded : true
				});
				if(node.getNodeAttribute().id == $.dqgb.tree.options.rootNodeID){			//根节点
					node.exp_coll.addClass("dqgb_tree_10").removeClass("dqgb_tree_11");
				}else{
					if($.dqgb.tree.isLastNode(node)){
						node.exp_coll.addClass("dqgb_tree_4").removeClass("dqgb_tree_5");
					}else{
						node.exp_coll.addClass("dqgb_tree_6").removeClass("dqgb_tree_8");
					}
				}
			};
			var children = node.getDomElement().next("div[parentNodeId='" + node.getNodeAttribute().id + "']");
			if(children && children.length == 1){
				children.slideDown(100,function(){
					afterExpand();
				});
			}else{
				afterExpand();
			}
		},
		/****************************
		 * 
		 * @TODO	闭合某个节点
		 * 
		 ***************************/
		collapseNode : function(node){
			function afterCollapse(){
				node.setNodeAttribute({
					expanded : false
				});
				if(node.getNodeAttribute().id == $.dqgb.tree.options.rootNodeID){			//根节点
					node.exp_coll.addClass("dqgb_tree_11").removeClass("dqgb_tree_10");
				}else{
					if($.dqgb.tree.isLastNode(node)){
						node.exp_coll.removeClass("dqgb_tree_4").addClass("dqgb_tree_5");
					}else{
						node.exp_coll.removeClass("dqgb_tree_6").addClass("dqgb_tree_8");
					}
				}
			};
			var children = node.getDomElement().next("div[parentNodeId='" + node.getNodeAttribute().id + "']");
			if(children && children.length == 1){
				children.slideUp(100,function(){
					afterCollapse();
				});
			}else{
				afterCollapse();
			}
		},
		/*********************
		 * 
		 * @TODO	绘制某个节点前面的空白区域
		 * 
		 */
		redrawNode : function(node){
			if(node.getNodeAttribute().expanded){				//如果是展开的状态
				if(node.getNodeAttribute().id == $.dqgb.tree.options.rootNodeID){			//根节点
					node.exp_coll.addClass("dqgb_tree_10").removeClass("dqgb_tree_11");
				}else{
					if(this.isLastNode(node)){					//如果该节点是最后一个节点
						if(node.getNodeAttribute().leaf){
							node.exp_coll.addClass("dqgb_tree_2").removeClass("dqgb_tree_11");
						}else{
							node.exp_coll.addClass("dqgb_tree_4").removeClass("dqgb_tree_11");
						}
					}else{
						if(node.getNodeAttribute().leaf){
							node.exp_coll.addClass("dqgb_tree_1").removeClass("dqgb_tree_11").removeClass("dqgb_tree_2");
						}else{
							node.exp_coll.addClass("dqgb_tree_6").removeClass("dqgb_tree_11").removeClass("dqgb_tree_4");
						}
					}
				}
			}else{												//如果是闭合的状态
				if(node.getNodeAttribute().id == $.dqgb.tree.options.rootNodeID){			//根节点
					node.exp_coll.addClass("dqgb_tree_11").removeClass("dqgb_tree_10");
				}else{
					if(this.isLastNode(node)){					//如果该节点是最后一个节点
						if(node.getNodeAttribute().leaf){
							node.exp_coll.addClass("dqgb_tree_2").removeClass("dqgb_tree_11");
						}else{
							node.exp_coll.addClass("dqgb_tree_5").removeClass("dqgb_tree_11");
						}
					}else{
						if(node.getNodeAttribute().leaf){
							node.exp_coll.addClass("dqgb_tree_1").removeClass("dqgb_tree_11").removeClass("dqgb_tree_2");
						}else{
							node.exp_coll.addClass("dqgb_tree_8").removeClass("dqgb_tree_11").removeClass("dqgb_tree_5");
						}
					}
				}
			}
			var temp = node;
			for(var i = 0; i < node.getNodeAttribute().level; i++){
				if(node.parentNode){
					temp = temp.parentNode;
					if(!this.isLastNode(temp)){
						var space = node.getDomElement().children(".space");
						$(space[space.length - 1 - i]).addClass("dqgb_tree_3");
					}
					if(temp.getNodeAttribute().level == 1){
						break;
					}
				}else{
					break;
				}
			}
			return node;
		},
		
		/***************************************************
		 * 
		 * @TODO	判断某个节点是否是同级中的最后一个节点
		 * 
		 */
		isLastNode : function(node){
			if(!node.parentNode){
				return true;
			}
			var subNode = node.parentNode.getSubNodes();
			if(subNode.length == 0)
				return false;
			return node == subNode[subNode.length - 1];
		},
		
		/***************************************************
		 * 
		 * @TODO	查找同级中的前一个节点
		 * 
		 */
		getPreviousNode : function(node){
			var subNode = node.parentNode.getSubNodes();
			if(subNode.length == 1){
				return null;
			}else{
				var pre;
				for(var i = 0; i < subNode.length; i++){
					if(node == subNode[i]){
						return pre;
					}
					pre = subNode[i];
				}
			}
		},
		/***************************************************
		 * 
		 * @TODO	展开所有节点
		 * 
		 */
		expandAll : function(root){
			this.expandNode(root);
			var subNode = root.getSubNodes();
			for(var i = 0; i < subNode.length; i++){
				if(subNode[i].getNodeAttribute().leaf == false){
					this.expandNode(subNode[i]);
					this.expandAll(subNode[i]);
				}
			}
		},
		/***************************************************
		 * 
		 * @TODO	闭合所有节点
		 * 
		 */
		collapseAll : function(root){
			var subNode = root.getSubNodes();
			for(var i = 0; i < subNode.length; i++){
				if(subNode[i].getNodeAttribute().leaf == false){
					this.collapseNode(subNode[i]);
					this.collapseAll(subNode[i]);
				}
			}
			this.collapseNode(root);
		},
		
		/***************************************************
		 * 
		 * @TODO	生成一棵树
		 * 
		 */
		getDqgbTree : function(options){
			var root = $.dqgb.tree.createRootNode(options);
			if(options.staticTree){											//静态树
				this.getStaticDqgbTree(root,options.treeData,options);
				return root;
			}
			return root;
		},
		/***************************************************
		 * 
		 * @TODO	生成一棵静态树
		 * @PARAM	options:节点配置信息
		 */
		getStaticDqgbTree : function(parent,data,options){
			var nodes = [];
			if(!data){
				return;
			}
			for(var i = 0; i < data.length; i++){
				nodes[nodes.length] = this.appendChildNode(parent,data[i],options);
			}
			for(var i = 0; i < nodes.length; i++){
				if(nodes[i].getNodeAttribute().children){
					this.getStaticDqgbTree(nodes[i],nodes[i].getNodeAttribute().children,options);
				}
			}
		}
	});
})(jQuery);

