/**
 * Store for history for a particular record
 */
Ext.define('Manage.store.HistoryStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.History',
    model: 'Manage.model.History',
    autoLoad : false,
    
    pageSize: 10,
    proxy: {
        type: 'ajax',
        url: '../record/getHistory',
        reader: {
            type: 'json',
            root: 'items',
            totalProperty: 'total'
        }
    }
});
