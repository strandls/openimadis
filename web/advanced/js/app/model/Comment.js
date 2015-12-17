/**
 * Model for an user comment
 */
Ext.define('Manage.model.Comment', {
    extend: 'Ext.data.Model',
    fields: ['name', 'comment', 'date'],
    idProperty : 'creationDate'
});
