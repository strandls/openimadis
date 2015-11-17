/*Task inspector store.*/
Ext.define('Manage.store.TaskInspectors', {
	extend: 'Ext.data.Store', 
    model: 'Manage.model.Task',
    data : [
    ],
    
    sorters: [{
    	         property: 'scheduledTimestamp',
    	         direction: 'DESC'
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
