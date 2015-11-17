/**
 * The view of all the controls of the image loaded
 */
Ext.define('Manage.view.ImageControls', {
	extend : 'Ext.panel.Panel',
	xtype : 'imagecontrols',
	alias : 'widget.imagecontrols',

	requires : [ 'Ext.form.field.Checkbox', 'Manage.view.Channels',
			'Manage.view.Overlays', 'Manage.view.Zoom' ],

	layout : {
		type : 'border'
	},

	items : [ {
		xtype : 'checkboxgroup',
		id : 'controlChecks',

		region : 'north',

		border : false,
//		height : 110,
		style : 'background-color:white',

		columns : 2,
		items : [ {
			boxLabel : 'All Channels',
			id : 'checkboxChannels'
		}, {
			boxLabel : 'Full Resolution',
			id : 'checkboxFullResolution'
		}, {
			boxLabel : 'Grey Scale',
			id : 'checkboxGreyScale'
		}, {
			boxLabel : 'Z Projection',
			id : 'checkboxZProjection'
		}, {
			boxLabel : 'Fit Width',
			id : 'checkboxFitWidth',
			checked : true
		}, {
			boxLabel : 'Legends',
			id : 'checkboxLegends',
			checked : false
		}, {
			boxLabel : 'Scalebar',
			id : 'checkboxScalebar',
			checked : false
		} ]
	}, {
		xtype : 'panel',
		id : 'controlArea',
		region : 'center',

		layout : {
			type : 'vbox',
			align : 'stretch'
		},

		items : [ {
			xtype : 'panel',
			id : 'innerControlArea',

			flex : 1,
			layout : 'card',

			border : false,

			items : [ {
				xtype : 'channels',
				title : 'Channels'
			}, {
				xtype : 'panel',
				id : 'sitecontrol',
				autoScroll : true,
				title : 'Sites'
			}, {
				xtype : 'overlays',
				title : 'Overlays'
			}, {
				xtype : 'zoom',
				title : 'Pan and Zoom'
			} ]
		} ]

	}, {
		xtype : 'panel',
		region : 'south',

		items : [ {
			xtype : 'panel',

			layout : {
				type : 'hbox',
				align : 'stretch'
			},

			items : [ {
				xtype : 'panel',

				flex : 1,
				layout : {
					type : 'vbox',
					align : 'stretch'
				},

				items : [ {
					xtype : 'button',
					id : 'buttonChannels',
					margin : 2,
					text : 'Channels'
				}, {
					xtype : 'button',
					id : 'buttonSites',
					margin : 2,
					text : 'Sites'
				} ]
			}, {
				xtype : 'panel',

				flex : 1,
				layout : {
					type : 'vbox',
					align : 'stretch'
				},

				items : [ {
					xtype : 'button',
					id : 'buttonOverlays',
					margin : 2,
					text : 'Overlay'
				}, {
					xtype : 'button',
					id : 'buttonZoom',
					margin : 2,
					text : 'Pan and Zoom'
				} ]
			} ]
		} ]
	} ]

});
