/**
 * Store for Profile Length Unit
 */
Ext.define('Admin.store.ProfileLengthUnitStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.ProfileLengthUnit',
    model : 'Admin.model.ProfileLengthUnit',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listLengthUnits',
        reader : {
            type : 'json'
        }
    }
});
