/**
 * Store for all the user fields of a project. This store
 * is loaded when the field editor is launched.
 */
Ext.define('Manage.store.summarytable.UserFields', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.Field',
    proxy : {
        type : 'ajax',
        url : '../annotation/getUserFields',
        reader : {
            type : 'json'
        }
    },
    autoLoad : false
});
