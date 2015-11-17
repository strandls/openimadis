/**
 * Store for the units
 */
Ext.define('Manage.store.admin.Units', {
	extend: 'Ext.data.Store',
	model: 'Manage.model.admin.Unit',

	storeId: 'admin.Units', // required to refer to the store elsewhere

	autoLoad: true,
	proxy: {
		type: 'ajax',
		url: '../admin/listUnits',
		reader: {
			type: 'json'
		}
	}
});
