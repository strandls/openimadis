/**
 * Window shown when the Search button is clicked
 */
Ext.define('Manage.view.SearchWindow', {
	extend: 'Ext.window.Window',
	xtype: 'searchwindow',
	alias: 'widget.searchwindow',

	requires: [
		'Manage.view.SearchPanel'
	],

	closable: true,
	closeAction: 'hide',
	frame: true,
	modal: true,
	title: 'Search',

	layout: {
		type: 'hbox',
		align: 'stretch'
	},

	height: 500,
	width: 800,

	bbar: {
		ui: 'footer',

		items:[{
			xtype: 'button',
			id: 'searchViewButton',
			text: 'Apply Selection'
		}]
	},

	items: [{
		xtype: 'searchPanel',
		flex: 1
	}, {
		xtype: 'thumbnails',
		store: 'SearchThumbnails',
		id: 'searchthumbnails',
		autoScroll: true,
	
		flex: 2
	}]

});

	
