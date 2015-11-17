/**
 * Panel for adding a bookmark
 */
Ext.define('Manage.view.AddFavouriteFolder', {
	extend: 'Ext.panel.Panel',
	xtype: 'addfavouritefolder',
	alias: 'widget.addfavouritefolder',

	height: 150,
	width: '100%',
	
    layout: {
    	type: 'hbox'
    },

    defaults: {
    	margin: '10px'
    },

	
	items: [{
		xtype: 'textfield',

		fieldLabel: 'Name',
		labelAlign: 'left',
	},{
		xtype: 'button',
		text: 'Add',
		handler: function() {
			var panel = this.up('addfavouritefolder');
			panel.fireEvent('addfavouritefolder', panel.down('textfield').getValue());
			panel.close();
		}
	}, {
		xtype: 'button',
		text: 'Cancel',
		handler: function() {
			this.up('panel').close();
		}
	}]
});
