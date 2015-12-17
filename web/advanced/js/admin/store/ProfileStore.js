/**
 * Store for profiles 
 */
Ext.define('Admin.store.ProfileStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.Profile',
    model : 'Admin.model.Profile',
    autoLoad : false,
    proxy : {
        type : 'ajax',
        url : '../admin/listAcqProfiles',
        reader : {
            type : 'json'
        }
    }
});
