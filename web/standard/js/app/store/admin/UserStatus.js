/**
 * Store for status of users
 */
Ext.define('Manage.store.admin.UserStatus', {
    extend : 'Ext.data.Store',

    model : 'Manage.model.admin.UserStatus',

    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listUserStatus',
        reader : {
            type : 'json'
        }
    }
});
