Ext.define('Manage.store.AttachmentStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Attachment',    
    model: 'Manage.model.Attachment',
    autoLoad : false,
    proxy: {
        type: 'ajax',
        url: '../project/getRecordAttachments',
        reader: 'json'
    }
});
