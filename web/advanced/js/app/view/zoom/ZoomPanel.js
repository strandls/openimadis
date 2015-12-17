/**
 * Zoom panel with Zoom canvas and zoom overlays
 */

Ext.require([
	'Manage.view.zoom.ZoomCanvas',
	'Manage.view.zoom.ZoomOverlays'
]);

Ext.define('Manage.view.zoom.ZoomPanel',{
	extend : 'Ext.panel.Panel',
	alias : 'widget.zoomPanel',
	
	initComponent:function(){
		var me=this;	
		var config= {
			height:'100%',
			width:'100%',
			border:false,
			layout:'border',
			items:[{
					xtype:'zoomCanvas',
					zoomId: me.zoomId,
					region:'center'
				}
				/*,{
					xtype:'zoomOverlays',
					region:'east',
					layout:'fit',
					width:200,
					height:'100%'
				}*/
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	}
});