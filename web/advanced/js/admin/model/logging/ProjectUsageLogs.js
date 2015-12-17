/**
 * Model for project usage logs
 */
Ext.define('Admin.model.logging.ProjectUsageLogs', {
    extend: 'Ext.data.Model',
    fields: ['userLogin', 'projectName', 'accessTime' , "accessType"]
});
