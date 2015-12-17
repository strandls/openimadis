/**
 * View for editing an existing token
 */
Ext.define('Manage.view.workflow.EditToken', {
    extend : 'Ext.form.Panel',
    xtype : 'editToken',
    alias : 'widget.editToken',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../compute/updateToken',

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
            url : '../compute/updateToken'
        });
        var serviceCheckBoxes = new Array();
        var services = Ext.StoreManager.get('Manage.store.ServiceStore').data.items;
        for (var i=0; i<services.length; ++i) {
            var next = services[i];
            var nextData = {
                boxLabel : next.data.name,
                name : next.data.value
            };
            if (_.contains(this.services, next.data.value))
                nextData["checked"] = true;
            serviceCheckBoxes.push(nextData);
        }
        Ext.apply(this, {
            items : [{
                xtype : 'checkboxgroup',
                fieldLabel : 'Services Allowed',
                vertical : true,
                columns : 1,
                items : serviceCheckBoxes
            }, {
                xtype : 'datefield',
                name : 'expiryDay',
                fieldLabel : 'Expiry Date',
                value : this.expiry,
                minValue : new Date()
            }]
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
    }, {
        text: 'Reset',
        handler: function() {
            this.up('form').getForm().reset();
        }
    }, {
        text: 'Update',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
            if (form.isValid()) {
                var values = form.getFieldValues();
                view.fireEvent("updateToken", view, values, view.id);
            }
        }
    }]
});
