/**
 * Store for overlays for a particular record
 */
Ext.define('Manage.store.Overlays', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Overlay',    
    model: 'Manage.model.Overlay',
    autoLoad : false,
    data : [],
    proxy: {
        type: 'memory'
    }
});
