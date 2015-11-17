/**
 * View for sharing records
 */
Ext.define('Manage.view.dialogs.ShareDialog', {
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
    	var flag = this.flag;
        Ext.apply (this, {
        	items: [
        	   	    {
        	   	    	xtype: 'hidden',
	        	   	  	name: 'flag',
	        	   	  	value: flag
        	   	    },
        	        {
        	           xtype : 'combobox',
        	           fieldLabel : 'Target Project',
			   labelAlign: 'top',
        	           name : 'targetProject',
        	           queryMode : 'local',
        	           displayField : 'name',
        	           valueField : 'value',
        	           allowBlank : false,
        	           editable : false,
        	           store : 'MemberProjects'
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
	    view.fireEvent('close');
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
                
                if(values.flag === 'true') {
                	view.fireEvent('transferSelected', form);
                }
                else {
                	view.fireEvent('shareSelected', form);
                }
            }
        }
    }]
});
