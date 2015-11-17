/**
 * Window shown when the Tile Viewer Tasks button is clicked
 */
Ext.define('Manage.view.TileViewerWindow', {
	extend: 'Ext.window.Window',
	xtype: 'tileViewerWindow',
	alias: 'widget.tileViewerWindow',
	
	requires: [ 'Manage.view.TileViewerPanel'],

	closable: true,
	closeAction: 'hide',
	frame: true,
	modal: true,
	draggable: false,
	title: 'Tile Viewer Task',

	layout: {
		type: 'vbox',
		align: 'stretch'
	},

	height: 500,
	width: 800,


	items: [{
		xtype: 'toolbar',
		style: {
			border: 0,
			padding: 0
		},
		items: [{
			iconCls: "refresh",
			tooltip: 'Refresh List',
			handler : function() {
				this.up('tileViewerWindow').fireEvent('refreshList');
			}
		}]
	},{
		xtype: 'tileViewerPanel',
		flex: 1
	}]

});