/**
 * Shows the list of user logged in the system
 */
Ext.define('Manage.view.admin.UsageLogs', {
	extend:'Ext.panel.Panel',
	alias : 'widget.usageLogs',

	requires: [
		'Manage.view.admin.UsageLogSearch', 'Manage.view.admin.UsageLogSearchResults'
	],
	
	initComponent:function(){
	
		var config={
			title: 'Access Logs',
			layout:'border',
			items: [{
				xtype: 'usageLogSearch',
				region:'north',
				height: 120
			}, {
				xtype: 'usageLogSearchResults',
				region: 'center'
			}]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	},
    
    validate: function(){
	var form=this.getForm();
	if(form.isValid() === false){
		return false;
	}
	return true;
    }
	
});
