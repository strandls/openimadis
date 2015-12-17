/**
 * View for displaying all the legends for a record
 */
Ext.define('Manage.view.imageview.Legends', {
	extend : 'Ext.panel.Panel',
	xtype : 'legends',
	alias : 'widget.legends',
    title : 'User Legends',
    layout : 'anchor',
	dockedItems : [ {
		xtype : 'toolbar',
		items : [
				{
					icon : "images/icons/add.png",
					tooltip : 'Choose Legends',
					handler : function() {
						Ext.ComponentQuery.query("legends")[0]
								.fireEvent("chooseColumns");
					}
				},
				{
					id : 'button_legends',
	                iconCls : 'bookmark-off',
	                enableToggle : 'true',
	                tooltip : 'Turn on Legends',
	                toggleHandler : function(btn, state) {
	                    if (state) {
	                        btn.setIconCls('bookmark-on'); 
	                    } else {
	                        btn.setIconCls('bookmark-off'); 
	                    }
	                }
				}
				]
	} ],
	items : [
		{
			xtype : 'panel',
			id : 'legendFields',
			collapsible:false,
			title : '<b>Fields<b>',
			autoScroll : true,
			anchor : "100% 50%",
			flex:1,
			border : false,
			hideHeaders : true,
			items: [{
						xtype : 'gridpanel',
						//collapsible:true,
						//selModel : Ext.create('Ext.selection.CheckboxModel'),
						store : 'Manage.store.LegendSelectedFieldStore',
						hideHeaders : true,
						columns : [
								{
									text : "Name",
									dataIndex : 'name',
									flex : 1
								}
						]
			}]
		},
		{
			xtype : 'panel',
			id : 'legendlocation',
			collapsible:false,
			title : '<b>Locations<b>',
			autoScroll : true,
			flex:1,
			border : false,
			items: [
				{
	            xtype: 'radiogroup',
	            id: 'legendLocationRadio',
	            flex: 1,
	            defaultType: 'radio', // each item will be a radio button
	            layout: 'anchor',
	            defaults: {
	                anchor: '100%',
	                hideEmptyLabel: false
	            },
	            items: [{
			                checked: true,
			                fieldLabel: 'Legend Location',
			                boxLabel: 'Top',
			                name: 'legend-location',
			                inputValue: 'top'
			            }, {
			                boxLabel: 'Bottom',
			                name: 'legend-location',
			                inputValue: 'bottom'
			            }]
        	}]
		}
	]
});
