Ext.define('datastore.view.DatabaseSelectionWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.DatabaseSelectionWindow',
    height: 100,
    id: 'DatabaseSelectionWindow',
    width: 300,
    modal:true,
   // layout: 'border',
    closable:false,
    title:'DB ENTRY',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
        	items:[
				{
					xtype: 'tabpanel',
				    activeTab:0,
				    items:[
						{
							title:"select a DB",
							items:[
							{
								xtype:'combobox',
								id:'DatabaseCombo',
								triggerAction: 'all',
								fieldLabel: 'DB',
								mode:'remote',
								displayField:'name',
								store:new Ext.data.SimpleStore({
									proxy:new Ext.data.HttpProxy({
										type:'ajax',
										url:'do/getDatabases',
									}),
									fields:[{name : 'name',mapping : 'name'}],  
								}),
								emptyText : '--',
								selectOnFocus : true,
							    forceSelection : true,
								triggerAction:"all",
								editable:false,
								//typeAhead : true,
								labelWidth:30,
								padding:'10,4,4,0',
								listeners: {
									change : {
							    		fn: me.change,
							    		scope: me
							    	}
								}
							}       
							]
						},
						{
							title:"create a DB",
							items:[
							{
								xtype:'form',
								id:'CreateDBForm',
								layout:'hbox',
								items:[
									{
						        		xtype:'textfield',
						        		labelWidth:30,
										padding:'10,4,4,0',
										fieldLabel: 'DB',
										name: 'dbName',
										regex:/^((?![\/:*?"<>|@' \u4E00-\u9FA5\uF900-\uFA2D\uFF00-\uFFEF]).)*$/,
								    	regexText:"FORBIDDEN TO INPUT CHINESE OR　\/:*?\"<>|@'【space】",
									},
						            {
						                xtype: 'button',
						                icon: 'image/create.png',
					                    tooltip: 'create',
					                    padding:'10,4,4,0',
						                handler: function(button, event) {
						                	var form=Ext.getCmp('CreateDBForm').getForm();
						                	if(form.isValid()){
						                		var selecteddb=form.findField('dbName').getValue();
						                		Ext.Ajax.request( {
					        						url : 'do/existsCustomDB',
					        						params : {  
														db : selecteddb
													},
					        					    success : function(response, options) {
					        					    	var isexisting=JSON.parse(response.responseText);
					        					    	if(!isexisting){
						        					    	Ext.getCmp('Main').DB=selecteddb;
								        			    	Ext.getStore('Collection').proxy.extraParams.db=Ext.getCmp('Main').DB;
								        					Ext.getStore("Collection").load();
								        					Ext.getCmp('DatabaseSelectionWindow').close();
					        					    	}else{
					        					    		Ext.Msg.alert("Alert","The DB is already existing,change it again.");
					        					    	}
					        					    },
					        					    failure: function(response, opts) {
					        			             	Ext.Msg.alert("Error","existsCustomDB request failure.");
					        			            }
					        					});
						                	}
						                }
						            }
								]
							}
							]
						}
				    ]
				},
				{
					id:'Display',
					items:[
						  
					]
				},
        	]
        });
        me.callParent(arguments);
    },
    change : function( that, newValue, oldValue, eOpts ){
		Ext.getCmp('Main').DB=newValue;
    	Ext.getStore('Collection').proxy.extraParams.db=Ext.getCmp('Main').DB;
		Ext.getStore("Collection").load();
		Ext.getCmp('DatabaseSelectionWindow').close();
	}
});