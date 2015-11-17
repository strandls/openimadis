/**
 * Model capturing a membership between project and an user
 */
Ext.define('Manage.model.admin.Membership', {
    extend : 'Ext.data.Model',
    fields : ['user', 'projectName', 'role']
});
