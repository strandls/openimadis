/**
 * Store for AuthCodes 
 */
Ext.define('Manage.store.admin.AuthCodes', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.AuthCodes',

    storeId: 'admin.AuthCodes', // required to refer to the store elsewhere 

    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listAllAuthCodes',
        reader : {
            type : 'json'
        }
    }
});
