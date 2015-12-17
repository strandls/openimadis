/**
 * Store for the record metadata table. This is a simple key-value table
 * of all metadata of the record. See model/RecordMetaData.js for more details. 
 */
Ext.define('Manage.store.RecordMetaDataStore', {
    extend: 'Ext.data.Store',
    model : 'Manage.model.RecordMetaData',
    autoLoad: true, // The data for this store is loaded via the RecordSelection controller
    data : [],
    proxy : {
        type : 'memory'
        /*reader : {
            type : 'json',
            root : 'metadata'
        }*/
    }
});
