/**
 * Model for a workflow category. Contains the name of the category
 * and the workflow items within it. Each workflow item in turn
 * consists of name and description.
 */
Ext.define('Manage.model.WorkflowCategory', {
    extend: 'Ext.data.Model',
    fields: ['name','folderID'],

    hasMany : {model : 'Manage.model.Workflow', name : 'workflows'}
});
