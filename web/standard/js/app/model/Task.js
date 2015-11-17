/**
 * Model for a task
 */
Ext.define('Manage.model.Task', {
    extend: 'Ext.data.Model',
    fields: [
    	'taskId', 'owner', 'project', 'priority', 'appName', 
	'appVersion', 'scheduledTime', 'scheduledTimestamp','state','terminatePermission'],
    idProperty : 'taskId'
});

