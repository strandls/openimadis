/**
 * Model for a usage logs
 */
Ext.define('Manage.model.admin.UsageLogs', {
    extend: 'Ext.data.Model',
    fields: ['user', 'appName', 'appVersion', 'loginTime']
});
