/**
 * View for editing project details
 */
Ext.define('Manage.view.admin.EditProject', {
    extend : 'Ext.form.Panel',
    xtype : 'editProject',
    alias : 'widget.editProject',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/editProject',

    layout: {
	type: 'hbox'
    },

    defaults: {
	margin: '10px'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
        var projectDetails = this.projectDetails;
        Ext.apply (this, {
            items: [{
                xtype : 'textfield',
                fieldLabel : 'Project Name',
		labelAlign: 'top',
                name : 'name',
                allowBlank : false,
                value : projectDetails.name,
                readOnly : true,
		height: 50,
		flex: 1
            },{
                xtype : 'textareafield',
                fieldLabel: 'Notes',
		labelAlign: 'left',
		labelWidth: 40,
                name: 'notes',
                value : projectDetails.notes,
                allowBlank: false,
		height: 80,
		flex: 2
            }]
        });
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        Ext.apply (this.initialConfig, {
            url : '../admin/editProject'
        });
        this.callParent();
    },

    
    // Reset and Submit buttons
    buttons: [{
        text : 'Cancel',
        handler : function() {
            var view = this.up('form');
	    
	    //reset the height
	    var pan = this.up('#adminSpace');
	    pan.setHeight(150);

            view.up().hide();
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
	    var me = this;
            if (form.isValid()) {
                var values = form.getFieldValues();
                form.submit({
                    success: function(form, action) {
                       Ext.Msg.alert('Success', "Updated project details");
                       view.fireEvent('refreshList');

		    
			    //reset the height
			    var pan = me.up('#adminSpace');
			    pan.setHeight(150);

			    view.up().hide();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to update project");
                    }
                });
            }
        }
    }]
});
