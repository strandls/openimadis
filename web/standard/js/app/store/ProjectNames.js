/**
 * Store for project names
 */
Ext.define('Manage.store.ProjectNames', {
    extend : 'Ext.data.Store',
    requires : 'Manage.model.ProjectName',
    model : 'Manage.model.ProjectName',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/getProjectsForUser',
        reader : {
            type : 'json'
        }
    }
});
