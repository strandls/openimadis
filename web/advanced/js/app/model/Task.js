/**
 * Model for a task
 */
Ext.define('Manage.model.Task', {
    extend: 'Ext.data.Model',
    fields: ['taskId', 'owner', 'project', 'priority', 'appName', 
				'appVersion', 'scheduledTime', 'state','terminatePermission'],
	idProperty : 'taskId'
});

