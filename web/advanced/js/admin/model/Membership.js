/**
 * Model capturing a membership between project and an user
 */
Ext.define('Admin.model.Membership', {
    extend : 'Ext.data.Model',
    fields : ['user', 'projectName', 'role']
});
