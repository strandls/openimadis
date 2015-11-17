/**
 * Store for projects 
 */
Ext.define('Manage.store.admin.Projects', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.Project',

    storeId: 'admin.Projects', // required to refer to the store elsewhere

    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listProjects',
        reader : {
            type : 'json'
        }
    }
});
