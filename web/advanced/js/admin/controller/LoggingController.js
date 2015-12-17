/**
 * Controller for logging related actions 
 */

Ext.require(['Admin.view.logging.UsageLogs','Admin.view.logging.UsageLogsSearch',
             'Admin.view.logging.UsageLogsSearchResults' 
             ]);

Ext.define ('Admin.controller.LoggingController', {
    extend : 'Ext.app.Controller',

    refs : [{
        ref : 'usageLogs',
        selector : 'usageLogs'
    },{
        ref : 'usageLogsSearch',
        selector : 'usageLogsSearch'
    },{
        ref : 'usageLogsSearchResults',
        selector : 'usageLogsSearchResults'
   }],

    init : function() {
        this.control({
            'usageLogsSearch' : {
        		onUsageLogsSearch : this.onUsageLogsSearch
           }
        });
    },

    
    onUsageLogsSearch : function() {
    	if(this.getUsageLogsSearch().validate()==false){
    		return;
    	}
    	var filters=this.getUsageLogsSearch().getForm().getFieldValues();
    	if(filters.app !== null){
    		var appDetails=this.getUsageLogsSearch().down('combobox[name=app]').store.findRecord('clientID',filters.app);
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
    	var store=this.getUsageLogsSearchResults().getStore();
    	store.getProxy().extraParams = filters;
    	store.loadPage(1);

    }
});