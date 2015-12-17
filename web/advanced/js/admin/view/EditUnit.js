/**
 * View for editing unit details
 */
Ext.define('Admin.view.EditUnit', {
    extend : 'Ext.form.Panel',
    xtype : 'editUnit',
    alias : 'widget.editUnit',
    bodyPadding: 5,
    url: '../admin/editUnit',
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
    	var allocatedQuota = this.allocatedStorage;
    	console.log("allocated quota for the team is ");
    	console.log(allocatedQuota);
    	var data = this.unit;
	      Ext.apply (this, {
	          items: [{
	              xtype : 'textfield',
	              fieldLabel : 'Team Name',
	              name : 'name',
	              readOnly : true,
	              value : data.name,
	              allowBlank : false
	          },{
	          	xtype : 'combobox',
	              fieldLabel : 'Type',
	              name : 'type',
	              queryMode : 'local',
	              displayField : 'name',
	              valueField : 'value',
	              allowBlank : false,
	              editable : false,
	              value : data.type,
	              store : 'Admin.store.UnitTypeStore'
	          },{
	              fieldLabel: 'Email ID',
	              name: 'email',
	              allowBlank: false,
	              value : data.email,
	              vtype : 'email'
	          }, {
	              xtype : 'numberfield',
	              fieldLabel : 'Storage Quota (in GB)',
	              name : 'globalStorage',
	              minValue : allocatedQuota,
	              maxValue : 1000000,
	              allowDecimal : true,
	              decimalPrecision : 1,
	              step : 0.1,
	              value : data.globalStorage,
	              allowBlank : false
	          }, {
	          	xtype : 'textfield',
	              fieldLabel : 'Allocated Storage(in GB)',
	              name : 'allocated',
	              allowBlank : false,
	              editable : false,
	              readOnly : true,
	              value : allocatedQuota
	          }]
	      });
//	       Bug in extjs - see http:// stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
	      this.on('afterrender', function(me) {
	          delete me.form._boundItems;
	      });
	      Ext.apply (this.initialConfig, {
	          url : '../admin/editUnit'
	      });
        this.callParent();
    },
    
    // Reset and Submit buttons
    buttons: [{
        text : 'Cancel',
        handler : function() {
            var view = this.up('form');
            view.up().close();
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
                       Ext.Msg.alert('Success', "Team details updated");
                       view.fireEvent("refreshList");
                       view.up().close();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to update team");
                    }
                });
            }
        }
    }]
});
