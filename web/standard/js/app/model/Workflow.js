/**
 * Model for a workflow. Contains name and description
 */
Ext.define('Manage.model.Workflow', {
    extend: 'Ext.data.Model',
    fields: ['name', 'description','version','appName','id','selected']
});
