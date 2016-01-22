/**
 * View for the attachments
 */
Ext.define('Manage.view.Attachments', {
	extend : 'Ext.panel.Panel',
	xtype : 'attachments',
	alias : 'widget.attachments',
	
	/**
	 * to show attachments and add attachments
	 */
	layout : {
		type: 'card'
	},
	
	/**
	 * value set on thumbnail selection change
	 */
	recordid: '',
	
	dockedItems : [{
		xtype : 'toolbar',
		items : [ {
			icon : "images/icons/add.png",
			tooltip : 'Add Attachment',
			handler : function() {
				var layout = this.up("attachments").getLayout();
				layout.setActiveItem(1);
				
			}
		}, {
			iconCls: "refresh",
			tooltip : 'Refresh Attachment List',
			handler : function() {
			var userStore = Ext.StoreMgr.get('UserAttachments');
								userStore.reload();
			}
		} ]
	}],
	
	items:[{
		xtype: 'panel',
		layout: 'anchor',
		items : [{
				xtype : 'gridpanel',
				id : 'userattachments',
				collapsible:false,
				title : '<b>User Attachments<b>',
				autoScroll : true,
				anchor : "100% 50%",
				flex:1,
				border : false,
				store : 'UserAttachments',
				hideHeaders : true,
				columns : [
						{
							header : 'Attachments',
							dataIndex : 'name',
							flex : 6,
							renderer : function(value, metaData,
									record, rowIndex, colIndex, store,
									view) {
								var recordid = view.up().up().up().recordid;
								if (record === null
										|| record.data === null
										|| record.data.name.length <= 0)
									return "";
								var html = '<b><a href="../project/downloadAttachment?recordid='
										+ recordid
										+ '&attachmentName='
										+ record.data.name
										+ '">'
										+ record.data.name
										+ '</a></b>';
								html += '<br/><div style="overflow: auto;"><p>' + record.data.notes
										+ '</p><div>';
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
													disabled : false, //TODO change this !showAdminLink,
													handler : function() 
													{
														view.up().up().up().deleteAttachment(record);
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
				store : 'SystemAttachments',
				hideHeaders : true,
				columns : [
						{
					header : 'Attachments',
					dataIndex : 'name',
					flex : 1,
					renderer : function(value, metaData, record, rowIndex, colIndex, store,
							view) {
						var recordid = view.up().up().up().recordid;
						if (record === null || record.data === null
								|| record.data.name.length <= 0)
							return "";
						var html = '<b><a href="../project/downloadAttachment?recordid='
								+ recordid + '&attachmentName=' + record.data.name + '">'
								+ record.data.name + '</a></b>';
						html += '<br/><p>' + record.data.notes + '</p>';
						return html;
					}
				} ]
			}
		]
	}, {
		xtype : 'form',
		height: 300,
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		items : [{
			xtype : 'filefield',
			name : 'attachmentFile',
			margin: 5,
			height: 50,
			allowBlank : false,
			listeners: {
				change: function(f,newValue) {
					var button=this.up().down('#AddAttachment');
					var form=this.up('form');
					
					var notes=form.getForm().findField('notes').getSubmitValue();
				    
					if(newValue!=""&&notes!=""){
					    button.enable();				    
				    }
				    else{
				    	button.disable();
				    }
				    
				}
			}
		},{
			xtype : 'textarea',
			fieldLabel : 'Notes',
			margin: 5,
			labelAlign: 'top',
			flex: 1,
			name : 'notes',
			allowBlank : false,
			listeners: {
				change: function(f,newValue) {
					var button=this.up().down('#AddAttachment');
					var form=this.up('form');
					
					var fileName=form.getForm().findField('attachmentFile').getSubmitValue();
				    
					if(newValue!=""&&fileName!=""){
					    button.enable();				    
				    }
				    else{
				    	button.disable();
				    }
				    
				}
			}
		}, {
			xtype : 'hidden',
			name : 'recordid'
		}],
		url : '../project/addRecordAttachments',
		buttons : [{
			itemId: 'AddAttachment',
			text : 'Add',
			disabled: true,
			handler : function() {
				// add attachment to the recor
				var view = this.up().up().up();
				var form = this.up('form').getForm();
				form.setValues({
					recordid: view.recordid
				});

				if (form.isValid()) {
					form.submit({
						success: function(form, action) {
							view.clearText();

							//TODO refresh attachments in a cleaner fashion 
							var userStore = Ext.StoreMgr.get('UserAttachments');
							userStore.reload();
						},
						failure: function(form, action) {
							view.clearText();
							showErrorMessage(action.response.responseText, "Failed to add attachment");
						}
					}); 
				}

				//change active item
				var layout = view.getLayout();
				layout.setActiveItem(0);
			}
		}, {
			text: 'Cancel',
			handler: function() {
				var view = this.up().up().up();
				var layout = view.getLayout();
				layout.setActiveItem(0);
			}
		}]
	}],

	/**
	 * clear the text field
	 */
	clearText: function() {
		var text = this.down('textarea');
		text.reset();
	},

	setRecordId : function(recordid) {
		this.recordid = recordid;
	},
	
	deleteAttachment : function(record) {
		var _this = this;
		console.log(record);
		Ext.Msg.confirm("Delete", "Are you sure you want to permanently delete the selected attachment?", function(id)  {
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
                        showErrorMessage(result.responseText, "Failed to delete the selected attachment");
                    }
                });
            }
		});
	}
});
