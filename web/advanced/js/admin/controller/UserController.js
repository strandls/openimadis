/**
 * User manipulation related actions
 */
Ext.require(['Admin.view.AddUser', 'Admin.view.EditUser',
             'Admin.view.ChangePassword', 'Admin.view.ImportLDAPUsers']);

Ext.define ('Admin.controller.UserController', {
    extend : 'Ext.app.Controller',

    refs : [{
        ref : 'userList',
        selector : 'userList'
    }],

    init : function() {
        this.control({
            'userList' : {
                onAddUser : this.onAddUser,
                searchUser: this.searchUser,
                onEditUser : this.onEditUser,
                onChangePassword : this.onChangePassword,
                onImportLDAPUsers : this.onImportLDAPUsers,
                onChangeAuthenticationType : this.onChangeAuthenticationType,
                onDownloadUserList : this.onDownloadUserList
            }, 'addUser' : {
                refreshList : this.onRefreshList
            }, 'editUser' : {
                refreshList : this.onRefreshList
            }, 'importLDAPUsers' : {
            	onAddLDAPUsers : this.onAddLDAPUsers
            }
        });
    },

    /**
     * Add new user ui
     */
    onAddUser : function() {
        Ext.create ('Ext.window.Window', {
            height : 250,
            title : 'Add New User',
            width : 400,
            items : [{
                xtype : 'addUser'   
            }]
        }).show();
    },

    /**
     * Edit user ui
     */
    onEditUser : function(user) {
        Ext.create ('Ext.window.Window', {
            height : 220,
            title : 'Edit User',
            width : 400,
            items : [{
                xtype : 'editUser',
                userDetails : user.data
            }]
        }).show();
    },

    /**
     * Change password ui
     */
    onChangePassword : function(user) {
        Ext.create ('Ext.window.Window', {
            height : 180,
            title : 'Change Password',
            width : 300,
            items : [{
                xtype : 'changePassword',
                userDetails : user.data
            }]
        }).show();
    },
    
    /**
     * Change password ui
     */
    onChangeAuthenticationType : function(user, type) 
    {
    	var _this = this; 
    	Ext.Ajax.request({
            method : 'POST',
            url : '../admin/changeAuthenticationType',
            params : {
                login : user.data.login,
                type : type
            },
            success : function(result, response) {
            	if( type == 'Internal')
            	{
            		Ext.Msg.alert("Message", "Successfully added " + user.data.name + " as internal user. Internal password is reset to "+user.data.login+"123");
            	}
            	
            	_this.onRefreshList();
            }
        });
    },
    
    /**
     * Change password ui
     */
    onDownloadUserList : function(user, type) 
    {
    	var _this = this; 
    	var downloadMessage = 'Download User list ';
		Ext.Msg.alert("Download",  downloadMessage , function ( ) {
			var url = "../admin/downloadUserList";
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
                    html : '<a href="../admin/downloadUserList" target="_blank">' + 'Download User List' + '</a>'
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
     * Import LDAP Users UI
     */
    onImportLDAPUsers : function() {
    	console.log("importing ldap users");
    	var selectedStore = Ext.StoreManager.get('Admin.store.SelectedLDAPUsersStore');
        selectedStore.removeAll();

        var availableStore = Ext.StoreManager.get('Admin.store.AvailableLDAPUsersStore');
        availableStore.load({
            callback : function(records, operation, success) {
                if (!success) {
                    showErrorMessage(null, "Failed");
                } else {
                    Ext.create ('Ext.window.Window', {
                        height : 400,
                        title : 'Import LDAP Users',
                        layout : 'fit',
                        width : 500,
                        items : [{
                            xtype : 'importLDAPUsers'
                        }]
                    }).show();
                }
            } 
        });
    },
    
    /**
     * Associate the names with the project projectName
     */
    onAddLDAPUsers : function(viewWindow, names, emails, rank) {
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
            success : function(result, response) {
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
    },
    
    /**
     * Search user
     */
    
    searchUser : function(){
    	var userList = this.getUserList();
    	Ext.Ajax.request({
            method : 'GET',
            url : '../admin/listUsers',
            params : {
    			q : userList.down('textfield[name=searchQuery]').getValue()
            },
            success : function(result, request) {
                var users = Ext.decode(result.responseText);
                userList.getStore().loadData(users);
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to load user list"); 
            }
        });
    }
});
