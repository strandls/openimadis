/**
 * View for adding a User to a project
 */
Ext.define('Manage.view.admin.WizardItemAddMember', {
	extend : 'Ext.form.Panel',
	xtype : 'WIaddMember',
	alias : 'widget.WIaddMember',
	itemId: 'AddMemberForm',
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
		});

		Ext.apply(this, {
			items: [{
				xtype : 'combo',

				submitValue: false, // do not submit this field
				fieldLabel : 'User',
				labelAlign: 'top',
				width: 250,

				name: "name",
				store : 'admin.AvailableUsers',
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
				id : 'projectName',
				name : 'projectName',
				value : Utils.ProjectName,
				maxWidth: 0
			}, {
				xtype : 'hiddenfield',
				name : 'userLogin',
				value : this.userLogin,
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
		text: 'Reset',
		handler: function() {
			this.up('form').getForm().reset();
		}
	}, {
        text: 'Skip',
        handler: function() {
			Ext.Msg.alert('Success', "Successfully Created");
			var view = this.up('form');
			var finalview = view.up('form');
			view.fireEvent('refreshList');
			finalview.fireEvent('refreshList');
			finalview.up().hide();
			
		}
	}, {
		text: 'Next',
		formBind: true, 
		disabled: true, //only enabled once the form is valid
		handler: function() {
			var view = this.up('form');
			var form = view.getForm();
			var me = this;
			if (form.isValid()) {
				var name = Ext.getCmp('projectName').setValue(Utils.ProjectName);
				var values = form.getFieldValues();

				//add the names field as an array
				// to conform with the server request written for the First UI
				var names = [];
				names.push(values.name);
				values.names = Ext.encode(names);

				form.setValues(values);

				form.submit({
					success: function(form, action) {
						Ext.Msg.alert('Success', "Successfully Created");
						var finalview = view.up('form');
						view.fireEvent('refreshList');
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
