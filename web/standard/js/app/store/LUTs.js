/**
 * Store for the LUT's.
 */
Ext.define('Manage.store.LUTs', {
	extend: 'Ext.data.Store',
	requires: 'Manage.model.LUT',
	model: 'Manage.model.LUT',

	autoLoad: true,
	proxy: {
		type: 'ajax',
		url: '../record/getAvailableLUTs',
		reader: 'json'
	}
});
