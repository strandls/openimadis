/**
 * Record meta data table. Lists all the metadata
 * for the record as a simple key value pair
 * Driven by record selection via thumbnail/summary table
 */
Ext.define('Manage.view.RecordMetaData', {
	extend : 'Ext.grid.Panel',
	xtype : 'recordmetadata',
	alias : 'widget.recordmetadata',

	store : 'RecordMetaDatas',

	columns: [
		{header: 'Record Field',  dataIndex: 'name', flex:1 },
		{header: 'Value', dataIndex: 'value',flex:1}
	],

	viewConfig: {
		enableTextSelection: true
	},

	listeners: {
		afterrender: function() {
			//adding tooltip to the columns
			// see Ext.tip.ToolTip example
			var view = this.getView();
			var tip = Ext.create('Ext.tip.ToolTip', {
				target: view.el,

				//select the row as the tooltip
				delegate: view.itemSelector,

				trackMouse: true,
				listeners: {
					beforeshow: function(tip) {
						var text = view.getRecord(tip.triggerElement).get('value');
						tip.update(text);
					}
				}
			});
		}
	}
});
