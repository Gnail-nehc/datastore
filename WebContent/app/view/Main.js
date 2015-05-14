Ext.define('datastore.view.Main', {
    extend: 'Ext.container.Viewport',
    id: 'Main',
    layout: 'border',
    
    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
        	items:[
        	{
        		xtype: 'panel',
        		bodyStyle:"background-image:url('image/head.png')",
        		region: 'north',
        		flex:3
        	},
        	{
        		xtype: 'tabpanel',
        		id:'TableTab',
        		region: 'center',
				flex:60,
			    activeTab:0,
			    listeners: {
			        tabchange : {
			            fn: me.tabchange,
			            scope: me
			        }
			    },
			    items:[
				{
					title:"Table",
					layout: 'border',
					items:[
					    {
				            xtype: 'gridpanel',
				            flex:1,
				            region: 'west',
				            autoFill : true,
				            store: 'Collection',
				            stripeRows : true,
				            listeners: {
						        itemmousedown : {
						            fn: me.griditemmousedown,
						            scope: me
						        }
						    },
				            columns: [
								{
								    xtype: 'gridcolumn',
									flex: 2,
								    dataIndex: 'name',
								    text: 'name',
								}
				            ],
				            dockedItems: [
				            {
				                xtype: 'toolbar',
				                dock: 'top',
				                items: [
				                {
				                    xtype: 'button',
				                    handler: function(button, event) {
				                    	Ext.widget('CreateCollectionWindow').show();
				                    },
				                    icon: 'image/add.png',
				                    tooltip: 'create a table'
				                },
				                {
				                    xtype: 'tbseparator'
				                },
				                {
				                    xtype: 'button',
				                    handler: function(button, event) {
				                    	Ext.getStore('Collection').proxy.extraParams.db=Ext.getCmp('Main').DB;
				                		Ext.getStore("Collection").load();
				                    },
				                    icon: 'image/refresh.png',
				                    tooltip: 'refresh'
				                }]
				            }]
				        },
				        {
				        	xtype:'gridpanel',
				        	id:'DataGrid',
				        	displayInfo : true,
				        	region: 'center',
				        	columns:[],
				        	flex:4,
				        	title:'Data',
				        	dockedItems:[
								{
									xtype:'toolbar',
									dock:'bottom',
									items:[
									{
										xtype:'label',
										id:'TotalRecordsText',
										text:''
									}]
								}        
				        	]
				        }
					],
			        listeners: {
			            activate: {
			                fn: function(window, eOpts) {
			                },
			                scope: me
			            }
			        }
				}
				]
        	},
        	{
        		xtype: 'panel',
        		region: 'south',
				html : '<a color="white" href="mailto:gnail_nehc@aliyun.com">Contact Author</a><font color="white"> |  Copyright © 2015 LL</font>',
				bodyStyle:"background-image:url('image/foot.png')",
				flex:2
        	}],
        	listeners: {
        		afterrender : {
	        		fn: me.afterrender,
	        		scope: me
	        	}
        	}
        });
        me.callParent(arguments);
    },
    afterrender : function(){
    	Ext.widget('DatabaseSelectionWindow').show();
	},
	tabchange: function( tabPanel, newCard, oldCard, eOpts ){
		var tabName = newCard.title;
		switch(tabName)
		{
    		case "Table":
				Ext.getStore('Collection').load();
    			break;
		}
	},
	griditemmousedown: function(that, record, item, index, e, eOpts){
		var lm = new Ext.LoadMask(Ext.getCmp('DataGrid'), { 
			msg : 'hold on...', 
			removeMask : true
		}); 
		lm.show();
		Ext.Ajax.request({    
	        url:'do/findall4gridpanel',    
	        params:{
	        	db:Ext.getCmp('Main').DB,
	        	collection:record.raw.name
	        },    
	        success:function(response,config){
	        	lm.hide();
	        	var json=JSON.parse(response.responseText);    
	          	var store = new Ext.data.JsonStore({
	          		pageSize : 50,
	          		data:json.data,    
	          		fields:json.fieldNames,
	          	});
	          	Ext.getCmp('DataGrid').reconfigure(store,json.columnModels);
	          	Ext.getCmp('TotalRecordsText').setText("total: "+json.data.length);
	        },    
	        failure:function(){
	        	lm.hide();
	        	Ext.Msg.alert("Error","findall4gridpanel request failure.");
	        }    
		});
//		Ext.data.JsonP.request({    
//	        url:'http://localhost:46328/TestOperationService.svc/ServiceList',
//	        callbackKey: 'testclient_callback',
//	        success: function(response){
//	        	lm.hide();
//	        },    
//	        failure: function(response){
//	        	lm.hide();
//	        	Ext.Msg.alert("Error","findall4gridpanel request failure.");
//	        },
//	        callback: function(){
//	        	
//	        }
//		});
	}
});
