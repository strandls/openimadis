/**
 * The list of fields for the showing the navigator.
 * TODO: This list will come from user selection 
 */
Ext.define('Manage.store.NavigatorStore', {
    extend: 'Ext.data.TreeStore',
    autoLoad : false,
    data : [],
    proxy: {
        type: 'memory'
    }
});
