/**
 * View for membership listing, adding and editing memberships
 */
Ext.define('Manage.view.admin.UserProjectList', {
    extend : 'Ext.grid.Panel',
    xtype : 'userProjectList',
    alias : 'widget.userProjectList',
    store : 'admin.UserProjects',
    title : 'Projects',

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
                	icon : 'images/icons/download.png',
                    text : 'Download As CSV',
                    tooltip : 'Download As CSV',
                    scope : this,
                    handler : this.onDownloadUserProjectList
                }
                /*, ' ', '-', ' ', {
                	xtype: 'textfield',
                	name: 'searchQuery',
                	listeners:{
	                	specialkey: function(field, e){
	                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
	                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
		                    if (e.getKey() == e.ENTER) {
		                        var form = field.up('userProjectList').onSearch();
		                    }
	                	}
                	}
                },{
                	text : 'Search',
                    scope : this,
                    handler : this.onSearch
                }*/
                ]
            }]
        });
        this.callParent();
    },
    
    onDownloadUserProjectList : function() {
    	console.log(this.userLogin);
        this.fireEvent('onUserProjectList' , "" , this.userLogin);
    },
    
    /**
     * Handler called when search is clicked
     */
    onSearch : function() {
        this.fireEvent('searchMember'); 
    }
});
