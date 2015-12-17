/**
 * Grid to show search results for tasks
 */
Ext.define('Admin.view.logging.UsageLogsSearchResults', {
    extend : 'Ext.grid.Panel',
    alias : 'widget.usageLogsSearchResults',
    margin: 5,
    store : 'Admin.store.logging.UsageLogs',
    
    initComponent:function(){
		var config={
				autoScroll:true,
			    columns : [
			    {
			        header : 'Login time', dataIndex : 'loginTime', flex:2
			    },{
			        header : 'User', dataIndex : 'user', flex:1
			    },{
			        header : 'Application Name', dataIndex : 'appName', flex:1
			    },{
			        header : 'Application Version', dataIndex : 'appVersion', flex:1
			    }],
			    
			    dockedItems: [{
			        xtype: 'pagingtoolbar',
			        store: 'Admin.store.logging.UsageLogs',
			        dock: 'bottom',
			        displayInfo: true
			    }]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
		this.getStore().loadPage(1);
	}
});
