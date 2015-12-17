/**
 * Store for AuthCodes 
 */
Ext.define('Admin.store.AuthCodesStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.AuthCodes',
    model : 'Admin.model.AuthCodes',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listAllAuthCodes',
        reader : {
            type : 'json'
        }
    }
});