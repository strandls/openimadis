/**
 * Store of channels of a record
 */

Ext.define('Manage.store.Channels', {
	extend: 'Ext.data.Store',
	requires: 'Manage.model.Channel',
	model: 'Manage.model.Channel',

	autoLoad: false,

	data: [],
	proxy: {
		type: 'memory'
	}
});


