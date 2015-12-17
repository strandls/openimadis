/**
 * Store for clients 
 */
Ext.define('Admin.store.ClientStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.Client',
    model : 'Admin.model.Client',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listClients',
        reader : {
            type : 'json'
        }
    }
});
