/**
 * Microscope manipulation related actions
 */
Ext.define ('Manage.controller.admin.Microscopes', {
	extend : 'Ext.app.Controller',

	refs : [{
		ref : 'microscopeList',
		selector : 'microscopeList'
	}, {
		ref: 'adminSpace',
		selector: '#adminSpace'
	}],

	init : function() {
		this.control({
			'microscopeList' : {
				onAddMicroscope : this.onAddMicroscope,
				onEditMicroscope : this.onEditMicroscope,
				onRemoveMicroscope : this.onRemoveMicroscope,
				selectionchange: this.onMicroscopeSelectionChange
			},
			'addMicroscope' : {
				refreshList : this.onRefreshList
			},
			'editMicroscope' : {
				refreshList : this.onRefreshList
			}
		});
	},

	/**
	 * adds the given Component to adminSpace
	 * @param {Ext.Component} comp the component to add
	 * @param {String} title the title to set the component
	 */
	addToAdminSpace: function(comp, title) {
		var ctr = this.getController('admin.Utils');
		ctr.addToAdminSpace(comp, title);
	},

	/**
	* Add new microscope ui
	*/
	onAddMicroscope : function() {
		var comp = Ext.create ('Manage.view.admin.AddMicroscope');
		this.addToAdminSpace(comp, "Add new microscope");
	},

	/**
	 * set the adminSpace on selectionchange
	 * @param {Ext.selection.Model} model the selection model
	 * @param {Ex.data.Model[]} selection the selected records
	 */
	onMicroscopeSelectionChange: function(model, selection) {
		var space = this.getAdminSpace();

		if(space.hidden) {
			return;
		}
	
		if(space.down('editMicroscope')) {
			this.getMicroscopeList().fireEvent('onEditMicroscope', selection[0]);
		}
	},


	/**
	* Edit microscope ui
	*/
	onEditMicroscope : function(microscope) {
		var comp = Ext.create ('Manage.view.admin.EditMicroscope', {
				microscope: microscope.data
		});
		this.addToAdminSpace(comp, "Edit Microscope");
	},

	/**
	* Remove microscope
	*/
	onRemoveMicroscope : function(microscope) {
		var me = this;
		Ext.Ajax.request( {
			method : 'GET',
			url : '../admin/deleteMicroscope',
			params : {
				'microscopeName' : microscope.data.microscopeName
			},
			success : function(result, response) {
				console.log("success on remove microscope");
				Ext.Msg.alert("Message", "Successfully removed microscope " + microscope.data.microscopeName);
				me.onRefreshList();
			},
			failure : function(result, response) {
				Ext.Msg.alert("Message", "Failed to remove microscope " + microscope.data.microscopeName);
			}
		});

	},

	/**
	* Refresh the microscope listing
	*/
	onRefreshList : function() {
		var microscopeList = this.getMicroscopeList();
		microscopeList.store.load();
	}
});
