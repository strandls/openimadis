/**
 * Store for user annotations for a project
 */
Ext.define('Admin.store.UserAnnotationStore', {
    extend : 'Ext.data.Store',
    requires : 'Admin.model.UserAnnotation',
    model : 'Admin.model.UserAnnotation',
    autoLoad : false,
    proxy : {
        type : 'ajax',
        url : '../admin/getUserAnnotations',
        reader : {
            type : 'json'
        }
    }
});
