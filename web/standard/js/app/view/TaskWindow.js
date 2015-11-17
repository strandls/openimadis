/**
 * Window shown when the Tasks button is clicked
 */
Ext.define('Manage.view.TaskWindow', {
	extend: 'Ext.window.Window',
	xtype: 'taskwindow',
	alias: 'widget.taskwindow',

	requires: [ 'Manage.view.TaskNavigator'],

	closable: true,
	closeAction: 'hide',
	frame: true,
	modal: true,
	draggable: false,
	title: 'Task',

	layout: {
		type: 'hbox',
		align: 'stretch'
	},

	height: 500,
	width: 800,


	items: [{
		xtype: 'taskNavigator',
		flex: 1
	}]

});

	
