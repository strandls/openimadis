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
				attachmovie:this.onAttachMovie,
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
	
	
	onAttachMovie : function(record) {
		var me = this;
		console.log(record.data);
		if(!record.data.isMovie){
			Ext.Msg.alert('Error', 'Only movies can be attached.');
			return;  
		}
		Ext.Msg.confirm("Attach movie", "Are you sure you want to attach the selected movie?", function(id)  {
			if (id === "yes") {
				var simple = Ext.create('Ext.form.Panel', {
					url:'save-form.php',
					frame:false,

					flex: 1,         
					bodyPadding: 5,
					defaultType: 'textareafield',
					defaults: {
						anchor: "100% 100%"
					},

					items: [{
						fieldLabel: 'Notes',
						id: 'notes-field',
						name: 'first',
						flex: 1,
						grow : true,
						allowBlank:false
					}],

					buttons: [{
						text: 'Submit',
						handler:function () {
							this.up('window').close();
							var url = '../project/addMovieAttachments';
							var notes = simple.getForm().findField('first').getSubmitValue();
							Ext.Ajax.request({
								url : url,
								method : 'get',

								params : {
									requestId : record.data.id,
									notes : notes,
									recordid : record.data.input_guids[0],
									name : record.data.name
								},
								success : function (result, request) {
									Ext.Msg.alert('', 'Movie attached successfully.');
									console.log(result);
									simple.removeAll(true);
								},
								failure : function (result, request) {
									Ext.Msg.alert('Error', 'Failed to attach the movie. ' + result.responseText.split(',')[0].split(':')[1].replace(/['"]+/g, '') + '. Error status code: ' + result.status + ' ' + result.statusText);
									console.log(result);
									simple.removeAll(true);
								}
							});
						}
					},{
						text: 'Cancel',
						handler:function () {
							this.up('window').close();
							simple.removeAll(true);
						}
					}]
				});
				var win = new Ext.Window(
						{
							layout: 'fit',
							title: 'Add notes to attachement',
							width: 300,
							modal: true,
							closeAction: 'hide',
							items: new Ext.Panel(
									{
										items: simple
									})
						});
				win.show();

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