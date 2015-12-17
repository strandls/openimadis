/**
 * View to add a new user annotation. The user will give a key
 * which will be treated as string and the type of value he wants
 * to enter. Based on the type, the correct input field will be
 * prompted for the value
 */
Ext.define('Manage.view.summarytable.AddFieldView', {
    extend : 'Ext.form.Panel',
    xtype : 'addField',
    alias : 'widget.addField',
    layout : 'anchor',
    bodyPadding : 10,
    bodyBorder : true,
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
        editable : false,
        // TODO: hardcoded types - can be loaded remotely instead
        store : 'Manage.store.summarytable.UserFieldTypes',
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
        name : 'TextValue',
        allowBlank : false 
    }, {
        xtype : 'numberfield',
        optionalField : true,
        fieldType : 'Integer',
        fieldLabel : 'Value',
        name : 'IntegerValue',
        allowBlank : true,
        allowDecimals : false,
        hidden : true
    }, {
        xtype : 'numberfield',
        optionalField : true,
        fieldType : 'Real',
        fieldLabel : 'Value',
        name : 'RealValue',
        allowBlank : true,
        hidden : true
    }, {
        xtype : 'datefield',
        optionalField : true,
        fieldType : 'Time',
        fieldLabel : 'Value',
        name : 'TimeValue',
        allowBlank : true,
        hidden : true
    }],
    buttons : [{
        text : 'Add',
        formBind : true,
        disabled : true,
        handler : function() {
            // Collect all the data
            var view = this.up().up();
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
            view.up().up().up().close();
        }
    }]
});

