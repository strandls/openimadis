/**
 * Store for AuthCodes 
 */
Ext.define('Admin.store.LicenseStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.License',
    model : 'Admin.model.License',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listAllAcqLicenses',
        reader : {
            type : 'json'
        }
    }
});