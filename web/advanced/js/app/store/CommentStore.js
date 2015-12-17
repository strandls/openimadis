/**
 * Store for comments for a particular record
 */
Ext.define('Manage.store.CommentStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Comment',    
    model: 'Manage.model.Comment',
    autoLoad : false,
    pageSize: 4,
    
    proxy: {
        type: 'ajax',
        url: '../record/getComments',
        reader: {
            type: 'json',
            root: 'items',
            totalProperty: 'total'
        }
    }
});
