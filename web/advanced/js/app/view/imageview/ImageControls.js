Ext.define('Manage.view.imageview.ImageControls', {
extend : 'Ext.panel.Panel',
xtype : 'imagecontrols',
alias : 'widget.imagecontrols',
requires: [
   		'Manage.view.imageview.ImageContrast',
   		'Manage.view.imageview.ZoomThumbnail'
   	],
initComponent : function() {
	var config = {
		dockedItems : [ {
			dock : 'top',
			xtype : 'imagetoolbar',
			height : 30
		} ],
		layout : 'border',
		autoScroll : false,
		
		items:[
			{
				xtype:'zoomThumbnail',
				region:'north',
				width:"100%",
				height:128,
				hidden:true
			},
			{
				xtype:'panel',
				region:'center',
				width:'100%',
				flex:1,
				layout : 'anchor',
				items : [
						{
							xtype : 'gridpanel',
							id : 'channelcontrol',
							//collapsible:true,
							title : '<b>Channels<b>',
							autoScroll : true,
							anchor : "100% 30%",
							flex:1,
							border : false,
							selModel : Ext.create(
									'Ext.selection.CheckboxModel',
									{
										checkOnly : true
									}),
							plugins : [ Ext.create(
									'Ext.grid.plugin.CellEditing',
									{
										clicksToEdit : 2
									}) ],
							store : 'Manage.store.ChannelStore',
							hideHeaders : true,
							listeners : {
								cellclick : function(view, c, col,
										record, rowel, row, e) {
									if (col == 3)
										view.up().fireEvent(
												'chooseChannelLUT',
												view.up(), row, c,
												e);
									if (col == 2)
										view.up().fireEvent(
												'chooseContrast',
												view.up(), row, c,
												e);
									if (col == 1) {
										view
												.up()
												.fireEvent(
														'changeChannelName',
														record, c);
									}
								}
							},
							columns : [
									{
										text : "Name",
										dataIndex : 'name',
										flex : 1,
										renderer : function(value,
												metaData, record,
												rowIndex, colIndex,
												store, view) {
											var displayName = record.data.name
													+ '';
											var wavelength = record.data.wavelength;
											if (wavelength > 0)
												displayName = displayName
														+ ' ('
														+ wavelength
														+ ')';
											metaData.tdAttr = 'data-qtip="'
													+ displayName
													+ '"';
											return '<a href="#">'
													+ displayName
													+ '</a';
										}
									},
									{
										text : "Contrast",
										dataIndex : 'colour',
										width : 30,
										renderer : function(value,
												metaData, record,
												rowIndex, colIndex,
												store, view) {
											return '<img src="images/icons/contrast.png"/>';
										}
									},
									{
										text : 'Color',
										dataIndex : 'colour',
										width : 100,
										renderer : function(value,
												metaData, record,
												rowIndex, colIndex,
												store, view) {
											var url = '../record/getLUTImage?lut='
													+ record.data.lut;
											return '<img src="'
													+ url
													+ '" width=88 height=11/>';
										}
									} ]
						},
						{
							xtype : 'panel',
							id : 'sitecontrol',
							title : '<b>Sites<b>',
							//collapsible:true,
							autoScroll : true,
							anchor : "100% 30%",
							flex:1
						},
						{
							xtype : 'gridpanel',
							//collapsible:true,
							selModel : Ext
									.create('Ext.selection.CheckboxModel'),
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
										renderer : function(value,
												metaData, record,
												rowIndex, colIndex,
												store, view) {
											return {
												xtype : 'button',
												tooltip : 'Edit Overlay',
												disabled : !(record
														.get('handCreated')),
												icon : 'images/icons/va_edit.png',
												handler : function() {
													view
															.up()
															.fireEvent(
																	'editOverlays',
																	view
																			.up());
												}
											};
										}
									},
									{
										text : 'Delete',
										dataIndex : 'name',
										xtype : 'componentcolumn',
										width : 35,
										renderer : function(value,
												metaData, record,
												rowIndex, colIndex,
												store, view) {
											return {
												xtype : 'button',
												tooltip : 'Delete Overlay',
												disabled : !(record
														.get('handCreated')),
												icon : 'images/icons/va_delete.png',
												handler : function() {
													view
															.up()
															.fireEvent(
																	'deleteOverlays',
																	view
																			.up());
												}
											};
										}
									} ],
							id : 'overlaycontrol',
							title : '<b>Overlays<b>',
							autoScroll : true,
							anchor : "100% 40%",
							flex:1,
							border : false,
							dockedItems : [ {
								xtype : 'toolbar',
								items : [ {
									icon : "images/icons/va_add.png",
									tooltip : 'Add Overlay',
									handler : function() {
										var view = this.up().up();
										view
												.fireEvent(
														'addOverlays',
																			view);
														}
													} ]
												} ]
											} ]
			}

		]
		
	};
	Ext.apply(this, Ext.apply(this.initialConfig, config));
	this.callParent();
}
});
