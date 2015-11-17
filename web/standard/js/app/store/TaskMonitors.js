/**
 * Store for task monitor
 */
Ext.define('Manage.store.TaskMonitors', {
    extend: 'Ext.data.Store',
    model: 'Manage.model.Task',
    autoLoad: true,
    proxy: {
	    type: 'ajax',
	    url: '../compute/getMonitoredTasks',
	    reader: 'json'
	},
	sorters: [{ 
		property: 'scheduledTimestamp'
	}],
	canRecordStateChange:function(record,id){
		var state=record.get('state');
		if( state === "SCHEDULED" 
				|| state === "WAITING" 
				|| state === "PAUSED"
				|| state === "ALLOCATED"
				|| state === "EXECUTING"
				|| state === "TERMINATING"){
			return true;
		}
		else{
			return false;
		}
	}
});
