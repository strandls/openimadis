/**
 * User manipulation related actions
 */
Ext.define ('Manage.controller.admin.Users', {
	extend : 'Ext.app.Controller',

	requires: [
		'Manage.view.admin.UserWizardForm','Manage.view.admin.WizardItemAddUser','Manage.view.admin.WizardItemAddUserToProject','Manage.view.admin.AddUser', 'Manage.view.admin.EditUser', 'Manage.view.admin.ChangePassword', 'Manage.view.admin.ChangePassword', 'Manage.view.admin.ImportLDAPUsers'
	],

	refs : [{
		ref : 'userList',
		selector : 'userList'
	}, {
		ref: 'adminSpace',
		selector: '#adminSpace'
	}, {
		ref: 'users',
		selector: 'userProjects #users'
	}],

	init : function() {
		this.control({
			'userList' : {
				onAddUser : this.onAddUser,
				searchUser: this.searchUser,
				onEditUser : this.onEditUser,
				onChangePassword : this.onChangePassword,
				onImportLDAPUsers : this.onImportLDAPUsers, //TODO
				onChangeAuthenticationType : this.onChangeAuthenticationType,
				onDownloadUserList : this.onDownloadUserList,
				selectionchange: this.onUserSelectionChange
			}, 'addUser' : {
				refreshList : this.onRefreshList
			}, 'editUser' : {
				refreshList : this.onRefreshList
			}, 'importLDAPUsers' : {
				onAddLDAPUsers : this.onAddLDAPUsers //TODO
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
	onUserSelectionChange: function(model, selection) {
		var space = this.getAdminSpace();

		if(space.hidden) {
			return;
		}
	
		if(space.down('editUser')) {
			this.getUserList().fireEvent('onEditUser', selection[0]);
		}
	},

	/**
	* Add new user 
	*/
	onAddUser : function() {
		// Change Code for showing Wizard based on role
		//var comp = Ext.create('Manage.view.admin.AddUser');
		var comp = Ext.create('Manage.view.admin.UserWizardForm');
		this.addToAdminSpace(comp, "Add new user");
	},

	/**
	* Edit user ui
	*/
	onEditUser : function(user) {
		var comp = Ext.create('Manage.view.admin.EditUser', {
			userDetails : user.data
		});
		this.addToAdminSpace(comp, "Edit User");
	},

	/**
	* Change password ui
	*/
	onChangePassword : function(user) {
		var comp = Ext.create('Manage.view.admin.ChangePassword', {
			userDetails : user.data
		});
		this.addToAdminSpace(comp, "Change Password");
	},

	/**
	* Change authentication type
	*/
	onChangeAuthenticationType : function(user, type) {
		var me = this; 
		Ext.Ajax.request({
			method : 'POST',
			url : '../admin/changeAuthenticationType',
			params : {
				login : user.data.login,
				type : type
			},
			success : function(result, response) {
				if( type == 'Internal') {
					Ext.Msg.alert("Message", "Successfully added " + user.data.name + " as internal user. Internal password is reset to "+user.data.login+"123");
				}
				me.onRefreshList();
			},
			failure: function(result, response) {
				Ext.Msg.alert("Failure", "Failed to change type of user " + user.data.name);
			}
		});
	},

	/**
	* Change password ui
	*/
	onDownloadUserList : function(user, type) 
	{
		var me = this; 
		var downloadMessage = 'Download User list ';
		Ext.Msg.alert("Download",  downloadMessage , function ( ) {
			var url = "../admin/downloadUserList";
			window.location = url;
		});
	},

	/**
	* Import LDAP Users UI
	*/
	onImportLDAPUsers : function()
	{
		console.log("importing ldap users");
		
		var selectedStore = Ext.StoreManager.get('admin.selectedLDAPUsersStore');
		console.log(selectedStore);
		selectedStore.removeAll();

		var availableStore = Ext.StoreManager.get('admin.availableLDAPUsersStore');
		console.log(availableStore);
		
		availableStore.load({
			callback : function(records, operation, success)
			{
				if (!success)
				{
					showErrorMessage(null, "Failed");
				}
				else
				{
					Ext.create('Ext.window.Window', {
						height : 400,
						title : 'Import LDAP Users',
						layout : 'fit',
						modal:true,
						draggable:false,
						width : 500,
						items : [ {
							xtype : 'importLDAPUsers'
						} ]
					}).show();					
				}
			}
		});
	},

	/**
	* Associate the names with the project projectName
	*/
	onAddLDAPUsers : function(viewWindow, names, emails, rank)
	{
		_this = this;
		var view = this.getUserList();
		Ext.Ajax.request({
			method : 'POST',
			url : '../admin/addLDAPUsers',
			params : {
				names : Ext.encode(names),
				emails : Ext.encode(emails),
				rank : rank
			},
			success : function(result, response)
			{
				Ext.Msg.alert("Message", "Successfully added " + names.length + " LDAP users");
				viewWindow.close();

				_this.onRefreshList();
			}
		});
	},

	/**
	* Refresh the user listing
	*/
	onRefreshList : function() {
		var userList = this.getUserList();
		userList.store.load();
		this.getUsers().getStore().load();
	},

	/**
	* Search user
	*/
	searchUser: function(){
		var userList = this.getUserList();
		Ext.Ajax.request({
			method: 'GET',
			url: '../admin/listUsers',
			params: {
				q: userList.down('textfield[name=searchQuery]').getValue()
			},
			success: function(result, request) {
				var users = Ext.decode(result.responseText);
				userList.getStore().loadData(users);
			},
			failure: function(result, request) {
				showErrorMessage(result.responseText, "Failed to load user list"); 
			}
		});
	}
});
