Ext.define('Manage.view.OverlayLocation', {
	extend : 'Ext.panel.Panel',
	xtype : 'overlaylocation',
	alias : 'widget.overlaylocation',
	
	layout:'fit',
	 
	items:[{
		xtype:'gridpanel',
		store:'OverlayLocation',
		autoScroll : true,
		columns : [
					{
						text : "Frame",
						dataIndex : 'frameNo',
						flex : 1
					},{
						text : "Slice",
						dataIndex : 'sliceNo',
						flex : 1
					}
				]
	}]

});