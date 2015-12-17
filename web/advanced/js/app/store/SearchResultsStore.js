/**
 * Store for results of a search
 */
Ext.define('Manage.store.SearchResultsStore', {
    extend: 'Ext.data.TreeStore',
    autoLoad : false,
    data : [],
    proxy: {
        type : 'memory'
    }
});
