/**
 * View for adding a new Microscope 
 */
Ext.define('Admin.view.AddMicroscope', {
    extend : 'Ext.form.Panel',
    xtype : 'addMicroscope',
    alias : 'widget.addMicroscope',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/registerMicroscope',

    // Fields will be arranged vertically, stretched to full width
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        Ext.apply (this.initialConfig, {
            url : '../admin/registerMicroscope'
        });
        this.callParent();
    },

    items: [{
        xtype : 'textfield',
        fieldLabel : 'Microscope Name',
        name : 'microscopeName',
        allowBlank : false
    }, {
        xtype : 'textfield',
        fieldLabel : 'IP Address',
        name : 'ipAddress',
        allowBlank : false,
	regex : /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/,  //also in EditMicroscope
	regexText : "Enter valid IP Address"
    }, {
        xtype : 'textfield',
        fieldLabel : 'MAC Address',
        name : 'macAddress',
        allowBlank : false,
	regex : /^([0-9a-fA-F]{12})$/, //also in EditMicroscope
	regexText : "Enter valid Mac Address"
	
    }, {
        xtype : 'textfield',
        fieldLabel : 'License',
        name : 'licenses',
	allowBlank : false,
	regex : /^[0-9]+/,
	regexText : "Enter a positive number"
    }],

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
        formBind: true, 
        disabled: true, //only enabled once the form is valid
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
            if (form.isValid()) {
                var values = form.getFieldValues();
                form.submit({
                    success: function(form, action) {
                       Ext.Msg.alert('Success', "Added new microscope");
                       view.fireEvent('refreshList');
                       view.up().close();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to add new microscope");
                    }
                });
            }
        }
    }]
});
