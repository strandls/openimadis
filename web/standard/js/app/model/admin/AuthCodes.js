/**
 * Model capturing all authCodes
 */
Ext.define('Manage.model.admin.AuthCodes', {
    extend : 'Ext.data.Model',
    fields : [ 'id' ,'userLogin' , 'client','creationTime', 'expiryTime', 'accessTime','validity' ]

});
