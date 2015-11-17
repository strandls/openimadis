/**
 * View for editing user details
 */
Ext.define('Manage.view.admin.EditUser', {
    extend : 'Ext.form.Panel',
    xtype : 'editUser',
    alias : 'widget.editUser',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/editUser',
    
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
	var userRankStore = Ext.create('Manage.store.admin.UserRanks');
	var userStatusStore = Ext.create('Manage.store.admin.UserStatus');
        Ext.apply (this, {
            items: [{
                fieldLabel : 'Login',
		labelAlign: 'top',
                name : 'login',
                readOnly : true,
                value : userDetails.login
            }, {
                fieldLabel: 'Name',
		labelAlign: 'top',
                name: 'name',
                allowBlank: false,
                value : userDetails.name
            }, {
                fieldLabel: 'Email ID',
		labelAlign: 'top',
                name: 'emailID',
                allowBlank: false,
                vtype : 'email',
                value : userDetails.emailID
            }, {
                xtype : 'combobox',
                fieldLabel : 'Rank',
		labelAlign: 'top',
                name : 'rank',
                queryMode : 'local',
                displayField : 'name',
                valueField : 'value',
                allowBlank : false,
                value : userDetails.rank,
                editable : false,
                store : userRankStore
            }, {
                xtype : 'combobox',
                fieldLabel : 'Status',
		labelAlign: 'top',
                name : 'status',
                queryMode : 'local',
                displayField : 'name',
                valueField : 'value',
                allowBlank : false,
                value : userDetails["status"],
                editable : false,
                store : userStatusStore
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
            view.up().hide();
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
                       view.up().hide();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to update user");
                    }
                });
            }
        }
    }]
});
