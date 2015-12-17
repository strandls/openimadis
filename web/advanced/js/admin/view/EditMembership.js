/**
 * View for editing membership details
 */
Ext.define('Admin.view.EditMembership', {
    extend : 'Ext.form.Panel',
    xtype : 'editMember',
    alias : 'widget.editMember',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/editMember',

    // Fields will be arranged vertically, stretched to full width
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
        var membershipDetails = this.membershipDetails;
        Ext.apply (this, {
            items: [{
                fieldLabel : 'User',
                name : 'name',
                readOnly : true,
                value : membershipDetails.user
            }, {
                xtype : 'combobox',
                fieldLabel : 'Role',
                name : 'role',
                queryMode : 'local',
                displayField : 'name',
                valueField : 'value',
                allowBlank : false,
                value : membershipDetails.role,
                editable : false,
                store : 'Admin.store.UserRoleStore'
            }, {
                xtype : 'hiddenfield',
                name : 'projectName',
                value : membershipDetails.projectName
            }]
        });
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        Ext.apply (this.initialConfig, {
            url : '../admin/editMember'
        });
        this.callParent();
    },

    
    // Reset and Submit buttons
    buttons: [{
        text: 'Cancel',
        handler: function() {
            this.up('form').up().close();
        }
    }, {
        text: 'Submit',
        disabled: false,
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
            if (form.isValid()) {
                var values = form.getFieldValues();
                form.submit({
                    success: function(form, action) {
                        Ext.Msg.alert('Success', "Membership details updated");
                        view.fireEvent('refreshList', view.membershipDetails.projectName);
                        view.up().close();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to update membership");
                    }
                });
            }
        }
    }]
});
