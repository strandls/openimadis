/**
 * Model capturing a user
 */
Ext.define('Manage.model.admin.User', {
    extend : 'Ext.data.Model',
    fields : ['name', 'login', 'emailID', 'type', 'rank', 'status', 'rankString']
});
