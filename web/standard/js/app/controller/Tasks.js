/**
 * Controller for tasks
 */
Ext.define('Manage.controller.Tasks', {
	extend: 'Ext.app.Controller',

	stores: [
		'TaskInspectors',
		'TaskMonitors',
		'Tasks',
		'TaskThumbnails'
	],
	
	controllers:[
	          'Thumbnails'
	],

	refs: [{
		ref: 'ownTaskMonitor',
		selector: 'ownTaskMonitor'
	}, {
		ref: 'taskInspector',
		selector: 'taskInspector'
	}, {
		ref: 'taskSearchPanel',
		selector: 'taskSearchPanel'
	}, {
		ref: 'ownTaskArea',
		selector: '#ownTaskArea'
        }, {
		ref: 'taskInspectorArea',
		selector: '#taskInspectorArea'
	}, {
		ref: 'ownGrid',
		selector: 'ownTaskMonitor > grid'
	}, {
		ref: 'taskInspectorGrid',
		selector: 'taskInspector > grid'
	}, {
		ref: 'owntaskmonitorThumbnails',
		selector: '#owntaskmonitorThumbnails'
	}, {
		ref: 'taskInspectorThumbnails',
		selector: '#taskInspectorThumbnails'
	}, {
		ref: 'taskWindow',
		selector: 'taskwindow'
	}],

	init: function() {
		this.control({
			'ownTaskMonitor > grid': {
				clearCell: this.onOwnTaskMonitorClear,
				terminate: this.onOwnTaskMonitorTerminate,
				select: this.onShowOwnTaskDetails,
				downloadLogs:this.downloadLogs
			}, 
			'taskInspector > grid': {
				clearCell: this.onTaskInspectorClear,
				terminate: this.onTaskInspectorTerminate,
				select: this.onShowTaskInspectorDetails 
			},
			'taskNavigator': {
				tabchange: this.onTabChange
			},
			'ownTaskMonitor': {
				refresh: this.onOwnTaskMonitorRefresh,
				applyselection: this.onOwnTaskMonitorApplySelection
			},
			'taskInspector': {
				refresh: this.onTaskInspectorRefresh,
				applyselection: this.onTaskInspectorApplySelection
			},
			'taskwindow': {
				show: this.onShow
			},
			'taskSearchPanel': {
				searchtask: this.onSearchTask
			},
			'taskSearchResults': {
				inspecttask: this.onInspectTask
			}
		});
	},
	
    downloadLogs: function(record){
    	
		var url = "../compute/getTaskLogs?taskId=" + record.data.taskId ;
		window.location = url;
    },

	onOwnTaskMonitorApplySelection: function(){
		var records=this.getOwntaskmonitorThumbnails().getSelectionModel().getSelection();
		
		if(records.length!=0){
			
			var recordids=[];
			for(var i=0;i<records.length;i++){
				recordids.push(records[i].raw.guid);
			}
						
			var me = this;
			// get the record details
			Ext.Ajax.request({
				method : 'GET',
				url : '../project/getProjectForRecord',
				params : {
					recordid : recordids[0]
				},
				success : function(result, response)
				{
					if (result.responseText && result.responseText.length > 0)
					{
						var projectName=Ext.decode(result.responseText)['projectName'];
						
						me.getThumbnailsController().onLoadTask(projectName,recordids);
						
						me.getTaskWindow().close();
					}
				},
				failure : function(result, request)
				{
					Ext.Msg.alert('Error', 'Could nor get records for tasks');
				}
			});
			
		}
	},
	
	onTaskInspectorApplySelection: function(){
		var records=this.getTaskInspectorThumbnails().getSelectionModel().getSelection();
		
		if(records.length!=0){
			
			var recordids=[];
			for(var i=0;i<records.length;i++){
				recordids.push(records[i].raw.guid);
			}
						
			var me = this;
			// get the record details
			Ext.Ajax.request({
				method : 'GET',
				url : '../project/getProjectForRecord',
				params : {
					recordid : recordids[0]
				},
				success : function(result, response)
				{
					if (result.responseText && result.responseText.length > 0)
					{
						var projectName=Ext.decode(result.responseText)['projectName'];
						
						me.getThumbnailsController().onLoadTask(projectName,recordids);
						
						me.getTaskWindow().close();
					}
				},
				failure : function(result, request)
				{
					Ext.Msg.alert('Error', 'Could nor get records for tasks');
				}
			});
			
		}
	},

	/**
	 * - Select an item when shown for the first time
	 * - Bring selected row into focus 
	 * - Refresh the stores
	 */
	onShow: function() {
		var selModel = this.getOwnGrid().getSelectionModel();
		var selection = selModel.getSelection();

		//select an item
		if(selection.length <= 0) {
			selModel.select(0, false, false);
		}

		//bring selected row into focus
		this.focusRow(this.getOwnGrid());
		this.focusRow(this.getTaskInspectorGrid());

		//refresh the stores
		this.taskRefresh(this.getTaskMonitorsStore());
		this.taskRefresh(this.getTaskInspectorsStore());
	},

	/**
	 * focus the selected row
	 * @param {Ext.grid.Panel} grid the grid in which the selected record has to be brought into focus
	 */
	focusRow: function(grid) {
		if(grid.getStore().getCount() <= 0 ) {
			return;
		}

		var selModel = grid.getSelectionModel();
		var view = grid.getView();

		view.focusRow(selModel.getLastSelected());
	},

	/**
	 * refresh the own task monitor
	 */
	onOwnTaskMonitorRefresh: function() {
		this.taskRefresh(this.getTaskMonitorsStore());
	},

	/**
	 * refresh the task inspector
	 */
	onTaskInspectorRefresh: function() {
		this.taskRefresh(this.getTaskInspectorsStore());
	},

	/**
	 * update the state of records in the given store
	 * @param {Ext.data.Store} store the store to update
	 */
	taskRefresh: function(store){
		var records = store.getRange();

		var changingRecordIds = [];
		for(var i=0; i < records.length; i++){
			var record = records[i];
			if(store.canRecordStateChange(record , record.get('taskId'))){
				changingRecordIds.push(record.get('taskId'));
			}
		}
		if(changingRecordIds.length <= 0){
			return;
		}
		Ext.Ajax.request({
			method: 'GET',
			url: '../compute/getUpdatedState',
			params: {
				'taskIds':Ext.JSON.encode(changingRecordIds)
			},
			success: function(result, response) {
				if (result.responseText && result.responseText.length > 0) {
					var updatedStates = Ext.JSON.decode(result.responseText);
					var storeUpdatedFlag = false;
					for(var i = 0; i < updatedStates.length; i++){
						var recordToUpdate = store.findRecord('taskId',updatedStates[i].taskId);

						if(recordToUpdate !== null){
							if(recordToUpdate.get('state') !== updatedStates[i].state){
								recordToUpdate.set('state',updatedStates[i].state);								
								storeUpdatedFlag = true;
							}
							console.log(updatedStates[i].progress);
							recordToUpdate.set('progress',updatedStates[i].progress);
						}
					}
				}
			}
		});
	},

	/**
	 * clear the ownTaskMonitor task
	 * @param task the task to clear
	 */
	onOwnTaskMonitorClear: function(task) {
		this.clearCell(task, this.getTaskMonitorsStore(), this.getOwnGrid().getSelectionModel());
	},

	/**
	 * clear the taskInspector task
	 * @param task the task to clear
	 */
	onTaskInspectorClear: function(task) {
		this.clearCell(task, this.getTaskInspectorsStore(), this.getTaskInspectorGrid().getSelectionModel());
	},

	/**
	 * select another cell on cell clear
	 * @param {Ext.selection.RowModel} selModel the selection model of the grid
	 */
	selectAdjacentRow: function(selModel) {
		var isSelected = selModel.selectNext(false, false);
		if(isSelected === false) {
			//no next row, select previous
			isSelected = selModel.selectPrevious(false, false);
		}
		if(isSelected === false) {
			//no rows, empty thumbnails
			this.setThumbnails([]);
		}
	},

	/**
	 * clear the cell, and on success select adjacent row
	 * @param task the task to clear
	 * @param {Ext.data.Store} store the store from which to clear the task
	 * @param {Ext.selection.RowModel} selModel the selection model of the grid
	 */
	clearCell: function(task, store, selModel) {
		var taskId = task.get('taskId');
		var me = this;

		Ext.Ajax.request({
			method: 'POST',
			url: '../compute/clearTaskMonitor',
			params: {
				'taskId': taskId
			},
			success: function(result, response) {
				me.selectAdjacentRow(selModel);
				store.remove(task);
			}
		});
	},
	
	/**
	 * terminate the ownMonitor task
	 * @param task the task to terminate
	 */
	onOwnTaskMonitorTerminate: function(task) {
		this.terminate(task.get('taskId'), this.getTaskMonitorsStore());
	},

	/**
	 * terminate the taskInspector task
	 * @param task the task to terminate
	 */
	onTaskInspectorTerminate: function(task) {
		this.terminate(task.get('taskId'), this.getTaskInspectorsStore());
	},


	/**
	 * terminate a task
	 * @param {Number} taskId the task to terminate
	 * @param {Ext.data.Store} store the store from which to terminate the task
	*/
	terminate: function(taskId, store) {
		var me = this;
		Ext.Ajax.request({
			method: 'POST',
			url: '../compute/terminateTask',
			params: {
				'taskId': taskId
			},
			success: function(result, response) {
				var task = store.findRecord('taskId', taskId);
				task.set('state', 'TERMINATING');
			}
		});
	},
	
	/**
	 * on tab change, 
	 *  - reload the TaskThumbnails store 
	 *     NOTE: have to do this, since the TaskThumbnails store is shared
	 *  - if no records selected, select the first one
	 *  - bring selected row into focus
	 * @param {Ext.tab.Panel} tabPanel The TabPanel
	 * @param {Ext.Component} newCard The newly activated item
	 */
	onTabChange: function(tabPanel, newCard) {
		if(newCard.xtype !== 'taskInspector' && newCard.xtype !== 'ownTaskMonitor') {
			return;
		}

		var grid = newCard.down('grid');
		var selModel = grid.getSelectionModel();

		if(grid.getStore().getCount() <= 0) {
			//set thumbnails to empty
			this.setThumbnails([]);
		} else {
			//if no records selected, select first one
			if(selModel.getSelection().length <= 0) {
				selModel.select(0);
			}
			
			//load the thumbnails for the task
			var task = selModel.getSelection()[0];
			this.showRecords(task.get('taskId'));
			
			//focus the selected row
			this.focusRow(grid);
		}
	},

	/**
	 * show the selected task details
	 * @param {Ext.selection.RowModel} rowModel the selection model
	 * @param {Manage.model.Task} task the task selected
	 * @param {Number} index the row index selected
	 */
	onShowOwnTaskDetails: function(rowModel, task, index) {
		var panel = this.getOwnTaskArea();
		var taskId = task.get('taskId');

		this.showParams(taskId, panel);

		this.showRecords(taskId);
	
	},

	/**
	 * show the selected task details
	 * @param {Ext.selection.RowModel} rowModel the selection model
	 * @param {Manage.model.Task} task the task selected
	 * @param {Number} index the row index selected
	 */
	onShowTaskInspectorDetails: function(rowModel, task, index) {
		var panel = this.getTaskInspectorArea();
		var taskId = task.get('taskId');

		this.showParams(taskId, panel);

		this.showRecords(taskId);
		
	},

	/**
	 * show the params of the task
	 * @param {Manage.model.Task} taskId the task selected 
	 * @param {Ext.panel.Panel} panel the panel to show the params in 
	 */
	showParams: function(taskId, panel) {
		var me = this;

		Ext.Ajax.request({
			method: 'GET',
			url: '../compute/getTaskExecutionDetails',
			params: {
				taskId: taskId
			},
			success: function(result, response) {
				if(result.responseText === false || result.responseText.length <= 0) {
					return;
				}
				var taskDetails = Ext.JSON.decode(result.responseText);
				var params = Ext.create('Manage.view.params.ParamsDialog', {
					appName: taskDetails.appName,
					appVersion: taskDetails.appVersion,
					itemConfigs: taskDetails.parameters,
					description: taskDetails.description
				});
				params.markAsReadOnly(taskDetails);
				params.remove(params.down('toolbar'));
            
				//show the params
				panel.removeAll();
				panel.add(params);
			}
		});
	},

	/**
	 * show the records for the selected task
	 * @param {Manage.model.Task} taskId the task selected 
	 */
	showRecords: function(taskId) {
		var me = this;
		Ext.Ajax.request({
			method: 'GET',
			url: '../project/getRecordsForTask',
			params: {
				'taskId': taskId
			},
			success: function(result, response) {
				if (result.responseText && result.responseText.length > 0) {
					var inputOutputRecords = Ext.JSON.decode(result.responseText);

					var inputRecords = inputOutputRecords.task_input_guids.data;
					var outputRecords = inputOutputRecords.task_output_guids.data;

					var allRecords = [];
					
					//the function to add the guids from the JSON array
					var add = function(element, index, array) {
						allRecords.push(element['Record ID']);
					}

					inputRecords.forEach(add);
					outputRecords.forEach(add);

					me.setThumbnails(allRecords);
				}
			}
		});
	},

	/**
	 * sets the thumbnail store with the given ids
	 * @params {Arrays} records the guids of the records to show
	 */
	setThumbnails: function(records) {
		var store = this.getTaskThumbnailsStore();
		var tdata = [];

		var i;
		for(i = 0; i < records.length; i++) {
			var guid = records[i];
			var next = {
				guid:  guid,
				imagesource : '../project/getThumbnail?recordid=' + guid
			};
			tdata.push(next);
		}
		store.loadData(tdata);
	},

	/**
	 * TODO the docs
	 */
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

		var store = this.getTasksStore();
		store.getProxy().extraParams = filters;
		store.loadPage(1);
	},

	/**
	 * add the task to the Task Inspector panel
	 */
	onInspectTask: function(task){
		var store = this.getTaskInspectorsStore();
		store.loadData([task], true);
	}
});
