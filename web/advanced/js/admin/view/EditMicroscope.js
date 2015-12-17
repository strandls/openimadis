/**
 * View for editing microscope details
 */
Ext.define('Admin.view.EditMicroscope', {
    extend : 'Ext.form.Panel',
    xtype : 'editMicroscope',
    alias : 'widget.editMicroscope',
    bodyPadding: 5,
    url: '../admin/editMicroscope',
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
    	      var data = this.microscope;
	      Ext.apply (this, {
	          items: [{
		      xtype : 'hiddenfield',
		      name : 'microscopeName',
                      value : data.microscopeName	
		  }, {
	              xtype : 'textfield',
	              fieldLabel : 'Microscope Name',
	              name : 'newName',
	              value : data.microscopeName,
	              allowBlank : false
	          }, {
	              xtype : 'textfield',
	              fieldLabel : 'IP Address',
	              name : 'ipAddress',
	              value : data.ipAddress,
	              allowBlank : false,
		      regex : /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/, //also in AddMicroscope
		      regexText : "Enter valid IP Address"
	          }, {
	              xtype : 'textfield',
	              fieldLabel : 'MAC Address',
	              name : 'macAddress',
	              value : data.macAddress,
	              allowBlank : false,
                      regex : /^([0-9a-fA-F]{12})$/, //also in AddMicroscope
	              regexText : "Enter valid Mac Address"
	          }, {
     		      xtype : 'textfield',
		      fieldLabel : 'License',
		      name : 'licenses',
		      value : data.licenses,
        	      allowBlank : false
    		  }]
	      });
//	       Bug in extjs - see http:// stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
	      this.on('afterrender', function(me) {
	          delete me.form._boundItems;
	      });
	      Ext.apply (this.initialConfig, {
	          url : '../admin/editMicroscope'
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
        formBind: true, 
        disabled: true, //only enabled once the form is valid
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
	    if (form.isValid()) {
                var values = form.getFieldValues();
                form.submit({
                    success: function(form, action) {
                       Ext.Msg.alert('Success', "Microscope details updated");
                       view.fireEvent("refreshList");
                       view.up().close();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to update microscope");
                    }
                });
            }
        }
    }]
});
