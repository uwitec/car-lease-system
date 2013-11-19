/***********************************************************************************
 * 
 * @FILE		jquery.tree.2.0js
 * @AUTHOR		9528
 * @DATE		2011-6-15
 * @COPY-RIGHT	大庆金桥成都分公司
 * 
 * @TODO		创建jquery tree	
 * 				1.0版本采用div布局，2.0版本采用ul进行布局，
 * 				增加checkbox选择功能，	在配置项中添加checkTree : true即可
 * 				增加右键菜单功能			在配置项中添加popUpMenu : true即可
 * 				增加了更多的api,事件
 * 		
 * 
 ***********************************************************************************/


(function($){
	if(!$.xqb9528){
		$.xqb9528 = {};
	}
	
	$.extend($.xqb9528,{						//创建名字空间
		tree : {}
	});
	
	$.extend($.xqb9528.tree,{
		
		/***
		 * 生成tree
		 * @param options
		 */
		get9528Tree : function(options){
			var attr = {id : options.id,parentId:'',iconCls:options.iconCls,text:options.text,id:(options.id ? options.id : 'root'),isRoot:true};
			if(options.url){
				attr.url = options.url;
			}
			var node = this.createNode(attr,options);								//创建根节点
			node.generateDomElement();												//生成dom元素
			if(options.staticTree){
				this.generateStaticTree(node,options.subNodes,options);
			}
			node.resizeAll();														//调整节点宽度
			if(options.popUpMenu){													//绑定右键菜单
				var container = options.container ? options.container : $(document.body);
				container.shieldRightMenu().shieldSelectStart();
				node.popUpMenu = $.xqb9528.tree.getPopUpMenu(options.popUpMenu);
			}
			return node;
		},
		
		/***
		 * 构造静态树
		 * @param 	node		父节点
		 * @param 	data		子节点数据
		 * @param 	options
		 */
		generateStaticTree : function(node,data,options){
			var nodes = [];
			if(!data){
				return;
			}
			data.depth = node.getDepth() + 1;
			for(var i = 0; i < data.length; i++){
				nodes[nodes.length] = this.createNode(data[i],options);
				node.addNode(nodes[nodes.length - 1]);
			}
			for(var i = 0; i < nodes.length; i++){
				if(nodes[i].attr.subNodes){
					this.generateStaticTree(nodes[i],nodes[i].attr.subNodes,options);
				}
			}
		},
		
		/***
		 * 构造节点
		 * @param attr		节点信息
		 * @param options	配置信息
		 */
		createNode : function(attr,options){
			var node = {};
			//静态树
			if(options.staticTree){
				if((attr.subNodes && attr.subNodes.length != 0) || attr.isRoot)
					attr.leaf = false;
				else{
					attr.leaf = true;
				}
			}
			if(options.listeners && options.listeners.beforeCreateNode){
				if($.isFunction(options.listeners.beforeCreateNode)){
					options.listeners.beforeCreateNode.call(this,attr,options);
				}
			}
			/**扩展节点属性**/
			$.extend(node,{
				options : options,		//节点配置属性
				parent : "",			//父节点对象
				subNodes : [],			//子节点
				attr : attr,			//节点信息{id,isLeaf,iconCls,url,text}
				url : '',				//加载地址
				depth : 0,				//层级、深度,根节点的该参数值默认为0
				selected : false,		//文本区域没被选中
				subMenu : '',			//子菜单区域
				//复选框状态	normal，checked，someChecked(部分子节点被选中)
				checkStatus : 'normal',
				//节点折叠状态	collapsed：折叠，expanded：展开，collapsing：折叠中，expanding：展开中
				status : options.staticTree ? "expanded" : "collapsed"				
			});
			
			/**
			 * 扩展节点方法
			 */
			$.extend(node,{
				/***
				 * 设置父节点
				 * @param parent
				 */
				setParent : function(parent){
					this.parent = parent;
				},
				
				/**
				 * 获取父节点
				 */
				getParent : function(){
					return this.parent;
				},
				
				/**
				 * 设置节点层级
				 * @param depth
				 */
				setDepth : function(depth){
					this.depth = depth;
				},
				
				/**
				 * 获取当前节点的层级
				 * @returns
				 */
				getDepth : function(){
					return this.depth;
				},
				
				/**
				 * 数据动态加载时的连接地址
				 * @param url
				 */
				setURL : function(url){
					this.url = url;
				},
				
				/**
				 * 获取数据加载地址
				 */
				getURL : function(){
					return this.url;
				},
				
				/**
				 * 设置节点状态
				 * @param status
				 */
				setStatus : function(status){
					this.status = status;
				},
				
				/**
				 * 获取节点状态
				 */
				getStatus : function(){
					return this.status;
				},
				
				/**
				 * 设置节点属性
				 * @param attr
				 */
				setAttr : function(attr){
					this.attr = attr;
				},
				
				/**
				 * 获取节点属性
				 */
				getAttr : function(){
					return this.attr;
				},
				/**
				 * 调整当前节点dom元素的div的宽度
				 */
				resizeDom : function(){
					var twd = this.text.width();
					var wd = 18 * (this.depth + 1) + 20 + 16 + twd + (this.options.spaceWidth ? this.options.spaceWidth : 20);
					this.dom.width(wd);
					
					//在options中注册当前树中宽度最大的节点
					if(!this.options.maxWidth){
						this.options.maxWidth = wd;
					}else{
						if(wd > this.options.maxWidth){
							this.options.maxWidth = wd;
						}
					}
				},
				/**
				 * 调整所有节点dom元素的div的宽度
				 */
				resizeAll : function(){
					this.setNodeWidth(this.options.maxWidth);
				},
				/**
				 * 强行设置节点的宽度
				 */
				setNodeWidth : function(wd){
					this.options.maxWidth = this.options.maxWidth < wd ? wd : this.options.maxWidth;
					var all = this.getAllNodes();
					for(var i = 0; i < all.length; i++){
						all[i].dom.width(wd);
					}
				},
				
				/**
				 * 添加一个子节点,子节点在添加的时候才创建dom元素
				 * @param child
				 */
				addNode : function(child){
					this.subNodes.push(child);
					this.getAttr().leaf = false;							//修改节点属性
					this.status = "expanded";								//修改节点状态
					child.setParent(this);
					child.getAttr().parentId = this.attr.id;
					child.setDepth(parseInt(this.getDepth()) + 1);
					//静态树的id动态生成
					if(node.options.staticTree)
						child.attr.id = this.attr.id + this.subNodes.length;
					
					//绑定dom元素
					if(!this.subMenu){
						this.subMenu = $("<div style='vertical-align : center'></div>").insertAfter(this.dom);
					}
					child.generateDomElement();
					child.dom.appendTo(this.subMenu);
					
					this.reDraw();
					if(this.subNodes.length > 1){
						var sub = this.subNodes[this.subNodes.length - 2];
						sub.reDraw();
						var subs = sub.getAllChilds();
						for(var i = 0; i < subs.length; i++){
							subs[i].reDraw();
						}
					}
					child.reDraw();
				},
				
				/**
				 * 删除节点自身
				 * @param node
				 */
				remove : function(){
					/**
					 * 删除节点时先转移右键菜单
					 */
					if(node.getPopUpMenu() && node.getPopUpMenu().getNode() == this){
						node.getPopUpMenu().hidePopMenu();
						node.getPopUpMenu().dom.appendTo(node.getRoot().dom);
					}
					/**
					 * 删除子节点
					 */
					if(this.subNodes.length){
						this.subMenu.remove();
						this.subNodes = "";
					}
					if(this.parent && this != this.parent){
						for(var i = 0; i < this.parent.subNodes.length; i++){
							var sn = this.parent.subNodes[i];
							if(sn.getAttr().id == node.getAttr().id){
								this.parent.subNodes = this.parent.subNodes.slice(0,i).concat(this.parent.subNodes.slice(i + 1,this.parent.subNodes.length));
								break;
							}
						}
					}
					this.dom.remove();
					
					/**如果子节点个数为0 则该节点变成叶子节点*/
					if(this.parent && this.parent.subNodes.length == 0){			
						this.parent.getAttr().leaf = true;		//修改节点属性
						this.parent.reDraw();
					}
					if(this.parent && this.parent.subNodes.length != 0){			
						if(this.parent.subNodes.length){
							var pre = this.parent.subNodes[this.parent.subNodes.length - 1];
							pre.reDraw();
							var preSon = pre.getAllChilds();
							for(var i = 0; i < preSon.length; i++){
								preSon[i].reDraw();
							}
							
						}
					}
				},
				/***
				 * 判断当前节点是否是同级节点中的最后一个节点
				 */
				isLast : function(){
					if(!this.parent){
						return true;
					}
					return this == this.parent.subNodes[this.parent.subNodes.length - 1];
				},
				
				/**
				 * 把一个节点修改成一个叶子节点
				 * @returns {___anonymous3066_20155}
				 */
				leaf : function(){
					this.attr.leaf = true;
					this.ecItem.removeClass("xqb_tree_5 xqb_tree_8");
					if(this.isLast()){
						this.ecItem.addClass("xqb_tree_2");
					}else{
						this.ecItem.addClass("xqb_tree_1");
					}
					return this;
				},
				
				/**
				 * 展开节点
				 */
				expand : function(){
					/**
					 * 节点展开后回调函数
					 * @param n(节点)
					 */
					function afterExpand(n){
						n.status = "expanded";
						if(n.attr.isRoot){														//根节点
							n.ecItem.addClass("xqb_tree_10").removeClass("xqb_tree_11");
						}else{
							if(n.isLast()){
								n.ecItem.addClass("xqb_tree_4").removeClass("xqb_tree_5");
							}else{
								n.ecItem.addClass("xqb_tree_6").removeClass("xqb_tree_8");
							}
						}
					};
					if(this.subMenu && this.status == "collapsed"){
						this.status = "expanding";
						var n = this;
						this.subMenu.slideDown(1,function(){
							afterExpand(n);
						});
					}
				},
				
				/**
				 * 闭合节点
				 */
				collapse : function(){
					/**
					 * 节点闭合后回调函数
					 * @param n(节点)
					 */
					function afterCollapse(n){
						n.status = "collapsed";
						if(n.attr.isRoot){														//根节点
							n.ecItem.addClass("xqb_tree_11").removeClass("xqb_tree_10");
						}else{
							if(n.isLast()){
								n.ecItem.removeClass("xqb_tree_4").addClass("xqb_tree_5");
							}else{
								n.ecItem.removeClass("xqb_tree_6").addClass("xqb_tree_8");
							}
						}
					};
					
					if(this.subMenu && this.status == "expanded"){
						this.status = "collapsing";
						var n = this;
						this.subMenu.slideUp(1,function(){
							afterCollapse(n);
						});
					}
				},
				
				collapseAll : function(){
					var nodes = this.getAllChilds();
					for(var i = 0; i < nodes.length; i++){
						nodes[i].collapse();
					}
					this.collapse();
					return this;
				},
				
				expandAll : function(){
					var nodes = this.getAllChilds();
					for(var i = 0; i < nodes.length; i++){
						nodes[i].expand();
					}
					this.expand();
					return this;
				},
				
				/**
				 * 强制加载子项
				 */
				forceLoad : function(){
					this.ecItem.trigger("click");
					return this;
				},
				/**
				 * 重新绘制节点,执行对空白区域的重绘，同级前一个节点的重绘，父级节点的重绘
				 */
				reDraw : function(){
					if(this.status == "expanded"){
						if(this.attr.isRoot){														//根节点
							this.ecItem.addClass("xqb_tree_10").removeClass("xqb_tree_11");
						}else{
							if(this.isLast()){
								if(this.attr.leaf){
									this.ecItem.addClass("xqb_tree_2").removeClass("xqb_tree_6").removeClass("xqb_tree_11").removeClass("xqb_tree_4");
								}else{
									this.ecItem.addClass("xqb_tree_4").removeClass("xqb_tree_6").removeClass("xqb_tree_5").removeClass("xqb_tree_11");
								}
							}else{
								if(this.attr.leaf){
									this.ecItem.removeClass("xqb_tree_2").removeClass("xqb_tree_6").removeClass("xqb_tree_11").addClass("xqb_tree_1");
								}else{
									this.ecItem.removeClass("xqb_tree_4").removeClass("xqb_tree_8").removeClass("xqb_tree_11").addClass("xqb_tree_6");
								}
							}
						}
					}else{
						if(this.attr.isRoot){														//根节点
							this.ecItem.addClass("xqb_tree_11").removeClass("xqb_tree_10");
						}else{
							if(this.isLast()){
								if(this.attr.leaf){
									this.ecItem.addClass("xqb_tree_2").removeClass("xqb_tree_6").removeClass("xqb_tree_11");
								}else{
									this.ecItem.addClass("xqb_tree_5").removeClass("xqb_tree_6").removeClass("xqb_tree_11");
								}
							}else{
								if(this.attr.leaf){
									this.ecItem.removeClass("xqb_tree_2").removeClass("xqb_tree_11").removeClass("xqb_tree_6").addClass("xqb_tree_1");
								}else{
									this.ecItem.removeClass("xqb_tree_5").removeClass("xqb_tree_6").removeClass("xqb_tree_11").addClass("xqb_tree_8");
								}
							}
						}
					}
					
					var p = this.parent;
					var space = node.dom.children(".space");
					for(var i = 0; i < node.getDepth(); i++){
						$(space[space.length - 1 - i]).removeClass("xqb_tree_3");
						if(!p.isLast())
							$(space[space.length - 1 - i]).addClass("xqb_tree_3");
						p = p.parent;
					}
					
				},
				
				/**
				 * 获取text元素父级含有absolute/relative定位的元素，且其parent 是body,如果没有就返回body的jQuery对象
				 */
				getFixedParent : function(){
					function getParentDom(dom){
						var p = dom.parent();
						if(p.get(0).tagName == "BODY"){			//父级窗口为body则直接返回
							if(dom.css("position") == "absolute" || dom.css("position") == "relative"){
								return dom;
							}
							return $(document.body);
						}else{
							return getParentDom(p);
						}
					}
					return getParentDom(this.text);
				},
				
				/**
				 * 生成页面元素tree节点
				 * @param options
				 */
				generateDomElement : function(){
					$.extend(node,{
						/**节点的dom元素**/
						dom : $("<div class='xqb_tree_node'></div>").appendTo(node.options.container ? node.options.container : $(document.body)).shieldSelectStart(),
						ecItem : $("<div class='xqb_expand_collapse xqb_tree_11'></div>"),	//expand Collapse Item
						clsIcon : $("<div class='xqb_tree_default'></div>"),				//样式
						checkBox : '',														//复选框区域
						text : $("<div class='xqb_tree_text'></div>"),						//文本区域
						subMenu : ''														//子菜单区域
					});
					
					//使用自定义icon样式
					if(node.attr.iconCls){
						node.clsIcon.removeClass("xqb_tree_default").addClass(node.attr.iconCls);
					}else{
						if(node.attr.leaf){
							node.clsIcon.removeClass("xqb_tree_default").addClass("xqb_tree_leaf");
						}
					}
					
					node.dom.hover(function(){
						clearTimeout(node.popMenuTimer);
						if(node.getSelectedNode() == node){
							return;
						}
						$(this).css("background-color","#EEEEEE");
					},function(){
						if(node.options.popUpMenu && node == node.getPopUpMenu().getNode()){
							node.popMenuTimer = setTimeout(function(){
								node.getPopUpMenu().hidePopMenu();
							},200);
						}
						if(node.getSelectedNode() == node){
							return;
						}
						$(this).css("background-color","transparent");
					}).click(function(){
						
					});
					
					node.dom.data("node",node);			//节点对象存储进jQuery对象
					
					//创建space区域
					for(var i = 0; i < node.getDepth(); i++){
						$("<div class='xqb_expand_collapse space'></div>").appendTo(node.dom);
					}
					
					node.dom.append(node.ecItem);
					node.dom.append(node.clsIcon);
					if(node.options && node.options.checkTree){
						node.checkBox = $("<div class='xqb_tree_check_normal'></div>");
						node.checkBox.data("node",node);
						node.checkBox.treeCheckBox();
						node.dom.append(node.checkBox);
					}
					node.dom.append(node.text.html(node.attr.text));
					
					//调整宽度
					node.resizeDom();	
					
					//折叠操作
					node.ecItem.click(function(){
						if(node.attr.leaf){
							return;
						}
						if(node.status == "expanded"){
							node.collapse();
						}else if(node.status == "collapsed"){
							if(node.options.staticTree)
								node.expand();
							else{
								if(node.loadStatus == "loaded"){
									node.expand();
								}else if(node.loadStatus == "loading"){
									return;
								}else{
									node.loadStatus = "loading";
									$.ajax({
										url:node.options.url,
										async:true,
										type:'post',
										data:{
											ID : node.getNodeId()
										},
										success:function(data){
											if(data.result == 'FAILED'){
												if(nodes.length == 0){
													node.leaf();
												}
												node.loadStatus = "loaded";
												node.resizeAll();
												return;
											}
											var nodes = data[node.options.root];
											for(var i = 0; i < nodes.length; i++){
												if(nodes[i].leaf == "false"){
													nodes[i].leaf = false;
												}else{
													nodes[i].leaf = true;
												}
												nodes[i].depth = node.getDepth() + 1;
												var n = $.xqb9528.tree.createNode(nodes[i],node.options);
												node.addNode(n);
											}
											if(nodes.length == 0){
												node.leaf();
											}
											node.loadStatus = "loaded";
											node.resizeAll();
										}
									});
								}
							}
						}
					});
					//折叠操作
					node.clsIcon.dblclick(function(){
						node.ecItem.trigger("click");
					});
					//折叠操作
					node.text.dblclick(function(){
						node.ecItem.trigger("click");
					});
					
					//注册单击回调函数
					node.text.click(function(e){
						node.selectNode();
						if(node.options.listeners && $.isFunction(node.options.listeners.click)){
							node.options.listeners.click.call(this,node);
						}
						if(node.attr.click && $.isFunction(node.attr.click)){
							node.attr.click.call(this,node);
						}
					});
					
					
					//注册popUpMenu
					if(node.options.popUpMenu){
						node.text.mousedown(function(e){
							var ppNode = node.getPopUpMenu().getNode();
							if(ppNode){
								clearTimeout(ppNode.popMenuTimer);
							}
							if(e.button != 2){					//如果不是右键直接返回
								node.getPopUpMenu().hidePopMenu();
								return;
							}
							node.getPopUpMenu().showPopMenu(node);
						});
					}
					
					if(node.options.listeners && $.isFunction(node.options.listeners.afterCreateNode)){
						node.options.listeners.afterCreateNode.call(this,node);
					}
				},
				
				
				/**
				 * 选中节点，使其文本出现阴影效果
				 */
				selectNode : function(){
					if(this.selected){
						return;
					}
					var selected = this.getSelectedNode();
					if(selected){
						selected.selected = false;
						selected.dom.css({"background-color":"transparent"});
					}
					this.dom.css({"background-color":'#D9E8FB'});
					this.selected = true;
				},
				
				/**
				 * 获取当前树中的选中节点
				 */
				getSelectedNode : function(){
					var _root = this.getRoot();
					function findSelectedNode(root){
						if(root.selected){
							return root;
						}
						for(var i = 0; i < root.subNodes.length; i++){
							if(root.subNodes[i].selected){
								return root.subNodes[i];
							}
							if(root.subNodes[i].subNodes.length){
								var tmp = findSelectedNode(root.subNodes[i]);
								if(tmp){
									return tmp;
								}
							}
						}
						return "";
					};
					return findSelectedNode(_root);
				},
				/**
				 * 设置容器，把树放进元素"container"中
				 * @param container
				 */
				setContainer : function(container){
					node.dom.appendTo(container);
					node.subMenu.appendTo(container);
					return this;
				},
				
				/**
				 *	获取节点id 
				 */
				getNodeId : function(){
					return this.attr.id;
				},
				
				/**
				 * 根据id获取当前树中的节点
				 * @param id
				 * @returns
				 */
				getNodeById : function(id){
					var _root = this.getRoot();
					function findFromRoot(root,_id){
						if(root.attr.id == _id){
							return root;
						}
						for(var i = 0; i < root.subNodes.length; i++){
							if(root.subNodes[i].attr.id == _id){
								return root.subNodes[i];
							}
							if(root.subNodes[i].subNodes.length){
								var tmp = findFromRoot(root.subNodes[i],_id);
								if(tmp){
									return tmp;
								}
							}
						}
						return "";
					};
					return findFromRoot(_root,id);
				},
				
				/**
				 * 获取根节点
				 */
				getRoot : function(){
					var root = this;
					while(true){
						var temp = root.parent;
						if(!temp || temp == root){
							return root;
						}
						root = temp;
					}
				},
				/**
				 * 获取所有子节点
				 */
				getAllChilds : function(){
					var nodes = [];
					nodes = nodes.concat(this.subNodes);
					for(var i = 0; i < this.subNodes.length; i++){
						nodes = nodes.concat(this.subNodes[i].getAllChilds());
					}
					return nodes;
				},
				/**
				 * 获取当前树中所有节点
				 */
				getAllNodes : function(){
					var root = this.getRoot();
					var nodes = this.getRoot().getAllChilds();
					nodes.push(root);
					return nodes;
				},
				/**
				 * 获取被checked的节点
				 * @param all			默认只选叶子节点，all为true时包括非叶子节点
				 */
				getCheckedNodes : function(all){
					var nodes = this.getAllNodes();
					var n = [];
					if(!this.options.checkTree){
						return n;
					}
					for(var i = 0; i < nodes.length; i++){
						if(!all){
							if(nodes[i].attr.leaf == true && nodes[i].isChecked()){
								n.push(nodes[i]);
							}
						}else{
							if(nodes[i].isChecked()){
								n.push(nodes[i]);
							}
						}
					}
					return n;
				},
				/**
				 * 获取checked的节点组成的树
				 */
				getCheckedTree : function(){
					var nodes = this.getAllNodes();
					var n = [];
					if(!this.options.checkTree){
						return n;
					}
					for(var i = 0; i < nodes.length; i++){
						if(!nodes[i].isNormal()){
							n.push(nodes[i]);
						}
					}
					return n;
				},
				
				/**
				 * 获取右键菜单
				 */
				getPopUpMenu : function(){
					var root = this.getRoot();
					return root.popUpMenu ? root.popUpMenu : null;
				}
			});
			
			return node;
		},
		/**
		 * 生成一个弹出菜单,采用绝对定位进行布局,默认item高度26
		 * @param options
		 */
		getPopUpMenu : function(options){
			var pm = {};
			
			$.extend(pm,{
				options : options,
				items : [],							//存储item
				/**
				 * 获取子节点
				 * @returns {Array}
				 */
				getItems : function(){
					return this.items;
				},
				/**
				 * 生成dom元素
				 */
				generateDomElement : function(){
					$.extend(pm,{
						dom : $("<div style='position:absolute;border:1px solid #718BB7; background-color:#F0F0F0;'><div>").appendTo(pm.node.dom)
					});
					this.dom.width(1000);
					var maxWidth = 0;
					for(var i = 0; i < this.options.items.length; i++){
						var item;
						if(this.options.items[i] == "-"){
							item = this.getSeparator(i, this);
						}else{
							item = this.generateItem(this.options.items[i],i,this);
							if(item.getWidth() > maxWidth){
								maxWidth = item.getWidth();
							}
						}
						this.items.push(item);
						item.dom.appendTo(this.dom);
					}
					/**
					 * 调整item的宽度
					 */
					this.dom.height(0);
					for(var i = 0; i < this.items.length; i++){
						this.items[i].setWidth(maxWidth + 20);
						if($.browser.msie && $.browser.version == "6.0"){
							//ie6会自动调整高度
						}
						else{
							this.dom.height(this.dom.height() + this.items[i].getHeight());
						}
					}
					this.dom.width(maxWidth + 20);
					//标识菜单的dom元素已经创建了
					pm.created = true;
				},
				/**
				 * 生成分隔符
				 * @param	width	分隔符宽度
				 */
				getSeparator : function(index,menu){
					var item = {};
					$.extend(item,{
						index : index,
						popMenu : menu,
						dom : $("<div></div>").appendTo($(document.body)).height(6).css({
							'margin':'0px','padding':'0px','overflow':'hidden'
						}),
						sep : $("<div></div>").appendTo($(document.body)).addClass("xqb_ppMenu_sep")
					});
					item.sep.appendTo(item.dom).css({
						'margin':'2px 6px','padding':'0px'
					});
					$.extend(item,{
						/**
						 * 调整separator的宽度
						 * @param wd
						 */
						setWidth : function(wd){
							this.dom.width(wd);
							this.sep.width(wd - 12);
						},
						/**
						 * 获取高度
						 * @returns
						 */
						getHeight : function(){
							return this.dom.height();
						},
						/**
						 * item 始终处于disabled状态
						 * @returns {Boolean}
						 */
						isEnabled : function(){
							return false;
						} 
					});
					return item;
				},
				
				/**
				 * 生成一个item 高度为26
				 * @param options
				 * @param index		item在popMenu中位置
				 * @param menu		弹出菜单
				 */
				generateItem : function(options,index,menu){
					var item = {};
					$.extend(item,{
						options : options,
						index : index,
						enabled : (options.disabled ? false : true),
						popMenu : menu,
						dom :  $("<div style='cursor:pointer; position : relative; z-index : 2;'></div>").appendTo($(document.body)).height(26),
						icon : $("<div></div>").css({'margin':'3px 8px','float':'left','position':'relative', 'z-index':'2'}).width(20).height(20),
						text : $("<div></div>").css({'margin':'3px','overflow':'hidden',
							'line-height':'20px','font-size':'13px','position':'relative', 'z-index':'2',
							'float':'left'}).addClass("xqb_default_font").height(20)
					});
					/**
					 * 修正ie6双倍margin的bug
					 */
					if($.browser.msie && $.browser.version == "6.0"){
						item.icon.css("margin","3px 4px");
					}
					
					$.extend(item,{
						/**
						 * 布局
						 */
						doLayout : function(){
							this.icon.appendTo(this.dom);
							this.text.appendTo(this.dom).html(this.options.text);
							if(!this.isEnabled()){
								this.dom.css("cursor","default");
								this.text.addClass("xqb_disabled_item");
							}else{
								this.dom.css("cursor","pointer");
								this.text.removeClass("xqb_disabled_item");
							}
							if(this.options.iconCls){
								this.icon.addClass(this.options.iconCls);
							}
							this.width = this.icon.outerWidth(true) + this.text.outerWidth(true);
							
							this.dom.unbind("click").click(function(){
								if(!item.isEnabled()){
									return true;
								}
								item.popMenu.hidePopMenu();
								if(item.options.click && $.isFunction(item.options.click)){
									item.options.click.call(this,item);
								}
							}).unbind("hover").hover(function(){
								if(!item.isEnabled()){
									return true;
								}
								var shadow = item.popMenu.getShadow();
								shadow.setPosition({x : 2,y : 1}).show().setWidth(item.getWidth() - 4);
								shadow.dom.appendTo(item.dom);
							},function(){
								if(!item.isEnabled()){
									return true;
								}
								var shadow = item.popMenu.getShadow();
								shadow.hide();
							});
							return this;
						},
						/**
						 * 获取item 的宽度
						 * @returns
						 */
						getWidth : function(){
							return this.width;
						},
						/**
						 * 获取高度
						 * @returns
						 */
						getHeight : function(){
							return this.dom.height();
						},
						/**
						 * 设置宽度
						 * @param wd
						 */
						setWidth : function(wd){
							this.width = wd;
						},
						/**
						 * disable item
						 */
						disable : function(){
							this.enabled = false;
						},
						/**
						 * enable item
						 */
						enable : function(){
							this.enabled = true;
						},
						/**
						 * 判断item是否是enabled状态
						 */
						isEnabled : function(){
							return this.enabled;
						}
					});
					return item.doLayout();
				},
				
				/**
				 * 生成一个鼠标移动时显示的阴影button 采用绝对定位进行布局
				 */
				generateHoverShadow : function(){
					var shadow = {};
					$.extend(shadow,{
						timer : '',							//隐藏定时器
						dom : $("<div style='position:absolute; z-index : 1;'></div>").appendTo($(document.body)).height(24),
						left : $("<div class='xqb_shadowBtn_left'></div>").width(4).height(24),
						right : $("<div class='xqb_shadowBtn_right'></div>").width(4).height(24),
						middle : $("<div class='xqb_shadowBtn_middle'></div>").width(1).height(24)
					});
					$.extend(shadow,{
						/**
						 * 布局
						 */
						doLayout : function(){
							this.left.appendTo(this.dom);
							this.middle.appendTo(this.dom);
							this.right.appendTo(this.dom);
							return this;
						},
						/**
						 * 调整宽度
						 * @param width
						 */
						setWidth : function(width){
							this.dom.width(width);
							this.middle.width(width - 8);
							return this;
						},
						/**
						 * 调整位置
						 */
						setPosition : function(p){
							this.dom.css({"left" : p.x + "px",'top' : p.y + "px"});
							return this;
						},
						/**
						 * show
						 */
						show : function(){
							clearTimeout(this.timer);
							this.dom.show();
							return this;
						},
						/**
						 * hide
						 */
						hide : function(){
							this.timer = setTimeout(function(){
								shadow.dom.hide();
							},50);
							return this;
						}
					});
					
					return shadow.doLayout();
				},
				/**
				 * 获取popMenu中的shadowButton对象
				 * @returns
				 */
				getShadow : function(){
					if(!this.shadow){
						this.shadow = this.generateHoverShadow();
					}
					return this.shadow;
				},
				
				/**
				 * 显示弹出菜单
				 * @param node	当前节点
				 */
				showPopMenu : function(node){
					if(this.getNode() == node && this.dom.css("display") == "block"){
						return;
					}
					this.setNode(node);
					node.selectNode();
					if(!this.created){					//还没有创建
						this.generateDomElement();
					}else{
						this.dom.appendTo(node.dom);
					}
					/**
					 * 注册beforeShow事件
					 */
					if(this.options.beforeShow && $.isFunction(this.options.beforeShow)){
						this.options.beforeShow.call(this,pm,node);
					}
					var x = node.getDepth() * 18 + 54 + 4;
					this.dom.css({'left':(x + "px"),'top':'20px'}).show();
				},
				/**
				 * 绑定菜单到popMenu中
				 */
				setNode : function(node){
					if(this.getNode()){
						this.getNode().dom.css("z-index","1");	//放低原来的dom的层高	
						this.getNode().dom.removeClass("relative");
						this.getNode().text.removeClass("relative");
					}
					node.dom.css("z-index","2");				//拉高现在的dom的层高
					node.dom.addClass("relative");
					node.text.addClass("relative");
					this.node = node;
				},
				/**
				 * 获取绑定在popMenu中的菜单节点
				 */
				getNode : function(){
					return this.node;
				},
				/**
				 * 隐藏弹出菜单
				 */
				hidePopMenu : function(){
					if(!this.dom){
						return;
					}
					this.dom.hide();
					this.getNode().text.css("background-color","transparent");
					this.getNode().dom.removeClass("relative");
					this.getNode().text.removeClass("relative");
					if(this.getShadow()){
						this.getShadow().hide();
					}
				}
			});
			
			return pm;
		}
		
	});
	
	/**
	 * 注册tree的专用3态 checkbox 插件
	 */
	$.fn.treeCheckBox = function(){
		var node = this.data("node");
		$.extend(node,{
			/**
			 * 全选
			 */
			check : function(){
				if(node.options.listeners && node.options.listeners.beforeCheck){
					if($.isFunction(node.options.listeners.beforeCheck)){
						var ret = node.options.listeners.beforeCheck.call(this,node);
						if(!ret){
							return;
						}
					}
				}
				this.checkBox.removeClass("xqb_tree_check_normal").removeClass("xqb_tree_check_someChecked").addClass("xqb_tree_check_checked");
				this.checkStatus = "checked";
				//选中子节点
				var childs = this.subNodes;
				for(var i = 0; i < childs.length; i++){
					if(!childs[i].isChecked()){
						childs[i].check();
					}
				}
				//逐级修改父节点的状态
				if(this.parent && this.parent != this){
					if(!this.parent.isChecked()){
						if(this.parent.isAllChildChecked()){			//是否全部子节点都被选中
							this.parent.check();
						}else{											//由于当前节点已经被选中，所有parent.someCheck()
							if(!this.parent.isSomeChecked()){
								this.parent.someCheck();
							}
						}
					}
					
				}
				if(node.options.listeners && node.options.listeners.afterCheck){
					if($.isFunction(node.options.listeners.afterCheck)){
						node.options.listeners.afterCheck.call(this,node);
					}
				}
			},
			/**
			 * 取消选中
			 */
			unCheck : function(){
				if(node.options.listeners && node.options.listeners.beforeUnCheck){
					if($.isFunction(node.options.listeners.beforeUnCheck)){
						node.options.listeners.beforeUnCheck.call(this,node);
					}
				}
				this.checkBox.addClass("xqb_tree_check_normal").removeClass("xqb_tree_check_someChecked").removeClass("xqb_tree_check_checked");
				this.checkStatus = "normal";
				//取消选中子节点
				var childs = this.subNodes;
				for(var i = 0; i < childs.length; i++){
					if(!childs[i].isNormal()){
						childs[i].unCheck();
					}
				}
				//逐级修改父节点的状态
				if(this.parent && this.parent != this){
					if(!this.parent.isNormal()){
						if(this.parent.isAllChildUnChecked()){			//是否全部子节点都没有被选中
							this.parent.unCheck();
						}else{											//由于当前节点已经被取消选中，所有parent.someCheck()
							if(!this.parent.isSomeChecked()){
								this.parent.someCheck();
							}
						}
					}
				}
				if(node.options.listeners && node.options.listeners.afterUnCheck){
					if($.isFunction(node.options.listeners.afterUnCheck)){
						node.options.listeners.afterUnCheck.call(this,node);
					}
				}
			},
			/**
			 * 部分选中
			 */
			someCheck : function(){
				this.checkBox.removeClass("xqb_tree_check_normal").addClass("xqb_tree_check_someChecked").removeClass("xqb_tree_check_checked");
				this.checkStatus = "someChecked";
				if(this.parent && this.parent != this){
					if(!this.parent.isSomeChecked()){
						this.parent.someCheck();
					}
				}
			},
			/**
			 * 检测当前节点是处于没有选中状态
			 */
			isNormal : function(){
				return this.checkStatus == "normal";
			},
			/**
			 * 检测当前节点是否被选中
			 */
			isChecked : function(){
				return this.checkStatus == "checked";
			},
			/**
			 * 检测当前节点是否处于部分子节点被选中状态
			 */
			isSomeChecked : function(){
				return this.checkStatus == "someChecked";
			},
			/**
			 * 是否所有子节点都被选中,对于叶子节点始终返回true
			 */
			isAllChildChecked : function(){
				if(this.subNodes && this.subNodes.length != 0){
					var allChecked = true;
					for(var i = 0; i < this.subNodes.length; i++){
						if(!this.subNodes[i].isChecked()){					//有节点没有被选中
							return false;
						}
					}
				}
				return true;
			},
			/**
			 * 是否所有子节点都没有被选中,对于叶子节点始终返回true
			 */
			isAllChildUnChecked : function(){
				if(this.subNodes && this.subNodes.length != 0){
					var allUnChecked = true;
					for(var i = 0; i < this.subNodes.length; i++){
						if(!this.subNodes[i].isNormal()){					//有节点被中选中或者节点的子节点中有被选中的
							return false;
						}
					}
				}
				return true;
			}
			
		});
		
		this.click(function(){
			if(node.isChecked()){
				node.unCheck();
			}else{
				node.check();
			}
		});
		
		return this;
	};
	/**
	 * 屏蔽系统右键菜单
	 */
	$.fn.shieldRightMenu = function(){
		if($.browser.mozilla){
			this.get(0).oncontextmenu = function(){ 
			    return false; 
			}; 
		}else if($.browser.msie){
			this.get(0).oncontextmenu = function(e){
				return false;
			};
		}
		return this;
	};
	/***
	 * 屏蔽onselectstart 事件
	 */
	$.fn.shieldSelectStart = function(){
		if($.browser.mozilla){
			this.css("-moz-user-select","-moz-none");
		}else if($.browser.msie){
			this.get(0).onselectstart = new Function("event.returnValue=false;");
		}
		return this;
	};
	
})(jQuery);