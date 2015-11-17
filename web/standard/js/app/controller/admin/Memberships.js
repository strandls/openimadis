/**
 * Membership manipulation related actions
 */
Ext.define ('Manage.controller.admin.Memberships', {
	extend : 'Ext.app.Controller',

	refs : [{
		ref : 'membershipList',
		selector : 'membershipList'
	},{
		ref : 'userProjectList',
		selector : 'userProjectList'
	}, {
		ref : 'membership',
		selector : 'membership'
	}, {
		ref : 'addMember',
		selector : 'addMember'
	}, {
		ref: 'adminSpace',
		selector: '#adminSpace'
	}],

	init : function() {
		this.control({
			'membershipList' : {
				onAddMembers : this.onAddMembers, 
				onDownloadMembershipList : this.onDownloadMembershipList,
				onEditMember : this.onEditMember,
				onRemoveMember : this.onRemoveMember,
				searchMember : this.searchMember,
				selectionchange: this.onMemberSelectionChange
			},'userProjectList' : {
				onUserProjectList : this.onDownloadMembershipList,
				searchMember : this.searchMember
			}, 'addMember' : {
				refreshList : this.onRefreshList
			}, 'editMember' : {
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
	onMemberSelectionChange: function(model, selection) {
		var space = this.getAdminSpace();

		if(space.hidden) {
			return;
		}
	
		if(space.down('editMember')) {
			this.getMembershipList().fireEvent('onEditMember', selection[0]);
		}
	},

	/**
	* Add new members ui
	*/
	onAddMembers: function(projectName, userLogin) {
		var availableStore = Ext.StoreManager.get('admin.AvailableUsers');
		var me = this;
		availableStore.load({
			params : {
				projectName : projectName , 
				userLogin : userLogin
			},
			callback : function(records, operation, success) {
				if (!success) {
					showErrorMessage(null, "Failed");
				} else {
					var comp = Ext.create ('Manage.view.admin.AddMember', {
								projectName : projectName , 
								userLogin : userLogin
					});
					me.addToAdminSpace(comp, "Add Member");
				}
			} 
		});
	},


	/**
	 * download the membership list
	 * @param {String} projectName name of the project to download membership od
	 * @param {String} userLogin name of the user
	 */
	onDownloadMembershipList : function(projectName , userLogin ) {
		this.projectName = projectName ;
		this.userLogin = userLogin ;
		
		var downloadMessage = 'Download membership list?';
		Ext.Msg.alert("Download",  downloadMessage , 
			function () {
				var url = "../admin/downloadMembershipList?projectName="+ this.projectName + "&userLogin="+ this.userLogin;
				window.location = url;
			}, 
			this 
		);

	},

	/**
	* Edit membership 
	*/
	onEditMember : function(membership) {
		var comp = Ext.create ('Manage.view.admin.EditMembership', {
			membershipDetails : membership.data
		});
		this.addToAdminSpace(comp, "Edit Membership");
	},

	/**
	* Remove membership
	*/
	onRemoveMember : function(membership) {
		var view = this.getMembershipList();
		Ext.Msg.confirm("Remove", "Are you sure you want to remove " + membership.data.user + " from the project " + membership.data.projectName + " ?", 
			function(id) {
				if (id === "yes") {
					Ext.Ajax.request({
						method : 'POST',
						url : '../admin/removeMember',
						params : {
							name : membership.data.user,
							projectName : membership.data.projectName
						},
						success : function (result, response){
							Ext.Msg.alert("Removed", "Removed user " + membership.data.user + " from project " + membership.data.projectName);
							view.store.load({
								params : {
									projectName : membership.data.projectName        
								}
							});
						},
						failure : function(result, request) {
							showErrorMessage(result.responseText, "Failed to delete overlay");
						} 
					});
				}
			}
		);
	},

	/**
	* Refresh list once a successful api call is made
	*/
	onRefreshList : function(projectName) {
		var view = this.getMembershipList();
		view.store.load({
			params : {
				projectName : projectName
			}
		});
	},

	/**
	* Search memebers
	*/
	searchMember : function(){
		var membershipList = this.getMembershipList();
		var membershipPanel=this.getMembership();
		Ext.Ajax.request({
			method : 'GET',
			url : '../admin/getMembers',
			params : {
				q : membershipList.down('textfield[name=searchQuery]').getValue(),
				projectName: membershipPanel.down('combobox[name=project]').getValue()
			},
			success : function(result, request) {
				var users = Ext.decode(result.responseText);
				membershipList.getStore().loadData(users);

			},
			failure : function(result, request) {
				showErrorMessage(result.responseText, "Failed to load user list"); 
			}
		});
	}
});
