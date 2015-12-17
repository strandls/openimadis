/**
 * View for sharing records
 */
Ext.define('Manage.view.summarytable.ShareDialog', {
    extend : 'Ext.form.Panel',
    xtype : 'shareDialog',
    alias : 'widget.shareDialog',
    
    bodyPadding: 5,

    // Fields will be arranged vertically, stretched to full width
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
    	var guids = this.guids;
    	var flag = this.flag;
        Ext.apply (this, {
        	items: [
        	        {
        	        	xtype: 'hidden',
	        	   	  	name: 'guids',
	        	   	  	value: guids
        	   	    }, 
        	   	    {
        	   	    	xtype: 'hidden',
	        	   	  	name: 'flag',
	        	   	  	value: flag
        	   	    },
        	        {
        	           xtype : 'combobox',
        	           fieldLabel : 'Target Project',
        	           name : 'targetProject',
        	           queryMode : 'local',
        	           displayField : 'name',
        	           valueField : 'value',
        	           allowBlank : false,
        	           editable : false,
        	           store : 'Manage.store.MemberProjectStore'
        	       }
        	]
        });
    	
    	
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        this.callParent();
    },

    // Reset and Submit buttons
    buttons: [{
        text : 'Cancel',
        handler : function() {
            var view = this.up('form');
            view.up().close();
        }
    }, {
        text: 'Reset',
        handler: function() {
            this.up('form').getForm().reset();
        }
    }, {
        text: 'Submit',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
            if (form.isValid()) {
                var values = form.getFieldValues();
                console.log(values.flag);
                
                if(values.flag === 'true')
                {
                	Ext.Ajax.request({
	                    method : 'POST',
	                    url : '../project/transferRecords',
	                    params : {
	                        targetProject : values.targetProject,
	                        guids: values.guids
	                    },
	                    success : function (result, response){
	                        Ext.Msg.alert("Success", "Records are transfered successfully.");
	                        view.up().close();
	                        view.fireEvent('transferSuccessful');
	                    },
	                    failure : function(result, request) {
	                        showErrorMessage(result.responseText, "Failed to transfer records");
	                    } 
                	});
                }
                else
                {
                	Ext.Ajax.request({
	                    method : 'POST',
	                    url : '../project/shareRecords',
	                    params : {
	                        targetProject : values.targetProject,
	                        guids: values.guids
	                    },
	                    success : function (result, response){
	                        Ext.Msg.alert("Success", "Records are shared successfully.");
	                        view.up().close();
	                    },
	                    failure : function(result, request) {
	                        showErrorMessage(result.responseText, "Failed to share records");
	                    } 
                	});
                }
            }
        }
    }]
});