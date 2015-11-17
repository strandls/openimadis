/**
 * Store for clients 
 */
Ext.define('Manage.store.admin.WebClients', {
    extend : 'Ext.data.Store',

    model : 'Manage.model.admin.Client',

    storeId: 'admin.WebClients', // required to refer to the store elsewhere 

    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listClients',
        reader : {
            type : 'json'
        }
    }
});
