/**
 * image metadata
 */
Ext.define('Manage.view.ImageMetaData', {
	extend:'Ext.grid.Panel',
	xtype: 'imagemetadata',
	alias: 'widget.imagemetadata',

	store: 'ImageMetaDatas',
    
	columns: [
		{ header: 'Image Field', dataIndex: 'field' }
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
				trackMouse: true,

				//select the individual cells for tooltip
				delegate: view.getCellSelector(), 

				listeners: {
					beforeshow: function(tip) {
						var text = tip.triggerElement.innerText;
						tip.update(text);
					}
				}
			});
		}
	}

});
