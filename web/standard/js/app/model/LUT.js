/**
 * Model for LUT
 */
Ext.define('Manage.model.LUT', {
	extend: 'Ext.data.Model',
	
	fields: ['name', 'url'],
	idProperty: 'name'
});
