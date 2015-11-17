/**
 * model for the navigator store
 */
Ext.define('Manage.model.Navigator', {
	extend: 'Ext.data.Model',

	fields: [
		'binned',
		'fieldName',
		'fieldType',
		'id', 
		{ name: 'isProjectNode', type: 'boolean'},
		'max',
		'min',
		'projectName', //optionally filled
		'text'
	]

});

