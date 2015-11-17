/**
 * Store for completed imports of project
 */
Ext.define('Manage.store.CompletedImports', {
    extend: 'Ext.data.Store',
    model: 'Manage.model.Import',
    autoLoad : true,
    pageSize: 10,
    
    proxy: {
        type: 'ajax',
        url: '../project/getImportStatus',
        reader: {
            type: 'json',
            root: 'items',
            totalProperty: 'total'
        }
    }
});
