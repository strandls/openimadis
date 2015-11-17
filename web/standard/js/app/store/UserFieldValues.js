/**
 * Store for all values for a user field of a project. This store
 * is loaded when the field is choosen .
 */
Ext.define('Manage.store.UserFieldValues', {
    extend: 'Ext.data.Store',
    fields: ['value'],
    proxy : {
        type : 'ajax',
        url : '../annotation/getFieldValues',
        reader : {
            type : 'json'
        }
    },
    autoLoad : true
});


