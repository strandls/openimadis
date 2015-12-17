/**
 * View for membership listing, adding and editing memberships
 */
Ext.define('Admin.view.MembershipList', {
    extend : 'Ext.grid.Panel',
    xtype : 'membershipList',
    alias : 'widget.membershipList',
    store : 'Admin.store.MembershipStore',
    title : 'Members',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'User', dataIndex : 'user', flex : 1},
                {header : 'Project', dataIndex : 'projectName', flex : 1},
                {header : 'Role', dataIndex : 'role', flex : 1}
            ],
            dockedItems : [{
                xtype : 'toolbar',
                items : [{
                    icon : 'images/icons/add.png',
                    text : 'Add',
                    tooltip : 'Add Members',
                    scope : this,
                    handler : this.onAddClick
                }, ' ', '-', ' ', {
                    icon : 'images/icons/wrench.png',
                    text : 'Edit',
                    tooltip : 'Edit Member',
                    scope : this,
                    handler : this.onEditClick
                }, ' ', '-', ' ', {
                    icon : 'images/icons/delete.png',
                    text : 'Remove',
                    tooltip : 'Remove Member',
                    scope : this,
                    handler : this.onRemoveClick
                }, ' ', '-', ' ',   {
                	icon : 'images/icons/download.png',
                    text : 'Download As CSV',
                    tooltip : 'Download As CSV',
                    scope : this,
                    handler : this.onDownloadMembershipList
                }, ' ', '-', ' ', {
                	xtype: 'textfield',
                	name: 'searchQuery',
                	listeners:{
	                	specialkey: function(field, e){
	                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
	                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
		                    if (e.getKey() == e.ENTER) {
		                        var form = field.up('membershipList').onSearch();
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
    	console.log("onAddClick : projectname:");
    	console.log(this.projectName);
    	console.log("onAddClick : userLogin:");
    	console.log(this.userLogin);
    	if(this.projectName == ""||this.projectName == null){
    		Ext.Msg.alert("Warning", "Select Project");
    	    
    	}else{
    		this.fireEvent('onAddMembers' , this.projectName , this.userLogin);
    	}
    },
    
    onDownloadMembershipList : function() {
        this.fireEvent('onDownloadMembershipList' , this.projectName , "");
    },

    /**
     * Handler called when edit is clicked
     */
    onEditClick : function() {
        var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
            this.fireEvent('onEditMember', selected[0]);
        } else {
            Ext.Msg.alert("Warning", "Select entry to edit from the table");
        }
    },

    /**
     * Handler called when remove is clicked
     */
    onRemoveClick : function() {
        var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
            this.fireEvent('onRemoveMember', selected[0]);
        } else {
            Ext.Msg.alert("Warning", "Select entry to remove from the table");
        }
    },
    
    /**
     * Handler called when search is clicked
     */
    onSearch : function() {
        this.fireEvent('searchMember'); 
    }
});
