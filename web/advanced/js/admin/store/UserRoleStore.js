/**
 * Store for roles of users
 */
Ext.define('Admin.store.UserRoleStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.UserRole',
    model : 'Admin.model.UserRole',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listUserRoles',
        reader : {
            type : 'json'
        }
    }
});
