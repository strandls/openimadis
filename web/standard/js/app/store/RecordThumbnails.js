/**
 * The store for the records shown in the main view thumbnails.
 */
Ext.define('Manage.store.RecordThumbnails', {
	extend: 'Ext.data.Store',
	model: 'Manage.model.RecordThumbnail',

	proxy: 'memory',
	data: []
});
