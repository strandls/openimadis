/**
 * View for generating a new auth code
 */
Ext.define('Manage.view.workflow.AuthCodeGenerator', {
    extend : 'Ext.form.Panel',
    xtype : 'authcodeGen',
    alias : 'widget.authcodeGen',
    
    bodyPadding: 15,

    // The form will submit an AJAX request to this URL when submitted
    url: '../compute/generateAuthCode',

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
            url : '../compute/generateAuthCode'
        });
        var serviceCheckBoxes = new Array();
        var services = Ext.StoreManager.get('Manage.store.ServiceStore').data.items;
        for (var i=0; i<services.length; ++i) {
            var next = services[i];
            serviceCheckBoxes.push({
                boxLabel : next.data.name,
                name : next.data.value
            });
        }
        Ext.apply(this, {
            items : [{
                 xtype : 'combobox',
                fieldLabel: 'Client',
                name: 'clientID',
                store : 'Manage.store.AllClientStore',
                queryMode : 'local',
                allowBlank: false,
                editable : false,
                displayField : 'name',
                valueField : 'clientID'
            }, {
                xtype : 'checkboxgroup',
                fieldLabel : 'Services Allowed',
                vertical : true,
                columns : 1,
                items : serviceCheckBoxes
            }, {
                xtype : 'datefield',
                name : 'expiryDay',
                fieldLabel : 'Expiry Date',
                minValue : new Date()
            } , {
                xtype: 'numberfield',
                fieldLabel: 'Number of Tokens',
                name: 'numberOfTokens',
                value: 1,
                minValue: 1,
                maxValue: 5
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
        text: 'Generate',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
            if (form.isValid()) {
                var values = form.getFieldValues();
                view.fireEvent("getAuthCode", view, values);
            }
        }
    }]
});
