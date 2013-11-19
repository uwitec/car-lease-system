<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<title>年度报表</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/ext/resources/css/ext-all.css" />
	 	<script type="text/javascript" src="${pageContext.request.contextPath }/ext/adapter/ext/ext-base.js"></script>
	    <script type="text/javascript" src="${pageContext.request.contextPath }/ext/ext-all.js"></script>
	 
	 	<style>
			.titleCls{
					background-image: url("images/modify.gif") !important;
				}
		</style>
	 	<script>
	 	 function generateData(){
		        var data = [];
		        for(var i = 0; i < 12; ++i){
		            data.push([Date.monthNames[i], (Math.floor(Math.random() *  11) + 1) * 100]);
		        }
		        return data;
		    }
	
		    Ext.onReady(function(){

		    	Ext.QuickTips.init();
			    
		        var store = new Ext.data.ArrayStore({
		            fields: ['month', 'hits'],
		            data: generateData()
		        });
		        
		        var panel = new Ext.Panel({
			        iconCls:'titleCls',
			        frame:true,
			        bodyStyle:{
			        	border:"1px solid #99BBE8"
			        },
		            width: 700,
		            height: 400,
		            renderTo: document.body,
		            title: '年度报表',
		            tbar: [{
		                		text: '上一年',
		               	 		tooltip:'上一年',
		                		iconCls:'dqgb_prev',
		                		handler: function(){
		                    		store.loadData(generateData());
		                    		panel.setTitle("年度报表(2011)");
		                		}
		            		},
		            		{
			                	text: '下一年',
			               	 	tooltip:'下一年',
			               	 	disabled:true,
			                	iconCls:'dqgb_next',
			                	handler: function(){
			                    	store.loadData(generateData());
			                	}
			            	}
		            ],
		            style:{
						"position":"absolute",
						"left":"50%",
						"margin-left":"-350px",
						"top":"50%",
						"margin-top":"-200px"
		 	        },
		            items: {
		                xtype: 'columnchart',
		                store: store,
		                yField: 'hits',
		    	   		url: 'ext/resources/charts.swf',
		                xField: 'month',
		                xAxis: new Ext.chart.CategoryAxis({
		                    title: 'Month'
		                }),
		                yAxis: new Ext.chart.NumericAxis({
		                    title: 'Hits'
		                }),
		                extraStyle: {
		                   xAxis: {
		                        labelRotation: 0
		                    }
		                }
		            }
		        });
		    });
		 	
	 	</script>
	</head>
	
	<body>
		<div id="container"></div>
	</body>
</html>