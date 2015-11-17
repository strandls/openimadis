/**
 * Store for memberships 
 */
Ext.define('Manage.store.admin.Associations', {
    extend : 'Ext.data.Store',

    model : 'Manage.model.admin.UnitMembership',

    storeId: 'admin.Associations', // required to refer to the store elsewhere 

    autoLoad : false,
    proxy : {
        type : 'ajax',
        url : '../admin/getAssociations',
        reader : {
            type : 'json'
        }
    }
});
