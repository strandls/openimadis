/**
 * Store for the record Meta Data.
 * This is a simple key-value table of all the data
 */
Ext.define('Manage.store.RecordMetaDatas', {
	extend: 'Ext.data.Store',
	model: 'Manage.model.RecordMetaData',

	autoLoad: true,
	data: [],

	proxy: {
		type: 'memory'
	}
});
