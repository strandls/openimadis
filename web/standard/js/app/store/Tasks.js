/**/
Ext.define('Manage.store.Tasks', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Task',    
    model: 'Manage.model.Task',
    autoLoad: false,
    pageSize: 10,
    proxy: {
        type: 'ajax',
        url: '../compute/searchTasks',
        reader: {
            type: 'json',
            root: 'items',
            totalProperty: 'total'
        }
    }
});
