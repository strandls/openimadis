/**
 * Model capturing all uploads
 */
Ext.define('Manage.model.admin.Uploads', {
    extend : 'Ext.data.Model',
    fields : ['userLogin', 'projectName', 'requestTime', 'status']

});
