Ext.define('datastore.view.CreateCollectionWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.CreateCollectionWindow',
    height: 400,
    id: 'CreateCollectionWindow',
    modal:true,
    width: 600,
    layout: 'border',
    title: 'Define/Create a table',
    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            items: [
            	{
            		xtype:'label',
            		region: 'north',
            		flex:2,
            		text:'Give table name and structure with json format and initial record.\n\rEach json text must end with ";" if has more than one record.'
            	},
            	{
            		xtype:'form',
            		region: 'center',
            		flex:11,
            		id:'CreateCollectionForm',
            		layout:'anchor',
            		items:[
						{
							xtype:'textfield',
							fieldLabel: 'table',
							labelWidth:50,
							anchor: '50% 10%',
							name:'name',
							regex:/^((?![\/:*?"<>|@' \u4E00-\u9FA5\uF900-\uFA2D\uFF00-\uFFEF]).)*$/,
					    	regexText:"FORBIDDEN TO INPUT CHINESE OR　\/:*?\"<>|@'【space】"
						},
						{
							xtype:'textarea',
							fieldLabel: 'json text',
							labelWidth:50,
							name:'json',
							anchor: '100% 90%',
						}
            		]
            	},
            	{
            		xtype:'button',
            		text:'create',
            		region: 'south',
            		flex:1,
            		handler:function(){
            			var form=Ext.getCmp('CreateCollectionForm').getForm();
            			if(form.isValid()){
            				var text=form.findField('json').getValue();
            				if(Ext.String.endsWith(text,';')){
            					text.substr(0,text.length-1);
            				}
            				var arr=text.split(';');
            				for(var i=0;i<arr.length;i++){
            					if(!me.isValidJsonFormat(arr[i])){
            						Ext.Msg.alert("Error","invalid JSON");
            						return;
            					}
            				}
            				Ext.Ajax.request( {
        						url : 'do/createCollection',
        						params : {  
									db : Ext.getCmp('Main').DB,
									collection: form.findField('name').getValue(),
									json: text
								},
        					    success : function(response, options) {
        					    	Ext.getCmp('CreateCollectionWindow').close();
        					    	Ext.getStore('Collection').proxy.extraParams.db=Ext.getCmp('Main').DB;
        							Ext.getStore("Collection").load();
        							
        					    },
        					    failure: function(response, opts) {
        			             	Ext.Msg.alert("Error","createCollection request failure.");
        			            }
        					});
            			}
            		}
            	}
            ]
        });
        me.callParent(arguments);
    },
    isValidJsonFormat: function(text){
    	if(text.indexOf('{') == 0 && text.indexOf('}') > 0){
			var json={};
			try{
				json=Ext.decode(text);
	    	}catch(e){
	    		json=null;
	    	}
			return (json!=null && typeof(json) == "object" && Object.prototype.toString.call(json).toLowerCase() == "[object object]" && !json.length);
    	}
    	return false;
    }
});