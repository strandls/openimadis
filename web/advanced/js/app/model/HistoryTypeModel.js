/**
 * Model for history type
 */
Ext.define('Manage.model.HistoryTypeModel', {
    extend: 'Ext.data.Model',
    fields: ['name', 'value'],
    idProperty : 'value'
});
