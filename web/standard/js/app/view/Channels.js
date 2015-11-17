/**
 * Channels for the image.
 */

Ext.define('Manage.view.Channels', {
	extend : 'Ext.grid.Panel',
	xtype : 'channels',
	alias : 'widget.channels',

	store : 'Channels',

	border : false,
	hideHeaders : true,

	plugins : [ Ext.create( 'Ext.grid.plugin.CellEditing', {
				clicksToEdit : 2
	})],

	selModel : Ext.create( 'Ext.selection.CheckboxModel', {
				checkOnly : true
	}),

	listeners : {
		cellclick : function(view, c, col, record, rowel, row, e) {
			if (col == 3) {
				view.up().fireEvent( 'chooseChannelLUT', view.up(), row, record.get('name'));
			} else if (col == 2) {
				view.up().fireEvent( 'chooseContrast', view.up(), row, record.get('name'));
			} else if (col == 1) {
				view.up().fireEvent( 'changeChannelName', record, c);
			}
		},

		afterrender: function() {
			console.log('Channels <afterrender>');
		},

		scope: this
	},

	columns : [{
		text : "Name",
		dataIndex : 'name',
		flex : 1,
		renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
			var displayName = record.data.name + '';
			var wavelength = record.data.wavelength;
			if (wavelength > 0) {
				displayName = displayName + ' (' + wavelength + ')';
			} 
			metaData.tdAttr = 'data-qtip="' + displayName + '"';
			return  displayName;
		}
	}, {
		text : "Contrast",
		dataIndex : 'color',
		width : 30,
		renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
			return '<img src="images/icons/contrast.png"/>';
		}
	}, {
		text : 'Color',
		dataIndex : 'color',
		width : 100,
		renderer : function(value, metaData, record, rowIndex, colIndex, store, view) {
			var url = '../record/getLUTImage?lut=' + record.data.lut;
			return '<img src="' + url + '" width=88 height=11/>';
		}
	}]
});
