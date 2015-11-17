/**
 * View for profile manipulation. This view will have the following,
 * 1. List of profiles and some properties of profiles in a table
 * 2. Ability to add a new profile
 */
Ext.define('Manage.view.admin.MicroscopeProfileList', {
    extend : 'Ext.grid.Panel',
    xtype : 'profileList',
    alias : 'widget.profileList',
    title : 'Profiles',

    store: 'admin.MicroscopeProfiles',

    initComponent : function() {
	console.log("in profile list");
        Ext.apply (this, {
            columns : [
		{header : 'Name', dataIndex : 'profileName', flex : 1},
		{header : 'x Pixel Size', dataIndex : 'xPixelSize', flex : 1},
		{header : 'X Type', dataIndex : 'xType', flex : 1},
		{header : 'y Pixel Size', dataIndex : 'yPixelSize', flex : 1},
		{header : 'Y Type', dataIndex : 'yType', flex : 1},
		{header : 'z Pixel Size', dataIndex : 'zPixelSize', flex : 1},
		{header : 'Z Type', dataIndex : 'zType', flex : 1},
		{header : 'Elapsed Time Unit', dataIndex : 'timeUnitLowerCase', flex : 1},
		{header : 'Exposure Time Unit', dataIndex : 'exposureTimeUnitLowerCase', flex : 1},
		{header : 'Length Unit', dataIndex : 'lengthUnitLowerCase', flex : 1}
            ],
            dockedItems : [{
                xtype : 'toolbar',
                items : [{
                    icon : 'images/icons/add.png',
                    text : 'Add',
                    tooltip : 'Add Profile',
                    scope : this,
                    handler : this.onAddClick
                }]
            }]
        });
        this.callParent();
    },

    /**
     * Handler called when add is clicked
     */
    onAddClick : function() {
	var microscopeName = this.microscopeName; //set by ProfileController
        if(microscopeName === null || microscopeName === undefined) {
		 Ext.Msg.alert("Warning", "Select microscope to add profile to");
	} else {
	        this.fireEvent('onAddProfile', microscopeName); 
	}
    },
     
});
