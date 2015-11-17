/**
 * Store for user projects 
 */
Ext.define('Manage.store.admin.UserProjects', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.Membership',

    autoLoad : false,

    storeId: 'admin.UserProjects', // required to refer to the store elsewhere

    proxy : {
        type : 'ajax',
        url : '../admin/getMembers',
        reader : {
            type : 'json'
        }
    }
});
