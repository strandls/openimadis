/**
 * Store downloads of user
 */
Ext.define('Manage.store.DownloadStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Downloads',
    model: 'Manage.model.Downloads',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../project/getDownloads',
        reader: 'json'
    }
});
