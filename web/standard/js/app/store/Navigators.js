Ext.define('Manage.store.Navigators', {
	extend: 'Ext.data.TreeStore',

	autoLoad: false,

	data: [],

	model: 'Manage.model.Navigator',

	proxy: {
		type: 'memory'
	}

});

