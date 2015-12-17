/**
 * Parser to parser int type config
 */
Ext.define("Manage.view.params.IntParser", {
    extend : 'Ext.Base',

    /**
     * Create component call
     */
    createComponent : function(config) {
        var params = {
            name : config["name"],
            fieldLabel : config["label"],
            allowDecimals : false,
            listeners: {
                render: function(c) {
                  Ext.QuickTips.register({
                    target: c.getEl(),
                    text: config["description"]
                  });
                }
              }
        };
        this.addIfInt(params, "maxValue", config["max"]);
        this.addIfInt(params, "minValue", config["min"]);
        this.addIfInt(params, "value", config["default"]);
        return Ext.create('Ext.form.field.Number', params);
    },

    /**
     * Add the int value (if the value is int) to the map with key provided
     */
    addIfInt : function(map, key, value) {
        var intValue = this.getInteger(value);
        if (intValue !== null)
            map[key] = intValue;
    },

    /**
     * Return the value if value is int else return null
     */
    getInteger : function(value) {
        if (value !== undefined && value !== null && typeof(value) === "number") {
            return value;
        } else {
            return null;
        }
    }

});
