/**
 * Store for user annotations for a project
 */
Ext.define('Manage.store.admin.UserAnnotations', {
    extend : 'Ext.data.Store',
    model : 'Manage.model.admin.UserAnnotation',

    autoLoad : false,

    storeId: 'admin.UserAnnotations', // required to refer to the store elsewhere

    proxy : {
        type : 'ajax',
        url : '../admin/getUserAnnotations',
        reader : {
            type : 'json'
        }
    }
});
