/*
 * this store is loaded dynamically 
 */
Ext.define('Manage.store.TaskThumbnails', {
	extend: 'Ext.data.Store',
	requires: 'Manage.model.Thumbnail',    
	model: 'Manage.model.Thumbnail'
});
