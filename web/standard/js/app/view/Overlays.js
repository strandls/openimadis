/**
 * Overlays for the image.
 */

Ext.define('Manage.view.Overlays', {
	extend : 'Ext.panel.Panel',
	xtype : 'overlays',
	alias : 'widget.overlays',

    layout : 'fit', 
	id : 'overlaycontrol',
	title : '<b>Overlays<b>',
	
	dockedItems : [ {
		xtype : 'toolbar',
		items : [ {
			icon : "images/icons/va_add.png",
			tooltip : 'Add Overlay',
			handler : function()
			{
				var view = this.up().up();
				view.fireEvent('addOverlays', view);
			}
		} ]
	} ],
	items : [{
		xtype : 'gridpanel',
		store : 'Overlays',
		id : 'checkboxgrid',
		autoScroll : true,
		hideHeaders: true,
		selModel : Ext.create('Ext.selection.CheckboxModel'),
		columns : [
				{
					text : "Name",
					dataIndex : 'name',
					flex : 8
				},
				{
					text : "Edit",
					dataIndex : 'name',
					xtype : 'componentcolumn',
					flex : 1,
					renderer : function(value, metaData, record, rowIndex, colIndex, store, view) 
					{
						return {
							xtype : 'panel',
							border : false,
							items : [{
								xtype : 'button',								
								tooltip : 'Edit Overlay',
								disabled : !(record.get('handCreated')),
								icon : 'images/icons/va_edit.png',
								width : 25,
								handler : function() {
									view.up().up().fireEvent('editOverlays', view.up().up(), record.get('name'));
								}
							}]								
						};
					}
				},
				{
					text : 'Delete',
					dataIndex : 'name',
					xtype : 'componentcolumn',
					flex : 1,
					renderer : function(value, metaData, record, rowIndex, colIndex, store, view) 
					{
						return {
							xtype : 'panel',
							border : false,
							items : [{
								xtype : 'button',								
								tooltip : 'Delete Overlay',
								disabled : !(record.get('handCreated')),
								icon : 'images/icons/va_delete.png',
								width : 25,
								handler : function() {
									console.log("delete pressed "+record.get('name'));
									view.up().up().fireEvent('deleteOverlays', view.up().up(), record.get('name'));
								}
							}]								
						};
					}
				},
				{
					text : 'Locations',
					dataIndex : 'name',
					xtype : 'componentcolumn',
					flex : 1,
					renderer : function(value, metaData, record, rowIndex, colIndex, store, view) 
					{
						return {
							xtype : 'panel',
							border : false,
							items : [{
								xtype : 'button',								
								tooltip : 'Overlay Locations',
								icon : 'images/icons/search.png',
								width : 25,
								handler : function() {
									console.log("locations pressed "+record.get('name'));
									view.up().up().fireEvent('locateOverlays', record.get('name'));
								}
							}]								
						};
					}
				}
			]
	 }]
});
