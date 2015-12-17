/**
 * Store for Profile Length Unit
 */
Ext.define('Manage.store.ProfileTypeStore', {
    extend : 'Ext.data.Store',
    requires : 'Manage.model.ProfileType',
    model : 'Manage.model.ProfileType',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listAcqProfileTypes',
        reader : {
            type : 'json'
        }
    }
});
