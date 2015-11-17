/**
 * View for adding a new client
 */
Ext.define('Manage.view.admin.AddClient', {
    extend : 'Ext.form.Panel',
    xtype : 'addClient',
    alias : 'widget.addClient',
    
    bodyPadding: 5,

    layout: {
    		type: 'hbox'
        },

   defaults: {
        	margin: '10px'
        },
    
    url : '../compute/addClient',
        
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        Ext.apply (this.initialConfig, {
            url : '../compute/addClient'
        });
        this.callParent();
    },

    items : [ {
		xtype : 'textfield',
		fieldLabel : 'Name',
		height : 50,
		labelAlign : 'top',
		name : 'name',
		allowBlank : false,
		flex : 1
	},{
		xtype : 'textfield',
        fieldLabel: 'Version',
        name: 'version',
        height : 50,
		labelAlign : 'top',
        allowBlank: false,
		flex : 1
    },{
		xtype : 'textfield',
        fieldLabel: 'Tags',
        name: 'tags',
        emptyText: 'Provide comma separated tags',
        height : 50,
		labelAlign : 'top',
        allowBlank: true,
		flex : 1
    }, {
		xtype : 'textareafield',
		height : 90,
		fieldLabel : 'Description',
		labelAlign : 'top',
		name : 'description',
		allowBlank : false,
		flex : 1
	}, {
		xtype : 'checkboxfield',
        fieldLabel: 'isWebClient',
        name: 'isWebClient',
        height : 50,
		labelAlign : 'top',
        allowBlank: false,
        value: false,
		flex : 1
	}, {
		xtype : 'textfield',
        fieldLabel: 'Url',
        name: 'url',
        height : 50,
		labelAlign : 'top',
		flex : 1
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
            if (form.isValid()) {
                var values = form.getFieldValues();
                form.submit({
                	submitEmptyText: false,
                    success: function(form, action) {
                       var resp = Ext.decode(action.response.responseText);
                       Ext.Msg.alert("Client ID", resp["clientID"]);
                       
                       view.fireEvent('refreshlist');
                       
                       var pan = view.up('#adminSpace');
                       pan.setHeight(150);
                       
                       view.up().hide();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to add new client");
                    }
                });
            }
        }
    }]
});
