/**
 * Model capturing all uploads
 */
Ext.define('Admin.model.Uploads', {
    extend : 'Ext.data.Model',
    fields : ['id','userLogin', 'projectName', 'requestTime', 'status']

});
