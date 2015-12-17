/**
 * Store for Profile Length Unit
 */
Ext.define('Manage.store.ProfileLengthUnitStore', {
    extend : 'Ext.data.Store',
    requires : 'Manage.model.ProfileLengthUnit',
    model : 'Manage.model.ProfileLengthUnit',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listLengthUnits',
        reader : {
            type : 'json'
        }
    }
});
