/**
 * Generate params component based on the json specification.
 * See README for more details
 */
Ext.require(['Manage.view.params.ComponentFactory']);

Ext.define('Manage.view.params.ParamsPanel', {
    extend : 'Ext.form.Panel',
    alias:'widget.paramsPanel',
    
    bodyPadding : 10,
    initComponent : function() {
        var itemConfigs = this.itemConfigs;
        var factory = Ext.create('Manage.view.params.ComponentFactory');
        var items = new Array();
        
        if (itemConfigs !== undefined && itemConfigs !== null) {
            for (var i=0; i<itemConfigs.length; ++i) {
                var next = itemConfigs[i];
                var component = factory.createComponent(next);
                items.push(component);
            }
        }
        Ext.apply(this, {
            items : items
        });
        this.callParent();
    },
    
    markAsReadOnly: function(taskDetails){
    	var nvpairs=taskDetails.nvpairs;
    	var fields=this.getForm().getFields();
    	var field,value;
    	for(var i=0;i<fields.length;i++){
    		field = fields.get(i);
    		
    		value = nvpairs[field.getName()]; 
    		if( value !== undefined){
    			field.setValue(value);
    		}
    		if(field.setReadOnly){
    			field.setReadOnly(true);
    			
    		}
    		if(field.setDisabled){
    			field.setDisabled(true);
    		}
    	}
    }
    

    
});
