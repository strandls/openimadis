/**
 * Controller for workflow related views
 */
Ext.define('Manage.controller.WorkflowController', {
    extend: 'Ext.app.Controller',

	controllers: ['Selections'],
    
	stores: [ 
		'TaskMonitors','SelectedWorkflowStore','AvailableWorkflowStore','FavouriteWorkflowStore','TagSearch'
	],
	
    refs :[{
        ref: 'workflows',
        selector: 'workflows'
    },{
        ref: 'selectedWorkflows',
        selector: 'selectedworkflows'
    },{
        ref: 'availableWorkflows',
        selector: 'availableworkflows'
    },{
		ref: 'thumbnails',
		selector: '#imageThumbnails'
	}, {
        ref : 'scheduledTime',
        selector : 'schedulePanel timefield'
    }, {
        ref : 'scheduledDate',
        selector : 'schedulePanel datefield'
    }, {
		ref: 'workflowArea',
		selector: '#workflowArea'
	},{
		ref: 'urlArea',
		selector: '#urlArea'
	}, {
		ref: 'summaryGrid',
		selector: '#summaryGrid'
	}, {
		ref: 'ownTaskMonitor',
		selector: 'ownTaskMonitor'
	},{ 
		ref: 'FavouriteApplicationChooser',
		selector: 'favouriteapplicationchooser #addfolder'
		
	},{
		ref:'availableWorkflowsGrid',
		selector:'#availableworkflowsgrid'
	},{
		ref:'selectedWorkflowsGrid',
		selector:'#selectedworkflowsgrid'
	},{
		ref:'availableLayout',
		selector:'#availablelayout'
	}],

    init: function() {
        this.control({
        	"addfavouritefolder":{
        		addfavouritefolder:this.onAddFavouriteFolder
        	},	"workflows" : {
        		expand: this.expandWorkflows,
                requestAppExecution:this.onRequestAppExecution
            }, "favouriteworkflows" : {
        		expand: this.expandWorkflows,
                requestAppExecution:this.onRequestAppExecution
            }, "availableworkflows" : {
        		expand: this.expandWorkflows,
                requestAppExecution:this.onRequestAppExecution,
                addToFavourite:this.onAddToFavourite,
                tagSearch: this.onSearch,
                resetSearch: this.onReset,
            }, "selectedworkflows" : {
        		expand: this.expandWorkflows,
                requestAppExecution:this.onRequestAppExecution,
                removeFromFavourite:this.onRemoveFromFavourite,
            	addFolder: this.onAddFolder,
            	removeFolder: this.onRemoveFolder
            }, "schedulePanel radiogroup" : {    
            	change: this.onScheduleOptionChange
            }, "selectedRecords" : {    
            	beforeadd : this.beforeAddSelectedRecords,
            	afterrender : this.afterrenderSelectedRecords
            }, "paramsDialog" : {    
            	submitAppExecution : this.onSubmitAppExecution
            }, "authcodeGen" : {    
                getAuthCode : this.onGetAuthCode
            }, "viewClients" : {
                addClient : this.onAddClient
            }, "viewPublishers" : {
                addPublisher : this.onAddPublisher
            }, "viewTokens" : {
                addToken : this.onGenerateAuthCode,
                editToken : this.onEditToken,
                removeToken : this.onRemoveToken
            }, "editToken" : {
                updateToken : this.onUpdateToken
            }, "webApplications" : {
            	requestInvokeLink : this.onRequestInvokeLink
            }, "webClientDialog" : {
            	invokeLink: this.onInvokeLink
            }
        });
    },
    
    onSearch:function(tag){
    	console.log("search "+tag);
    	var store=this.getTagSearchStore();
    	store.load({params: {tag: tag,
    						projectName:this.getController('Selections').getActiveProject()}});
    	this.getAvailableLayout().getLayout().setActiveItem(1);
    	
    },
    
    onReset:function(){
    	this.getAvailableLayout().getLayout().setActiveItem(0);
    },
    
    onAddFolder:function(){
		var panel=this.getFavouriteApplicationChooser();
		panel.add(Ext.create('Manage.view.AddFavouriteFolder', {
			title: 'Add Favourite folder'
		}));
    },
    
    onAddFavouriteFolder:function(folderName){
    	var projectName = this.getController('Selections').getActiveProject();
    	//var selections=this.getSelectedWorkflows().getSelectionModel().getSelection()
    	var parentID=0;

    	var me=this;
    	Ext.Ajax.request({
            method : 'POST',
            url : '../compute/addFavouriteFolder',
            params : {
    			'projectName':projectName,
    			'parentID':parentID,
    			'folderName':folderName
    		},
            success : function (result, response){
        		
        		var selstore=me.getSelectedWorkflowStoreStore();
        		selstore.load({params: {projectName: projectName}});
        		
        		var favstore=me.getFavouriteWorkflowStoreStore();
        		favstore.load({params: {projectName: projectName}});   
        		
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to add favourites folder");
            }
        });
    },
    
    onRemoveFolder:function(){
    	var projectName = this.getController('Selections').getActiveProject();
    	var selections=this.getSelectedWorkflowsGrid().getSelectionModel().getSelection()
    	var folderID=selections[0].data.id;

    	var me=this;
    	Ext.Ajax.request({
            method : 'POST',
            url : '../compute/removeFavouriteFolder',
            params : {
    			'projectName':projectName,
    			'folderID':folderID
    		},
            success : function (result, response){
            	
        		var availstore=me.getAvailableWorkflowStoreStore();
        		availstore.load({params: {projectName: projectName}});
        		
        		var selstore=me.getSelectedWorkflowStoreStore();
        		selstore.load({params: {projectName: projectName}});   
        		
        		var favstore=me.getFavouriteWorkflowStoreStore();
        		favstore.load({params: {projectName: projectName}}); 
        		
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to add application "+record.data.appName+" to favourites");
            }
        });
    },
    
    onAddToFavourite:function(record){

    	var projectName = this.getController('Selections').getActiveProject();
    	var clientId = record.data.id;
    	
    	var selections=this.getSelectedWorkflowsGrid().getSelectionModel().getSelection();
    	var availables=this.getAvailableWorkflowsGrid().getSelectionModel().getSelection();
    	
    	if(this.getSelectedWorkflowsGrid().store.tree.root.childNodes.length==0){
    		Ext.Msg.alert("","Create a folder in Favourite Applications");
    		return;
    	}
    		
    	if(selections.length==0){
    		Ext.Msg.alert("","Choose a folder from Favourite Applications");
    		return;
    	}
    	
    	var me=this;
    	Ext.Ajax.request({
            method : 'POST',
            url : '../compute/addProjectClient',
            params : {
    			'clientID':clientId,
    			'projectName':projectName,
    			'folderID':selections[0].data.id
    		},
            success : function (result, response){
            	
        		var availstore=me.getAvailableWorkflowStoreStore();
        		availstore.load({params: {projectName: projectName}});
        		
        		var selstore=me.getSelectedWorkflowStoreStore();
        		selstore.load({params: {projectName: projectName}}); 
        		
        		var favstore=me.getFavouriteWorkflowStoreStore();
        		favstore.load({params: {projectName: projectName}}); 
        		
        		var tagstore=me.getTagSearchStore();
            	tagstore.reload();
        		
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to add application "+record.data.appName+" to favourites");
            }
        });
    	
    },
    
    onRemoveFromFavourite:function(record){
    	var projectName = this.getController('Selections').getActiveProject();
    	var clientId = record.data.id;
    	
    	var me=this;
    	Ext.Ajax.request({
            method : 'POST',
            url : '../compute/removeProjectClient',
            params : {
    			'clientID':clientId,
    			'projectName':projectName
    		},
            success : function (result, response){
            	
        		var availstore=me.getAvailableWorkflowStoreStore();
        		availstore.load({params: {projectName: projectName}});
            	
        		var selstore=me.getSelectedWorkflowStoreStore();
        		selstore.load({params: {projectName: projectName}});
        		
        		var favstore=me.getFavouriteWorkflowStoreStore();
        		favstore.load({params: {projectName: projectName}}); 
        		
        		var tagstore=me.getTagSearchStore();
            	tagstore.reload();
        		
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to remove application"+record.data.appName+"from favourites");
            }
        });
    },

    expandWorkflows:function(){
    	Ext.StoreManager.get('Manage.store.WorkflowStore').removeAll();
    	Ext.StoreManager.get('Manage.store.WorkflowStore').load();
    },
    
    beforeAddSelectedRecords: function(parent,grid){
    	return;
    	
    	var selections = this.getThumbnails().getSelectionModel().getSelection();

    	console.log('beforeAddSelectedRecords');
		var records = [];
		for(var i=0;i<selections.length;i++) 
		{
			records.push(selections[i].data.id);
		}
		
		var projectName = this.getController('Selections').getActiveProject();
		
		// get relavent data for these records
		Ext.Ajax.request({
            method : 'POST',
            url : '../project/getRecords',
            params : {
    			'recordids':Ext.encode(records),
    			'projectName':projectName
    		},
            success : function (result, response){
               var recordData = Ext.decode(result.responseText);
               console.log(recordData);
               var data = new Array();
               for ( var i = 0; i < recordData.count; i++)
               {
            	   	var rec = recordData.data[i];
					entry = {};
					console.log(rec);
					entry['Record ID'] = rec['Record ID'];
					entry['Source Folder'] = rec['Source Folder'];
					entry['Source File'] = rec['Source File'];
					entry['Frame Count'] = rec['Frame Count'];
					entry['Slice Count'] = rec['Slice Count'];
					data.push(entry);
					grid.store.loadData(data);
               }
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to fetch parameter list for "+appName+" "+appVersion);
            }
        });
    },
    
    afterrenderSelectedRecords: function(grid){
    	grid.getSelectionModel().selectAll();
    },
    
    onScheduleOptionChange: function(field,newValue,oldValue){
    	if(newValue.when === 'now'){
    		this.getScheduledTime().disable();
    		this.getScheduledDate().disable();
    	}
    	else if(newValue.when === 'later'){
    		this.getScheduledTime().enable();
    		this.getScheduledDate().enable();
    	}
    },
    
    onRequestAppExecution : function(appName,appVersion,description) {
    	console.log("onRequestAppExecution"+appName+" "+appVersion+" "+description);
    	var me = this;
    	Ext.Ajax.request({
            method : 'POST',
            url : '../compute/getApplicationParameters',
            params : {
    			'AppName':appName,
    			'AppVersion':appVersion
    		},
            success : function (result, response){
                var itemConfigs = Ext.decode(result.responseText);
                var dlg = Ext.create ('widget.paramsDialog', {
                			flex: 1,
	                        width : 400,
	                        appName:appName,
	                        appVersion:appVersion,
	                        itemConfigs : itemConfigs,
	                        description:description
				});      
				me.getWorkflowArea().remove(1);
				me.getWorkflowArea().add(dlg);
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to fetch parameter list for "+appName+" "+appVersion);
            }
        });
    },
    
    onRequestInvokeLink : function(appId, appName, appDescription, appUrl) {
    	console.log("onRequestInvokeLink"+appName);
    	
    	var guids = Ext.encode(this.getRecordsSelected());
    	var dlg = Ext.create ('widget.webClientDialog', {
				flex: 1,
	            width : 400,
	            id:appId,
	            appName:appName,
	            appUrl : appUrl,
	            description:appDescription,
	            guids: guids
		});
    	
    	this.getUrlArea().remove(1);
    	this.getUrlArea().add(dlg);
    },
    onInvokeLink: function(clientid){
    	var success = false;
    	var data;
    	var guids = Ext.encode(this.getRecordsSelected());
    	Ext.Ajax.request({
            method : 'POST',
            url : '../compute/launchWebApplication',
            async: false,
            params : {
    			'clientid':clientid,
    			'guids':guids
    		},
    		success : function (result, response){
            	data = Ext.decode(result.responseText);
            	console.log(data.url);
            	success = true;
            	//window.open(data.url);
            	/*var myBtn = Ext.ComponentQuery.query('#invokeURLHiddenButton')[0];
            	//myBtn.href = data.url;
            	myBtn.setText(data.url);
            	console.log(myBtn.getText());
            	myBtn.fireEvent('click', myBtn);
            	//myBtn.handler.call(myBtn.scope);
            	//myBtn.handler.call(Ext.ComponentQuery.query('#webClientDialog')[0].scope);
            	//window.open(data.url);*/
            	//Ext.Msg.alert("Invoke", "<a href="+data.url+" target='_blank'>Click Here</a> to invoke url");
			},
			failure : function(result, request) {
				showErrorMessage(result.responseText, "Failed to invoke url");
			} 
   });
    	if(success == true){
    		window.open(data.url);
    	}
    },
    
    onSubmitAppExecution : function(view,params) {
    	var me=this;
    	if(params['scheduledDate']!=null && params['scheduledTime']!=null){
    		var date=Ext.Date.parse(params['scheduledDate'], 'm/d/Y').valueOf();
        	var time=Ext.Date.parse(params['scheduledTime'], 'h:i A');
        	var hours=time.getHours();
        	var minutes=time.getMinutes()+hours*60;
        	var dateTime=date+minutes*60*1000;
        	params['scheduledOn']=dateTime;
    	}
    	params['projectName'] = this.getController('Selections').getActiveProject();
    	params['guids'] = Ext.encode(this.getRecordsSelected());

	// hide the window
	this.getSelectionsController().onSelectionClick();
    	
    	Ext.Ajax.request({
            method : 'POST',
            url : '../compute/scheduleApplication',
            params : params,
            success : function (result, response){
    			Ext.Msg.alert('Success', "Application scheduled for execution.");
    			view.close();
			me.getTaskMonitorsStore().load();
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to schedule application.");
                view.close();
            }
        });
    },

	/**
	 * get the records selected in the SelectionWindow as an array of guids
	 * @return {Array} an array of selected Record ID's
	 */
	getRecordsSelected: function() {
		var selection = this.getSummaryGrid().getSelectionModel().getSelection();
		var arr = [];
		
		var i;
		for( i = 0; i < selection.length; i++) {
			arr.push(selection[i].get('Record ID'));
		}

		return arr;
	},

});