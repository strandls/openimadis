/**
 * store holding image's metadata
 */
Ext.define('Manage.store.ImageMetaDatas', {
	extend: 'Ext.data.Store',
	model: 'Manage.model.ImageMetaData',

	autoLoad : false,
	fields : ['field', '0', '1'],

	data: [],

	proxy : {
		type : 'memory'
	}
});
