Ext.define('Manage.view.ImageData', {
	extend: 'Ext.panel.Panel',
	xtype: 'imagedata',
	alias: 'widget.imagedata',

	requires: [
		 'Ext.layout.container.Accordion', 
		 'Manage.view.Attachments', 'Manage.view.Comments', 'Manage.view.History', 'Manage.view.ImageMetaData',
		 'Manage.view.RecordMetaData', 'Manage.view.UserAnnotations'
	],

	layout: 'accordion',

	minWidth: 300,

	items: [{
		xtype: 'attachments',
		title: 'Attachments'
	}, {
		xtype: 'comments',
		title: 'Comments'
	}, {
		xtype: 'history',
		title: 'History'
	}, {
		xtype: 'imagemetadata',
		title: 'Image Metadata'
	}, {
		xtype: 'recordmetadata',
		title: 'Record Metadata'
	}, {
		xtype: 'userannotations',
		title: 'User Annotation'
	}]

});


