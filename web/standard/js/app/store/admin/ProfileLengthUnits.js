/**
 * Store for Profile Length Unit
 */
Ext.define('Manage.store.admin.ProfileLengthUnits', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.ProfileLengthUnit',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listLengthUnits',
        reader : {
            type : 'json'
        }
    }
});
