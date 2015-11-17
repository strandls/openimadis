/**
 * Parser to parser bool type config
 */
Ext.define("Manage.view.params.BoolParser", {
    extend : 'Ext.Base',

    /**
     * Create component call
     */
    createComponent : function(config) {
        var params = {
            name : config["name"],
            boxLabel : config["label"],
            inputValue : "true"
        };
        this.addIfBool(params, "checked", config["default"]);
        return Ext.create('Ext.form.field.Checkbox', params);
    },

    /**
     * Add the boolean value (if the value is boolean) to the map with key provided
     */
    addIfBool : function(map, key, value) {
        if (value !== undefined && value !== null && typeof(value) === "boolean")
            map[key] = value;
    }

});
