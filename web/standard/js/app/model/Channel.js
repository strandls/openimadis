/**
 * Model for a channel
 */
Ext.define('Manage.model.Channel', {
	extend: 'Ext.data.Model',
	fields: ['channelNumber', 'lut', 'name', 'color'],

	idProperty: 'channelNumber'
});

