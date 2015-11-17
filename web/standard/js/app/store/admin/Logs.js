/**
 * Store for users
 */
Ext.define('Manage.store.admin.Logs', {
    extend : 'Ext.data.Store',

    model : 'Manage.model.admin.Log',

    storeId: 'admin.Logs', // required to refer to the store elsewhere

    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listLogFiles',
        reader : {
            type : 'json',
            root: 'items',
            totalProperty: 'total'
        }
    }
});
