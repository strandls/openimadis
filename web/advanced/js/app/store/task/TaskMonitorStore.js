/**
 * Store for task monitor
 */
Ext.define('Manage.store.task.TaskMonitorStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Task',    
    model: 'Manage.model.Task',
    autoLoad: true,
    proxy: {
	    type: 'ajax',
	    url: '../compute/getMonitoredTasks',
	    reader: 'json'
	},
	
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
