Ext.define('Manage.view.SummaryTable', {
	extend: 'Ext.grid.Panel',
	xtype: 'summarytable',
	alias: 'widget.summarytable',

	store: 'SummaryTables',

	columns: {
		defaults: {
			flex: 1
		},
		
		items: [{
			text: 'Uploaded By',
			dataIndex: 'uploadedBy'
		}, {
			text: 'Slice Count',
			dataIndex: 'sliceCount'
		}, {
			text: 'Frame Count',
			dataIndex: 'frameCount'
		}, {
			text: 'Image Width',
			dataIndex: 'imageWidth'
		}]
	}

});

