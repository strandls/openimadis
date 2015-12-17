/**
 * Project manipulation related actions
 */
Ext.require(['Admin.view.AddProject', 'Admin.view.EditProject']);

Ext.define ('Admin.controller.ProjectController', {
    extend : 'Ext.app.Controller',

    refs : [{
        ref : 'projectList',
        selector : 'projectList'
    }],

    init : function() {
        this.control({
            'projectList' : {
                onAddProject : this.onAddProject,
                searchProject: this.searchProject,
                onEditProject : this.onEditProject,
                onDownloadProjectList : this.onDownloadProjectList,
                onArchiveRestoreClick: this.onArchiveRestoreClick
            }, 'addProject' : {
                refreshList : this.onRefreshList
            }, 'editProject' : {
                refreshList : this.onRefreshList
            }
        });
    },
    
    onDownloadProjectList : function() {
    	var _this = this; 
		var downloadMessage = 'Download Project list';
		Ext.Msg.alert("Download",  downloadMessage , function ( ) {
			var url = "../admin/downloadProjectList";
			window.location = url;
			});
    	/*
    	
    	Ext.create('Ext.window.Window', {
            title : 'Download',
            layout : 'fit',
            width : 200,
            height : 70,
            items : [{
                xtype : 'panel',
                layout : {
                    type : 'hbox',
                    pack : 'center',
                    align : 'middle'
                },
                items : [{
                    layout : 'fit',
                    width : 150,
                    height : 20,
                    border : false,
                    html : '<a href="../admin/downloadProjectList" target="_blank">' + 'Download Project List' + '</a>'
                }]
            }],
            buttons : [{
                text : 'Close',
                handler : function() {
                    this.up('window').close();
                }
            }]
        }).show();*/
    },

    /**
     * Add new project ui
     */
    onAddProject : function() {
        Ext.create ('Ext.window.Window', {
            height : 220,
            title : 'Add New Project',
            width : 400,
            items : [{
                xtype : 'addProject'   
            }]
        }).show();
    },
    
    /**
    * on archive/restore request
    */
    onArchiveRestoreClick :  function(project, locationValue, urlValue) {
    	var _this = this;
    	console.log(urlValue);
    	Ext.Ajax.request({
            method : 'GET',
            url : urlValue,
            params : {
                projectName : project,
                location : locationValue
            },
            success : function (result, request) {
                Ext.Msg.alert("Success", "Request submitted successfully");
                _this.getProjectList().store.load();
                _this.getProjectList().getView().refresh();
            },
            failure : function (result, request) {
                showErrorMessage(result.responseText, "Failed to get values for the field: " + fieldName);
            }
        });
    },

    /**
     * Edit project ui
     */
    onEditProject : function(project) {
        Ext.create ('Ext.window.Window', {
            height : 250,
            title : 'Edit Project',
            width : 400,
            items : [{
                xtype : 'editProject',
                projectDetails : project.data
            }]
        }).show();
    },
    
    /**
     * Refresh the project listing
     */
    onRefreshList : function() {
        var projectList = this.getProjectList();
        projectList.store.load();
    },
    
    /**
     * Search projects
     */
    
    searchProject : function(){
    	var projectList = this.getProjectList();
    	Ext.Ajax.request({
            method : 'GET',
            url : '../admin/listProjects',
            params : {
    			q : projectList.down('textfield[name=searchQuery]').getValue()
            },
            success : function(result, request) {
                var users = Ext.decode(result.responseText);
                projectList.getStore().loadData(users);
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to load user list"); 
            }
        });
    }

});
