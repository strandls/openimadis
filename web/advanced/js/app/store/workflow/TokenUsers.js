/**
 * Store for users listed in the combobox for token/client
 * listing. If the logged in user is administrator, the store
 * will have all the users available. else only self.
 */
Ext.define('Manage.store.workflow.TokenUsers', {
    extend: 'Ext.data.Store',
    autoLoad : true,
    fields : [{
        name : 'name',
        type : 'string'
    }],
    proxy: {
        type: 'ajax',
        url: '../compute/listUsers',
        reader: 'json'
    }
});
