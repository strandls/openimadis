/**
 * View for adding a profile
 */
Ext.define('Manage.view.admin.AddProfile', {
    extend : 'Ext.form.Panel',
    xtype : 'addProfile',
    alias : 'widget.addProfile',
    bodyPadding: 5,
    url: '../admin/addAcquisitionProfile',
    
    autoScroll: true,
    layout: {
	type: 'vbox',
	align: 'stretch'
    },

    defaults: {
	flex: 1
    },

    // The fields
    defaultType: 'textfield',
    initComponent : function() {
	var timeStore = Ext.create('Manage.store.admin.ProfileTimeUnits');
	var lengthStore = Ext.create('Manage.store.admin.ProfileLengthUnits');
	var typeStore = Ext.create('Manage.store.admin.ProfileTypes');
	Ext.apply (this, {
		items: [{
			xtype: 'container',
			layout: {
				type: 'hbox',
				align: 'stretch'
			},

			defaults: {
				flex: 1,
				margin: '2 10 2 10'
			},

			items: [{
				xtype : 'hiddenfield',
				name : 'microscopeName',
				value : this.microscopeName,
				maxWidth: 0
			}, {
				xtype : 'textfield',
				fieldLabel : 'Profile Name',
				labelAlign: 'top',
				name : 'profileName',
				allowBlank : false
			}, {
				xtype : 'numberfield',
				fieldLabel : 'x Pixel Size',
				labelAlign: 'top',
				name : 'xPixelSize',
				minValue : 0,
				decimalPrecision : 10,
				hideTrigger : true,
				keyNavEnabled : false,
				mouseWheelEnabled : false,
				allowBlank : true,
				validator: function(value) {
					var view = this.up('form');
					var form = view.getForm();
					var values = form.getFieldValues();
					if(values.xPixelSize === null && values.xType === null)
						return true;
					if(values.xPixelSize !== null && values.xType !== null)
						return true;

					return false;
				}
			}, {
				xtype : 'combo',
				forceSelection: true,
				fieldLabel : 'X Type',
				labelAlign: 'top',
				queryMode : 'local',
				name : 'xType',
				valueField : 'name',
				displayField : 'value',
				store : typeStore,
				validator: function(value) {
				var view = this.up('form');
				var form = view.getForm();
				var values = form.getFieldValues();
				if(values.xPixelSize === null && values.xType === null)
					return true;
				if(values.xPixelSize !== null && values.xType !== null)
					return true;

				return false;
				}
			},  {
				xtype : 'numberfield',
				fieldLabel : 'y Pixel Size',
				labelAlign: 'top',
				name : 'yPixelSize',
				minValue : 0,
				decimalPrecision : 10,
				hideTrigger : true,
				keyNavEnabled : false,
				mouseWheelEnabled : false,
				allowBlank : true,
				validator: function(value)
				{
					var view = this.up('form');
					var form = view.getForm();
					var values = form.getFieldValues();
					if(values.yPixelSize === null && values.yType === null)
						return true;
					if(values.yPixelSize !== null && values.yType !== null)
						return true;

					return false;
				}
			}, {
				xtype : 'combo',
				forceSelection: true,
				fieldLabel : 'Y Type',
				labelAlign: 'top',
				queryMode : 'local',
				name : 'yType',
				valueField : 'name',
				displayField : 'value',
				store : typeStore,
				validator: function(value)
				{
					var view = this.up('form');
					var form = view.getForm();
					var values = form.getFieldValues();
					if(values.yPixelSize === null && values.yType === null)
						return true;
					if(values.yPixelSize !== null && values.yType !== null)
						return true;

					return false;
				}
			}]
		}, {
			xtype: 'container',
			layout: {
				type: 'hbox',
				align: 'stretch'
			},

			defaults: {
				flex: 1,
				margin: '2 10 2 10'
			},

			items: [{
				xtype : 'numberfield',
				fieldLabel : 'z Pixel Size',
				labelAlign: 'top',
				name : 'zPixelSize',
				minValue : 0,
				decimalPrecision : 10,
				hideTrigger : true,
				keyNavEnabled : false,
				mouseWheelEnabled : false,
				allowBlank : true,
				validator: function(value)
				{
					var view = this.up('form');
					var form = view.getForm();
					var values = form.getFieldValues();
					if(values.zPixelSize === null && values.zType === null)
						return true;
					if(values.zPixelSize !== null && values.zType !== null)
						return true;

					return false;
				}
			},  {
				xtype : 'combo',
				forceSelection: true,
				fieldLabel : 'Z Type',
				labelAlign: 'top',
				queryMode : 'local',
				name : 'zType',
				valueField : 'name',
				displayField : 'value',
				store : typeStore,
				validator: function(value) {
					var view = this.up('form');
					var form = view.getForm();
					var values = form.getFieldValues();
					if(values.zPixelSize === null && values.zType === null)
						return true;
					if(values.zPixelSize !== null && values.zType !== null)
						return true;

					return false;
				}
			}, {
				xtype : 'combo',
				forceSelection: true,
				fieldLabel : 'Elapsed Time Unit',
				labelAlign: 'top',
				queryMode : 'local',
				name : 'timeUnit',
				valueField : 'name',
				displayField : 'value',
				store : timeStore,
				allowBlank : true	
			}, {
				xtype : 'combo',
				forceSelection: true,
				fieldLabel : 'Exposure Time Unit',
				labelAlign: 'top',
				queryMode : 'local',
				name : 'exposureTimeUnit',
				valueField : 'name',
				displayField : 'value',
				store : timeStore,
				allowBlank : true	
			}, {
				xtype : 'combo',
				forceSelection: true,
				fieldLabel : 'Length Unit',
				labelAlign: 'top',
				queryMode : 'local',
				name : 'lengthUnit',
				valueField : 'name',
				displayField : 'value',
				store : lengthStore,
				allowBlank : true		 
			}]
		}]

	});
	//	       Bug in extjs - see http:// stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
	this.on('afterrender', function(me) {
	delete me.form._boundItems;
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
        formBind: true, 
        disabled: true, //only enabled once the form is valid
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
	    if (form.isValid()) {
                var values = form.getFieldValues();
                form.submit({
                    success: function(form, action) {
                       Ext.Msg.alert('Success', "Profile added");
                       view.fireEvent("refreshList", view.microscopeName);
                       view.up().hide();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to add profile");
                    }
                });
            }
        }
    }]
});
