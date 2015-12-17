/**
 * View for adding a new publisher
 */
Ext.define('Manage.view.summarytable.ExportDialog', {
    extend : 'Ext.form.Panel',
    xtype : 'exportDialog',
    alias : 'widget.exportDialog',
    
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
        Ext.apply (this, {
        	items: [
        	        {
        	        	xtype: 'hidden',
	        	   	  	name: 'guids',
	        	   	  	value: guids
        	   	    },
        	        {
        	           fieldLabel: 'Name',
        	           name: 'name',
        	           allowBlank: false
        	        }, 
//        	        {
//        	           fieldLabel : 'Description',
//        	           name : 'description',
//        	           allowBlank : false,
//        	           xtype : 'textarea'
//        	        },
        	        {
        	           xtype : 'combobox',
        	           fieldLabel : 'Format',
        	           name : 'format',
        	           queryMode : 'local',
        	           displayField : 'name',
        	           valueField : 'value',
        	           allowBlank : false,
        	           editable : false,
        	           store : 'Manage.store.ExportFormatStore'
        	       },
        	       {
        	           xtype : 'datefield',
        	           name : 'validity',
        	           fieldLabel : 'Expiry Date',
        	           allowBlank : false,
        	           value : this.expiry,
        	           minValue : new Date()
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
                console.log(values.validity);
                var date=values.validity.getTime();
                console.log(date);
                console.log(values.guids);
                Ext.Ajax.request({
                    method : 'POST',
                    url : '../project/submitDownload',
                    params : {
                        name : values.name,
                        format : values.format,
                        validity: date,
                        guids: values.guids
                    },
                    success : function (result, response){
                        Ext.Msg.alert("Success", "Record download request is submitted successfully.");
                        view.up().close();
                    },
                    failure : function(result, request) {
                        showErrorMessage(result.responseText, "Failed to sumbit download request");
                    } 
                });
            }
        }
    }]
});