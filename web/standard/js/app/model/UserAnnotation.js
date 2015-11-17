/*
 * Model for user annotations
 * This is a simple key - value table. The values 
 * are dynamically populated based on record selection
 */
Ext.define('Manage.model.UserAnnotation', {
	extend: 'Ext.data.Model',

	fields: ['name', 'value']
});
