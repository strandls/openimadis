/**
 * TODO update docs
 */
Ext.define('Manage.view.SelectionWindow', {
	extend: 'Ext.window.Window',
	xtype: 'selectionwindow',
	alias: 'widget.selectionwindow',

	closable: true,
	closeAction: 'hide',
	modal: true,
	draggable: false,
	title: 'Downloads',

	layout: {
		type: 'hbox',
		align: 'stretch'
	},

	height: 500,
	width: 1000,

	items: [{
		xtype: 'panel',
		id: 'formPanel',

		hideHeader: true,
		width: 300,
		split: true,

		layout: 'fit',

		items: [] //are dynamically added
	}, {  
		xtype:'tabpanel',
		id:'dynamicPanel',
		activeTab:0,

		flex: 1,
		split: true,

	    defaults: {
	        split: true
	    },
		items:[{
				title: 'Select Records',
				xtype:'selectionpanel'
		}
		]
	 }]

});

	
