/**
 * Meta store that is not attached to any view directly. On laoding, this
 * store pushes the loaded data to LegendAvailableFieldStore and
 * LegendSelectedFieldStore
 */
Ext.require([
    'Manage.store.LegendAvailableFieldStore',
    'Manage.store.LegendSelectedFieldStore'
]);

Ext.define('Manage.store.LegendFieldStore', {
    extend: 'Ext.data.Store',
    autoLoad: false,
    fields : ['user', 'available'],
    proxy: {
        type: 'ajax',
        url: '../annotation/getLegendFieldsChosen',
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
            Ext.StoreManager.get('Manage.store.LegendAvailableFieldStore').loadData(availableData);
            var userData = data.user;
            Ext.StoreManager.get('Manage.store.LegendSelectedFieldStore').loadData(userData);
        }
    }
});
