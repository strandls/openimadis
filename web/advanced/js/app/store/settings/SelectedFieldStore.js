/**
 * Store for the selected fields to be used for navigation
 */
Ext.define('Manage.store.settings.SelectedFieldStore', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.Field',
    autoLoad : true,
    data : [],
    proxy: {
        type: 'memory'
    }
});
