/**
 * View for editing user details
 */
Ext.define('Admin.view.EditUser', {
    extend : 'Ext.form.Panel',
    xtype : 'editUser',
    alias : 'widget.editUser',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/editUser',

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
                fieldLabel : 'Login',
                name : 'login',
                readOnly : true,
                value : userDetails.login
            }, {
                fieldLabel: 'Name',
                name: 'name',
                allowBlank: false,
                value : userDetails.name
            }, {
                fieldLabel: 'Email ID',
                name: 'emailID',
                allowBlank: false,
                vtype : 'email',
                value : userDetails.emailID
            }, {
                xtype : 'combobox',
                fieldLabel : 'Rank',
                name : 'rank',
                queryMode : 'local',
                displayField : 'name',
                valueField : 'value',
                allowBlank : false,
                value : userDetails.rank,
                editable : false,
                store : 'Admin.store.UserRankStore'
            }, {
                xtype : 'combobox',
                fieldLabel : 'Status',
                name : 'status',
                queryMode : 'local',
                displayField : 'name',
                valueField : 'value',
                allowBlank : false,
                value : userDetails["status"],
                editable : false,
                store : 'Admin.store.UserStatusStore'
            }]
        });
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        Ext.apply (this.initialConfig, {
            url : '../admin/editUser'
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
    },{
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
                form.submit({
                    success: function(form, action) {
                       Ext.Msg.alert('Success', "User details updated");
                       view.fireEvent("refreshList");
                       view.up().close();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to update user");
                    }
                });
            }
        }
    }]
});
