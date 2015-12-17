/**
 * Model capturing all authCodes
 */
Ext.define('Admin.model.AuthCodes', {
    extend : 'Ext.data.Model',
    fields : [ 'id' ,'userLogin' , 'client','creationTime', 'expiryTime', 'accessTime','validity' ]

});
