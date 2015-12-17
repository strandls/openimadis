Ext.define('Manage.view.SearchBar', {
	extend:'Ext.form.Panel',
	xtype:'searchbar',
	alias:'widget.searchbar',
	initComponent:function() {
	
	  var config = {
		layout: 'hbox',
		height: 20,
		margin: 5,
		items:[
			{
				xtype: 'displayfield',
           			value: 'Showing 1-1 of 1 records',
				flex: 10
			},
			{
				xtype: 'textfield',
				flex: 5
			},{
				xtype: 'button',
				text: 'Search',
				flex: 3
			},{xtype: 'tbseparator',flex: 1},
			{
				xtype: 'button',
				iconCls: 'leftarrow'
			},{
				xtype: 'textfield',
				value: 1,
				flex: 1
			},{
				xtype: 'displayfield',
           			value: 'of 1',
				flex: 1
			},{
				xtype: 'button',
				iconCls: 'rightarrow'
			},{
				xtype: 'displayfield',
           			value: ' Page Size: ',
				flex: 2
			},
			{
				xtype: 'textfield',
				value: 1000,
				flex: 2
			}
		]
	  };
	  
	  Ext.apply(this, Ext.apply(this.initialConfig, config));
	  Manage.view.SearchBar.superclass.initComponent.apply(this, arguments);

	}

	
});
