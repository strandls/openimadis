/**
 * Model for a publisher
 */
Ext.define('Manage.model.Publisher', {
    extend: 'Ext.data.Model',
    fields: ['name', 'description', 'publisherCode'],
    idProperty : 'name'
});
