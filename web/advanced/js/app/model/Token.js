/**
 * Model for auth code / access token.
 */
Ext.define('Manage.model.Token', {
    extend: 'Ext.data.Model',
    fields: ['id', 'client', 'services', 'creationTime', 'expiryTime', 'accessTime', 'filters']
});
