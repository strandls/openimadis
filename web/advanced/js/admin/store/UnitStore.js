/**
 * Store for units 
 */
Ext.define('Admin.store.UnitStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.Unit',
    model : 'Admin.model.Unit',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listUnits',
        reader : {
            type : 'json'
        }
    }
});
