/**
 * Model for record history
 */
Ext.define('Manage.model.History', {
    extend: 'Ext.data.Model',
    fields: ['type', 'user', 'description', 'date']
});
