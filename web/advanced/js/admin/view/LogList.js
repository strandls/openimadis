/**
 * Downloads panel used to current users downloads
 * 
 */
Ext.define('Admin.view.LogList', {
    extend: 'Ext.panel.Panel',
    xtype : 'logList',
    alias: 'widget.logList',
    layout : 'anchor',
    title : 'System Logs',
    
    dockedItems : [ {
		xtype : 'toolbar',
		items : [
				{
					icon : "js/extjs/resources/themes/images/default/grid/refresh.gif",
					tooltip : 'Refresh Log File List',
					handler : function() {
						this.up('logList').updateLogs();
					}
				} ]
	} ],
	items : [ {
		xtype : 'gridpanel',
		store : 'Admin.store.LogStore',
		hideHeaders : false,
		autoScroll : true,
		anchor : "100% 80%",
		columns : [ 
		{
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
					items : [
					         {
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
					         }
							]
					
				};
			}
		}
		],
		
	    dockedItems: [{
	        xtype: 'pagingtoolbar',
	        store: 'Admin.store.LogStore',
	        dock: 'bottom',
	        displayInfo: true
	    }],
		
		listeners : {
			'select' : function(selection, record, index, eopts) {
				this.up().fireEvent("itemclick", record);
			}
		}
	}],
	
	initComponent : function() {
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
