/**
 * Store for users
 */
Ext.define('Admin.store.UserStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.User',
    model : 'Admin.model.User',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listUsers',
        reader : {
            type : 'json'
        }
    }
});
