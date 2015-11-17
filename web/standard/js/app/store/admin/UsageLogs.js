/*Store to load login history*/
Ext.define('Manage.store.admin.UsageLogs', {
    extend: 'Ext.data.Store',

    model: 'Manage.model.admin.UsageLogs',

    storeId: 'admin.UsageLogs', // required to refer to the store elsewhere

    autoLoad: false,
    pageSize: 20,
    proxy: {
        type: 'ajax',
        url: '../admin/getLoginHistory',
        reader: {
            type: 'json',
            root: 'items',
            totalProperty: 'total'
        }
    }
});
