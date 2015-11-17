// Dependencies
Ext.require(['Manage.view.params.MultiValueParser', 'Manage.view.params.IntParser',
             'Manage.view.params.DecimalParser', 'Manage.view.params.StringParser',
             'Manage.view.params.BoolParser']);

/**
 * Factory to create components of specific types
 */
Ext.define('Manage.view.params.ComponentFactory', {
    extend : 'Ext.Base',

    constructor : function() {
        this.parsers = {
            "multi" : Ext.create('Manage.view.params.MultiValueParser'),
            "int" : Ext.create('Manage.view.params.IntParser'),
            "double" : Ext.create('Manage.view.params.DecimalParser'),
            "String" : Ext.create('Manage.view.params.StringParser'),
            "boolean" : Ext.create('Manage.view.params.BoolParser')
        };
    },

    /**
     * Create component based on the config passed
     * If the config is multi-valued  use a combo box
     * Else pass the call the appropriate type-parser
     */
    createComponent : function(config) {
        var parser = null;
        if (this.isNonEmptyArray(config["values"])) {
            parser = this.parsers["multi"];
        } else {
            parser = this.parsers[config["type"]];
        }
        return parser.createComponent.call(parser, config);
    },

    isNonEmptyArray : function(values) {
        return (values !== undefined && values !== null && values instanceof Array && values.length > 0);
    }

});

