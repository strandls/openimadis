/**
 * The main image view. This shows the image and has an overlay
 * div on which overlay is drawn. The size of the image and the size 
 * of the overlay div are always the same 
 * */

Ext.define('Manage.view.imageview.ImageView', {
	extend:'Ext.panel.Panel',
	alias:'widget.imageview',
	requires: [
		'Manage.view.imageview.ImageProperties',
		'Manage.view.imageview.ImageSliders',
		'Manage.view.imageview.ImagePanel'
	],
	
	initComponent:function() {
		var config={
			layout:'border',
			border:false,
			items:[
				{
					xtype:'imageProperties',
					region:'east'
				},
				{
					xtype:'panel',
					region:'center', 
					layout:'border',
					border:false,
					items:[
						{
							xtype:'imagesliders',
							region:'south'
						},
						{
							xtype:'imagePanel',
							region:'center'
						}
					]
				}
			]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	}
});