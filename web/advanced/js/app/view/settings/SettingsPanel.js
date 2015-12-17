/**
 * Panel which contains all the 'settings' views
 * All the logically related settings are grouped
 * together in a tabbed view
 */
Ext.require([
    'Manage.view.settings.FieldChooser'
]);

Ext.define('Manage.view.settings.SettingsPanel', {
    extend : 'Ext.tab.Panel',
    xtype : 'settingsPanel',
    alias : 'widget.settingsPanel',
    border : false,
    preventHeader : true,
    layout : 'fit',
    items : [{
        title : 'Navigator',
        xtype : 'fieldChooser'
    }] 
});
