/**
 * Model for a usage logs
 */
Ext.define('Admin.model.logging.UsageLogs', {
    extend: 'Ext.data.Model',
    fields: ['user', 'appName', 'appVersion', 'loginTime']
});
