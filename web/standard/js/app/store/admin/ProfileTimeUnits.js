/**
 * Store for Profile Time Unit
 */
Ext.define('Manage.store.admin.ProfileTimeUnits', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.ProfileTimeUnit',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listTimeUnits',
        reader : {
            type : 'json'
        }
    }
});
