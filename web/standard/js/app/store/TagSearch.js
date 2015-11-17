/**
 * Store for workflows tree. This store is loaded from
 * WorkflowStore
 */
Ext.define('Manage.store.TagSearch', {
    extend: 'Ext.data.Store',
    model: 'Manage.model.Workflow',
    autoLoad: true,

    proxy: {
        type: 'ajax',
        url : '../compute/getAllClientsForTag',
        reader: {
            type: 'json'
        }
    }
});
