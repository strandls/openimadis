/**
 * Store for results of a search
 */
Ext.define('Manage.store.SearchResults', {
	extend: 'Ext.data.TreeStore',

	model: 'Manage.model.SearchResult',

	autoLoad : false,

	data : [],

	proxy: {
		type : 'memory'
	}
});
