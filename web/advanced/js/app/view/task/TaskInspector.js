/**
 * Component shows task explicitly added for inspection
 */

Ext.require(['Ext.TaskManager']);

Ext.define('Manage.view.task.TaskInspector', {
    extend : 'Ext.grid.Panel',
    alias : 'widget.taskInspector',
    store : 'Manage.store.task.TaskInspectorStore',
    
    initComponent:function(){
		var me=this;
		var config={
		    border:false,
		    autoScroll : true,
            anchor : '100% 100%',
			hideHeaders:true,
		    columns : [
		    {
		        dataIndex : 'taskDetails',
		        xtype: 'componentcolumn',
		        sortable : false,
	            flex : 1,
		        renderer : function(value, metaData, record, rowIndex, colIndex, store, view){
					var gridCellPanel={
						xtype: 'taskMonitorCell',
						record: record,
						view : view
					};
					
			        return gridCellPanel;
		    	}
		    }],
		    

			listeners:{
			'select':function(selection, record,index,eopts){
					//TODO: If possible eliminate clicks for clear, terminate 
					this.fireEvent('showTaskRecords',record);
				}
				
			},
			
			task:{
			    run: function(){
					me.updateRecordStates();
			    },
			    interval: 15000 //15 second
			}
			
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		Ext.TaskManager.start(this.task);
		this.callParent();
	},

	
	
	updateRecordStates: function(){
		var store=Ext.data.StoreManager.lookup('Manage.store.task.TaskInspectorStore');
		var records=store.getRange();
		var changingRecordIds = [];
		for(var i=0; i < records.length; i++){
			var record = records[i];
			if(store.canRecordStateChange(record , record.get('taskId'))){
				changingRecordIds.push(record.get('taskId'));
			}
		}
		if(changingRecordIds.length > 0){
			Ext.Ajax.request({
	            method: 'GET',
	            url: '../compute/getUpdatedState',
	            params: {
	    			'taskIds':Ext.JSON.encode(changingRecordIds)
	    		},
	            success: function(result, response) {
	                if (result.responseText && result.responseText.length > 0) {
	                    var updatedStates = Ext.JSON.decode(result.responseText);
	                    var storeUpdatedFlag=false;
	                    for(var i=0; i < updatedStates.length; i++){
	                    	var recordToUpdate=store.findRecord('taskId',updatedStates[i].taskId);
	                    	
	                    	if(recordToUpdate !== null){
	                    		if(recordToUpdate.get('state') !== updatedStates[i].state){
	                    			recordToUpdate.set('state',updatedStates[i].state);
	                    			storeUpdatedFlag=true;
	                    		}
	                    		
	                    	}
	                    }
	                    if(storeUpdatedFlag === true){
	                    	store.fireEvent('datachanged');
	                    }
	                }
	            }
	        });
		}
	}
});
