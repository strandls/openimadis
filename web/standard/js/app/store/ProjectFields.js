/**
 * The fields of the records in the current project.
 * Is used to populate the drop down to sort the thumbnails.
 */
Ext.define('Manage.store.ProjectFields', {
	extend: 'Ext.data.Store',

	fields: ['name'],

	data: [],
	proxy: 'memory'
});
