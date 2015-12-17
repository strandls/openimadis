/**
 * Store for selected users
 */
Ext.define('Admin.store.SelectedLDAPUsersStore', {
    extend : 'Ext.data.Store',
    fields : ['name'],
    autoLoad : false,
    data : [],
    proxy : {
        type : 'memory'
    }
});
