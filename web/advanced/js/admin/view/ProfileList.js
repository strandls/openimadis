/**
 * View for profile manipulation. This view will have the following,
 * 1. List of profiles and some properties of profiles in a table
 * 2. Ability to add a new profile
 */
Ext.define('Admin.view.ProfileList', {
    extend : 'Ext.grid.Panel',
    xtype : 'profileList',
    alias : 'widget.profileList',
    store : 'Admin.store.ProfileStore',
    title : 'Profiles',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Name', dataIndex : 'profileName', flex : 1},
                {header : 'x Pixel Size', dataIndex : 'xPixelSize', flex : 1},
        		{header : 'X Type', dataIndex : 'xType', flex : 1},
        		{header : 'y Pixel Size', dataIndex : 'yPixelSize', flex : 1},
        		{header : 'Y Type', dataIndex : 'yType', flex : 1},
        		{header : 'z Pixel Size', dataIndex : 'zPixelSize', flex : 1},
        		{header : 'Z Type', dataIndex : 'zType', flex : 1},
        		{header : 'Time Unit', dataIndex : 'timeUnitLowerCase', flex : 1},
        		{header : 'Length Unit', dataIndex : 'lengthUnitLowerCase', flex : 1}
		/*,
		{header : 'Source Type', dataIndex : 'sourceType', flex : 1}*/
            ],
            dockedItems : [{
                xtype : 'toolbar',
                items : [{
                    icon : 'images/icons/add.png',
                    text : 'Add',
                    tooltip : 'Add Profile',
                    scope : this,
                    handler : this.onAddClick
                }, {
                	icon : 'images/icons/delete.png',
                    text : 'Delete',
                    tooltip : 'Delete Profile',
                    scope : this,
                    handler : this.onDeleteClick
                }]
            }]
        });
        this.callParent();
    },
    
    /**
     * handler called when delete button is clicked
     */
    onDeleteClick : function() {
    	var selected = this.getSelectionModel().getSelection();
        if (selected && selected !== null && selected.length === 1) {
            this.fireEvent('onDeleteProfile', selected[0]);
        } else {
            Ext.Msg.alert("Warning", "Select entry to edit from the table");
        }
    },

    /**
     * Handler called when add is clicked
     */
    onAddClick : function() {
	var microscopeName = this.microscopeName; //set by ProfileController
    	console.log("on add profile");
        if(microscopeName === null || microscopeName === undefined) {
		 Ext.Msg.alert("Warning", "Select microscope to add profile to");
	} else {
	        this.fireEvent('onAddProfile', microscopeName); 
	}
    },
     
});
