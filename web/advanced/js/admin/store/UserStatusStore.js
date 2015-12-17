/**
 * Store for status of users
 */
Ext.define('Admin.store.UserStatusStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.UserStatus',
    model : 'Admin.model.UserStatus',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listUserStatus',
        reader : {
            type : 'json'
        }
    }
});
