/**
 * Meta store that is not attached to any view directly. On laoding, this
 * store pushes the loaded data to NavigatorAvailableFields and NavigatorSelectedFields stores
 */
Ext.define('Manage.store.NavigatorFields', {
    extend: 'Ext.data.Store',
    autoLoad: false,
    fields : ['user', 'available'],
    proxy: {
        type: 'ajax',
        url: '../navigator/getFieldsChosen',
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
            Ext.StoreManager.get('NavigatorAvailableFields').loadData(availableData);
            var userData = data.user;
            Ext.StoreManager.get('NavigatorSelectedFields').loadData(userData);
        }
    }
});
