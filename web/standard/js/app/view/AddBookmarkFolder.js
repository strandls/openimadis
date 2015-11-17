/**
 * Panel for adding a bookmark
 */
Ext.define('Manage.view.AddBookmarkFolder', {
	extend: 'Ext.panel.Panel',
	xtype: 'addBookmarkFolder',
	alias: 'widget.addBookmarkFolder',

	height: 150,
	width: '100%',

	bbar: [{
		xtype: 'button',
		text: 'Add',
		handler: function() {
			var panel = this.up('addBookmarkFolder');
			panel.fireEvent('addbookmarkfolder', panel.down('textfield').getValue());
			panel.close();
		}
	}, {
		xtype: 'button',
		text: 'Cancel',
		handler: function() {
			this.up('panel').close();
		}
	}],
	
	items: [{
		xtype: 'textfield',

		fieldLabel: 'Name',
		labelAlign: 'top',

		margin: '10px'
	}]
});
