/**
 * View for adding a new Unit 
 */
Ext.define('Admin.view.AddUnit', {
    extend : 'Ext.form.Panel',
    xtype : 'addUnit',
    alias : 'widget.addUnit',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/addUnit',

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
            url : '../admin/addUnit'
        });
        this.callParent();
    },

    items: [{
        xtype : 'textfield',
        fieldLabel : 'Team Name',
        name : 'name',
        allowBlank : false
    },{
    	xtype : 'combobox',
        fieldLabel : 'Type',
        name : 'type',
        queryMode : 'local',
        displayField : 'name',
        valueField : 'value',
        allowBlank : false,
        editable : false,
        store : 'Admin.store.UnitTypeStore'
    },{
        fieldLabel: 'Email ID',
        name: 'email',
        allowBlank: false,
        vtype : 'email'
    }, {
        xtype : 'numberfield',
        fieldLabel : 'Storage Quota (in GB)',
        name : 'globalStorage',
        minValue : 1,
        maxValue : 1000000,
        allowDecimal : true,
        decimalPrecision : 1,
        step : 0.1,
        value : 100,
        allowBlank : false
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
                       Ext.Msg.alert('Success', "Added new team");
                       view.fireEvent('refreshList');
                       view.up().close();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to add new team");
                    }
                });
            }
        }
    }]
});
