/**
 * Model capturing all authCodes
 */
Ext.define('Admin.model.License', {
    extend : 'Ext.data.Model',
    fields : [ 'user', 'creationTime', 'macAddress', 'ipAddress', 'accessToken' ]

});
