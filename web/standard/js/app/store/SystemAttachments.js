/**
 * store for system attachments
 */
Ext.define('Manage.store.SystemAttachments', {
    extend: 'Ext.data.Store',
    model: 'Manage.model.Attachment',

    autoLoad : false,

    proxy: {
        type: 'ajax',
        url: '../project/getSystemAttachments',
        reader: 'json'
    }
});
