/**
 * View for adding a new project 
 */
Ext.define('Manage.view.admin.AddProject', {
    extend : 'Ext.form.Panel',
    xtype : 'addProject',
    alias : 'widget.addProject',
    
    bodyPadding: 5,

    layout: {
	type: 'hbox'
    },

    defaults: {
	margin: '10px'
    },

    initComponent : function() {
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        Ext.apply (this, {
            url : '../admin/addProject'
        });
        this.callParent();
    },

    items: [{
        xtype : 'textfield',
        fieldLabel : 'Project Name',
	height: 50,
	labelAlign: 'top',
        name : 'name',
        allowBlank : false,
	flex: 1
    },{
        xtype : 'textareafield',
	height: 80,
        fieldLabel: 'Notes',
	labelAlign: 'left',
        name: 'notes',
        allowBlank: false,
	flex: 2
    }],

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
            var view = this.up('form');
            var form = view.getForm();
            var me = this;
            if (form.isValid()) {
                var values = form.getFieldValues();
                form.submit({
                    success: function(form, action) {
                       Ext.Msg.alert('Success', "Added new project");
                       view.fireEvent('refreshList');
			    
			    //reset the height
			    var pan = me.up('#adminSpace');
			    pan.setHeight(150);

			    view.up().hide();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to add new project");
                    }
                });
            }
        }
    }]
});
