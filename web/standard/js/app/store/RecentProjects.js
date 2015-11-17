/**
 * Store recent projects used by a user. This will usually
 * be a small set, say the recent 10 projects
 */
Ext.define('Manage.store.RecentProjects', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.Project',

    autoLoad: true,

    proxy: {
        type: 'ajax',
        url : '../project/recentList', // TODO: recentlist
        reader: {
            type: 'json',
            root: 'items'
        }
    }
});
