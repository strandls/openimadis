/**
 * Store for ranks of users
 */
Ext.define('Manage.store.admin.UserRanks', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.UserRank',
    storeId: 'admin.userRanks',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listUserRanks',
        reader : {
            type : 'json'
        }
    }
});
