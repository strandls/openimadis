/**
 * Store for the non-member users in a particular project
 */
Ext.define('Manage.store.admin.AvailableUsers', {
    extend : 'Ext.data.Store',

    fields : ['name'],
    autoLoad : false,

    storeId: 'admin.AvailableUsers',

    data : [],
    proxy : {
        type : 'ajax',
        url : '../admin/getAvailableMembers',
        reader : {
            type : 'json'
        }
    }
});
