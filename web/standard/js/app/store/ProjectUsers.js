/**
 * Store for users
 */
Ext.define('Manage.store.ProjectUsers', {
    extend : 'Ext.data.Store',
    requires : 'Manage.model.ProjectUser',
    model : 'Manage.model.ProjectUser',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/getMembers',
        reader : {
            type : 'json'
        }
    }
});
