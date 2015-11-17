/**
 * History view. Allows viewing all the history for a record
 */
Ext.define('Manage.view.History', {
	extend : 'Ext.panel.Panel',
	xtype : 'history',
	alias : 'widget.history',
	layout : 'card',
	dockedItems : [{
	xtype : 'toolbar',
	items : [{
		icon : "images/icons/Filter.png",
		tooltip : 'Filter History',
		handler : function() {
			var layout = this.up('history').getLayout();
			layout.setActiveItem(1);
		}
	}, {
		iconCls: "refresh",
		tooltip : 'Refresh History List',
		handler : function() {
		this.up('history').updateHistory();
		}
	} ]
	},{
		xtype: 'pagingtoolbar',
		store: 'Historys',
		dock: 'bottom',
		displayInfo: true
	} ],
	items : [ {
		xtype : 'gridpanel',
		store : 'Historys',
		autoScroll : true,
		anchor : "100% 100%",

		//enable text selection
		viewConfig: {
			enableTextSelection: true
		},

		columns : [ {
			dataIndex : 'notes',
			sortable : false,
			flex : 1,
			renderer : function(value, metaData, record) {
				var originalDiscription = record.data.description;
				originalDiscription = Ext.util.Format.htmlEncode(originalDiscription);
				originalDiscription = originalDiscription.replace(/\(/g," ( ");
				originalDiscription = originalDiscription.replace(/\)/g," ) ");
				var splitComment = originalDiscription.split(" ");
				var descriptionString = "";
				for(var i=0;i<splitComment.length-1;i++)
				{
					console.log("");
					if(splitComment[i].match("record"))
					{
						if(splitComment[i+1].match("[0-9]+"))
						{
							var guid = splitComment[i+1];
							splitComment[i+1] = '<a href="#" onclick="var comments = Ext.ComponentQuery.query(\'comments\')[0]; comments.recordClicked(\''+guid+'\'); return false;">' + splitComment[i+1] + '</a>';
						}
						
					}
					else if(splitComment[i].match("guid:"))
					{
						console.log(splitComment[i]);
						var guid = splitComment[i].split(":")[1];
						splitComment[i] = '<a href="#" onclick="var comments = Ext.ComponentQuery.query(\'comments\')[0]; comments.recordClicked(\''+guid+'\'); return false;">' + splitComment[i] + '</a>';
					}
					descriptionString = descriptionString + " "+ splitComment[i];
				}
				descriptionString = descriptionString + " "+ splitComment[i];// for last
				
				var html = '<div style="white-space:normal !important; font: 11px tahoma,arial,verdana,sans-serif;">';
				html += '<h4>' + record.data.type + ' by '
				+ record.data.user + '</h4>';
				html += '<p>' + descriptionString
				+ '</p>';
				html += '<div class="commentTimeStamp"><p>'
				+ record.data.date + '</p></div>';
				html += '</div>';
				return html;
			}
		} ]

	}, {
		xtype: 'panel',
		layout: 'fit',

		items: [{
			xtype : 'form',
			bodyPadding : 10,
			layout: 'anchor',
			items : [{
				fieldLabel : 'History Type',
				xtype : 'combo',
				labelAlign: 'top',
				name : 'historytype',
				queryMode : 'local',
				displayField : 'name',
				valueField : 'value',
				editable : true,
				anchor: '100%',
				typeAhead: true,
				store : 'HistoryTypes'
			},{
				fieldLabel : 'From date',
				name : 'fromDate',
				labelAlign: 'top',
				anchor: '100%',
				xtype : 'datefield'
			}, {
				fieldLabel : 'To date',
				name : 'toDate',
				labelAlign: 'top',
				anchor: '100%',
				xtype : 'datefield'
			}, {
				xtype : 'hidden',
				name : 'recordid'
			}],
			url : '../record/getHistory',
			buttons : [{
				text : 'Filter',
				success: function(form,action) {
					button.up('.window').close();
				},
				handler : function() {
					// filter history
					var view = this.up('history');
					var recordid = view.recordid;
					var form = this.up('form').getForm();
					if (form.isValid()) {
						var fromDateTime = null;
						if(form.getFieldValues()['fromDate']!==null)
							fromDateTime = form.getFieldValues()['fromDate'].getTime();

						var toDateTime = null;
						if(form.getFieldValues()['toDate']!==null)
							toDateTime = form.getFieldValues()['toDate'].getTime();
						view.updateHistory(recordid, form.getFieldValues()['historytype'], fromDateTime, toDateTime);
					}

					var layout = this.up('history').getLayout();
					layout.setActiveItem(0);
				}
			}, {
				text: 'Cancel',
				handler: function() {
					var layout = this.up('history').getLayout();
					layout.setActiveItem(0);
				}
			}]
		}]
	}],

	getHistoryView : function() {
		return this.items.items[0];
	},

	setRecordID : function(recordid) {
		this.recordid = recordid;
		var form = this.down('form').getForm();
		form.setValues({
			recordid: recordid
		});
		console.log("set record id");
	},

	updateHistory : function(recordid, historytype, fromDate, toDate) {
		if (!recordid || recordid === null)
			recordid = this.recordid;
		else
			this.recordid = recordid;

		showConsoleLog('History', 'updateHistory', recordid);

		var store = this.getHistoryView().store;
		console.log(store);
		store.getProxy().extraParams = {
			'recordid' : recordid,
			'historytype' : historytype,
			'fromDate' : fromDate,
			'toDate' : toDate
		};
		store.loadPage(1);
	}
});
