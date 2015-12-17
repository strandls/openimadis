// Dependencies
Ext.require(['Manage.view.workflow.AuthCodeGenerator', 'Manage.view.workflow.ViewTokens',
             'Manage.view.workflow.EditToken','Manage.view.params.ParamsDialog','Manage.view.params.SchedulePanel',
             'Manage.view.params.SelectedRecords']);

/**
 * Controller for workflow related views
 */
Ext.define('Manage.controller.WorkflowController', {
    extend: 'Ext.app.Controller',

    refs :[{
        ref: 'workflows',
        selector: 'workflows'
    }, {
        ref : 'tokenList',
        selector : 'tokenList'
    }, {
        ref : 'scheduledTime',
        selector : 'schedulePanel timefield'
    }, {
        ref : 'scheduledDate',
        selector : 'schedulePanel datefield'
    },{
        ref : 'selectedRecords',
        selector : 'selectedRecords'
    },{
        ref : 'ownTaskMonitor',
        selector : 'ownTaskMonitor'
    }],

    init: function() {
        this.control({
            "workflows" : {
        		expand: this.expandWorkflows,
                addClient : this.onAddClient,
                addPublisher : this.onAddPublisher,
                viewClients : this.onViewClients,
                viewPublishers : this.onViewPublishers,
                generateAuthCode : this.onGenerateAuthCode,
                requestAppExecution:this.onRequestAppExecution,
                viewTokens : this.onViewTokens
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
            }
        });
    },

    expandWorkflows:function(){
    	Ext.StoreManager.get('Manage.store.WorkflowStore').removeAll();
    	Ext.StoreManager.get('Manage.store.WorkflowStore').load();
    },
    
    /**
     * View clients for a user
     */
    onViewClients : function() {
        var viewWindow = Ext.create ('Ext.window.Window', {
            height : 400,
            title : 'Manage Clients',
            width : 500,
            items : [{
                xtype : 'viewClients'
            }],
            buttons : [{
                text : 'Close',
                handler : function() {
                    var win = this.up().up();
                    win.items.items[0].close();
                    this.up().up().close();
                }
            }]
        });
        viewWindow.show();
    },
    
    /**
     * View all registered publishers 
     */
    onViewPublishers : function() {
        var viewWindow = Ext.create ('Ext.window.Window', {
            height : 400,
            title : 'Manage Publishers',
            width : 500,
            items : [{
                xtype : 'viewPublishers'
            }],
            buttons : [{
                text : 'Close',
                handler : function() {
                    var win = this.up().up();
                    win.items.items[0].close();
                    this.up().up().close();
                }
            }]
        });
        viewWindow.show();
    },

    /**
     * Add a new client
     */
    onAddClient : function() {
        Ext.create ('Ext.window.Window', {
            height : 230,
            title : 'Add New Client',
            width : 400,
            items : [{
                xtype : 'addClient'
            }]
        }).show();
    },
    
    /**
     * Add new publisher
     */
    onAddPublisher : function() {
        Ext.create ('Ext.window.Window', {
            height : 230,
            title : 'Add New Publisher',
            width : 400,
            items : [{
                xtype : 'addPublisher'
            }]
        }).show();
    },
    
    /**
     * Show view to generate auth code
     */
    onGenerateAuthCode : function() {
        Ext.create ('Ext.window.Window', {
            height : 450,
            title : 'Generate Authorization Code',
            width : 400,
            items : [{
                xtype : 'authcodeGen'
            }]
        }).show();
    },
    
    beforeAddSelectedRecords: function(parent,grid){
    	var records=this.getController('Manage.controller.SummaryTableController').getSelectedRecordsData();
    	var rec;
    	var data=new Array();
    	for(var i=0;i<records.length;i++){
    		rec=records[i].data;
    		entry={};
    		entry['Record ID']=rec['Record ID'];
    		entry['Source Folder']=rec['Source Folder'];
    		entry['Source File']=rec['Source File'];
    		entry['Frame Count']=rec['Frame Count'];
    		entry['Slice Count']=rec['Slice Count'];
    		data.push(entry);
    	}
    	grid.store.loadData(data);
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
    	
    	Ext.Ajax.request({
            method : 'POST',
            url : '../compute/getApplicationParameters',
            params : {
    			'AppName':appName,
    			'AppVersion':appVersion
    		},
            success : function (result, response){
               var itemConfigs = Ext.decode(result.responseText);
                var dlg= Ext.create ('Ext.window.Window', {
                    title:appName + ' ' + appVersion,
                    items : [{
                        xtype : 'paramsDialog',
                        height : 400,
                        width : 400,
                        appName:appName,
                        appVersion:appVersion,
                        itemConfigs : itemConfigs,
                        description:description
                    }]
                }).show();
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to fetch parameter list for "+appName+" "+appVersion);
            }
        });
    	
    	
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
    	params['projectName'] = this.getController('Manage.controller.NavigatorState').activeProject;
    	
    	Ext.Ajax.request({
            method : 'POST',
            url : '../compute/scheduleApplication',
            params : params,
            success : function (result, response){
    			Ext.Msg.alert('Success', "Application scheduled for execution.");
    			view.close();
    			me.getOwnTaskMonitor().getStore().load();
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to schedule application.");
                view.close();
            }
        });
    },	

    /**
     * Based on the information entered by user, generate authcode
     */
    onGetAuthCode : function(view, values) {
        var _this = this;
        var params = new Object();
        params["clientID"] = values["clientID"];
        params["expiryTime"] = values["expiryDay"].getTime();
        params["numberOfTokens"] = values["numberOfTokens"];
        delete values["clientID"];
        delete values["expiryDay"];
        delete values["numberOfTokens"];
        var services = new Array();
        for (var key in values) {
            if (values[key])
                services.push(key);
        }
        params["services"] = Ext.encode(services);
        
        var tokens =  params["numberOfTokens"] ;
        	Ext.Ajax.request({
        		method : 'POST',
        		url : '../compute/generateAuthCode',
        		params : params,
        		success : function (result, response){
        			var resp = Ext.decode(result.responseText);
        			var authCode = resp["authCode"];
        			var msg = "Codes are : " ;
        			for (var i = 0 ; i < tokens ; i++){
        				msg += authCode[i] + "<br/>" ; 
        			}
        			Ext.Msg.alert("Authorization Code",msg + "<br/><br/>Please copy and use the Authorization Code immediately. Authorization Code will not be displayed again anywhere. You can always generate another code if required." );
        			view.up().close();
        			_this.refreshTokenStore();
        		},
        		failure : function(result, request) {
        			showErrorMessage(result.responseText, "Failed to generate auth code");
        		}
        	});
       
    },

    /**
     * Refresh the token store
     */
    refreshTokenStore : function() {
        var table = this.getTokenList();
        if (table && table !== null) {
            table.getStore().load({
                params : {
                    user : table.activeUser
                }
            });
        }
    },

    /**
     * View tokens
     */
    onViewTokens : function() {
        var viewWindow = Ext.create ('Ext.window.Window', {
            height : 400,
            title : 'Manage Auth Tokens',
            width : 500,
            items : [{
                xtype : 'viewTokens'
            }],
            layout : 'fit',
            buttons : [{
                text : 'Close',
                handler : function() {
                    var win = this.up().up();
                    win.items.items[0].close();
                    this.up().up().close();
                }
            }]
        });
        viewWindow.show();
    },
   
    /**
     * Remove the selected token with confirmation
     */
    onRemoveToken : function(table, tokenModel) {
        var _this = this;
        Ext.Msg.confirm("Delete", "Are you sure you want to delete the selected token", function(id)  {
            if (id === "yes")
                _this.doRemoveToken(table, tokenModel);
        });
    },

    /**
     * Actual remove token
     */
    doRemoveToken : function(table, tokenModel) {
        var tokenID = tokenModel.data["id"];
        Ext.Ajax.request({
            url : '../compute/removeToken',
            method : 'POST',
            params : {
                id : tokenID
            },
            success : function (result, response) {
                table.getStore().load({
                    params : {
                        user : table.activeUser
                    }
                });
            },
            failure : function (result, response) {
                showErrorMessage(result.responseText, "Failed to delete token");
            }
        });
    },
    
    /**
     * Edit chosen token
     */
    onEditToken : function(table, tokenModel) {
        Ext.create ('Ext.window.Window', {
            height : 300,
            title : 'Edit Token',
            width : 400,
            items : [{
                xtype : 'editToken',
                services : tokenModel.data.services,
                expiry : new Date(tokenModel.data.expiryTime),
                id : tokenModel.data.id
            }]
        }).show();

    },
    
    /**
     * Post the new values for a token to the server
     */
    onUpdateToken : function(view, values, id) {
        var table = this.getTokenList();
        var params = new Object();
        params["expiryTime"] = values["expiryDay"].getTime();
        delete values["expiryDay"];
        var services = new Array();
        for (var key in values) {
            if (values[key])
                services.push(key);
        }
        params["services"] = Ext.encode(services);
        params["id"] = id;
        Ext.Ajax.request({
            method : 'POST',
            url : '../compute/updateToken',
            params : params,
            success : function (result, response){
                Ext.Msg.alert("Success", "Token Updated");
                view.up().close();
                table.getStore().load({
                    params : {
                        user : table.activeUser
                    }
                });
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to update token");
            }
        });
    }
});
