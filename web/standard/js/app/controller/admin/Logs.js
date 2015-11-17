/**
 * Controller for logging related actions 
 */
Ext.define ('Manage.controller.admin.Logs', {
	extend : 'Ext.app.Controller',

	refs : [{
		ref : 'usageLogs',
		selector : 'usageLogs'
	},{
		ref : 'usageLogSearch',
		selector : 'usageLogSearch'
	},{
		ref : 'usageLogSearchResults',
		selector : 'usageLogSearchResults'
	}],

	init : function() {
		this.control({
			'usageLogSearch' : {
				onUsageLogSearch : this.onUsageLogSearch
			}
		});
	},


	onUsageLogSearch : function() {
		if(this.getUsageLogSearch().validate()==false){
			return;
		}
		var filters=this.getUsageLogSearch().getForm().getFieldValues();
		if(filters.app !== null){
			var appDetails=this.getUsageLogSearch().down('combobox[name=app]').store.findRecord('clientID',filters.app);
			delete filters['app'];
			filters['appName']=appDetails.get('name');
			filters['appVersion']=appDetails.get('version');
		}

		if(filters.fromDate !== null){
			filters['fromDate']=filters.fromDate.getTime();
		}
		if(filters.fromDate !== null){
			//to incluse toDate in the results timestamp should be just less than the next day 
			//add 24*60*60*1000=86400000 milisec to the toDate.getTime()
			filters['toDate']=filters.toDate.getTime()+86400000;
		}
		var store = this.getUsageLogSearchResults().getStore();
		store.getProxy().extraParams = filters;
		store.loadPage(1);

	}
});
