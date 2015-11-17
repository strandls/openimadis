/**
 * Window shown when the Project button is clicked
 */
Ext.define('Manage.view.ProjectWindow', {
	extend: 'Ext.window.Window',
	xtype: 'projectwindow',
	alias: 'widget.projectwindow',

	requires: [ 'Manage.view.ProjectListing'],

	closable: true,
	closeAction: 'hide',
	modal: true, 
	draggable: false,
	title: 'Choose Project',

	layout: 'fit',
	height: 500,
	width: 800,

	items: [{
		xtype: 'projectListing'
	}]
});

	

