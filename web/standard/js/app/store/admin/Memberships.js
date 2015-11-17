/**
 * Store for memberships 
 */
Ext.define('Manage.store.admin.Memberships', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.Membership',

    autoLoad : false,

    storeId: 'admin.Memberships', // required to refer to the store elsewhere

    proxy : {
        type : 'ajax',
        url : '../admin/getMembers',
        reader : {
            type : 'json'
        }
    }
});
