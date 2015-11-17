/**
 * Store for available units
 */
Ext.define('Manage.store.admin.AvailableUnits', {
    extend : 'Ext.data.Store',

    fields : ['name', 'availableSpace'],
    autoLoad : false,

    storeId: 'admin.AvailableUnits',

    data : [],
    proxy : {
        type : 'ajax',
        url : '../admin/getAvailableUnits',
        reader : {
            type : 'json'
        }
    }
});
