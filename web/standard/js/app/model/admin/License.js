/**
 * Model capturing all authCodes
 */
Ext.define('Manage.model.admin.License', {
    extend : 'Ext.data.Model',
    fields : [ 'user', 'creationTime', 'macAddress', 'ipAddress', 'accessToken' ]

});
