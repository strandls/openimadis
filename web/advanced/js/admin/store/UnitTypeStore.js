/**
 * Store for type of units
 */
Ext.define('Admin.store.UnitTypeStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.UnitType',
    model : 'Admin.model.UnitType',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listUnitTypes',
        reader : {
            type : 'json'
        }
    }
});
