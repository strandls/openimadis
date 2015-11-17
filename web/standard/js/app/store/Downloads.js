/**
 * Store downloads of user
 */
Ext.define('Manage.store.Downloads', {
    extend: 'Ext.data.Store',
    model: 'Manage.model.Download',
    autoLoad : true,
    proxy: {
        type: 'ajax',
        url: '../project/getDownloads',
        reader: 'json'
    }
});
