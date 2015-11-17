/**
 * controls for all the Downloads related activity
 */
Ext.define('Manage.controller.Downloads', {
	extend: 'Ext.app.Controller',

	stores: [
		'DownloadsThumbnails', 'Downloads'
	],

	refs: [{
		ref: 'downloads',
		selector: 'downloads'
	}, {
		ref: 'downloadsGrid',
		selector: 'downloads > grid'
	}],

	init: function() {
		this.control({
			'downloads': {
				itemclick: this.loadRecords,
				deletedownload: this.onDeleteDownload,
				refresh: this.onRefresh
			},
			'downloadwindow': {
				show: this.onRefresh
			}
		});

		this.getDownloadsStore().on({
			load: this.select,
			scope: this
		});
	},

	/**
	 * loads the records from the download links
	 */
	loadRecords: function(records) {
		var i = 0;
		var tdata = [];
		var next = '';
		var guid = '';
		var store = this.getDownloadsThumbnailsStore();
		var guids = records.data.input_guids;

		for(i = 0; i < guids.length; i++) {
			guid = guids[i];
			next = {
				id: guid,
				imagesource: '../project/getThumbnail?recordid=' + guid
			};
			tdata.push(next);
		}
		store.loadData(tdata);
		
	},

	/**
	 * delete the selected link
	 */
	onDeleteDownload : function(record) {
		var me = this;
		Ext.Msg.confirm("Delete", "Are you sure you want to delete the selected export?", function(id)  {
			if (id === "yes") {
				var deleteUrl = '../project/deleteExport';
				if(record.data.isMovie){
            		deleteUrl = '../movie/deleteMovie';
				}
				Ext.Ajax.request({
					url : deleteUrl,
					method : 'get',
					params : {
						requestId : record.data.id
					},
					success : function (result, request) {
						me.onRefresh();
					},
					failure : function (result, request) {
						showErrorMessage(result.responseText, "Failed to delete the exported record");
					}
				});
			}
		});
	},

	/**
	 * reloads the download store
	 */
	onRefresh: function() {
		var store = this.getDownloadsStore();
		store.load();
	},

	/**
	 * selects the item in grid if any
	 * @param {Ext.data.Store} store the store that fired the load event
	 * @param {Ext.data.Model[]} records an Array of records
	 * @param {Boolean} successful True if the load was successfull
	 */
	select: function(store, records, successful) {
		if(!successful) {
			return;
		}
		
		//clear the downloads thumbnails
		var thumbsStore = this.getDownloadsThumbnailsStore();
		thumbsStore.removeAll();

		//select a record
		if(records.length > 0) {
			var grid = this.getDownloadsGrid();
			var selModel = grid.getSelectionModel();
			selModel.select(0);
		}
	}
});
