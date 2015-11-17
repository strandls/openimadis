/**
 * Store for clients 
 */
Ext.define('Manage.store.admin.Clients', {
    extend : 'Ext.data.Store',

    model : 'Manage.model.admin.Client',

    storeId: 'admin.Clients', // required to refer to the store elsewhere 

    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listClients',
        reader : {
            type : 'json'
        }
    }
});
