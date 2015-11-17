/**
 * Window shown when the Project button is clicked
 */
Ext.define('Manage.view.settings.SettingsWindow', {
	extend: 'Ext.window.Window',
	xtype: 'settingswindow',
	alias: 'widget.settingswindow',

	requires: ['Manage.view.Settings'],

	closable: true,
	closeAction: 'hide',

	title : 'Settings',
	height: 500,
	width: 900,
	modal: true,
	draggable: false,
	layout: 'fit',
	items : [{
		xtype : 'settings'
	}]
});

	

