/**
 * Model for a client
 */
Ext.define('Manage.model.Client', {
    extend: 'Ext.data.Model',
    fields: ['clientID', 'name', 'description', 'version', 'isWorkflow','text'],
    idProperty : 'clientID'
});
