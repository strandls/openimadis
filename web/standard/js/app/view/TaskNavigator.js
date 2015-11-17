/**
 * Task panel used to search for active, archived tasks
 * 
 */
Ext.require([
    'Manage.view.OwnTaskMonitor',
    'Manage.view.TaskInspector',
    'Manage.view.TaskSearchPanel',
    'Manage.view.TaskSearchResults'
]);

Ext.define('Manage.view.TaskNavigator', {
	extend: 'Ext.tab.Panel',
	alias: 'widget.taskNavigator',

	bodyPadding : 0,
	border: false,

	items:[{
		xtype:'ownTaskMonitor',
		title:'My tasks',
		dockedItems: [{
			xtype: 'toolbar',
			style: {
				border: 0,
				padding: 0
			},
			items: [{
				iconCls: "refresh",
				tooltip: 'Refresh List',
				handler : function() {
					this.up('ownTaskMonitor').fireEvent('refresh');
				}
			}]
		}]
	}, {
		xtype:'taskInspector',
		title:'Task Inspector',
		dockedItems: [{
			xtype: 'toolbar',
			style: {
				border: 0,
				padding: 0
			},
			items: [{
				iconCls: "refresh",
				tooltip: 'Refresh List',
				handler : function() {
					this.up('taskInspector').fireEvent('refresh');
				}
			}]
		}]
	}, {
		xtype: 'panel',
		title: 'Search and Add',

		layout: {
			type: 'vbox',
			align: 'stretch'
		},

		items: [{
			xtype: 'taskSearchPanel',
			flex: 1
		}, {
			xtype: 'taskSearchResults',
			flex: 1
		}]
	}]
});
