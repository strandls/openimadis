/**
 * The list of COLUMNS to show in the SummaryTable of Record Selection
 *  and sorting Thumbnails
 */
Ext.define('Manage.store.ProjectAvailableFields', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.Field',
    autoLoad: true,
    data : [],
    proxy: {
        type: 'memory'
    }
});

