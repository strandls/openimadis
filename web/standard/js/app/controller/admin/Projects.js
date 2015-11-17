/**
 * Project manipulation related actions
 */

Ext.define ('Manage.controller.admin.Projects', {
	extend : 'Ext.app.Controller',

	requires: [
		'Manage.view.admin.WizardForm','Manage.view.admin.WizardItemAddProject','Manage.view.admin.WizardItemAddTeamProjectAssociations','Manage.view.admin.WizardItemAddMember','Manage.view.admin.AddProject', 'Manage.view.admin.EditProject'
	],

	refs : [{
		ref : 'projectList',
		selector : 'projectList'
	}, {
		ref: 'adminSpace',
		selector: '#adminSpace'
	}, {
		ref: 'association',
		selector: 'association #projectlist'
	}],
	
	stores: [ 
	  		'Projects'
	],

	init : function() {
		this.control({
			'projectList' : {
				onAddProject : this.onAddProject,
				searchProject: this.searchProject,
				onEditProject : this.onEditProject,
				onDownloadProjectList : this.onDownloadProjectList,
				onArchiveRestoreClick: this.onArchiveRestoreClick, 
				selectionchange: this.onProjectSelectionChange
			}, 'WIaddMember' : {
				refreshList : this.onRefreshList
			}, 'addProject' : {
				refreshList : this.onRefreshList
			}, 'editProject' : {
				refreshList : this.onRefreshList
			}
		});
	},

	/**
	 * adds the given Component to adminSpace
	 * @param {Ext.Component} comp the component to add
	 * @param {String} title the title to set the component
	 */
	addToAdminSpace: function(comp, title) {
		var ctr = this.getController('admin.Utils');
		ctr.addToAdminSpace(comp, title);
	},

	/**
	 * set the adminSpace on selectionchange
	 * @param {Ext.selection.Model} model the selection model
	 * @param {Ex.data.Model[]} selection the selected records
	 */
	onProjectSelectionChange: function(model, selection) {
		var space = this.getAdminSpace();

		if(space.hidden) {
			return;
		}
	
		if(space.down('editProject')) {
			this.getProjectList().fireEvent('onEditProject', selection[0]);
		}
	},

	onDownloadProjectList : function() {
		var downloadMessage = 'Download Project list';
		Ext.Msg.alert("Download",  downloadMessage , function ( ) {
			var url = "../admin/downloadProjectList";
			window.location = url;
		});
	},

	/**
	* Add new project ui
	*/
	onAddProject : function() {
		//Ext.Msg.alert("ssjk");
		/*
		 * Change Code based on Access Permissions
		 */
		var comp = Ext.create('Manage.view.admin.WizardForm');
		this.addToAdminSpace(comp, "Add new project");
		this.getAdminSpace().setHeight(200);
		
	},

	/**
	* on archive/restore request
	*/
	onArchiveRestoreClick :  function(project, locationValue, urlValue) {
		var me = this;
		Ext.Ajax.request({
			method : 'GET',
			url : urlValue,
			params : {
				projectName : project,
				location : locationValue
			},
			success : function (result, request) {
				Ext.Msg.alert("Success", "Request submitted successfully");
				me.getProjectList().store.load();
				me.getProjectList().getView().refresh();
			},
			failure : function (result, request) {
				Ext.Msg.alert("Failure", result.statusText);
			}
		});
	},

	/**
	* Edit project ui
	*/
	onEditProject : function(project) {
		var comp = Ext.create('Manage.view.admin.EditProject', {
			projectDetails : project.data
		});
		this.addToAdminSpace(comp, "Edit Project");
		this.getAdminSpace().setHeight(200);
	},

	/**
	* Refresh the project listing
	*/
	onRefreshList : function() {
		var projectList = this.getProjectList();
		projectList.store.load();
		this.getAssociation().getStore().load();
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
