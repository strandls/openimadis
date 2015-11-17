/**
 * The Bookmarks shown in the Selection Window
 */
Ext.define('Manage.store.BookmarksInSelection', {
	extend: 'Ext.data.TreeStore',

	model: 'Manage.model.Bookmark',
	autoLoad : false,

	proxy: {
		type: 'memory'
	},

	folderSort: true,
	sorters: [{
		property: 'text',
		direction: 'ASC'
	}]
});

