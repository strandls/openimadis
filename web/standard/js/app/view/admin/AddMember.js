/**
 * View for adding a User to a project
 */
Ext.define('Manage.view.admin.AddMember', {
	extend : 'Ext.form.Panel',
	xtype : 'addMember',
	alias : 'widget.addMember',

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
				name : 'projectName',
				value : this.projectName,
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
			console.log("ul: "+this.userLogin);
			console.log("pn: "+this.projectName);
			if (form.isValid()) {
				var values = form.getFieldValues();
				console.log("vn: "+values.name);
				//add the names field as an array
				// to conform with the server request written for the First UI
				var names = [];
				names.push(values.name);
				values.names = Ext.encode(names);

				form.setValues(values);

				form.submit({
					success: function(form, action) {
						Ext.Msg.alert('Success', "Added user to project");
						view.fireEvent('refreshList', view.projectName);
						view.up().hide();
					},
					failure: function(form, action) {
						Ext.Msg.alert("Error", "Failed to add user to project");
					}
				});
			}
		}
	}]
});
