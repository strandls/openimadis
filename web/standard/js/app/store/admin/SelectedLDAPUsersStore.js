/**
 * Store for selected users
 */
Ext.define('Manage.store.admin.SelectedLDAPUsersStore', {
    extend : 'Ext.data.Store',
    storeId : 'admin.selectedLDAPUsersStore',
    fields : ['name'],
    autoLoad : false,
    data : [],
    proxy : {
        type : 'memory'
    }
});
