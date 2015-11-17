/**
 * View for adding a new Microscope 
 */
Ext.define('Manage.view.admin.AddMicroscope', {
    extend : 'Ext.form.Panel',
    xtype : 'addMicroscope',
    alias : 'widget.addMicroscope',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/registerMicroscope',

    layout: {
	type: 'hbox',
	align: 'stretch'
    },

    defaults: {
	margin: '10px',
	flex: 1
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        Ext.apply (this, {
            url : '../admin/registerMicroscope'
        });
        this.callParent();
    },

    items: [{
        xtype : 'textfield',
        fieldLabel : 'Microscope Name',
	labelAlign: 'top',
        name : 'microscopeName',
        allowBlank : false
    }, {
        xtype : 'textfield',
        fieldLabel : 'IP Address',
	labelAlign: 'top',
        name : 'ipAddress',
        allowBlank : false,
	regex : /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/,  //also in EditMicroscope
	regexText : "Enter valid IP Address"
    }, {
        xtype : 'textfield',
        fieldLabel : 'MAC Address',
	labelAlign: 'top',
        name : 'macAddress',
        allowBlank : false,
	regex : /^([0-9a-f]{12})$/, //also in EditMicroscope
	regexText : "Enter valid Mac Address"
	
    }, {
        xtype : 'textfield',
        fieldLabel : 'License',
	labelAlign: 'top',
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
            view.up().hide();
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
                       view.up().hide();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to add new microscope");
                    }
                });
            }
        }
    }]
});
