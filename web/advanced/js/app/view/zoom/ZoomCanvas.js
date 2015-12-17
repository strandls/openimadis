/**
 * Container for html div which renders zoom ui using PanoJS
 */
Ext.define('Manage.view.zoom.ZoomCanvas',{
	extend : 'Ext.panel.Panel',
	alias : 'widget.zoomCanvas',
	
	initComponent:function(){
		console.log('zoomId:'+this.zoomId);
		var me=this;
		var config= {
			border:false,	
			html:'<div id="zoomViewer_"'+' class="viewer" style="width: 100%; height: 100%;" ></div>'
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	}
});