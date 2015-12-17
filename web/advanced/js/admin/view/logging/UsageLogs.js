/**
 * Shows the list of user logged in the system
 */
Ext.require(['Admin.view.logging.UsageLogsSearch',
             'Admin.view.logging.UsageLogsSearchResults']);
Ext.define('Admin.view.logging.UsageLogs', {
	extend:'Ext.panel.Panel',
	alias : 'widget.usageLogs',
	
	initComponent:function(){
	
		var config={
			title: 'Access Logs',
			layout:'border',
			items : [
				{
					xtype : 'usageLogsSearch',
					region:'north',
					height:100
				},
				{
					xtype : 'usageLogsSearchResults',
					region: 'center'
				}
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	},
    
    validate: function(){
    	var form=this.getForm();
    	if(form.isValid()==false){
    		return false;
    	}
    	//console.log(form.getRecord());
    	return true;
    }
	
});