Ext.define('Manage.model.SummaryTable', {
	extend: 'Ext.data.Model',
	
	fields: [ 
		{name:'uploadedBy', type:'string'},
		{name:'sliceCount', type:'int'},
		{name:'frameCount', type:'int'},
		{name:'imageWidth', type:'int'}
	]

});
