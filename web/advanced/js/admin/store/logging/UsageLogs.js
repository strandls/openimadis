/*Store to load login history*/
Ext.define('Admin.store.logging.UsageLogs', {
    extend: 'Ext.data.Store',
    requires: 'Admin.model.logging.UsageLogs',    
    model: 'Admin.model.logging.UsageLogs',
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
