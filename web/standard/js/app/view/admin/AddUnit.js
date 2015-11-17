/**
 * View for adding a new Unit 
 */
Ext.define('Manage.view.admin.AddUnit', {
    extend : 'Ext.form.Panel',
    xtype : 'addUnit',
    alias : 'widget.addUnit',
    
    bodyPadding: 5,

    // The form will submit an AJAX request to this URL when submitted
    url: '../admin/addUnit',

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
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
	var store = Ext.create("Manage.store.admin.UnitTypes");
        Ext.apply (this, {
            url : '../admin/addUnit',

	    items: [{
		xtype : 'textfield',
		fieldLabel : 'Team Name',
		width: 130,
		labelAlign: 'top',
		name : 'name',
		allowBlank : false
	    },{
		xtype : 'combobox',
		fieldLabel : 'Type',
		width: 130,
		labelAlign: 'top',
		name : 'type',
		queryMode : 'local',
		displayField : 'name',
		valueField : 'value',
		allowBlank : false,
		editable : false,
		store : store
	    },{
		fieldLabel: 'Email ID',
		labelAlign: 'top',
		width: 200,
		name: 'email',
		allowBlank: false,
		vtype : 'email'
	    }, {
		xtype : 'numberfield',
		fieldLabel : 'Storage Quota (in GB)',
		labelAlign: 'top',
		name : 'globalStorage',
		minValue : 1,
		maxValue : 1000000,
		allowDecimal : true,
		decimalPrecision : 1,
		step : 0.1,
		value : 100,
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
                       view.up().hide();
                    },
                    failure: function(form, action) {
                        showErrorMessage(action.response.responseText, "Failed to add new team");
                    }
                });
            }
        }
    }]
});
