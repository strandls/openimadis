/**
 * history type
 */
Ext.define('Manage.store.HistoryTypes', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.HistoryType',    
    model: 'Manage.model.HistoryType',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../record/getHistoryTypes',
        reader: 'json'
    }
});
