/**
 * The field editor to annotate data
 */
Ext.define('Manage.view.dialogs.FieldEditor', {
	extend: 'Ext.form.Panel',
	xtype: 'fieldEditor',
	alias: 'widget.fieldEditor',

	layout: {
		type: 'vbox',
		align: 'stretch'
	},

	defaults: {
		margin: 5
	},

	// NOTE do not change item order
	// items order is used in annotation type filling
	items: [{
		xtype : 'combobox',

		store : 'UserFields',

		allowBlank : false,
		emptyText: '<EnterNew>',
		fieldLabel : 'Name',
		labelAlign: 'top',

		displayField : 'name',
		valueField : 'name',

		queryMode : 'local',

		listeners: {
			//catching and firing here, since the field editor is used in annotation pop up and annotation,
			//so assigning ids to the combo box wont work
			beforequery: function(queryPlan) {
				this.up('form').fireEvent('fieldbeforequery', queryPlan);
			},
			select: function(combo, records) {
				this.up('form').fireEvent('fieldselect', combo, records);
			}
		}
	}, {
		xtype: 'combobox',

		store: 'UserFieldTypes',
		queryMode : 'local',
		editable: false,

		displayField: 'name',
		valueField: 'id',

		allowBlank : false,
		fieldLabel: 'Type',
		labelAlign: 'top',

		listeners: {
			//catching and firing here, since the field editor is used in annotation pop up and annotation,
			//so assigning ids to the combo box wont work
			select: function(combo, records) {
				this.up('form').fireEvent('typeselect', combo, records);
			}
		}
	}, {
		xtype: 'textfield',

		allowBlank : false,
		fieldLabel: 'Value',
		labelAlign: 'top'
	}],

	buttons: [{
		text: 'Cancel',
		handler: function() {
			var form = this.up('form');
			form.fireEvent('close');
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
			var form = this.up('form');
			if (form.isValid()) {
				form.fireEvent('submitproperty', form);
			}
		}
	}]
});

