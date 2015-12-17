/**
 * View for adding a profile
 */
Ext.define('Admin.view.AddProfile', {
	extend : 'Ext.form.Panel',
	xtype : 'addProfile',
	alias : 'widget.addProfile',
	bodyPadding : 5,
	url : '../admin/addAcquisitionProfile',
	layout : 'anchor',
	defaults : {
		anchor : '100%'
	},

	// The fields
	defaultType : 'textfield',
	initComponent : function()
	{
		Ext.apply(this, {
			items : [
					{
						xtype : 'hiddenfield',
						name : 'microscopeName',
						value : this.microscopeName
					},
					{
						xtype : 'textfield',
						fieldLabel : 'Profile Name',
						name : 'profileName',
						allowBlank : false
					},
					{
						xtype : 'numberfield',
						fieldLabel : 'x Pixel Size',
						name : 'xPixelSize',
						minValue : 0,
						decimalPrecision : 10,
						hideTrigger : true,
						keyNavEnabled : false,
						mouseWheelEnabled : false,
						allowBlank : true,
						validator : function(value)
						{
							var view = this.up('form');
							var form = view.getForm();
							var values = form.getFieldValues();
							if (values.xPixelSize == null
									&& values.xType == null) return true;
							if (values.xPixelSize != null
									&& values.xType != null) return true;

							return false;
						}
					},
					{
						xtype : 'combo',
						fieldLabel : 'X Type',
						queryMode : 'local',
						name : 'xType',
						valueField : 'name',
						displayField : 'value',
						store : 'Admin.store.ProfileTypeStore',
						validator : function(value)
						{
							var view = this.up('form');
							var form = view.getForm();
							var values = form.getFieldValues();
							if (values.xPixelSize == null
									&& values.xType == null) return true;
							if (values.xPixelSize != null
									&& values.xType != null) return true;

							return false;
						}
					},
					{
						xtype : 'numberfield',
						fieldLabel : 'y Pixel Size',
						name : 'yPixelSize',
						minValue : 0,
						decimalPrecision : 10,
						hideTrigger : true,
						keyNavEnabled : false,
						mouseWheelEnabled : false,
						allowBlank : true,
						validator : function(value)
						{
							var view = this.up('form');
							var form = view.getForm();
							var values = form.getFieldValues();
							if (values.yPixelSize == null
									&& values.yType == null) return true;
							if (values.yPixelSize != null
									&& values.yType != null) return true;

							return false;
						}
					},
					{
						xtype : 'combo',
						fieldLabel : 'Y Type',
						queryMode : 'local',
						name : 'yType',
						valueField : 'name',
						displayField : 'value',
						store : 'Admin.store.ProfileTypeStore',
						validator : function(value)
						{
							var view = this.up('form');
							var form = view.getForm();
							var values = form.getFieldValues();
							if (values.yPixelSize == null
									&& values.yType == null) return true;
							if (values.yPixelSize != null
									&& values.yType != null) return true;

							return false;
						}
					},
					{
						xtype : 'numberfield',
						fieldLabel : 'z Pixel Size',
						name : 'zPixelSize',
						minValue : 0,
						decimalPrecision : 10,
						hideTrigger : true,
						keyNavEnabled : false,
						mouseWheelEnabled : false,
						allowBlank : true,
						validator : function(value)
						{
							var view = this.up('form');
							var form = view.getForm();
							var values = form.getFieldValues();
							if (values.zPixelSize == null
									&& values.zType == null) return true;
							if (values.zPixelSize != null
									&& values.zType != null) return true;

							return false;
						}
					},
					{
						xtype : 'combo',
						fieldLabel : 'Z Type',
						queryMode : 'local',
						name : 'zType',
						valueField : 'name',
						displayField : 'value',
						store : 'Admin.store.ProfileTypeStore',
						validator : function(value)
						{
							var view = this.up('form');
							var form = view.getForm();
							var values = form.getFieldValues();
							if (values.zPixelSize == null
									&& values.zType == null) return true;
							if (values.zPixelSize != null
									&& values.zType != null) return true;

							return false;
						}
					}, {
						xtype : 'combo',
						fieldLabel : 'Time Unit',
						queryMode : 'local',
						name : 'timeUnit',
						valueField : 'name',
						displayField : 'value',
						store : 'Admin.store.ProfileTimeUnitStore',
						allowBlank : true,
						forceSelection : true
					}, {
						xtype : 'combo',
						fieldLabel : 'Length Unit',
						queryMode : 'local',
						name : 'lengthUnit',
						valueField : 'name',
						displayField : 'value',
						store : 'Admin.store.ProfileLengthUnitStore',
						allowBlank : true,
						forceSelection : true
					} ]
		});
		//	       Bug in extjs - see http:// stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
		this.on('afterrender', function(me)
		{
			delete me.form._boundItems;
		});
		Ext.apply(this.initialConfig, {
			url : '../admin/addAcquisitionProfile'
		});
		this.callParent();
	},

	// Reset and Submit buttons
	buttons : [
			{
				text : 'Cancel',
				handler : function()
				{
					var view = this.up('form');
					view.up().close();
				}
			},
			{
				text : 'Reset',
				handler : function()
				{
					this.up('form').getForm().reset();
				}
			},
			{
				text : 'Submit',
				formBind : true,
				disabled : true, //only enabled once the form is valid
				handler : function()
				{
					var view = this.up('form');
					var form = view.getForm();
					if (form.isValid())
					{
						var values = form.getFieldValues();
						form.submit({
							success : function(form, action)
							{
								Ext.Msg.alert('Success', "Profile added");
								view.fireEvent("refreshList",
										this.form.microscopeName);
								view.up().close();
							},
							failure : function(form, action)
							{
								showErrorMessage(action.response.responseText,
										"Failed to add profile");
							}
						});
					}

				}
			} ]
});
