/**
 * List of available columns to choose from for the summary table
 */
Ext.define('Manage.store.LegendAvailableFieldStore', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.Field',
    autoLoad: true,
    data : [],
    proxy: {
        type: 'memory'
    }
});
