/**
 * Store for tokens for a particular user
 */
Ext.define('Manage.store.Tokens', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Token',    
    model: 'Manage.model.Token',
    autoLoad : false,
    proxy: {
        type: 'ajax',
        url: '../compute/listTokens',
        reader: 'json'
    }
});
