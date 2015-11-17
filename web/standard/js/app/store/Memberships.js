/**
 * Store for memberships 
 */
Ext.define('Manage.store.Memberships', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.Membership',

    autoLoad : true,

    proxy : {
        type : 'ajax',
        url : '../admin/getMembers',
        reader : {
            type : 'json'
        }
    }
});

