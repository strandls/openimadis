/**
 * View of the tree structure of bookmarks
 * Two of these components are created
 *  - in add Bookmarks Folder
 *  - in add records to Bookmarks
 */
Ext.define('Manage.view.Bookmarks', {
	extend: 'Ext.tree.Panel',
	xtype : 'bookmarks',
	alias: 'widget.bookmarks',

	//store - has to be a config option on creation

	bodyPadding : 5,
	rootVisible : true,
	viewConfig: {
		plugins: {
			ptype: 'treeviewdragdrop'
		}
	},
	useArrows: true
});

