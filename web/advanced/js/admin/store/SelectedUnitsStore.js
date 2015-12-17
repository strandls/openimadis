/**
 * Store for selected users
 */
Ext.define('Admin.store.SelectedUnitsStore', {
    extend : 'Ext.data.Store',
    fields : ['name', 'availableSpace'],
    autoLoad : false,
    data : [],
    proxy : {
        type : 'memory'
    }
});
