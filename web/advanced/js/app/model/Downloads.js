/**
 * Model for user downloads
 */
Ext.define('Manage.model.Downloads', {
    extend: 'Ext.data.Model',
    fields: ['name', 'input_guids', 'project', 'size', 'link', 'id', 'status', 'isMovie'],
    idProperty : 'creationDate'
});