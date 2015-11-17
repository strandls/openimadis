Ext.define('Manage.store.TileViewerStore', {
    extend : 'Ext.data.Store',
    requires : 'Manage.model.TileViewer',
    model : 'Manage.model.TileViewer',
    autoLoad : true,
    proxy : {
        type : 'ajax',
        url : '../record/getTileStatus',
        reader : {
            type : 'json'
        }
    }
});