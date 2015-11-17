/**
 * The list of fields for the showing the navigator.
 * TODO: This list will come from user selection 
 */
Ext.define('Manage.store.NavigatorAvailableFields', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.Field',
    autoLoad: true,
    data : [],
    proxy: {
        type: 'memory'
    }
});
