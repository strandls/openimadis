/**
 * Store for workflows tree. This store is loaded from
 * WorkflowStore
 */
Ext.define('Manage.store.WebApplicationsTree', {
    extend: 'Ext.data.TreeStore',
    model: 'Manage.model.WebApplication',
    autoLoad : false,
    proxy: {
        type: 'memory',
        reader: 'json'
    },
    root : {
        expanded : true,
        text : 'Root node'
    }
});
