/**
 * Store for Microscopes 
 */
Ext.define('Manage.store.admin.Microscopes', {
   extend : 'Ext.data.Store',
    model : 'Manage.model.admin.Microscope',

    autoLoad : true,

    storeId: 'admin.Microscopes', // required to refer to the store elsewhere

    proxy : {
        type : 'ajax',
        url : '../admin/listMicroscopes',
        reader : {
            type : 'json'
        }
    }
});
