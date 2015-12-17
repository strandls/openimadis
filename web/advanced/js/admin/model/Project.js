/**
 * Model capturing a project 
 */
Ext.define('Admin.model.Project', {
    extend : 'Ext.data.Model',
    fields : ['name', 'notes', 'status', 'createdBy', 'creationDate', 'noOfRecords', 'spaceUsage', 'storageQuota']
});
