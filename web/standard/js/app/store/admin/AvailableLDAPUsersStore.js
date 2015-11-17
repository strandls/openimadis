/**
 * Store for available users
 */
Ext.define('Manage.store.admin.AvailableLDAPUsersStore', {
    extend : 'Ext.data.Store',
    fields : ['name', 'email'],
    storeId : 'admin.availableLDAPUsersStore',
    autoLoad : false,
    data : [],
    proxy : {
        type : 'ajax',
        url : '../admin/getAvailableLDAPUsers',
        reader : {
            type : 'json'
        }
    }
});
