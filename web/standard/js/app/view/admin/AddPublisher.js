/**
 * View for adding a new publisher 
 */
Ext.define('Manage.view.admin.AddPublisher', {
    extend : 'Ext.form.Panel',
    xtype : 'addPublisher',
    alias : 'widget.addPublisher',
    
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
            url : '../compute/addPublisher'
        });
        this.callParent();
    },

	items : [ {
		xtype : 'textfield',
		fieldLabel : 'Publisher Name',
		height : 50,
		labelAlign : 'top',
		name : 'name',
		allowBlank : false,
		flex : 1
	}, {
		xtype : 'textareafield',
		height : 80,
		fieldLabel : 'Description',
		labelAlign : 'left',
		name : 'description',
		allowBlank : false,
		flex : 2
	} ],

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
                       view.fireEvent('refreshlist');
			    
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
