Ext.define('Manage.store.ProfileStore', {
    extend : 'Ext.data.Store',
    requires : 'Manage.model.Profile',
    model : 'Manage.model.Profile',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listAcqProfiles',
        reader : {
            type : 'json'
        }
    }
});