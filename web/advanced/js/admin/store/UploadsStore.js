Ext.define('Admin.store.UploadsStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.Uploads',
    model : 'Admin.model.Uploads',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listAllUploads',
        reader : {
            type : 'json'
        }
    }
});