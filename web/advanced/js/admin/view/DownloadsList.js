/**
 * View for Downloads manipulation. This view will have the following,
 * List of downloads and some properties of clients in a table
 */
Ext.define('Admin.view.DownloadsList', {
    extend : 'Ext.grid.Panel',
    xtype : 'downloadsList',
    alias : 'widget.downloadsList',
    store : 'Admin.store.DownloadsStore',
    title : 'Downloads',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Name', dataIndex : 'name', flex : 1},
                {header : 'Format', dataIndex : 'format', flex : 1},
                {header : 'Size', dataIndex : 'size', flex : 1},
                {header : 'Validity', dataIndex : 'validity', flex : 1},
                {header : 'Status', dataIndex : 'status', flex : 1},
                {
            header : 'Link',
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
									var downloadMessage = 'Download ' + record.data.name;
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
							                    html : '<a href="../project/downloadArchive?requestId=' + record.data.id + '" target="_blank">' + downloadMessage + '</a>'
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
			header : 'Remove',
			text : "Remove",
			dataIndex : 'name',
			xtype : 'componentcolumn',
			flex : 1,
			renderer : function(value, metaData, record, rowIndex, colIndex, store, view) 
			{
				var _this = this.up().up();
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
											Ext.Msg.confirm("Delete", "Are you sure you want to delete the selected export?", function(id)  {
									            if (id === "yes") {
									            	Ext.Ajax.request({
									                    url : '../project/deleteExport',
									                    method : 'get',
									                    params : {
									        				requestId : record.data.id
									                    },
									                    success : function (result, request) {
									                    	var store = _this.store;
															store.load();
									                    },
									                    failure : function (result, request) {
									                        showErrorMessage(action.result.responseText, "Failed to delete the exported record");
									                    }
									                });
									            }
									        });
									} 
					         }
					]
					
				};
			}
		}
            ]
        });
        this.callParent();
    }
});
