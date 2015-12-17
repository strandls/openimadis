/**
 * Store for Microscopes 
 */
Ext.define('Admin.store.MicroscopeStore', {
   extend : 'Ext.data.Store',
    requires : 'Admin.model.Microscope',
    model : 'Admin.model.Microscope',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listMicroscopes',
        reader : {
            type : 'json'
        }
    }
});
