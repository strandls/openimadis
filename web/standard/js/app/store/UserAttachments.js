/**
 * the store for user attachments
 */
Ext.define('Manage.store.UserAttachments', {
    extend: 'Ext.data.Store',
    model: 'Manage.model.Attachment',

    autoLoad : false,

    proxy: {
        type: 'ajax',
        url: '../project/getUserAttachments',
        reader: 'json'
    }
});
