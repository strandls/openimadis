/**
 * Store for type of units
 */
Ext.define('Manage.store.admin.UnitTypes', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.UnitType',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listUnitTypes',
        reader : {
            type : 'json'
        }
    }
});
