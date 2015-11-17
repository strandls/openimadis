/**
 * Component shows monitored task for logged in user
 */

Ext.require(['Ext.TaskManager','Manage.view.TaskMonitorCell']);

Ext.define('Manage.view.OwnTaskMonitor', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.ownTaskMonitor',

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
    				this.up('ownTaskMonitor').fireEvent('applyselection');
    			}
    		}]
    	},

	items:[{
		xtype: 'grid',
        store : 'TaskMonitors',
		border:false,
		autoScroll : true,
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
			}/*,
			doSort: function(state) {
                var ds = this.up('grid').getStore();
                var field = this.getSortParam();
                ds.sort({
                    property: field,
                    direction: state,
                    sorterFn: function(v1, v2){
                        v1 = v1.get(field);
                        v2 = v2.get(field);
                        return v1.length > v2.length ? 1 : (v1.length < v2.length ? -1 : 0);
                    }
                });
            }*/
		}],


		listeners:{
			'afterrender':function(){
				this.progressLoaded = false;
			},
			'loadProgress':function(){
				//HACK: need to refresh progressbar 
				//when they are made visible for the first time
				if(this.progressLoaded == false){
					var progressbars=Ext.ComponentQuery.query('progressbar');
					console.log(progressbars);
					for(var i=0; i< progressbars.length; i++){
						var pbar=progressbars[i];
						pbar.fireEvent('refresh');
					}
					this.progressLoaded = true; 
				}
			}
		}
	}, {
		xtype: 'tabpanel',
		flex: 1,
		
		items: [{
			xtype: 'thumbnails',
			id:'owntaskmonitorThumbnails',
			//this store is dynamically populated based on task selected
			store: 'TaskThumbnails',

			layout: 'fit',
			title: 'Thumbnails'
		}, {
			xtype: 'panel',
			id: 'ownTaskArea',
			
			layout: 'fit',
			title: 'Params'
		}]
	}]
});
