/**
 * Parser to parser multi-valued config and create a combobox component
 */
Ext.define("Manage.view.params.MultiValueParser", {
    extend : 'Ext.Base',

    /**
     * Create component call
     */
    createComponent : function(config) {
        var store = Ext.create('Ext.data.Store', {
            fields : ['value'],
            data : this.getData(config["values"])
        });
        var params = {
            fieldLabel : config['label'],
            store : store,
            queryMode : 'local',
            name : config['name'],
            displayField : 'value',
            editable : false,
            valueField : 'value'
        };
        if (config["default"] !== undefined && config["default"] !== null) {
            params["value"] = config["default"];
        }
        return Ext.create('Ext.form.field.ComboBox', params);
    },

    getData : function(values) {
        var data = new Array();
        for (var i=0; i<values.length; ++i) {
            data.push({"value" : values[i]});
        }
        return data;
    }
});
