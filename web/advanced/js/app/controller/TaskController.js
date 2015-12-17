/**
 * Controller for task related views
 */

Ext.require(['Manage.view.task.TaskSearchPanel','Manage.view.task.TaskSearchResults']);

Ext.define('Manage.controller.TaskController', {
	extend: 'Ext.app.Controller',
	
	refs :[
		{
	        ref: 'taskNavigator',
	        selector: 'taskNavigator'
	    },
	    {
	        ref: 'taskSearchPanel',
	        selector: 'taskSearchPanel'
	    }, 
	    { 
	        ref: 'summarytable',
	        selector:'summarytable'
	    }, 
	    {
	        ref: 'taskSearchResults',
	        selector: 'taskSearchResults'
	    },
	    {
	        ref: 'ownTaskMonitor',
	        selector: 'ownTaskMonitor'
	    },
	    {
	        ref: 'taskInspector',
	        selector: 'taskInspector'
	    }
	],
    
    init: function() {
    	this.control({
    		'taskSearchPanel': {
    			onSearchTask: this.onSearchTask
    		},
    		'taskSearchResults': {
    			inspectTask: this.onInspectTask
    		},
    		'taskNavigator': {
    			showTaskSearch:this.onshowTaskSearch
    		},
    		'ownTaskMonitor':{
    			showTaskRecords:this.onShowTaskRecords,
    			clearCell:this.onClearMonitor,
    			showParams:this.onShowParams,
    			terminate:this.onTerminate,
    			downloadLogs:this.downloadLogs
    		},
    		'taskInspector':{
    			showTaskRecords:this.onShowTaskRecords,
    			clearCell:this.onClearInspector,
    			showParams:this.onShowParams,
    			terminate:this.onTerminate
    		}
    	});
    },
    
    downloadLogs: function(record){
    	var taskId=record.data.taskId;
    	
			
			var url = "../compute/getTaskLogs?taskId=" + record.data.taskId ;
			window.location = url;
//    	Ext.Ajax.request({
//            method: 'GET',
//            url: '../compute/getTaskLogs',
//            params: {
//    			'taskId':taskId
//    		},
//            success: function(result, response) {
//            }
//        });
    },
    
    
    onshowTaskSearch: function (){
    	Ext.create ('Ext.window.Window', {
             height : 540,
             title : 'Search Task',
             width : 800,
             items : [
	            {
	                 xtype : 'taskSearchPanel'
	            },
	            {
	            	 xtype : 'taskSearchResults',
	            	 height:300,
	            	 taskUnderInspect:this.getTaskUnderInspect()
	            }
             ]
         }).show();
    	this.getTaskSearchResults().getStore().removeAll();
    },
    
    getTaskUnderInspect : function(){
    	var taskIds = {};
    	var store=this.getTaskInspector().getStore();
    	var records = store.getRange();
    	var row;
    	for(var i=0; i < records.length; i++){
    		row=records[i].data;
    		taskIds[row.taskId]=true;
    	}
    	return taskIds;
    },
    
    onSearchTask:function(){
    	if(this.getTaskSearchPanel().validate()==false){
    		return;
    	}
    	var filters=this.getTaskSearchPanel().getForm().getFieldValues();
    	if(filters.app !== null){
    		var appDetails=this.getTaskSearchPanel().down('combobox[name=app]').store.findRecord('clientID',filters.app);
        	delete filters['app'];
        	filters['appName']=appDetails.get('name');
        	filters['appVersion']=appDetails.get('version');
    	}
    	
    	if(filters.fromDate !== null){
    		filters['fromDate']=filters.fromDate.getTime();
    	}
    	if(filters.fromDate !== null){
    		//to incluse toDate in the results timestamp should be just less than the next day 
    		//add 24*60*60*1000=86400000 milisec to the toDate.getTime()
    		filters['toDate']=filters.toDate.getTime()+86400000;
    	}
    	var store=this.getTaskSearchResults().getStore();
    	store.getProxy().extraParams = filters;
    	store.loadPage(1);
    },
    
    /**
     * Find appName, appVersion from clientId
     */
    findAppDetails:function(clientID){
    	var WorkflowClientStore=Ext.getStore('Manage.store.task.WorkflowClientStore');
    	var index=WorkflowClientStore.find('clientID',clientID);
    	return WorkflowClientStore.getAt(index).data;
    },
    
    onShowTaskRecords:function(record){
    	var taskId=record.data.taskId;
    	var me = this;
    	var controller=this.getController('Manage.controller.SummaryTableController');
    	Ext.Ajax.request({
            method: 'GET',
            url: '../project/getRecordsForTask',
            params: {
    			'taskId':taskId
    		},
            success: function(result, response) {
                if (result.responseText && result.responseText.length > 0) {
                    var inputOutputRecords = Ext.JSON.decode(result.responseText);
                    
                    var inputRecords = inputOutputRecords.task_input_guids;
                    var outputRecords = inputOutputRecords.task_output_guids;
                    
                    var allRecords = new Object();
                    allRecords["count"] = inputRecords.count + outputRecords.count;
                    allRecords["data"] = inputRecords.data.concat(outputRecords.data);
                    allRecords["fields"] = inputRecords.fields;
                    var mode='taskInspection';
                    controller.loadDataToTable(allRecords,mode);
                    controller.setActiveProject(record.data.project);
                }
            }
        });
    },
    
    onInspectTask: function(task){
    	var store=this.getTaskInspector().getStore();
    	store.loadData([task],true);
    },
    
    onClearMonitor: function(record){
    	var taskId=record.data.taskId;
    	var me=this;
    	Ext.Ajax.request({
            method: 'POST',
            url: '../compute/clearTaskMonitor',
            params: {
    			'taskId':taskId
    		},
            success: function(result, response) {
    			me.getOwnTaskMonitor().getStore().remove(record);
            }
        });
    	
    },
    
    onTerminate : function(record){
    	var taskId=record.data.taskId;
    	var me=this;
    	Ext.Ajax.request({
            method: 'POST',
            url: '../compute/terminateTask',
            params: {
    			'taskId':taskId
    		},
            success: function(result, response) {    			
    			var ownTaskrecord = me.getOwnTaskMonitor().getStore().findRecord('taskId',taskId);
    			if(ownTaskrecord !== null){
    				ownTaskrecord.set('state','TERMINATING');
    			}
    			
    			var inspectTaskrecord = me.getTaskInspector().getStore().findRecord('taskId',taskId);
    			if(inspectTaskrecord !== null){
    				inspectTaskrecord.set('state','TERMINATING');
    			}
            }
        });
    	
    },
    
    onClearInspector:function(record){
    	this.getTaskInspector().getStore().remove(record);
    },
    
    onShowParams: function(record){
    	var taskId=record.data.taskId;
    	var me=this;
    	Ext.Ajax.request({
            method: 'GET',
            url: '../compute/getTaskExecutionDetails',
            params: {
    			'taskId':taskId
    		},
            success: function(result, response) {    			
    			if (result.responseText && result.responseText.length > 0) {
                    var taskDetails = Ext.JSON.decode(result.responseText);
                    console.log(taskDetails);
                    
                    var paramsDialog= Ext.create ('Manage.view.params.ParamsDialog', {
                    	region:'center',
                        appName:taskDetails.appName,
                        appVersion:taskDetails.appVersion,
                        itemConfigs : taskDetails.parameters,
                        description:taskDetails.description
                    });
                    
                    paramsDialog.markAsReadOnly(taskDetails);
                    
                    var dlg= Ext.create ('Ext.window.Window', {
                    	height : 400,
                        width : 400,
                        layout:'border',
                        title:taskDetails.appName + ' ' + taskDetails.appVersion,
                        items : [paramsDialog]
                    });
                    dlg.show();
                }
            }
        });
    	
    }
});
