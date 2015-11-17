/**
 * Model capturing a client 
 */
Ext.define('Manage.model.admin.Client', {
    extend : 'Ext.data.Model',
    fields : ['name', 'version', 'description', 'clientId','url']
});
