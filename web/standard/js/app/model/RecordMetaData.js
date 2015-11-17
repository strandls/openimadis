/*
 * Model for record metadata.
 * This is a simple key - value table. The values 
 * are dynamically populated based on record selection
 */
Ext.define('Manage.model.RecordMetaData', {
	extend: 'Ext.data.Model',
	fields: ['name', 'value']
});
