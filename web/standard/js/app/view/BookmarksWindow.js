/**
 * The window shown when the Bookmarks button is clicked
 */
Ext.define('Manage.view.BookmarksWindow', {
	extend: 'Ext.window.Window',
	xtype: 'bookmarkswindow',
	alias: 'widget.bookmarkswindow',

	requires: [
		'Manage.view.Bookmarks'
	],

	closable: true,
	closeAction: 'hide',
	modal: true,
	draggable: false,
	title: 'Bookmarks',

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
			handler: function() {
				this.up('bookmarkswindow').fireEvent('applyselection');
			}
		}]
	},

	items: [{
		xtype: 'panel',
		id: 'bookmarkPanel',
		flex: 1,

		layout: {
			type: 'vbox',
			align: 'stretch'
		},

		items: [{
			xtype: 'bookmarks',
			id: 'bookmarks',
			store: 'Bookmarks',
			flex: 1
		}]
	}, {
		xtype: 'thumbnails',
		store: 'BookmarksThumbnails',

		autoScroll: true,
	
		flex: 2
	}]
});


