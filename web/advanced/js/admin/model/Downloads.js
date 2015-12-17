/**
 * Model capturing all downloads
 */
Ext.define('Admin.model.Downloads', {
    extend : 'Ext.data.Model',
    fields : ['id', 'name', 'format', 'validity', 'status', 'size','link']

});
