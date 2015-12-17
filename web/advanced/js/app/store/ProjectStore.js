/**
 * Store for all projects for a particular user
 */
Ext.define('Manage.store.ProjectStore', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.Project',
    autoLoad: true
});
