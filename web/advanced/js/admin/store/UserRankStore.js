/**
 * Store for ranks of users
 */
Ext.define('Admin.store.UserRankStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.UserRank',
    model : 'Admin.model.UserRank',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../admin/listUserRanks',
        reader : {
            type : 'json'
        }
    }
});
