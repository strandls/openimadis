/**
 * User manipulation related actions
 */
Ext.require(['Admin.view.AddUnit', 'Admin.view.EditUnit', 'Admin.view.AddAssociations', 'Admin.view.EditAssociation']);

Ext.define ('Admin.controller.UnitController', {
    extend : 'Ext.app.Controller',

    refs : [{
        ref : 'associationList',
        selector : 'associationList'
    }, {
        ref : 'association',
        selector : 'association'
    }, {
        ref : 'unitList',
        selector : 'unitList'
    }, {
        ref : 'projectList',
        selector : 'projectList'
    }],

    init : function() {
        this.control({
            'unitList' : {
                onAddUnit : this.onAddUnit,
                onEditUnit : this.onEditUnit,
                onRemoveClick : this.onRemoveUnit
            }, 
            'projectList' : {
            	onAddUnit : this.onAddAssociation
            },
            'addUnit' : {
                refreshList : this.onRefreshList
            },
            'editUnit' : {
                refreshList : this.onRefreshList
            }, 
            'addAssociations' : {
            	manageAssociations : this.onManageAssociations
            },
            'associationList' : {
            	onAddAssociation : this.onAddAssociation,
            	onEditAssociation : this.onEditAssociation,
            	onRemoveAssociation : this.onRemoveAssociation,
            	searchUnit:this.searchUnit
            },
            'editAssociation' : {
            	onRefreshAssociationList : this.onRefreshAssociationList
            }
        });
    },
    
    /**
     * Associate the units with the project
     */
    onManageAssociations : function(viewWindow, projectName, names, quota) {
        var view = this.getAssociationList();
        var _this = this;
        Ext.Ajax.request({
            method : 'POST',
            url : '../admin/associateUnits',
            params : {
                projectName : projectName,
                unitNames : Ext.encode(names),
                quota : quota
            },
            success : function(result, response) {
                view.store.load({
                    params : {
                        projectName : projectName,
                        unitName : ""
                    }
                });
                Ext.Msg.alert("Message", "Successfully added " + names.length + " teams to project " + projectName);
                _this.onRefreshProjectList();
                viewWindow.close();
                
            }
        });
    },
    
    /**
     * Add new members ui
     */
    onAddAssociation: function(projectName) {
    	console.log(projectName);
        var selectedStore = Ext.StoreManager.get('Admin.store.SelectedUnitsStore');
        selectedStore.removeAll();

        var availableStore = Ext.StoreManager.get('Admin.store.AvailableUnitsStore');
        availableStore.load({
            params : {
                projectName : projectName
            },
            callback : function(records, operation, success) {
                if (!success) {
                    showErrorMessage(null, "Failed");
                } else {
                    Ext.create ('Ext.window.Window', {
                        height : 400,
                        title : 'Add New Members',
                        layout : 'fit',
                        width : 500,
                        items : [{
                            xtype : 'addAssociations',
                            projectName : projectName
                        }]
                    }).show();
                }
            } 
        });
    },
    
    /**
     * Remove association
     */
    onRemoveAssociation : function(association) {
        var view = this.getAssociationList();
        console.log(association);
        
        var _this = this;
        
        var k = 0;
    	
    	Ext.Ajax.request( {
			method : 'GET',
			url : '../admin/getProjectUnusedSpace',
			params : {
				'projectName' : association.data.projectName
			},
			success : function(result, response) {
				if (result.responseText && result.responseText.length > 0) {
					var projectSpace = Ext.JSON.decode(result.responseText);
					k = projectSpace.unused;
				}
				
				if(association.data.storageContribution <= k)
		    	{
		    		Ext.Ajax.request( {
		    			method : 'GET',
		    			url : '../admin/removeAssociation',
		    			params : {
		    				'projectName' : association.data.projectName,
		    				'unitName' : association.data.unitName
		    			},
		    			success : function(result, response) {
		    				Ext.Msg.alert("Message", "Successfully removed team " + association.data.unitName + " from project " + association.data.projectName);
		    				_this.onRefreshAssociationList(association.data.projectName, association.data.unitName);
		    			}
		    		});
		    	}
		    	else
		    	{
		    		showErrorMessage(null, "Cannot remove the association. Project space usage violated.");
		    	}
			}
		});
    },
    
    /**
     * Edit association UI
     */
    onEditAssociation: function(association) {
    	console.log("adding units to project");
    	
    	var maxAvailable = 0;
    	var usage = 0;
    	
    	Ext.Ajax.request( {
			method : 'GET',
			url : '../admin/getProjectSpaceUsage',
			params : {
				'projectName' : association.data.projectName
			},
			success : function(result, response) {
				if (result.responseText && result.responseText.length > 0) {
					var projectSpace = Ext.JSON.decode(result.responseText);
					usage = projectSpace.usage;
					
			    	Ext.Ajax.request( {
						method : 'GET',
						url : '../admin/getUnitMaxAvailable',
						params : {
							'unitName' : association.data.unitName
						},
						success : function(result, response) {
							if (result.responseText && result.responseText.length > 0) {
								var unitSpace = Ext.JSON.decode(result.responseText);
								maxAvailable = unitSpace.available;
								
								Ext.create ('Ext.window.Window', {
						            height : 400,
						            title : 'Edit Team',
						            layout : 'fit',
						            width : 500,
						            items : [{
						                xtype : 'editAssociation',
						                projectName : association.data.projectName,
						                unitName : association.data.unitName,
						                spaceContributed : association.data.storageContribution,
						                projectUsage : usage,
						                availableStorage : maxAvailable
						            }]
						        }).show();
							}
						}
					});
				}
			}
		});
    },

    /**
     * Add new unit ui
     */
    onAddUnit : function() {
    	console.log("on add unit");
    	
        Ext.create ('Ext.window.Window', {
            height : 250,
            title : 'Add New Team',
            width : 400,
            items : [{
                xtype : 'addUnit'   
            }]
        }).show();
    },

    /**
     * Edit unit ui
     */
    onEditUnit : function(unit) {
    	var k = 0;
    	
    	Ext.Ajax.request( {
			method : 'GET',
			url : '../admin/getAllocatedSpace',
			params : {
				'name' : unit.data.name
			},
			success : function(result, response) {
				if (result.responseText && result.responseText.length > 0) {
					var allocatedQuota = Ext.JSON.decode(result.responseText);
					console.log(allocatedQuota);
					k = allocatedQuota.allocatedQuota;
					
			        Ext.create ('Ext.window.Window', {
			            height : 250,
			            title : 'Edit Team',
			            width : 420,
			            items : [{
			                xtype : 'editUnit',
			                allocatedStorage: k,
			                unit : unit.data
			            }]
			        }).show();
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
								_this.onRefreshAssociationList("", unit.data.name);
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
     * Refresh the unit listing
     */
    onRefreshList : function() {
        var unitList = this.getUnitList();
        unitList.store.load();
    },
    
    /**
     * Refresh the project listing
     */
    onRefreshProjectList : function() {
        var projectList = this.getProjectList();
        projectList.store.load();
    },
    
    /**
     * Refresh the user listing
     */
    onRefreshAssociationList : function(projectName, unitName) {
        var unitList = this.getAssociationList();
        unitList.store.load({
            params : {
                projectName : projectName,
                unitName : unitName
            }
        });
    },
    
    /**
     * Search units
     */
    searchUnit : function(){
    	var associationList = this.getAssociationList();
    	var associationPanel=this.getAssociation();
    	Ext.Ajax.request({
            method : 'GET',
            url : '../admin/getAssociations',
            params : {
    			q : associationList.down('textfield[name=searchQuery]').getValue(),
    			projectName: associationPanel.down('combobox[name=project]').getValue()
            },
            success : function(result, request) {
                var units = Ext.decode(result.responseText);
                associationList.getStore().loadData(units);
                
            },
            failure : function(result, request) {
                showErrorMessage(result.responseText, "Failed to load team list"); 
            }
        });
    }
});
