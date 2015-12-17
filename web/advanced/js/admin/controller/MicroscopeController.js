/**
 * Microscope manipulation related actions
 */
Ext.require(['Admin.view.AddMicroscope', 'Admin.view.EditMicroscope']); //

Ext.define ('Admin.controller.MicroscopeController', {
    extend : 'Ext.app.Controller',

    refs : [{
           ref : 'microscopeList',
           selector : 'microscopeList'
    }],

    init : function() {
        this.control({
            'microscopeList' : {
                onAddMicroscope : this.onAddMicroscope,
                onEditMicroscope : this.onEditMicroscope,
                onRemoveMicroscope : this.onRemoveMicroscope
            },
            'addMicroscope' : {
                refreshList : this.onRefreshList
            },
            'editMicroscope' : {
                refreshList : this.onRefreshList
            }
        });
    },
    
   

    /**
     * Add new microscope ui
     */
    onAddMicroscope : function() {
    	console.log("on add microscope");
    	
        Ext.create ('Ext.window.Window', {
            height : 250,
            title : 'Add New Microscope',
            width : 400,
            items : [{
                xtype : 'addMicroscope'   
            }]
        }).show();
    },

    /**
     * Edit microscope ui
     */
    onEditMicroscope : function(microscope) {
    	console.log("on edit microscope");
    	
        Ext.create ('Ext.window.Window', {
            height : 220,
            title : 'Edit Microscope',
            width : 400,
            items : [{
                xtype : 'editMicroscope',
		microscope: microscope.data
            }]
        }).show();
    },
    
    /**
     * Remove microscope
     */
    onRemoveMicroscope : function(microscope) {
      	var _this = this;
    	Ext.Ajax.request( {
		method : 'GET',
		url : '../admin/deleteMicroscope',
		params : {
			'microscopeName' : microscope.data.microscopeName
		},
		success : function(result, response) {
			console.log("success on remove microscope");
		 	Ext.Msg.alert("Message", "Successfully removed microscope " + microscope.data.microscopeName);
			_this.onRefreshList();
		},
		failure : function(result, response) {
			console.log("failure onremove microscope");
		}
	});
    	
    },

    /**
     * Refresh the microscope listing
     */
    onRefreshList : function() {
        var microscopeList = this.getMicroscopeList();
        microscopeList.store.load();
    }
});
