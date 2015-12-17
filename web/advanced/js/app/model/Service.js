/**
 * Model for a service exposed by engine
 */
Ext.define('Manage.model.Service', {
    extend: 'Ext.data.Model',
    fields: ['name', 'value'],
    idProperty : 'value'
});
