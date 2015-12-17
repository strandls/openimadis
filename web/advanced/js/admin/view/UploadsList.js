/**
 * View for Uploads manipulation. This view will have the following,
 * List of uploads and some properties of clients in a table
 */
Ext.define('Admin.view.UploadsList', {
    extend : 'Ext.grid.Panel',
    xtype : 'uploadsList',
    alias : 'widget.uploadsList',
    store : 'Admin.store.UploadsStore',
    title : 'Uploads',
    dockedItems : [ {
		xtype : 'toolbar',
		items : [
				{
					icon : "js/extjs/resources/themes/images/default/grid/refresh.gif",
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
                {header : 'Status', dataIndex : 'status', flex : 1},
                {
                    
                	header: 'Disable',
                    align: 'center',
                    dataIndex: 'validity',
                    flex : 1,
            		xtype : 'componentcolumn',
            		renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
            			return {
            				xtype : 'panel',
            				border : false,
            				items : [
            				         {
            				         	xtype : 'button',
            							tooltip : 'Disable',
            							icon : 'images/icons/cancelled.png',
            							handler : function() {
            								var disableMessage = 'Disable upload ticket : ' + record.data.id + 'for user : ' + record.data.userLogin;
            								var ticketId = record.data.id ; 
            								Ext.Msg.alert("Disable",  disableMessage , function () {
            									//var url = "../admin/disableAuthCode?id="  + this.authCodeId ;
            									//window.location = url;
            									var authid = this.authCodeId ;
            									Ext.Ajax.request( {
            										method : 'GET',
            										url : '../admin/disableUploadTicket',
            										params : {
            											'id' : ticketId
            										}
            									});
            								} , this);
            							}
            				         }
            						]
            				
            			};
            		}
            	}
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
