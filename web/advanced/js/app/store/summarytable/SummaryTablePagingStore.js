/**
 * Store for the record data shown in summary table with 
 * pagination support
 */
Ext.define('Manage.store.summarytable.SummaryTablePagingStore', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.Record',
    model: 'Manage.model.Record',
    autoLoad: {start: 0, limit: 20},
    proxy : {
        type : 'pagingmemory'
    },
    pageSize : 20,
    remoteSort : true,
    data : [],
    listeners: {
        'load' :  function(store, records, options) {
            if (records === null || records.length <= 0)
                return;
            var thumbnailData = new Array();
            for (var i=0; i < records.length; ++i){
                var guid = records[i].data['Record ID'];
                var next = {
                    id : guid,
                    imagesource : '../project/getThumbnail?recordid=' + guid
                };
                thumbnailData.push(next);
            }
            Ext.StoreManager.get('Manage.store.ThumbnailRecords').loadData(thumbnailData);
        }
    }
});
