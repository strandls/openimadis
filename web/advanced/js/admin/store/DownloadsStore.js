/**
 * Store for downloads 
 */
Ext.define('Admin.store.DownloadsStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.Downloads',
    model : 'Admin.model.Downloads',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listAllDownloads',
        reader : {
            type : 'json'
        }
    }
});