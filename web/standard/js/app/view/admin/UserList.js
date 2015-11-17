/**
 * View for user manipulation. This view will have the following,
 * 1. List of users and some properties of users in a table
 * 2. Ability to add a new user
 * 3. Ability to edit user attributes (including password reset)
 */

Ext.define('Manage.view.admin.UserList', {
    extend : 'Ext.grid.Panel',
    xtype : 'userList',
    alias : 'widget.userList',
    title : 'Users',
    store: 'admin.Users',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Name', dataIndex : 'name', flex : 1},
                {header : 'Login', dataIndex : 'login', flex : 1},
                {header : 'Email', dataIndex : 'emailID', flex : 1},
                {header : 'Authentication', dataIndex : 'type', flex : 1},
                {header : 'Credentials', dataIndex : 'rankString', flex : 1},
                {header : 'Status', dataIndex : 'status', flex : 1}
            ],
	    bbar : {
		    xtype : 'pagingtoolbar',
		    store : this.store
	    },
            dockedItems : [{
                xtype : 'toolbar',
		dock: 'top',
                items : [{
                    icon : 'images/icons/add.png',
                    text : 'Add',
                    tooltip : 'Add User',
                    scope : this,
                    handler : this.onAddClick
                }, ' ', '-' , ' ', {
                    icon : 'images/icons/wrench.png',
                    text : 'Edit',
                    tooltip : 'Edit User',
                    scope : this,
                    handler : this.onEditClick
                }, ' ', '-', ' ', {
                    icon : 'images/icons/password.png',
                    text : 'Change Password',
                    tooltip : 'Change Password',
                    scope : this,
                    handler : this.onChangePassword
                }, ' ', '-', ' ', {
                    icon : 'images/icons/allocated.gif',
                    text : 'Import LDAP Users',
                    tooltip : 'Import LDAP Users',
                    scope : this,
                    handler : this.onImportLDAPUsers
                }, ' ', '-', ' ', {
                    icon : 'images/icons/leftarrow.png',
                    text : 'Change To External',
                    tooltip : 'Change To External User',
                    scope : this,
                    handler : this.onChangeToExternal
                }, ' ', '-', ' ', {
                    icon : 'images/icons/rightarrow.png',
                    text : 'Change To Internal',
                    tooltip : 'Change To Internal User',
                    scope : this,
                    handler : this.onChangeToInternal
                }] 
	}, {
                xtype : 'toolbar',
		dock: 'top',
		items: [{
                    icon : 'images/icons/download.png',
                    text : 'Download As CSV',
                    tooltip : 'Download As CSV',
                    scope : this,
                    handler : this.onDownloadUserList
                }, ' ', '-', ' ', {
                	xtype: 'textfield',
                	name: 'searchQuery',
                	listeners:{
	                	specialkey: function(field, e){
	                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
	                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
		                    if (e.getKey() == e.ENTER) {
		                        var form = field.up('userList').onSearch();
		                    }
	                	}
                	}
                },{
                	text : 'Search',
                    scope : this,
                    handler : this.onSearch
                }]
            }]
        });
        this.callParent();
    },
    
    /**
     * Handler called when add is clicked
     */
    onAddClick : function() {
        this.fireEvent('onAddUser'); 
    },
    
    /**
     * Handler called when search is clicked
     */
    onSearch : function() {
        this.fireEvent('searchUser'); 
    },

    /**
     * Handler called when edit is clicked
     */
    onEditClick : function() {
        var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
            this.fireEvent('onEditUser', selected[0]);
        } else {
            Ext.Msg.alert("Warning", "Select user to edit from the table");
        }
    },

    /**
     * Handler called when change password is clicked
     */
    onChangePassword : function() {
        var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
            this.fireEvent('onChangePassword', selected[0]);
        } else {
            Ext.Msg.alert("Warning", "Select user to change password from the table");
        }
    },
    
    /**
     * Handler called when change to internal is clicked
     */
    onChangeToInternal : function() {
        var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
            this.fireEvent('onChangeAuthenticationType', selected[0], 'Internal');
        } else {
            Ext.Msg.alert("Warning", "Select exactly 1 user to change authentication type from the table");
        }
    },
    
    /**
     * Handler called when change to internal is clicked
     */
    onDownloadUserList : function() {
        this.fireEvent('onDownloadUserList');
    },
    
    /**
     * Handler called when change to external is clicked
     */
    onChangeToExternal : function() {
        var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
            this.fireEvent('onChangeAuthenticationType', selected[0], 'External');
        } else {
            Ext.Msg.alert("Warning", "Select exactly 1 user to change authentication type from the table");
        }
    },
    
    /**
     * Handler called when change password is clicked
     */
    onImportLDAPUsers : function() {
        this.fireEvent('onImportLDAPUsers');
    }
});
