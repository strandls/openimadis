/**
 * Store for Profile Time Unit
 */
Ext.define('Admin.store.ProfileTimeUnitStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.ProfileTimeUnit',
    model : 'Admin.model.ProfileTimeUnit',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listTimeUnits',
        reader : {
            type : 'json'
        }
    }
});
