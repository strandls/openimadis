/**
 * Model for record metadata. This shows the same fields as in Record.js
 * The difference is that this is a simple key, value table. The values
 * are dynamically populate based on the record selection via thumbnail/summary table
 * No default proxy used here as a result
 */
Ext.define('Manage.model.RecordMetaData', {
    extend: 'Ext.data.Model',
    fields : ['name', 'value']
});
