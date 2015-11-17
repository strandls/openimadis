/**
 * Component shows task explicitly added for inspection
 */

Ext.require(['Ext.TaskManager']);

Ext.define('Manage.view.TaskInspector', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.taskInspector',

        layout: {
                type: 'hbox',
                align: 'stretch'
        },

        defaults: {
                flex: 1
        },
        
    	bbar: {
    		ui: 'footer',

    		items: [{ 
    			xtype: 'button', 
    			text: 'Apply Selection',
    			handler: function() {
    				this.up('taskInspector').fireEvent('applyselection');
    			}
    		}]
    	},
    	
	items: [{
		xtype: 'grid',
		store : 'TaskInspectors',

		border:false,
		autoScroll : true,
		anchor : '100% 100%',
		hideHeaders:true,

		viewConfig: {
			emptyText: 'No tasks',
			deferEmptyText: false
		},

		columns : [{
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
		}]
	}, {
		xtype: 'tabpanel',
		flex: 1,
		
		items: [{
			xtype: 'thumbnails',
			id:'taskInspectorThumbnails',
			//this store is dynamically populated based on task selected
			store: 'TaskThumbnails',

			layout: 'fit',
			title: 'Thumbnails'
		}, {
			xtype: 'panel',
			id: 'taskInspectorArea',
			
			layout: 'fit',
			title: 'Params'
		}]
	}]
});
