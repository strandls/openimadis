/**
 * View for displaying all the user annotations for a record
 */
Ext.define('Manage.view.UserAnnotations', {
	extend : 'Ext.panel.Panel',
	xtype : 'userannotations',
	alias : 'widget.userannotations',
	
	dockedItems : [{
		xtype : 'toolbar',
		items : [{
			icon : "images/icons/add.png",
			tooltip : 'Add Annotation',
			handler : function() {
				this.up('userannotations').fireEvent('annotateSelected');				
			}
		}]
	}],

	layout: 'card',

	items:[{
		xtype: 'gridpanel',
		store: 'UserAnnotations',
		
		columns: [
			{ header: 'User Annotation', dataIndex: 'name', flex : 1,renderer: 'htmlEncode' },
			{ header: 'Value', dataIndex: 'value', flex : 1,renderer: 'htmlEncode' }
		],

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
