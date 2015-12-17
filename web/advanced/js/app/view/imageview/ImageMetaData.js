Ext.define('Manage.view.imageview.ImageMetaData', {
	extend:'Ext.grid.Panel',
	xtype:'imagemetadata',
	alias:'widget.imagemetadata',
	store:'Manage.store.ImageMetaDataList',
    columns: [
        { header: 'Image Field', dataIndex: 'field' }
    ]
});
