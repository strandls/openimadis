/**
 * Store for status of projects
 */
Ext.define('Admin.store.ProjectStatusStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.ProjectStatus',
    model : 'Admin.model.ProjectStatus',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listProjectStatus',
        reader : {
            type : 'json'
        }
    }
});
