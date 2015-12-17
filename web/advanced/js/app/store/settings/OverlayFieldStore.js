/**
 * Store for the selected fields to be used for fields to be used for field overlays
 */
Ext.define('Manage.store.settings.OverlayFieldStore', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.Field',
    autoLoad : true,
    data : [],
    proxy: {
        type: 'memory'
    }
});
