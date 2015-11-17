/**
 * The window shown when the Bookmarks button is clicked
 */
Ext.define('Manage.view.ImportWindow', {
	extend: 'Ext.window.Window',
	xtype: 'importwindow',
	alias: 'widget.importwindow',
	
	requires: [
	   		'Manage.view.Imports'
	],

	closable: true,
	closeAction: 'hide',
	modal: true,
	draggable: false,
	title: 'Imports Status',

	height: 500,
	width: 800,
	layout:  {
		type: 'hbox',
		align: 'stretch'
	},	

	items : [{
		xtype: 'imports',
		style: 'background-color:white',
		flex: 5,
		autoScroll: true
	}],
	
	initComponent : function() {
		var me = this;
		var config = {
			task : {
				run : function() {
					if(me.isVisible())
						me.down('imports').fireEvent('refresh');
				},
				interval :15000
			}
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		Ext.TaskManager.start(this.task);
		this.callParent();		
	}
});


