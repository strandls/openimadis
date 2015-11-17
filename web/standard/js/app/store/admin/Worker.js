/**
 * The store for the records shown when selecting the records for different operations
 * Example: share, transfer, delete, annotate, workflow
 */
Ext.define('Manage.store.admin.Worker', {
	extend: 'Ext.data.Store',
	model: 'Manage.model.admin.Worker',

	storeId : 'admin.Worker',
	
	proxy: 'memory',
	data: []
});

