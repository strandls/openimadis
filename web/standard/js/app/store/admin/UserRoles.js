/**
 * Store for roles of users
 */
Ext.define('Manage.store.admin.UserRoles', {
    extend : 'Ext.data.Store',

    model : 'Manage.model.admin.UserRole',

    storeId: 'admin.UserRoles',

    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listUserRoles',
        reader : {
            type : 'json'
        }
    }
});
