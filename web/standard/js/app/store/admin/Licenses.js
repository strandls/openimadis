/**
 * Store for AuthCodes 
 */
Ext.define('Manage.store.admin.Licenses', {
    extend : 'Ext.data.Store',

    model : 'Manage.model.admin.License',

    storeId: 'admin.Licenses', // required to refer to the store elsewhere 

    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listAllAcqLicenses',
        reader : {
            type : 'json'
        }
    }
});
