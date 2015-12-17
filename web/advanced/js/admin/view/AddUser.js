/**
 * View for adding a new user
 */
Ext.define('Admin.view.AddUser', {
    extend : 'Ext.form.Panel',
    xtype : 'addUser',
    alias : 'widget.addUser',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/addUser',

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
            url : '../admin/addUser'
        });
        this.callParent();
    },

    items: [{
        fieldLabel : 'Login ID',
        name : 'login',
        allowBlank : false,
        vtype : 'alpha'
    },{
        fieldLabel: 'Name',
        name: 'name',
        allowBlank: false
    },{
        fieldLabel: 'Email ID',
        name: 'emailID',
        allowBlank: false,
        vtype : 'email'
    }, {
        fieldLabel : 'Password',
        name : 'password',
        allowBlank : false,
        inputType : 'password'
    }, {
        fieldLabel : 'Password Again',
        name : 'repassword',
        allowBlank : false,
        inputType : 'password'
    }, {
        xtype : 'combobox',
        fieldLabel : 'Rank',
        name : 'rank',
        queryMode : 'local',
        displayField : 'name',
        valueField : 'value',
        allowBlank : false,
        value : 'TeamMember',
        editable : false,
        store : 'Admin.store.UserRankStore'
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
                           Ext.Msg.alert('Success', "Added new user");
                           view.fireEvent("refreshList");
                           view.up().close();
                        },
                        failure: function(form, action) {
                            showErrorMessage(action.response.responseText, "Failed to add new user");
                        }
                    });
                }
            }
        }
    }]
});
