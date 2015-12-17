/**
 * Store for projects 
 */
Ext.define('Admin.store.ProjectStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.Project',
    model : 'Admin.model.Project',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listProjects',
        reader : {
            type : 'json'
        }
    }
});
