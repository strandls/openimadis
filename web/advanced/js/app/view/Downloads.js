/**
 * Downloads panel used to current users downloads
 * 
 */
Ext.define('Manage.view.Downloads', {
    extend: 'Ext.panel.Panel',
    xtype : 'downloads',
    alias: 'widget.downloads',
    layout : 'anchor',
    dockedItems : [ {
		xtype : 'toolbar',
		items : [
				{
					icon : "js/extjs/resources/themes/images/default/grid/refresh.gif",
					tooltip : 'Refresh My Downloads List',
					handler : function() {
						this.up('downloads').updateDownloads();
					}
				} ]
	} ],
	items : [ {
		xtype : 'gridpanel',
		store : 'Manage.store.DownloadStore',
		hideHeaders : true,
		autoScroll : true,
		anchor : "100% 80%",
		columns : [ 
		{
			text: "Name",
			dataIndex : 'name',
			flex : 2
		},
		{
			text: "Status",
			dataIndex : 'status',
			flex : 2
		},
		{
			text: "Size",
			dataIndex : 'size',
			flex : 2
		},
		{
			text : "Export",
			dataIndex : 'name',
			xtype : 'componentcolumn',
			flex : 1,
			renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
				return {
					xtype : 'panel',
					border : false,
					items : [
					         {
					         	xtype : 'button',
								tooltip : 'Download',
								disabled : (record.data.link == ""),
								icon : 'images/icons/download.png',
								handler : function() {
									var html;
									if(record.data.isMovie)
									{
										var downloadMessage = 'Download Movie';
										html = '<a href="../movie/downloadMovie?movieId=' + record.data.id + '" target="_blank">' + downloadMessage + '</a>';
									} 
									else
									{
										var downloadMessage = 'Download ' + record.data.name;
										html = '<a href="../project/downloadArchive?requestId=' + record.data.id + '" target="_blank">' + downloadMessage + '</a>';
									}
									console.log(record.data.isMovie);
									console.log(html)
									Ext.create('Ext.window.Window', {
							            title : 'Download',
							            layout : 'fit',
							            width : 200,
							            height : 70,
							            items : [{
							                xtype : 'panel',
							                layout : {
							                    type : 'hbox',
							                    pack : 'center',
							                    align : 'middle'
							                },
							                items : [{
							                    layout : 'fit',
							                    width : 80,
							                    height : 20,
							                    border : false,
							                    html : html
							                }]
							            }],
							            buttons : [{
							                text : 'Close',
							                handler : function() {
							                    this.up('window').close();
							                }
							            }]
							        }).show();
								}
					         }
							]
					
				};
			}
		},
		{
			text : "Remove",
			dataIndex : 'name',
			xtype : 'componentcolumn',
			flex : 1,
			renderer : function(value, metaData, record, rowIndex, colIndex, store, view) 
			{
				return {
					xtype : 'panel',
					border : false,
					items : [
					         {
					        	 xtype : 'button',
									tooltip : 'Delete',
									icon : 'images/icons/delete.png',
									width : 20,
									handler : function() 
									{
										view.up().up().deleteDownload(record);
									} 
					         }
					]
					
				};
			}
		}  
		],
		
		listeners : {
			'select' : function(selection, record, index, eopts) {
				this.up().fireEvent("itemclick", record);
			}
		}
	}],
	
	initComponent : function() {
		var me = this;
		var config = {
			task : {
				run : function() {
					me.updateDownloads();
				},
				interval : 15000
			}
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		Ext.TaskManager.start(this.task);
		this.callParent();
		this.updateDownloads();
	},

	getDownloads : function() {
		return this.items.items[0];
	},

	setRecordID : function(recordid) {
		this.recordid = recordid;
		console.log("set record id");
	},
	
	deleteDownload : function(record) {
		var _this = this;
		Ext.Msg.confirm("Delete", "Are you sure you want to delete the selected export?", function(id)  {
            if (id === "yes") {
            	var url = '../project/deleteExport';
            	if(record.data.isMovie)
            		url = '../movie/deleteMovie';
            	
            	Ext.Ajax.request({
                    url : url,
                    method : 'get',
                    params : {
        				requestId : record.data.id
                    },
                    success : function (result, request) {
                    	_this.updateDownloads();
                    	_this.down('gridpanel').getSelectionModel().select(0);
                    },
                    failure : function (result, request) {
                        showErrorMessage(action.result.responseText, "Failed to delete the exported record");
                    }
                });
            }
        });
	},

	updateDownloads : function() {
		if(!this.collapsed)
		{
			var store = this.getDownloads().store;
			store.load();
		}
	}
});
