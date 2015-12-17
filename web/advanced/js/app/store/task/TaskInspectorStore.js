/*Task inspector store.*/
Ext.define('Manage.store.task.TaskInspectorStore', {
	extend: 'Ext.data.Store', 
    requires: 'Manage.model.Task',    
    model: 'Manage.model.Task',
    data : [
    ],
	
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