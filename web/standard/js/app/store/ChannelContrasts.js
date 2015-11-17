/**
 * Store for ChannelContrast  
 */
Ext.define('Manage.store.ChannelContrasts', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.ChannelContrast',    
    model: 'Manage.model.ChannelContrast',

    autoLoad : false,

    proxy: {
        type: 'ajax',
        url: '../record/getContrastSettings',
        reader: 'json'
    }
});
