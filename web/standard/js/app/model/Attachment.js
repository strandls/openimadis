/**
 * Model for an attachment
 * Used by system and user attachment stores
 */
Ext.define('Manage.model.Attachment', {
    extend: 'Ext.data.Model',
    fields: ['name', 'notes'],
    idProperty : 'name'
});
