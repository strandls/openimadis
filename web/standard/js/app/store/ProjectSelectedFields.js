/**
 * Store for the selected Fields used to sort thumbnails 
 *  and the Columns shown in SummaryTable of Record Selection
 */
Ext.define('Manage.store.ProjectSelectedFields', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.Field',
    autoLoad : true,
    data : [],
    proxy: {
        type: 'memory'
    }
});

