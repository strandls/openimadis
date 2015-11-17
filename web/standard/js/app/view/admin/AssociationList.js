/**
 * View for membership listing, adding and editing memberships
 */
Ext.define('Manage.view.admin.AssociationList', {
    extend : 'Ext.grid.Panel',
    xtype : 'associationList',
    alias : 'widget.associationList',
    store : 'admin.Associations',
    title : 'Team-Project Association',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Team', dataIndex : 'unitName', flex : 1},
                {header : 'Project', dataIndex : 'projectName', flex : 1},
                {header : 'Quota', dataIndex : 'storageContribution', flex : 1}
            ],
            dockedItems : [{
                xtype : 'toolbar',
                items : [{
                    icon : 'images/icons/add.png',
                    text : 'Add',
                    tooltip : 'Add Team',
                    scope : this,
                    handler : this.onAddClick
                }, ' ' , '-', ' ', {
                    icon : 'images/icons/wrench.png',
                    text : 'Edit',
                    tooltip : 'Edit Association',
                    scope : this,
                    handler : this.onEditClick
                }, ' ', '-', ' ', {
                    icon : 'images/icons/delete.png',
                    text : 'Remove',
                    tooltip : 'Remove Team',
                    scope : this,
                    handler : this.onRemoveClick
                }, ' ', '-', ' ', {
                	xtype: 'textfield',
                	name: 'searchQuery',
                	listeners:{
	                	specialkey: function(field, e){
	                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
	                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
		                    if (e.getKey() == e.ENTER) {
		                        var form = field.up('associationList').onSearch();
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
        this.fireEvent('onAddAssociation', this.projectName); 
    },

    /**
     * Handler called when edit is clicked
     */
    onEditClick : function() {
        var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
            this.fireEvent('onEditAssociation', selected[0]);
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
            this.fireEvent('onRemoveAssociation', selected[0]);
        } else {
            Ext.Msg.alert("Warning", "Select entry to remove from the table");
        }
    },
    
    /**
     * Handler called when search is clicked
     */
    onSearch : function() {
        this.fireEvent('searchUnit'); 
    }
});
