Ext.define('Manage.store.SystemAttachmentStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Attachment',    
    model: 'Manage.model.Attachment',
    autoLoad : false,
    proxy: {
        type: 'ajax',
        url: '../project/getSystemAttachments',
        reader: 'json'
    }
});
