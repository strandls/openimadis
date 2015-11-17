/**
 * Model for history type
 */
Ext.define('Manage.model.HistoryType', {
    extend: 'Ext.data.Model',
    fields: ['name', 'value'],
    idProperty : 'value'
});
