/**
 * Comments view. Allows viewing all the comments for a record
 * and to add new comments.
 */
Ext.define('Manage.view.imageview.Comments', {
    extend : 'Ext.panel.Panel',
	xtype : 'comments',
	alias : 'widget.comments',
	layout : 'anchor',
	
	dockedItems: [{
        xtype: 'pagingtoolbar',
        store: 'Manage.store.CommentStore',
        dock: 'bottom',
        displayInfo: true
    }],
	
	items : [
			{
				xtype : 'form',
				anchor : "100% 20%",
				items : [ {
					xtype : 'textareafield',
					grow : true,
					name : 'comment',
					fieldLabel : 'Add',
					hideLabel : true,
					labelAlign : 'top',
					anchor : '100% 100%',
					maxLength : 255,
					//baseCls:'x-comment-TextArea'
					fieldCls : 'x-comment-TextArea',
					allowBlank: false,
					listeners:{
						'change':function(){
							if(this.isValid()){
								this.up('form').down('button').enable();
							}
							else{
								this.up('form').down('button').disable();
							}
						}
					}	
				}, {
					xtype : 'hidden',
					name : 'recordid',
					value : this.recordid
				} ],
				url : '../record/addComment',
				buttons : [ {
					text : 'Add Comment',
					formBind : true, //only enabled once the form is valid
					disabled : true,
					handler : function() {
						var form = this.up('form').getForm();
						var view = this.up().up().up();
						form.setValues( {
							recordid : view.recordid
						});
						if (form.isValid()) {
							form
									.submit( {
										success : function(
												form, action) {
											form.reset();
											// reload comments
											view
													.updateComments();
										},
										failure : function(
												form, action) {
											showErrorMessage(
													action.response.responseText,
													"Failed to add comment");
										}
									});
						}

					}
				} ]
			},
			{
				xtype : 'gridpanel',
				store : 'Manage.store.CommentStore',
				autoScroll : true,
				anchor : "100% 80%",
				columns : [ {
					header : 'Comments',
					dataIndex : 'notes',
					sortable : false,
					flex : 1,
					renderer : function(value, metaData, record) {
						//var commentString = '<a href="#" onclick="var comments = Ext.ComponentQuery.query(\'comments\')[0]; comments.recordClicked(); return false;">' + record.data.comment + '</a>'
						var originalComment = record.data.comment;
						var splitComment = originalComment.split(" ");
						var commentString = "";
						for(var i=0;i<splitComment.length;i++)
						{
							if(splitComment[i].match("guid:"))
							{
								console.log(splitComment[i]);
								var guid = splitComment[i].split(":")[1];
								splitComment[i] = '<a href="#" onclick="var comments = Ext.ComponentQuery.query(\'comments\')[0]; comments.recordClicked(\''+guid+'\'); return false;">' + splitComment[i] + '</a>';
							}
							commentString = commentString + " "+ splitComment[i];
						}
						var html = '<div style="white-space:normal !important;">';
						html += '<h3>' + record.data.name
								+ '</h3><br/>';
						html += '<p>'+commentString+'</p><br/>';
						html += '<div class="commentTimeStamp"><p>'
								+ record.data.date
								+ '</p></div>';
						html += '</div>';
						return html;
					}
				} ]

			} ],
			
	recordClicked : function (guid) {
		this.fireEvent("recordLinkClicked", guid);
	},

	getCommentsView : function() {
		return this.items.items[1];
	},

	setRecordID : function(recordid) {
		this.recordid = recordid;
		console.log("set record id");
	},

	updateComments : function(recordid) {
		if (!recordid || recordid === null)
			recordid = this.recordid;
		else
			this.recordid = recordid;
		var store = this.getCommentsView().store;
		store.getProxy().extraParams = {
			recordid : recordid
		};
		store.loadPage(1);
	}
});
