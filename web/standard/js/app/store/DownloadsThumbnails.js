/*
 * this store is loaded dynamically by calling loadData
 * As an example check onImagesChanged in controller/Thumbnails
 */
Ext.define('Manage.store.DownloadsThumbnails', {
	extend: 'Ext.data.Store',
	requires: 'Manage.model.Thumbnail',    
	model: 'Manage.model.Thumbnail'

});
