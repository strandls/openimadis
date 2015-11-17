/**
 * View for adding a new user
 */
Ext.define('Manage.view.admin.WizardItemAddUser', {
    extend : 'Ext.form.Panel',
    xtype : 'WIaddUser',
    alias : 'widget.WIaddUser',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/addUser',

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
        var me = this;
	var store = Ext.create('Manage.store.admin.UserRanks');
        Ext.apply (this, {
            url : '../admin/addUser',

	    items: [{
		fieldLabel : 'Login ID',
		labelAlign: 'top',
		name : 'login',
		allowBlank : false,
		vtype : 'alpha'
	    },{
		fieldLabel: 'Name',
		labelAlign: 'top',
		name: 'name',
		allowBlank: false
	    },{
		fieldLabel: 'Email ID',
		labelAlign: 'top',
		name: 'emailID',
		allowBlank: false,
		vtype : 'email'
	    }, {
		fieldLabel : 'Password',
		labelAlign: 'top',
		name : 'password',
		allowBlank : false,
		inputType : 'password'
	    }, {
		fieldLabel : 'Password Again',
		labelAlign: 'top',
		name : 'repassword',
		allowBlank : false,
		inputType : 'password'
	    }, {
		xtype : 'combobox',
		fieldLabel : 'Rank',
		labelAlign: 'top',
		name : 'rank',
		queryMode : 'local',
		displayField : 'name',
		valueField : 'value',
		allowBlank : false,
		value : 'TeamMember',
		editable : false,
		store : store
	    }]
        });
        this.callParent();
    },

    // Reset and Submit buttons
    buttons: [{
        text : 'Cancel',
        handler : function() {
            var view = this.up('form');
            var finalview = view.up('form');
			finalview.up().hide();
        }
    }, {
        text: 'Reset',
        handler: function() {
            this.up('form').getForm().reset();
        }
    }, {
        text: 'Next',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
            var me = this;
            if (form.isValid()) {
            	UserLogin.userLogin = form.findField("login").getValue();
                var values = form.getFieldValues();
                if (values.password !== values.repassword) {
                    Ext.Msg.alert("Warning", "Passwords do not match");
                } else {
                    form.submit({
                        success: function(form, action) {
                    	   var dispVal = Ext.getCmp('Display-Login').setValue(form.findField("login").getValue());
						   Manage.app.getController('admin.Users').onRefreshList(); 
                           var pan = me.up('#adminSpace');
   					       pan.setTitle("Add User to Project");
                           var wizard = me.up('#UserWizardForm');
                           wizard.getLayout().setActiveItem('AddUserToProjectForm');
                           //view.up().hide();
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
