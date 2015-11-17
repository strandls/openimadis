/**
 * The window shown when the navigator button is clicked.
 */
Ext.define('Manage.view.NavigatorWindow', {
	extend: 'Ext.window.Window',
	xtype: 'navigatorwindow',
	alias: 'widget.navigatorwindow',

	requires: [
		'Manage.view.NavigatorFieldChooser'
	],

	closable: true,
	closeAction: 'hide',
	modal: true,
	draggable: false,
	title: 'Navigator',

	//golden ration width/height
	height: 500,
	width: 800,
	layout:  {
		type: 'hbox',
		align: 'stretch'
	},

	bbar: {
		ui: 'footer',

		items: [{ 
			xtype: 'button', 
			text: 'Apply Selection',
			id: 'navigatorChangeButton'
		}]
	},

	items: [{
		xtype: 'navigator',
		style: 'background-color:white',
		flex: 2
		
	}, {
		xtype: 'tabpanel',
		id: 'navigatorViewArea',
		flex: 3,

		items:[{
			xtype: 'panel',
			title: 'Thumbnails',
			autoScroll: true,

			items: [{
				//different store for the navigator thumbnails
				xtype: 'thumbnails',
				store: 'NavigatorThumbnails',
				id: 'navigatorThumbnails',
				autoScroll: true,
				flex: 3
			}]
		}, {
			xtype: 'navigatorFieldChooser',
			title: 'Fields'
		}]
	}]

});
