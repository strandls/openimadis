

Ext.define('Manage.view.admin.CacheStatus', {
	extend:'Ext.panel.Panel',
	alias : 'widget.cacheStatus',
	xtype:'cacheStatus',
	title:'Cache Status',
	layout: 'fit',
	
	dockedItems : [{
		xtype: 'toolbar',
		items:[{
			iconCls: "refresh",
			tooltip : 'Refresh Cache Status',
			handler : function() {
				this.up().up().down('grid').fireEvent('refreshCacheTable');
			}
		},
		{
			xtype:'button',
			text: 'Clean',
			handler: function(){
				this.up().up().down('grid').fireEvent('cleanCache','cache');
			}
		}]
	}],
	
	items:[{
		xtype: 'grid',
		id: 'cachegrid',
		store: 'admin.Cache',
		region: 'center',
		forceFit: false,
		split: true,
		flex: 1,
		
		viewConfig: {
			enableTextSelection: true
		},

		columns :[
		          {
		        	header : 'Cache Type',
		        	dataIndex : 'type'
		          },
		          {
			        	header : 'Count',
			        	dataIndex : 'count'
			      },
			      {
        	        	xtype : 'actioncolumn',
        	            header : 'Clean',
        	            width : 50,
        	            align : 'center',
        	            dataIndex: 'type',
        	            items : [
        	                {
        	                    icon:'images/icons/clean.png',
        	                    tooltip : 'Clean',
        	                    handler : function (grid, rowIndex, colIndex, item, e, record) {
        	                    	this.up().up().fireEvent('cleanCache',record.data.type);
        	                    }
        	                }
        	            ]
        	        }
		]
		
		}]
	
});