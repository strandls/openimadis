/**
 * View for editing user details
 */
Ext.define('Manage.view.admin.ChangePassword', {
    extend : 'Ext.form.Panel',
    
    /**
     * WARNING - no xtype here,
     * xtype: 'changePassword' already present in view/settings/ChangePassword
     * So if in future adding xtype take care of this xtype name clash
     */

    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/resetPassword',

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
        var userDetails = this.userDetails;
        Ext.apply (this, {
            items: [{
                fieldLabel : 'Login',
		labelAlign: 'top',
                name : 'login',
                readOnly : true,
                value : userDetails.login
            }, {
                fieldLabel: 'New Password',
		labelAlign: 'top',
                name: 'password',
                allowBlank: false,
                inputType : 'password'
            }, {
                fieldLabel: 'New Password (Again)',
		labelAlign: 'top',
                name: 'repassword',
                allowBlank: false,
                inputType : 'password'
            }]
        });
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        Ext.apply (this.initialConfig, {
            url : '../admin/resetPassword'
        });
        this.callParent();
    },

    
    // Reset and Submit buttons
    buttons: [{
        text: 'Cancel',
        handler: function() {
            this.up('form').up().hide();
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
                if (values.password !== values.repassword) {
                    Ext.Msg.alert("Warning", "Passwords do not match");
                } else {
                    form.submit({
                        success: function(form, action) {
                           Ext.Msg.alert('Success', "Password updated");
                           view.up().hide();
                        },
                        failure: function(form, action) {
                            showErrorMessage(action.response.responseText, "Failed to change password");
                        }
                    });
                }
            }
        }
    }]
});
