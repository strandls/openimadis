/**
 * Validators for different types of data
 */
var iManageValidators = function() {
    var textCmp = Ext.create('Ext.form.field.Text', {});
    var intCmp = Ext.create('Ext.form.field.Number', {
        allowDecimal : false 
    });
    var realCmp = Ext.create('Ext.form.field.Number', {});

    return {
        text : function (value) {
            return textCmp.validateValue(value);
        },
        integer : function (value) {
            return intCmp.validateValue(value);
        },
        real : function (value) {
            return realCmp.validateValue(value);
        }
    };
}();

/**
 * Component to choose value from a combobox or add a new value
 * Validation is also taken care of
 */
Ext.define('Manage.view.summarytable.FieldValueChooser', {
    extend : 'Ext.form.Panel',
    xtype : 'fieldValueChooser',
    alias : 'widget.fieldValueChooser',
    layout : 'anchor',
    bodyPadding : 10,
    bodyBorder : true,
    url : '#',
    initComponent : function() {
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
 
        // TODO: better dispatch.
        var comp = {
            xtype : 'combobox',
            fieldLabel : 'Property Value',
            editable : true,
            store : {
                fields : ['value'],
                proxy : 'memory',
                data : this.fieldData
            },
            displayField : 'value',
            valueField : 'value',
            queryMode : 'local',
            allowBlank : false
        };
        switch (this.fieldType) {
            case "Integer":
                comp.validator = iManageValidators.integer;
                break;
            case "Real":
                comp.validator = iManageValidators.real;
                break;
            case "Time":
                comp = {
                    xtype : 'datefield',
                    fieldLabel : 'Property Value'
                };
                break;
            default:
                comp.validator = iManageValidators.text;
                break;
        }
        var type = this.fieldType;
        var name = this.fieldName;
        var config = {
            items : [comp],
            buttons : [{
                text : 'Add',
                formBind : true,
                disabled : true,
                handler : function() {
                    var view = this.up().up();
                    var name = view.fieldName;
                    var value = this.up().up().items.items[0].getValue();
                    if (value !== null)
                        view.fireEvent('updateProperty', name, type, value);
                    view.up().up().up().close();
                    console.log("update property "+name+" " +value);
                } 
            }]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Manage.view.summarytable.FieldEditor.superclass.initComponent.apply(this, arguments);
    }
});

/**
 * View for editing a field. Allows for choosing a column
 * and setting a value or adding a new column with value
 */
Ext.define('Manage.view.summarytable.FieldEditor', {
    extend : 'Ext.form.Panel',
    xtype : 'fieldEditor',
    alias : 'widget.fieldEditor',
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
        fieldLabel : 'Property Name',
        name : 'name',
        displayField : 'name',
        valueField : 'name',
        allowBlank : false,
        queryMode : 'local',
        store : 'summarytable.UserFields'
    }],
    buttons : [{
        text : 'Next',
        formBind : true,
        disabled : true,
        handler : function() {
            var parentPanel = this.up();
            var view = this.up().up();
            var combobox = parentPanel.up().items.items[0];
            combobox.setDisabled(true);
            parentPanel.remove(this);
            var selectedValue = combobox.getValue(); 
            view.fireEvent('fieldChosen', selectedValue, parentPanel);
        }
    }]
 

});
