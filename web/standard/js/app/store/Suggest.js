/**
 * Store for suggestions allowed for a search
 */
Ext.define('Manage.store.Suggest', {
    extend: 'Ext.data.Store',
    model: 'Manage.model.Suggest',
    alias: 'store.suggest',
	proxy: 'memory',
	data: []
});
