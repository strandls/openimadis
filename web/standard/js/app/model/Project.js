/**
 * Model capturing a project
 */
Ext.define('Manage.model.Project', {
    extend: 'Ext.data.Model',
    fields: ['projectID', 'name', 'notes', 'noOfRecords', 'spaceUsage', 'storageQuota', 'status']
    
});
