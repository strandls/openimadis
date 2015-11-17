/**
 * View for unit manipulation. This view will have the following,
 * 1. List of units and some properties of units in a table
 * 2. Ability to add a new unit 
 * 3. Ability to edit a unit's attributes
 */
Ext.define('Manage.view.admin.UnitList', {
	extend : 'Ext.grid.Panel',
	xtype : 'unitList',
	alias : 'widget.unitList',
	title : 'Teams',
	
	store: 'admin.Units',

	initComponent : function() {
		Ext.apply (this, {
			columns : [
				{header : 'Name', dataIndex : 'name', flex : 1},
				{header : 'Type', dataIndex : 'type', flex : 1},
				{header : 'Storage Quota (in GB)', dataIndex : 'globalStorage', flex : 1, renderer : Ext.util.Format.numberRenderer("0.0")},
				{header : 'Contact', dataIndex : 'email', flex : 1}
			],
			dockedItems : [{
				xtype : 'toolbar',
				items : [{
					icon : 'images/icons/add.png',
					text : 'Add',
					tooltip : 'Add Team',
					scope : this,
					handler : this.onAddClick
				}, ' ' , '-', ' ', {
					icon : 'images/icons/wrench.png',
					text : 'Edit',
					tooltip : 'Edit Team',
					scope : this,
					handler : this.onEditClick
				}, ' ', '-', ' ', {
					icon : 'images/icons/delete.png',
					text : 'Remove',
					tooltip : 'Remove Team',
					scope : this,
					handler : this.onRemoveClick
				}]
			}]
		});
		this.callParent();
	},

	/**
	* Handler called when add is clicked
	*/
	onAddClick : function() {
		console.log("on add");
		this.fireEvent('onAddUnit'); 
	},

	/**
	* Handler called when add is clicked
	*/
	onRemoveClick : function() {
		var selected = this.getSelectionModel().getSelection();
		if (selected && selected !== null && selected.length === 1) {
			this.fireEvent('onRemoveClick', selected[0]);
		} else {
			Ext.Msg.alert("Warning", "Select team to remove from the table");
		}
	},

	/**
	* Handler called when edit is clicked
	*/
	onEditClick : function() {
		var selected = this.getSelectionModel().getSelection();
		if (selected && selected !== null && selected.length === 1) {
			this.fireEvent('onEditUnit', selected[0]);
		} else {
			Ext.Msg.alert("Warning", "Select team to edit from the table");
		}
	}
});
