/**
 * store for user uploads
 */
Ext.define('Manage.store.admin.Uploads', {
    extend : 'Ext.data.Store',

    model : 'Manage.model.admin.Uploads',

    storeId: 'admin.Uploads', // required to refer to the store elsewhere 

    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listAllUploads',
        reader : {
            type : 'json'
        }
    }
});
