/**
 * The window shown when the download button is clicked.
 */
Ext.define('Manage.view.DownloadWindow', {
	extend: 'Ext.window.Window',
	xtype: 'downloadwindow',
	alias: 'widget.downloadwindow',

	requires: [
		'Manage.view.Downloads'
	],

	closable: true,
	closeAction: 'hide',
	modal: true,
	draggable: false,
	title: 'Links',

	height: 500,
	width: 1050,

	layout: {
		type: 'hbox',
		align: 'stretch'
	},

	items: [{
		xtype: 'downloads',
		style: 'background-color:white',
		flex: 5,
		autoScroll: true
		
	}, {
		//different store for the download thumbnails
		xtype: 'thumbnails',
		store: 'DownloadsThumbnails',
		autoScroll: true,
		flex: 2
	}],
	
	initComponent : function() {
		var me = this;
		var config = {
			task : {
				run : function() {
					if(me.isVisible())
						me.down('downloads').fireEvent('refresh');
				},
				interval : 15000
			}
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		Ext.TaskManager.start(this.task);
		this.callParent();		
	}

});
