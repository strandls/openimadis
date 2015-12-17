Ext.define('Manage.store.ImageMetaDataList', {
    extend: 'Ext.data.Store',
    requires: 'Manage.model.ImageMetaData',    
    model: 'Manage.model.ImageMetaData',
    autoLoad : false,
    fields : ['field', '0', '1'],
    proxy : {
        type : 'ajax',
        url : '../project/getImageMetaData',
        reader : 'json'
    }
});
