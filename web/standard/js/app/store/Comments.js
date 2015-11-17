/**
 * Store for user comments
 */
Ext.define('Manage.store.Comments', {
	extend: 'Ext.data.Store',
	model: 'Manage.model.Comment',
	autoLoad: false,
	pageSize: 10,
	proxy: {
		type: 'ajax',
		url: '../record/getComments',
		reader: {
            type: 'json',
            root: 'items',
            totalProperty: 'total'
        }
	}
});
