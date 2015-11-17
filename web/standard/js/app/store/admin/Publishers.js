/**
 * Store for publishers 
 */
Ext.define('Manage.store.admin.Publishers', {
    extend : 'Ext.data.Store',

    model : 'Manage.model.admin.Publisher',

    storeId: 'admin.Publishers', // required to refer to the store elsewhere 

    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listPublishers',
        reader : {
            type : 'json'
        }
    }
});
