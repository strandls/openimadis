/**
 * Profile manipulation related actions
 */
Ext.require(['Admin.view.AddProfile']); 

Ext.define ('Admin.controller.ProfileController', {
    extend : 'Ext.app.Controller',

    refs : [{
           ref : 'profileList',
           selector : 'profileList'
    }],

    init : function() {
        this.control({
            'profileList' : {
                onAddProfile : this.onAddProfile,
                onDeleteProfile : this.onDeleteProfile
            },
            'addProfile' : {
                refreshList : this.onRefreshList
            }
        });
    },
    
   

    /**
     * Add new profile
     */
    onAddProfile : function(microscopeName) {
    	console.log("on add profile");
    	
        Ext.create ('Ext.window.Window', {
            height : 310,
            title : 'Add New Profile',
	    layout : 'fit',
            width : 500,
            items : [{
                xtype : 'addProfile',
                microscopeName : microscopeName   
            }]
        }).show();
    },
    
    /**
     * Add new profile
     */
    onDeleteProfile : function(profile) {
    	console.log("on delete profile");
    	console.log(profile.get('profileName'));
    	console.log(profile.get('microscopeName'));
    	
    	var _this = this;
    	var profileName = profile.get('profileName');
    	var microscopeName = profile.get('microscopeName');
    	
    	Ext.Ajax.request({
            method : 'GET',
            url : '../admin/deleteAcquisitionProfile',
            params : {
            	profileName : profileName,
            	microscopeName : microscopeName
            },
            success : function (result, request) {
                Ext.Msg.alert("Success", "Request submitted successfully");
                _this.onRefreshList(microscopeName);
            },
            failure : function (result, request) {
                showErrorMessage(result.responseText, "Failed to selected profile");
            }
        });
    },

    /**
     * Refresh the profile listing
     */
    onRefreshList : function(microscope) {
        var profileList = this.getProfileList();
        profileList.store.load({
		params : {
			microscopeName : microscope
		}
	}); 
    }
});
