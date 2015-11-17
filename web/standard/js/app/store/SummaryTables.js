Ext.define('Manage.store.SummaryTables', {
	extend: 'Ext.data.Store',
	requires: 'Manage.model.SummaryTable',
	model: 'Manage.model.SummaryTable',

	autoLoad: true,

	proxy: {
		type: 'ajax',
		url: 'data/summary.json',
		reader: {
			type: 'json',
			root: 'results'
		}
	}
});
