/**
 * View for listing all the tokens managed by the user.
 * The user can add, edit or delete tokens from the toolbar
 * actions present in this view.
 */
Ext.require(['Manage.view.workflow.TokenList']);

Ext.define('Manage.view.workflow.ViewTokens', {
    extend : 'Manage.view.workflow.ComboSelection',
    require : 'Manage.view.workflow.ComboSelection',
    xtype : 'viewTokens',
    alias : 'widget.viewTokens',

    bodyPadding: 5,
    
    comboConf : {
       store : 'Manage.store.workflow.TokenUsers',
       displayField : 'name',
       valueField : 'name',
       queryMode : 'local',
       editable : false,
       allowBlank : false
    },
    
    comboListener : 'Manage.view.workflow.TokenList'
});
