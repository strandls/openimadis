/**
 * Record meta data table. Lists all the metadata
 * for the record as a simple key value pair
 * Driven by record selection via thumbnail/summary table
 */
Ext.define('Manage.view.imageview.RecordMetaData', {
    extend : 'Ext.grid.Panel',
    xtype : 'recordmetadata',
    alias : 'widget.recordmetadata',
    store : 'Manage.store.RecordMetaDataStore',
    headerPosition : 'bottom',
    title  : 'Record Metadata',
    columns: [
        {header: 'Record Field',  dataIndex: 'name', flex:1 },
        {header: 'Value', dataIndex: 'value',flex:1}
    ]
});
