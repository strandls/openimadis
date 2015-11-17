Ext.define('Manage.store.admin.Cache', {
	extend: 'Ext.data.Store',
	model: 'Manage.model.admin.Cache',

    autoLoad : true,
    
    storeId: 'admin.Cache',
    
    proxy: {
        type: 'ajax',
        url: '../admin/getCacheStatus',
        reader: 'json'
    }

});