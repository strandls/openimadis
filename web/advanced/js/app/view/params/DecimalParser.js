/**
 * Parser to parser int type config
 */
Ext.define("Manage.view.params.DecimalParser", {
    extend : 'Ext.Base',

    /**
     * Create component call
     */
    createComponent : function(config) {
        var params = {
            name : config["name"],
            fieldLabel : config["label"],
            allowDecimals : true,
            listeners: {
                render: function(c) {
                  Ext.QuickTips.register({
                    target: c.getEl(),
                    text: config["description"]
                  });
                }
              }
        };
        this.addIfDecimal(params, "maxValue", config["max"]);
        this.addIfDecimal(params, "minValue", config["min"]);
        this.addIfDecimal(params, "value", config["default"]);
        return Ext.create('Ext.form.field.Number', params);
    },

    /**
     * Add the int value (if the value is int) to the map with key provided
     */
    addIfDecimal : function(map, key, value) {
        var decimalValue = this.getDecimal(value);
        if (decimalValue !== null)
            map[key] = decimalValue;
    },

    /**
     * Return the value if value is int else return null
     */
    getDecimal : function(value) {
        if (value !== undefined && value !== null && typeof(value) === "number") {
            return value;
        } else {
            return null;
        }
    }

});
