Ext.define('Manage.store.ExportFormats', {
    extend : 'Ext.data.Store',
    requires : 'Manage.model.ExportFormat',
    model : 'Manage.model.ExportFormat',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../project/listExportFormats',
        reader : {
            type : 'json'
        }
    }
});