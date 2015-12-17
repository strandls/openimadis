/**
 * Store for available users
 */
Ext.define('Admin.store.AvailableLDAPUsersStore', {
    extend : 'Ext.data.Store',
    fields : ['name', 'email'],
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
