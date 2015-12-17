/**
 * Store for channels for a particular record
 */
Ext.define('Manage.store.ChannelStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Channel',    
    model: 'Manage.model.Channel',
    autoLoad : false,
    data : [],
    proxy: {
        type: 'memory'
    }
});
