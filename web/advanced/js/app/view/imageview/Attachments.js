Ext.define('Manage.view.imageview.Attachments', {
	extend : 'Ext.panel.Panel',
	xtype : 'attachments',
	alias : 'widget.attachments',
	
	layout : 'anchor',
	
	dockedItems : [ {
						xtype : 'toolbar',
						items : [ {
							icon : "images/icons/add.png",
							tooltip : 'Add Attachment',
							handler : function() {
								Ext.ComponentQuery.query("attachments")[0]
										.fireEvent("addattachment");
							}
						} ]
					} ],
	
	items : [
	         
		{
			xtype : 'gridpanel',
			id : 'userattachments',
			collapsible:false,
			title : '<b>User Attachments<b>',
			autoScroll : true,
			anchor : "100% 50%",
			flex:1,
			border : false,
			store : 'Manage.store.UserAttachmentStore',
			hideHeaders : true,
			columns : [
					{
						header : 'Attachments',
						dataIndex : 'name',
						flex : 6,
						renderer : function(value, metaData,
								record, rowIndex, colIndex, store,
								view) {
							var recordid = view.up().up().recordid;
							if (record === null
									|| record.data === null
									|| record.data.name.length <= 0)
								return "";
							var html = '<h3><a href="../project/downloadAttachment?recordid='
									+ recordid
									+ '&attachmentName='
									+ record.data.name
									+ '">'
									+ record.data.name
									+ '</a></h3>';
							html += '<br/><p>' + record.data.notes
									+ '</p><br/>';
							return html;
						}
					},
					{
						text : "Remove",
						dataIndex : 'name',
						xtype : 'componentcolumn',
						flex : 1,
						renderer : function(value, metaData, record, rowIndex, colIndex, store, view) 
						{
							return {
								xtype : 'panel',
								border : false,
								items : [
								         {
								        	 xtype : 'button',
												tooltip : 'Delete',
												icon : 'images/icons/delete.png',
												width : 25,
												disabled : !showAdminLink,
												handler : function() 
												{
													view.up().up().deleteAttachment(record);
												} 
								         }
								]
								
							};
						}
					}
			]
		},
		{
			xtype : 'gridpanel',
			id : 'systemattachments',
			collapsible:false,
			title : '<b>System Attachments<b>',
			autoScroll : true,
			anchor : "100% 50%",
			flex:1,
			border : false,
			store : 'Manage.store.SystemAttachmentStore',
			hideHeaders : true,
			columns : [
					{
				header : 'Attachments',
				dataIndex : 'name',
				flex : 1,
				renderer : function(value, metaData, record, rowIndex, colIndex, store,
						view) {
					var recordid = view.up().up().recordid;
					if (record === null || record.data === null
							|| record.data.name.length <= 0)
						return "";
					var html = '<h3><a href="../project/downloadAttachment?recordid='
							+ recordid + '&attachmentName=' + record.data.name + '">'
							+ record.data.name + '</a></h3>';
					html += '<br/><p>' + record.data.notes + '</p><br/>';
					return html;
				}
			} ]
		}
	],
	
	deleteAttachment : function(record) {
		var _this = this;
		console.log(record);
		Ext.Msg.confirm("Delete", "Are you sure you want to permanantly delete the selected attachment?", function(id)  {
			if (id === "yes") {
            	Ext.Ajax.request({
                    url : '../project/deleteAttachment',
                    method : 'get',
                    params : {
            			recordid : _this.recordid, 
        				name : record.data.name
                    },
                    success : function (result, request) {
                    	_this.down('[id=userattachments]').store.load({
                            params : {
                                recordid : _this.recordid
                            }
                        });
                    },
                    failure : function (result, request) {
                        showErrorMessage(action.result.responseText, "Failed to delete the selected attachment");
                    }
                });
            }
		});
	}
});
