/**
 * Store for workflows tree. This store is loaded from
 * WorkflowStore
 */
Ext.define('Manage.store.WorkflowTreeStore', {
    extend: 'Ext.data.TreeStore',
    model: 'Manage.model.Workflow',
    autoLoad : false,
    proxy: {
        type: 'memory',
        reader: 'json'
    },
    root : {
        expanded : false,
        text : 'Root node'
    }
});
