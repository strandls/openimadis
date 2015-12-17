/**
 * Store for clients for a user
 */
Ext.define('Manage.store.UserClientStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Client',    
    model: 'Manage.model.Client',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../compute/getUserClients',
        reader: 'json'
    }
});
