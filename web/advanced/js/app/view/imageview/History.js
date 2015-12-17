/**
 * History view. Allows viewing all the history for a record
 */
Ext.define('Manage.view.imageview.History', {
	extend : 'Ext.panel.Panel',
	xtype : 'history',
	alias : 'widget.history',
	layout : 'anchor',
	dockedItems : [ {
		xtype : 'toolbar',
		items : [
				{
					icon : "images/icons/Filter.png",
					tooltip : 'Filter History',
					handler : function() {
						Ext.ComponentQuery.query("history")[0]
								.fireEvent("filterHistory");
					}
				},
				{
					icon : "js/extjs/resources/themes/images/default/grid/refresh.gif",
					tooltip : 'Refresh History List',
					handler : function() {
						this.up('history').updateHistory();
					}
				} ]
	},
	{
		xtype: 'pagingtoolbar',
        store: 'Manage.store.HistoryStore',
        dock: 'bottom',
        displayInfo: true
	}],
	items : [ {
		xtype : 'gridpanel',
		store : 'Manage.store.HistoryStore',
		autoScroll : true,
		anchor : "100% 100%",
		columns : [ {
			header : 'History',
			dataIndex : 'notes',
			sortable : false,
			flex : 1,
			renderer : function(value, metaData, record) {
				var html = '<div style="white-space:normal !important;">';
				html += '<h3>' + record.data.type + ' by '
						+ record.data.user + '</h3><br/>';
				html += '<p>' + record.data.description
						+ '</p><br/>';
				html += '<div class="commentTimeStamp"><p>'
						+ record.data.date + '</p></div>';
				html += '</div>';
				return html;
			}
		} ]

	} ],

	getHistoryView : function() {
		return this.items.items[0];
	},

	setRecordID : function(recordid) {
		this.recordid = recordid;
		console.log("set record id");
	},
	
	updateHistory : function(recordid, historytype, fromDate,
			toDate) {
		if (!recordid || recordid === null)
			recordid = this.recordid;
		else
			this.recordid = recordid;

		var store = this.getHistoryView().store;
		store.getProxy().extraParams = {
			recordid : recordid,
			'historytype' : historytype,
			'fromDate' : fromDate,
			'toDate' : toDate
		};
    	store.loadPage(1);
	}
});
