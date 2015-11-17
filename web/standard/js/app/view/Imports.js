/**
 * Downloads panel used to current users downloads
 * 
 */
Ext.define('Manage.view.Imports', {
	extend: 'Ext.tab.Panel',
	xtype : 'imports',
	alias: 'widget.imports',
	
	dockedItems : [{
		xtype: 'toolbar',
		items:[{
			iconCls: "refresh",
			tooltip : 'Refresh Import List',
			handler : function() {
				this.up('imports').fireEvent('refresh');
			}
		}]
	}],
	
	items : [{
		xtype : 'gridpanel',
		store : 'Imports',
		id:'ongoingImportsPanel',
		autoScroll : true,
		title:'Ongoing Imports',

		viewConfig: {
			emptyText: 'Imports Empty',
			deferEmptyText: false
		},

		columns : [{
			header :"User",
			dataIndex : 'user',
			flex : 1
		}, {
			header :"Source Folder",
			dataIndex : 'location',
			flex : 1
		},{
			header:"Import Request Time",
			dataIndex : 'requestTime',
			flex : 1
		}, {
			header:"Status",
			dataIndex : 'status',
			flex : 1
		}],
		
        bbar: {
    		xtype: 'pagingtoolbar',
    		store: 'Imports',
    		dock: 'bottom',
    		displayInfo: true
    	},
    	
		listeners:{
			beforerender:function(){
				this.up('imports').fireEvent('setParams');
			}
		}
		
	},{
		xtype : 'gridpanel',
		store : 'CompletedImports',
		id:'completedImportsPanel',
		autoScroll : true,
		title:'Completed Imports',

		viewConfig: {
			emptyText: 'Imports Empty',
			deferEmptyText: false
		},

		columns : [{
			header :"User",
			dataIndex : 'user',
			flex : 1
		},{
			header :"Source Folder",
			dataIndex : 'location',
			flex : 1
		}, {
			header:"Import Request Time",
			dataIndex : 'requestTime',
			flex : 1
		}, {
			header:"Status",
			dataIndex : 'status',
			flex : 1
		}],
		
        bbar: {
    		xtype: 'pagingtoolbar',
    		store: 'CompletedImports',
    		dock: 'bottom',
    		displayInfo: true
    	},
		
		listeners:{
			beforerender:function(){
				this.up('imports').fireEvent('setParams');
			}
		}
	}]
});
