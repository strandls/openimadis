/**
 * View for editing microscope details
 */
Ext.define('Manage.view.admin.EditMicroscope', {
    extend : 'Ext.form.Panel',
    xtype : 'editMicroscope',
    alias : 'widget.editMicroscope',
    bodyPadding: 5,
    url: '../admin/editMicroscope',

    layout: {
	type: 'hbox',
	align: 'stretch'
    },

    defaults: {
	margin: '10px',
	flex: 1
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
//	       Bug in extjs - see http:// stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
	      this.on('afterrender', function(me) {
	          delete me.form._boundItems;
	      });

    	      var data = this.microscope;
	      Ext.apply (this, {
	          items: [{
		      xtype : 'hiddenfield',
		      name : 'microscopeName',
                      value : data.microscopeName,
		      maxWidth: 0
		  }, {
	              xtype : 'textfield',
	              fieldLabel : 'Microscope Name',
	              labelAlign: 'top',
	              name : 'newName',
	              value : data.microscopeName,
	              allowBlank : false
	          }, {
	              xtype : 'textfield',
	              fieldLabel : 'IP Address',
	              labelAlign: 'top',
	              name : 'ipAddress',
	              value : data.ipAddress,
	              allowBlank : false,
		      regex : /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/, //also in AddMicroscope
		      regexText : "Enter valid IP Address"
	          }, {
	              xtype : 'textfield',
	              fieldLabel : 'MAC Address',
	              labelAlign: 'top',
	              name : 'macAddress',
	              value : data.macAddress,
	              allowBlank : false,
                      regex : /^([0-9a-f]{12})$/, //also in AddMicroscope
	              regexText : "Enter valid Mac Address"
	          }, {
     		      xtype : 'textfield',
		      fieldLabel : 'License',
	              labelAlign: 'top',
		      name : 'licenses',
		      value : data.licenses,
        	      allowBlank : false
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
                       view.up().hide();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to update microscope");
                    }
                });
            }
        }
    }]
});
