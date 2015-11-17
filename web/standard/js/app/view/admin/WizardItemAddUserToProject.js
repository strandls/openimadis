/**
 * View for adding a User to a project
 */
Ext.define('Manage.view.admin.WizardItemAddUserToProject', {
	extend : 'Ext.form.Panel',
	xtype : 'WIaddUserToProject',
	alias : 'widget.WIaddUserToProject',
	itemId: 'AddUserToProjectForm',
	bodyPadding: 5,

	// The form will submit an AJAX request to this URL when submitted
	url: '../admin/addMembers',

	layout: {
		type: 'hbox',
		align: 'stretch'
	},

	defaults: {
		margin: '10px'
	},

	// The fields
	defaultType: 'textfield',
	initComponent : function() {
		// Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
		this.on('afterrender', function(me) {
			delete me.form._boundItems;
			Ext.getCmp('name').setValue(UserLogin.userLogin);
		});

		Ext.apply(this, {
			items: [{
			        xtype: 'displayfield',
			        fieldLabel: 'User Login',
			        id: 'Display-Login',
			        //width: 250,
			        name: 'login',
			        value: UserLogin.userLogin
			}, {
				xtype : 'combo',
				
				//submitValue: false, // do not submit this field
				fieldLabel : 'Project',
				labelAlign: 'top',
				width: 250,
				
				name : 'projectName',
				store : 'admin.Projects',
				displayField : 'name',
				valueField : 'name',
				
				allowBlank : false,
				forceSelection : true,
				queryMode : 'local'
				
				
			}, {
				xtype : 'combobox',

				fieldLabel : 'Role',
				labelAlign: 'top',
				width: 250,

				name : 'role',
				store : 'admin.UserRoles',
				displayField : 'name',
				valueField : 'value',

				allowBlank : false,
				forceSelection : true,
				queryMode : 'local'
			}, {
				xtype : 'hiddenfield',
				name : 'name',
				id: 'name',
				value : UserLogin.userLogin,
			}, {
				xtype : 'hiddenfield',
				name : 'userLogin',
				id: 'userLogin',
				value : UserLogin.userLogin,
				maxWidth: 0
			}, {
				xtype: 'hiddenfield',
				name: 'names'
			}]
		});
		this.callParent();
	},

	// Reset and Submit buttons
	buttons: [{
		text : 'Skip',
		handler : function() {
			Ext.Msg.alert('Success', "Successfully Added");
			var view = this.up('form');
			view.fireEvent("refreshList");
			var finalview = view.up('form');
			finalview.up().hide();
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
			console.log("UserLogin: "+ UserLogin.userLogin);
			if (form.isValid()) {
				var name = Ext.getCmp('name').setValue(UserLogin.userLogin);
				var values = form.getFieldValues();

				//add the names field as an array
				// to conform with the server request written for the First UI
				var names = [];
				names.push(values.name);
				values.names = Ext.encode(names);

				form.setValues(values);

				form.submit({
					success: function(form, action) {
						Ext.Msg.alert('Success', "Successfully Added");
						view.fireEvent("refreshList");
						var finalview = view.up('form');
						finalview.up().hide();
					},
					failure: function(form, action) {
						Ext.Msg.alert("Error", "Failed to add user to project");
					}
				});
			}
		}
	}]
});
