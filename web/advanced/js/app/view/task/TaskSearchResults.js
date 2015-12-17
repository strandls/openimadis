/**
 * Grid to show search results for tasks
 */
Ext.define('Manage.view.task.TaskSearchResults', {
    extend : 'Ext.grid.Panel',
    alias : 'widget.taskSearchResults',
    margin: 5,
    store : 'Manage.store.task.TaskStore',
    
    initComponent:function(){
		var config={
			    columns : [
//			               {
//			        HEADER : 'taskId', DATAINDEX : 'taskId'
//			    }, 
			    {
			        header : 'Scheduled time', dataIndex : 'scheduledTime', flex:2
			    },{
			        header : 'Application Name', dataIndex : 'appName', flex:1
			    },{
			        header : 'Application Version', dataIndex : 'appVersion', flex:1
			    },{
			        header : 'State', dataIndex : 'state', flex:1
			    },{
			        header : 'Owner', dataIndex : 'owner', flex:1
			    },{
			        header : 'Project', dataIndex : 'project', flex:1
			    },{
			        header : 'Priority', dataIndex : 'priority', flex:1
			    },
		        {
		            header: 'Inspect',
		            dataIndex : 'notes',
		            flex:1,
		            xtype : 'componentcolumn',
		            // Custom renderer to show a inspect button. Uses an extjs plugin 'componentcolumn'
		            renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
		                // Already loaded projects are a parameter in projectListing
		                var taskUnderInspect = view.panel.taskUnderInspect;
		                var enabled = true;
		                if (taskUnderInspect && (record.data.taskId in taskUnderInspect))
		                    enabled = false;
		                var button = null;
		                if (enabled) {
		                    button = { 
		                        xtype : 'button',
		                        text : ' Inspect ',
		                        icon : 'images/launch.png',
		                        handler : function() {
		                            // Fire addProject event when clicked
		                    		console.log(view.panel);
		                            view.panel.fireEvent("inspectTask", record.data);
		                            this.setText("Added");
		                            this.setDisabled(true);
		                            //view.up().up().up().close();
		                        }  
		                    }; 
		                } else {
		                    button = { 
		                        xtype : 'button',
		                        text : ' Added ',
		                        icon : 'images/launch.png',
		                        disabled : true
		                    };
		                }
		                return {
		                    xtype : 'panel',
		                    border : false,
		                    layout : 'fit',
		                    items : [button]
		                };
		            }
		        }],
			    
			    dockedItems: [{
			        xtype: 'pagingtoolbar',
			        store: 'Manage.store.task.TaskStore',
			        dock: 'bottom',
			        displayInfo: true
			    }]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	}
});
