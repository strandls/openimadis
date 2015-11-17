/**
 * Store for Profile Length Unit
 */
Ext.define('Manage.store.admin.ProfileTypes', {
	extend : 'Ext.data.Store',
	
	model : 'Manage.model.admin.ProfileType',

	autoLoad : true,
	proxy : {
		type : 'ajax',
		url : '../admin/listAcqProfileTypes',
		reader : {
			type : 'json'
		}
	}
});

