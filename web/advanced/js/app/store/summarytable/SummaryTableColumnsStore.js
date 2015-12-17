/**
 * Meta store that is not attached to any view directly. On laoding, this
 * store pushes the loaded data to SummaryTableAvailableColumnsStore and
 * SummaryTableSelectedColumnsStore
 */
Ext.require([
    'Manage.store.summarytable.SummaryTableAvailableColumnsStore',
    'Manage.store.summarytable.SummaryTableSelectedColumnsStore'
]);

Ext.define('Manage.store.summarytable.SummaryTableColumnsStore', {
    extend: 'Ext.data.Store',
    autoLoad: false,
    fields : ['user', 'available'],
    proxy: {
        type: 'ajax',
        url: '../annotation/getFieldsChosen',
        reader: {
            type: 'json'
        }
    },
    listeners: {
        'load' :  function(store,records,options) {
            if (records === null || records.length != 1)
                return;
            var data = records[0].data;
            var availableData = data.available;
            Ext.StoreManager.get('Manage.store.summarytable.SummaryTableAvailableColumnsStore').loadData(availableData);
            var userData = data.user;
            Ext.StoreManager.get('Manage.store.summarytable.SummaryTableSelectedColumnsStore').loadData(userData);
        }
    }
});
