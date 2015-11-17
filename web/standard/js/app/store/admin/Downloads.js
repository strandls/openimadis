/**
 * Store for downloads 
 */
Ext.define('Manage.store.admin.Downloads', {
    extend : 'Ext.data.Store',

    model : 'Manage.model.admin.Downloads',

    storeId: 'admin.Downloads', // required to refer to the store elsewhere 

    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listAllDownloads',
        reader : {
            type : 'json'
        }
    }
});
