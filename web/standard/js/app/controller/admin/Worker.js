/**
 * Controller for worker related functionalities
 */
Ext.define('Manage.controller.admin.Worker', {
	extend: 'Ext.app.Controller',
	
	refs: [{
		ref: 'cacheGrid',
		selector: '#cachegrid'
	},{
		ref: 'workerGrid',
		selector: '#workergrid'
	}],

	init: function() {
		this.control({
			'#workergrid': {
				setWorkerTable: this.setWorkerTable,
				restartService: this.restartService
			},
			'#cachegrid':{
				refreshCacheTable: this.refreshCacheTable,
				cleanCache: this.cleanCache
			}
		});
	},
	
	setWorkerTable: function(){
		var workerGrid = this.getWorkerGrid();
		var columns = [];
		
		//push worker column
		columns.push({
			header:'Worker',
			dataIndex: 'workerId'
		});
		
		var fields = [];
		fields.push(Ext.create('Ext.data.Field', {
			name : 'workerId'
		}));
		
		
		var metaData;
		var _this = this;
		Ext.Ajax.request({
            method : 'GET',
            url : '../admin/getWorkerMetaData',
            success : function (result, response) {
            	metaData = Ext.decode(result.responseText);
            	
        		var i;
        		for (i = 0; i < metaData.length; i++)
        		{
        			var subcolumns = [];
        			
        			for(var j=0;j<metaData[i].serviceParameters.length;j++)
        			{
        				subcolumns.push({
        					header : metaData[i].serviceParameters[j],
        					dataIndex : metaData[i].serviceParameters[j],
        				});
        				
        				fields.push(Ext.create('Ext.data.Field', {
        					name : metaData[i].serviceParameters[j]
        				}));
        			}
        			
        			//to restart the service
        	        subcolumns.push({
        	        	xtype : 'actioncolumn',
        	            header : 'Restart',
        	            width : 50,
        	            align : 'center',
        	            autoEl: {
        	                type: metaData[i].serviceType
        	            },
        	            items : [
        	                {
        	                    icon:'images/icons/restart.png',
        	                    tooltip : 'Restart',
        	                    handler : function (grid, rowIndex, colIndex, item, e, record) {
        	                    	this.up().up().up().up().down('grid').fireEvent('restartService',record, this.autoEl.type);
        	                    }
        	                }
        	            ]
        	        });
        			
        			columns.push({
        				header:metaData[i].serviceName,
        				columns: subcolumns,
        			});
        			
        		workerGrid.reconfigure(undefined, columns);
        		_this.getWorkerGrid().getStore().model.setFields(fields);
        		
            }
        		
    		Ext.Ajax.request({
                method : 'GET',
                url : '../admin/getWorkerStatus',
                success : function (result, response) {
                	var status = Ext.decode(result.responseText);
                	_this.getWorkerGrid().getStore().loadData(status);
                }
    		});	
         }
	   });

	},
	
	restartService: function(record, serviceType){
		Ext.Ajax.request({
            method : 'GET',
            url : '../admin/restartService',
			params : {
				workerId: record.data.workerId,
				serviceName: serviceType
			},
            success : function (result, response) {
            }
		});
	},
	
	refreshCacheTable: function(){
		this.getCacheGrid().getStore().load();
	},
	
	cleanCache: function(cacheType){
		var _this=this;
		
		if(cacheType === 'cache'){
			Ext.Ajax.request({
	            method : 'GET',
	            url : '../admin/cleanFullCache',
	            success : function (result, response) {
	            	_this.refreshCacheTable();
	            }
			});
			
			return;
		}
		
		Ext.Ajax.request({
            method : 'GET',
            url : '../admin/cleanCache',
			params : {
				type:cacheType
			},
            success : function (result, response) {
            	_this.refreshCacheTable();
            }
		});
	}
	
	
});