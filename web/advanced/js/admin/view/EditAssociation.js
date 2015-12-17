/**
 * View for editing unit details
 */
Ext.define('Admin.view.EditAssociation', {
    extend : 'Ext.form.Panel',
    xtype : 'editUnit',
    alias : 'widget.editAssociation',
    bodyPadding: 5,
    url: '../admin/editAssociation',
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
    	var allocatedQuota = this.allocatedStorage;
    	var _this = this;
	      Ext.apply (this, {
	          items: [{
	              xtype : 'textfield',
	              fieldLabel : 'Team Name',
	              name : 'unitName',
	              readOnly : true,
	              value : _this.unitName,
	              allowBlank : false
	          },{
	              xtype : 'textfield',
	              fieldLabel : 'Project Name',
	              name : 'projectName',
	              readOnly : true,
	              value : _this.projectName,
	              allowBlank : false
	          }, {
	              xtype : 'numberfield',
	              fieldLabel : 'Team Storage Quota (in GB)',
	              name : 'globalStorage',
	              minValue : _this.projectUsage,
	              maxValue : ( _this.spaceContributed + _this.availableStorage),
	              allowDecimal : true,
	              decimalPrecision : 1,
	              step : 0.1,
	              value : _this.spaceContributed,
	              allowBlank : false
	          }, 
	          {
		          	xtype : 'textfield',
		              fieldLabel : 'Available Space for Team(in GB)',
		              name : 'allocated',
		              allowBlank : false,
		              editable : false,
		              readOnly : true,
		              value : _this.availableStorage
		          },
		      {
	          	xtype : 'textfield',
	              fieldLabel : 'Project Storage Usage(in GB)',
	              name : 'allocated',
	              allowBlank : false,
	              editable : false,
	              readOnly : true,
	              value : _this.projectUsage
	          }]
	      });
//	       Bug in extjs - see http:// stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
	      this.on('afterrender', function(me) {
	          delete me.form._boundItems;
	      });
	      Ext.apply (this.initialConfig, {
	          url : '../admin/editAssociation'
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
                       Ext.Msg.alert('Success', "Team-Project Association updated");
                       view.fireEvent("onRefreshAssociationList",values['projectName'], values['unitName']);
                       view.up().close();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to update association");
                    }
                });
            }
        }
    }]
});
