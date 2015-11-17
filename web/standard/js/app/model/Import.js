/**
 * Model for project imports
 */
Ext.define('Manage.model.Import', {
    extend: 'Ext.data.Model',
    fields: ['user','status','requestTime','location'],
});