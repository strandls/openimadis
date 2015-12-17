/**
 * Store for Profile Length Unit
 */
Ext.define('Admin.store.ProfileTypeStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.ProfileType',
    model : 'Admin.model.ProfileType',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listAcqProfileTypes',
        reader : {
            type : 'json'
        }
    }
});
