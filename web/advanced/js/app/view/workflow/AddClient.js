/**
 * View for adding a new client
 */
Ext.define('Manage.view.workflow.AddClient', {
    extend : 'Ext.form.Panel',
    xtype : 'addClient',
    alias : 'widget.addClient',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../compute/addClient',

    // Fields will be arranged vertically, stretched to full width
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
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

    items: [{
        fieldLabel: 'Name',
        name: 'name',
        allowBlank: false
    },{
        fieldLabel: 'Version',
        name: 'version',
        allowBlank: false
    }, {
        fieldLabel : 'Description',
        name : 'description',
        allowBlank : false,
        xtype : 'textarea'
    }],

    // Reset and Submit buttons
    buttons: [{
        text : 'Cancel',
        handler : function() {
            var view = this.up('form');
            view.up().close();
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
                    success: function(form, action) {
                       var resp = Ext.decode(action.response.responseText);
                       Ext.StoreManager.get('Manage.store.UserClientStore').load(); 
                       Ext.StoreManager.get('Manage.store.AllClientStore').load(); 
                       Ext.StoreManager.get('Manage.store.task.WorkflowClientStore').load();
                       Ext.Msg.alert("Client ID", resp["clientID"]);
                       view.up().close();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to add new client");
                    }
                });
            }
        }
    }]
});
