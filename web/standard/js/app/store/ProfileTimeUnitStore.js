/**
 * Store for Profile Time Unit
 */
Ext.define('Manage.store.ProfileTimeUnitStore', {
    extend : 'Ext.data.Store',
    requires : 'Manage.model.ProfileTimeUnit',
    model : 'Manage.model.ProfileTimeUnit',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listTimeUnits',
        reader : {
            type : 'json'
        }
    }
});
