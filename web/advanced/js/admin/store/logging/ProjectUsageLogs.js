/*Store to load login history*/
Ext.define('Admin.store.logging.ProjectUsageLogs', {
    extend: 'Ext.data.Store',
    requires: 'Admin.model.logging.ProjectUsageLogs',    
    model: 'Admin.model.logging.ProjectUsageLogs',
    autoLoad: true,
    pageSize: 20,
    proxy: {
        type: 'ajax',
        url: '../admin/getProjectLoginHistory',
        reader: {
            type: 'json',
            root: 'items',
            totalProperty: 'total'
        }
    }
});

