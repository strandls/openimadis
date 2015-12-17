/**
 * Shows the list of user logged in to specific projects of the system
 */
Ext.define('Admin.view.logging.ProjectUsageLogs', {
	 	extend: 'Ext.panel.Panel',
	 	xtype : 'projectUsageLogs',
	 	alias : 'widget.projectUsageLogs',
	    layout : 'anchor',
	    title : 'Project Usage Logs',
	    dockedItems : [ {
			xtype : 'toolbar',
			items : [
					{
						icon : "js/extjs/resources/themes/images/default/grid/refresh.gif",
						tooltip : 'Refresh Log File List',
						handler : function() {
							this.up('projectUsageLogs').updateLogs();
						}
					} ]
		} ],
		items : [ {
			xtype : 'gridpanel',
		    store : 'Admin.store.logging.ProjectUsageLogs',
			hideHeaders : false,
			autoScroll : true,
			anchor : "100% 80%",
			columns : [ 
				{header : 'User', dataIndex : 'userLogin', flex : 1},
	            {header : 'Project', dataIndex : 'projectName', flex : 1},
	            {header : 'Access Time', dataIndex : 'accessTime', flex : 2},
	            {header : 'Access Type', dataIndex : 'accessType', flex : 1}
	        ],
		
	        dockedItems: [{
	        	xtype: 'pagingtoolbar',
	        	store : 'Admin.store.logging.ProjectUsageLogs',
	        	dock: 'bottom',
	        	displayInfo: true
	        }]
		
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
