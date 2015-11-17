/**
 * View for listing all the tokens managed by the user.
 * The user can add, edit or delete tokens from the toolbar
 * actions present in this view.
 */
Ext.require(['Manage.view.settings.TokenList']);

Ext.define('Manage.view.settings.ViewTokens', {
    extend : 'Manage.view.settings.ComboSelection',
    require : 'Manage.view.settings.ComboSelection',
    xtype : 'viewTokens',
    alias : 'widget.viewTokens',

    bodyPadding: 5,
    
    height : 500,
    
    comboConf : {
       store : 'TokenUsers',
       displayField : 'name',
       valueField : 'name',
       queryMode : 'local',
       editable : false,
       allowBlank : false
    },
    
    comboListener : 'Manage.view.settings.TokenList',
    
    comboView : 'Manage.view.settings.TokenContainer'
});
