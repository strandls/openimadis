/**
 * history type
 */
Ext.define('Manage.store.HistoryTypeStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.HistoryTypeModel',    
    model: 'Manage.model.HistoryTypeModel',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../record/getHistoryTypes',
        reader: 'json'
    }
});
