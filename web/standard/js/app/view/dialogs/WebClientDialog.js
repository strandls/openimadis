/**
 * View for sharing records
 */
Ext.define('Manage.view.dialogs.WebClientDialog', {
    extend : 'Ext.form.Panel',
    xtype : 'webClientDialog',
    alias : 'widget.webClientDialog',
    
    bodyPadding: 5,

    // Fields will be arranged vertically, stretched to full width
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
    	var id = this.id;
    	var appName = this.appName;
    	var appUrl = this.appUrl;
    	var appDescription = this.description;
    	var guids = this.guids;
        Ext.apply (this, {
        	items: [
        	   	    {
        	   	    	xtype: 'hidden',
	        	   	  	name: 'id',
	        	   	  	value: id
        	   	    },
        	   	    {
        	   	    	xtype: 'hidden',
	        	   	  	name: 'guids',
	        	   	  	value: guids
        	   	    },
        	   	    {
        	   	    	xtype: 'textfield',
         	           fieldLabel: 'Name',
         	           labelAlign: 'top',
         	           name: 'name',
         	           id:'appname',
         	           allowBlank: false,
         	          readOnly: true,
         	           value: appName
         	        },
         	       {
        	   	    	xtype: 'textfield',
         	           fieldLabel: 'Url',
         	           labelAlign: 'top',
         	           name: 'url',
         	           id:'appurl',
         	           readOnly: true,
         	           allowBlank: false,
         	           value: appUrl
         	        },
         	       {
        	   	    	xtype: 'textarea',
         	           fieldLabel: 'Description',
         	           labelAlign: 'top',
         	           name: 'url',
         	           id:'appDescription',
         	           readOnly: true,
         	           allowBlank: false,
         	           value: appDescription
         	        },
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
            view.close();
        }
    }, {
        text: 'Invoke',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
            if (form.isValid()) {
                var values = form.getFieldValues();
                
                view.fireEvent("invokeLink", values.id);
            }
        }
    }]
});
