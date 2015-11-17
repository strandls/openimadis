/**
 * View to add a new user annotation. The user will give a key
 * which will be treated as string and the type of value he wants
 * to enter. Based on the type, the correct input field will be
 * prompted for the value
 */
Ext.define('Manage.view.dialogs.AddFieldDialog', {
    extend : 'Ext.form.Panel',
    xtype : 'addField',
    alias : 'widget.addField',
    layout : 'anchor',
    bodyPadding : 10,
    border: false,
    url : '#',
    initComponent : function() {
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        this.callParent();
    },
    items : [{
        xtype : 'combobox',
        fieldLabel : 'Type',
	labelAlign: 'top',
        editable : false,
        // TODO: hardcoded types - can be loaded remotely instead
        store : 'UserFieldTypes',
        queryMode : 'local',
        displayField : 'name',
        valueField : 'id',
        allowBlank : false,
        value : 'Text',
        listeners : {
            change : function(field, newValue, oldValue, opts) {
                var allParentItems = field.up().items.items;
                // For every item, check if the fieldType matches newValue
                // If yes, show it, else hide the field
                for (var i=0; i<allParentItems.length; ++i){
                    var nextItem = allParentItems[i];
                    if (!nextItem.optionalField)
                        continue;
                    if (nextItem.fieldType === newValue) {
                        nextItem.show();
                        nextItem.allowBlank = false;
                    } else {
                        nextItem.hide();
                        nextItem.allowBlank = true;
                    }
                }
            }
        }
    }, {
        xtype : 'textfield',
        optionalField : true,
        fieldType : 'Text',
        fieldLabel : 'Value',
	labelAlign: 'top',
        name : 'TextValue',
        allowBlank : false 
    }, {
        xtype : 'numberfield',
        optionalField : true,
        fieldType : 'Integer',
        fieldLabel : 'Value',
	labelAlign: 'top',
        name : 'IntegerValue',
        allowBlank : true,
        allowDecimals : false,
        hidden : true
    }, {
        xtype : 'numberfield',
        optionalField : true,
        fieldType : 'Real',
        fieldLabel : 'Value',
	labelAlign: 'top',
        name : 'RealValue',
        allowBlank : true,
        hidden : true
    }, {
        xtype : 'datefield',
        optionalField : true,
        fieldType : 'Time',
        fieldLabel : 'Value',
	labelAlign: 'top',
        name : 'TimeValue',
        allowBlank : true,
        hidden : true
    }],
    buttons : [{
    	text: 'Cancel',
	handler: function() {
		var view = this.up('fieldEditor');
		view.fireEvent('close');
	}
}, {
        text : 'Add',
        formBind : true,
        disabled : true,
        handler : function() {
            // Collect all the data
	    var view = this.up('addField');
            var name = view.fieldName;
            var type = view.items.items[0].getValue();
            var value = null;
            for (var i=0; i < view.items.items.length; ++i) {
                var next = view.items.items[i];
                if (next !== null && next.fieldType === type) {
                    value = next.getValue();
                }
            }
            // Fire event to perform update
            if (value !== null)
                view.fireEvent("addProperty", name, type, value);
            console.log("add property " + name + " " + type + " " + value);
        }
    }]
});

