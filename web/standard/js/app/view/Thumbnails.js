/*
 * View of thumbnails
 */
Ext.define( 'Manage.view.Thumbnails',
	{
		extend : 'Ext.view.View',
		xtype : 'thumbnails',
		alias : 'widget.thumbnails',

		itemSelector : 'div.thumb-wrap',

		/**
		 * store is a Required field pass it as a config object on
		 * creation
		 */

		tpl : [
				'<tpl for=".">',
				'<div class="thumb-wrap" style="padding:0px; margin:2px;">',
				'<div class="thumb">',
				(!Ext.isIE6 ? '<img src="{imagesource}" style="width:50px;height:50px"/>'
						: '<div style="width:50px;height:50px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'{imagesource}&t={[new Date().getTime()]}\')"></div>'),
				'</div>', '</div>', '</tpl>' ],

		multiSelect : true,
		singleSelect : false,

		cls : 'x-image-view'

});