/**
 * The Bookmarks saved per user on the server.
 */
Ext.define('Manage.store.Bookmarks', {
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

