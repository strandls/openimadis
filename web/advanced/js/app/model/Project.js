/**
 * Model capturing a project
 */
Ext.define('Manage.model.Project', {
    extend: 'Ext.data.Model',
    fields: ['projectID', 'name', 'notes', 'noOfRecords', 'spaceUsage', 'storageQuota', 'status'],
    
    proxy: {
        type: 'ajax',
        url : '../project/list',
        reader: {
            type: 'json',
            root: 'items'
        }
    }
});
