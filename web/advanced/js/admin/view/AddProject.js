/**
 * View for adding a new project 
 */
Ext.define('Admin.view.AddProject', {
    extend : 'Ext.form.Panel',
    xtype : 'addProject',
    alias : 'widget.addProject',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/addProject',

    // Fields will be arranged vertically, stretched to full width
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    initComponent : function() {
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        Ext.apply (this.initialConfig, {
            url : '../admin/addProject'
        });
        this.callParent();
    },

    items: [{
        xtype : 'textfield',
        fieldLabel : 'Project Name',
        name : 'name',
        allowBlank : false
    },{
        xtype : 'textareafield',
        fieldLabel: 'Notes',
        name: 'notes',
        allowBlank: false
    }
//    , {
//        xtype : 'numberfield',
//        fieldLabel : 'Storage Quota (in GB)',
//        name : 'storageQuota',
//        minValue : 1,
//        maxValue : 1000,
//        allowDecimal : true,
//        decimalPrecision : 1,
//        step : 0.1,
//        value : 100,
//        allowBlank : false
//    }
    ],

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
                       Ext.Msg.alert('Success', "Added new project");
                       view.fireEvent('refreshList');
                       view.up().close();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to add new project");
                    }
                });
            }
        }
    }]
});
