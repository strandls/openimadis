/**
 * Overlay control for zoom
 */
Ext.define('Manage.view.zoom.ZoomOverlays', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.zoomOverlays',

	initComponent : function () {
		var config = {
			selModel : Ext.create('Ext.selection.CheckboxModel'),
			store : 'Manage.store.OverlayStore',
			hideHeaders : true,
			columns : [
					{
						text : "Name",
						dataIndex : 'name',
						flex : 1
					},
					{
						text : "Edit",
						dataIndex : 'name',
						xtype : 'componentcolumn',
						width : 35,
						renderer : function(value, metaData, record, rowIndex,
								colIndex, store, view) {
							return {
								xtype : 'button',
								tooltip : 'Edit Overlay',
								icon : 'images/icons/va_edit.png',
								handler : function() {
									view.up().fireEvent('editOverlays',
											view.up());
								}
							};
						}
					},
					{
						text : 'Delete',
						dataIndex : 'name',
						xtype : 'componentcolumn',
						width : 35,
						renderer : function(value, metaData, record, rowIndex,
								colIndex, store, view) {
							return {
								xtype : 'button',
								tooltip : 'Delete Overlay',
								icon : 'images/icons/va_delete.png',
								handler : function() {
									view.up().fireEvent('deleteOverlays',
											view.up());
								}
							};
						}
					} ],
			title : 'Overlays',
			autoScroll : true,
			anchor : "100% 40%",
			border : false,
			dockedItems : [ {
				xtype : 'toolbar',
				items : [ {
					icon : "images/icons/va_add.png",
					tooltip : 'Add Overlay',
					handler : function() {
						var view = this.up().up();
						view.fireEvent('addOverlays', view);
					}
				} ]
			} ]
		};
		Ext.apply(this, Ext.apply(this.initialConfig, config));
		this.callParent();
	}
});