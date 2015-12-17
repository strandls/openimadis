/**
 * List of types that a user added annotation can have
 */
Ext.define('Manage.store.summarytable.UserFieldTypes', {
    extend: 'Ext.data.Store',
    proxy : 'memory',
    fields: ['name', 'id'],
    data : [
        {"name" : "Number", "id": "Real"},
        {"name" : "Text", "id": "Text"},
        {"name" : "Date", "id": "Time"}
    ]
});
