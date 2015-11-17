/*
 * this store is loaded dynamically 
 */
Ext.define('Manage.store.BookmarksThumbnails', {
	extend: 'Ext.data.Store',
	requires: 'Manage.model.Thumbnail',    
	model: 'Manage.model.Thumbnail'

});

