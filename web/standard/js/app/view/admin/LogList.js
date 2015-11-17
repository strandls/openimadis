/**
 * Downloads panel used to current users downloads
 * 
 */
Ext.define('Manage.view.admin.LogList', {
	extend: 'Ext.panel.Panel',
	xtype : 'logList',
	alias: 'widget.logList',
	layout : 'fit',
	title : 'System Logs',


	dockedItems : [ {
		xtype : 'toolbar',
		items : [{
			iconCls: "refresh",
			tooltip : 'Refresh Log File List',
			handler : function() {
				this.up('logList').updateLogs();
			}
		}]
	}],

	initComponent: function() {
		Ext.apply(this, {
			items : [{
				xtype : 'gridpanel',
				store: 'admin.Logs',
				hideHeaders : false,
				autoScroll : true,
				columns : [{
					text: "Name",
					dataIndex : 'name',
					flex : 2
				},{
					text : "View",
					dataIndex : 'name',
					flex : 1,
					xtype : 'componentcolumn',
					renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
						return {
							xtype : 'panel',
							border : false,
							items : [{
								xtype : 'button',
								tooltip : 'Download',
								icon : 'images/icons/download.png',
								handler : function() {
									var downloadMessage = 'Download ' + record.data.name;

									Ext.Msg.alert("Download",  downloadMessage , function() {
										var url = "../admin/downloadLog?logfileName=" + record.data.name ;
										window.location = url;
									});
								}
							}]
						};
					}
				}],

				dockedItems: [{
					xtype: 'pagingtoolbar',
					store: 'admin.Logs',
					dock: 'bottom',
					displayInfo: true
				}],

				listeners : {
					'select' : function(selection, record, index, eopts) {
						this.up().fireEvent("itemclick", record);
					}
				}
			}]
		});

		this.callParent();
		this.updateLogs();
	},

	getLogs : function() {
		return this.items.items[0];
	},

	updateLogs : function() {
		var store = this.getLogs().store;
		store.load();
	}
});
