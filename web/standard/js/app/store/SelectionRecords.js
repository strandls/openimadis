/**
 * The store for the records shown when selecting the records for different operations
 * Example: share, transfer, delete, annotate, workflow
 */
Ext.define('Manage.store.SelectionRecords', {
	extend: 'Ext.data.Store',
	model: 'Manage.model.RecordThumbnail',

	proxy: 'memory',
	data: []
});

