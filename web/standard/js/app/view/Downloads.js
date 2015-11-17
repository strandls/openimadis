/**
 * Downloads panel used to current users downloads
 * 
 */
Ext.define('Manage.view.Downloads', {
	extend: 'Ext.panel.Panel',
	xtype : 'downloads',
	alias: 'widget.downloads',
	layout : 'fit',
	dockedItems : [{
		xtype: 'toolbar',
		items:[{
			iconCls: "refresh",
			tooltip : 'Refresh My Downloads List',
			handler : function() {
				this.up('downloads').fireEvent('refresh');
			}
		}]
	}],
	items : [{
		xtype : 'gridpanel',
		store : 'Downloads',
		autoScroll : true,

		viewConfig: {
			emptyText: 'Links Empty',
			deferEmptyText: false
		},

		columns : [{
			header :"Name",
			dataIndex : 'name',
			flex : 2
		}, {
			header:"Status",
			dataIndex : 'status',
			flex : 2
		}, {
			header:"Size",
			dataIndex : 'size',
			flex : 2
		}, {
			header:"Creation Date",
			dataIndex : 'creationDate',
			flex : 2
		},{
			header:"Expiry Date",
			dataIndex : 'expiryDate',
			flex : 2
		},{
			dataIndex : 'name',
			xtype : 'componentcolumn',
			flex : 1,
			renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
				var url;
				if(record.data.isMovie)
				{
					url = "../movie/downloadMovie?movieId=" + record.data.id;
				} 
				else
				{
					url = "../project/downloadArchive?requestId=" + record.data.id;
				}
				
				return {
					xtype : 'panel',
					border : false,
					items : [{
						xtype : 'button',
						width: 25,
						disabled : (record.data.status != "successful"),
						tooltip : 'Download',
						icon : 'images/icons/download.png',
						href: url 
					}]

				};
			}
		},{
			dataIndex : 'name',
			xtype : 'componentcolumn',
			flex : 2,
			renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
				var url;
				if(record.data.isMovie)
				{
					url = "../download/downloadMovie?movieId=" + record.data.id;
				} 
				else
				{
					url = "../download/downloadArchive?requestId=" + record.data.id;
				}
				
				return {
					xtype : 'panel',
					border : false,
					items : [{
						xtype : 'button',
						width: 80,
						tooltip : 'Show Link',
						text:'Show link',
						disabled : (record.data.status != "successful"),
						listeners : {
							'click' : function(selection, record, index, eopts) {
								Ext.Msg.alert('Link', '<html><a href='+url+'>'+window.location+url+'</a></html>');
							}
						}
					}]

				};
			}
		}, {
			dataIndex : 'name',
			xtype : 'componentcolumn',
			flex : 1,
			renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
				return {
					xtype : 'panel',
					border : false,
					items : [{
						xtype : 'button',
						tooltip : 'Delete',
						icon : 'images/icons/delete.png',
						width : 25,
						handler : function() {
							view.up('downloads').fireEvent('deletedownload', record);
						} 
					}]

				};
			}
		}  
		],

		listeners : {
			'select' : function(selection, record, index, eopts) {
				this.up().fireEvent("itemclick", record);
			}
		}
		
	}]
});
