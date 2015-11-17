/**
 * Model capturing all downloads
 */
Ext.define('Manage.model.admin.Downloads', {
    extend : 'Ext.data.Model',
    fields : ['name', 'format', 'validity', 'status', 'size', 'link', 'id']

});
