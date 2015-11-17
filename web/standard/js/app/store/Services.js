/**
 * Store for services allowed for a user
 */
Ext.define('Manage.store.Services', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Service',    
    model: 'Manage.model.Service',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../compute/getServices',
        reader: 'json'
    }
});
