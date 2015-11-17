/**
 * Handles all functionality related to the Team
 *  - add, edit, remove teams
 *  - add, edit, remove, search Team-Project associations
 */
Ext.define('Manage.controller.admin.Units', {
	extend: 'Ext.app.Controller',

	requires: [
		'Manage.view.admin.AddUnit', 'Manage.view.admin.EditUnit'
	],
		
	refs: [{
		ref: 'unitList',
		selector: 'unitList'
	}, {
		ref: 'projectList',
		selector: 'projectList'
	}, {
		ref: 'adminSpace',
		selector: '#adminSpace'
	}, {
		ref : 'associationList',
		selector : 'associationList'
	}, {
		ref : 'association',
		selector : 'association'
	}],

	init: function() {
		this.control({
			'unitList': {
				onAddUnit: this.onAddUnit,
				onEditUnit: this.onEditUnit,
				onRemoveClick: this.onRemoveUnit,
				selectionchange: this.onUnitSelectionChange
			}, 
			'addUnit': {
				refreshList: this.onRefreshList
			},
			'editUnit': {
				refreshList: this.onRefreshList
			},
			'associationList': {
				onAddAssociation: this.onAddAssociation, 
				onEditAssociation: this.onEditAssociation,
				onRemoveAssociation: this.onRemoveAssociation,
				searchUnit: this.searchUnit,
				selectionchange: this.onAssociationSelectionChange
			},
			'addTeamProjectAssociations': {
				onRefreshAssociationList: this.onRefreshAssociationList
			},
			'editAssociation': {
				onRefreshAssociationList: this.onRefreshAssociationList
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
	* Add new unit ui
	*/
	onAddUnit: function() {
		var comp = Ext.create('Manage.view.admin.AddUnit');
		this.addToAdminSpace(comp, "Add new team");
	},

	/**
	 * Edit the selected unit
	 */
	onEditUnit: function(unit) {
		var me = this;
		Ext.Ajax.request( {
			method: 'GET',
			url: '../admin/getAllocatedSpace',
			params: {
				'name': unit.data.name
			},
			success: function(result, response) {
				if (result.responseText && result.responseText.length > 0) {
					var allocatedQuota = Ext.JSON.decode(result.responseText);
					var k = allocatedQuota.allocatedQuota;

					var comp = Ext.create('Manage.view.admin.EditUnit', {
							allocatedStorage: k,
							unit: unit.data
					});
					me.addToAdminSpace(comp, "Edit Team");	
				}
			}
		});
	},
	
	/**
     * Remove unit
     */
    onRemoveUnit : function(unit) {
    	var k = false;
    	var _this = this;
    	
    	Ext.Ajax.request( {
			method : 'GET',
			url : '../admin/isSafeToDelete',
			params : {
				'unitName' : unit.data.name
			},
			success : function(result, response) {
				if (result.responseText && result.responseText.length > 0) {
					var unitData = Ext.JSON.decode(result.responseText);
					k = unitData.safeToDelete;
					if(k)
					{
						Ext.Ajax.request( {
							method : 'GET',
							url : '../admin/removeUnit',
							params : {
								'unitName' : unit.data.name
							},
							success : function(result, response) {
								Ext.Msg.alert("Message", "Successfully removed team: " + unit.data.name);
								_this.onRefreshList();
							}
						});
					}
					else
					{
						showErrorMessage(null, "Cannot remove the unit. Project space usage violated.");
					}
				}
			}
		});
    },

	/**
	 * set the adminSpace on selectionchange
	 * @param {Ext.selection.Model} model the selection model
	 * @param {Ex.data.Model[]} selection the selected records
	 */
	onUnitSelectionChange: function(model, selection) {
		var space = this.getAdminSpace();

		if(space.hidden) {
			return;
		}
	
		var unit = this.getUnitList();
		if(space.down('editUnit')) {
			unit.fireEvent('onEditUnit', selection[0]);
		} else if(space.down('changePassword')) {
			unit.fireEvent('onChangePassword', selection[0]);
		}
	},

	/**
	 * set the adminSpace on selectionchange
	 * @param {Ext.selection.Model} model the selection model
	 * @param {Ex.data.Model[]} selection the selected records
	 */
	onAssociationSelectionChange: function(model, selection) {
		var space = this.getAdminSpace();

		if(space.hidden) {
			return;
		}

		if(space.down('editAssociation')) {
			this.getAssociationList().fireEvent('onEditAssociation', selection[0]);
		}
	},

	/**
	* Add new team-project associations
	*/
	onAddAssociation: function(projectName, userLogin) {
		var availableStore = Ext.StoreManager.get('admin.AvailableUnits');
		var me = this;
		availableStore.load({
			params : {
				projectName : projectName 
			},
			callback : function(records, operation, success) {
				if (!success) {
					showErrorMessage(null, "Failed");
				} else {
					var comp = Ext.create ('Manage.view.admin.AddTeamProjectAssociations', {
								projectName : projectName 
					});
					me.addToAdminSpace(comp, "Add Association");
				}
			} 
		});
	},

	/**
	* Edit association UI
	*/
	onEditAssociation: function(association) {
		var maxAvailable = 0;
		var usage = 0;

		me = this;
		Ext.Ajax.request( {
			method: 'GET',
			url: '../admin/getProjectSpaceUsage',
			params: {
				'projectName': association.data.projectName
			},
			success: function(result, response) {
				if (result.responseText && result.responseText.length > 0) {
					var projectSpace = Ext.JSON.decode(result.responseText);
					usage = projectSpace.usage;

					Ext.Ajax.request( {
						method: 'GET',
						url: '../admin/getUnitMaxAvailable',
						params: {
							'unitName': association.data.unitName
						},
						success: function(result, response) {
							if (result.responseText && result.responseText.length > 0) {
								var unitSpace = Ext.JSON.decode(result.responseText);
								maxAvailable = unitSpace.available;

								var comp = Ext.create('Manage.view.admin.EditAssociation', {
										projectName: association.data.projectName,
										unitName: association.data.unitName,
										spaceContributed: association.data.storageContribution,
										projectUsage: usage,
										availableStorage: maxAvailable
								});
								me.addToAdminSpace(comp, "Edit Association");
							}
						}
					});
				}
			}
		});
	},

	/**
	* Refresh the unit listing
	*/
	onRefreshList: function() {
		var unitList = this.getUnitList();
		unitList.store.load();
	},
	
	/**
	* Refresh the association listing
	*/
	onRefreshAssociationList: function(projectName, unitName) {
		var unitList = this.getAssociationList();
		unitList.store.load({
			params: {
				projectName: projectName,
				unitName: unitName
			}
		});
	},

	/**
	* Remove association
	*/
	onRemoveAssociation: function(association) {
		var view = this.getAssociationList();

		var k = 0;
		var me = this;
		Ext.Ajax.request( {
			method: 'GET',
			url: '../admin/getProjectUnusedSpace',
			params: {
				'projectName': association.data.projectName
			},
			success: function(result, response) {
				if (result.responseText && result.responseText.length > 0) {
					var projectSpace = Ext.JSON.decode(result.responseText);
					k = projectSpace.unused;
				}

				if(association.data.storageContribution <= k){
					Ext.Ajax.request( {
					method: 'GET',
					url: '../admin/removeAssociation',
					params: {
						'projectName': association.data.projectName,
						'unitName': association.data.unitName
					},
					success: function(result, response) {
						Ext.Msg.alert("Message", "Successfully removed team " + association.data.unitName + " from project " + association.data.projectName);
						me.onRefreshAssociationList(association.data.projectName, association.data.unitName);
					}
					});
				} else {
					showErrorMessage(null, "Cannot remove the association. Project space usage violated.");
				}
			}
		});
	},

	/**
	* Search units
	*/
	searchUnit: function(){
		var associationList = this.getAssociationList();
		var associationPanel=this.getAssociation();
		Ext.Ajax.request({
			method: 'GET',
			url: '../admin/getAssociations',
			params: {
				q: associationList.down('textfield[name=searchQuery]').getValue(),
				projectName: associationPanel.down('combobox[name=project]').getValue()
			},
			success: function(result, request) {
				var units = Ext.decode(result.responseText);
				associationList.getStore().loadData(units);

			},
			failure: function(result, request) {
				showErrorMessage(result.responseText, "Failed to load team list"); 
			}
		});
	}

});
