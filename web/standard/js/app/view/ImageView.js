/**
 * The main image view. This shows the image and has an overlay 
 * div on which the overlay is drawn. The size of the image and 
 * the size of the overlay are always the same
 */


Ext.define('Manage.view.ImageView', {
	extend: 'Ext.panel.Panel',
	xtype: 'imageview',
	alias: 'widget.imageview',
	
	requires: [
		'Manage.view.ImagePanel',
		'Manage.view.ImageSliders'
	],

	layout: 'border',

	items: [{
		xtype: 'imagesliders',
		region: 'south'
	}, {
		xtype: 'imagepanel',
		region: 'center'
	}]
});

	
