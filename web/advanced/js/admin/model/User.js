/**
 * Model capturing a user
 */
Ext.define('Admin.model.User', {
    extend : 'Ext.data.Model',
    fields : ['name', 'login', 'emailID', 'type', 'rank', 'status', 'rankString']
});
