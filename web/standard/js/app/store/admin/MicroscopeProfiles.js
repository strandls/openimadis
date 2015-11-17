/**
 * Store for profiles 
 */
Ext.define('Manage.store.admin.MicroscopeProfiles', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.MicroscopeProfile',

    storeId: 'admin.MicroscopeProfiles', // required to refer to the store elsewhere

    autoLoad : false,
    proxy : {
        type : 'ajax',
        url : '../admin/listAcqProfiles',
        reader : {
            type : 'json'
        }
    }
});
