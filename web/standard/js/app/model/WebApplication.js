/**
 * Model for a workflow. Contains name and description
 */
Ext.define('Manage.model.WebApplication', {
    extend: 'Ext.data.Model',
    fields: ['name', 'description','version','url','id']
});
