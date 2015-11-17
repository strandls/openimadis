/**
 * Store for history of a particular record
 */
Ext.define('Manage.store.Historys', {
    extend: 'Ext.data.Store',
    model: 'Manage.model.History',
    autoLoad: false,
    pageSize : 10,
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
