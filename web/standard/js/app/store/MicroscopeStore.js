Ext.define('Manage.store.MicroscopeStore', {
    extend : 'Ext.data.Store',
    requires : 'Manage.model.Microscope',
    model : 'Manage.model.Microscope',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listMicroscopes',
        reader : {
            type : 'json'
        }
    }
});