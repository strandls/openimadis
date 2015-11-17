/**
 * Grid to show search results for tasks
 */
Ext.define('Manage.view.admin.UsageLogSearchResults', {
    extend : 'Ext.grid.Panel',
    alias : 'widget.usageLogSearchResults',
    margin: 5,

    store: 'admin.UsageLogs',
    
    initComponent:function(){
		var config={
				autoScroll:true,
			    columns : [
			    {
			        header : 'Login time', dataIndex : 'loginTime', flex:1
			    },{
			        header : 'User', dataIndex : 'user', flex:1
			    },{
			        header : 'Application Name', dataIndex : 'appName', flex:2
			    },{
			        header : 'Application Version', dataIndex : 'appVersion', flex:1
			    }],
			    
			    dockedItems: [{
			        xtype: 'pagingtoolbar',
			        store: this.store,
			        dock: 'bottom',
			        displayInfo: true
			    }]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
		this.getStore().loadPage(1);
	}
});
