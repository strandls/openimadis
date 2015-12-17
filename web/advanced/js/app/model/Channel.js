/**
 * Model for an channel
 */
Ext.define('Manage.model.Channel', {
    extend: 'Ext.data.Model',
    fields : ['channelNumber', 'name', 'colour'],
    idProperty : 'channelNumber'
});
