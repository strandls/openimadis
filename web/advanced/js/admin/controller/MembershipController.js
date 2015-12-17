/**
 * Membership manipulation related actions
 */
Ext.require(['Admin.view.AddMembers', 'Admin.view.EditMembership']);

Ext.define ('Admin.controller.MembershipController', {
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
        ref : 'addMembers',
        selector : 'addMembers'
    }],
    
    init : function() {
        this.control({
            'membershipList' : {
                onAddMembers : this.onAddMembers,
                onDownloadMembershipList : this.onDownloadMembershipList,
                onEditMember : this.onEditMember,
                onRemoveMember : this.onRemoveMember,
                searchMember : this.searchMember
            },'userProjectList' : {
                onUserProjectList : this.onDownloadMembershipList,
                searchMember : this.searchMember
            }, 'addMembers' : {
                manageMembers : this.onManageMembers,
                refreshList : this.onRefreshList
            }, 'editMember' : {
                refreshList : this.onRefreshList
            }
        });
    },

    /**
     * Add new members ui
     */
    onAddMembers: function(projectName, userLogin) {
        var selectedStore = Ext.StoreManager.get('Admin.store.SelectedStore');
        selectedStore.removeAll();

        var availableStore = Ext.StoreManager.get('Admin.store.AvailableStore');
        availableStore.load({
            params : {
                projectName : projectName , 
                userLogin : userLogin
            },
            callback : function(records, operation, success) {
                if (!success) {
                    showErrorMessage(null, "Failed");
                } else {
                    Ext.create ('Ext.window.Window', {
                        height : 400,
                        title : 'Add New Members',
                        layout : 'fit',
                        width : 500,
                        items : [{
                            xtype : 'addMembers',
                            projectName : projectName , 
                            userLogin : userLogin
                        }]
                    }).show();
                }
            } 
        });
    },


    onDownloadMembershipList : function(projectName , userLogin ) {
    	var _this = this; 
    	this.projectName = projectName ;
    	this.userLogin = userLogin ;
    	console.log(projectName);
    	console.log(userLogin);
		var downloadMessage = 'Download membership list?';
		
		Ext.Msg.alert("Download",  downloadMessage , function () {
			var url = "../admin/downloadMembershipList?projectName="+ this.projectName + "&userLogin="+ this.userLogin;
			window.location = url;
			}, this );
		
    
    /*	Ext.create('Ext.window.Window', {
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
                    html : '<a href="../admin/downloadMembershipList?projectName="+ projectName + "&userLogin="+ userLogin  target="_blank">' + 'Download Membership List' + '</a>'
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
     * Edit membership ui
     */
    onEditMember : function(membership) {
        Ext.create ('Ext.window.Window', {
            height : 150,
            title : 'Edit Membership',
            layout : 'fit',
            width : 350,
            items : [{
                xtype : 'editMember',
                membershipDetails : membership.data
           }]
        }).show();
    },

    /**
     * Remove membership
     */
    onRemoveMember : function(membership) {
        var view = this.getMembershipList();
        Ext.Msg.confirm("Remove", "Are you sure you want to remove " + membership.data.user + " from the project " + membership.data.projectName + " ?", function(id) {
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
        });
    },

    /**
     * Associate the names with the project projectName
     */
    onManageMembers : function(viewWindow, projectName , userLogin , names, role) {
    	var _this = this;
        var view = this.getMembershipList();
        Ext.Ajax.request({
            method : 'POST',
            url : '../admin/addMembers',
            params : {
                projectName : projectName,
                userLogin :userLogin ,
                names : Ext.encode(names),
                role : role
            },
            success : function(result, response) {
                view.store.load({
                    params : {
                        projectName : projectName , 
                        userLogin : userLogin
                        
                    }
                });
                if(projectName != null){
                	Ext.Msg.alert("Message", "Successfully added " + names.length + " members to project " + projectName );
                	_this.onRefreshList(projectName);
                } else if(userLogin != null){
                	Ext.Msg.alert("Message", "Successfully added " + names.length + " members to user " + userLogin);
                }
                viewWindow.close();
            }
        });
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
    },
    
    /**
     * Search memebers
     */
    searchUserProject : function(){
    	console.log("on search user project");
    }
});
