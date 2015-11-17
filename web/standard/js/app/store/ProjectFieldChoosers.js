/**
 * Meta store that is not attached to any view directly. On laoding, this
 * store pushes the loaded data to ProjectAvailableFields and ProjectSelectedFields stores
 */
Ext.define('Manage.store.ProjectFieldChoosers', {
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
            Ext.StoreManager.get('ProjectAvailableFields').loadData(availableData);
            var userData = data.user;
            Ext.StoreManager.get('ProjectSelectedFields').loadData(userData);
        }
    }
});


