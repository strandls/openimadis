Ext.define('Manage.model.Field', {
    extend: 'Ext.data.Model',
    fields: ['name', 'type', 'isUserField'],
    idProperty : 'name', 
    proxy: {
        type: 'ajax',
        url: 'data/fields.json',
        reader: {
            type: 'json',
            root: 'results'
        }
    }
});
