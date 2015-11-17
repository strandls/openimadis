/**
 * Model capturing a project 
 */
Ext.define('Manage.model.admin.Project', {
    extend : 'Ext.data.Model',
    fields : ['name', 'notes', 'status', 'createdBy', 'creationDate', 'noOfRecords', 'spaceUsage', 'storageQuota']
});
