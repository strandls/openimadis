Ext.define('Manage.view.settings.ChangePassword', {
    extend : 'Ext.form.Panel',
    xtype : 'changePassword',
    alias : 'widget.changePassword',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/changeUserPassword',

    // Fields will be arranged vertically, stretched to full width
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
        var userDetails = this.userDetails;
        Ext.apply (this, {
            items: [{
                fieldLabel: 'Current Password',
                name: 'currentPassword',
                allowBlank: false,
                inputType : 'password'
            }, {
                fieldLabel: 'New Password',
                name: 'password',
                allowBlank: false,
                inputType : 'password'
            }, {
                fieldLabel: 'New Password (Again)',
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
            url : '../admin/changeUserPassword'
        });
        this.callParent();
    },

    
    // Reset and Submit buttons
    buttons: [
              {
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
                           view.up().close();
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
