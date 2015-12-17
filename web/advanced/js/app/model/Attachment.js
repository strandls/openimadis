/**
 * Model for an attachment
 */
Ext.define('Manage.model.Attachment', {
    extend: 'Ext.data.Model',
    fields: ['name', 'notes'],
    idProperty : 'name'
});
