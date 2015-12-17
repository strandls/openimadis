/**
 * View for modifying microscope profiles. This view will have the following
 * 1. View profiles of a particular microscope in a table
 * 2. Add profiles to a microscope
 */
Ext.require(['Admin.view.ProfileList']);

Ext.define('Admin.view.MicroscopeProfiles', {
    extend : 'Ext.panel.Panel',
    xtype : 'microscopeProfiles',
    alias : 'widget.microscopeProfiles',
    title : 'Microscope Profiles',
    border : false,
    initComponent : function() {
	console.log("in microscope profile");
        this.microscopeChooser = Ext.create('Ext.form.field.ComboBox', {
            fieldLabel : 'Select Microscope',
	    labelWidth : 150,
            name : 'microscopeName',
            queryMode : 'local',
            displayField : 'microscopeName',
            width : 450, 
            editable : true,
            typeAhead:true,
            store : 'Admin.store.MicroscopeStore',
            padding : '10 10 10 20',
            forceSelection : true,
            listeners : {
                select : {
			fn:function(field, records, opts) {
                        	this.onMicroscopeChange(field.getValue());
                    	},
                    	scope:this
        	},
        	change : {
        		fn:function(field, newValue, oldValue, opts) {
                	    	if(newValue == '' || newValue == null ){
                	    		this.onMicroscopeChange(newValue);
                    		}
                  	},
                    	scope:this
        	} 
            }
        });
        this.profileList = Ext.create('Admin.view.ProfileList', {
            region : 'center'
        });
        Ext.apply (this, {
            items : [{
                	xtype : 'panel',
                	region : 'north',
                	items : [this.microscopeChooser]
            	}, 
		this.profileList
	    ],
            layout : 'border'
        });
        this.callParent();
    },
 
    onMicroscopeChange : function(microscope) {
        var view = this.profileList;
 
        this.profileList.getStore().load({
            params : {
            	microscopeName : microscope
            },
            callback : function() {
                view.microscopeName = microscope;
            }
        });
    }
});
