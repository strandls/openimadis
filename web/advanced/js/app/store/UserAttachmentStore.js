Ext.define('Manage.store.UserAttachmentStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Attachment',    
    model: 'Manage.model.Attachment',
    autoLoad : false,
    proxy: {
        type: 'ajax',
        url: '../project/getUserAttachments',
        reader: 'json'
    }
});
