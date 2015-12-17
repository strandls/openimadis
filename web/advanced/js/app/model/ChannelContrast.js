/**
 * Model for a channel constrast
 */
Ext.define('Manage.model.ChannelContrast', {
    extend: 'Ext.data.Model',
    fields: ['channelNumber', 'min', 'max', 'gamma'],
    idProperty : 'channelNumber'
});
