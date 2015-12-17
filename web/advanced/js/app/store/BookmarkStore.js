/**
 * The Bookmarks saved per user on the server.
 */
Ext.define('Manage.store.BookmarkStore', {
    extend: 'Ext.data.TreeStore',
    requires: 'Manage.model.Bookmark',   
    model:'Manage.model.Bookmark',
    autoLoad : false,
    root: {
        text: 'Bookmarks',
        id: 'myTree',
        expanded : false,
        bookmarkName: 'Bookmarks'
    },
    folderSort: true,
    sorters: [{
        property: 'text',
        direction: 'ASC'
    }]
});
