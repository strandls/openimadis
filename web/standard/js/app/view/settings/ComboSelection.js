/**
 * View which has combobox and place for another view. Based on selection
 * in the combobox, an event "comboselected" is fired with the selected value
 * as the argument.
 */
Ext.define('Manage.view.settings.ComboSelection', {
    extend : 'Ext.panel.Panel',
    bodyPadding: 5,
    layout : 'border',

    initComponent : function() {
        // Listener to change event from combobox
        var comboListener = Ext.create(this.comboListener, {});
        var comboView = Ext.create(this.comboView, {});
        Ext.apply (this.comboConf, {
            region : 'north',
            listeners : {
                change : function(field, newValue, oldValue, opts) {
                    comboListener.fireEvent("comboselected", newValue, oldValue);
                }
            }
        });
        this.combobox = Ext.create('Ext.form.field.ComboBox', this.comboConf);
        Ext.apply(this, {
            items : [this.combobox, comboView]
        });
        this.callParent();
    },

    listeners : {
        afterrender : function(comp, opts) {
            // Select the first element in combobox
            var comboStore = comp.combobox.store;
            var count = comboStore.getCount();
            if (count < 1)
                return;
            comp.combobox.select(comboStore.getAt(0));

            if (comboStore.getCount() == 1) {
                // If only 1 element in combobox, hide it
                comp.combobox.hide();
            }

        }
    }
});
