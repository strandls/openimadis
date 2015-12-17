/**
 * Store for available units
 */
Ext.define('Admin.store.AvailableUnitsStore', {
    extend : 'Ext.data.Store',
    fields : ['name', 'availableSpace'],
    autoLoad : false,
    data : [],
    proxy : {
        type : 'ajax',
        url : '../admin/getAvailableUnits',
        reader : {
            type : 'json'
        }
    }
});
