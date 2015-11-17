/**
 * Store for all the user annotations for a particular record
 */
Ext.define('Manage.store.UserAnnotations', {
    extend: 'Ext.data.Store',

    model: 'Manage.model.UserAnnotation',

    autoLoad : false,

    proxy : {
        type : 'ajax',
        url : '../annotation/getUserAnnotations',
        reader : 'json'
    }
});
