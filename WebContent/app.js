Ext.application({
	views: [
	        'Main',
	        'DatabaseSelectionWindow',
	        'CreateCollectionWindow'
    ],
    models: [
             'Collection'
    ],
    stores: [
             'Collection'
    ],
    autoCreateViewport: true,
    name: 'datastore'
});
Ext.Loader.setConfig({
    enabled: true
});