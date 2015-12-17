/**
 * Store for users
 */
Ext.define('Admin.store.LogStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.LogModel',
    model : 'Admin.model.LogModel',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listLogFiles',
        reader : {
            type : 'json',
            root: 'items',
            totalProperty: 'total'
        }
    }
});
