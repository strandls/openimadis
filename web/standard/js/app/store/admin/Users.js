/**
 * Store for users
 */
Ext.define('Manage.store.admin.Users', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.User',

    autoLoad : true,
    pageSize : 30,

    storeId: 'admin.Users', // required to refer to the store elsewhere

    proxy : {
        type : 'ajax',
        url : '../admin/listUsers',
        reader : {
            type : 'json',
            root: 'items',
            totalProperty: 'total'
        }
    }
});
