/**
 * Store for the record data shown in summary table
 */
Ext.define('Manage.store.summarytable.SummaryTableRecords', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Record',    
    model: 'Manage.model.Record',
    listeners: {
        'load' :  function(store,records,options) {
            if (records === null || records.length != 1)
                return;
            Ext.StoreManager.get('Manage.store.summarytable.SummaryTablePagingStore').proxy.data = records;
        }
    }
});
