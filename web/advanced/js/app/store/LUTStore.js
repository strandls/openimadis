/**
 * Store for LUTs 
 */
Ext.define('Manage.store.LUTStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.LUT',    
    model: 'Manage.model.LUT',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../record/getAvailableLUTs',
        reader: 'json'
    }
});
