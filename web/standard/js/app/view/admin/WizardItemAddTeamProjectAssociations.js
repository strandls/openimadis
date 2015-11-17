/**
 * View for adding new members to a project
 */
Ext.define('Manage.view.admin.WizardItemAddTeamProjectAssociations', {
	extend : 'Ext.form.Panel',
	xtype : 'WIaddTeamProjectAssociations',
	alias : 'widget.WIaddTeamProjectAssociations',
	itemId  : 'AddTeamProjectAssociationsForm',

	bodyPadding: 5,

	// The form will submit an AJAX request to this URL when submitted
	url: '../admin/associateUnits',

	layout: {
		type: 'hbox',
		align: 'stretch'
	},

	defaults: {
		margin: '10px'
	},

	initComponent : function() {
		// Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
		this.on('afterrender', function(me) {
			delete me.form._boundItems;
		});

		var me = this;
		Ext.apply(this, {
			items: [{
				xtype : 'combo',

				submitValue: false, // do not submit this field
				fieldLabel : 'Team',
				labelAlign: 'top',
				width: 250,

				name: "name",
				store : 'admin.AvailableUnits',
				displayField : 'name',
				valueField : 'name',

				allowBlank : false,
				forceSelection : true,
				queryMode : 'local',

				listeners: {
					change: function(combo, records) {
						me.setQuotaMax(records);
					},
					scope: this
				}
			}, {
				xtype : 'numberfield',
				id: 'quota',

				fieldLabel : 'Storage contribution by team (in GB)',
				labelAlign: 'top',
				width: 250,

				name : 'quota',
				allowBlank : false
			}, {
				xtype : 'textfield',
				id: 'available',

				fieldLabel : 'Available Quota',
				labelAlign: 'top',
				width: 250,

				name : 'available',
				allowBlank : false,
				editable : false,
	            readOnly : true,
	            value : 0
			}, {
				xtype : 'hiddenfield',
				id: 'projectName',
				name : 'projectName',
				value : Utils.ProjectName,
				maxWidth: 0
			}, {
				xtype: 'hiddenfield',
				name: 'unitNames'
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
			var me = this;
			// Load Available Users Store
			var availableStore = Ext.StoreManager.get('admin.AvailableUsers');
			availableStore.load({
				params : {
					projectName : Utils.ProjectName, 
					userLogin : me.userLogin
				},
				callback : function(records, operation, success) {
					if (!success) {
						showErrorMessage(null, "Failed");
					} else {
						console.log("success loading store");
					}
				} 
			});
			var pan = this.up('#adminSpace');
		    pan.setTitle("Add Member");
            var wizard = this.up('#wizardForm');
            wizard.getLayout().setActiveItem('AddMemberForm');
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
				values.unitNames = Ext.encode(names);

				form.setValues(values);
				console.log("ProjectName: "+ Utils.ProjectName);
				form.submit({
					success: function(form, action) {
						// Load Available Users Store
						var availableStore = Ext.StoreManager.get('admin.AvailableUsers');
						availableStore.load({
							params : {
								projectName : Utils.ProjectName,
								userLogin : me.userLogin
							},
							callback : function(records, operation, success) {
								if (!success) {
									showErrorMessage(null, "Failed");
								} else {
									console.log("success loading store");
								}
							} 
						});
						var pan = me.up('#adminSpace');
					    pan.setTitle("Add Member");
						var wizard = me.up('#wizardForm');
	                	wizard.getLayout().setActiveItem('AddMemberForm');
					},
					failure: function(form, action) {
						Ext.Msg.alert("Error", "Failed to add Association");
					}
				});
				
			}
		}
	}],

	/**
	 * set the max available space on the quota based on team selected
	 * @param {String} team
	 */
	setQuotaMax: function(team) {
		var store = Ext.StoreManager.get('admin.AvailableUnits');

		var index = store.find('name', team);
		var record = store.getAt(index);

		var max = record.get('availableSpace');
		this.down('#quota').setMaxValue(max);
		this.down('#available').setValue(max);
	}
});
