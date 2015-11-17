/**
 * View for adding a new download
 */
Ext.define('Manage.view.dialogs.ExportDialog', {
    extend : 'Ext.form.Panel',
    xtype : 'exportDialog',
    alias : 'widget.exportDialog',
    
    bodyPadding: 5,

    // Fields will be arranged vertically, stretched to full width
    layout: 'anchor',
    defaults: {
        anchor: '100%'
    },
    
    // The fields
    defaultType: 'textfield',
    initComponent : function() {
        Ext.apply (this, {
        	items: [
        	        {
        	           fieldLabel: 'Name',
        	           labelAlign: 'top',
        	           name: 'name',
        	           id:'exportname',
        	           allowBlank: false,
        	           value: 'My Export'
        	        },
        	        {
        	           xtype : 'combobox',
        	           id : 'format',
        	           fieldLabel : 'Format',
        	           labelAlign: 'top',
        	           name : 'format',
        	           queryMode : 'local',
        	           displayField : 'name',
        	           valueField : 'value',
        	           allowBlank : false,
        	           editable : false,
        	           forceSelection:true,
        	           store : 'ExportFormats',
        	           value: 'ORIGINAL_FORMAT'
        	       },
        	       {
        	           xtype : 'datefield',
        	           name : 'validity',
        	           fieldLabel : 'Expiry Date',
        	           labelAlign: 'top',
        	           allowBlank : false,
        	           value : Ext.Date.add(new Date(),Ext.Date.DAY,1),
        	           showToday : false,
        	           minValue : Ext.Date.add(new Date(),Ext.Date.DAY,1),
        	           maxValue : Ext.Date.add(new Date(),Ext.Date.MONTH,1)
        	       }
        	]
        });
    	
        // Bug in extjs - see http://stackoverflow.com/questions/6795511/extjs-simple-form-ignores-formbind
        this.on('afterrender', function(me) {
            delete me.form._boundItems;
        });
        this.callParent();
    },

    // Reset and Submit buttons
    buttons: [{
        text : 'Cancel',
        handler : function() {
            var view = this.up('form');
	    view.fireEvent('close');
        }
    }, {
        text: 'Reset',
        handler: function() {
            this.up('form').getForm().reset();
        }
    }, {
        text: 'Submit',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler: function() {
            var view = this.up('form');
            var form = view.getForm();
            if (form.isValid()) {
		view.fireEvent('downloadselected', form);
		return;
            }
        }
    }]
});
