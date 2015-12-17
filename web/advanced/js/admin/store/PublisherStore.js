/**
 * Store for publishers 
 */
Ext.define('Admin.store.PublisherStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.Publisher',
    model : 'Admin.model.Publisher',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listPublishers',
        reader : {
            type : 'json'
        }
    }
});
