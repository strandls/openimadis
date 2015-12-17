/**
 * Store for available users
 */
Ext.define('Admin.store.AvailableStore', {
    extend : 'Ext.data.Store',
    fields : ['name'],
    autoLoad : false,
    data : [],
    proxy : {
        type : 'ajax',
        url : '../admin/getAvailableMembers',
        reader : {
            type : 'json'
        }
    }
});
