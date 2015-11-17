/**
 * View for Uploads manipulation. This view will have the following,
 * List of uploads and some properties of clients in a table
 */
Ext.define('Manage.view.admin.UploadsList', {
    extend : 'Ext.grid.Panel',
    xtype : 'uploadsList',
    alias : 'widget.uploadsList',
    store : 'admin.Uploads',
    title : 'Uploads',
    dockedItems : [ {
		xtype : 'toolbar',
		items : [
				{
					iconCls: "refresh",
					tooltip : 'Refresh History List',
					handler : function() {
						this.up('uploadsList').updateUploads();
					}
				} ]
	} ],

    initComponent : function() {
        Ext.apply (this, {
            columns : [
                {header : 'User Login', dataIndex : 'userLogin', flex : 1},
                {header : 'Project Name', dataIndex : 'projectName', flex : 1},
                {header : 'Request Time', dataIndex : 'requestTime', flex : 1},
                {header : 'Status', dataIndex : 'status', flex : 1}
            ]
        });
        this.callParent();
    },
    
    updateUploads : function() {
    	console.log("updating uploads");
    	var store = this.getHistoryView().store;
		store.load();
    }
});
