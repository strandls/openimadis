/**
 * View for Downloads manipulation. This view will have the following,
 * List of downloads and some properties of clients in a table
 */
Ext.define('Manage.view.admin.DownloadsList', {
    extend : 'Ext.grid.Panel',
    xtype : 'downloadsList',
    alias : 'widget.downloadsList',
    store : 'admin.Downloads',
    title : 'Downloads',

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'Name', dataIndex : 'name', flex : 1},
                {header : 'Format', dataIndex : 'format', flex : 1},
                {header : 'Size', dataIndex : 'size', flex : 1},
                {header : 'Validity', dataIndex : 'validity', flex : 1},
                {header : 'Status', dataIndex : 'status', flex : 1}
            ]
        });
        this.callParent();
    }
});
