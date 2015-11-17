/**
 * Store for the selected fields to be used for navigation
 */
Ext.define('Manage.store.NavigatorSelectedFields', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.Field',
    autoLoad : true,
    data : [],
    proxy: {
        type: 'memory'
    }
});
