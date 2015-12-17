/**
 * Store for memberships 
 */
Ext.define('Admin.store.MembershipStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.Membership',
    model : 'Admin.model.Membership',
    autoLoad : false,
    proxy : {
        type : 'ajax',
        url : '../admin/getMembers',
        reader : {
            type : 'json'
        }
    }
});
