Ext.define('Manage.view.SelectionPanel',{
		extend : 'Ext.panel.Panel',
		xtype : 'selectionpanel',
		alias : 'widget.selectionpanel',
		
		id: 'selectPanel',		
		
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		
		items: [{
			xtype: 'panel',
			flex: 1,
			region: 'north',
			autoScroll: true,
			split: true,
			items:[{
				xtype: 'thumbnails',
				id: 'selectionThumbnails',
				store: 'SelectionRecords',

				flex: 1,
				style: 'background-color: white;',
				
				autoScroll: true,
				split: true
			}]
		}, {
			xtype: 'grid',
			id: 'summaryGrid',
			store: 'SelectionRecords',
			region: 'center',
			autoScroll: true,
			forceFit: false,
			height: 200,
			split: true,
			flex: 1,
			
			viewConfig: {
				enableTextSelection: true
			},

			selType: 'checkboxmodel',

			columns: [], //dynamically added based on project selection

			listeners: {
				afterrender: function() {
					//adding tooltip to the columns
					// see Ext.tip.ToolTip example
					var view = this.getView();
					var tip = Ext.create('Ext.tip.ToolTip', {
						target: view.el,
						trackMouse: true,

						//select the individual cells for tooltip
						delegate: view.getCellSelector(), 

						listeners: {
							beforeshow: function(tip) {
								var text = tip.triggerElement.innerText;
								tip.update(text);
							}
						}
					});
				}
			}
		}]
});