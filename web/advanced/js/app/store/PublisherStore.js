/**
 * Store for all the publishers available
 */
Ext.define('Manage.store.PublisherStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Publisher',    
    model: 'Manage.model.Publisher',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../compute/getPublishers',
        extraParams:{
			isWorkflow:false
		},
        reader: 'json'
    }
});
