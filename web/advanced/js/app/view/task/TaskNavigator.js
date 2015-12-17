/**
 * Task panel used to search for active, archieved tasks
 * 
 */
Ext.require([
    'Ext.tree.*',
    'Manage.view.task.OwnTaskMonitor',
    'Manage.view.task.TaskInspector'
]);

Ext.define('Manage.view.task.TaskNavigator', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.taskNavigator',
    anchor : '100% 100%',
    layout:'border',
    //store: 'Manage.store.TaskPanelStore',
    bodyPadding : 0,
    border: false,
    //rootVisible : false,
    listeners:{
		'expand':function(){
			console.log("on expand");
			this.down('ownTaskMonitor').fireEvent('loadProgress');
		}
	},
    items : [
        {
        	xtype:'tabpanel',
	        region:'center',
	        layout: 'border',
        	items:[
				{
					xtype:'ownTaskMonitor',
					title:'My tasks',
					dockedItems: [{
				        xtype: 'toolbar',
				        style: {
				            border: 0,
				            padding: 0
				        },
				        items: [
				            {
				                icon :"js/extjs/resources/themes/images/default/grid/refresh.gif",
				                tooltip: 'Refresh List',
				                handler : function() {
				                    this.up('ownTaskMonitor').updateRecordStates();
				                }
				            }
				        ]
				    }]
				},
				{
					xtype:'taskInspector',
					title:'Task Inspector',
					dockedItems: [{
				        xtype: 'toolbar',
				        style: {
				            border: 0,
				            padding: 0
				        },
				        items: [
				            {
				                icon : "images/icons/search2.png",
				                tooltip: 'Search & Add Task',
				                handler : function() {
				                    this.up('taskNavigator').fireEvent("showTaskSearch");
				                }
				            },
				            {
				                icon :"js/extjs/resources/themes/images/default/grid/refresh.gif",
				                tooltip: 'Refresh List',
				                handler : function() {
				                    this.up('taskInspector').updateRecordStates();
				                }
				            }
				        ]
				    }]
				}
        	]
        }
    ]	

});
