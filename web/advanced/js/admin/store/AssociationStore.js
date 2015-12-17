/**
 * Store for memberships 
 */
Ext.define('Admin.store.AssociationStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.UnitMembership',
    model : 'Admin.model.UnitMembership',
    autoLoad : false,
    proxy : {
        type : 'ajax',
        url : '../admin/getAssociations',
        reader : {
            type : 'json'
        }
    }
});
