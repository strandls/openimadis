Ext.define('Manage.model.FieldValues', {
    extend: 'Ext.data.Model',
    fields: ['id', 'name', 'text', 'frequency','leaf'],
    
    proxy: {
        type: 'ajax',
        url: 'data/project.json',
        reader: {
            type: 'json',
            root: 'results'
        }
    }
});