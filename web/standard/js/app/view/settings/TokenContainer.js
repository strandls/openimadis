/**
 * View for listing all the tokens managed by the user.
 * The user can add, edit or delete tokens from the toolbar
 * actions present in this view.
 */
Ext.define('Manage.view.settings.TokenContainer', {
	extend : 'Ext.panel.Panel',
    xtype : 'tokenContainer',
    alias : 'widget.tokenContainer',
    
    width: 900,
    height: 600,
    
    layout: {
        type: 'hbox',
        pack: 'start',
        align: 'stretch'
    },
    
    listeners : {
        comboselected : function(newValue, oldValue) {
            _this.activeUser = newValue;
            var store = _this.getStore();
            store.load({
                params : {
                    user : newValue
                }
            });
        }
    },
    
    items : [{
        xtype : 'tokenList',
        flex: 2
    }, {
        xtype : 'panel',
        id : 'rightTokenPane',
        flex : 3
    }]
});
