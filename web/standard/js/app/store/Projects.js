/**
 * Store for all projects for a particular user
 */
Ext.define('Manage.store.Projects', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.Project',
    
    autoLoad: true,

    proxy: {
        type: 'ajax',
        url : '../project/list',
        reader: {
            type: 'json',
            root: 'items'
        }
    }
});
