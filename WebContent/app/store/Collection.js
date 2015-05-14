Ext.define('datastore.store.Collection', {
    extend: 'Ext.data.Store',

    requires: [
        'datastore.model.Collection'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            model: 'datastore.model.Collection',
            storeId: 'Collection',
            pageSize : 50,
            proxy: {
                type: 'ajax',
                afterRequest: function(request, success) {
                    if(!success){
                        Ext.Msg.alert('Error','collectionStore request failure');
                        return;
                    }else{
                    	if(request.action!='read'){
                    		var obj=request.proxy.reader.rawData;
                        	if(obj.success){
                        		switch (request.action)
        	    				{
        		    				case "destroy":
        		    					Ext.getStore("Collection").reload();
        		    					break;
        		    				default:
        		    					break;
        	    				}
                        	}else{
                        		Ext.Msg.alert('Error',obj.msg);
                        		Ext.getStore("Collection").reload();
                        	}
                    	}	
                    }
                },
                api: {
                    read: 'do/getCollections',
                    //destroy: ''
                },
                extraParams: {
                    db: ''
                },
                reader: {
                    type: 'json',
                    messageProperty: 'msg',
                    root: 'rows'
                },
                writer: {
                    type: 'json',
                    allowSingle: false
                }
            }
        }, cfg)]);
    }
});