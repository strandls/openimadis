/**
 * Store for all the user annotations for a particular record
 */
Ext.define('Manage.store.UserAnnotationStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.RecordMetaData',    
    model: 'Manage.model.RecordMetaData',
    autoLoad : false,
    proxy : {
        type : 'ajax',
        url : '../annotation/getUserAnnotations',
        reader : 'json'
    }
});
