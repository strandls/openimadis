Ext.define('Manage.view.admin.WorkerStatus', {
	extend:'Ext.panel.Panel',
	alias : 'widget.workerStatus',
	xtype:'workerStatus',
	title: 'Worker Status',
	layout: 'fit',
	
	dockedItems : [{
		xtype: 'toolbar',
		items:[{
			iconCls: "refresh",
			tooltip : 'Refresh Worker Status',
			handler : function() {
				this.up().up().down('grid').fireEvent('setWorkerTable');
			}
		}]
	}],

		
	items:[{
		
		xtype: 'grid',
		id: 'workergrid',
		store: 'admin.Worker',
		region: 'center',
		//autoScroll: true,
		forceFit: false,
		split: true,
		flex: 1,
		
		viewConfig: {
			enableTextSelection: true
		},
		
		columns: [], //dynamically added based on project selection
	}],
	

	listeners: {
	    afterrender: function(){
	    	this.down('grid').fireEvent('setWorkerTable');
	    }
	}
});